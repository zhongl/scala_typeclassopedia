package applicative

import educational.Applicative
import org.scalatest.{FunSpec, MustMatchers}

class ApplicativeExamplesSpec
  extends FunSpec
  with MustMatchers {

  describe("derived methods") {
    it("option instance") {
      val optionApplicative: Applicative[Option] = new Applicative[Option] {
        def pure[A](a: A): Option[A] = Some(a)
        def ap[A, B](ff: Option[A => B])(fa: Option[A]): Option[B] = (ff,fa) match {
          case (Some(fab), Some(faa)) => Some(fab(faa))
          case (_,_) => None
        }
      }
    }

    it("ValidatedNel instance") {
      case class Nel[A](head: A, tail: Nel[A])

      sealed trait ValidatedNel[A]
      case class SuccessVN[A](value: A) extends ValidatedNel[A]
      case class Errors[A](errors: Nel[Throwable]) extends ValidatedNel[A]

      val optionApplicative: Applicative[Option] = new Applicative[Option] {
        def pure[A](a: A): Option[A] = Some(a)
        def ap[A, B](ff: Option[A => B])(fa: Option[A]): Option[B] = (ff,fa) match {
          case (Some(fab), Some(faa)) => Some(fab(faa))
          case (_,_) => None
        }
      }
    }

    it("examples for Option") {
      import cats.Applicative
      import cats.implicits.catsStdInstancesForOption

      val add3: Int => Int = a => a + 3

      Applicative[Option].pure(42) mustBe Some(42)
      Applicative[Option].ap(Some(add3))(Some(39)) mustBe Some(42)
    }

    it("examples for List") {
      import cats.Applicative
      import cats.implicits.catsStdInstancesForList


      val list1 = List(1, 2)
      val listFns: List[Int => Int] = List(a => a + 3, a => a * a)

      Applicative[List].ap(listFns)(list1) mustBe List(4,5,1,4)
      Applicative[List].pure("foo") mustBe List("foo")
    }

    it("map2 on composed Applicative") {
      import cats.Applicative
      import cats.implicits.catsStdInstancesForList
      import cats.implicits.catsStdInstancesForOption

      val listOpt = Applicative[List] compose Applicative[Option]

      val list1 = List(Some(2), None)
      val list2 = List(Some(10), Some(2))
      listOpt.map2(list1, list2)(_ + _) mustBe List(Some(12), Some(4), None, None)
    }
  }
}

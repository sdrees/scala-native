package java.lang

import java.lang.reflect.{Field, Method}

import scalanative.native._
import scalanative.runtime.{Array => _, _}

// These two methods are generated at link-time by the toolchain
// using current closed-world knowledge of classes and traits in
// the current application.
@extern
object rtti {
  def __check_class_has_trait(classId: Int, traitId: Int): scala.Boolean =
    extern
  def __check_trait_has_trait(leftId: Int, rightId: Int): scala.Boolean =
    extern
}
import rtti._

final class _Class[A](val rawty: RawPtr) {
  private def ty: Ptr[Type] =
    fromRawPtr[Type](rawty)

  def cast(obj: Object): A =
    obj.asInstanceOf[A]

  def getComponentType(): _Class[_] = {
    if (rawty == typeof[BooleanArray]) classOf[scala.Boolean]
    else if (rawty == typeof[CharArray]) classOf[scala.Char]
    else if (rawty == typeof[ByteArray]) classOf[scala.Byte]
    else if (rawty == typeof[ShortArray]) classOf[scala.Short]
    else if (rawty == typeof[IntArray]) classOf[scala.Int]
    else if (rawty == typeof[LongArray]) classOf[scala.Long]
    else if (rawty == typeof[FloatArray]) classOf[scala.Float]
    else if (rawty == typeof[DoubleArray]) classOf[scala.Double]
    else classOf[java.lang.Object]
  }

  def getInterfaces(): Array[_Class[_]] =
    ???

  def getName(): String =
    ty.name

  def getSimpleName(): String =
    getName.split('.').last.split('$').last

  def getSuperclass(): Class[_ >: A] =
    ???

  @stub
  def getField(name: String): Field =
    ???

  def isArray(): scala.Boolean =
    (rawty == typeof[BooleanArray] ||
      rawty == typeof[CharArray] ||
      rawty == typeof[ByteArray] ||
      rawty == typeof[ShortArray] ||
      rawty == typeof[IntArray] ||
      rawty == typeof[LongArray] ||
      rawty == typeof[FloatArray] ||
      rawty == typeof[DoubleArray] ||
      rawty == typeof[ObjectArray])

  def isAssignableFrom(that: Class[_]): scala.Boolean =
    is(that.asInstanceOf[_Class[_]].ty, ty)

  def isInstance(obj: Object): scala.Boolean =
    is(obj.getClass.asInstanceOf[_Class[_]].ty, ty)

  private def is(left: Ptr[Type], right: Ptr[Type]): Boolean =
    // This replicates the logic of the compiler-generated instance check
    // that you would normally get if you do (obj: L).isInstanceOf[R],
    // where rtti for L and R are `left` and `right`.
    left.kind match {
      case CLASS_KIND =>
        right.kind match {
          case CLASS_KIND =>
            val rightCls  = right.asInstanceOf[Ptr[ClassType]]
            val rightFrom = rightCls.idRangeFrom
            val rightTo   = rightCls.idRangeTo
            val leftId    = left.id
            leftId >= rightFrom && leftId <= rightTo
          case TRAIT_KIND =>
            __check_class_has_trait(left.id, right.id)
          case STRUCT_KIND =>
            false
        }
      case TRAIT_KIND =>
        right.kind match {
          case CLASS_KIND =>
            false
          case TRAIT_KIND =>
            __check_trait_has_trait(left.id, right.id)
          case STRUCT_KIND =>
            false
        }
      case STRUCT_KIND =>
        right.kind match {
          case CLASS_KIND =>
            false
          case TRAIT_KIND =>
            false
          case STRUCT_KIND =>
            left.id == right.id
        }
    }

  def isInterface(): scala.Boolean =
    ty.kind == TRAIT_KIND

  def isPrimitive(): scala.Boolean =
    (rawty == typeof[PrimitiveBoolean] ||
      rawty == typeof[PrimitiveChar] ||
      rawty == typeof[PrimitiveByte] ||
      rawty == typeof[PrimitiveShort] ||
      rawty == typeof[PrimitiveInt] ||
      rawty == typeof[PrimitiveLong] ||
      rawty == typeof[PrimitiveFloat] ||
      rawty == typeof[PrimitiveDouble] ||
      rawty == typeof[PrimitiveUnit])

  override def equals(other: Any): scala.Boolean =
    other match {
      case other: _Class[_] =>
        rawty == other.rawty
      case _ =>
        false
    }

  override def hashCode: Int =
    Intrinsics.castRawPtrToLong(rawty).##

  override def toString = {
    val name = getName
    val prefix = ty.kind match {
      case CLASS_KIND  => "class "
      case TRAIT_KIND  => "interface "
      case STRUCT_KIND => "struct "
    }
    prefix + name
  }

  @stub
  def getClassLoader(): java.lang.ClassLoader = ???
  @stub
  def getConstructor(args: Array[Object]): java.lang.reflect.Constructor[_] =
    ???
  @stub
  def getConstructors(): Array[Object]  = ???
  def getDeclaredFields(): Array[Field] = ???
  def getMethod(name: java.lang.String,
                args: Array[Class[_]]): java.lang.reflect.Method       = ???
  def getMethods(): Array[Method]                                      = ???
  def getResourceAsStream(name: java.lang.String): java.io.InputStream = ???
}

object _Class {
  private[java] implicit def _class2class[A](cls: _Class[A]): Class[A] =
    cls.asInstanceOf[Class[A]]
  private[java] implicit def class2_class[A](cls: Class[A]): _Class[A] =
    cls.asInstanceOf[_Class[A]]

  @stub
  def forName(name: String): Class[_] = ???
  @stub
  def forName(name: String,
              init: scala.Boolean,
              loader: ClassLoader): Class[_] = ???
}

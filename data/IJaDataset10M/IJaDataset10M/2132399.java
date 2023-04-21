package java.lang.annotation;

import java.lang.reflect.Method;

/**
 * Thrown when accessing an element within an annotation for
 * which the type has changed, since compilation or serialization
 * took place.  The mismatch between the compiled or serialized
 * type and the current type causes this exception to be thrown.
 * 
 * @author Tom Tromey (tromey@redhat.com)
 * @author Andrew John Hughes (gnu_andrew@member.fsf.org)
 * @since 1.5
 */
public class AnnotationTypeMismatchException extends RuntimeException {

    /**
   * For compatability with Sun's JDK
   */
    private static final long serialVersionUID = 8125925355765570191L;

    /**
   * Constructs an <code>AnnotationTypeMismatchException</code>
   * which is due to a mismatched type in the annotation
   * element, <code>m</code>. The erroneous type used for the
   * data in <code>m</code> is represented by the string,
   * <code>type</code>.  This string is of an undefined format,
   * and may contain the value as well as the type.
   *
   * @param m the element from the annotation.
   * @param type the name of the erroneous type found in <code>m</code>.
   */
    public AnnotationTypeMismatchException(Method m, String type) {
        this.element = m;
        this.foundType = type;
    }

    /**
   * Returns the element from the annotation, for which a
   * mismatch occurred.
   *
   * @return the element with the mismatched type.
   */
    public Method element() {
        return element;
    }

    /**
   * Returns the erroneous type used by the element,
   * represented as a <code>String</code>.  The format
   * of this <code>String</code> is not explicitly specified,
   * and may contain the value as well as the type.
   *
   * @return the type found in the element.
   */
    public String foundType() {
        return foundType;
    }

    /**
   * The element from the annotation.
   *
   * @serial the element with the mismatched type.
   */
    private Method element;

    /**
   * The erroneous type used by the element.
   *
   * @serial the type found in the element.
   */
    private String foundType;
}

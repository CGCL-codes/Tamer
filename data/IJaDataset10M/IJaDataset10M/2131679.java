package moio.util;

/**
 * Number is a generic superclass of all the numeric classes, including
 * the wrapper classes {@link Byte}, {@link Short}, {@link Integer},
 * {@link Long}, {@link Float}, and {@link Double}.  Also worth mentioning
 * are the classes in {@link java.math}.
 *
 * It provides ways to convert numeric objects to any primitive.
 *
 * @author Paul Fisher
 * @author John Keiser
 * @author Warren Levy
 * @author Eric Blake (ebb9@email.byu.edu)
 * @since 1.0
 * @status updated to 1.4
 */
public abstract class Number implements Serializable {

    /**
   * Compatible with JDK 1.1+.
   */
    private static final long serialVersionUID = -8742448824652078965L;

    /**
   * Table for calculating digits, used in Character, Long, and Integer.
   */
    static final char[] digits = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z' };

    /**
   * The basic constructor (often called implicitly).
   */
    public Number() {
    }

    /**
   * Return the value of this <code>Number</code> as an <code>int</code>.
   *
   * @return the int value
   */
    public abstract int intValue();

    /**
   * Return the value of this <code>Number</code> as a <code>long</code>.
   *
   * @return the long value
   */
    public abstract long longValue();

    /**
   * Return the value of this <code>Number</code> as a <code>float</code>.
   *
   * @return the float value
   */
    public abstract float floatValue();

    /**
   * Return the value of this <code>Number</code> as a <code>float</code>.
   *
   * @return the double value
   */
    public abstract double doubleValue();

    /**
   * Return the value of this <code>Number</code> as a <code>byte</code>.
   *
   * @return the byte value
   * @since 1.1
   */
    public byte byteValue() {
        return (byte) intValue();
    }

    /**
   * Return the value of this <code>Number</code> as a <code>short</code>.
   *
   * @return the short value
   * @since 1.1
   */
    public short shortValue() {
        return (short) intValue();
    }
}

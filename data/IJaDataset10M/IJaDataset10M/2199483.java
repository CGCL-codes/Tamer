package rpi.goldsd.container;

/**
 * The <tt>Real</tt> class provides a wrapper-class for the primitive data
 * type <tt>double</tt>.  The actual <tt>double</tt> value (the <tt>double</tt>
 * instance variable) is a public instance variable.
 *
 * @see Comparable
 * @see Hashable
 * @version 1.0, 4/16/98
 * @author David Goldschmidt
 */
public class Real implements Hashable {

    /** The smallest value of type <tt>Real</tt>. */
    public static final double MIN_VALUE = Double.MIN_VALUE;

    /** The largest value of type <tt>Real</tt>. */
    public static final double MAX_VALUE = Double.MAX_VALUE;

    /**
   * Initializes the underlying <tt>double</tt> primitive data type.
   * @param v the initial <tt>double</tt> value.
   */
    public Real(double v) {
        value = v;
    }

    /** Constructs a <tt>Real</tt> object with a default value of 0.0. */
    public Real() {
        value = 0.0;
    }

    /**
   * Returns <tt>true</tt> if this <tt>Real</tt> object is equal to the
   * given <tt>Comparable</tt> argument <tt>C</tt>, which must be of type
   * <tt>Real</tt>.
   * @param C the right-hand side of the comparison.
   * @return <tt>true</tt> if this <tt>Real</tt> object is equal to the given
   *         <tt>Real</tt> object <tt>C</tt>; <tt>false</tt> otherwise.
   * @exception IllegalArgumentException if the <tt>Comparable</tt> argument
   *                                     is not of type <tt>Real</tt>.
   */
    public boolean isEqualTo(Comparable C) throws IllegalArgumentException {
        if (C instanceof Real) return (value == ((Real) C).value); else throw new IllegalArgumentException("Must be a Real type.");
    }

    /**
   * Returns <tt>true</tt> if this <tt>Real</tt> object is less than the
   * given <tt>Comparable</tt> argument <tt>C</tt>, which must be of type
   * <tt>Real</tt>.
   * @param C the right-hand side of the comparison.
   * @return <tt>true</tt> if this <tt>Real</tt> object is less than the given
   *         <tt>Real</tt> object <tt>C</tt>; <tt>false</tt> otherwise.
   * @exception IllegalArgumentException if the <tt>Comparable</tt> argument
   *                                     is not of type <tt>Real</tt>.
   */
    public boolean isLessThan(Comparable C) throws IllegalArgumentException {
        if (C instanceof Real) return (value < ((Real) C).value); else throw new IllegalArgumentException("Must be a Real type.");
    }

    /**
   * Returns the hash value of this <tt>Real</tt> object, which is simply
   * the underlying <tt>double</tt> value, truncated.  If a more complex
   * hashing function is required, this method may be overridden.
   * @return the hash value of this <tt>Real</tt> object.
   */
    public int hash() {
        return (int) value;
    }

    /**
   * Returns the hash value of this <tt>Real</tt> object, based on the
   * given hashtable size.
   * @param tableSize the size of the hashtable making use of
   *                  this <tt>Real</tt> object.
   */
    public int hash(int tableSize) {
        return (Math.abs(hash()) % tableSize);
    }

    /**
   * Displays this <tt>Real</tt> object in the form of a <tt>String</tt>
   * object.  This method simply returns a <tt>String</tt> representation
   * of the underlying <tt>double</tt> value.
   * @return a <tt>String</tt> representing this <tt>Real</tt> object.
   */
    public String toString() {
        return ("" + value);
    }

    /** The underlying primitive data type. */
    public double value;
}

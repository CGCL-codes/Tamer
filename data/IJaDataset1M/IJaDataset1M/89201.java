package org.jmlspecs.unfinished;

import java.math.BigInteger;

public class JMLFiniteInteger extends JMLInfiniteIntegerClass {

    protected final BigInteger val;

    /** The number zero (0).
     */
    public static final JMLFiniteInteger ZERO = new JMLFiniteInteger();

    /** The number one (1).
     */
    public static final JMLFiniteInteger ONE = new JMLFiniteInteger(BigInteger.ONE);

    public JMLFiniteInteger() {
        val = BigInteger.ZERO;
    }

    public JMLFiniteInteger(BigInteger n) throws IllegalArgumentException {
        if (n != null) {
            val = n;
        } else {
            throw new IllegalArgumentException();
        }
    }

    /** Initialize this finite integer from the given long.
     */
    public JMLFiniteInteger(long n) {
        val = BigInteger.valueOf(n);
    }

    /** Return the sign of this integer.
     */
    public int signum() {
        return val.signum();
    }

    /** Return true.
     */
    public boolean isFinite() {
        return true;
    }

    /** Return the value of this integer.
     */
    public BigInteger finiteValue() {
        return val;
    }

    /** Compare this to the given integer, returning a comparison code.
     */
    public int compareTo(JMLInfiniteInteger n) {
        if (n instanceof JMLFiniteInteger) {
            return val.compareTo(((JMLFiniteInteger) n).val);
        } else {
            return -1 * n.signum();
        }
    }

    /** Compare this to o, returning a comparison code.
     *  @param o the object this is compared to.
     *  @exception ClassCastException when o is not
     *             a JMLInfiniteInteger or a BigInteger.
     */
    public int compareTo(Object o) throws NullPointerException, ClassCastException {
        if (o == null) {
            throw new NullPointerException();
        } else if (o instanceof BigInteger) {
            return val.compareTo((BigInteger) o);
        } else if (o instanceof JMLFiniteInteger) {
            return val.compareTo(((JMLFiniteInteger) o).val);
        } else if (o instanceof JMLPositiveInfinity) {
            return -1;
        } else if (o instanceof JMLNegativeInfinity) {
            return +1;
        } else {
            throw new ClassCastException();
        }
    }

    /** Return a hash code for this object.
     */
    public int hashCode() {
        return val.hashCode();
    }

    /** Return the negation of this integer.
     */
    public JMLInfiniteInteger negate() {
        BigInteger n = val.negate();
        return new JMLFiniteInteger(n);
    }

    /** Return the sum of this integer and the argument.
     */
    public JMLInfiniteInteger add(JMLInfiniteInteger n) {
        if (n instanceof JMLFiniteInteger) {
            BigInteger s = val.add(((JMLFiniteInteger) n).val);
            return new JMLFiniteInteger(s);
        } else {
            return n;
        }
    }

    /** Return the difference between this integer and the argument.
     */
    public JMLInfiniteInteger subtract(JMLInfiniteInteger n) {
        if (n instanceof JMLFiniteInteger) {
            BigInteger s = val.subtract(((JMLFiniteInteger) n).val);
            return new JMLFiniteInteger(s);
        } else {
            return n.negate();
        }
    }

    /** Return the product of this integer and the argument.
     */
    public JMLInfiniteInteger multiply(JMLInfiniteInteger n) {
        if (n instanceof JMLFiniteInteger) {
            BigInteger s = val.multiply(((JMLFiniteInteger) n).val);
            return new JMLFiniteInteger(s);
        } else if (val.equals(BigInteger.ZERO)) {
            return ZERO;
        } else if (val.signum() == +1) {
            return n;
        } else {
            JMLInfiniteInteger ret = n.negate();
            return ret;
        }
    }

    /** Return the quotient of this integer divided by the argument.
     */
    public JMLInfiniteInteger divide(JMLInfiniteInteger n) {
        if (n instanceof JMLFiniteInteger) {
            BigInteger s = val.divide(((JMLFiniteInteger) n).val);
            return new JMLFiniteInteger(s);
        } else {
            return ZERO;
        }
    }

    /** Return the remainder of this integer divided by the argument.
     */
    public JMLInfiniteInteger remainder(JMLInfiniteInteger n) {
        if (n instanceof JMLFiniteInteger) {
            BigInteger s = val.remainder(((JMLFiniteInteger) n).val);
            return new JMLFiniteInteger(s);
        } else if (n.signum() == +1) {
            return this;
        } else {
            return this.negate();
        }
    }

    /** Return this integer modulo the argument.
     */
    public JMLInfiniteInteger mod(JMLInfiniteInteger n) throws ArithmeticException {
        if (n instanceof JMLFiniteInteger) {
            BigInteger s = val.mod(((JMLFiniteInteger) n).val);
            return new JMLFiniteInteger(s);
        } else if (n.signum() == +1) {
            return n;
        } else {
            throw new ArithmeticException("negative divisor for mod");
        }
    }

    /** Return this integer raised to the argument's power.
     */
    public JMLInfiniteInteger pow(int n) {
        BigInteger s = val.pow(n);
        return new JMLFiniteInteger(s);
    }

    /** Return this integer approximated by a double.
     */
    public double doubleValue() {
        return val.doubleValue();
    }

    /** Return this integer approximated by a float.
     */
    public float floatValue() {
        return val.floatValue();
    }

    /** Return the decimal representation of this integer.
     */
    public String toString() {
        return val.toString();
    }

    /** Return the digits representing this integer in the given radix.
     */
    public String toString(int radix) {
        return val.toString(radix);
    }

    /** Converts this BigInteger to a long
     */
    public long longValue() {
        return val.longValue();
    }

    /** Converts this BigInteger to an integer
     */
    public int intValue() {
        return val.intValue();
    }
}

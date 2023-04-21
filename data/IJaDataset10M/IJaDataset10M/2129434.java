package org.josef.math;

import org.josef.util.CDebug;

/**
 * This class wraps a primitive float value.
 * <br>The main difference between this class and class {@link Float} is that
 * this class has support for arithmetic operations that protect against both
 * underflow and overflow.<br>
 * Design considerations: Class {@link Float} is final so this class extends
 * class {@link Number} instead. This class is immutable and contains private
 * constructors in combination with static factory methods as suggested by
 * Joshua Bloch (Effective Java, Second Edition, item 15).
 * @author Kees Schotanus
 * @version 2.0 $Revision: 678 $
 */
public final class CFloat extends Number implements Comparable<CFloat> {

    /**
     * Serial Version UID for this class.
     */
    private static final long serialVersionUID = -2908741486539033733L;

    /**
     * Standard message used by all arithmetic methods.
     */
    private static final String UNDER_OR_OVERFLOW_MESSAGE = "Arithmetic underflow or overflow caused by: %f %s %f.";

    /**
     * The wrapped float value.
     * @serial
     */
    private final float value;

    /**
     * Constructs this CFloat from the supplied primitive float value.
     * @param value Primitive float value.
     */
    private CFloat(final float value) {
        this.value = value;
    }

    /**
     * Returns the value of this CFloat as a byte.
     * @return The value of this CFloat as a byte.
     */
    @Override
    public byte byteValue() {
        return (byte) value;
    }

    /**
     * Returns the value of this CFloat as a short.
     * @return The value of this CFloat as a short.
     */
    @Override
    public short shortValue() {
        return (short) value;
    }

    /**
     * Returns the value of this CFloat as an int.
     * @return The value of this CFloat as an int.
     */
    @Override
    public int intValue() {
        return (int) value;
    }

    /**
     * Returns the value of this CFloat as a long.
     * @return The value of this CFloat as a long.
     */
    @Override
    public long longValue() {
        return (long) value;
    }

    /**
     * Returns the value of this CFloat as a float.
     * @return The value of this CFloat as a float.
     */
    @Override
    public float floatValue() {
        return value;
    }

    /**
     * Returns the value of this CFloat as a double.
     * @return The value of this CFloat as a double.
     */
    @Override
    public double doubleValue() {
        return value;
    }

    /**
     * Returns a CFloat object holding the value of the supplied value.
     * @param value The int value.
     * @return A CFloat object holding the value of the supplied value.
     * @throws IllegalArgumentException When the supplied value could not be
     *  converted to a float without losing precision.
     */
    public static CFloat valueOf(final int value) {
        if (Float.valueOf(value).intValue() != value) {
            throw new IllegalArgumentException(String.format("Value:%d couldn't be converted without losing precision!", value));
        }
        return new CFloat(value);
    }

    /**
     * Returns a CFloat object holding the value of the supplied value.
     * @param value The long value.
     * @return A CFloat object holding the value of the supplied value.
     * @throws IllegalArgumentException When the supplied value could not be
     *  converted to a float without losing precision.
     */
    public static CFloat valueOf(final long value) {
        if (Float.valueOf(value).longValue() != value) {
            throw new IllegalArgumentException(String.format("Value:%d couldn't be converted without losing precision!", value));
        }
        return new CFloat(value);
    }

    /**
     * Returns a CFloat object holding the value of the supplied value.
     * <br>Depending on the type of the supplied value this may involve
     * rounding.
     * @param value The Number that holds the value.
     * @return A CFloat object holding the value of the supplied value.
     *  <br>When the supplied value is Not A Number, 0 is returned.
     * @throws NullPointerException When the supplied value is null.
     */
    public static CFloat valueOf(final Number value) {
        CDebug.checkParameterNotNull(value, "value");
        return new CFloat(value.floatValue());
    }

    /**
     * Returns a CFloat object holding the parsed value of the supplied value.
     * @param value The String that is parsed.
     * @return A CFloat object holding the value of the supplied value, after
     *  parsing.
     * @throws NumberFormatException When the supplied value does not contain a
     *  parsable float (this includes a null or empty value).
     * @see Float#parseFloat(String)
     */
    public static CFloat valueOf(final String value) {
        return valueOf(Float.parseFloat(value));
    }

    /**
     * Determines whether this object and the supplied object are the same.
     * <br>The two objects are the same if the supplied object is not null and
     * the supplied object is a CFloat and the bit pattern of the stored value
     * in both objects is the same. For consequences of comparing the bit
     * pattern see {@link java.lang.Float#equals(Object)}.
     * @param object The CFloat object to compare with.
     * @return True if both objects are the same, false otherwise.
     */
    @Override
    public boolean equals(final Object object) {
        return object instanceof CFloat && Float.floatToIntBits(value) == Float.floatToIntBits(((CFloat) object).value);
    }

    /**
     * Determines whether the supplied left and right float are equal to
     * each other within the limit of the supplied delta.
     * <br>Both floats are considered equal when the absolute difference
     * between the two floats is less than the supplied delta value. Using a
     * delta is often necessary! The following expression for example does not
     * result in 0.0: 7 * 0.1 - 0.7<br>
     * Note: This method tries to mimic evaluating: left == right<br>
     * See the <a href="http://java.sun.com/docs/books/jls/second_edition/html/expressions.doc.html#5198">jls</a>
     * to read about how special values like NaN and infinity are handled.
     * @param left The float on the left of the mathematical equals sign.
     * @param right The float on the right of the mathematical equals sign.
     * @param delta The limit within the two floats are considered to be equal.
     * @return True when the left float equals the right float, false
     *  otherwise.
     */
    public static boolean equals(final float left, final float right, final float delta) {
        return left == right ? true : Math.abs(left - right) <= Math.abs(delta);
    }

    /**
     * Computes the hash code value for this object.
     * <br>The float is 'treated' as an int and this int value is returned.
     * @return The hash code value for this object.
     */
    @Override
    public int hashCode() {
        return Float.floatToIntBits(value);
    }

    /**
     * Compares two CFloat objects numerically.
     * @param object CFloat object to compare with this object.
     * @return a negative number when this value &lt; other value, 0 when both
     *  values are equal and a positive number when this value &gt; other value.
     * @throws NullPointerException When the supplied object is null.
     * @see Comparable
     * @see Float#compare(float, float)
     */
    public int compareTo(final CFloat object) {
        return Float.compare(value, object.value);
    }

    /**
     * Adds the supplied value to this value.
     * <br>This method allows for expressions like:
     * CFloat value = i.add(x).multiply(y) where i is a CFloat and x and y are
     * primitive floats.
     * @param value Value to add to the stored value.
     * @return Result of the addition in a new CFloat.
     *  <br>When this or the supplied value is NaN the result will be NaN.
     * @throws ArithmeticException When an arithmetic underflow or overflow
     *  occurs, possibly due to one of the values being infinite.
     * @see #add(float, float)
     */
    public CFloat add(final float value) {
        return new CFloat(add(this.value, value));
    }

    /**
     * Adds y to x.
     * @param x Operand one.
     * @param y Operand two.
     * @return Result of the addition.
     *  <br>When one of the supplied values is NaN the result will be NaN.
     * @throws ArithmeticException When an arithmetic underflow or overflow
     *  occurs, possibly due to one of the values being infinite.
     */
    public static float add(final float x, final float y) {
        final double result = x + y;
        if (result != (float) result) {
            throw new ArithmeticException(String.format(UNDER_OR_OVERFLOW_MESSAGE, x, "+", y));
        }
        return (float) result;
    }

    /**
     * Subtracts the supplied value from this value.
     * <br>This method allows for expressions like:
     * CFloat value = i.subtract(x).multiply(y) where i is a CFloat and x
     * and y are primitive floats.
     * @param value Value to subtract from the stored value.
     * @return Result of the subtraction in a new CFloat.
     *  <br>When this or the supplied value is NaN the result will be NaN.
     * @throws ArithmeticException When an arithmetic underflow or overflow
     *  occurs, possibly due to one of the values being infinite.
     * @see #subtract(float, float)
     */
    public CFloat subtract(final long value) {
        return new CFloat(subtract(this.value, value));
    }

    /**
     * Subtracts y from x.
     * @param x Operand one.
     * @param y operand two.
     * @return Result of the subtraction.
     *  <br>When one of the supplied values is NaN the result will be NaN.
     * @throws ArithmeticException When an arithmetic underflow or overflow
     *  occurs, possibly due to one of the values being infinite.
     */
    public static float subtract(final float x, final float y) {
        final double result = x - y;
        if (result != (float) result) {
            throw new ArithmeticException(String.format(UNDER_OR_OVERFLOW_MESSAGE, x, "+", y));
        }
        return (float) result;
    }

    /**
     * Multiplies this value by the supplied value.
     * <br>This method allows for expressions like:
     * CFloat value = i.multiply(x).subtract(y) where i is a CFloat and x
     * and y are primitive floats.
     * @param value Value to multiply by the stored value.
     * @return Result of the multiplication in a new CFloat.
     *  <br>When this or the supplied value is NaN the result will be NaN.
     * @throws ArithmeticException When an arithmetic underflow or overflow
     *  occurs, possibly due to one of the values being infinite.
     * @see #multiply(float, float)
     */
    public CFloat multiply(final float value) {
        return new CFloat(multiply(this.value, value));
    }

    /**
     * Multiplies x by y.
     * @param x Operand one.
     * @param y Operand two.
     * @return Result of the multiplication.
     *  <br>When one of the supplied values is NaN the result will be NaN.
     * @throws ArithmeticException When an arithmetic underflow or overflow
     *  occurs, possibly due to one of the values being infinite.
     */
    public static float multiply(final float x, final float y) {
        final double result = x * y;
        if (result != (float) result) {
            throw new ArithmeticException(String.format(UNDER_OR_OVERFLOW_MESSAGE, x, "+", y));
        }
        return (float) result;
    }

    /**
     * Divides this value by the supplied value.
     * <br>This method allows for expressions like:
     * CFloat value = i.divide(x).add(y) where i is a CFloat and x and y are
     * primitive floats.
     * @param value Value by which this value will be divided.
     * @return Result of the division in a new CFloat.
     *  <br>When this or the supplied value is NaN the result will be NaN.
     * @throws ArithmeticException When an arithmetic overflow occurs or when
     *  this value is infinite or when the supplied value is 0.
     * @see #divide(float, float)
     */
    public CFloat divide(final float value) {
        return new CFloat(divide(this.value, value));
    }

    /**
     * Divides x by y.
     * @param x Operand one.
     * @param y Operand two.
     * @return Result of the division.
     *  <br>When one of the supplied values is NaN the result will be NaN.
     * @throws ArithmeticException When an arithmetic overflow occurs or when
     *  the supplied x value is infinite or when the supplied value is 0.
     */
    public static float divide(final float x, final float y) {
        if (y == 0) {
            throw new ArithmeticException("Divsion by zero!");
        }
        final double result = x / y;
        if (result != (float) result) {
            throw new ArithmeticException(String.format(UNDER_OR_OVERFLOW_MESSAGE, x, "*", y));
        }
        return (float) result;
    }

    /**
     * Converts the stored primitive float value to a String.
     * @return String representation of the stored value.
     */
    @Override
    public String toString() {
        return Float.toString(value);
    }
}

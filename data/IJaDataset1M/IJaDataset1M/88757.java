package org.josef.math;

import org.josef.util.CDebug;

/**
 * This class wraps a primitive byte value.
 * <br>The main difference between this class and class {@link Byte} is that
 * this class has support for arithmetic operations that protect against both
 * underflow and overflow.<br>
 * Design considerations: Class {@link Byte} is final so this class extends
 * class {@link Number} instead. This class is immutable and contains private
 * constructors in combination with static factory methods as suggested by
 * Joshua Bloch (Effective Java, Second Edition, item 15).
 * @author Kees Schotanus
 * @version 2.0 $Revision: 678 $
 */
public final class CByte extends Number implements Comparable<CByte> {

    /**
     * Serial Version UID for this class.
     */
    private static final long serialVersionUID = 8818787627015439136L;

    /**
     * Standard message used by all arithmetic methods.
     */
    private static final String UNDER_OR_OVERFLOW_MESSAGE = "Arithmetic underflow or overflow caused by: %d %s %d.";

    /**
     * The wrapped byte value.
     * @serial
     */
    private final byte value;

    /**
     * Constructs this CByte from the supplied primitive byte value.
     * @param value Primitive byte value.
     */
    private CByte(final byte value) {
        this.value = value;
    }

    /**
     * Returns the value of this CByte as a byte.
     * @return The value of this CByte as a byte.
     */
    @Override
    public byte byteValue() {
        return value;
    }

    /**
     * Returns the value of this CByte as a short.
     * @return The value of this CByte as a short.
     */
    @Override
    public short shortValue() {
        return value;
    }

    /**
     * Returns the value of this CByte as an int.
     * @return The value of this CByte as an int.
     */
    @Override
    public int intValue() {
        return value;
    }

    /**
     * Returns the value of this CByte as a long.
     * @return The value of this CByte as a long.
     */
    @Override
    public long longValue() {
        return value;
    }

    /**
     * Returns the value of this CByte as a float.
     * @return The value of this CByte as a float.
     */
    @Override
    public float floatValue() {
        return value;
    }

    /**
     * Returns the value of this CByte as a double.
     * @return The value of this CByte as a double.
     */
    @Override
    public double doubleValue() {
        return value;
    }

    /**
     * Returns a CByte object holding the value of the supplied value.
     * @param value The byte value.
     * @return A CByte object holding the value of the supplied value.
     */
    public static CByte valueOf(final byte value) {
        return new CByte(value);
    }

    /**
     * Returns a CByte object holding the value of the supplied value.
     * @param value The long value.
     * @return A CByte object holding the value of the supplied value.
     * @throws IllegalArgumentException When the supplied value is not in the
     *  range: [{@link Byte#MIN_VALUE} to {@link Byte#MAX_VALUE}].
     */
    public static CByte valueOf(final long value) {
        if (value != (byte) value) {
            throw new IllegalArgumentException(String.format("Value:%d not in range: [%d to %d]!", value, Byte.MIN_VALUE, Byte.MAX_VALUE));
        }
        return new CByte((byte) value);
    }

    /**
     * Returns a CByte object holding the value of the supplied value.
     * <br>Depending on the type of the supplied value this may involve
     * rounding.
     * @param value The Number that holds the value.
     * @return A CByte object holding the value of the supplied value.
     *  <br>When the supplied value is Not A Number, 0 is returned.
     * @throws NullPointerException When the supplied value is null.
     * @throws IllegalArgumentException When the supplied value is not in the
     *  range: [{@link Byte#MIN_VALUE} to {@link Byte#MAX_VALUE}].
     */
    public static CByte valueOf(final Number value) {
        CDebug.checkParameterNotNull(value, "value");
        final double doubleValue = value.doubleValue();
        final double doubleValueRounded = Math.rint(doubleValue);
        if (doubleValueRounded < Byte.MIN_VALUE || doubleValueRounded > Byte.MAX_VALUE) {
            if (doubleValue == doubleValueRounded) {
                throw new IllegalArgumentException(String.format("Value:%s not in range: [%d to %d]!", value.toString(), Byte.MIN_VALUE, Byte.MAX_VALUE));
            }
            throw new IllegalArgumentException(String.format("Value:%s, rounded to:%s not in range: [%d to %d]!", value.toString(), String.valueOf(doubleValueRounded), Byte.MIN_VALUE, Byte.MAX_VALUE));
        }
        return new CByte((byte) doubleValueRounded);
    }

    /**
     * Returns a CByte object holding the parsed value of the supplied value.
     * @param value The String that is parsed.
     * @return A CByte object holding the value of the supplied value, after
     *  parsing.
     * @throws IllegalArgumentException When the supplied value (after parsing)
     *  is not in the range: [{@link Byte#MIN_VALUE} to {@link Byte#MAX_VALUE}].
     * @throws NumberFormatException When the supplied value does not contain a
     *  parsable byte (this includes a null or empty value).
     * @see Byte#parseByte(String)
     */
    public static CByte valueOf(final String value) {
        return valueOf(Byte.parseByte(value));
    }

    /**
     * Determines whether this object and the supplied object are the same.
     * <br>The two objects are the same if the supplied object is not null and
     * the supplied object is a CByte and the stored value in both objects is
     * the same.
     * @param object The CByte object to compare with.
     * @return True if both objects are the same, false otherwise.
     */
    @Override
    public boolean equals(final Object object) {
        return object instanceof CByte && this.value == ((CByte) object).value;
    }

    /**
     * Computes the hash code value for this object.
     * @return Primitive integer value of the stored byte value.
     */
    @Override
    public int hashCode() {
        return value;
    }

    /**
     * Compares two CByte objects numerically.
     * @param object CByte object to compare with this object.
     * @return a negative number when this value &lt; other value, 0 when both
     *  values are equal and a positive number when this value &gt; other value.
     * @throws NullPointerException When the supplied object is null.
     * @see Comparable
     */
    public int compareTo(final CByte object) {
        return this.value - object.value;
    }

    /**
     * Adds the supplied value to this value.
     * <br>This method allows for expressions like:
     * CByte value = i.add(x).multiply(y) where i is a CByte and x and y are
     * primitive bytes.
     * @param value Value to add to the stored value.
     * @return Result of the addition in a new CByte.
     * @throws ArithmeticException When an arithmetic underflow or overflow
     *  occurs.
     * @see #add(byte, byte)
     */
    public CByte add(final byte value) {
        return new CByte(add(this.value, value));
    }

    /**
     * Adds y to x.
     * @param x Operand one.
     * @param y Operand two.
     * @return Result of the addition.
     * @throws ArithmeticException When an arithmetic underflow or overflow
     *  occurs.
     */
    public static byte add(final byte x, final byte y) {
        final int result = x + y;
        if (result != (byte) result) {
            throw new ArithmeticException(String.format(UNDER_OR_OVERFLOW_MESSAGE, x, "+", y));
        }
        return (byte) result;
    }

    /**
     * Subtracts the supplied value from this value.
     * <br>This method allows for expressions like:
     * CByte value = i.subtract(x).multiply(y) where i is a CByte and x and y
     * are primitive bytes.
     * @param value Value to subtract from the stored value.
     * @return Result of the subtraction in  anew CByte.
     * @throws ArithmeticException When an arithmetic underflow or overflow
     *  occurs.
     * @see #subtract(byte, byte)
     */
    public CByte subtract(final byte value) {
        return new CByte(subtract(this.value, value));
    }

    /**
     * Subtracts y from x.
     * @param x Operand one.
     * @param y operand two.
     * @return Result of the subtraction.
     * @throws ArithmeticException When an arithmetic underflow or overflow
     *  occurs.
     */
    public static byte subtract(final byte x, final byte y) {
        final int result = x - y;
        if (result != (byte) result) {
            throw new ArithmeticException(String.format(UNDER_OR_OVERFLOW_MESSAGE, x, "-", y));
        }
        return (byte) result;
    }

    /**
     * Multiplies this value by the supplied value.
     * <br>This method allows for expressions like:
     * CByte value = i.multiply(x).subtract(y) where i is a CByte and x and y
     * are primitive bytes.
     * @param value Value to multiply by the stored value.
     * @return Result of the multiplication in a new CByte.
     * @throws ArithmeticException When an arithmetic underflow or overflow
     *  occurs.
     * @see #multiply(byte, byte)
     */
    public CByte multiply(final byte value) {
        return new CByte(multiply(this.value, value));
    }

    /**
     * Multiplies x by y.
     * @param x Operand one.
     * @param y Operand two.
     * @return Result of the multiplication.
     * @throws ArithmeticException When an arithmetic underflow or overflow
     *  occurs.
     */
    public static byte multiply(final byte x, final byte y) {
        final int result = x * y;
        if (result != (byte) result) {
            throw new ArithmeticException(String.format(UNDER_OR_OVERFLOW_MESSAGE, x, "*", y));
        }
        return (byte) result;
    }

    /**
     * Divides this value by the supplied value.
     * <br>This method allows for expressions like:
     * CByte value = i.divide(x).add(y) where i is a CByte and x and y are
     * primitive bytes.
     * @param value Value by which this value will be divided.
     * @return Result of the division in a new CByte.
     * @throws ArithmeticException When an arithmetic overflow occurs or when
     *  the supplied value is 0.
     * @see #divide(byte, byte)
     */
    public CByte divide(final byte value) {
        return new CByte(divide(this.value, value));
    }

    /**
     * Divides x by y.
     * @param x Operand one.
     * @param y Operand two.
     * @return Result of the division.
     * @throws ArithmeticException When an arithmetic overflow occurs or when
     *  the supplied y value is 0.
     */
    public static byte divide(final byte x, final byte y) {
        final int result = x / y;
        if (result != (byte) result) {
            throw new ArithmeticException(String.format(UNDER_OR_OVERFLOW_MESSAGE, x, "/", y));
        }
        return (byte) result;
    }

    /**
     * Divides this value by the supplied value and floors the result.
     * <br>This method does not work like integer division in Java. In Java
     * -1 / 4 results in 0 but this method returns a value of -1. It works as if
     * the result of the standard Java division is floored and then returned.
     * @param value Value by which this value will be divided.
     * @return Result of the division in a new CByte.
     * @throws ArithmeticException When an arithmetic overflow occurs or when
     *  the supplied value is 0.
     * @see #divideAndFloor(byte, byte)
     */
    public CByte divideAndFloor(final byte value) {
        return new CByte(divideAndFloor(this.value, value));
    }

    /**
     * Divides x by y and floors the result.
     * <br>This method does not work like integer division in Java. In Java
     * -1 / 4 results in 0 but this method returns a value of -1. It works as if
     * the result of the standard Java division is floored and then returned.
     * @param x Operand one.
     * @param y Operand two.
     * @return Result of the division.
     * @throws ArithmeticException When an arithmetic overflow occurs or when
     *  the supplied y value is 0.
     */
    public static byte divideAndFloor(final byte x, final byte y) {
        int result = x / y;
        if (x * y < 0 && x % y != 0) {
            --result;
        }
        if (result != (byte) result) {
            throw new ArithmeticException(String.format(UNDER_OR_OVERFLOW_MESSAGE, x, "/", y));
        }
        return (byte) result;
    }

    /**
     * Converts the stored primitive byte value to a String.
     * @return String representation of the stored byte value.
     */
    @Override
    public String toString() {
        return Byte.toString(value);
    }

    /**
     * For test purposes only.
     * @param args Not used.
     */
    public static void main(final String[] args) {
        System.out.println(CByte.valueOf(new Double(Double.NaN)));
    }
}

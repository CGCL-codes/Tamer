package org.qtitools.qti.value;

import org.qtitools.qti.exception.QTIParseException;

/**
 * Implementation of <code>BaseType</code> integer value.
 * <p>
 * An integer value is A whole number in the range [-2147483648, 2147483647].
 * <p>
 * Example values: 1, +3, -4.
 * <p>
 * This class is not mutable and cannot contain NULL value.
 * <p>
 * <code>Cardinality</code> of this class is always single and <code>BaseType</code> is always integer.
 *
 * @see org.qtitools.qti.value.Cardinality
 * @see org.qtitools.qti.value.BaseType
 * 
 * @author Jiri Kajaba
 */
public class IntegerValue extends NumberValue {

    private static final long serialVersionUID = 1L;

    private int intValue;

    /**
	 * Constructs <code>IntegerValue</code> from given <code>int</code>.
	 *
	 * @param value <code>int</code>
	 */
    public IntegerValue(int value) {
        this.intValue = value;
    }

    /**
	 * Constructs <code>IntegerValue</code> from given <code>String</code> representation.
	 *
	 * @param value <code>String</code> representation of <code>IntegerValue</code>
	 * @throws QTIParseException if <code>String</code> representation of <code>IntegerValue</code> is not valid
	 */
    public IntegerValue(String value) throws QTIParseException {
        this.intValue = parseInteger(value);
    }

    /**
	 * Constructs <code>IntegerValue</code> from given <code>String</code> representation and radix.
	 *
	 * @param value <code>String</code> representation of <code>IntegerValue</code>
	 * @param radix Radix or base to use when interpreting value
	 * @throws QTIParseException if <code>String</code> representation of <code>IntegerValue</code> is not valid
	 */
    public IntegerValue(String value, Integer radix) {
        this.intValue = parseInteger(value, radix);
    }

    public BaseType getBaseType() {
        return BaseType.INTEGER;
    }

    @Override
    public int intValue() {
        return intValue;
    }

    @Override
    public double doubleValue() {
        return intValue;
    }

    @Override
    public boolean equals(Object object) {
        if (object == null || !getClass().equals(object.getClass())) return false;
        IntegerValue value = (IntegerValue) object;
        return intValue == value.intValue;
    }

    @Override
    public int hashCode() {
        return intValue();
    }

    @Override
    public String toString() {
        return Integer.toString(intValue);
    }

    /**
	 * Parses the <code>String</code> argument as A <code>int</code>.
	 *
	 * @param value <code>String</code> representation of <code>int</code>
	 * @return parsed <code>int</code>
	 * @throws QTIParseException if <code>String</code> representation of <code>int</code> is not valid
	 */
    public static int parseInteger(String value) throws QTIParseException {
        return parseInteger(value, 10);
    }

    /**
	 * Parses the <code>String</code> argument as A <code>int</code>.
	 *
	 * @param value <code>String</code> representation of <code>int</code>
	 * @param radix base to use in conversion
	 * @return parsed <code>int</code>
	 * @throws QTIParseException if <code>String</code> representation of <code>int</code> is not valid
	 */
    public static int parseInteger(String value, int radix) throws QTIParseException {
        if (value != null) value = value.trim();
        if (value == null || value.length() == 0) throw new QTIParseException("Invalid integer '" + value + "'. Length is not valid.");
        String originalValue = value;
        if (value.startsWith("+")) {
            value = value.substring(1);
            if (value.length() == 0 || !Character.isDigit(value.codePointAt(0))) throw new QTIParseException("Invalid integer '" + originalValue + "'.");
        }
        try {
            return Integer.parseInt(value, radix);
        } catch (NumberFormatException ex) {
            throw new QTIParseException("Invalid integer '" + originalValue + "'.", ex);
        }
    }
}

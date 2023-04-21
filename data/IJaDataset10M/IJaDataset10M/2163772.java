package org.qtitools.qti.value;

import org.qtitools.qti.exception.QTIParseException;

/**
 * Implementation of <code>BaseType</code> boolean value.
 * <p>
 * Legal <code>String</code> representations of boolean value are: true, 1, false, 0.
 * <p>
 * This class is not mutable and cannot contain NULL value.
 * <p>
 * <code>Cardinality</code> of this class is always single and <code>BaseType</code> is always boolean.
 *
 * @see org.qtitools.qti.value.Cardinality
 * @see org.qtitools.qti.value.BaseType
 * 
 * @author Jiri Kajaba
 */
public class BooleanValue extends SingleValue {

    private static final long serialVersionUID = 1L;

    private boolean booleanValue;

    /**
	 * Constructs <code>BooleanValue</code> from given <code>boolean</code>.
	 *
	 * @param value <code>boolean</code>
	 */
    public BooleanValue(boolean value) {
        this.booleanValue = value;
    }

    /**
	 * Constructs <code>BooleanValue</code> from given <code>String</code> representation.
	 *
	 * @param value <code>String</code> representation of <code>BooleanValue</code>
	 * @throws QTIParseException if <code>String</code> representation of <code>BooleanValue</code> is not valid
	 */
    public BooleanValue(String value) throws QTIParseException {
        this.booleanValue = parseBoolean(value);
    }

    public BaseType getBaseType() {
        return BaseType.BOOLEAN;
    }

    /**
	 * Returns the value of this <code>BooleanValue</code> as A <code>boolean</code>.
	 *
	 * @return the value of this <code>BooleanValue</code> as A <code>boolean</code>
	 */
    public boolean booleanValue() {
        return booleanValue;
    }

    @Override
    public boolean equals(Object object) {
        if (object == null || !getClass().equals(object.getClass())) return false;
        BooleanValue value = (BooleanValue) object;
        return booleanValue == value.booleanValue;
    }

    @Override
    public int hashCode() {
        return booleanValue ? 1 : 0;
    }

    @Override
    public String toString() {
        return Boolean.toString(booleanValue);
    }

    /**
	 * Parses the <code>String</code> argument as A <code>boolean</code>.
	 *
	 * @param value <code>String</code> representation of <code>boolean</code>
	 * @return parsed <code>boolean</code>
	 * @throws QTIParseException if <code>String</code> representation of <code>boolean</code> is not valid
	 */
    public static boolean parseBoolean(String value) throws QTIParseException {
        if (value != null) value = value.trim();
        if (value == null || value.length() == 0) throw new QTIParseException("Invalid boolean '" + value + "'. Length is not valid.");
        if (value.equals("true") || value.equals("1")) return true; else if (value.equals("false") || value.equals("0")) return false; else throw new QTIParseException("Invalid boolean '" + value + "'.");
    }
}

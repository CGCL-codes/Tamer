package org.qtitools.qti.value;

/**
 * Represents NULL value.
 * <p>
 * This class is not mutable and can contain only NULL value.
 * <p>
 * <code>Cardinality</code> of this class is always null and <code>BaseType</code> is always null.
 *
 * @see org.qtitools.qti.value.Cardinality
 * @see org.qtitools.qti.value.BaseType
 * 
 * @author Jiri Kajaba
 */
public class NullValue implements Value {

    private static final long serialVersionUID = 1L;

    public boolean isNull() {
        return true;
    }

    public Cardinality getCardinality() {
        return null;
    }

    public BaseType getBaseType() {
        return null;
    }

    /**
	 * Returns true for any value which is NULL; false otherwise.
	 *
	 * @param object the reference object with which to compare
	 * @return true for any value which is NULL; false otherwise
	 */
    @Override
    public boolean equals(Object object) {
        if (object == null || !(object instanceof Value)) return false;
        Value value = (Value) object;
        return value.isNull();
    }

    @Override
    public int hashCode() {
        return 0;
    }

    @Override
    public String toString() {
        return "NULL";
    }
}

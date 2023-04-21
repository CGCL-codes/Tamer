package org.qtitools.qti.value;

import java.io.Serializable;

/**
 * Represents any JQTI value object. Every JQTI value implementation must implement this interface.
 * <p>
 * This value can be single, multiple, ordered, record or NULL.
 *
 * @see org.qtitools.qti.value.Cardinality
 * @see org.qtitools.qti.value.BaseType
 * 
 * @author Jiri Kajaba
 */
public interface Value extends Serializable {

    /**
	 * Returns true if this value is NULL or false otherwise.
	 *
	 * @return true if this value is NULL or false otherwise
	 */
    public boolean isNull();

    /**
	 * Returns cardinality of this value.
	 * <p>
	 * If value is NULL returns null.
	 *
	 * @return cardinality of this value
	 */
    public Cardinality getCardinality();

    /**
	 * Returns baseType of this value.
	 * <p>
	 * If value is NULL or record cardinality returns null.
	 *
	 * @return baseType of this value
	 */
    public BaseType getBaseType();
}

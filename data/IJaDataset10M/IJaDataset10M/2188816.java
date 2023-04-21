package net.teqlo.db;

import net.teqlo.TeqloException;

public interface ContextFieldLookup {

    /**
     * 
     * Returns the fully qualified name of this context field
     * 
     * @return String fqn of this field
     * 
     */
    public String getFieldFqn();

    /**
     * 
     * Returns the label of this field, if defined.
     * 
     * @return String label of this field, or null if none.
     * 
     */
    public String getFieldLabel();

    /**
     * 
     * Returns the unique serial number of this context field
     * 
     * @return Short field serial number
     * 
     */
    public Short getFieldSerial();

    /**
     * 
     * Returns the Java class of this field
     * 
     * @return Class of this field
     * 
     */
    @SuppressWarnings("unchecked")
    public Class getJavaClass();

    /**
     * 
     * Asserts the validity in this field of the supplied value
     * 
     * @param value
     *            to be validated
     * @return validated value (type converted if required)
     * 
     * @throws TeqloException
     *             if the value is invalid for this field
     * 
     */
    public Object assertValidity(Object value) throws TeqloException;
}

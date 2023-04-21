package org.openliberty.arisidbeans;

import java.util.List;
import java.util.Locale;

/**
 * PropertyValue is used to hold property values to be modified in
 * Attribute Authority for a DigitalSubject.
 * A PropertyValue may be either String or Binary based on the datatype
 * of the Property in Attribute Authority.
 */
public class ModPropertyValue extends PropertyValue {

    private static final long serialVersionUID = 6752324454280929366L;

    public static final int ADD_VALUE = 1;

    public static final int DELETE_VALUE = 2;

    public static final int REPLACE_VALUE = 3;

    private int modOperation;

    /**
     * ModPropertyValue is initialized with attributeName and attributeValues
     * 
     * @param name
     *            Property Name
     * @param values
     *            List of Property values
     */
    public ModPropertyValue(String name, List values) {
        super(name, values);
        this.modOperation = REPLACE_VALUE;
    }

    /**
     * ModPropertyValue is initialized with attributeName and attributeValues
     * and modifyOperation
     * 
     * @param name
     *            Property Name
     * @param values
     *            List of Property values
     * @param modOp
     *            Valid Modify Operation - ADD_VALUE/DELETE_VALUE/REPLACE_VALUE
     * @throws InvalidValueException
     *            When the supplied modify operation is invalid
     */
    public ModPropertyValue(String name, List values, int modOp) throws InvalidValueException {
        super(name, values);
        setModOperation(modOp);
    }

    /**
     * ModPropertyValue is initialized with attributeName and attributeValue
     * 
     * @param name
     *            Property Name
     * @param value
     *            Property value
     */
    public ModPropertyValue(String name, Object value) {
        super(name, value);
        this.modOperation = REPLACE_VALUE;
    }

    /**
     * ModPropertyValue is initialized with attributeName and attributeValue
     * and modifyOperation
     * 
     * @param name
     *            Property Name
     * @param value
     *            Property Value
     * @param modOp
     *            Valid Modify Operation - ADD_VALUE/ DELETE_VALUE/ REPLACE_VALUE
     * @throws InvalidValueException
     *            When the supplied modify operation is invalid
     */
    public ModPropertyValue(String name, Object value, int modOp) throws InvalidValueException {
        super(name, value);
        setModOperation(modOp);
    }

    /**
     * ModPropertyValue is initialized with attributeName and attributeValues
     * 
     * @param name
     *            Property Name
     * @param values
     *            List of Property values
     * @param locale
     *            Launguage code
     */
    public ModPropertyValue(String name, List values, String locale) {
        super(name, values, locale);
        this.modOperation = REPLACE_VALUE;
    }

    /**
     * ModPropertyValue is initialized with attributeName and attributeValues
     * 
     * @param name
     *            Property Name
     * @param values
     *            List of Property values
     * @param locale
     *            Locale value
     */
    public ModPropertyValue(String name, List values, Locale locale) {
        super(name, values, locale);
        this.modOperation = REPLACE_VALUE;
    }

    /**
     * ModPropertyValue is initialized with attributeName and attributeValues
     * and modifyOperation
     * 
     * @param name
     *            Property Name
     * @param values
     *            List of Property values
     * @param modOp
     *            Valid Modify Operation - ADD_VALUE/DELETE_VALUE/REPLACE_VALUE
     * @param locale
     *            Launguage code
     * @throws InvalidValueException
     *            When the supplied modify operation is invalid
     */
    public ModPropertyValue(String name, List values, int modOp, String locale) throws InvalidValueException {
        super(name, values, locale);
        setModOperation(modOp);
    }

    /**
     * ModPropertyValue is initialized with attributeName and attributeValues
     * and modifyOperation
     * 
     * @param name
     *            Property Name
     * @param values
     *            List of Property values
     * @param modOp
     *            Valid Modify Operation - ADD_VALUE/DELETE_VALUE/REPLACE_VALUE
     * @param locale
     *            Locale value
     * @throws InvalidValueException
     *            When the supplied modify operation is invalid
     */
    public ModPropertyValue(String name, List values, int modOp, Locale locale) throws InvalidValueException {
        super(name, values, locale);
        setModOperation(modOp);
    }

    /**
     * ModPropertyValue is initialized with attributeName and attributeValue
     * 
     * @param name
     *            Property Name
     * @param value
     *            Property value
     * @param locale
     *            Launguage code
     */
    public ModPropertyValue(String name, Object value, String locale) {
        super(name, value, locale);
        this.modOperation = REPLACE_VALUE;
    }

    /**
     * ModPropertyValue is initialized with attributeName and attributeValue
     * 
     * @param name
     *            Property Name
     * @param value
     *            Property value
     * @param locale
     *            Locale value
     */
    public ModPropertyValue(String name, Object value, Locale locale) {
        super(name, value, locale);
        this.modOperation = REPLACE_VALUE;
    }

    /**
     * ModPropertyValue is initialized with attributeName and attributeValue
     * and modifyOperation
     * 
     * @param name
     *            Property Name
     * @param value
     *            Property Value
     * @param modOp
     *            Valid Modify Operation - ADD_VALUE/ DELETE_VALUE/ REPLACE_VALUE
     * @param locale
     *            Launguage code
     * @throws InvalidValueException
     *            When the supplied modify operation is invalid
     */
    public ModPropertyValue(String name, Object value, int modOp, String locale) throws InvalidValueException {
        super(name, value, locale);
        setModOperation(modOp);
    }

    /**
     * ModPropertyValue is initialized with attributeName and attributeValue
     * and modifyOperation
     * 
     * @param name
     *            Property Name
     * @param value
     *            Property Value
     * @param modOp
     *            Valid Modify Operation - ADD_VALUE/ DELETE_VALUE/ REPLACE_VALUE
     * @param locale
     *            Locale value
     * @throws InvalidValueException
     *            When the supplied modify operation is invalid
     */
    public ModPropertyValue(String name, Object value, int modOp, Locale locale) throws InvalidValueException {
        super(name, value, locale);
        setModOperation(modOp);
    }

    /**
     * Returns the Modify Opeartion 
     * 
     * @return Modify Operation (ADD_VALUE/ DELETE_VALUE/ REPLACE_VALUE)
     */
    public int getModOperation() {
        return this.modOperation;
    }

    /**
     * Set the Modify Operation
     * 
     * @param modOp
     *            Valid Modify Operation - ADD_VALUE/ DELETE_VALUE/ REPLACE_VALUE
     * @throws InvalidValueException
     *            When the supplied modify operation is invalid
     */
    public void setModOperation(int modOp) throws InvalidValueException {
        if (modOp != ADD_VALUE && modOp != DELETE_VALUE && modOp != REPLACE_VALUE) {
            throw new InvalidValueException("Invalid modify operation supplied: " + modOp);
        }
        this.modOperation = modOp;
    }
}

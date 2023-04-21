package org.webdocwf.util.loader.graphicaleditor.model;

import java.io.Serializable;

/**
 * This class represent an XML attribute.
 * 
 * @author Adnan Veseli
 */
public class Attribute implements Serializable {

    private static final long serialVersionUID = 4263760938466692488L;

    /**
     * Determines whether this attribute is requiered or not
     * 
     * @author Adnan Veseli
     */
    public static enum REQUIREDTYPE {

        REQUIRED {

            public String toString() {
                return "true";
            }
        }
        , IMPLIED {

            public String toString() {
                return "false";
            }
        }

    }

    ;

    /**
     * Attribute name
     */
    private final String name;

    /**
     * Attribute value
     */
    private String value;

    /**
     * value must be one of the strings in possibleValues
     */
    private final String[] validValues;

    /**
     * Is this attribute required
     */
    private final REQUIREDTYPE isRequired;

    /**
     * the attribute value cannot be changed anymore
     */
    private boolean isFrozen = false;

    /**
     * Default constructor
     * 
     * @param name
     *            The name of the Attribute
     * @param validValues
     *            Set the valid values. They cannot be changed.
     * @param isAttributeRequired
     *            Set whether the Attribute is required or not.
     */
    public Attribute(final String name, final String[] validValues, final REQUIREDTYPE isAttributeRequired) {
        this.name = name;
        if (isAttributeRequired == null) {
            this.isRequired = REQUIREDTYPE.IMPLIED;
        } else {
            this.isRequired = isAttributeRequired;
        }
        this.validValues = validValues;
    }

    /**
     * @return Returns the name of the Attribute.
     */
    public String getName() {
        return name;
    }

    /**
     * @return True if Attribute allows only predefined Values, otherwise false.
     */
    public boolean hasValidValues() {
        return !(validValues == null);
    }

    /**
     * @return Valid (predefined) Values for this attribute.
     */
    public String[] getValidValues() {
        return validValues;
    }

    /**
     * @return Returns the value of the Attribute
     */
    public String getValue() {
        return value;
    }

    /**
     * sets the value of the property
     * 
     * @param value
     *            value to be set
     * @exception java.lang.IllegalArgumentException
     *                if value is not appropriate as described in dtd, the
     *                exception will be thrown.
     */
    public void setValue(final String value) throws IllegalArgumentException {
        if (!isFrozen()) {
            if (value.length() != 0) {
                if (!hasValidValues()) {
                    this.value = value;
                } else {
                    for (int i = 0; i < validValues.length; i++) {
                        if (validValues[i].equals(value)) {
                            this.value = value;
                            return;
                        }
                    }
                    throw new IllegalArgumentException("Value '" + value + "' is not valid for attribute '" + getName() + "'");
                }
            } else {
                throw new IllegalArgumentException("Value '" + value + "' is not valid for attribute '" + getName() + "'");
            }
        } else {
            throw new IllegalArgumentException("The attribute '" + getName() + "' has been frozen. You cannot change the value anymore");
        }
    }

    /**
     * @return If attribute is required REQUIRED will be returned, else IMPLIED.
     */
    public REQUIREDTYPE isRequired() {
        return isRequired;
    }

    /**
     * @return True if Attribute is valid, otherwise false.
     */
    public boolean isValid() {
        return getValue() != null;
    }

    /**
     * After you call this method, attribute value cannot be changed anymore.
     */
    public void freeze() {
        if (!isValid()) {
            throw new IllegalArgumentException("Attribute '" + getName() + "' has no valid value to freeze!!");
        }
        isFrozen = true;
    }

    /**
     * @return True if Attribute has been frozen, otherwise false.
     */
    public boolean isFrozen() {
        return isFrozen;
    }
}

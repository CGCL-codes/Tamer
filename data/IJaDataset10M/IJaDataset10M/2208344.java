package org.wsmoss.adapter.storage.jpa.model;

/**
 * The Class WorkSessionAttribute.
 */
public class WorkSessionAttribute {

    /** The attribute name. */
    private String attributeName;

    /** The attribute value. */
    private Byte[] attributeValue;

    /**
 * Instantiates a new work session attribute.
 */
    public WorkSessionAttribute() {
    }

    /**
 * Sets the attribute name.
 * 
 * @param attributeName the attributeName to set
 */
    public void setAttributeName(String attributeName) {
        this.attributeName = attributeName;
    }

    /**
 * Gets the attribute name.
 * 
 * @return the attributeName
 */
    public String getAttributeName() {
        return attributeName;
    }

    /**
 * Sets the attribute value.
 * 
 * @param attributeValue the attributeValue to set
 */
    public void setAttributeValue(Byte[] attributeValue) {
        this.attributeValue = attributeValue;
    }

    /**
 * Gets the attribute value.
 * 
 * @return the attributeValue
 */
    public Byte[] getAttributeValue() {
        return attributeValue;
    }
}

package org.innoq.objectbrowser.framework.common;

/**
 * Allows access to named values, called attributes
 *
 * @author frb
 */
public interface AttributeHolder {

    /**
     * replaces any old value previously stored under the given name
     */
    void setAttributeValue(String pName, Object pValue);

    /**
     * @return <code>true</code> if a value for the given attribute is set,
     * even if the value is <code>null</code>
     */
    boolean containsAttribute(String pName);

    /**
     * @return <code>null</code> for unknown attributes, otherwise the value set
     */
    Object getAttributeValue(String pName);

    /**
     * @return <code>false</code> if there's no value for the given attribute
     * or the value is <code>null</code>
     * @see #getAttributeValue
     */
    boolean containsValueFor(String pName);
}

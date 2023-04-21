package org.jlf.dataMap;

import org.jlf.log.*;

/**
 * This class is used to hold attributes of type Double.
 * It converts any Number type atribute into a Double, losing
 * precision automatically if called for.
 *
 * @version: $Revision: 1.2 $
 * @author:  Todd Lauinger
 *
 * @see DataAttribute
 * @see DataAttributeDescriptor
 */
public class DoubleAttribute extends DataAttribute {

    /**
     * Protect the default constructor.  Must at least
     * have an attribute name to construct the attribute.
     */
    public DoubleAttribute() {
    }

    /**
     * Normal attribute constructor.
     */
    public DoubleAttribute(String name) {
        super(name);
    }

    /**
     * Override for Double-specific behavior.
     */
    public void basicSetValue(Object newValue) {
        if (newValue == null) {
            super.basicSetValue(newValue);
        } else if (newValue instanceof Number) {
            basicSetValue((Number) newValue);
        } else if (newValue instanceof String) {
            basicSetValue((String) newValue);
        } else {
            throw new DataMapError("Error: attribute " + name + " is a DoubleAttribute," + "trying to set it to type " + newValue.getClass().getName() + " with value: " + newValue, null, Log.WARNING_LEVEL);
        }
    }

    /**
     * Main setter for String values to ensure the resulting
     * value set is of type Double.
     */
    public void basicSetValue(String newValue) {
        if ((newValue == null) || newValue.equals("")) {
            super.basicSetValue(null);
        } else {
            super.basicSetValue(new Double(newValue));
        }
    }

    /**
     * Main setter for Number values to ensure the resulting
     * value set is of type Double.
     */
    public void basicSetValue(Number newValue) {
        super.basicSetValue(new Double(newValue.doubleValue()));
    }

    /**
     * Returns the double value of the attribute if
     * the attribute has one.
     *
     * @exception DataMapError if the attribute
     *   cannot be safely converted to a double or
     *   if the attribute is null.
     */
    public double getDoubleValue() {
        Object value = getValue();
        if (value == null) {
            throw new DataMapError("Error: trying to getDoubleValue() of attribute " + name + ", attribute is null", null, Log.WARNING_LEVEL);
        } else {
            return ((Double) value).doubleValue();
        }
    }
}

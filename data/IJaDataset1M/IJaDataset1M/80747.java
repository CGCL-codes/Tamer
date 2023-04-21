package org.jlib.core.system;

/**
 * Exception thrown when trying to retrieve the value of a property that is not set.
 * 
 * @author Igor Akkerman
 */
public class PropertyNotSetException extends Exception {

    /** name of the property that is not set */
    private String propertyName;

    /**
     * Creates a new PropertyNotSetException.
     * 
     * @param propertyName
     *        String specifying the name of the property that is not set
     */
    public PropertyNotSetException(String propertyName) {
        super();
        this.propertyName = propertyName;
    }

    /**
     * Returns the name of the property that is not set.
     * 
     * @return String specifying the name of the property that is not set
     */
    public String getPropertyName() {
        return propertyName;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[" + propertyName + "]";
    }
}

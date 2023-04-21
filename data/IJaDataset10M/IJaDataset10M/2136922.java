package uk.ac.ed.ph.jqtiplus.attribute;

/**
 * Attributes with known value set should implement this interface.
 * It can help user to input value of attribute.
 * Instead of typing values, he can choose from supported values.
 * For example boolean values or enumeration values.
 * 
 * @author Jiri Kajaba
 */
public interface EnumerateAttribute extends Attribute {

    /**
     * Gets all supported values of this attribute.
     *
     * @return all supported values of this attribute
     */
    public Object[] getSupportedValues();
}

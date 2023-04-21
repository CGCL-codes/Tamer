package org.qtitools.mathassess.attribute;

import org.qtitools.mathassess.type.SyntaxType;
import org.qtitools.qti.attribute.EnumerateAttribute;
import org.qtitools.qti.attribute.SingleAttribute;
import org.qtitools.qti.node.XmlNode;

/**
 * Attribute with syntaxType value.
 * 
 * @author  Jonathon Hare
 * @version $Revision: 2205 $
 */
public class SyntaxTypeAttribute extends SingleAttribute implements EnumerateAttribute {

    private static final long serialVersionUID = 8834496656714809174L;

    /**
     * Constructs attribute.
     * 
     * @param parent attribute's parent
     * @param name attribute's name
     */
    public SyntaxTypeAttribute(XmlNode parent, String name) {
        super(parent, name);
    }

    /**
     * Constructs attribute.
     * 
     * @param parent attribute's parent
     * @param name attribute's name
     * @param defaultValue attribute's default value
     */
    public SyntaxTypeAttribute(XmlNode parent, String name, SyntaxType defaultValue) {
        super(parent, name, defaultValue);
    }

    /**
     * Constructs attribute.
     * 
     * @param parent attribute's parent
     * @param name attribute's name
     * @param value attribute's value
     * @param defaultValue attribute's default value
     * @param required is this attribute required
     */
    public SyntaxTypeAttribute(XmlNode parent, String name, SyntaxType value, SyntaxType defaultValue, boolean required) {
        super(parent, name, value, defaultValue, required);
    }

    @Override
    public SyntaxType getValue() {
        return (SyntaxType) super.getValue();
    }

    /**
     * Sets new value of attribute.
     * 
     * @param value new value of attribute
     * @see #getValue
     */
    public void setValue(SyntaxType value) {
        super.setValue(value);
    }

    @Override
    public SyntaxType getDefaultValue() {
        return (SyntaxType) super.getDefaultValue();
    }

    /**
     * Sets new default value of attribute.
     * 
     * @param defaultValue new default value of attribute
     * @see #getDefaultValue
     */
    public void setDefaultValue(SyntaxType defaultValue) {
        super.setDefaultValue(defaultValue);
    }

    @Override
    protected SyntaxType parseValue(String value) {
        return SyntaxType.parseSyntaxType(value);
    }

    /**
     * Gets all supported values of this attribute.
     * 
     * @return all supported values of this attribute
     */
    public SyntaxType[] getSupportedValues() {
        return SyntaxType.values();
    }
}

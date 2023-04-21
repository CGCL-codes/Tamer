package uk.ac.ed.ph.jqtiplus.attribute.value;

import uk.ac.ed.ph.jqtiplus.attribute.MultipleAttribute;
import uk.ac.ed.ph.jqtiplus.node.XmlNode;
import uk.ac.ed.ph.jqtiplus.value.FloatValue;
import java.util.List;

/**
 * Attribute with float values.
 * 
 * @author Jiri Kajaba
 */
public class FloatMultipleAttribute extends MultipleAttribute<Double> {

    private static final long serialVersionUID = 1L;

    /**
     * Constructs attribute.
     *
     * @param parent attribute's parent
     * @param name attribute's name
     */
    public FloatMultipleAttribute(XmlNode parent, String name) {
        super(parent, name);
    }

    /**
     * Constructs attribute.
     *
     * @param parent attribute's parent
     * @param name attribute's name
     * @param defaultValue attribute's default value
     */
    public FloatMultipleAttribute(XmlNode parent, String name, List<Double> defaultValue) {
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
    public FloatMultipleAttribute(XmlNode parent, String name, List<Double> value, List<Double> defaultValue, boolean required) {
        super(parent, name, value, defaultValue, required);
    }

    @Override
    public List<Double> getValues() {
        return super.getValues();
    }

    @Override
    public List<Double> getDefaultValues() {
        return super.getDefaultValues();
    }

    @Override
    protected Double parseValue(String value) {
        return FloatValue.parseFloat(value);
    }
}

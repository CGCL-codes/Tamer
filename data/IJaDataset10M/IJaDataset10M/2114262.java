package org.qtitools.qti.node.expression.operator.math;

import org.qtitools.qti.attribute.EnumerateAttribute;
import org.qtitools.qti.attribute.SingleAttribute;
import org.qtitools.qti.node.XmlNode;

/**
 * Wraps {@link MathConstantTarget} attribute up in QTI style
 * 
 * @author   David McKain
 * @revision $Revision: 2710 $
 */
public class MathConstantNameAttribute extends SingleAttribute implements EnumerateAttribute {

    private static final long serialVersionUID = -2367561162140765443L;

    /**
     * Constructs attribute.
     *
     * @param parent attribute's parent
     * @param name attribute's name
     */
    public MathConstantNameAttribute(XmlNode parent, String name) {
        super(parent, name);
    }

    @Override
    public MathConstantTarget getValue() {
        return (MathConstantTarget) super.getValue();
    }

    public void setValue(MathConstantTarget value) {
        super.setValue(value);
    }

    @Override
    public MathConstantTarget getDefaultValue() {
        return (MathConstantTarget) super.getDefaultValue();
    }

    public void setDefaultValue(MathConstantTarget defaultValue) {
        super.setDefaultValue(defaultValue);
    }

    @Override
    protected MathConstantTarget parseValue(String value) {
        return MathConstantTarget.parseConstant(value);
    }

    public MathConstantTarget[] getSupportedValues() {
        return MathConstantTarget.values();
    }
}

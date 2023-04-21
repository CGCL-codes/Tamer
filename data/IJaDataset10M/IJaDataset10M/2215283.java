package ru.pda.chemistry.entities.attributeValue;

import ru.pda.chemistry.entities.Attribute;
import ru.pda.chemistry.entities.AttributeType;

/**
 * User: 1
 * Date: 05.06.2010
 * Time: 12:30:22
 */
public class BooleanAttributeValue extends AttributeValue {

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = (Boolean) value;
    }

    public String getSqlAttributeValue() {
        if (value == null) return "null";
        return value ? "1" : "0";
    }

    public String getHtmlAttributeValue() {
        if (value == null) return "-";
        if (value) return "<input type=\"checkbox\" checked=\"checked\" disabled=\"disabled\"/>";
        return "<input type=\"checkbox\" disabled=\"disabled\"/>";
    }

    public void setAttribute(Attribute attribute) {
        if (attribute.getType() != AttributeType.BOOLEAN) throw new IllegalArgumentException("Attribute type must be boolean");
        this.attribute = attribute;
    }

    private Boolean value;
}

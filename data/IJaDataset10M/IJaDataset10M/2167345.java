package ru.pda.chemistry.entities.attributeValue;

import ru.pda.chemistry.entities.Attribute;

/**
 * User: 1
 * Date: 05.06.2010
 * Time: 12:28:23
 */
public abstract class AttributeValue {

    public abstract Object getValue();

    public abstract void setValue(Object value);

    public abstract String getSqlAttributeValue();

    public abstract String getHtmlAttributeValue();

    public Attribute getAttribute() {
        return attribute;
    }

    public void setAttribute(Attribute attribute) {
        this.attribute = attribute;
    }

    protected Attribute attribute;
}

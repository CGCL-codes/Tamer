package org.vikamine.swing;

import org.vikamine.kernel.data.Attribute;

public class AttributeContainer {

    private Attribute attribute;

    public AttributeContainer(Attribute attribute) {
        this.attribute = attribute;
    }

    @Override
    public String toString() {
        return attribute.getDescription();
    }

    public Attribute getAttribute() {
        return attribute;
    }
}

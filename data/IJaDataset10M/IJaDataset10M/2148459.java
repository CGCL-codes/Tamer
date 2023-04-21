package com.luxoft.fitpro.core.templates;

import java.util.ArrayList;
import java.util.List;

public class MockTemplateBuilderRegistry implements ITemplateBuilderRegistry {

    private List<ITemplateBuilderExtensionElement> elements = new ArrayList<ITemplateBuilderExtensionElement>();

    public List<ITemplateBuilderExtensionElement> getElements() {
        return this.elements;
    }

    public void addElement(MockTemplateBuilderExtensionElement element) {
        elements.add(element);
    }
}

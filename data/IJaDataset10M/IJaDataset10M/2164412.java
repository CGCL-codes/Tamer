package com.volatileengine.xml.elements;

import com.volatileengine.xml.IFileLoader;
import com.volatileengine.xml.XMLParsingException;

/**
 * @author darkprophet
 * 
 */
public class EnumXMLElement extends SimpleXMLElement<Enum<?>> {

    @Override
    public Element<?> instantiateChild(String tagName, IFileLoader<?> loader) {
        throw new XMLParsingException(getAssociatedTag() + " element does not have any type of child.");
    }

    @Override
    public String getAssociatedTag() {
        return "enum";
    }

    @Override
    public void processChild(Element<?> child, IFileLoader<?> loader) {
        throw new XMLParsingException(getAssociatedTag() + " element does not have any type of child.");
    }

    @SuppressWarnings("unchecked")
    @Override
    public void endElement(IFileLoader<?> loader) {
        Class enumClass = null;
        try {
            enumClass = Class.forName(attributes.get("class"));
        } catch (ClassNotFoundException e) {
            throw new XMLParsingException("Cannot find class: " + attributes.get("class"));
        }
        value = Enum.valueOf(enumClass, attributes.get("value"));
    }
}

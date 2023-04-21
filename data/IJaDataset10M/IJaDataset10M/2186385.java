package net.woodstock.rockapi.pojo.converter.xml.internal;

import net.woodstock.rockapi.pojo.converter.xml.XmlAttributeConverter;
import net.woodstock.rockapi.xml.dom.XmlElement;

class BooleanConverter implements XmlAttributeConverter<Boolean> {

    public Boolean fromXml(XmlElement element) {
        return new Boolean(element.getBoolean());
    }

    public XmlElement toXml(XmlElement element, String name, Boolean b) {
        if (b == null) {
            return element.addElement(name);
        }
        return element.addElement(name, b.booleanValue());
    }
}

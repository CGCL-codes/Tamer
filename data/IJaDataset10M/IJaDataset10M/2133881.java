package net.woodstock.rockapi.pojo.converter.xml.internal;

import java.io.IOException;
import java.io.Serializable;
import net.woodstock.rockapi.pojo.converter.ConverterException;
import net.woodstock.rockapi.pojo.converter.xml.XmlAttributeConverter;
import net.woodstock.rockapi.xml.dom.XmlElement;

class ObjectConverter implements XmlAttributeConverter<Object> {

    public Object fromXml(XmlElement element) {
        try {
            return element.getObject();
        } catch (Exception e) {
            throw new ConverterException(e);
        }
    }

    public XmlElement toXml(XmlElement element, String name, Object o) {
        if (o == null) {
            return element.addElement(name);
        }
        try {
            return element.addObject(name, (Serializable) o);
        } catch (IOException e) {
            throw new ConverterException(e);
        }
    }
}

package org.jtools.config.invocation.handler;

import org.jpattern.xml.QName;
import org.jtools.config.invocation.ConstructorSpecification;

/**
 * TODO type-description
 * @author <a href="mailto:rainer.noack@jtools.org">Rainer Noack</a>
 */
public class ObjectHandler implements AttributeHandler {

    public Class<?> getAttributeClass() {
        return Object.class;
    }

    public Class<?> getAttributeSuperClass() {
        return null;
    }

    public Object testAttribute(int invocationLevel, ConstructorSpecification comp, Object parent, Class<?> attributeClass, QName name, String value) {
        return value;
    }

    public Object createAttribute(Object parent, Class<?> attributeClass, QName name, String value, Object data) {
        return data;
    }
}

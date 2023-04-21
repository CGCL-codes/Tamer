package com.risertech.xdav.internal.webdav.xml.element;

import java.util.List;
import javax.xml.namespace.QName;
import org.jdom.Element;
import com.risertech.xdav.internal.webdav.xml.AbstractElementConverter;
import com.risertech.xdav.internal.webdav.xml.XMLConverterRegistry;

public abstract class AbstractAnyConverter extends AbstractElementConverter {

    public AbstractAnyConverter(Class<?> clazz, QName elementName) {
        super(clazz, elementName);
    }

    @Override
    protected Element doCreateElement(Object object) {
        Element element = createElement();
        Object anyObject = getAnyObject(object);
        if (anyObject == null) {
            return element;
        }
        if (anyObject instanceof List<?>) {
            for (Object item : (List<?>) anyObject) {
                element.addContent(XMLConverterRegistry.getElement(item));
            }
        } else {
            element.addContent(XMLConverterRegistry.getElement(anyObject));
        }
        return element;
    }

    protected abstract Object getAnyObject(Object object);

    @Override
    protected Object doCreateObject(Element element) {
        List<?> children = getChildrenObjects(element, false);
        if (children.size() == 0) {
            return createObjectWithParameter(null);
        } else if (children.size() == 1) {
            return createObjectWithParameter(children.get(0));
        } else {
            return createObjectWithParameter(children);
        }
    }

    protected abstract Object createObjectWithParameter(Object object);
}

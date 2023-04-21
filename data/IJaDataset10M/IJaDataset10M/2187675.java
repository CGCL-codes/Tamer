package com.risertech.xdav.internal.caldav.xml.property;

import org.jdom.Element;
import com.risertech.xdav.caldav.property.MaxInstances;
import com.risertech.xdav.internal.caldav.xml.CalDAVConverter;

/**
 * <!ELEMENT max-instances (#PCDATA)>
 * 		PCDATA value: a numeric value (integer greater than zero)
 * 
 * @author phil
 */
public class MaxInstancesConverter extends CalDAVConverter {

    public MaxInstancesConverter() {
        super(MaxInstances.class, "max-instances");
    }

    @Override
    protected Element doCreateElement(Object object) {
        Element element = createElement();
        element.setText(((MaxInstances) object).getMaxInstances() + "");
        return element;
    }

    @Override
    protected Object doCreateObject(Element element) {
        return new MaxInstances(Integer.parseInt(element.getText()));
    }
}

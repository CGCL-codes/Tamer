package com.risertech.xdav.internal.caldav.xml.element;

import org.jdom.Element;
import com.risertech.xdav.caldav.element.CalendarQuery;
import com.risertech.xdav.caldav.element.Filter;
import com.risertech.xdav.caldav.element.Timezone;
import com.risertech.xdav.internal.caldav.xml.CalDAVConverter;
import com.risertech.xdav.internal.webdav.IProp;
import com.risertech.xdav.internal.webdav.xml.XMLConverterRegistry;

/**
 * Definition:
 * 		<!ELEMENT calendar-query ((DAV:allprop | DAV:propname | DAV:prop)?, filter, timezone?)>
 * 
 * @author phil
 */
public class CalendarQueryConverter extends CalDAVConverter {

    public CalendarQueryConverter() {
        super(CalendarQuery.class, "calendar-query");
    }

    @Override
    protected Element doCreateElement(Object object) {
        Element element = createElement();
        CalendarQuery calendarQuery = (CalendarQuery) object;
        if (calendarQuery.getProp() != null) {
            element.addContent(XMLConverterRegistry.getElement(calendarQuery.getProp()));
        }
        if (calendarQuery.getTimeZone() != null) {
            element.addContent(XMLConverterRegistry.getElement(calendarQuery.getTimeZone()));
        }
        element.addContent(XMLConverterRegistry.getElement(calendarQuery.getFilter()));
        return element;
    }

    @Override
    protected Object doCreateObject(Element element) {
        IProp prop = null;
        Filter filter = null;
        Timezone timeZone = null;
        for (Object object : getChildrenObjects(element, true)) {
            if (object instanceof IProp) {
                prop = (IProp) object;
            } else if (object instanceof Filter) {
                filter = (Filter) object;
            } else if (object instanceof Timezone) {
                timeZone = (Timezone) object;
            }
        }
        return new CalendarQuery(prop, filter, timeZone);
    }
}

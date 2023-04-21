package com.risertech.xdav.caldav.element;

import com.risertech.xdav.internal.caldav.CalDAVElement;

/**
 * 9.1.  CALDAV:calendar XML Element
 * 
 * Name:  calendar
 * Namespace:  urn:ietf:params:xml:ns:caldav
 * Purpose:  Specifies the resource type of a calendar collection.
 * Description:  See Section 4.2.
 * 
 * Definition:
 *       <!ELEMENT calendar EMPTY>
 * 
 * @author phil
 */
public class Calendar extends CalDAVElement {

    public Calendar() {
        super("calendar");
    }
}

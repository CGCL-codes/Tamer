package com.risertech.xdav.caldav.element;

import com.risertech.xdav.internal.caldav.CalDAVElement;

/**
 * 9.8.  CALDAV:timezone XML Element
 * 
 * Name:  timezone
 * Namespace:  urn:ietf:params:xml:ns:caldav
 * Purpose:  Specifies the time zone component to use when determining
 * 		the results of a report.
 * Description:  The CALDAV:timezone XML element specifies that for a
 * 		given calendaring REPORT request, the server MUST rely on the
 * 		specified VTIMEZONE component instead of the CALDAV:calendar-
 * 		timezone property of the calendar collection, in which the
 * 		calendar object resource is contained to resolve "date" values and
 * 		"date with local time" values (i.e., floating time) to "date with
 * 		UTC time" values.  The server will require this information to
 * 		determine if a calendar component scheduled with "date" values or
 * 		"date with local time" values intersects a CALDAV:time-range
 * 		specified in a CALDAV:calendar-query REPORT.
 * 		
 * 		Note:  The iCalendar data embedded within the CALDAV:timezone XML
 * 		element MUST follow the standard XML character data encoding
 * 		rules, including use of &lt;, &gt;, &amp; etc. entity encoding or
 * 		the use of a <![CDATA[ ... ]]> construct.  In the later case, the
 * 		iCalendar data cannot contain the character sequence "]]>", which
 * 		is the end delimiter for the CDATA section.
 * 
 * Definition:
 * 		<!ELEMENT timezone (#PCDATA)>
 * 		PCDATA value: an iCalendar object with exactly one VTIMEZONE
 * 
 * @author phil
 */
public class Timezone extends CalDAVElement {

    private final java.util.TimeZone timezone;

    public Timezone(java.util.TimeZone timezone) {
        super("timezone");
        this.timezone = timezone;
    }

    public java.util.TimeZone getTimeZone() {
        return timezone;
    }
}

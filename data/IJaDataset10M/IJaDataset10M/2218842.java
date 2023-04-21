package com.risertech.xdav.caldav.property;

import java.util.TimeZone;
import com.risertech.xdav.webdav.AbstractProperty;

/**
 * 5.2.2.  CALDAV:calendar-timezone Property
 * 
 * Name:  calendar-timezone
 * Namespace:  urn:ietf:params:xml:ns:caldav
 * Purpose:  Specifies a time zone on a calendar collection.
 * Conformance:  This property SHOULD be defined on all calendar
 * 		collections.  If defined, it SHOULD NOT be returned by a PROPFIND
 * 		DAV:allprop request (as defined in Section 12.14.1 of [RFC2518]).
 * Description:  The CALDAV:calendar-timezone property is used to
 * 		specify the time zone the server should rely on to resolve "date"
 * 		values and "date with local time" values (i.e., floating time) to
 * 		"date with UTC time" values.  The server will require this
 * 		information to determine if a calendar component scheduled with
 * 		"date" values or "date with local time" values overlaps a CALDAV:
 * 		time-range specified in a CALDAV:calendar-query REPORT.  The
 * 		server will also require this information to compute the proper
 * 		FREEBUSY time period as "date with UTC time" in the VFREEBUSY
 * 		component returned in a response to a CALDAV:free-busy-query
 * 		REPORT request that takes into account calendar components
 * 		scheduled with "date" values or "date with local time" values.  In
 * 		the absence of this property, the server MAY rely on the time zone
 * 		of their choice.
 * 
 * 		Note:  The iCalendar data embedded within the CALDAV:calendar-
 * 		timezone XML element MUST follow the standard XML character data
 * 		encoding rules, including use of &lt;, &gt;, &amp; etc. entity
 * 		encoding or the use of a <![CDATA[ ... ]]> construct.  In the
 * 		later case, the iCalendar data cannot contain the character
 * 		sequence "]]>", which is the end delimiter for the CDATA section.
 * 
 * Definition:
 * 		<!ELEMENT calendar-timezone (#PCDATA)>
 * 		PCDATA value: an iCalendar object with exactly one VTIMEZONE
 * 		component.
 * 
 * Example:
 * 		<C:calendar-timezone xmlns:C="urn:ietf:params:xml:ns:caldav">BEGIN:VCALENDAR
 * 		PRODID:-//Example Corp.//CalDAV Client//EN
 * 		VERSION:2.0
 * 		BEGIN:VTIMEZONE
 * 		TZID:US-Eastern
 * 		LAST-MODIFIED:19870101T000000Z
 * 		BEGIN:STANDARD
 * 		DTSTART:19671029T020000
 * 		RRULE:FREQ=YEARLY;BYDAY=-1SU;BYMONTH=10
 * 		TZOFFSETFROM:-0400
 * 		TZOFFSETTO:-0500
 * 		TZNAME:Eastern Standard Time (US &amp; Canada)
 * 		END:STANDARD
 * 		BEGIN:DAYLIGHT
 * 		DTSTART:19870405T020000
 * 		RRULE:FREQ=YEARLY;BYDAY=1SU;BYMONTH=4
 * 		TZOFFSETFROM:-0500
 * 		TZOFFSETTO:-0400
 * 		TZNAME:Eastern Daylight Time (US &amp; Canada)
 * 		END:DAYLIGHT
 * 		END:VTIMEZONE
 * 		END:VCALENDAR
 * 		</C:calendar-timezone>
 * 
 * @author phil
 */
public class CalendarTimezone extends AbstractProperty {

    private final TimeZone timeZone;

    public CalendarTimezone() {
        super(true);
        timeZone = null;
    }

    public CalendarTimezone(TimeZone timeZone) {
        super(false);
        this.timeZone = timeZone;
    }

    public TimeZone getTimeZone() {
        return timeZone;
    }
}

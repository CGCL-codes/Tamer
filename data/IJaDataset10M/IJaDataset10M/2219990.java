package com.risertech.xdav.caldav.element;

import java.util.Calendar;
import com.risertech.xdav.internal.caldav.AbstractTimeRange;

/**
 * 9.6.5.  CALDAV:expand XML Element
 * 
 * Name:  expand
 * Namespace:  urn:ietf:params:xml:ns:caldav
 * Purpose:  Forces the server to expand recurring components into
 * 		individual recurrence instances.
 * Description:  The CALDAV:expand XML element specifies that for a
 * 		given calendaring REPORT request, the server MUST expand the
 * 		recurrence set into calendar components that define exactly one
 * 		recurrence instance, and MUST return only those whose scheduled
 * 		time intersect a specified time range.
 * 		
 * 		The "start" attribute specifies the inclusive start of the time
 * 		range, and the "end" attribute specifies the non-inclusive end of
 * 		the time range.  Both attributes are specified as date with UTC
 * 		time value.  The value of the "end" attribute MUST be greater than
 * 		the value of the "start" attribute.
 * 
 * 		The server MUST use the same logic as defined for CALDAV:time-
 * 		range to determine if a recurrence instance intersects the
 * 		specified time range.
 * 
 * 		Recurring components, other than the initial instance, MUST
 * 		include a RECURRENCE-ID property indicating which instance they
 * 		refer to.
 * 
 * 		The returned calendar components MUST NOT use recurrence
 * 		properties (i.e., EXDATE, EXRULE, RDATE, and RRULE) and MUST NOT
 * 		have reference to or include VTIMEZONE components.  Date and local
 * 		time with reference to time zone information MUST be converted
 * 		into date with UTC time.
 * 
 * Definition:
 * 		<!ELEMENT expand EMPTY>
 * 		<!ATTLIST expand start CDATA #REQUIRED
 * 						 end   CDATA #REQUIRED>
 * 		start value: an iCalendar "date with UTC time"
 * 		end value: an iCalendar "date with UTC time"
 * 
 * @author phil
 */
public class Expand extends AbstractTimeRange {

    public Expand(Calendar start, Calendar end) {
        super("expand", start, end);
    }
}

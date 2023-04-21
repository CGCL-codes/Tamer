package com.sitescape.team.util;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;
import com.sitescape.team.calendar.TimeZoneHelper;
import com.sitescape.team.context.request.RequestContext;
import com.sitescape.team.context.request.RequestContextHolder;
import com.sitescape.team.domain.User;

public class CalendarHelper {

    /**
	 * Returns calendar with the same time like <code>calendar</code> but in given <code>newTimeZone</code>.
	 * Routine creates a new calendar object so given <code>calendar</code> is untouched.
	 * 
	 * @param calendar
	 * @param newTimeZone
	 * @return
	 */
    public static Calendar convertToTimeZone(Calendar calendar, TimeZone newTimeZone) {
        if (calendar == null) {
            return null;
        }
        if (newTimeZone == null) {
            newTimeZone = TimeZoneHelper.getTimeZone("GMT");
        }
        Calendar result = new GregorianCalendar(newTimeZone);
        result.setTimeInMillis(calendar.getTimeInMillis());
        return result;
    }

    public static int getFirstDayOfWeek() {
        return Calendar.getInstance(getLocale()).getFirstDayOfWeek();
    }

    private static Locale getLocale() {
        RequestContext rc = RequestContextHolder.getRequestContext();
        if (rc != null) {
            User user = rc.getUser();
            if (user != null) return user.getLocale(); else return Locale.getDefault();
        } else {
            return Locale.getDefault();
        }
    }
}

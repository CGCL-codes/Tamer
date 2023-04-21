package net.sourceforge.compete.components;

import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.Parameter;
import java.sql.Time;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Copyright (c) 2010-2011 Rodica Balasa (rodiq@rodiq.ro).
 *
 * See the LICENSE file for terms of use.
 *
 */
public class InsertTime {

    @Property
    @Parameter(required = true, autoconnect = true)
    private Time time;

    public boolean hasTime() {
        if (time != null) return true;
        return false;
    }

    public String getHour() {
        if (time != null) {
            Calendar calendar = new GregorianCalendar();
            calendar.setTimeInMillis(time.getTime());
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            return format(hour);
        }
        return "";
    }

    public String getMinute() {
        if (time != null) {
            Calendar calendar = new GregorianCalendar();
            calendar.setTimeInMillis(time.getTime());
            int minute = calendar.get(Calendar.MINUTE);
            return format(minute);
        }
        return "";
    }

    public String getSecond() {
        if (time != null) {
            Calendar calendar = new GregorianCalendar();
            calendar.setTimeInMillis(time.getTime());
            int minute = calendar.get(Calendar.SECOND);
            return format(minute);
        }
        return "";
    }

    private String format(int value) {
        if (value < 10) {
            return "0" + value;
        } else {
            return "" + value;
        }
    }
}

package com.worldware.misc;

import java.util.Date;

/** Deal with bug in microsoft's date format routines. */
public class MyDate extends Date {

    private static final String[] dayNames = { "Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat" };

    private static final String[] monthNames = { "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec" };

    private static final String[] monthNamesCaps = { "JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL", "AUG", "SEP", "OCT", "NOV", "DEC" };

    public String toString() {
        String s = super.toString();
        int dayOfWeek = getDay();
        int dayOfMonth = getDate();
        int month = getMonth();
        String dayText = Integer.toString(dayOfMonth);
        String hourText = Integer.toString(getHours());
        String minuteText = Integer.toString(getMinutes());
        if (minuteText.length() == 1) minuteText = "0" + minuteText;
        String secondText = Integer.toString(getSeconds());
        if (secondText.length() == 1) secondText = "0" + secondText;
        int timeZoneOffset = -getTimezoneOffset();
        boolean neg = false;
        if (timeZoneOffset < 0) {
            neg = true;
            timeZoneOffset = -timeZoneOffset;
        }
        timeZoneOffset /= 60;
        String timeZoneText = Integer.toString(timeZoneOffset) + "00";
        if (timeZoneOffset < 10) timeZoneText = "0" + timeZoneText;
        if (neg) timeZoneText = "-" + timeZoneText;
        return dayNames[dayOfWeek] + ", " + dayText + " " + monthNames[month] + " " + (1900 + getYear()) + " " + hourText + ":" + minuteText + ":" + secondText + " " + timeZoneText;
    }

    /** Gets the current date */
    public MyDate() {
        super();
    }

    /**  Creates a MyDate object for the specifed date/time */
    public MyDate(long time) {
        super(time);
    }
}

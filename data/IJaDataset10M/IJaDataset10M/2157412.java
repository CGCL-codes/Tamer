package com.skillworld.webapp.web.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * This class should be used to format dates and others fields
 * 
 * */
public class DateHandler {

    public DateHandler() {
    }

    public static String formatDafe(Calendar x) {
        String ret = "";
        SimpleDateFormat bartDateFormat = null;
        if (x != null) {
            bartDateFormat = new SimpleDateFormat("dd-MM-yyyy H:m:s");
            ret = bartDateFormat.format(x.getTime());
        }
        return ret;
    }
}

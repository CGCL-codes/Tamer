package com.simplerss.helper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class DateHelper {

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss z");

    public static Calendar parseDate(String text) {
        GregorianCalendar calendar = new GregorianCalendar();
        try {
            calendar.setTime(DATE_FORMAT.parse(text));
        } catch (ParseException e) {
            calendar = null;
        }
        return calendar;
    }

    public static String renderDate(Calendar calendar) {
        String dateStr = "";
        if (calendar != null) {
            dateStr = DATE_FORMAT.format(calendar.getTime());
        }
        return dateStr;
    }
}

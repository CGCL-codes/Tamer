package com.bazaaroid.server.gae.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

public class DateUtil {

    public static String dateToString(Date date, boolean leadingZeros) {
        String dateStr;
        if (date == null) {
            dateStr = "";
        } else {
            GregorianCalendar calendar = new GregorianCalendar();
            calendar.setTime(date);
            int day = calendar.get(GregorianCalendar.DAY_OF_MONTH);
            int month = calendar.get(GregorianCalendar.MONTH) + 1;
            dateStr = (leadingZeros && day < 10 ? "0" : "") + day + ". " + (leadingZeros && month < 10 ? "0" : "") + month + ". " + calendar.get(GregorianCalendar.YEAR) + ".";
        }
        return dateStr;
    }

    public static String dateToString(Date date) {
        return dateToString(date, false);
    }

    public static Date stringToDate(String dateStr) {
        Date date;
        if (dateStr == null || dateStr.equals("")) {
            date = null;
        } else {
            dateStr = dateStr.trim();
            DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
            try {
                date = dateFormat.parse(dateStr);
            } catch (ParseException e) {
                date = null;
            }
        }
        return date;
    }

    public static int compareSafeDesc(Date date1, Date date2) {
        int cmp;
        if (date1 == date2) cmp = 0; else if (date1 == null || date2 == null) cmp = date1 == null ? -1 : 1; else cmp = date2.compareTo(date1);
        return cmp;
    }
}

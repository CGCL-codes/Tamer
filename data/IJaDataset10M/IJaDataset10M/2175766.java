package com.googlecode.cbrates.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author Alex Askerov
 */
public class DateUtils {

    public static Date dotStringToDate(String date) {
        String[] splitDate = date.split("\\.");
        int day = Integer.parseInt(splitDate[0]);
        int month = Integer.parseInt(splitDate[1]) - 1;
        int year = Integer.parseInt(splitDate[2]) - 1900;
        return new Date(year, month, day);
    }

    public static String dateToSlashString(Date date) {
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        return formatter.format(date);
    }
}

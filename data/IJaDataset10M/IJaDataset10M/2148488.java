package com.autoescola.core.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author leonardo luz fernandes
 * @version 0.1
 * @since 08/11/2010
 */
public class DateUtils {

    public static Date sysDate() {
        return new Date(System.currentTimeMillis());
    }

    public static String parseToDDMMYYY(Date date) {
        return parseTo(date, "dd/MM/yyyy");
    }

    public static String parseTo(Date date, String pattern) {
        DateFormat dt = new SimpleDateFormat(pattern);
        return dt.format(date);
    }
}

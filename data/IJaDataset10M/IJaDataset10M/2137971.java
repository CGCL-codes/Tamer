package com.ws.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 
 * DateUtil
 *
 */
public class DateUtil {

    /**
     * Converts a date into DD-MMM-YYYY format from any format.
     * @param date  The date in any format.
     * @return      The date in DD-MMM-YYYY format.
     */
    public static String toDDMMMYYYY(Date date) {
        SimpleDateFormat sd = new SimpleDateFormat("dd-MMM-yyyy");
        return sd.format(date);
    }

    /**
     * @param date asssumed to be of MM/dd/yyyy format
     * @return the date in 'dd MMM yyyy' format
     */
    public static String convertToDDMMMYYYY(String date) {
        SimpleDateFormat sd = new SimpleDateFormat("MM/dd/yyyy");
        date = sd.format(new Date(date));
        SimpleDateFormat ddmmmyyyyformat = new SimpleDateFormat("dd-MMM-yyyy");
        date = ddmmmyyyyformat.format(new Date(date));
        return date;
    }

    public static String convertToDDMMYYYY(Date date) {
        SimpleDateFormat sd = new SimpleDateFormat("MM/dd/yyyy");
        String strDate = sd.format(date);
        return strDate;
    }
}

package com.restsql.atom.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class RFC3339Date {

    public static java.util.Date parseRFC3339Date(String datestring) throws java.text.ParseException, IndexOutOfBoundsException {
        Date d = new Date();
        if (datestring.endsWith("Z")) {
            try {
                SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
                d = s.parse(datestring);
            } catch (java.text.ParseException pe) {
                SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'");
                s.setLenient(true);
                d = s.parse(datestring);
            }
            return d;
        }
        String firstpart = datestring.substring(0, datestring.lastIndexOf('-'));
        String secondpart = datestring.substring(datestring.lastIndexOf('-'));
        secondpart = secondpart.substring(0, secondpart.indexOf(':')) + secondpart.substring(secondpart.indexOf(':') + 1);
        datestring = firstpart + secondpart;
        SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
        try {
            d = s.parse(datestring);
        } catch (java.text.ParseException pe) {
            s = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSSZ");
            s.setLenient(true);
            d = s.parse(datestring);
        }
        return d;
    }

    /**
     * @author Jiannan Lu
     * @return rfc3339 Date
     */
    public static String getCurrentRFC3339Date() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        Date date = new Date();
        return dateFormat.format(date);
    }

    /**
     * @author Jiannan Lu
     * @return rfc3339 Date
     */
    public static String getRFC3339Date(Date d) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        return dateFormat.format(d);
    }
}

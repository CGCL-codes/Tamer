package com.jvantage.ce.common.util;

import java.util.Calendar;

/**
 *
 * @author dev
 */
public class UniqueNumberUtils {

    /** Creates a new instance of UniqueNumberUtils */
    public UniqueNumberUtils() {
    }

    /**
     *  Returns a unique serial number that can be used for any purpose.  Note,
     *  this method should not be used as a unique key generator.  It is intended
     *  for use in assigning Serial Numbers to products or license keys, etc.  The
     *  returned String is generated as follows:
     *
     *      LastTwoDigitsOfTheCurrentYear + CurrentMonthAsAHexValue + CurrentTimeInMilliseconds
     *
     *  The first three digits (year and month) are there so the time period that
     *  the key was generated in can be easily human recognizable.
     *
     *
     */
    public static String generateUniqueSerialNumber() {
        String systemTime = String.valueOf(System.currentTimeMillis());
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH) + 1;
        String monthHex = Integer.toHexString(month).toUpperCase();
        String yearString = String.valueOf(year);
        yearString = yearString.substring(yearString.length() - 2);
        StringBuffer buf = new StringBuffer();
        buf.append(yearString).append(monthHex).append(systemTime);
        String sn = SimpleStringEncoder.insertDelimiters(buf.toString(), 4);
        return sn;
    }
}

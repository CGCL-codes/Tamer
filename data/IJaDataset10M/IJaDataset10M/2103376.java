package org.dctmutils.common;

import java.util.Calendar;
import java.util.Date;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.documentum.fc.common.IDfTime;

/**
 * Utility methods for working with Dates.
 * 
 * @author <a href="mailto:luther@dctmutils.org">Luther E. Birdzell</a>
 */
public class DateHelper {

    private static Log log = LogFactory.getLog(DateHelper.class);

    /**
     * Creates a new <code>DateHelper</code> instance. All methods are static.
     */
    protected DateHelper() {
    }

    /**
     * Calculate the absolute value of the number of days between the two dates.
     * 
     * @param date1
     * @param date2
     * @return a <code>long</code> value
     */
    public static long getTimeDifferenceInDays(IDfTime date1, IDfTime date2) {
        Date d1 = date1.getDate();
        Date d2 = date2.getDate();
        return getTimeDifferenceInDays(d1, d2);
    }

    /**
     * Calculate the absolute value of the number of days between the two dates.
     * 
     * @param date1
     * @param date2
     * @return a <code>long</code> value
     */
    public static long getTimeDifferenceInDays(Date date1, Date date2) {
        Calendar c1 = Calendar.getInstance();
        c1.setTime(date1);
        Calendar c2 = Calendar.getInstance();
        c2.setTime(date2);
        return getTimeDifferenceInDays(c1, c2);
    }

    /**
     * Calculate the absolute value of the number of days between the two dates.
     * 
     * @param date1
     * @param date2
     * @return a <code>long</code> value
     */
    public static long getTimeDifferenceInDays(Calendar date1, Calendar date2) {
        long daysDiff = -1;
        long milliSecond1 = date1.getTime().getTime();
        long milliSecond2 = date2.getTime().getTime();
        long diff = Math.abs(milliSecond1 - milliSecond2);
        log.debug("diff = " + diff);
        daysDiff = diff / (1000 * 60 * 60 * 24);
        log.debug("daysDiff = " + daysDiff);
        return daysDiff;
    }
}

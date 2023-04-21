package org.plugger.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * A helper class to manipulate dates.
 *
 * @author "Antonio Begue"
 * @version $Revision: 1.0 $
 */
public class DateHelper {

    DateFormat dateFormat;

    public DateHelper() {
        this("yyyy-mm-dd");
    }

    /**
     * A constructor that receive the date format.<BR/>
     * Example of date format: <code>"yyyy-mm-dd"<code/>.
     * @param format
     */
    public DateHelper(String format) {
        dateFormat = new SimpleDateFormat(format);
    }

    /**
     * Return a string representing the current date.
     * @return The date string.
     */
    public String getDate() {
        String fecha = dateFormat.format(getCalendar().getTime());
        return fecha;
    }

    /**
     * Return a string representing the input date.
     * @param date The date to be converted to string.
     * @return The date string.
     */
    public String getDate(Date date) {
        String fecha = dateFormat.format(date);
        return fecha;
    }

    /**
     * Return the current calendar.
     * @return The Calendar object.
     */
    public Calendar getCalendar() {
        return Calendar.getInstance();
    }

    /**
     * Return a Date object representing the current date.
     * @return A Date object.
     */
    public Date getCurrentDate() {
        return getCalendar().getTime();
    }

    /**
     * Set the Calendar format.<BR/>
     * Example of date format: <code>"yyyy-mm-dd"<code/>.
     * @param format The format string.
     */
    public void setFormat(String format) {
        SimpleDateFormat df = (SimpleDateFormat) dateFormat;
        df.applyPattern(format);
    }

    /**
     * Add minutes to a date.
     * @param date The date to be modified.
     * @param minuts The minutes to add.
     * @return The modified Date object.
     */
    public Date addMinuts(Date date, int minuts) {
        Calendar cal = getCalendar();
        cal.setTime(date);
        cal.add(Calendar.MINUTE, minuts);
        return cal.getTime();
    }
}

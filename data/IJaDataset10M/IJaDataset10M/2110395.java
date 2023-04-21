package gnu.kinsight;

import java.io.*;
import java.util.Calendar;

/**
 * <code>Date</code> is a datastructure for Dates that doesn't require all
 * information to be available.  Unset data is stored as 0 (maybe not good for
 * years). 
 *
 * @author <a href="mailto:gann@pobox.com">Gann Bierner</a>
 * @version $Revision: 1.5 $
 * @see Serializable
 */
public class Date implements Serializable {

    int day, month, year;

    boolean approximation;

    public Date() {
        Calendar c = Calendar.getInstance();
        init(c);
    }

    public Date(long time) {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(time);
        init(c);
    }

    /**
     * Creates a new <code>Date</code> instance.
     *
     * @param d an <code>int</code>
     * @param m an <code>int</code>
     * @param y an <code>int</code>
     * @param approx a <code>boolean</code>
     */
    public Date(int d, int m, int y, boolean approx) {
        day = d;
        month = m;
        year = y;
        approximation = approx;
    }

    private void init(Calendar c) {
        day = c.get(Calendar.DATE);
        month = c.get(Calendar.MONTH) + 1;
        year = c.get(Calendar.YEAR);
    }

    /**
     * Creates a new <code>Date</code> instance.
     *
     * @param d day 
     * @param m month
     * @param y year
     */
    public Date(int d, int m, int y) {
        this(d, m, y, false);
    }

    /**
     * Creates a new <code>Date</code> instance.
     *
     * @param d day
     * @param m month
     * @param y year
     */
    public Date(String d, String m, String y) {
        this(d.equals("") ? 0 : Integer.parseInt(d), m.equals("") ? 0 : Integer.parseInt(m), y.equals("") ? 0 : Integer.parseInt(y));
    }

    /**
     * <code>getDay</code> returns the day
     *
     * @return an <code>int</code> value for the day (0 if unset)
     */
    public int getDay() {
        return day;
    }

    /**
     * <code>getMonth</code> returns the month
     *
     * @return an <code>int</code> value for the month (0 if unset)
     */
    public int getMonth() {
        return month;
    }

    /**
     * <code>getYear</code> returns the year
     *
     * @return an <code>int</code> value for the year (0 if unset)
     */
    public int getYear() {
        return year;
    }

    /**
     * Returns whether this date is just an approximation
     *
     * @return a <code>boolean</code>
     */
    public boolean isApproximation() {
        return approximation;
    }

    public Date floor() {
        return new Date(day == 0 ? 1 : day, month == 0 ? 1 : month, year);
    }

    public Date ceil() {
        return new Date(day == 0 ? 30 : day, month == 0 ? 12 : month, year);
    }

    public Date middle() {
        return new Date(day == 0 ? 15 : day, month == 0 ? 6 : month, year);
    }

    public static Date middle(Date d1, Date d2) {
        long t1 = d1.getTime();
        long t2 = d2.getTime();
        return new Date(t1 + (t2 - t1) / 2);
    }

    public long getTime() {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.DATE, day);
        c.set(Calendar.MONTH, month - 1);
        c.set(Calendar.YEAR, year);
        return c.getTimeInMillis();
    }

    public boolean isEmpty() {
        return day == 0 && month == 0 && year == 0;
    }

    public boolean before(Date d) {
        if (isEmpty() || d.isEmpty()) {
            return false;
        } else {
            if (getYear() != d.getYear()) {
                return getYear() < d.getYear();
            } else if (getMonth() != d.getMonth()) {
                return getMonth() < d.getMonth();
            } else {
                return getDay() < d.getDay();
            }
        }
    }

    public boolean after(Date d) {
        return d.before(this);
    }

    public boolean in(Date d) {
        return (getYear() == 0 || d.getYear() == 0 || getYear() == d.getYear()) && (getMonth() == 0 || d.getMonth() == 0 || getMonth() == d.getMonth()) && (getDay() == 0 || d.getDay() == 0 || getDay() == d.getDay());
    }

    /**
     * <code>toString</code> creates a very specific String for this date.
     * You don't want to use this if you want to be internationally friendly.
     * If not information is set, "?" is returned.  Otherwise, the format is
     * "month/day/year" with unset information removed.
     *
     * @return a <code>String</code> value
     */
    public String toString() {
        if (isEmpty()) return "?";
        String date = isApproximation() ? "~" : "";
        if (month != 0) date += month + "/";
        if (day != 0) date += day + "/";
        date += year;
        return date;
    }
}

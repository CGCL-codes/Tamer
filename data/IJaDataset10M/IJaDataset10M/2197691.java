package gov.lanl.Pids;

import java.awt.*;
import java.util.Date;
import java.util.Calendar;

/**
 * provides a date/time class to support the data in the TeleMed server
 * <p>
 * Date and time are saved together as a double, with the integer
 * part the Julian date, and the fractional part the time in hours
 * (24 hour standard) divided by 24, down to seconds.
 * Thus every time stamp in the TeleMed data base has date and time.
 * <p>
 * The julian conversions are modelled after "Numerical Recipes.."
 * by Press, Teukolsky, Vettering, and Flannery; Cambridge University
 * Press
 * <p>
 * Modified for PIDS time stamp
 *
 * @author Jim George
 * @version 10/15/97
 *
 * @log Revision 1.1  1997/10/15 Jim George
 * @log initial revision
 */
public class DateTime implements Cloneable {

    /**The month, 1-12*/
    int month;

    /**The day of the month, 1-31*/
    int day;

    /**The year, all digits*/
    int year;

    /**The hour of the day, 0-23*/
    int hour;

    /**The minutes of the hour, 0-59*/
    int min;

    /**The seconds of the minute, 0-59*/
    int sec;

    /**
  * creates a DateTime object with the current date time
  */
    public DateTime() {
        Calendar rightNow = Calendar.getInstance();
        year = rightNow.get(Calendar.YEAR);
        month = rightNow.get(Calendar.MONTH);
        day = rightNow.get(Calendar.DAY_OF_MONTH);
        hour = rightNow.get(Calendar.HOUR);
        min = rightNow.get(Calendar.MINUTE);
        sec = rightNow.get(Calendar.SECOND);
    }

    /**
   * creates a DateTime object with the requested values
   * @param     yr  year (full year, e.g., 1997)
   * @param     mo  month ( 1-12)
   * @param     da  date (1-31)
   * @param     hr  hour of day (0-23)
   * @param     mn  minute of hour (0-59)
   * @param     sc  second of minute (0-59)
   * @exception IllegalArgumentException if something wrong in date
   * @return    a DateTime object initialized to the requested values if valid
   */
    public DateTime(int yr, int mo, int da, int hr, int mn, int sc) {
        year = yr;
        month = mo;
        day = da;
        hour = hr;
        min = mn;
        sec = sc;
        if (!isValid()) throw new IllegalArgumentException();
    }

    /**
  * creates a DateTime object with the requested values, 0 for rest
  * @param     yr  year (full year, e.g., 1997)
  * @param     mo  month ( 1-12)
  * @param     da  date (1-31)
  * @exception IllegalArgumentException if something wrong in date
  * @return    a DateTime object initialized to the requested values if valid
  */
    public DateTime(int yr, int mo, int da) {
        this(yr, mo, da, 0, 0, 0);
    }

    /**
  * creates a DateTime object with the requested julian date
  *@param     julian  the date time in our julian expanded format, i.e. date/time
  *@exception IllegalArgumentException if something wrong
  *@return    a DateTime object initilalized to the requested julian date/time
  */
    public DateTime(int julian) {
        fromJulian(julian);
        if (!isValid()) throw new IllegalArgumentException();
    }

    /**
  * advances this day by n day
  * @param  n  the number of days by which to change this day, + or -
  */
    public void advance(int n) {
        fromJulian(toJulian() + n);
    }

    /**
  * returns the day of the month
  * @return  day of the month as an int, 1-31
  */
    public int getDay() {
        return day;
    }

    /**
  * returns the month
  * @return month as an int, 1-12
  */
    public int getMonth() {
        return month;
    }

    /**
  * returns the year
  * @return year as an int
  */
    public int getYear() {
        return year;
    }

    /**
  * returns the hours
  * @return hours as int
  */
    public int getHour() {
        return hour;
    }

    /**
  * returns the minutes
  * @return minutes as an int
  */
    public int getMin() {
        return min;
    }

    /**
  * returns the seconds
  * @return seconds as an int
  */
    public int getSecs() {
        return sec;
    }

    /**
  * returns the weekday
  * @return weekday (0-6, Sunday-Saturday)
  */
    public int weekday() {
        return (((int) toJulian() + 1) % 7);
    }

    /**
  * determines the days between this and the given parameter
  * @param   other  any date
  * @return  the number of days between this and the parameter
  *          positive if this is later than the parameter
  */
    public int daysBetween(DateTime other) {
        int ithis, iother;
        ithis = (int) toJulian();
        iother = (int) other.toJulian();
        return (ithis - iother);
    }

    /**
  * clones instances of this class
  * @return a clone of this object
  */
    public Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e) {
            return null;
        }
    }

    /**
  * determines if the date represented by this object is valid
  * @return <code>true</code> if object is valid; <code>false</code> otherwise
  */
    public boolean isValid() {
        DateTime dt = new DateTime();
        dt.fromJulian(this.toJulian());
        return dt.day == day && dt.month == month && dt.year == year && dt.hour == hour && dt.min == min && dt.sec == sec;
    }

    /**
  * returns julian day as integer
  * @return the julian day as the integer part and the 24hr time as fractional
  * <p>
  * Example: <i>2440000 was 5/23/68 a Thursday</i>
  */
    public double toJulian() {
        int y = year;
        int m = month;
        double julian, timeofday;
        int ijulian;
        int IGREG = 15 + 31 * (10 + 12 * 1582);
        int adj;
        if (y < 0) y = y + 1;
        if (m > 2) m = m + 1; else {
            y = y - 1;
            m = m + 13;
        }
        ijulian = (int) (365.25 * y) + (int) (30.6001 * m) + day + 1720995;
        if (day + 31 * (m + 12 * y) >= IGREG) {
            adj = y / 100;
            ijulian = ijulian + 2 - adj + adj / 4;
        }
        timeofday = hour / 24.0 + min / 1440.0 + sec / 86400.0;
        return (ijulian + timeofday);
    }

    /**
  * converts a TeleMed julian (both date & time) to a DateTime;
  * the DataTime object exists and its values are changed
  * @param  julian  the TeleMed julian daytime number
  */
    public void fromJulian(double julian) {
        int ja, jalpha, jb, jc, jd, je;
        int JGREG = 2299161;
        ja = (int) julian;
        if (ja >= JGREG) {
            jalpha = (int) (((ja - 1867216) - 0.25) / 36524.25);
            ja = ja + 1 + jalpha - jalpha / 4;
        }
        jb = ja + 1524;
        jc = (int) (6680.0 + ((jb - 2439870) - 122.1) / 365.25);
        jd = 365 * jc + jc / 4;
        je = (int) ((jb - jd) / 30.6001);
        day = jb - jd - (int) (30.6001 * je);
        month = je - 1;
        if (month > 12) month = month - 12;
        year = jc - 4715;
        if (month > 2) year = year - 1;
        if (year <= 0) year = year - 1;
        double thetime = julian - (int) julian;
        thetime = thetime * 24;
        hour = (int) thetime;
        thetime = (thetime - hour) * 60;
        min = (int) thetime;
        thetime = (thetime - min) * 60;
        sec = (int) thetime;
    }

    /**
  * converts from PID time to the DateTime class
  * @param  thepidtime  the pid time as a long, has both date and time
  */
    public void fromPIDTime(long thepidtime) {
        double jul;
        jul = (double) thepidtime;
        jul = jul / 10000000 / 60 / 60 / 24;
        fromJulian(jul);
    }

    /**
  * converts the DateTime to PID time
  * @return the PID time as a long, both date and time
  */
    public long toPIDTime() {
        double jul = toJulian();
        long pidtime = (long) jul * 24 * 60 * 60 * 10000000;
        return pidtime;
    }
}

package sale;

import java.util.*;

/**
  * This class is an implementation of the {@link Time Time} interface.
  *
  * <p>The time is represented as a Gregorian Calendar.
  *
  * <p>Only a simple Adapterclass for java.util.GregorianCalendar
  *
  * @author Thomas Medack
  * @version 2.0 29/11/2000
  * @since v2.0
  */
public class CalendarTime extends Object implements Time {

    /**
    * The Calender containing the current date
    *
    * @serial
    */
    private Calendar m_cCalendar;

    /**
    * The default interval for timesteps
    *
    * @serial
    */
    private int m_iDefaultInterval = 1;

    /**
    * The field which {@link #goAhead} will increase.
    *
    * @see #YEAR
    * @see #MONTH
    * @see #DATE
    * @see #HOUR
    * @see #MINUTE
    * @see #SECOND
    *
    * @serial
    */
    private int m_iTimeToCount;

    /**
    * Creates a new CalendarTime with the current systemtime.
    */
    public CalendarTime() {
        this(System.currentTimeMillis());
    }

    /**
    * Creates a new CalendarTime with the given starting time.
    *
    * @param lTimeInMillis a long representing the time in milliseconds
    */
    public CalendarTime(long lTimeInMillis) {
        this(lTimeInMillis, DATE);
    }

    /**
    * Creates a new CalendarTime with the given starting time and an int that defines the field which will
    * be increased by {@link #goAhead}.
    * @see #YEAR
    * @see #MONTH
    * @see #DATE
    * @see #HOUR
    * @see #MINUTE
    * @see #SECOND
    *
    * @param lTimeInMillis a long representing the time in milliseconds
    * @param iTimeToCount an int representing the field which will be increased by {@link #goAhead}
    */
    public CalendarTime(long lTimeInMillis, int iTimeToCount) {
        m_cCalendar = new GregorianCalendar(TimeZone.getTimeZone("europe/Berlin"), Locale.GERMAN);
        m_cCalendar.setTime(new java.util.Date(lTimeInMillis));
        m_iTimeToCount = iTimeToCount;
    }

    /**
    * Creates a new CalendarTime with the given starting time
    *
    * @param year an int representing the Year you want to start with
    * @param month an int representing the Month you want to start with
    * @param date an int representing the Day you want to start with
    * @param hours an int representing the Hour you want to start with
    * @param minutes an int representing the Minute you want to start with
    * @param seconds an int representing the Second you want to start with
    */
    public CalendarTime(int year, int month, int date, int hours, int minutes, int seconds) {
        this(year, month, date, hours, minutes, seconds, DATE);
    }

    /**
    * Creates a new CalendarTime with the given starting time and an int that defines the field which will
    * be increased by {@link #goAhead}.
    *
    * @see #YEAR
    * @see #MONTH
    * @see #DATE
    * @see #HOUR
    * @see #MINUTE
    * @see #SECOND
    *
    * @param year an int representing the Year you want to start with
    * @param month an int representing the Month you want to start with
    * @param date an int representing the Day you want to start with
    * @param hours an int representing the Hour you want to start with
    * @param minutes an int representing the Minute you want to start with
    * @param seconds an int representing the Second you want to start with
    * @param iTimeToCount an int representing the field which will be increased by {@link #goAhead}
    */
    public CalendarTime(int year, int month, int date, int hours, int minutes, int seconds, int iTimeToCount) {
        m_cCalendar = new GregorianCalendar(year, month, date, hours, minutes, seconds);
        m_cCalendar.setTimeZone(TimeZone.getTimeZone("europe/Berlin"));
        m_iTimeToCount = iTimeToCount;
    }

    /**
    * Set the given date.
    *
    * @override Never
    *
    * @param oTime a java.util.Date representing the date to be set
    *
    * @exception IllegalArgumentException if Parameter is not a java.util.Date
    */
    public void setTime(Object oTime) throws IllegalArgumentException {
        try {
            m_cCalendar.setTime((java.util.Date) oTime);
        } catch (ClassCastException cce) {
            throw new IllegalArgumentException("Parameter has to be of type java.util.Date.");
        }
    }

    /**
    * Set an int that will define which time field will be increased by {@link #goAhead}.
    *
    * @override Never
    *
    * @param iTime an int representing a constant
    *
    * @see #YEAR
    * @see #MONTH
    * @see #DATE
    * @see #HOUR
    * @see #MINUTE
    * @see #SECOND
    */
    public void setTimeToCount(int iTime) {
        m_iTimeToCount = iTime;
    }

    /**
    * Get the calendars date
    *
    * @override Never
    *
    * @return a java.util.Date representing the calendars date
    */
    public Object getTime() {
        return m_cCalendar.getTime();
    }

    /**
    * Increment the time by the given time interval.
    *
    * @override Never
    *
    * @param oInterval the interval by which to increment time. Must be an Integer object that gives the
    * number of days by which to increment.
    *
    * @exception IllegalArgumentException if the given interval is no Integer or is null.
    */
    public void goAhead(Object oInterval) throws IllegalArgumentException {
        try {
            m_cCalendar.add(m_iTimeToCount, ((Integer) oInterval).intValue());
        } catch (NullPointerException npe) {
            throw new IllegalArgumentException("Interval must be specified in calls to goAhead!");
        } catch (ClassCastException cce) {
            throw new IllegalArgumentException("Parameter has to be of type long");
        }
    }

    /**
    * Get the default time interval.
    *
    * @override Never
    *
    * @return an Integer describing the default time step.
    */
    public Object getDefaultInterval() {
        return new Integer(m_iDefaultInterval);
    }

    /**
    * Get the adapted Calendar.
    *
    * @override Never
    *
    * @return a Calendar object describing the calendar of this class.
    */
    public Calendar getCalendar() {
        return m_cCalendar;
    }

    /**
    * Left-pad the given number with 0 to fill a two digit string.
    *
    * <p>Helper function used by {@link #getTimeStamp}.</p>
    */
    private String getCorrectString(int iTime) {
        if (Integer.toString(iTime).length() == 1) return ("0" + Integer.toString(iTime));
        return Integer.toString(iTime);
    }

    /**
    * Create and return a time stamp.
    *
    * @override Never
    *
    * @param lStampNumber the number of the stamp
    *
    * @return a fresh time stamp.
    */
    public Comparable getTimeStamp(long lStampNumber) {
        String sReturn = ("0000000000000000000" + Long.toString(lStampNumber)).substring(Long.toString(lStampNumber).length());
        sReturn = (getCorrectString(m_cCalendar.get(SECOND))) + sReturn;
        sReturn = (getCorrectString(m_cCalendar.get(MINUTE))) + sReturn;
        sReturn = (getCorrectString(m_cCalendar.get(HOUR) + m_cCalendar.get(Calendar.ZONE_OFFSET) / (1000 * 60 * 60))) + sReturn;
        sReturn = (getCorrectString(m_cCalendar.get(DATE))) + sReturn;
        sReturn = (getCorrectString(m_cCalendar.get(MONTH) + 1)) + sReturn;
        sReturn = (Integer.toString(m_cCalendar.get(YEAR))) + sReturn;
        return sReturn;
    }

    /**
    * Field number for {@link #setTimeToCount} indicates that the seconds will be increased by {@link #goAhead}
    */
    public static int SECOND = Calendar.SECOND;

    /**
    * Field number for {@link #setTimeToCount} indicates that the minutes will be increased by {@link #goAhead}
    */
    public static int MINUTE = Calendar.MINUTE;

    /**
    * Field number for {@link #setTimeToCount} indicates that the hours will be increased by {@link #goAhead}
    */
    public static int HOUR = Calendar.HOUR_OF_DAY;

    /**
    * Field number for {@link #setTimeToCount} indicates that the days will be increased by {@link #goAhead}
    */
    public static int DATE = Calendar.DATE;

    /**
    * Field number for {@link #setTimeToCount} indicates that the month will be increased by {@link #goAhead}
    */
    public static int MONTH = Calendar.MONTH;

    /**
    * Field number for {@link #setTimeToCount} indicates that the years will be increased by {@link #goAhead}
    */
    public static int YEAR = Calendar.YEAR;
}

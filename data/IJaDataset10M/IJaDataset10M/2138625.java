package org.apache.axis.types;

import org.apache.axis.utils.Messages;
import java.util.Calendar;

/**
 * Implementation of the XML Schema type duration. Duration supports a minimum
 * fractional second precision of milliseconds.
 *
 * @author Wes Moulder <wes@themindelectric.com>
 * @author Dominik Kacprzak (dominik@opentoolbox.com)
 * @see <a href="http://www.w3.org/TR/xmlschema-2/#duration">XML Schema 3.2.6</a>
 */
public class Duration implements java.io.Serializable {

    boolean isNegative = false;

    int years;

    int months;

    int days;

    int hours;

    int minutes;

    double seconds;

    /**
     * Default no-arg constructor
     */
    public Duration() {
    }

    /**
     * @param negative
     * @param aYears
     * @param aMonths
     * @param aDays
     * @param aHours
     * @param aMinutes
     * @param aSeconds
     */
    public Duration(boolean negative, int aYears, int aMonths, int aDays, int aHours, int aMinutes, double aSeconds) {
        isNegative = negative;
        years = aYears;
        months = aMonths;
        days = aDays;
        hours = aHours;
        minutes = aMinutes;
        setSeconds(aSeconds);
    }

    /**
     * Constructs Duration from a String in an xsd:duration format -
     * PnYnMnDTnHnMnS.
     *
     * @param duration String
     * @throws SchemaException if the string doesn't parse correctly.
     */
    public Duration(String duration) throws IllegalArgumentException {
        int position = 1;
        int timePosition = duration.indexOf("T");
        if (duration.indexOf("P") == -1 || duration.equals("P")) {
            throw new IllegalArgumentException(Messages.getMessage("badDuration"));
        }
        if (duration.lastIndexOf("T") == duration.length() - 1) {
            throw new IllegalArgumentException(Messages.getMessage("badDuration"));
        }
        if (duration.startsWith("-")) {
            isNegative = true;
            position++;
        }
        if (timePosition != -1) {
            parseTime(duration.substring(timePosition + 1));
        } else {
            timePosition = duration.length();
        }
        if (position != timePosition) {
            parseDate(duration.substring(position, timePosition));
        }
    }

    /**
     * Constructs Duration from a Calendar.
     *
     * @param calendar Calendar
     * @throws IllegalArgumentException if the calendar object does not
     * represent any date nor time.
     */
    public Duration(boolean negative, Calendar calendar) throws IllegalArgumentException {
        this.isNegative = negative;
        this.years = calendar.get(Calendar.YEAR);
        this.months = calendar.get(Calendar.MONTH);
        this.days = calendar.get(Calendar.DATE);
        this.hours = calendar.get(Calendar.HOUR);
        this.minutes = calendar.get(Calendar.MINUTE);
        this.seconds = calendar.get(Calendar.SECOND);
        this.seconds += ((double) calendar.get(Calendar.MILLISECOND)) / 100;
        if (years == 0 && months == 0 && days == 0 && hours == 0 && minutes == 0 && seconds == 0) {
            throw new IllegalArgumentException(Messages.getMessage("badCalendarForDuration"));
        }
    }

    /**
     * This method parses the time portion of a String that represents
     * xsd:duration - nHnMnS.
     *
     * @param time
     * @throws IllegalArgumentException if time does not match pattern
     *
     */
    public void parseTime(String time) throws IllegalArgumentException {
        if (time.length() == 0 || time.indexOf("-") != -1) {
            throw new IllegalArgumentException(Messages.getMessage("badTimeDuration"));
        }
        if (!time.endsWith("H") && !time.endsWith("M") && !time.endsWith("S")) {
            throw new IllegalArgumentException(Messages.getMessage("badTimeDuration"));
        }
        try {
            int start = 0;
            int end = time.indexOf("H");
            if (start == end) {
                throw new IllegalArgumentException(Messages.getMessage("badTimeDuration"));
            }
            if (end != -1) {
                hours = Integer.parseInt(time.substring(0, end));
                start = end + 1;
            }
            end = time.indexOf("M");
            if (start == end) {
                throw new IllegalArgumentException(Messages.getMessage("badTimeDuration"));
            }
            if (end != -1) {
                minutes = Integer.parseInt(time.substring(start, end));
                start = end + 1;
            }
            end = time.indexOf("S");
            if (start == end) {
                throw new IllegalArgumentException(Messages.getMessage("badTimeDuration"));
            }
            if (end != -1) {
                setSeconds(Double.parseDouble(time.substring(start, end)));
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(Messages.getMessage("badTimeDuration"));
        }
    }

    /**
     * This method parses the date portion of a String that represents
     * xsd:duration - nYnMnD.
     *
     * @param date
     * @throws IllegalArgumentException if date does not match pattern
     *
     */
    public void parseDate(String date) throws IllegalArgumentException {
        if (date.length() == 0 || date.indexOf("-") != -1) {
            throw new IllegalArgumentException(Messages.getMessage("badDateDuration"));
        }
        if (!date.endsWith("Y") && !date.endsWith("M") && !date.endsWith("D")) {
            throw new IllegalArgumentException(Messages.getMessage("badDateDuration"));
        }
        try {
            int start = 0;
            int end = date.indexOf("Y");
            if (start == end) {
                throw new IllegalArgumentException(Messages.getMessage("badDateDuration"));
            }
            if (end != -1) {
                years = Integer.parseInt(date.substring(0, end));
                start = end + 1;
            }
            end = date.indexOf("M");
            if (start == end) {
                throw new IllegalArgumentException(Messages.getMessage("badDateDuration"));
            }
            if (end != -1) {
                months = Integer.parseInt(date.substring(start, end));
                start = end + 1;
            }
            end = date.indexOf("D");
            if (start == end) {
                throw new IllegalArgumentException(Messages.getMessage("badDateDuration"));
            }
            if (end != -1) {
                days = Integer.parseInt(date.substring(start, end));
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(Messages.getMessage("badDateDuration"));
        }
    }

    /**
     *
     */
    public boolean isNegative() {
        return isNegative;
    }

    /**
     *
     */
    public int getYears() {
        return years;
    }

    /**
     *
     */
    public int getMonths() {
        return months;
    }

    /**
     *
     */
    public int getDays() {
        return days;
    }

    /**
     *
     */
    public int getHours() {
        return hours;
    }

    /**
     *
     */
    public int getMinutes() {
        return minutes;
    }

    /**
     *
     */
    public double getSeconds() {
        return seconds;
    }

    /**
     * @param negative
     */
    public void setNegative(boolean negative) {
        isNegative = negative;
    }

    /**
     * @param years
     */
    public void setYears(int years) {
        this.years = years;
    }

    /**
     * @param months
     */
    public void setMonths(int months) {
        this.months = months;
    }

    /**
     * @param days
     */
    public void setDays(int days) {
        this.days = days;
    }

    /**
     * @param hours
     */
    public void setHours(int hours) {
        this.hours = hours;
    }

    /**
     * @param minutes
     */
    public void setMinutes(int minutes) {
        this.minutes = minutes;
    }

    /**
     * @param seconds
     * @deprecated use {@link #setSeconds(double) setSeconds(double)}
     * instead
     */
    public void setSeconds(int seconds) {
        this.seconds = seconds;
    }

    /**
     * Sets the seconds. NOTE: The fractional value of seconds is rounded up to
     * milliseconds.
     *
     * @param seconds double
     */
    public void setSeconds(double seconds) {
        this.seconds = ((double) (Math.round(seconds * 100))) / 100;
    }

    /**
     * This returns the xml representation of an xsd:duration object.
     */
    public String toString() {
        StringBuffer duration = new StringBuffer();
        duration.append("P");
        if (years != 0) {
            duration.append(years + "Y");
        }
        if (months != 0) {
            duration.append(months + "M");
        }
        if (days != 0) {
            duration.append(days + "D");
        }
        if (hours != 0 || minutes != 0 || seconds != 0.0) {
            duration.append("T");
            if (hours != 0) {
                duration.append(hours + "H");
            }
            if (minutes != 0) {
                duration.append(minutes + "M");
            }
            if (seconds != 0) {
                if (seconds == (int) seconds) {
                    duration.append((int) seconds + "S");
                } else {
                    duration.append(seconds + "S");
                }
            }
        }
        if (duration.length() == 1) {
            duration.append("T0S");
        }
        if (isNegative) {
            duration.insert(0, "-");
        }
        return duration.toString();
    }

    /**
     * The equals method compares the time represented by duration object, not
     * its string representation.
     * Hence, a duration object representing 65 minutes is considered equal to a
     * duration object representing 1 hour and 5 minutes.
     *
     * @param object
     */
    public boolean equals(Object object) {
        if (!(object instanceof Duration)) {
            return false;
        }
        Calendar thisCalendar = this.getAsCalendar();
        Duration duration = (Duration) object;
        return this.isNegative == duration.isNegative && this.getAsCalendar().equals(duration.getAsCalendar());
    }

    public int hashCode() {
        int hashCode = 0;
        if (isNegative) {
            hashCode++;
        }
        hashCode += years;
        hashCode += months;
        hashCode += days;
        hashCode += hours;
        hashCode += minutes;
        hashCode += seconds;
        hashCode += (seconds * 100) % 100;
        return hashCode;
    }

    /**
     * Returns duration as a calendar.  Due to the way a Calendar class works,
     * the values for particular fields may not be the same as obtained through
     * getter methods.  For example, if a duration's object getMonths
     * returns 20, a similar call on a calendar object will return 1 year and
     * 8 months.
     *
     * @return Calendar
     */
    public Calendar getAsCalendar() {
        return getAsCalendar(Calendar.getInstance());
    }

    /**
     * Returns duration as a calendar.  Due to the way a Calendar class works,
     * the values for particular fields may not be the same as obtained through
     * getter methods.  For example, if a Duration's object getMonths
     * returns 20, a similar call on a Calendar object will return 1 year and
     * 8 months.
     *
     * @param startTime Calendar
     * @return Calendar
     */
    public Calendar getAsCalendar(Calendar startTime) {
        Calendar ret = (Calendar) startTime.clone();
        ret.set(Calendar.YEAR, years);
        ret.set(Calendar.MONTH, months);
        ret.set(Calendar.DATE, days);
        ret.set(Calendar.HOUR, hours);
        ret.set(Calendar.MINUTE, minutes);
        ret.set(Calendar.SECOND, (int) seconds);
        ret.set(Calendar.MILLISECOND, (int) (seconds * 100 - Math.round(seconds) * 100));
        return ret;
    }
}

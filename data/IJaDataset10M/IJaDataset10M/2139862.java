package de.bea.domingo.util;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

/**
 * A date-only Gregorian calendar.
 *
 * @author <a href=mailto:kriede@users.sourceforge.net>Kurt Riede</a>
 */
public final class GregorianDate extends GregorianCalendar {

    /** A comparator for instances of type {@link GregorianDate}. */
    public static final Comparator COMPARATOR = new GregorianDateComparator();

    /** serial version ID for serialization. */
    private static final long serialVersionUID = 1L;

    /** Default time zone: Greenwich meantime. */
    private static final TimeZone GMT = TimeZone.getTimeZone("GMT");

    /**
     * Returns a {@link GregorianDate}. If the given calendar is not a
     * <tt>GregorianDate</tt>, a new <tt>GregorianDate</tt> is returned,
     * else the given calendar is returned unchanged.
     *
     * @param calendar any calendar instance
     * @return a <tt>GregorianDate</tt>
     * @see #newInstance(Calendar)
     */
    public static GregorianDate getInstance(final Calendar calendar) {
        return calendar instanceof GregorianDate ? (GregorianDate) calendar : new GregorianDate(calendar);
    }

    /**
     * Returns a new {@link GregorianDate}. Even if the given calendar
     * already is a <tt>GregorianDate</tt>, a new <tt>GregorianDate</tt> is
     * returned.
     *
     * @param calendar any calendar instance
     * @return a new <tt>GregorianDate</tt>
     * @see #getInstance(Calendar)
     */
    public static GregorianDate newInstance(final Calendar calendar) {
        return new GregorianDate(calendar);
    }

    /**
     * Default Constructor.
     */
    public GregorianDate() {
        super(GMT);
        clearFields();
    }

    /**
     * Creates a new Gregorian date from a given <code>java.util.Date</code>.
     *
     * @param date the date for the new calendar
     */
    public GregorianDate(final Date date) {
        super(GMT);
        if (date != null) {
            setTime(date);
        }
        clearFields();
    }

    /**
     * Creates a new Gregorian date from a given <code>java.util.Calendar</code>.
     *
     * @param calendar the original calendar for the new calendar
     */
    public GregorianDate(final Calendar calendar) {
        super(GMT);
        if (calendar != null) {
            set(Calendar.ERA, calendar.get(Calendar.ERA));
            set(Calendar.YEAR, calendar.get(Calendar.YEAR));
            set(Calendar.MONTH, calendar.get(Calendar.MONTH));
            set(Calendar.DATE, calendar.get(Calendar.DATE));
        }
        clearFields();
    }

    /**
     * Creates a new Gregorian date from given year, month and date.
     *
     * <p>The first month of the year is <code>JANUARY</code> which is 0; the
     * last month is <code>DEDCEMBER</code> which is 11.</p>
     *
     * @param year the year of the new calendar
     * @param month the month of the new calendar
     * @param date the day of the new calendar
     */
    public GregorianDate(final int year, final int month, final int date) {
        super(GMT);
        set(Calendar.YEAR, year);
        set(Calendar.MONTH, month);
        set(Calendar.DATE, date);
        clearFields();
    }

    /**
     * {@inheritDoc}
     *
     * @see java.util.Calendar#get(int)
     */
    public int get(final int field) {
        if (isTimeField(field)) {
            return 0;
        }
        return super.get(field);
    }

    /**
     * Overwrite prevents setting time fields.
     *
     * @param field the given calendar field.
     * @param value the value to be set for the given calendar field.
     *
     * @see java.util.Calendar#set(int, int)
     */
    public void set(final int field, final int value) {
        if (isTimeField(field)) {
            return;
        }
        super.set(field, value);
        super.get(Calendar.DAY_OF_WEEK);
    }

    /**
     * Overwrite to prevent setting time fields.
     *
     * @param field the time field.
     * @param amount the amount of date or time to be added to the field.
     *
     * @see java.util.GregorianCalendar#add(int, int)
     */
    public void add(final int field, final int amount) {
        if (isTimeField(field)) {
            return;
        }
        super.add(field, amount);
    }

    /**
     * Checks if a given field number indicating a time field.
     * @param field field index
     * @return <code>true</code> if the field number indicating a time field, else <code>false</code>
     */
    private boolean isTimeField(final int field) {
        if (field == Calendar.HOUR || field == Calendar.HOUR_OF_DAY || field == Calendar.AM_PM) {
            return true;
        }
        if (field == Calendar.MINUTE || field == Calendar.SECOND) {
            return true;
        }
        if (field == Calendar.MILLISECOND) {
            return true;
        }
        return false;
    }

    /**
     * Overwrite prevents setting a time zone to keep the time unchanged and unavailable.
     *
     * @param zone the new time zone (ignored)
     *
     * @see java.util.Calendar#setTimeZone(java.util.TimeZone)
     */
    public void setTimeZone(final TimeZone zone) {
    }

    /**
     * Overwrites {@link Calendar#setTimeInMillis(long)}, to disable all time fields.
     *
     * @param millis the new time in UTC milliseconds from the epoch.
     * @see java.util.Calendar#setTimeInMillis(long)
     */
    public void setTimeInMillis(final long millis) {
        super.setTimeInMillis(millis);
        clearFields();
    }

    /**
     * Clears all time fields.
     */
    private void clearFields() {
        clearIfNeeded(AM_PM);
        clearIfNeeded(HOUR);
        clearIfNeeded(HOUR_OF_DAY);
        clearIfNeeded(MINUTE);
        clearIfNeeded(SECOND);
        clearIfNeeded(MILLISECOND);
    }

    /**
     * Clears a field if it is set.
     *
     * @param field the time field to be cleared.
     */
    private void clearIfNeeded(final int field) {
        if (isSet(field)) {
            clear(field);
        }
    }

    /**
     * Returns the month of the calendar.
     *
     * @return the month
     */
    public int getMonth() {
        return get(Calendar.MONTH);
    }

    /**
     * Returns the day of the calendar.
     *
     * @return the day
     */
    public int getDay() {
        return get(Calendar.DATE);
    }

    /**
     * Returns the day of the calendar.
     *
     * @return the day
     */
    public int getDate() {
        return get(Calendar.DATE);
    }

    /**
     * Returns the year of the calendar.
     *
     * @return the year
     */
    public int getYear() {
        return get(Calendar.YEAR);
    }

    /**
     * {@inheritDoc}
     *
     * @see java.util.Calendar#toString()
     */
    public String toString() {
        return DateUtil.getDateString(this);
    }

    /**
     * Compares this <tt>GregorianDate</tt> to an object reference.
     *
     * @param obj the object reference with which to compare
     * @return <code>true</code> if this object is equal to <code>obj</code>,
     *         otherwise <code>false</code>
     */
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof GregorianCalendar)) {
            return false;
        }
        Calendar that = (Calendar) obj;
        return get(ERA) == that.get(ERA) && get(YEAR) == that.get(YEAR) && get(MONTH) == that.get(MONTH) && get(DATE) == that.get(DATE) && isLenient() == that.isLenient() && getFirstDayOfWeek() == that.getFirstDayOfWeek() && getMinimalDaysInFirstWeek() == that.getMinimalDaysInFirstWeek() && getTimeZone().equals(that.getTimeZone());
    }

    /**
     * Generates the hash code for a GregorianDate object.
     *
     * @return a hash code value for this object.
     */
    public int hashCode() {
        return super.hashCode() + 1;
    }

    /**
     * Comparator for gregorian dates.
     */
    private static final class GregorianDateComparator implements Comparator, Serializable {

        /** serial version ID for serialization. */
        private static final long serialVersionUID = 1L;

        /**
         * {@inheritDoc}
         *
         * This implementation allows all instances of type
         * {@link GregorianDate} and any sub classes as arguments, but only
         * compares the date fields {@link Calendar#YEAR},
         * {@link Calendar#MONTH} and {@link Calendar#DATE}.
         *
         * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
         */
        public int compare(final Object o1, final Object o2) {
            return GregorianDate.compare(o1, o2);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    public int compareTo(final Object o) {
        return GregorianDate.compare(this, o);
    }

    /**
     * Compares two gregorian dates. This implementation allows all instances of
     * type {@link GregorianDate} and any sub classes as arguments, but only
     * compares the date fields {@link Calendar#YEAR}, {@link Calendar#MONTH}
     * and {@link Calendar#DATE}.
     *
     * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
     */
    private static int compare(final Object o1, final Object o2) {
        if (o1 == null && o2 == null) {
            return 0;
        } else if (o1 == null || o2 == null) {
            return o1 == null ? -1 : 1;
        } else if (!(o1 instanceof GregorianCalendar) || !(o2 instanceof GregorianCalendar)) {
            throw new IllegalArgumentException("Both arguments must be of type GregorianCalendar, but was " + o1.getClass().getName() + ", " + o2.getClass().getName());
        } else if (((Calendar) o1).get(Calendar.ERA) != ((Calendar) o2).get(Calendar.ERA)) {
            return ((Calendar) o1).get(Calendar.ERA) - ((Calendar) o2).get(Calendar.ERA);
        } else if (((Calendar) o1).get(Calendar.YEAR) != ((Calendar) o2).get(Calendar.YEAR)) {
            return ((Calendar) o1).get(Calendar.YEAR) - ((Calendar) o2).get(Calendar.YEAR);
        } else if (((Calendar) o1).get(Calendar.MONTH) != ((Calendar) o2).get(Calendar.MONTH)) {
            return ((Calendar) o1).get(Calendar.MONTH) - ((Calendar) o2).get(Calendar.MONTH);
        } else if (((Calendar) o1).get(Calendar.DATE) != ((Calendar) o2).get(Calendar.DATE)) {
            return ((Calendar) o1).get(Calendar.DATE) - ((Calendar) o2).get(Calendar.DATE);
        }
        return 0;
    }
}

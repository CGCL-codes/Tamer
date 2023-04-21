package de.ufinke.cubaja.util;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Extension of <tt>GregorianCalendar</tt>.
 * This class offers date calculations on a day basis.
 * Time components like hour, minute and second are stripped in the constructors
 * so that the day represents the time at midnight.
 * @author Uwe Finke
 */
public class Day extends GregorianCalendar implements Externalizable {

    /**
   * Creates a <tt>Date</tt> set to midnight of the current day.
   * @return today <tt>Day</tt> instance
   */
    public static Date today() {
        return new Day().date();
    }

    /**
   * Creates a <tt>Day</tt> initialized to midnight of the current day.
   */
    public Day() {
        stripTime();
    }

    /**
   * Creates a <tt>Day</tt> initialized to midnight of the supplied date.
   * @param date
   */
    public Day(Date date) {
        setTime(date);
        stripTime();
    }

    /**
   * Creates a <tt>Day</tt> initialized to midnight of the supplied calendar object.
   * @param calendar
   */
    public Day(Calendar calendar) {
        setTimeInMillis(calendar.getTimeInMillis());
        stripTime();
    }

    /**
   * Creates a <tt>Day</tt> initialized to midnight of the supplied time.
   * @param millis
   */
    public Day(long millis) {
        setTimeInMillis(millis);
        stripTime();
    }

    /**
   * Creates a <tt>Day</tt> initialized to the specified date.
   * Note that <tt>month</tt> starts with <tt>1</tt> for january.
   * @param year
   * @param month
   * @param day
   */
    public Day(int year, int month, int day) {
        set(YEAR, year);
        set(MONTH, month - 1);
        set(DAY_OF_MONTH, day);
        stripTime();
    }

    private void stripTime() {
        set(HOUR_OF_DAY, 0);
        set(MINUTE, 0);
        set(SECOND, 0);
        set(MILLISECOND, 0);
    }

    /**
   * Returns a string with format <tt>yyyy-MM-dd</tt>.
   */
    public String toString() {
        return format("yyyy-MM-dd");
    }

    /**
   * Creates a copy of this object.
   */
    public Day clone() {
        return new Day(getTimeInMillis());
    }

    /**
   * Writes this objects values to a stream.
   */
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeShort(get(YEAR));
        out.writeByte(get(MONTH));
        out.writeByte(get(DAY_OF_MONTH));
    }

    /**
   * Reads this objects values from a stream.
   */
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        clear();
        set(YEAR, in.readShort());
        set(MONTH, in.readByte());
        set(DAY_OF_MONTH, in.readByte());
    }

    /**
   * Compares this object to a <tt>Date</tt>.
   * The comparation takes place without the time components of the date.
   * @param date
   * @return see <tt>java.util.Comparator</tt>
   */
    public int compareTo(Date date) {
        return compareTo(new Day(date));
    }

    /**
   * Returns this object's year value.
   * @return year
   */
    public int year() {
        return get(YEAR);
    }

    /**
   * Returns this object's month value, ranging from <tt>1</tt> to <tt>12</tt>.
   * @return month
   */
    public int month() {
        return get(MONTH) + 1;
    }

    /**
   * Returns this object's day of month value.
   * @return day of month
   */
    public int day() {
        return get(DAY_OF_MONTH);
    }

    /**
   * Returns this day as date.
   * @return date
   */
    public Date date() {
        return getTime();
    }

    /**
   * Returns this day as a SQL date.
   * @return SQLDate
   */
    public java.sql.Date getSqlDate() {
        return new java.sql.Date(getTimeInMillis());
    }

    /**
   * Returns the weekday of this day.
   * @return weekday
   */
    public Weekday getWeekday() {
        return Weekday.getWeekday(this);
    }

    /**
   * Formats this day according to the specified pattern.
   * For pattern see <tt>java.text.SimpleDateFormat</tt>.
   * @param pattern
   * @return formatted string
   */
    public String format(String pattern) {
        return Util.format(getTime(), pattern);
    }

    /**
   * Determines whether this day is a workday.
   * @param config
   * @return flag
   */
    public boolean isWorkday(HolidayConfig config) {
        return config.isWorkday(this);
    }

    /**
   * Determines whether this day is a holiday.
   * @param config
   * @return flag
   */
    public boolean isHoliday(HolidayConfig config) {
        return config.isHoliday(this);
    }

    /**
   * Tells whether this day is the first day of a month.
   * @return flag
   */
    public boolean isFirstDayOfMonth() {
        return get(DAY_OF_MONTH) == 1;
    }

    /**
   * Tells whether this day is the last day of a month.
   * @return flag
   */
    public boolean isLastDayOfMonth() {
        return get(DAY_OF_MONTH) == getActualMaximum(DAY_OF_MONTH);
    }

    /**
   * Tells whether this day is the first day of a year.
   * @return flag
   */
    public boolean isFirstDayOfYear() {
        return get(MONTH) == 0 && get(DAY_OF_MONTH) == 1;
    }

    /**
   * Tells whether this day is the last day of a year.
   * @return flag
   */
    public boolean isLastDayOfYear() {
        return get(MONTH) == 11 && get(DAY_OF_MONTH) == 31;
    }

    /**
   * Adds an amount of days to this object.
   * The amount may be negative.
   * @param count
   * @return this
   */
    public Day addDays(int count) {
        add(DATE, count);
        return this;
    }

    /**
   * Adds an amount of workdays to this object.
   * The amount may be negative.
   * @param count
   * @param config
   * @return this
   */
    public Day addWorkdays(int count, HolidayConfig config) {
        int step = (count < 0) ? -1 : 1;
        count = Math.abs(count);
        while (count > 0) {
            add(DATE, step);
            if (config.isWorkday(this)) {
                count--;
            }
        }
        return this;
    }

    /**
   * Adds an amount of months to this object.
   * The amount may be negative.
   * @param count
   * @return this
   */
    public Day addMonths(int count) {
        add(MONTH, count);
        return this;
    }

    /**
   * Adds an amount of month to this object adjusting end of month.
   * The amount may be negative.
   * If <tt>retainLastDayOfMonth</tt> is <tt>true</tt>
   * and the current day is the last day of a month,
   * than it is guaranteed that the result will also be the last day of a month.
   * @param count
   * @param retainLastDayOfMonth
   * @return this
   */
    public Day addMonths(int count, boolean retainLastDayOfMonth) {
        boolean adjustLastDay = retainLastDayOfMonth && isLastDayOfMonth();
        add(MONTH, count);
        if (adjustLastDay) {
            adjustLastDayOfMonth();
        }
        return this;
    }

    /**
   * Adds an amount of years to this object.
   * The amount may be negative. 
   * @param count
   * @return this
   */
    public Day addYears(int count) {
        add(YEAR, count);
        return this;
    }

    /**
   * Adds an amount of years to this object adjusting end of month.
   * The amount may be negative.
   * If <tt>retainLastDayOfMonth</tt> is <tt>true</tt>
   * and the current day is the last day of a month,
   * than it is guaranteed that the result will also be the last day of a month
   * (this is a special case for the last day of february in leap years).
   * @param count
   * @param retainLastDayOfMonth
   * @return this
   */
    public Day addYears(int count, boolean retainLastDayOfMonth) {
        boolean adjustLastDay = retainLastDayOfMonth && isLastDayOfMonth();
        add(YEAR, count);
        if (adjustLastDay) {
            adjustLastDayOfMonth();
        }
        return this;
    }

    /**
   * Sets this day conditionally to a specified weekday in the future.
   * If the current weekday is already the desired weekday, this day leaves as it is.
   * @param weekday
   * @return this
   */
    public Day adjustNextWeekday(Weekday weekday) {
        int difference = weekday.getCalendarConstant() - get(DAY_OF_WEEK);
        if (difference == 0) {
            return this;
        } else if (difference < 0) {
            difference += 7;
        }
        addDays(difference);
        return this;
    }

    /**
   * Sets this day conditionally to a specified weekday in the past.
   * If the current weekday is already the desired weekday, this day leaves as it is.
   * @param weekday
   * @return this
   */
    public Day adjustPreviousWeekday(Weekday weekday) {
        int difference = weekday.getCalendarConstant() - get(DAY_OF_WEEK);
        if (difference == 0) {
            return this;
        } else if (difference > 0) {
            difference -= 7;
        }
        addDays(difference);
        return this;
    }

    /**
   * Sets this day conditionally to the next workday.
   * If this day is already a workday, it leaves as it is.
   * @param config
   * @return this
   */
    public Day adjustNextWorkday(HolidayConfig config) {
        while (config.isHoliday(this)) {
            add(DATE, 1);
        }
        return this;
    }

    /**
   * Sets this day conditionally to the previous workday.
   * If this day is already a workday, it leaves as it is.
   * @param config
   * @return this
   */
    public Day adjustPreviousWorkday(HolidayConfig config) {
        while (config.isHoliday(this)) {
            add(DATE, -1);
        }
        return this;
    }

    /**
   * Sets this day conditionally to the next holiday.
   * If this day is already a workday, it leaves as it is.
   * @param config
   * @return this
   */
    public Day adjustNextHoliday(HolidayConfig config) {
        while (config.isWorkday(this)) {
            add(DATE, 1);
        }
        return this;
    }

    /**
   * Sets this day conditionally to the previous holiday.
   * If this day is already a workday, it leaves as it is.
   * @param config
   * @return this
   */
    public Day adjustPreviousHoliday(HolidayConfig config) {
        while (config.isWorkday(this)) {
            add(DATE, -1);
        }
        return this;
    }

    /**
   * Sets this object to the first day of the month.
   * @return this
   */
    public Day adjustFirstDayOfMonth() {
        set(DAY_OF_MONTH, 1);
        return this;
    }

    /**
   * Sets this object to the last day of the month.
   * @return this
   */
    public Day adjustLastDayOfMonth() {
        set(DAY_OF_MONTH, getActualMaximum(DAY_OF_MONTH));
        return this;
    }

    /**
   * Sets this object to the first day of the year.
   * @return this
   */
    public Day adjustFirstDayOfYear() {
        set(MONTH, 0);
        set(DAY_OF_MONTH, 1);
        return this;
    }

    /**
   * Sets this object to the last day of the year.
   * @return this
   */
    public Day adjustLastDayOfYear() {
        set(MONTH, 11);
        set(DAY_OF_MONTH, 31);
        return this;
    }

    /**
   * Returns the number of days between this day and a specified date.
   * The specified date may be in the past.
   * @param until
   * @return day count
   */
    public int dayCount(Date until) {
        return dayCount(new Day(until));
    }

    /**
   * Returns the number of days between this day and another day.
   * The other day may be in the past.
   * @param until
   * @return day count
   */
    public int dayCount(Calendar until) {
        if (get(YEAR) == until.get(YEAR)) {
            return until.get(DAY_OF_YEAR) - get(DAY_OF_YEAR);
        }
        boolean swap = compareTo(until) > 0;
        Calendar from = swap ? until : this;
        Calendar to = swap ? this : until;
        Day calc = new Day(from);
        calc.adjustLastDayOfYear();
        int count = calc.get(DAY_OF_YEAR) - from.get(DAY_OF_YEAR);
        calc.add(DATE, 1);
        while (calc.get(YEAR) != to.get(YEAR)) {
            count += calc.getActualMaximum(DAY_OF_YEAR);
            calc.add(YEAR, 1);
        }
        count += to.get(DAY_OF_YEAR);
        if (swap) {
            count *= -1;
        }
        return count;
    }

    /**
   * Returns the number of months between this day and another day.
   * The other day may be in the past.
   * Counts only complete months.
   * <p>
   * If a day is the last day of a month, the internal calculation day of month is set to 31.
   * The effect is that the period between january 30th and february 28th counts as 1 month
   * and the period between february 28th (in a non-leaf year) and march 30th counts as 0 month.
   * @param until
   * @return month count
   */
    public int monthCount(Date until) {
        return monthCount(new Day(until));
    }

    /**
   * Returns the number of months between this day and another day.
   * The other day may be in the past.
   * Counts only complete months.
   * <p>
   * If a day is the last day of a month, the internal calculation day of month is set to 31.
   * The effect is that the period between january 30th and february 28th counts as 1 month
   * and the period between february 28th (in a non-leaf year) and march 30th counts as 0 month.
   * @param until
   * @return month count
   */
    public int monthCount(Calendar until) {
        boolean swap = compareTo(until) > 0;
        Calendar from = swap ? until : this;
        int fromYear = from.get(YEAR);
        int fromMonth = from.get(MONTH);
        int fromDay = from.get(DAY_OF_MONTH);
        Calendar to = swap ? this : until;
        int toYear = to.get(YEAR);
        int toMonth = to.get(MONTH);
        int toDay = to.get(DAY_OF_MONTH);
        if (toDay == to.getActualMaximum(DAY_OF_MONTH)) {
            toDay = 31;
        }
        if (fromDay == from.getActualMaximum(DAY_OF_MONTH)) {
            fromDay = 31;
        }
        int count = (toYear * 12 + toMonth) - (fromYear * 12 + fromMonth);
        if (toDay < fromDay) {
            count--;
        }
        if (swap) {
            count *= -1;
        }
        return count;
    }

    /**
   * Returns the number of years between this day and another day.
   * The other day may be in the past;
   * @param until
   * @return year count
   */
    public int yearCount(Date until) {
        return yearCount(new Day(until));
    }

    /**
   * Returns the number of years between this day and another day.
   * The other day may be in the past;
   * @param until
   * @return year count
   */
    public int yearCount(Calendar until) {
        return monthCount(until) / 12;
    }
}

package com.ibm.oti.pim;

import java.util.Date;
import java.util.Enumeration;
import java.util.NoSuchElementException;
import java.util.Vector;
import javax.microedition.pim.RepeatRule;

public class RepeatDateEnumeration implements Enumeration {

    private RepeatRule rule;

    private long start;

    private long begin;

    private long end;

    private Date date = null;

    private long nextDate = -1;

    private long[] repeatFields = { -1, -1, -1, -1, -1, -1, 1, -1, -1 };

    private long[] exceptionDates = null;

    public RepeatDateEnumeration(RepeatRule rule, long startDate, long subsetBeginning, long subsetEnding) {
        this.rule = rule;
        this.start = startDate;
        this.begin = subsetBeginning;
        this.end = subsetEnding;
        getRepeatFields();
        getExceptionDates();
    }

    /**
	 * @see java.util.Enumeration#hasMoreElements()
	 */
    public boolean hasMoreElements() {
        if (date == null) getNext();
        return date != null;
    }

    /**
	 * @see java.util.Enumeration#nextElement()
	 */
    public Object nextElement() {
        if (date == null) getNext();
        if (date == null) throw new NoSuchElementException();
        Date result = date;
        date = null;
        return result;
    }

    /**
	 * Answers the next occurance.
	 */
    private void getNext() {
        long endDate = repeatFields[PIMUtil.END];
        if (endDate != -1 && start > endDate) return;
        if (nextDate == -1) {
            nextDate = start;
            if (!isValidStartDate()) nextDate = getNextDate();
            while (nextDate < begin) nextDate = getNextDate();
        } else {
            nextDate = getNextDate();
        }
        if (nextDate <= end && (endDate == -1 ? true : nextDate <= endDate)) date = new Date(nextDate);
    }

    private long getNextDate() {
        return DateHelper.getNextDate(repeatFields, exceptionDates, nextDate);
    }

    /**
	 * Reads set fields and fills 'repeatFields'.
	 */
    private void getRepeatFields() {
        if (rule == null) return;
        int[] fields = rule.getFields();
        for (int i = 0; i < fields.length; i++) {
            int field = fields[i];
            if (field != RepeatRule.END) repeatFields[PIMUtil.getRepeatFieldIndexFromID(field)] = rule.getInt(field); else repeatFields[PIMUtil.getRepeatFieldIndexFromID(field)] = rule.getDate(field);
        }
    }

    /**
	 * Returns true if the start date is a valid occurance.
	 * @return boolean
	 */
    private boolean isValidStartDate() {
        int dayInWeek = DateHelper.getDayInWeek(start);
        switch((int) repeatFields[PIMUtil.FREQUENCY]) {
            case RepeatRule.DAILY:
                return true;
            case RepeatRule.WEEKLY:
                return (repeatFields[PIMUtil.DAY_IN_WEEK] & dayInWeek) == dayInWeek;
            case RepeatRule.MONTHLY:
                if (repeatFields[PIMUtil.DAY_IN_WEEK] != -1) {
                    return ((repeatFields[PIMUtil.DAY_IN_WEEK] & dayInWeek) == dayInWeek && isValidWeekInMonth());
                } else return repeatFields[PIMUtil.DAY_IN_MONTH] == DateHelper.getDayInMonth(start);
            case RepeatRule.YEARLY:
                if (repeatFields[PIMUtil.DAY_IN_YEAR] != -1) return repeatFields[PIMUtil.DAY_IN_YEAR] == DateHelper.getDayInYear(start); else if (repeatFields[PIMUtil.DAY_IN_WEEK] != -1) {
                    int monthInYear = DateHelper.getMonthInYear(start);
                    return (repeatFields[PIMUtil.DAY_IN_WEEK] & dayInWeek) == dayInWeek && isValidWeekInMonth() && (repeatFields[PIMUtil.MONTH_IN_YEAR] & monthInYear) == monthInYear;
                } else {
                    int dayInMonth = DateHelper.getDayInMonth(start);
                    int monthInYear = DateHelper.getMonthInYear(start);
                    return (repeatFields[PIMUtil.DAY_IN_MONTH] & dayInMonth) == dayInMonth && (repeatFields[PIMUtil.MONTH_IN_YEAR] & monthInYear) == monthInYear;
                }
            default:
                return false;
        }
    }

    /**
	 * Answers if the weekInMonth start date is valid
	 */
    private boolean isValidWeekInMonth() {
        int[] weeksInMonth = DateHelper.getWeekInMonth(start);
        for (int i = 0; i < weeksInMonth.length; i++) {
            if ((repeatFields[PIMUtil.WEEK_IN_MONTH] & weeksInMonth[i]) == weeksInMonth[i]) return true;
        }
        return false;
    }

    /**
	 * Gets all exception dates and fills 'exceptionDates'.
	 */
    private void getExceptionDates() {
        Vector dates = new Vector();
        Enumeration exceptDates = rule.getExceptDates();
        while (exceptDates.hasMoreElements()) dates.addElement(exceptDates.nextElement());
        exceptionDates = new long[dates.size()];
        for (int i = 0; i < dates.size(); i++) {
            exceptionDates[i] = ((Date) dates.elementAt(i)).getTime();
        }
    }
}

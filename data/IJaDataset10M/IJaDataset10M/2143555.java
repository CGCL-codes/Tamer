package org.jbudget.util;

import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Locale;

/**
 * Utilities for formating objects rlated to dates.
 * @author petrov
 */
public class DateFormater {

    private final String[] monthStrings;

    private final String[] shortMonthStrings;

    private final String[] weekdayStrings;

    private final String[] shortWeekdayStrings;

    private final Calendar calendar;

    /** Creates a new instance of DateFormater */
    public DateFormater() {
        calendar = Calendar.getInstance();
        DateFormatSymbols symbols = new DateFormatSymbols();
        monthStrings = symbols.getMonths();
        shortMonthStrings = symbols.getShortMonths();
        weekdayStrings = symbols.getWeekdays();
        shortWeekdayStrings = symbols.getShortWeekdays();
    }

    /** Creates a new instance of DateFormater */
    public DateFormater(Locale locale) {
        calendar = Calendar.getInstance(locale);
        DateFormatSymbols symbols = new DateFormatSymbols(locale);
        monthStrings = symbols.getMonths();
        shortMonthStrings = symbols.getShortMonths();
        weekdayStrings = symbols.getWeekdays();
        shortWeekdayStrings = symbols.getShortWeekdays();
    }

    /** Returns a string representation of a month */
    public String formatMonth(int month) {
        if (month < Calendar.JANUARY || month > Calendar.DECEMBER) throw new IllegalArgumentException("Bad month argument: " + month);
        return monthStrings[month];
    }

    /** Returns a string representation of a month */
    public String formatMonthShort(int month) {
        if (month < Calendar.JANUARY || month > Calendar.DECEMBER) throw new IllegalArgumentException("Bad month argument: " + month);
        return shortMonthStrings[month];
    }

    /** Parses a string representation of a month */
    public int parseMonth(String str) throws ParseException {
        while (str.startsWith(" ")) str = str.substring(1);
        while (str.endsWith(" ")) str = str.substring(0, str.length() - 1);
        for (int month = Calendar.JANUARY; month <= Calendar.DECEMBER; month++) if (str.equalsIgnoreCase(monthStrings[month]) || str.equalsIgnoreCase(shortMonthStrings[month])) {
            return month;
        }
        throw new ParseException("Could not parse month '" + str + "'", 0);
    }

    /** Returns a string representation of a week day */
    public String formatWeekday(int day) {
        int min = calendar.getMinimum(Calendar.DAY_OF_WEEK);
        int max = calendar.getMaximum(Calendar.DAY_OF_WEEK);
        if (day < min || day > max) throw new IllegalArgumentException("Bad day argument: " + day);
        return weekdayStrings[day];
    }

    /** Returns a string representation of a week day */
    public String formatWeekdayShort(int day) {
        int min = calendar.getMinimum(Calendar.DAY_OF_WEEK);
        int max = calendar.getMaximum(Calendar.DAY_OF_WEEK);
        if (day < min || day > max) throw new IllegalArgumentException("Bad day argument: " + day);
        return shortWeekdayStrings[day];
    }

    /** Parses a string representation of a weekday */
    public int parseWeekday(String str) throws ParseException {
        while (str.startsWith(" ")) str = str.substring(1);
        while (str.endsWith(" ")) str = str.substring(0, str.length() - 1);
        int min = calendar.getMinimum(Calendar.DAY_OF_WEEK);
        int max = calendar.getMaximum(Calendar.DAY_OF_WEEK);
        for (int day = min; day <= max; day++) if (str.equalsIgnoreCase(weekdayStrings[day]) || str.equalsIgnoreCase(shortWeekdayStrings[day])) {
            return day;
        }
        throw new ParseException("Could not parse a weekday string: '" + str + "'", 0);
    }
}

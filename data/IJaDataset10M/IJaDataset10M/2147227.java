package com.google.gwt.i18n.shared.impl.cldr;

/**
 * Implementation of DateTimeFormatInfo for the "nr" locale.
 */
public class DateTimeFormatInfoImpl_nr extends DateTimeFormatInfoImpl {

    @Override
    public String[] erasFull() {
        return new String[] { "BC", "AD" };
    }

    @Override
    public int firstDayOfTheWeek() {
        return 0;
    }

    @Override
    public String formatMonthFullWeekdayDay() {
        return "EEEE, MMMM d";
    }

    @Override
    public String formatMonthNumDay() {
        return "M/d";
    }

    @Override
    public String formatYearMonthAbbrev() {
        return "MMM y";
    }

    @Override
    public String formatYearMonthAbbrevDay() {
        return "MMM d, y";
    }

    @Override
    public String formatYearMonthFull() {
        return "MMMM y";
    }

    @Override
    public String formatYearMonthFullDay() {
        return "MMMM d, y";
    }

    @Override
    public String formatYearMonthNum() {
        return "M/y";
    }

    @Override
    public String formatYearMonthNumDay() {
        return "M/d/y";
    }

    @Override
    public String formatYearMonthWeekdayDay() {
        return "EEE, MMM d, y";
    }

    @Override
    public String formatYearQuarterFull() {
        return "QQQQ y";
    }

    @Override
    public String formatYearQuarterShort() {
        return "Q y";
    }

    @Override
    public String[] monthsFull() {
        return new String[] { "Janabari", "uFeberbari", "uMatjhi", "u-Apreli", "Meyi", "Juni", "Julayi", "Arhostosi", "Septemba", "Oktoba", "Usinyikhaba", "Disemba" };
    }

    @Override
    public String[] monthsNarrow() {
        return new String[] { "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12" };
    }

    @Override
    public String[] monthsShort() {
        return new String[] { "Jan", "Feb", "Mat", "Apr", "Mey", "Jun", "Jul", "Arh", "Sep", "Okt", "Usi", "Dis" };
    }

    @Override
    public String[] quartersFull() {
        return new String[] { "Q1", "Q2", "Q3", "Q4" };
    }

    @Override
    public String[] weekdaysFull() {
        return new String[] { "uSonto", "uMvulo", "uLesibili", "Lesithathu", "uLesine", "ngoLesihlanu", "umGqibelo" };
    }

    @Override
    public String[] weekdaysNarrow() {
        return new String[] { "1", "2", "3", "4", "5", "6", "7" };
    }

    @Override
    public String[] weekdaysShort() {
        return new String[] { "Son", "Mvu", "Bil", "Tha", "Ne", "Hla", "Gqi" };
    }
}

package com.google.gwt.i18n.client.impl.cldr;

/**
 * Implementation of DateTimeFormatInfo for the "gaa" locale.
 */
public class DateTimeFormatInfoImpl_gaa extends DateTimeFormatInfoImpl {

    @Override
    public String[] ampms() {
        return new String[] { "LB", "SN" };
    }

    @Override
    public String dateFormatShort() {
        return "yy/MM/dd";
    }

    @Override
    public String[] erasFull() {
        return new String[] { "Dani Jesu", "KJ" };
    }

    @Override
    public String[] erasShort() {
        return new String[] { "DJ", "KJ" };
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
        return new String[] { "Aharabata", "Oflo", "Ochokrikri", "Abeibee", "Agbeinaa", "Otukwadan", "Maawe", "Manyawale", "Gbo", "Anton", "Alemle", "Afuabee" };
    }

    @Override
    public String[] monthsNarrow() {
        return new String[] { "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12" };
    }

    @Override
    public String[] monthsShort() {
        return new String[] { "Aha", "Ofl", "Och", "Abe", "Agb", "Otu", "Maa", "Man", "Gbo", "Ant", "Ale", "Afu" };
    }

    @Override
    public String[] quartersFull() {
        return new String[] { "Q1", "Q2", "Q3", "Q4" };
    }

    @Override
    public String[] weekdaysFull() {
        return new String[] { "Hogbaa", "Dzu", "Dzufo", "Sho", "Soo", "Sohaa", "Ho" };
    }

    @Override
    public String[] weekdaysNarrow() {
        return new String[] { "1", "2", "3", "4", "5", "6", "7" };
    }

    @Override
    public String[] weekdaysShort() {
        return new String[] { "Hog", "Dzu", "Dzf", "Sho", "Soo", "Soh", "Ho" };
    }
}

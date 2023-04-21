package com.google.gwt.i18n.shared.impl.cldr;

/**
 * Implementation of DateTimeFormatInfo for the "en" locale.
 */
public class DateTimeFormatInfoImpl_en extends DateTimeFormatInfoImpl {

    @Override
    public String dateFormatFull() {
        return "EEEE, MMMM d, y";
    }

    @Override
    public String dateFormatLong() {
        return "MMMM d, y";
    }

    @Override
    public String dateFormatMedium() {
        return "MMM d, y";
    }

    @Override
    public String dateFormatShort() {
        return "M/d/yy";
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
    public String timeFormatFull() {
        return "h:mm:ss a zzzz";
    }

    @Override
    public String timeFormatLong() {
        return "h:mm:ss a z";
    }

    @Override
    public String timeFormatMedium() {
        return "h:mm:ss a";
    }

    @Override
    public String timeFormatShort() {
        return "h:mm a";
    }
}

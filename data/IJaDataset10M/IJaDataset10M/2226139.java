package com.google.gwt.i18n.shared.impl.cldr;

/**
 * Implementation of DateTimeFormatInfo for the "en_IE" locale.
 */
public class DateTimeFormatInfoImpl_en_IE extends DateTimeFormatInfoImpl_en {

    @Override
    public String[] ampms() {
        return new String[] { "a.m.", "p.m." };
    }

    @Override
    public String dateFormatFull() {
        return "EEEE d MMMM y";
    }

    @Override
    public String dateFormatLong() {
        return "d MMMM y";
    }

    @Override
    public String dateFormatMedium() {
        return "d MMM y";
    }

    @Override
    public String dateFormatShort() {
        return "dd/MM/yyyy";
    }

    @Override
    public int firstDayOfTheWeek() {
        return 1;
    }

    @Override
    public String formatMonthFullDay() {
        return "d MMMM";
    }

    @Override
    public String formatMonthNumDay() {
        return "d/M";
    }

    @Override
    public String formatYearMonthAbbrevDay() {
        return "d MMM y";
    }

    @Override
    public String formatYearMonthFullDay() {
        return "d MMMM y";
    }

    @Override
    public String formatYearMonthNumDay() {
        return "d/M/yyyy";
    }

    @Override
    public String formatYearMonthWeekdayDay() {
        return "EEE d MMM y";
    }

    @Override
    public String timeFormatFull() {
        return "HH:mm:ss zzzz";
    }

    @Override
    public String timeFormatLong() {
        return "HH:mm:ss z";
    }

    @Override
    public String timeFormatMedium() {
        return "HH:mm:ss";
    }

    @Override
    public String timeFormatShort() {
        return "HH:mm";
    }
}

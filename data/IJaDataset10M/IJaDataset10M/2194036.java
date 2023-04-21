package com.google.gwt.i18n.client.impl.cldr;

/**
 * Implementation of DateTimeFormatInfo for the "en_BZ" locale.
 */
public class DateTimeFormatInfoImpl_en_BZ extends DateTimeFormatInfoImpl_en {

    @Override
    public String dateFormatFull() {
        return "dd MMMM y";
    }

    @Override
    public String dateFormatLong() {
        return "dd MMMM y";
    }

    @Override
    public String dateFormatMedium() {
        return "dd-MMM-y";
    }

    @Override
    public String dateFormatShort() {
        return "dd/MM/yy";
    }

    @Override
    public String formatMonthAbbrevDay() {
        return "dd MMM";
    }

    @Override
    public String formatMonthFullDay() {
        return "dd MMMM";
    }

    @Override
    public String formatMonthFullWeekdayDay() {
        return "EEEE dd MMMM";
    }

    @Override
    public String formatYearMonthAbbrevDay() {
        return "dd MMM y";
    }

    @Override
    public String formatYearMonthWeekdayDay() {
        return "EEE dd MMM y";
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

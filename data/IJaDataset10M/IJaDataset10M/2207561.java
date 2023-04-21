package com.google.gwt.i18n.shared.impl.cldr;

/**
 * Implementation of DateTimeFormatInfo for the "es_CL" locale.
 */
public class DateTimeFormatInfoImpl_es_CL extends DateTimeFormatInfoImpl_es_419 {

    @Override
    public String dateFormatMedium() {
        return "dd-MM-yyyy";
    }

    @Override
    public String dateFormatShort() {
        return "dd-MM-yy";
    }

    @Override
    public String formatHour24Minute() {
        return "H:mm";
    }

    @Override
    public String formatHour24MinuteSecond() {
        return "H:mm:ss";
    }

    @Override
    public String formatMonthNumDay() {
        return "dd-MM";
    }

    @Override
    public String formatYearMonthNum() {
        return "MM-yy";
    }

    @Override
    public String formatYearMonthNumDay() {
        return "dd-MM-yy";
    }

    @Override
    public String timeFormatLong() {
        return "H:mm:ss z";
    }

    @Override
    public String timeFormatMedium() {
        return "H:mm:ss";
    }

    @Override
    public String timeFormatShort() {
        return "H:mm";
    }
}

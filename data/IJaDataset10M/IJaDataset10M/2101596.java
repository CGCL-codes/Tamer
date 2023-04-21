package com.google.gwt.i18n.shared.impl.cldr;

/**
 * Implementation of DateTimeFormatInfo for the "mfe" locale.
 */
public class DateTimeFormatInfoImpl_mfe extends DateTimeFormatInfoImpl {

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
        return "d MMM, y";
    }

    @Override
    public String dateFormatShort() {
        return "d/M/yyyy";
    }

    @Override
    public String[] erasFull() {
        return new String[] { "avan Zezi-Krist", "apre Zezi-Krist" };
    }

    @Override
    public String[] erasShort() {
        return new String[] { "av. Z-K", "ap. Z-K" };
    }

    @Override
    public String formatMinuteSecond() {
        return "m:ss";
    }

    @Override
    public String formatMonthAbbrev() {
        return "MMM";
    }

    @Override
    public String formatMonthAbbrevDay() {
        return "d MMM";
    }

    @Override
    public String formatMonthFull() {
        return "MMMM";
    }

    @Override
    public String formatMonthFullDay() {
        return "d MMMM";
    }

    @Override
    public String formatMonthFullWeekdayDay() {
        return "EEEE d MMMM";
    }

    @Override
    public String formatMonthNumDay() {
        return "d/M";
    }

    @Override
    public String formatYearMonthAbbrev() {
        return "MMM y";
    }

    @Override
    public String formatYearMonthAbbrevDay() {
        return "d MMM, y";
    }

    @Override
    public String formatYearMonthFull() {
        return "MMMM y";
    }

    @Override
    public String formatYearMonthFullDay() {
        return "d MMMM y";
    }

    @Override
    public String formatYearMonthNum() {
        return "M/y";
    }

    @Override
    public String formatYearMonthWeekdayDay() {
        return "EEE d MMM y";
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
        return new String[] { "zanvie", "fevriye", "mars", "avril", "me", "zin", "zilye", "out", "septam", "oktob", "novam", "desam" };
    }

    @Override
    public String[] monthsNarrow() {
        return new String[] { "z", "f", "m", "a", "m", "z", "z", "o", "s", "o", "n", "d" };
    }

    @Override
    public String[] monthsShort() {
        return new String[] { "zan", "fev", "mar", "avr", "me", "zin", "zil", "out", "sep", "okt", "nov", "des" };
    }

    @Override
    public String[] quartersFull() {
        return new String[] { "1e trimes", "2em trimes", "3em trimes", "4em trimes" };
    }

    @Override
    public String[] quartersShort() {
        return new String[] { "T1", "T2", "T3", "T4" };
    }

    @Override
    public String[] weekdaysFull() {
        return new String[] { "dimans", "lindi", "mardi", "merkredi", "zedi", "vandredi", "samdi" };
    }

    @Override
    public String[] weekdaysNarrow() {
        return new String[] { "d", "l", "m", "m", "z", "v", "s" };
    }

    @Override
    public String[] weekdaysShort() {
        return new String[] { "dim", "lin", "mar", "mer", "ze", "van", "sam" };
    }
}

package com.google.gwt.i18n.client.impl.cldr;

/**
 * Implementation of DateTimeFormatInfo for the "bm" locale.
 */
public class DateTimeFormatInfoImpl_bm extends DateTimeFormatInfoImpl {

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
        return new String[] { "jezu krisiti ɲɛ", "jezu krisiti minkɛ" };
    }

    @Override
    public String[] erasShort() {
        return new String[] { "J.-C. ɲɛ", "ni J.-C." };
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
        return "d MMM y";
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
    public String formatYearMonthNumDay() {
        return "d/M/y";
    }

    @Override
    public String formatYearMonthWeekdayDay() {
        return "EEE d MMM y";
    }

    @Override
    public String formatYearQuarterFull() {
        return "'T'QQQQ y";
    }

    @Override
    public String formatYearQuarterShort() {
        return "'T'Q y";
    }

    @Override
    public String[] monthsFull() {
        return new String[] { "zanwuye", "feburuye", "marisi", "awirili", "mɛ", "zuwɛn", "zuluye", "uti", "sɛtanburu", "ɔkutɔburu", "nowanburu", "desanburu" };
    }

    @Override
    public String[] monthsNarrow() {
        return new String[] { "Z", "F", "M", "A", "M", "Z", "Z", "U", "S", "Ɔ", "N", "D" };
    }

    @Override
    public String[] monthsShort() {
        return new String[] { "zan", "feb", "nar", "awi", "mɛ", "zuw", "zul", "uti", "sɛt", "ɔku", "now", "des" };
    }

    @Override
    public String[] quartersFull() {
        return new String[] { "kalo saba fɔlɔ", "kalo saba filanan", "kalo saba sabanan", "kalo saba naaninan" };
    }

    @Override
    public String[] quartersShort() {
        return new String[] { "KS1", "KS2", "KS3", "KS4" };
    }

    @Override
    public String[] weekdaysFull() {
        return new String[] { "kari", "ntɛnɛ", "tarata", "araba", "alamisa", "juma", "sibiri" };
    }

    @Override
    public String[] weekdaysNarrow() {
        return new String[] { "K", "N", "T", "A", "A", "J", "S" };
    }

    @Override
    public String[] weekdaysShort() {
        return new String[] { "kar", "ntɛ", "tar", "ara", "ala", "jum", "sib" };
    }
}

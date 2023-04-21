package com.google.gwt.i18n.shared.impl.cldr;

/**
 * Implementation of DateTimeFormatInfo for the "eo" locale.
 */
public class DateTimeFormatInfoImpl_eo extends DateTimeFormatInfoImpl {

    @Override
    public String[] ampms() {
        return new String[] { "atm", "ptm" };
    }

    @Override
    public String dateFormatFull() {
        return "EEEE, d-'a' 'de' MMMM y";
    }

    @Override
    public String dateFormatLong() {
        return "y-MMMM-dd";
    }

    @Override
    public String dateFormatMedium() {
        return "y-MMM-dd";
    }

    @Override
    public String dateFormatShort() {
        return "yy-MM-dd";
    }

    @Override
    public String[] erasFull() {
        return new String[] { "aK", "pK" };
    }

    @Override
    public String[] erasShort() {
        return new String[] { "aK", "pK" };
    }

    @Override
    public String formatYearMonthFullDay() {
        return "y-MMMM-d";
    }

    @Override
    public String[] monthsFull() {
        return new String[] { "januaro", "februaro", "marto", "aprilo", "majo", "junio", "julio", "aŭgusto", "septembro", "oktobro", "novembro", "decembro" };
    }

    @Override
    public String[] monthsNarrow() {
        return new String[] { "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12" };
    }

    @Override
    public String[] monthsShort() {
        return new String[] { "jan", "feb", "mar", "apr", "maj", "jun", "jul", "aŭg", "sep", "okt", "nov", "dec" };
    }

    @Override
    public String[] quartersFull() {
        return new String[] { "1a kvaronjaro", "2a kvaronjaro", "3a kvaronjaro", "4a kvaronjaro" };
    }

    @Override
    public String[] quartersShort() {
        return new String[] { "K1", "K2", "K3", "K4" };
    }

    @Override
    public String timeFormatFull() {
        return "H-'a' 'horo' 'kaj' m:ss zzzz";
    }

    @Override
    public String[] weekdaysFull() {
        return new String[] { "dimanĉo", "lundo", "mardo", "merkredo", "ĵaŭdo", "vendredo", "sabato" };
    }

    @Override
    public String[] weekdaysNarrow() {
        return new String[] { "1", "2", "3", "4", "5", "6", "7" };
    }

    @Override
    public String[] weekdaysShort() {
        return new String[] { "di", "lu", "ma", "me", "ĵa", "ve", "sa" };
    }
}

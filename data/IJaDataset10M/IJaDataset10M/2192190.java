package com.google.gwt.i18n.client.impl.cldr;

/**
 * Implementation of DateTimeFormatInfo for the "fur" locale.
 */
public class DateTimeFormatInfoImpl_fur extends DateTimeFormatInfoImpl {

    @Override
    public String[] ampms() {
        return new String[] { "a.", "p." };
    }

    @Override
    public String dateFormatFull() {
        return "EEEE d 'di' MMMM 'dal' y";
    }

    @Override
    public String dateFormatLong() {
        return "d 'di' MMMM 'dal' y";
    }

    @Override
    public String dateFormatMedium() {
        return "dd/MM/yyyy";
    }

    @Override
    public String dateFormatShort() {
        return "dd/MM/yy";
    }

    @Override
    public String[] erasFull() {
        return new String[] { "pdC", "ddC" };
    }

    @Override
    public String[] erasShort() {
        return new String[] { "pdC", "ddC" };
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
        return new String[] { "Zenâr", "Fevrâr", "Març", "Avrîl", "Mai", "Jugn", "Lui", "Avost", "Setembar", "Otubar", "Novembar", "Dicembar" };
    }

    @Override
    public String[] monthsNarrow() {
        return new String[] { "Z", "F", "M", "A", "M", "J", "L", "A", "S", "O", "N", "D" };
    }

    @Override
    public String[] monthsShort() {
        return new String[] { "Zen", "Fev", "Mar", "Avr", "Mai", "Jug", "Lui", "Avo", "Set", "Otu", "Nov", "Dic" };
    }

    @Override
    public String[] quartersFull() {
        return new String[] { "Prin trimestri", "Secont trimestri", "Tierç trimestri", "Cuart trimestri" };
    }

    @Override
    public String[] quartersShort() {
        return new String[] { "T1", "T2", "T3", "T4" };
    }

    @Override
    public String[] weekdaysFull() {
        return new String[] { "domenie", "lunis", "martars", "miercus", "joibe", "vinars", "sabide" };
    }

    @Override
    public String[] weekdaysNarrow() {
        return new String[] { "D", "L", "M", "M", "J", "V", "S" };
    }

    @Override
    public String[] weekdaysShort() {
        return new String[] { "dom", "lun", "mar", "mie", "joi", "vin", "sab" };
    }
}

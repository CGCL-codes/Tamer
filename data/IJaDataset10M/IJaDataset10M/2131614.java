package com.google.gwt.i18n.client.impl.cldr;

/**
 * Implementation of DateTimeFormatInfo for the "el" locale.
 */
public class DateTimeFormatInfoImpl_el extends DateTimeFormatInfoImpl {

    @Override
    public String[] ampms() {
        return new String[] { "π.μ.", "μ.μ." };
    }

    @Override
    public String dateFormatFull() {
        return "EEEE, d MMMM y";
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
        return "d/M/yy";
    }

    @Override
    public String[] erasFull() {
        return new String[] { "π.Χ.", "μ.Χ." };
    }

    @Override
    public String[] erasShort() {
        return new String[] { "π.Χ.", "μ.Χ." };
    }

    @Override
    public String formatMonthAbbrevDay() {
        return "d MMM";
    }

    @Override
    public String formatMonthFullDay() {
        return "d MMMM";
    }

    @Override
    public String formatMonthFullWeekdayDay() {
        return "EEEE, d MMMM";
    }

    @Override
    public String formatMonthNumDay() {
        return "d/M";
    }

    @Override
    public String formatYearMonthAbbrev() {
        return "LLL y";
    }

    @Override
    public String formatYearMonthAbbrevDay() {
        return "d MMM y";
    }

    @Override
    public String formatYearMonthFull() {
        return "LLLL y";
    }

    @Override
    public String formatYearMonthFullDay() {
        return "d MMMM y";
    }

    @Override
    public String formatYearMonthNum() {
        return "M/yyyy";
    }

    @Override
    public String formatYearMonthNumDay() {
        return "d/M/yyyy";
    }

    @Override
    public String formatYearMonthWeekdayDay() {
        return "EEE, d MMM y";
    }

    @Override
    public String[] monthsFull() {
        return new String[] { "Ιανουαρίου", "Φεβρουαρίου", "Μαρτίου", "Απριλίου", "Μαΐου", "Ιουνίου", "Ιουλίου", "Αυγούστου", "Σεπτεμβρίου", "Οκτωβρίου", "Νοεμβρίου", "Δεκεμβρίου" };
    }

    @Override
    public String[] monthsFullStandalone() {
        return new String[] { "Ιανουάριος", "Φεβρουάριος", "Μάρτιος", "Απρίλιος", "Μάιος", "Ιούνιος", "Ιούλιος", "Αύγουστος", "Σεπτέμβριος", "Οκτώβριος", "Νοέμβριος", "Δεκέμβριος" };
    }

    @Override
    public String[] monthsNarrow() {
        return new String[] { "Ι", "Φ", "Μ", "Α", "Μ", "Ι", "Ι", "Α", "Σ", "Ο", "Ν", "Δ" };
    }

    @Override
    public String[] monthsShort() {
        return new String[] { "Ιαν", "Φεβ", "Μαρ", "Απρ", "Μαϊ", "Ιουν", "Ιουλ", "Αυγ", "Σεπ", "Οκτ", "Νοε", "Δεκ" };
    }

    @Override
    public String[] monthsShortStandalone() {
        return new String[] { "Ιαν", "Φεβ", "Μάρ", "Απρ", "Μάι", "Ιούν", "Ιούλ", "Αυγ", "Σεπ", "Οκτ", "Νοέ", "Δεκ" };
    }

    @Override
    public String[] quartersFull() {
        return new String[] { "1ο τρίμηνο", "2ο τρίμηνο", "3ο τρίμηνο", "4ο τρίμηνο" };
    }

    @Override
    public String[] quartersShort() {
        return new String[] { "Τ1", "Τ2", "Τ3", "Τ4" };
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

    @Override
    public String[] weekdaysFull() {
        return new String[] { "Κυριακή", "Δευτέρα", "Τρίτη", "Τετάρτη", "Πέμπτη", "Παρασκευή", "Σάββατο" };
    }

    @Override
    public String[] weekdaysNarrow() {
        return new String[] { "Κ", "Δ", "Τ", "Τ", "Π", "Π", "Σ" };
    }

    @Override
    public String[] weekdaysShort() {
        return new String[] { "Κυρ", "Δευ", "Τρι", "Τετ", "Πεμ", "Παρ", "Σαβ" };
    }

    @Override
    public String[] weekdaysShortStandalone() {
        return new String[] { "Κυρ", "Δευ", "Τρί", "Τετ", "Πέμ", "Παρ", "Σάβ" };
    }
}

package com.google.gwt.i18n.client.impl.cldr;

/**
 * Implementation of DateTimeFormatInfo for the "ar" locale.
 */
public class DateTimeFormatInfoImpl_ar extends DateTimeFormatInfoImpl {

    @Override
    public String[] ampms() {
        return new String[] { "ص", "م" };
    }

    @Override
    public String dateFormatFull() {
        return "EEEE، d MMMM، y";
    }

    @Override
    public String dateFormatLong() {
        return "d MMMM، y";
    }

    @Override
    public String dateFormatMedium() {
        return "dd‏/MM‏/yyyy";
    }

    @Override
    public String dateFormatShort() {
        return "d‏/M‏/yyyy";
    }

    @Override
    public String[] erasFull() {
        return new String[] { "قبل الميلاد", "ميلادي" };
    }

    @Override
    public String[] erasShort() {
        return new String[] { "ق.م", "م" };
    }

    @Override
    public int firstDayOfTheWeek() {
        return 6;
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
        return "EEEE d MMMM";
    }

    @Override
    public String formatMonthNumDay() {
        return "d‏/M";
    }

    @Override
    public String formatYearMonthAbbrev() {
        return "MMM y";
    }

    @Override
    public String formatYearMonthAbbrevDay() {
        return "d MMM، y";
    }

    @Override
    public String formatYearMonthFull() {
        return "MMMM y";
    }

    @Override
    public String formatYearMonthFullDay() {
        return "d MMMM، y";
    }

    @Override
    public String formatYearMonthNum() {
        return "M‏/yyyy";
    }

    @Override
    public String formatYearMonthNumDay() {
        return "d‏/M‏/yyyy";
    }

    @Override
    public String formatYearMonthWeekdayDay() {
        return "EEE، d MMM، y";
    }

    @Override
    public String formatYearQuarterFull() {
        return "yyyy QQQQ";
    }

    @Override
    public String formatYearQuarterShort() {
        return "yyyy Q";
    }

    @Override
    public String[] monthsFull() {
        return new String[] { "يناير", "فبراير", "مارس", "أبريل", "مايو", "يونيو", "يوليو", "أغسطس", "سبتمبر", "أكتوبر", "نوفمبر", "ديسمبر" };
    }

    @Override
    public String[] monthsNarrow() {
        return new String[] { "ي", "ف", "م", "أ", "و", "ن", "ل", "غ", "س", "ك", "ب", "د" };
    }

    @Override
    public String[] monthsShort() {
        return new String[] { "يناير", "فبراير", "مارس", "أبريل", "مايو", "يونيو", "يوليو", "أغسطس", "سبتمبر", "أكتوبر", "نوفمبر", "ديسمبر" };
    }

    @Override
    public String[] quartersFull() {
        return new String[] { "الربع الأول", "الربع الثاني", "الربع الثالث", "الربع الرابع" };
    }

    @Override
    public String[] quartersShort() {
        return new String[] { "الربع الأول", "الربع الثاني", "الربع الثالث", "الربع الرابع" };
    }

    @Override
    public String timeFormatFull() {
        return "zzzz h:mm:ss a";
    }

    @Override
    public String timeFormatLong() {
        return "z h:mm:ss a";
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
        return new String[] { "الأحد", "الاثنين", "الثلاثاء", "الأربعاء", "الخميس", "الجمعة", "السبت" };
    }

    @Override
    public String[] weekdaysNarrow() {
        return new String[] { "ح", "ن", "ث", "ر", "خ", "ج", "س" };
    }

    @Override
    public String[] weekdaysShort() {
        return new String[] { "الأحد", "الاثنين", "الثلاثاء", "الأربعاء", "الخميس", "الجمعة", "السبت" };
    }

    @Override
    public int weekendEnd() {
        return 6;
    }

    @Override
    public int weekendStart() {
        return 5;
    }
}

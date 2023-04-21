package com.google.gwt.i18n.client.impl.cldr;

/**
 * Implementation of DateTimeFormatInfo for the "my" locale.
 */
public class DateTimeFormatInfoImpl_my extends DateTimeFormatInfoImpl {

    @Override
    public String[] ampms() {
        return new String[] { "နံနက်", "ညနေ" };
    }

    @Override
    public String dateFormatShort() {
        return "yy/MM/dd";
    }

    @Override
    public String[] erasFull() {
        return new String[] { "ခရစ်တော် မပေါ်မီကာလ", "ခရစ်တော် ပေါ်ထွန်းပြီးကာလ" };
    }

    @Override
    public String[] erasShort() {
        return new String[] { "ဘီစီ", "အေဒီ" };
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
    public String formatYearMonthNum() {
        return "yy/M";
    }

    @Override
    public String[] monthsFull() {
        return new String[] { "ဇန်နဝါရီ", "ဖေဖော်ဝါရီ", "မတ်", "ဧပြီ", "မေ", "ဇွန်", "ဇူလိုင်", "ဩဂုတ်", "စက်တင်ဘာ", "အောက်တိုဘာ", "နိုဝင်ဘာ", "ဒီဇင်ဘာ" };
    }

    @Override
    public String[] monthsNarrow() {
        return new String[] { "ဇ", "ဖ", "မ", "ဧ", "မ", "ဇ", "ဇ", "ဩ", "စ", "အ", "န", "ဒ" };
    }

    @Override
    public String[] monthsShort() {
        return new String[] { "ဇန်", "ဖေ", "မတ်", "ဧ", "မေ", "ဇွန်", "ဇူ", "ဩ", "စက်", "အောက်", "နို", "ဒီ" };
    }

    @Override
    public String[] quartersFull() {
        return new String[] { "ပထမ သုံးလပတ်", "ဒုတိယ သုံးလပတ်", "တတိယ သုံးလပတ်", "စတုတ္ထ သုံးလပတ်" };
    }

    @Override
    public String[] quartersShort() {
        return new String[] { "ပ-စိတ်", "ဒု-စိတ်", "တ-စိတ်", "စ-စိတ်" };
    }

    @Override
    public String[] weekdaysFull() {
        return new String[] { "တနင်္ဂနွေ", "တနင်္လာ", "အင်္ဂါ", "ဗုဒ္ဓဟူး", "ကြာသပတေး", "သောကြာ", "စနေ" };
    }

    @Override
    public String[] weekdaysNarrow() {
        return new String[] { "တ", "တ", "အ", "ဗ", "က", "သ", "စ" };
    }

    @Override
    public String[] weekdaysShort() {
        return new String[] { "နွေ", "လာ", "ဂါ", "ဟူး", "တေး", "ကြာ", "နေ" };
    }
}

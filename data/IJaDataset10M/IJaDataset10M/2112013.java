package com.google.gwt.i18n.client.impl.cldr;

/**
 * Implementation of DateTimeFormatInfo for the "ar_SY" locale.
 */
public class DateTimeFormatInfoImpl_ar_SY extends DateTimeFormatInfoImpl_ar_001 {

    @Override
    public int firstDayOfTheWeek() {
        return 6;
    }

    @Override
    public String[] monthsFull() {
        return new String[] { "كانون الثاني", "شباط", "آذار", "نيسان", "أيار", "حزيران", "تموز", "آب", "أيلول", "تشرين الأول", "تشرين الثاني", "كانون الأول" };
    }

    @Override
    public String[] monthsShort() {
        return new String[] { "كانون الثاني", "شباط", "آذار", "نيسان", "أيار", "حزيران", "تموز", "آب", "أيلول", "تشرين الأول", "تشرين الثاني", "كانون الأول" };
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

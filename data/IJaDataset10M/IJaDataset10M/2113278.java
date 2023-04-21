package com.google.gwt.i18n.client.impl.cldr;

/**
 * Implementation of DateTimeFormatInfo for the "zh_Hans_SG" locale.
 */
public class DateTimeFormatInfoImpl_zh_Hans_SG extends DateTimeFormatInfoImpl_zh_Hans {

    @Override
    public String dateFormatMedium() {
        return "y年M月d日";
    }

    @Override
    public String dateFormatShort() {
        return "dd/MM/yy";
    }

    @Override
    public String formatHour12Minute() {
        return "ahh:mm";
    }

    @Override
    public String formatHour24Minute() {
        return "HH:mm";
    }

    @Override
    public String formatHour24MinuteSecond() {
        return "HH:mm:ss";
    }

    @Override
    public String formatYear() {
        return "y";
    }

    @Override
    public String formatYearMonthNum() {
        return "y年M月";
    }

    @Override
    public String formatYearQuarterFull() {
        return "y年第QQQQ季度";
    }

    @Override
    public String formatYearQuarterShort() {
        return "y年第Q季度";
    }

    @Override
    public String[] monthsFull() {
        return new String[] { "一月", "二月", "三月", "四月", "五月", "六月", "七月", "八月", "九月", "十月", "十一月", "十二月" };
    }

    @Override
    public String[] monthsNarrowStandalone() {
        return new String[] { "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12" };
    }

    @Override
    public String[] monthsShortStandalone() {
        return new String[] { "1月", "2月", "3月", "4月", "5月", "6月", "7月", "8月", "9月", "10月", "11月", "12月" };
    }

    @Override
    public String[] quartersFull() {
        return new String[] { "第一季度", "第二季度", "第三季度", "第四季度" };
    }

    @Override
    public String[] quartersShort() {
        return new String[] { "1季度", "2季度", "3季度", "4季度" };
    }

    @Override
    public String timeFormatFull() {
        return "zzzzah:mm:ss";
    }

    @Override
    public String timeFormatLong() {
        return "ahh:mm:ssz";
    }

    @Override
    public String timeFormatShort() {
        return "ahh:mm";
    }
}

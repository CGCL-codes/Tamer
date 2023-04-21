package org.wdcode.common.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import org.wdcode.common.constants.DateConstants;
import org.wdcode.common.constants.StringConstants;
import org.wdcode.common.exception.CustomRuntimeException;
import org.wdcode.common.params.WdCommonParams;
import org.wdcode.common.tools.Conversion;

/**
 * 获得日期,日期类型和字符串类型之间的转化
 * @author WD
 * @since JDK6
 * @version 1.0 2009-03-01
 */
public final class DateUtil {

    private static final Calendar CALENDAR;

    static {
        CALENDAR = Calendar.getInstance();
    }

    /**
	 * 获得Calendar
	 * @return Calendar
	 */
    public static final Calendar getCalendar() {
        return CALENDAR;
    }

    /**
	 * 取得格式为默认格式的系统日期 返回的日期是字符串格式
	 * @return String 当前日期
	 */
    public static final String getDate() {
        return getDate(WdCommonParams.getDateFormat());
    }

    /**
	 * 取得time的日期
	 * @param time 毫秒
	 * @return String time的日期
	 */
    public static final String getDate(long time) {
        return dateToString(new Date(time));
    }

    /**
	 * 取得time的日期
	 * @param time 毫秒
	 * @param format 日期显示格式
	 * @return String time的日期
	 */
    public static final String getDate(long time, String format) {
        return dateToString(new Date(time), format);
    }

    /**
	 * 取得格式为yyyy-MM-dd HH:mm:ss 的系统日期 返回的日期是字符串格式
	 * @return String 当前日期
	 */
    public static final String getLongDate() {
        return getDate(DateConstants.FORMAT_Y_M_D_H_M_S);
    }

    /**
	 * 取得格式为yyyy-MM-dd 的系统日期 返回的日期是字符串格式
	 * @return String 当前日期
	 */
    public static final String getShortDate() {
        return getDate(DateConstants.FORMAT_YYYY_MM_DD);
    }

    /**
	 * 取得格式为HH:mm:ss 的系统日期 返回的日期是字符串格式
	 * @return String 当前日期
	 */
    public static final String getTheTime() {
        return getDate(DateConstants.FORMAT_HH_MM_SS);
    }

    /**
	 * 取得指定格式的系统日期 返回的日期是字符串格式
	 * @param format 日期格式，如 "yyyy-MM-dd HH:mm:sss"
	 * @return String 当前日期
	 */
    public static final String getDate(String format) {
        return dateToString(getCurrentDate(), format);
    }

    /**
	 * 取得当前时间 返回的是Date类型
	 * @return Date 当前日期
	 */
    public static final Date getCurrentDate() {
        return new Date();
    }

    /**
	 * 取得当前时间的毫秒数 返回的是long类型
	 * @return long
	 */
    public static final long getTime() {
        return System.currentTimeMillis();
    }

    /**
	 * 获得现在时间毫秒数
	 * @param date 要取的时间
	 * @return long
	 */
    public static final long getTime(Date date) {
        return date.getTime();
    }

    /**
	 * 获得现在时间毫秒数
	 * @param date 要取的时间
	 * @return long
	 */
    public static final long getTime(String dateString) {
        return getTime(stringToDate(dateString));
    }

    /**
	 * 根据时间变量返回时间字符串
	 * @param date 时间变量
	 * @param format 时间字符串样式
	 * @return 返回时间字符串
	 */
    public static final String dateToString(Date date, String format) {
        return new SimpleDateFormat(format).format(date);
    }

    /**
	 * 根据给定的时间返回相对的字符串 默认格式
	 * @param date 日期
	 * @return String 转换后的日期
	 */
    public static final String dateToString(Date date) {
        return dateToString(date, WdCommonParams.getDateFormat());
    }

    /**
	 * 字符串转换为日期 默认格式
	 * @param dateString 字符串
	 * @return Date 转换后的日期
	 */
    public static final Date stringToDate(String dateString) {
        return stringToDate(dateString, WdCommonParams.getDateFormat());
    }

    /**
	 * 字符串转换为日期 dateString为空或异常返回当前时间
	 * @param dateString 字符串
	 * @param format 日期格式
	 * @return Date 转换后的日期
	 */
    public static final Date stringToDate(String dateString, String format) {
        try {
            DateFormat df = CommonUtil.isEmpty(format) ? new SimpleDateFormat() : new SimpleDateFormat(format);
            return df.parse(dateString);
        } catch (ParseException e) {
            throw new CustomRuntimeException(e);
        }
    }

    /**
	 * 计算两个日期相差的天数 传入的日期格式是 默认格式
	 * @param oneDate 开始日期
	 * @param twoDate 结束日期
	 * @return 返回两个日期相差的天数
	 */
    public static final int marginDay(String oneDate, String twoDate) {
        return marginDay(oneDate, twoDate, WdCommonParams.getDateFormat());
    }

    /**
	 * 计算两个日期相差的天数
	 * @param oneDate 开始日期
	 * @param twoDate 结束日期
	 * @param format 日期格式
	 * @return 返回两个日期相差的天数
	 */
    public static final int marginDay(String oneDate, String twoDate, String format) {
        return marginDay(stringToDate(oneDate, format), stringToDate(twoDate, format));
    }

    /**
	 * 计算两个日期相差的天数
	 * @param oneDate 日期
	 * @param twoDate 日期
	 * @return 返回两个日期相差的天数
	 */
    public static final int marginDay(Date oneDate, Date twoDate) {
        return Conversion.toInt((twoDate.getTime() - oneDate.getTime()) / DateConstants.TIME_DAY);
    }

    /**
	 * 两个日期相隔几个月 默认日期格式
	 * @param oneDate 日期
	 * @param twoDate 日期
	 * @return 返回两个日期相隔几个月
	 */
    public static final int marginMonth(String oneDate, String twoDate) {
        return marginMonth(oneDate, twoDate, WdCommonParams.getDateFormat());
    }

    /**
	 * 两个日期相隔几个月
	 * @param oneDate 日期
	 * @param twoDate 日期
	 * @param format 日期格式
	 * @return 返回两个日期相隔几个月
	 */
    public static final int marginMonth(String oneDate, String twoDate, String format) {
        return marginMonth(stringToDate(oneDate, format), stringToDate(twoDate, format));
    }

    /**
	 * 两个日期相隔几个月
	 * @param oneDate 日期
	 * @param twoDate 日期
	 * @return 返回两个日期相隔几个月
	 */
    public static final int marginMonth(Date oneDate, Date twoDate) {
        int oneYear = getYear(oneDate);
        int oneMonth = getMonth(oneDate);
        int twoYear = getYear(twoDate);
        int twoMonth = getMonth(twoDate);
        if (oneMonth == 0) {
            oneMonth = 12;
            oneYear -= 1;
        }
        if (twoMonth == 0) {
            twoMonth = 12;
            twoYear -= 1;
        }
        return (twoYear - oneYear) * 12 + (twoMonth - oneMonth);
    }

    /**
	 * 根据日期取得星期几 周日返回的是0
	 * @return 返回星期几
	 */
    public static final int getDayOfWeek() {
        return getDayOfWeek(getCurrentDate());
    }

    /**
	 * 根据日期取得星期几 默认格式 周日返回的是0
	 * @param date 日期字符串
	 * @return 返回星期几
	 */
    public static final int getDayOfWeek(String date) {
        return getDayOfWeek(date, WdCommonParams.getDateFormat());
    }

    /**
	 * 根据日期取得星期几 周日返回的是0
	 * @param date 日期字符串
	 * @param format 日期格式
	 * @return 返回星期几
	 */
    public static final int getDayOfWeek(String date, String format) {
        return getDayOfWeek(stringToDate(date, format));
    }

    /**
	 * 根据日期取得星期几 周日返回的是0
	 * @param date 日期
	 * @return 返回星期几
	 */
    public static final int getDayOfWeek(Date date) {
        return get(date, Calendar.DAY_OF_WEEK) - 1;
    }

    /**
	 * 根据本周的的日期
	 * @return 本周的日期
	 */
    public static final String[] getDayOfWeeks() {
        return getDayOfWeeks(getDate());
    }

    /**
	 * 根据日期取得当前星期7天的日期 默认格式
	 * @param date 日期字符串
	 * @return 本周的日期
	 */
    public static final String[] getDayOfWeeks(String date) {
        return getDayOfWeeks(date, WdCommonParams.getDateFormat());
    }

    /**
	 * 根据日期取得当前星期7天的日期 默认格式
	 * @param date 日期字符串
	 * @param format 日期格式
	 * @return 本周的日期
	 */
    public static final String[] getDayOfWeeks(String date, String format) {
        return getDayOfWeeks(stringToDate(date, format), format);
    }

    /**
	 * 根据日期取得当前星期7天日期
	 * @param date 日期
	 * @return 本周的日期
	 */
    public static final String[] getDayOfWeeks(Date date) {
        return getDayOfWeeks(date, WdCommonParams.getDateFormat());
    }

    /**
	 * 根据日期取得当前星期7天日期
	 * @param date 日期
	 * @param format 返回的日期格式
	 * @return 本周的日期
	 */
    public static final String[] getDayOfWeeks(Date date, String format) {
        String[] weekInfo = new String[7];
        int week = getDayOfWeek(date);
        for (int i = 1; i < 8; i++) {
            weekInfo[i - 1] = getDate(date, -(week - i), format);
        }
        return weekInfo;
    }

    /**
	 * 根据日期取得一年的第N周
	 * @return 返回第N周
	 */
    public static final int getWeek() {
        return getWeek(getCurrentDate());
    }

    /**
	 * 根据日期取得一年的第N周 默认格式
	 * @param date 日期字符串
	 * @return 返回第N周
	 */
    public static final int getWeek(String date) {
        return getWeek(date, WdCommonParams.getDateFormat());
    }

    /**
	 * 根据日期取得一年的第N周
	 * @param date 日期字符串
	 * @param format 日期格式
	 * @return 返回第N周
	 */
    public static final int getWeek(String date, String format) {
        return getWeek(stringToDate(date, format));
    }

    /**
	 * 根据日期取得一年的第N周
	 * @param date 日期
	 * @return 返回第N周
	 */
    public static final int getWeek(Date date) {
        return get(date, Calendar.WEEK_OF_YEAR);
    }

    /**
	 * 根据日期取得一年的第N天
	 * @return 返回第N天
	 */
    public static final int getDayOfYear() {
        return getDayOfYear(getCurrentDate());
    }

    /**
	 * 根据日期取得一年的第N天
	 * @param date 日期字符串
	 * @return 返回第N天
	 */
    public static final int getDayOfYear(String date) {
        return getDayOfYear(date, WdCommonParams.getDateFormat());
    }

    /**
	 * 根据日期取得一年的第N天
	 * @param date 日期字符串
	 * @param format 日期格式
	 * @return 返回第N天
	 */
    public static final int getDayOfYear(String date, String format) {
        return getDayOfYear(stringToDate(date, format));
    }

    /**
	 * 根据日期取得一年的第N天
	 * @param date 日期
	 * @return 返回第N天
	 */
    public static final int getDayOfYear(Date date) {
        return get(date, Calendar.DAY_OF_YEAR);
    }

    /**
	 * 取得当前日期的N天后的日期(如果想获得前几天的日期用-number) 默认格式
	 * @param number N天
	 * @return N天的日期
	 */
    public static final String getDate(int number) {
        return getDate(number, WdCommonParams.getDateFormat());
    }

    /**
	 * 取得当前日期的N天后的日期(如果想获得前几天的日期用-number)
	 * @param number N天
	 * @param format 日期格式
	 * @return N天的日期
	 */
    public static final String getDate(int number, String format) {
        return getDate(getCurrentDate(), number, format);
    }

    /**
	 * 取得当前日期的N天后的日期(如果想获得前几天的日期用-number)
	 * @param date 日期字符串
	 * @param number N天
	 * @return N天的日期
	 */
    public static final String getDate(String date, int number) {
        return getDate(date, number, WdCommonParams.getDateFormat());
    }

    /**
	 * 取得当前日期的N天后的日期(如果想获得前几天的日期用-number)
	 * @param date 日期字符串
	 * @param number N天
	 * @param format 日期格式
	 * @return N天的日期
	 */
    public static final String getDate(String date, int number, String format) {
        return getDate(stringToDate(date, format), number, format);
    }

    /**
	 * 取得当前日期的N天后的日期(如果想获得前几天的日期用-number)
	 * @param date 日期
	 * @param number N天
	 * @return N天的日期
	 */
    public static final String getDate(Date date, int number) {
        return getDate(date, number, WdCommonParams.getDateFormat());
    }

    /**
	 * 取得当前日期的N天后的日期(如果想获得前几天的日期用-number)
	 * @param date 日期
	 * @param number N天
	 * @param format 日期格式
	 * @return N天的日期
	 */
    public static final String getDate(Date date, int number, String format) {
        return discrepancy(date, number, format, Calendar.DAY_OF_MONTH);
    }

    /**
	 * 取得当前日期的N月后的日期(如果想获得前几月的日期用-number) 默认格式
	 * @param number N月
	 * @return N月的日期
	 */
    public static final String getMonth(int number) {
        return getMonth(number, WdCommonParams.getDateFormat());
    }

    /**
	 * 取得当前日期的N月后的日期(如果想获得前几月的日期用-number)
	 * @param number N月
	 * @param format 日期格式
	 * @return N月的日期
	 */
    public static final String getMonth(int number, String format) {
        return getMonth(getCurrentDate(), number, format);
    }

    /**
	 * 取得当前日期的N月后的日期(如果想获得前几月的日期用-number)
	 * @param date 日期字符串
	 * @param number N月
	 * @return N月的日期
	 */
    public static final String getMonth(String date, int number) {
        return getMonth(date, number, WdCommonParams.getDateFormat());
    }

    /**
	 * 取得当前日期的N月后的日期(如果想获得前几月的日期用-number)
	 * @param date 日期字符串
	 * @param number N天
	 * @param format 日期格式
	 * @return N天的日期
	 */
    public static final String getMonth(String date, int number, String format) {
        return getMonth(stringToDate(date, format), number, format);
    }

    /**
	 * 取得当前日期的N月后的日期(如果想获得前几月的日期用-number)
	 * @param date 日期
	 * @param number N月
	 * @return N月的日期
	 */
    public static final String getMonth(Date date, int number) {
        return getMonth(date, number, WdCommonParams.getDateFormat());
    }

    /**
	 * 取得当前日期的N月后的日期(如果想获得前几月的日期用-number)
	 * @param date 日期
	 * @param number N月
	 * @param format 日期格式
	 * @return N月的日期
	 */
    public static final String getMonth(Date date, int number, String format) {
        return discrepancy(date, number, format, Calendar.MONTH);
    }

    /**
	 * 获得当前日期的月份所有日期
	 * @return 返回本月的所有日期
	 */
    public static final String[] getMonths() {
        return getMonths(getDate());
    }

    /**
	 * 根据指定日期的月份所有日期
	 * @param date 日期字符串
	 * @return 返回本月的所有日期
	 */
    public static final String[] getMonths(String date) {
        return getMonths(stringToDate(date, WdCommonParams.getDateFormat()), WdCommonParams.getDateFormat());
    }

    /**
	 * 根据指定日期的月份所有日期
	 * @param date 日期字符串
	 * @param format 日期格式
	 * @return 返回本月的所有日期
	 */
    public static final String[] getMonths(String date, String format) {
        return getMonths(stringToDate(date, format), format);
    }

    /**
	 * 根据指定日期的月份所有日期
	 * @param date 日期
	 * @return 回本月的所有日期 默认格式
	 */
    public static final String[] getMonths(Date date) {
        return getMonths(date, WdCommonParams.getDateFormat());
    }

    /**
	 * 根据指定日期的月份所有日期
	 * @param date 日期
	 * @param format 日期格式
	 * @return 回本月的所有日期
	 */
    public static final String[] getMonths(Date date, String format) {
        int day = getDay(date);
        int maxDay = getMonthHaveDay(date);
        String[] monthInfo = new String[maxDay];
        for (int i = 1; i <= maxDay; i++) {
            monthInfo[i - 1] = getDate(date, -(day - i), format);
        }
        return monthInfo;
    }

    /**
	 * 取得当前日期的N年后的日期(如果想获得前几年的日期用-number) 默认格式
	 * @param number N年
	 * @return N年的日期
	 */
    public static final String getYear(int number) {
        return getYear(number, WdCommonParams.getDateFormat());
    }

    /**
	 * 取得当前日期的N年后的日期(如果想获得前几年的日期用-number)
	 * @param number N年
	 * @param format 日期格式
	 * @return N年的日期
	 */
    public static final String getYear(int number, String format) {
        return getYear(getCurrentDate(), number, format);
    }

    /**
	 * 取得当前日期的N年后的日期(如果想获得前几年的日期用-number)
	 * @param date 日期字符串
	 * @param number N年
	 * @return N年的日期
	 */
    public static final String getYear(String date, int number) {
        return getYear(date, number, WdCommonParams.getDateFormat());
    }

    /**
	 * 取得当前日期的N年后的日期(如果想获得前几年的日期用-number)
	 * @param date 日期字符串
	 * @param number N天
	 * @param format 日期格式
	 * @return N天的日期
	 */
    public static final String getYear(String date, int number, String format) {
        return getYear(stringToDate(date, format), number, format);
    }

    /**
	 * 取得当前日期的N年后的日期(如果想获得前几年的日期用-number)
	 * @param date 日期
	 * @param number N年
	 * @return N年的日期
	 */
    public static final String getYear(Date date, int number) {
        return getYear(date, number, WdCommonParams.getDateFormat());
    }

    /**
	 * 取得当前日期的N年后的日期(如果想获得前几年的日期用-number)
	 * @param date 日期
	 * @param number N年
	 * @param format 日期格式
	 * @return N年的日期
	 */
    public static final String getYear(Date date, int number, String format) {
        return discrepancy(date, number, format, Calendar.YEAR);
    }

    /**
	 * 日期字符串格式转换
	 * @param src 日期字符串
	 * @param srcfmt 源日期格式
	 * @param desfmt 目标日期格式
	 * @return 转换后的日期
	 */
    public static final String format(String src, String srcfmt, String desfmt) {
        return dateToString(stringToDate(src, srcfmt), desfmt);
    }

    /**
	 * 取指定日期的年份
	 * @param date 日期
	 * @return 年
	 */
    public static final int getYear(Date date) {
        return get(date, Calendar.YEAR);
    }

    /**
	 * 取指定日期的月份
	 * @param date 日期
	 * @return 月
	 */
    public static final int getMonth(Date date) {
        return get(date, Calendar.MONTH);
    }

    /**
	 * 取指定日期月份的日
	 * @param date 日期
	 * @return 日
	 */
    public static final int getDay(Date date) {
        return get(date, Calendar.DATE);
    }

    /**
	 * 获取当前 时
	 * @param date 日期
	 * @return 时
	 */
    public static final int getHour(Date date) {
        return get(date, Calendar.HOUR);
    }

    /**
	 * 获取当前 分
	 * @param date 日期
	 * @return 分
	 */
    public static final int getMinute(Date date) {
        return get(date, Calendar.MINUTE);
    }

    /**
	 * 获取当前 秒
	 * @param date 日期
	 * @return 秒
	 */
    public static final int getSecond(Date date) {
        return get(date, Calendar.SECOND);
    }

    /**
	 * 获取当前 年
	 * @return 年
	 */
    public static final int getYear() {
        return getYear(getCurrentDate());
    }

    /**
	 * 获取当前 月
	 * @return 月
	 */
    public static final int getMonth() {
        return getMonth(getCurrentDate());
    }

    /**
	 * 获取当前月份的 日
	 * @return 日
	 */
    public static final int getDay() {
        return getDay(getCurrentDate());
    }

    /**
	 * 获取当前 时
	 * @return 时
	 */
    public static final int getHour() {
        return getHour(getCurrentDate());
    }

    /**
	 * 获取当前 分
	 * @return 分
	 */
    public static final int getMinute() {
        return getMinute(getCurrentDate());
    }

    /**
	 * 获取当前 秒
	 * @return 秒
	 */
    public static final int getSecond() {
        return getSecond(getCurrentDate());
    }

    /**
	 * 获得当前月份有几天
	 * @return 几天
	 */
    public static final int getMonthHaveDay() {
        return getMonthHaveDay(getCurrentDate());
    }

    /**
	 * 获得指定月份有几天
	 * @param date 日期
	 * @return 几天
	 */
    public static final int getMonthHaveDay(Date date) {
        return getActualMaximum(date, Calendar.DAY_OF_MONTH);
    }

    /**
	 * 获得当前年份有几天
	 * @return 几天
	 */
    public static final int getYearHaveDay() {
        return getYearHaveDay(getCurrentDate());
    }

    /**
	 * 获得指定年份有几天
	 * @param date 日期
	 * @return 几天
	 */
    public static final int getYearHaveDay(Date date) {
        return getActualMaximum(date, Calendar.DAY_OF_YEAR);
    }

    /**
	 * 转换时间到字符串格式 例如 输入300 转成00:05:00
	 * @param time 要转换的时间 单位秒
	 * @return 转换完格式的字符串
	 */
    public static final String secondToTime(int time) {
        StringBuilder timeString = new StringBuilder();
        int hour = 0;
        int minute = 0;
        int second = 0;
        boolean isNegative = false;
        if (time < 0) {
            time *= -1;
            isNegative = true;
        }
        if (time >= 60) {
            minute = time / 60;
            second = time % 60;
            if (minute >= 60) {
                hour = minute / 60;
                minute = minute % 60;
            }
        } else {
            second = time;
        }
        if (hour < 10) {
            timeString.append(StringConstants.ZERO);
        }
        timeString.append(hour);
        timeString.append(":");
        if (minute < 10) {
            timeString.append(StringConstants.ZERO);
        }
        timeString.append(minute);
        timeString.append(":");
        if (second < 10) {
            timeString.append(StringConstants.ZERO);
        }
        if (isNegative) {
            time *= -1;
            timeString.append("-");
            timeString.append(timeString);
        }
        return timeString.toString();
    }

    /**
	 * 获得日期格式
	 * @return format
	 */
    public static final String getFormat() {
        return WdCommonParams.getDateFormat();
    }

    /**
	 * 设置日期格式
	 * @param format 日期格式
	 */
    public static final void setFormat(String format) {
        WdCommonParams.setDateFormat(format);
    }

    private static int get(Date date, int field) {
        getCalendar().setTime(date);
        return getCalendar().get(field);
    }

    private static String discrepancy(Date date, int number, String format, int field) {
        getCalendar().setTime(date);
        getCalendar().add(field, number);
        return new SimpleDateFormat(format).format(getCalendar().getTime());
    }

    private static int getActualMaximum(Date date, int field) {
        getCalendar().setTime(date);
        return getCalendar().getActualMaximum(field);
    }

    private DateUtil() {
    }
}

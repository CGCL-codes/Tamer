package com.handy.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * <p>
 * 此类主要用来取得本地系统的系统时间并用下面5种格式显示.
 * </p>
 * 1. YYMMDDHH 8位 <br>
 * 2. YYMMDDHHmm 10位 <br>
 * 3. YYMMDDHHmmss 12位 <br>
 * 4. YYYYMMDDHHmmss 14位 <br>
 * 5. YYMMDDHHmmssxxx 15位 (最后的xxx 是毫秒) <br>
 */
public class DateTool {

    public static void main(String[] args) {
        System.out.println(format(new java.util.Date(), true));
        System.out.println(format(new java.util.Date(), false));
        System.out.println(DateTool.getTimeString(DateTool.YYMMDDhhmmssxxx));
    }

    /**
	 * 格式化显示日期型数据,将指定日期格式化为"yyyy-mm-dd hh:mm:ss"字串。
	 * 
	 * @param date
	 *            日期型数据
	 * @param bShowTimePart
	 *            是否显示时间部分
	 * @return
	 */
    public static String format(Date dt_in, boolean bShowTimePart) {
        if (bShowTimePart) return (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(dt_in); else return (new SimpleDateFormat("yyyy-MM-dd")).format(dt_in);
    }

    public static Date parseDate(String date, String pattern) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("", Locale.getDefault());
        dateFormat.applyPattern(pattern);
        Date sDate = new Date();
        try {
            sDate = dateFormat.parse(date);
        } catch (ParseException e) {
            log.error(e.getMessage());
        }
        return sDate;
    }

    /**
	 * 取得本地系统的时间，时间格式由参数决定.
	 * 
	 * @param format
	 *            时间格式由常量决定
	 * @return String 具有format格式的字符串
	 */
    public static String getTimeString(int format) {
        StringBuffer cTime = new StringBuffer(10);
        Calendar time = Calendar.getInstance();
        int miltime = time.get(Calendar.MILLISECOND);
        String sSecond = "", sMinute = "", sHour = "", sDay = "", sMonth = "";
        int second = time.get(Calendar.SECOND);
        if (second < 10) sSecond = "0" + second; else sSecond = "" + second;
        int minute = time.get(Calendar.MINUTE);
        if (minute < 10) sMinute = "0" + minute; else sMinute = "" + minute;
        int hour = time.get(Calendar.HOUR_OF_DAY);
        if (hour < 10) sHour = "0" + hour; else sHour = "" + hour;
        int day = time.get(Calendar.DAY_OF_MONTH);
        if (day < 10) sDay = "0" + day; else sDay = "" + day;
        int month = time.get(Calendar.MONTH) + 1;
        if (month < 10) sMonth = "0" + month; else sMonth = "" + month;
        int year = time.get(Calendar.YEAR);
        if (format == 11) {
            return sMonth + "-" + sDay + " " + sHour + ":" + sMinute + ":" + sSecond;
        }
        if (format == 16) {
            return "" + sMonth + sDay + sHour + sMinute + sSecond;
        }
        if (format != 14) {
            if (year >= 2000) year = year - 2000; else year = year - 1900;
        }
        if (format >= 2) {
            if (format == 14) cTime.append(year); else if (format != 13) cTime.append(getFormatTime(year, 2));
        }
        if (format >= 4) cTime.append(getFormatTime(month, 2));
        if (format >= 6) cTime.append(getFormatTime(day, 2));
        if (format >= 8) cTime.append(getFormatTime(hour, 2));
        if (format >= 10) cTime.append(getFormatTime(minute, 2));
        if (format >= 12) cTime.append(getFormatTime(second, 2));
        if (format >= 15) cTime.append(getFormatTime(miltime, 3));
        return cTime.toString();
    }

    /**
	 * 产生任意位的字符串.
	 * 
	 * @param time
	 *            要转换格式的时间
	 * @param format
	 *            转换的格式
	 * @return String 转换的时间
	 */
    private static String getFormatTime(int time, int format) {
        StringBuffer numm = new StringBuffer();
        int length = String.valueOf(time).length();
        if (format < length) return null;
        for (int i = 0; i < format - length; i++) {
            numm.append("0");
        }
        numm.append(time);
        return numm.toString().trim();
    }

    public static final int _BMMDDhhmmss = 16;

    public static final int YYMMDDhhmmssxxx = 15;

    public static final int YYYYMMDDhhmmss = 14;

    public static final int YYMMDDhhmmss = 12;

    public static final int MMDDhhmmss = 13;

    public static final int _MMDDhhmmss = 11;

    public static final int YYMMDDhhmm = 10;

    public static final int YYMMDDhh = 8;

    private static Log log = LogFactory.getLog(DateTool.class);
}

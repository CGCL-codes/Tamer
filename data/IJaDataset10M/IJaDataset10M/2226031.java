package ast.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * @Description:
 * @Author: zhangzuoqiang
 * @Date: Sep 20, 2011
 */
public class DateUtils {

    public static final String FORMAT_YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";

    public static final String FORMAT_YYYY_MM_DD = "yyyy-MM-dd";

    public static final String FORMAT_YYYYMMDD = "yyyyMMdd";

    public static final String FORMAT_YYYY_N_MM_Y_DD_R = "yyyy年MM月dd日";

    /**
	 * 转换Date的格式
	 * 
	 * @param date
	 * @param format
	 * @return
	 */
    public static String changeFormat(String date, String format) {
        String result = "";
        try {
            final SimpleDateFormat formater = new SimpleDateFormat(format);
            result = format(formater.parse(date), format);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
	 * 格式化日期
	 * 
	 * @param date
	 *            日期
	 * @param format
	 *            格式
	 * @return
	 */
    public static String format(Date date, String format) {
        final SimpleDateFormat formater = new SimpleDateFormat(format);
        return formater.format(date);
    }

    /**
	 * 计算时间差
	 * 
	 * @param start
	 *            开始时间
	 * @param end
	 *            结束时间
	 * @return 相差(小时)
	 */
    public static long dateDiff(Date start, Date end) {
        int divide = 3600000;
        long diff = end.getTime() - start.getTime();
        return (diff % divide) != 0 ? diff / divide + 1 : diff / divide;
    }

    /**
	 * 解析时间
	 * 
	 * @param date
	 *            时间字符串
	 * @param format
	 *            格式
	 * @return 日期
	 */
    public static Date parseDate(String date, String format) {
        try {
            final SimpleDateFormat formater = new SimpleDateFormat(format);
            return formater.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
	 * 解析日期
	 * 
	 * @param date
	 *            日期
	 * @param format
	 *            格式
	 * @return 日期
	 */
    public static Date parseDate(Date date, String format) {
        try {
            final SimpleDateFormat formater = new SimpleDateFormat(format);
            return formater.parse(format(date, format));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
	 * 判断指定日期是否在某一时间段内
	 * 
	 * @param src
	 *            指定日期
	 * @param start
	 *            开始日期
	 * @param end
	 *            结束日期
	 * @return
	 */
    public static boolean isBetween(Date src, Date start, Date end) {
        return start.getTime() <= src.getTime() && src.getTime() <= end.getTime();
    }

    /**
	 * 判断当前时间是否在某一时间段内
	 * 
	 * @param start
	 *            开始日期
	 * @param end
	 *            结束日期
	 * @return
	 */
    public static boolean isBetween(Date start, Date end) {
        return isBetween(Calendar.getInstance().getTime(), start, end);
    }

    public static String getCurrentDate() {
        String result = "";
        Calendar todaysDate = new GregorianCalendar();
        int year = todaysDate.get(Calendar.YEAR);
        int month = todaysDate.get(Calendar.MONTH) + 1;
        int day = todaysDate.get(Calendar.DAY_OF_MONTH);
        int hourOfDay = todaysDate.get(Calendar.HOUR_OF_DAY);
        int minute = todaysDate.get(Calendar.MINUTE);
        int second = todaysDate.get(Calendar.SECOND);
        result = year + "-" + month + "-" + day + " " + hourOfDay + ":" + minute + ":" + second;
        return result;
    }
}

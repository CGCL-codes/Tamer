package com.app.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import org.apache.log4j.Logger;

public class DateTimeUtil {

    /**
	 * Logger for this class
	 */
    private static final Logger logger = Logger.getLogger(DateTimeUtil.class);

    /**
	 * 
	 */
    public static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd";

    public static final String DEFAULT_DATE_FORMAT_ZN = "yyyy年MM月dd日";

    /**
	 * 
	 */
    public static final String DEFAULT_DATE_FORMAT_EN = "MM/dd/yyyy";

    /**
	 */
    public static final String DEFAULT_PAGE_TIME_FORMAT = "HH:mm:ss";

    /**
	 */
    public static final String DEFAULT_DATETIME_FORMAT = "yyyy/MM/dd HH:mm";

    /**
	 */
    public static final String DEFAULT_DATETIME_FORMAT_EN = "MM/dd/yyyy HH:mm";

    /**
	 */
    public static final String DEFAULT_DATETIME_SS_FORMAT = "yyyy/MM/dd HH:mm:ss";

    /**
	 */
    public static final String DEFAULT_DATETIME_SS_FORMAT_EN = "MM/dd/yyyy HH:mm:ss";

    /**
	 */
    public static final String DEFAULT_PAGE_FORMAT = "yyyyMMddHHmm";

    /**
	 */
    public static final String DEFAULT_OA_SYSTEM_FORMAT = "yyyyMMddHHmmss";

    /**
	 */
    public static final String DEFAULT_OA_PAGE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    /**
	 */
    public static final String DEFAULT_SYSTEM_TIME_FORMAT = "HHmmss";

    /**
	 */
    public static final String DEFAULT_SYSTEM_DATE_FORMAT = "yyyyMMdd";

    /**
	 */
    public static final String DATE_WEEK_FORMAT = "yyyy-MM-dd";

    public static final String DATE_YEAR = "yyyy";

    public static final String DEFAULT_KNOW_FORMAT = "yyyy-MM-dd HH:mm:ss";

    private DateTimeUtil() {
    }

    public static Date getNow() {
        return Calendar.getInstance().getTime();
    }

    public static String getDate() {
        Locale locale = Locale.getDefault();
        if (locale.getLanguage().toLowerCase().equals("zh")) {
            return getDateTime(DEFAULT_DATE_FORMAT);
        } else if (locale.getLanguage().toLowerCase().equals("en")) {
            return getDateTime(DEFAULT_DATE_FORMAT_EN);
        } else {
            return getDateTime(DEFAULT_DATE_FORMAT);
        }
    }

    public static String getDate(String pars) {
        return getDateTime(pars);
    }

    public static String getDateZn() {
        return getDateTime(DEFAULT_DATE_FORMAT_ZN);
    }

    public static String getDateTime() {
        Locale locale = Locale.getDefault();
        if (locale.getLanguage().toLowerCase().equals("zh")) {
            return getDateTime(DEFAULT_DATETIME_FORMAT);
        } else if (locale.getLanguage().toLowerCase().equals("en")) {
            return getDateTime(DEFAULT_DATETIME_FORMAT_EN);
        } else {
            return getDateTime(DEFAULT_DATETIME_FORMAT);
        }
    }

    public static String getDateTimeSecond() {
        Locale locale = Locale.getDefault();
        if (locale.getLanguage().toLowerCase().equals("zh")) {
            return getDateTime(DEFAULT_DATETIME_SS_FORMAT);
        } else if (locale.getLanguage().toLowerCase().equals("en")) {
            return getDateTime(DEFAULT_DATETIME_SS_FORMAT_EN);
        } else {
            return getDateTime(DEFAULT_DATETIME_SS_FORMAT);
        }
    }

    public static String getDateTime(String pattern) {
        Date datetime = Calendar.getInstance().getTime();
        return getDateTime(datetime, pattern);
    }

    public static String getDateTime(Date date, String pattern) {
        if (null == pattern || "".equals(pattern)) {
            pattern = DEFAULT_DATETIME_FORMAT;
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
        return dateFormat.format(date);
    }

    public static String getDate(Date date, String pattern, Locale locale) {
        if (null == locale) {
            locale = Locale.getDefault();
        }
        if (null == pattern || "".equals(pattern)) {
            if (locale.getLanguage().toLowerCase().equals("zh")) {
                pattern = DEFAULT_DATE_FORMAT;
            } else if (locale.getLanguage().toLowerCase().equals("en")) {
                pattern = DEFAULT_DATE_FORMAT_EN;
            }
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
        return dateFormat.format(date);
    }

    public static String getDateZn(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(DEFAULT_DATE_FORMAT_ZN);
        return dateFormat.format(date);
    }

    public static String getDate(Date date, String format) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        return dateFormat.format(date);
    }

    public static String getDateTime(Date date, String pattern, Locale locale) {
        if (null == locale) {
            locale = Locale.getDefault();
        }
        if (null == pattern || "".equals(pattern)) {
            if (locale.getLanguage().toLowerCase().equals("zh")) {
                pattern = DEFAULT_DATETIME_FORMAT;
            } else if (locale.getLanguage().toLowerCase().equals("en")) {
                pattern = DEFAULT_DATETIME_FORMAT_EN;
            }
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
        return dateFormat.format(date);
    }

    public static String getDbDate(String date) {
        String t_date = "";
        Locale locale = Locale.getDefault();
        if (locale.getLanguage().toLowerCase().equals("zh")) {
            t_date = convertFormat(date, DEFAULT_DATE_FORMAT, DEFAULT_SYSTEM_DATE_FORMAT);
        } else if (locale.getLanguage().toLowerCase().equals("en")) {
            t_date = convertFormat(date, DEFAULT_DATE_FORMAT_EN, DEFAULT_SYSTEM_DATE_FORMAT);
        }
        return t_date;
    }

    public static String getDbDateTime(String datetime) {
        String t_datetime = "";
        Locale locale = Locale.getDefault();
        if (locale.getLanguage().toLowerCase().equals("zh")) {
            t_datetime = convertFormat(datetime, DEFAULT_DATETIME_FORMAT, DEFAULT_PAGE_FORMAT);
        } else if (locale.getLanguage().toLowerCase().equals("en")) {
            t_datetime = convertFormat(datetime, DEFAULT_DATETIME_FORMAT_EN, DEFAULT_PAGE_FORMAT);
        }
        return t_datetime;
    }

    public static String getDbTime(String time) {
        return convertFormat(time, DEFAULT_PAGE_TIME_FORMAT, DEFAULT_SYSTEM_TIME_FORMAT);
    }

    public static String getPageDate(String date) {
        String t_date = "";
        Locale locale = Locale.getDefault();
        if (locale.getLanguage().toLowerCase().equals("zh")) {
            t_date = convertFormat(date, DEFAULT_SYSTEM_DATE_FORMAT, DEFAULT_DATE_FORMAT);
        } else if (locale.getLanguage().toLowerCase().equals("en")) {
            t_date = convertFormat(date, DEFAULT_SYSTEM_DATE_FORMAT, DEFAULT_DATE_FORMAT_EN);
        }
        return t_date;
    }

    public static String getPageDateTime(String datetime) {
        String t_datetime = "";
        Locale locale = Locale.getDefault();
        if (locale.getLanguage().toLowerCase().equals("zh")) {
            t_datetime = convertFormat(datetime, DEFAULT_PAGE_FORMAT, DEFAULT_DATETIME_FORMAT);
        } else if (locale.getLanguage().toLowerCase().equals("en")) {
            t_datetime = convertFormat(datetime, DEFAULT_PAGE_FORMAT, DEFAULT_DATETIME_FORMAT_EN);
        }
        return t_datetime;
    }

    public static String getPageDateTimeForChange(String datetime, String source_FORMAT, String DATETIME_FORMAT) {
        String t_datetime = "";
        String source_FORMAT_en = "yyyyMMddHHmmss";
        String DATETIME_FORMAT_en = "MM/dd/yyyy HH:mm:ss";
        Locale locale = Locale.getDefault();
        if (locale.getLanguage().toLowerCase().equals("zh")) {
            t_datetime = convertFormat(datetime, source_FORMAT, DATETIME_FORMAT);
        } else if (locale.getLanguage().toLowerCase().equals("en")) {
            t_datetime = convertFormat(datetime, source_FORMAT_en, DATETIME_FORMAT_en);
        }
        return t_datetime;
    }

    public static String getPageTime(String time) {
        return convertFormat(time, DEFAULT_SYSTEM_TIME_FORMAT, DEFAULT_PAGE_TIME_FORMAT);
    }

    public static String convertFormat(String datestr, String pattern, String targetpattern) {
        if (logger.isDebugEnabled()) {
            logger.debug("convertFormat(String, String, String) - start");
        }
        String t_date = "";
        Date date = null;
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
        try {
            date = dateFormat.parse(datestr);
            dateFormat = new SimpleDateFormat(targetpattern);
            t_date = dateFormat.format(date);
        } catch (ParseException e) {
            logger.error("convertFormat(String, String, String)", e);
            e.printStackTrace();
            if (logger.isDebugEnabled()) {
                logger.debug("convertFormat(String, String, String) - end");
            }
            return null;
        } catch (NullPointerException e) {
            logger.error("convertFormat(String, String, String)", e);
            e.printStackTrace();
            if (logger.isDebugEnabled()) {
                logger.debug("convertFormat(String, String, String) - end");
            }
            return null;
        }
        if (logger.isDebugEnabled()) {
            logger.debug("convertFormat(String, String, String) - end");
        }
        return t_date;
    }

    public static int getCurrentYear() {
        return Calendar.getInstance().get(Calendar.YEAR);
    }

    public static int getCurrentMonth() {
        return Calendar.getInstance().get(Calendar.MONTH) + 1;
    }

    public static int getCurrentDay() {
        return Calendar.getInstance().get(Calendar.DATE);
    }

    public static Date addDays(int days) {
        return add(getNow(), days, Calendar.DATE);
    }

    public static Date addDays(Date date, int days) {
        return add(date, days, Calendar.DATE);
    }

    public static Date addMonths(int months) {
        return add(getNow(), months, Calendar.MONTH);
    }

    public static Date addMonths(Date date, int months) {
        return add(date, months, Calendar.MONTH);
    }

    public static Date add(Date date, int amount, int field) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(field, amount);
        return calendar.getTime();
    }

    public static long diffDays(Date one, Date two) {
        return (one.getTime() - two.getTime()) / (24 * 60 * 60 * 1000);
    }

    public static long diffTimes(Date one, Date two) {
        return (one.getTime() - two.getTime());
    }

    public static int diffMonths(Date one, Date two) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(one);
        int yearOne = calendar.get(Calendar.YEAR);
        int monthOne = calendar.get(Calendar.MONDAY);
        calendar.setTime(two);
        int yearTwo = calendar.get(Calendar.YEAR);
        int monthTwo = calendar.get(Calendar.MONDAY);
        return (yearOne - yearTwo) * 12 + (monthOne - monthTwo);
    }

    public static Date parse(String datestr, String pattern) {
        Date date = null;
        if (null == pattern || "".equals(pattern)) {
            pattern = DEFAULT_DATE_FORMAT;
        }
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
            date = dateFormat.parse(datestr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public static Date parse(String datestr) {
        try {
            if (null != datestr && !"".equals(datestr)) {
                if (datestr.length() == 12) {
                    return DateTimeUtil.parse(datestr, "yyyyMMddHHmm");
                } else if (datestr.length() == 10) {
                    if (datestr.indexOf('-') > 0) {
                        return DateTimeUtil.parse(datestr, "yyyy-MM-dd");
                    } else if (datestr.indexOf('/') > 0) {
                        return DateTimeUtil.parse(datestr, "MM/dd/yyyy");
                    } else {
                        return DateTimeUtil.parse(datestr, "yyyyMMddHH");
                    }
                } else if (datestr.length() == 8) {
                    return DateTimeUtil.parse(datestr, "yyyyMMdd");
                } else if (datestr.length() == 6) {
                    return DateTimeUtil.parse(datestr, "yyyyMM");
                } else if (datestr.length() == 4) {
                    return DateTimeUtil.parse(datestr, "yyyy");
                }
            }
        } catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage());
        }
        return null;
    }

    public static Date getMonthLastDay() {
        return getMonthLastDay(getNow());
    }

    public static Date getMonthLastDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, 1);
        calendar.add(Calendar.DATE, -1);
        return calendar.getTime();
    }

    public static String startWeekDate(String time) {
        if (logger.isDebugEnabled()) {
            logger.debug("startWeekDate(String) - start");
        }
        if (null != time && !"".equals(time)) {
            time = getDateTime("yyyyMMddHHmm");
        }
        try {
            Date t_d_time = parse(time, "yyyyMMddHHmm");
            Calendar t_c_time = Calendar.getInstance();
            t_c_time.setTime(t_d_time);
            int t_i_day_week = t_c_time.get(Calendar.DAY_OF_WEEK) - 1;
            if (t_i_day_week > 0) {
                String t_s_day_week = "-" + new Integer(t_i_day_week).toString();
                Date t_rtn = addDays(t_d_time, new Integer(t_s_day_week).intValue());
                t_c_time.setTime(t_rtn);
                t_c_time.set(Calendar.AM_PM, Calendar.HOUR);
                t_c_time.set(Calendar.HOUR_OF_DAY, 0);
                t_c_time.set(Calendar.MINUTE, 0);
                t_c_time.set(Calendar.SECOND, 0);
                String returnString = DateTimeUtil.getDateTime(t_c_time.getTime(), "yyyyMMdd");
                if (logger.isDebugEnabled()) {
                    logger.debug("startWeekDate(String) - end");
                }
                return returnString;
            } else {
                String returnString = DateTimeUtil.getDateTime(t_d_time, "yyyyMMdd");
                if (logger.isDebugEnabled()) {
                    logger.debug("startWeekDate(String) - end");
                }
                return returnString;
            }
        } catch (NumberFormatException nfe) {
            logger.error("startWeekDate(String)", nfe);
        } catch (Exception e) {
            logger.error("startWeekDate(String)", e);
        }
        if (logger.isDebugEnabled()) {
            logger.debug("startWeekDate(String) - end");
        }
        return null;
    }

    public static Date toNextWeek(Date date, int week) {
        Calendar t_c_time = Calendar.getInstance();
        t_c_time.setTime(date);
        int t_s_day_week = t_c_time.get(Calendar.DAY_OF_WEEK);
        t_s_day_week--;
        int chazhi = week - t_s_day_week;
        if (chazhi > 0) {
            date = DateTimeUtil.addDays(date, chazhi);
        } else {
            date = DateTimeUtil.addDays(date, 7 + chazhi);
        }
        return date;
    }

    public static String endWeekDate(String time) {
        if (logger.isDebugEnabled()) {
            logger.debug("endWeekDate(String) - start");
        }
        if (null != time && !"".equals(time)) {
            time = getDateTime("yyyyMMddHHmm");
        }
        try {
            Date t_d_time = parse(time, "yyyyMMddHHmm");
            Calendar t_c_time = Calendar.getInstance();
            t_c_time.setTime(t_d_time);
            int t_s_day_week = t_c_time.get(Calendar.DAY_OF_WEEK);
            if (7 - t_s_day_week != 0) {
                t_d_time = addDays(t_d_time, (7 - t_s_day_week));
            }
            t_c_time.setTime(t_d_time);
            t_c_time.set(Calendar.AM_PM, Calendar.HOUR);
            t_c_time.set(Calendar.HOUR_OF_DAY, 23);
            t_c_time.set(Calendar.MINUTE, 59);
            t_c_time.set(Calendar.SECOND, 59);
            String returnString = DateTimeUtil.getDateTime(t_c_time.getTime(), "yyyyMMdd");
            if (logger.isDebugEnabled()) {
                logger.debug("endWeekDate(String) - end");
            }
            return returnString;
        } catch (NumberFormatException nfe) {
            logger.error("endWeekDate(String)", nfe);
        } catch (Exception e) {
            logger.error("endWeekDate(String)", e);
        }
        if (logger.isDebugEnabled()) {
            logger.debug("endWeekDate(String) - end");
        }
        return null;
    }

    public static int dateNum(int timeType) {
        return dateNum(null, timeType);
    }

    public static int dateNum(Date date, int field) {
        int t_temp = 100;
        Calendar calendar = Calendar.getInstance();
        if (null == date) date = getNow();
        calendar.setTime(date);
        t_temp = calendar.get(field);
        return t_temp;
    }

    public static Date startWeekDate(String time, String pattern) {
        if (logger.isDebugEnabled()) {
            logger.debug("startWeekDate(String, String) - start");
        }
        if (null == pattern || "".equals(pattern)) {
            pattern = DEFAULT_PAGE_FORMAT;
        }
        if (null == time || "".equals(time)) {
            time = getDateTime(pattern);
        }
        try {
            Date t_d_time = parse(time, pattern);
            Calendar t_c_time = Calendar.getInstance();
            t_c_time.setTime(t_d_time);
            int t_i_day_week = t_c_time.get(Calendar.DAY_OF_WEEK) - 1;
            if (t_i_day_week > 0) {
                String t_s_day_week = "-" + new Integer(t_i_day_week).toString();
                Date t_rtn = addDays(t_d_time, new Integer(t_s_day_week).intValue());
                t_c_time.setTime(t_rtn);
                t_c_time.set(Calendar.AM_PM, Calendar.HOUR);
                t_c_time.set(Calendar.HOUR_OF_DAY, 0);
                t_c_time.set(Calendar.MINUTE, 0);
                t_c_time.set(Calendar.SECOND, 0);
                Date returnDate = t_c_time.getTime();
                if (logger.isDebugEnabled()) {
                    logger.debug("startWeekDate(String, String) - end");
                }
                return returnDate;
            } else {
                if (logger.isDebugEnabled()) {
                    logger.debug("startWeekDate(String, String) - end");
                }
                return t_d_time;
            }
        } catch (NumberFormatException nfe) {
            logger.error("startWeekDate(String, String)", nfe);
        } catch (Exception e) {
            logger.error("startWeekDate(String, String)", e);
        }
        if (logger.isDebugEnabled()) {
            logger.debug("startWeekDate(String, String) - end");
        }
        return null;
    }

    public static Date endWeekDate(String time, String pattern) {
        if (logger.isDebugEnabled()) {
            logger.debug("endWeekDate(String, String) - start");
        }
        if (null == pattern || "".equals(pattern)) {
            pattern = DEFAULT_PAGE_FORMAT;
        }
        if (null == time || "".equals(time)) {
            time = getDateTime(pattern);
        }
        try {
            Date t_d_time = parse(time, pattern);
            Calendar t_c_time = Calendar.getInstance();
            t_c_time.setTime(t_d_time);
            int t_s_day_week = t_c_time.get(Calendar.DAY_OF_WEEK);
            if (7 - t_s_day_week != 0) {
                t_d_time = addDays(t_d_time, (7 - t_s_day_week));
            }
            t_c_time.setTime(t_d_time);
            t_c_time.set(Calendar.AM_PM, Calendar.HOUR);
            t_c_time.set(Calendar.HOUR_OF_DAY, 23);
            t_c_time.set(Calendar.MINUTE, 59);
            t_c_time.set(Calendar.SECOND, 59);
            Date returnDate = t_c_time.getTime();
            if (logger.isDebugEnabled()) {
                logger.debug("endWeekDate(String, String) - end");
            }
            return returnDate;
        } catch (NumberFormatException nfe) {
            logger.error("endWeekDate(String, String)", nfe);
        } catch (Exception e) {
            logger.error("endWeekDate(String, String)", e);
        }
        if (logger.isDebugEnabled()) {
            logger.debug("endWeekDate(String, String) - end");
        }
        return null;
    }

    public static String switchFormat(String time) {
        return switchFormat(time, null);
    }

    public static String switchFormat(String time, String pattern) {
        String t_date = null;
        if (null != time) {
            if (null != pattern) t_date = getDateTime(parse(time, DEFAULT_PAGE_FORMAT), pattern); else t_date = getDateTime(parse(time, DEFAULT_PAGE_FORMAT), DEFAULT_DATETIME_FORMAT);
        }
        return t_date;
    }

    public static String getDateWeek() {
        SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd E");
        return SDF.format(Calendar.getInstance().getTime());
    }

    public static void main(String[] args) {
        Date date = DateTimeUtil.parse("2008-12-18");
        DateTimeUtil.toNextWeek(date, 3);
    }

    public static String getOADBTime() {
        Date datetime = Calendar.getInstance().getTime();
        return getDateTime(datetime, DateTimeUtil.DEFAULT_OA_SYSTEM_FORMAT);
    }

    public static String getOADefaultTime() {
        Date datetime = Calendar.getInstance().getTime();
        return getDateTime(datetime, DateTimeUtil.DEFAULT_OA_PAGE_FORMAT);
    }

    public static String getPanasonicPageTime() {
        Date datetime = Calendar.getInstance().getTime();
        return getDateTime(datetime, DateTimeUtil.DEFAULT_OA_PAGE_FORMAT);
    }

    /**
	 * @param dbTime
	 * @return
	 * @throws Exception
	 */
    public static String panasonicDBToPageTime(String dbTime) throws Exception {
        try {
            if (null != dbTime) {
                return DateTimeUtil.getDateTime(DateTimeUtil.parse(dbTime, DateTimeUtil.DEFAULT_OA_SYSTEM_FORMAT), DateTimeUtil.DEFAULT_OA_PAGE_FORMAT);
            } else {
                return "";
            }
        } catch (RuntimeException e) {
            throw e;
        }
    }

    /**
	 * @param pageTime
	 * @return
	 * @throws Exception
	 */
    public static String panasonicPageToDBTime(String pageTime) throws Exception {
        try {
            return DateTimeUtil.getDateTime(DateTimeUtil.parse(pageTime, DateTimeUtil.DEFAULT_OA_PAGE_FORMAT), DateTimeUtil.DEFAULT_OA_SYSTEM_FORMAT);
        } catch (RuntimeException e) {
            throw e;
        }
    }

    public static String getKnowledgeDate() {
        String temp = getDateTime(DEFAULT_KNOW_FORMAT);
        return temp;
    }

    public static long getLeaveDays(String createDate, String autoGradeDate) {
        if (null != createDate && null != autoGradeDate) {
            Date createDateObj = DateTimeUtil.parse(createDate, DateTimeUtil.DEFAULT_KNOW_FORMAT);
            Date now = new Date();
            long difdays = DateTimeUtil.diffDays(now, createDateObj);
            return Long.parseLong(autoGradeDate) - difdays;
        }
        return 0;
    }

    public static long getPastTime(String timeStr) {
        String pattern = "yyyy-MM-dd HH:mm:ss";
        Date pastDate = DateTimeUtil.parse(timeStr, pattern);
        Date now = new Date();
        return DateTimeUtil.diffDays(now, pastDate);
    }

    public static String getDateForHashId() {
        String temp = getDateTime("yyyy-MM-dd-HH-mm");
        return temp;
    }
}

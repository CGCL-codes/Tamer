package com.diancai.custom;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 *
 * @author Zhong Fuqiang
 * @since 2012/2/6
 */
public class MyDatetime {

    public static String FORMAT = "yyyy/MM/dd";

    /**
     * 两个时间之间相差的天数
     * @param dt1
     * @param dt2
     * @return 
     */
    public static int getDays(Date dt1, Date dt2) throws ParseException {
        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTime(dt1);
        calendar1.set(calendar1.get(Calendar.YEAR), calendar1.get(Calendar.MONTH), calendar1.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        dt1 = calendar1.getTime();
        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTime(dt2);
        calendar2.set(calendar2.get(Calendar.YEAR), calendar2.get(Calendar.MONTH), calendar2.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        dt2 = calendar2.getTime();
        long day = (dt2.getTime() - dt1.getTime()) / (24 * 60 * 60 * 1000);
        return (int) day;
    }

    /**
     * 获取几天前的日期
     * @param value
     * @return 
     */
    public static Date getFirstDateOfDay(int value) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        calendar.add(Calendar.DAY_OF_MONTH, -value);
        return calendar.getTime();
    }

    /**
     * 获取几天前的日期
     * @param value
     * @return 
     */
    public static Date getDateOfDay(Date dt, int value) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dt);
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        calendar.add(Calendar.DAY_OF_MONTH, -value);
        return calendar.getTime();
    }

    /**
     * 两个时间之间相差的周数
     * @param dt1
     * @param dt2
     * @return 
     */
    public static int getWeeks(Date dt1, Date dt2) throws ParseException {
        int weeks = 0;
        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTime(dt1);
        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTime(dt2);
        while (calendar1.before(calendar2)) {
            calendar1.add(Calendar.DAY_OF_YEAR, 1);
            if (calendar1.get(Calendar.DAY_OF_WEEK) == calendar1.getFirstDayOfWeek()) {
                weeks++;
            }
        }
        return weeks;
    }

    /**
     * 
     * @param value
     * @return 
     */
    public static Date getFirstDateOfWeek(int value) {
        value = value * 7;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DAY_OF_MONTH, -value);
        int w = calendar.get(Calendar.DAY_OF_WEEK);
        switch(w) {
            case Calendar.SUNDAY:
                value = 7 - 7;
                break;
            case Calendar.SATURDAY:
                value = 7 - 1;
                break;
            case Calendar.FRIDAY:
                value = 7 - 2;
                break;
            case Calendar.THURSDAY:
                value = 7 - 3;
                break;
            case Calendar.WEDNESDAY:
                value = 7 - 4;
                break;
            case Calendar.TUESDAY:
                value = 7 - 5;
                break;
            case Calendar.MONDAY:
                value = 7 - 6;
                break;
        }
        calendar.add(Calendar.DAY_OF_MONTH, -value);
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        return calendar.getTime();
    }

    /**
     * 
     * @param value
     * @return 
     */
    public static Date getDateOfWeek(Date dt, int value) {
        value = value * 7;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dt);
        calendar.add(Calendar.DAY_OF_MONTH, -value);
        int w = calendar.get(Calendar.DAY_OF_WEEK);
        switch(w) {
            case Calendar.SUNDAY:
                value = 7 - 7;
                break;
            case Calendar.SATURDAY:
                value = 7 - 1;
                break;
            case Calendar.FRIDAY:
                value = 7 - 2;
                break;
            case Calendar.THURSDAY:
                value = 7 - 3;
                break;
            case Calendar.WEDNESDAY:
                value = 7 - 4;
                break;
            case Calendar.TUESDAY:
                value = 7 - 5;
                break;
            case Calendar.MONDAY:
                value = 7 - 6;
                break;
        }
        calendar.add(Calendar.DAY_OF_MONTH, -value);
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        return calendar.getTime();
    }

    /**
     * 两个时间之间相差的月数
     * @param dt1
     * @param dt2
     * @return 
     */
    public static int getMonths(Date dt1, Date dt2) throws ParseException {
        int months = 0;
        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTime(dt1);
        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTime(dt2);
        calendar1.add(Calendar.DAY_OF_MONTH, 1);
        while (calendar1.before(calendar2)) {
            if (calendar1.get(Calendar.DAY_OF_MONTH) == 1) {
                months++;
            }
            calendar1.add(Calendar.DAY_OF_MONTH, 1);
        }
        return months;
    }

    /**
     * 几个月前的第一天
     * @param value
     * @return 
     */
    public static Date getFirstDateOfMonth(int value) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.MONTH, -value);
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), 1, 0, 0, 0);
        return calendar.getTime();
    }

    /**
     * 几个月前的第一天
     * @param value
     * @return 
     */
    public static Date getDateOfMonth(Date dt, int value) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dt);
        calendar.add(Calendar.MONTH, -value);
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), 1, 0, 0, 0);
        return calendar.getTime();
    }

    /**
     * 两个时间之间相差的季数
     * @param dt1
     * @param dt2
     * @return 
     */
    public static int getSeasons(Date dt1, Date dt2) throws ParseException {
        int seasons = 0;
        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTime(dt1);
        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTime(dt2);
        while (calendar1.before(calendar2)) {
            calendar1.add(Calendar.DAY_OF_MONTH, 1);
            if (calendar1.get(Calendar.MONTH) + 1 == 1 && calendar1.get(Calendar.DAY_OF_MONTH) == 1) {
                seasons++;
            }
            if (calendar1.get(Calendar.MONTH) + 1 == 4 && calendar1.get(Calendar.DAY_OF_MONTH) == 1) {
                seasons++;
            }
            if (calendar1.get(Calendar.MONTH) + 1 == 7 && calendar1.get(Calendar.DAY_OF_MONTH) == 1) {
                seasons++;
            }
            if (calendar1.get(Calendar.MONTH) + 1 == 10 && calendar1.get(Calendar.DAY_OF_MONTH) == 1) {
                seasons++;
            }
        }
        return seasons;
    }

    /**
     * 几个Seasons前的第一天
     * @param value
     * @return 
     */
    public static Date getFirstDateOfSeasons(int value) {
        int seasons = 0;
        value = value * 3;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.MONTH, -value);
        switch(calendar.get(Calendar.MONTH) + 1) {
            case 1:
                seasons = 1;
                break;
            case 2:
                seasons = 1;
                break;
            case 3:
                seasons = 1;
                break;
            case 4:
                seasons = 4;
                break;
            case 5:
                seasons = 4;
                break;
            case 6:
                seasons = 4;
                break;
            case 7:
                seasons = 7;
                break;
            case 8:
                seasons = 7;
                break;
            case 9:
                seasons = 7;
                break;
            case 10:
                seasons = 10;
                break;
            case 11:
                seasons = 10;
                break;
            case 12:
                seasons = 10;
                break;
            default:
                seasons = 0;
                break;
        }
        if (seasons > 0) {
            seasons--;
        }
        calendar.set(calendar.get(Calendar.YEAR), seasons, 1, 0, 0, 0);
        return calendar.getTime();
    }

    /**
     * 几个Seasons前的第一天
     * @param value
     * @return 
     */
    public static Date getDateOfSeasons(Date dt, int value) {
        int seasons = 0;
        value = value * 3;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dt);
        calendar.add(Calendar.MONTH, -value);
        switch(calendar.get(Calendar.MONTH) + 1) {
            case 1:
                seasons = 1;
                break;
            case 2:
                seasons = 1;
                break;
            case 3:
                seasons = 1;
                break;
            case 4:
                seasons = 4;
                break;
            case 5:
                seasons = 4;
                break;
            case 6:
                seasons = 4;
                break;
            case 7:
                seasons = 7;
                break;
            case 8:
                seasons = 7;
                break;
            case 9:
                seasons = 7;
                break;
            case 10:
                seasons = 10;
                break;
            case 11:
                seasons = 10;
                break;
            case 12:
                seasons = 10;
                break;
            default:
                seasons = 0;
                break;
        }
        if (seasons > 0) {
            seasons--;
        }
        calendar.set(calendar.get(Calendar.YEAR), seasons, 1, 0, 0, 0);
        return calendar.getTime();
    }

    public static int getYears(Date dt1, Date dt2) throws ParseException {
        Calendar calendar1 = new GregorianCalendar();
        calendar1.setTime(dt1);
        Calendar calendar2 = new GregorianCalendar();
        calendar2.setTime(dt2);
        return calendar2.get(Calendar.YEAR) - calendar1.get(Calendar.YEAR);
    }

    /**
     * 几年前的第一天
     * @param value
     * @return 
     */
    public static Date getFirstDateOfYear(int value) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.YEAR, -value);
        calendar.set(calendar.get(Calendar.YEAR), 0, 1, 0, 0, 0);
        return calendar.getTime();
    }

    /**
     * 几年前的第一天
     * @param value
     * @return 
     */
    public static Date getDateOfYear(Date dt, int value) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dt);
        calendar.add(Calendar.YEAR, -value);
        calendar.set(calendar.get(Calendar.YEAR), 0, 1, 0, 0, 0);
        return calendar.getTime();
    }

    /**
     * 获取TokenID,如果当前TokenID 小于 前一个Token ID， 则当前TokenID 为前一个TokenID + 1
     * @return 
     */
    private static long TokenID = 0;

    public static long getTokenID() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        long res = (GetTicks(calendar) - 628614432000000000L) / 600000000;
        if (res <= TokenID) {
            TokenID++;
            res = TokenID;
        }
        TokenID = res;
        return res;
    }

    /**
     * 
     * @param date
     * @return 
     */
    public static Date getStartDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        Calendar calendar1 = Calendar.getInstance();
        calendar1.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        return calendar1.getTime();
    }

    /**
     * 结束时间
     * @param date
     * @return 
     */
    public static Date getEndDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        Calendar calendar1 = Calendar.getInstance();
        calendar1.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), 23, 59, 59);
        return calendar1.getTime();
    }

    /**
     * 格式化时间
     * @param format
     * @param date
     * @return 
     */
    public static String dateFormat(String format, Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        return dateFormat.format(date);
    }

    /**
     * 
     * @return 
     */
    public static long getID() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        Calendar calendar1 = Calendar.getInstance();
        calendar1.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        return calendar1.getTimeInMillis() - 728496000000L;
    }

    /**
     * 
     * @param calEnd
     * @return 
     */
    public static long GetTicks(Calendar calEnd) {
        Calendar calStart = Calendar.getInstance();
        calStart.set(1, 1, 3, 0, 0, 0);
        long epochStart = calStart.getTime().getTime();
        long epochEnd = calEnd.getTime().getTime();
        long all = epochEnd - epochStart;
        long ticks = ((all / 1000) * 1000000) * 10;
        return ticks;
    }
}

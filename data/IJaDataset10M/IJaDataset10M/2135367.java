package com.pioneer.app.util;

import java.sql.Connection;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;
import com.pioneer.app.db.DBConnectionManager;

/**
 * @desc: 本周时间方法
 * @auther : winsen
 * @date : 2007-11-14
 */
public class WeekDate {

    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd 00:00:00");

    Calendar cal = Calendar.getInstance();

    Date date = new Date();

    public String[] disWeekDate() {
        String[] WeekDays = new String[4];
        WeekDays[0] = getMonday();
        WeekDays[1] = getSunday();
        WeekDays[2] = getLastMonday();
        WeekDays[3] = getLastSunday();
        return WeekDays;
    }

    public static String getMonday() {
        Calendar c = new GregorianCalendar(TimeZone.getTimeZone("GMT+8:00"), Locale.CHINA);
        c.setTime(new Date());
        int day = c.get(Calendar.DAY_OF_WEEK);
        c.add(Calendar.DAY_OF_WEEK, 1 - (day + 7));
        c.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        return new SimpleDateFormat("yyyy-MM-dd 00:00:00").format(c.getTime());
    }

    public static String getSunday() {
        Calendar c = new GregorianCalendar(TimeZone.getTimeZone("GMT+8:00"), Locale.CHINA);
        c.setTime(new Date());
        c.add(Calendar.WEEK_OF_YEAR, 1);
        int day = c.get(Calendar.DAY_OF_WEEK);
        c.add(Calendar.DAY_OF_WEEK, 1 - (day + 7));
        c.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        return new SimpleDateFormat("yyyy-MM-dd 23:59:59").format(c.getTime());
    }

    public static String getLastMonday() {
        Calendar c = new GregorianCalendar(TimeZone.getTimeZone("GMT+8:00"), Locale.CHINA);
        c.setTime(new Date());
        int day = c.get(Calendar.DAY_OF_WEEK);
        c.add(Calendar.DAY_OF_WEEK, 1 - (day + 14));
        c.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        return new SimpleDateFormat("yyyy-MM-dd 00:00:00").format(c.getTime());
    }

    public static String getLastSunday() {
        Calendar c = new GregorianCalendar(TimeZone.getTimeZone("GMT+8:00"), Locale.CHINA);
        c.setTime(new Date());
        c.add(Calendar.WEEK_OF_YEAR, 1);
        int day = c.get(Calendar.DAY_OF_WEEK);
        c.add(Calendar.DAY_OF_WEEK, 1 - (day + 14));
        c.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        return new SimpleDateFormat("yyyy-MM-dd 23:59:59").format(c.getTime());
    }

    public static void main(String[] args) {
        String[] week = new WeekDate().disWeekDate();
        for (int i = 0; i < 4; i++) {
            System.out.println(week[i]);
        }
    }

    /**
	 * @desc: 查看当前时间是否是一周开始。
	 * @auther : winsen
	 * @date : 2007-11-14
	 */
    public boolean getIsWeekDay(String userId) throws Exception {
        Connection conn = null;
        ResultSet RS = null;
        String sql = null;
        Date DT = new Date();
        sql = "select nonce_weekend from t_user where id = '" + userId + "'";
        conn = DBConnectionManager.getInstance().getConn();
        SimpleDateFormat simpleDateFormats = new SimpleDateFormat("yyyy-MM-dd 23:59:59");
        try {
            RS = conn.createStatement().executeQuery(sql);
            RS.next();
            String weekend = RS.getString("nonce_weekend");
            Date WeekendDay = simpleDateFormats.parse(weekend);
            if (WeekendDay.after(DT)) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static WeekDate init() {
        return new WeekDate();
    }
}

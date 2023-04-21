package com.ak.utils;

import java.util.Calendar;
import java.util.Queue;
import java.math.BigDecimal;
import java.util.Date;
import java.text.SimpleDateFormat;
import org.jfree.util.Log;

public class CommonUtils {

    public static String replicate(String inStr, int count) {
        String out = "";
        for (int i = 0; i < count; i++) {
            out += inStr;
        }
        return out;
    }

    public static String getTodayDateString() {
        return now("yyyy-MM-dd");
    }

    public static String getTodayDateString(String format) {
        return now(format);
    }

    public static String getNowTimeString() {
        return now("HH:mm:ss");
    }

    public static String getNowTimeMilliSecondString() {
        return now("HH:mm:sss");
    }

    public static String getNowDateTimeString() {
        return now("yyyy-MM-dd HH:mm:ss");
    }

    public static String getFormatedDateTimeString(Date date, String dateFormat) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        return sdf.format(cal.getTime());
    }

    public static Date getFormatedDateTimeString(Date date, int dateAdd) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, dateAdd);
        return cal.getTime();
    }

    public static boolean isWeekend(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int dow = cal.get(Calendar.DAY_OF_WEEK);
        if (dow == Calendar.SATURDAY || dow == Calendar.SUNDAY) {
            return true;
        } else {
            return false;
        }
    }

    public static Date getDateTimeInNextMinutes(int minutes) {
        Calendar calendar = Calendar.getInstance();
        int second = calendar.get(Calendar.SECOND);
        calendar.add(Calendar.MILLISECOND, (60 - second) * 1000 + 100 + ((minutes - 1) * 60 * 1000));
        return calendar.getTime();
    }

    public static Date getDateFromHistoricalDateString(String dateString) throws Exception {
        int year, month, day, hour, min, second;
        try {
            year = Integer.parseInt(dateString.substring(0, 4));
            month = Integer.parseInt(dateString.substring(4, 6)) - 1;
            day = Integer.parseInt(dateString.substring(6, 8));
            hour = Integer.parseInt(dateString.substring(10, 12));
            min = Integer.parseInt(dateString.substring(13, 15));
            second = Integer.parseInt(dateString.substring(16, 18));
        } catch (Exception e) {
            throw new Exception(e);
        }
        Calendar cal = Calendar.getInstance();
        cal.set(year, month, day, hour, min, second);
        Date date = cal.getTime();
        return date;
    }

    public static Date getDatePlusDate(int days) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, days);
        return calendar.getTime();
    }

    private static String now(String dateFormat) {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        return sdf.format(cal.getTime());
    }

    @SuppressWarnings("unused")
    public static void sleep(double second) {
        try {
            Thread.sleep((long) (second * 1000));
        } catch (InterruptedException e) {
        }
    }

    @SuppressWarnings("unused")
    public static String[][] string2array(String inStr) {
        String[] strArray = inStr.split("\\^");
        String[] strArray1 = strArray[0].split("\\|");
        int rowCount = strArray.length;
        int fieldCount = strArray1.length;
        String[][] out = new String[rowCount][fieldCount];
        for (int i = 0; i < rowCount; i++) {
            String[] fieldArray = strArray[i].split("\\|");
            for (int j = 0; j < fieldCount; j++) {
                out[i][j] = fieldArray[j];
            }
        }
        return out;
    }

    public static Object[] copyArray(Object[] fromArray, int count) {
        Object[] out = new Object[count];
        for (int i = 0; i < count; i++) {
            out[i] = fromArray[i];
        }
        return out;
    }

    public static double[] queue2doubleArray(Queue queue) {
        double[] out = new double[queue.size()];
        Object[] queueObj = queue.toArray();
        for (int i = 0; i < queueObj.length; i++) {
            out[i] = Double.parseDouble(queueObj[i].toString());
        }
        return out;
    }

    public static String getFixDouble2Decimal(double d) {
        java.text.DecimalFormat df2 = new java.text.DecimalFormat("#.00");
        return df2.format(d);
    }
}

package com.baldwin.www.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Util {

    protected Util() {
    }

    public static String formatDouble(double d) {
        DecimalFormat format = new DecimalFormat("###,##0.00");
        return format.format(d);
    }

    public static String formatLong(long d) {
        DecimalFormat format = new DecimalFormat("###,##0");
        return format.format(d);
    }

    public static double round(double source) {
        return round(source, 2);
    }

    public static double round(double d1, double d2) {
        return round(d1, d2, 2);
    }

    public static double round(double d1, double d2, int scale) {
        if (d2 == 0) d2 = 1;
        return round(d1 / d2, scale);
    }

    public static double round(double source, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException("The scale must be a positive integer or zero");
        }
        try {
            BigDecimal b = new BigDecimal(source);
            BigDecimal one = new BigDecimal("1");
            return b.divide(one, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
        } catch (NumberFormatException e) {
            System.out.println(source);
        }
        return 0.0;
    }

    public static String percentFormat(double source) {
        source = source * 100;
        if ((source % 2) == 0) return formatLong((int) round(source, 0)) + "%"; else return formatDouble(round(source, 2)) + "%";
    }

    public static String percentFormat(double d1, double d2) {
        if (d2 == 0) d2 = 1;
        return percentFormat(d1 / d2);
    }

    public static boolean isBooleanString(String boolString) {
        return boolString != null && (boolString.equals("true") || boolString.equals("false"));
    }

    public static void doForward(String nextPage, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestDispatcher rd = request.getRequestDispatcher(nextPage);
        if (rd != null) rd.forward(request, response);
        return;
    }

    public static String[] split(String str, String deslim) {
        return str.split(deslim);
    }

    public static String getNow() {
        return getDateString(new Date(), "yyyy-MM-dd HH:mm:ss");
    }

    public static String getDefaultDateString(Date date) {
        return getDateString(date, "yyyy-MM-dd HH:mm:ss");
    }

    public static String getDateString(Date date, String format) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat(format);
            String dateString = dateFormat.format(date);
            return dateString;
        } catch (Exception e) {
        }
        return "";
    }

    public static String getDateString(Date date) {
        return getDateString(date, "yyyy-MM-dd");
    }

    public static Date getDateFromString(String dateString) throws ParseException {
        return getDateFromString(dateString, "yyyy-MM-dd");
    }

    public static Date getDateFromString(String dateString, String formatString) {
        SimpleDateFormat format = new SimpleDateFormat(formatString);
        try {
            return format.parse(dateString);
        } catch (Exception e) {
        }
        return null;
    }

    public static String getBeforeNineteenDay(String startDate) {
        String newStartDate = "";
        try {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(Util.getDateFromString(startDate));
            calendar.add(Calendar.DAY_OF_MONTH, -19);
            newStartDate = Util.getDateString(calendar.getTime());
        } catch (ParseException e1) {
            e1.printStackTrace();
        }
        return newStartDate;
    }

    public static String addDay(String startDate, int day) {
        String newStartDate = "";
        try {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(Util.getDateFromString(startDate));
            calendar.add(Calendar.DAY_OF_MONTH, day);
            newStartDate = Util.getDateString(calendar.getTime());
        } catch (ParseException e1) {
            e1.printStackTrace();
        }
        return newStartDate;
    }

    public static String subtractDay(String startDate, int day) {
        String newStartDate = "";
        try {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(Util.getDateFromString(startDate));
            calendar.add(Calendar.DAY_OF_MONTH, day);
            newStartDate = Util.getDateString(calendar.getTime());
        } catch (ParseException e1) {
            e1.printStackTrace();
        }
        return newStartDate;
    }

    public static String getUrlByMap(Map<String, String> map) {
        String baseUrl = "";
        Iterator<String> iter = map.keySet().iterator();
        while (iter.hasNext()) {
            String key = iter.next();
            String value = map.get(key);
            if (key.equals("currentPage") || key.equals("pageSize") || key.equals("sortBy") || key.equals("asc")) {
                continue;
            } else {
                baseUrl += (key + "=" + value + "&");
            }
        }
        baseUrl = baseUrl.substring(0, baseUrl.length() - 1);
        baseUrl = StringUtils.urlEncode(baseUrl, "UTF8");
        baseUrl = baseUrl.replaceAll("%3D", "=");
        baseUrl = baseUrl.replaceAll("%26", "&");
        return baseUrl;
    }

    public static String getPeriodString(String startDate, String endDate, String dateType) {
        endDate = getEndString(startDate, endDate, dateType);
        final String dd = " -- ";
        return startDate + dd + endDate;
    }

    public static String getEndString(String startDate, String endDate, String dateType) {
        Date dStart = new Date();
        try {
            dStart = Util.getDateFromString(startDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String result = "";
        if (dateType.equals("N/A")) {
            result = endDate;
        } else if (dateType.equals("year")) {
            Calendar c1 = Calendar.getInstance();
            c1.setTime(dStart);
            c1.add(Calendar.YEAR, 1);
            c1.add(Calendar.DAY_OF_MONTH, -1);
            result = Util.getDateString(c1.getTime());
        } else if (dateType.equals("month")) {
            Calendar c1 = Calendar.getInstance();
            c1.setTime(dStart);
            c1.add(Calendar.MONTH, 1);
            c1.add(Calendar.DAY_OF_MONTH, -1);
            result = Util.getDateString(c1.getTime());
        } else if (dateType.equals("week")) {
            Calendar c1 = Calendar.getInstance();
            c1.setTime(dStart);
            c1.add(Calendar.DAY_OF_MONTH, 6);
            result = Util.getDateString(c1.getTime());
        } else if (dateType.equals("day")) {
            result = startDate;
        } else {
            throw new RuntimeException("cann't be here!");
        }
        return result;
    }

    public static String getHistoryPeriod(String startDate, String endDate, String dateType, boolean isStart) {
        endDate = getEndString(startDate, endDate, dateType);
        Date dStart = new Date();
        Date dEnd = new Date();
        try {
            dStart = Util.getDateFromString(startDate);
            dEnd = Util.getDateFromString(endDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String historyStart = "";
        String historyEnd = "";
        int span = 0;
        Calendar s = Calendar.getInstance();
        s.setTime(dStart);
        Calendar e = Calendar.getInstance();
        e.setTime(dEnd);
        Calendar hE = Calendar.getInstance();
        hE.setTime(dStart);
        hE.add(Calendar.DAY_OF_MONTH, -1);
        historyEnd = Util.getDateString(hE.getTime());
        if (dateType.equals("N/A")) {
            while (s.before(e)) {
                s.add(Calendar.DAY_OF_MONTH, 1);
                span++;
            }
            hE.add(Calendar.DAY_OF_MONTH, -span);
            historyStart = Util.getDateString(hE.getTime());
        } else if (dateType.equals("year")) {
            hE.add(Calendar.YEAR, -1);
            hE.add(Calendar.DAY_OF_MONTH, 1);
            historyStart = Util.getDateString(hE.getTime());
        } else if (dateType.equals("month")) {
            hE.add(Calendar.MONTH, -1);
            hE.add(Calendar.DAY_OF_MONTH, 1);
            historyStart = Util.getDateString(hE.getTime());
        } else if (dateType.equals("week")) {
            hE.add(Calendar.DAY_OF_MONTH, -6);
            historyStart = Util.getDateString(hE.getTime());
        } else if (dateType.equals("day")) {
            historyStart = historyEnd;
        } else {
            throw new RuntimeException("cann't be here!");
        }
        if (isStart) return historyStart; else return historyEnd;
    }

    public static int getPeriodSpan(String startDate, String endDate) {
        Date dStart = new Date();
        Date dEnd = new Date();
        try {
            dStart = Util.getDateFromString(startDate);
            dEnd = Util.getDateFromString(endDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        int span = 1;
        Calendar s = Calendar.getInstance();
        s.setTime(dStart);
        Calendar e = Calendar.getInstance();
        e.setTime(dEnd);
        while (s.before(e)) {
            s.add(Calendar.DAY_OF_MONTH, 1);
            span++;
        }
        return span;
    }

    public static Date[] getStartEndDateFromYearWeek(int year, int week) {
        return getStartEndDateFromYearWeek(year, week, true);
    }

    public static Date[] getStartEndDateFromYearWeek(int year, int week, boolean firstIsMonday) {
        try {
            Calendar c = Calendar.getInstance();
            if (firstIsMonday) {
                c.setFirstDayOfWeek(Calendar.MONDAY);
            } else {
                c.setFirstDayOfWeek(Calendar.SUNDAY);
            }
            c.setTime(getDateFromString(year + "-01-01"));
            c.add(Calendar.WEEK_OF_YEAR, week - 1);
            if (firstIsMonday) {
                while (c.get(Calendar.DAY_OF_WEEK) != Calendar.MONDAY) {
                    c.add(Calendar.DAY_OF_MONTH, -1);
                }
            } else {
                while (c.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
                    c.add(Calendar.DAY_OF_MONTH, -1);
                }
            }
            Date start = c.getTime();
            c.add(Calendar.DAY_OF_MONTH, 6);
            Date end = c.getTime();
            return new Date[] { start, end };
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return new Date[] { new Date(), new Date() };
    }

    public static Date[] getStartEndDateFromYearMonth(int year, int month) {
        try {
            Calendar c = Calendar.getInstance();
            c.setTime(getDateFromString(year + "-01-01"));
            c.add(Calendar.MONTH, month - 1);
            Date start = c.getTime();
            c.add(Calendar.MONTH, 1);
            c.add(Calendar.DAY_OF_MONTH, -1);
            Date end = c.getTime();
            return new Date[] { start, end };
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return new Date[] { new Date(), new Date() };
    }

    public static Date[] getStartEndDateFromYear(int year) {
        try {
            Calendar c = Calendar.getInstance();
            c.setTime(getDateFromString(year + "-01-01"));
            Date start = c.getTime();
            c.add(Calendar.YEAR, 1);
            c.add(Calendar.DAY_OF_YEAR, -1);
            Date end = c.getTime();
            return new Date[] { start, end };
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return new Date[] { new Date(), new Date() };
    }

    public static String getRootPath(HttpServletRequest request) {
        return request.getSession().getServletContext().getRealPath("");
    }

    public static String getHumanReadTime(long time) {
        String hh = "";
        String mm = "";
        String ss = "";
        long hour = time / (60 * 60);
        long minute = (time % (60 * 60)) / 60;
        long second = (time % (60 * 60)) % 60;
        if (hour >= 0 && hour <= 9) hh = String.format("0%d", hour); else hh = String.format("%d", hour);
        if (minute >= 0 && minute <= 9) mm = String.format("0%d", minute); else mm = String.format("%d", minute);
        if (second >= 0 && second <= 9) ss = String.format("0%d", second); else ss = String.format("%d", second);
        return (hh + ":" + mm + ":" + ss);
    }

    public static void downloadFile(HttpServletRequest request, HttpServletResponse response, String fileName) throws IOException {
        byte[] buf = new byte[1024];
        int len = 0;
        BufferedInputStream br = null;
        OutputStream out = null;
        try {
            br = new BufferedInputStream(new FileInputStream(fileName));
            out = response.getOutputStream();
            while ((len = br.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            out.flush();
        } catch (Exception e) {
        } finally {
            if (br != null) {
                br.close();
                br = null;
            }
            if (out != null) {
                out.close();
                out = null;
            }
        }
    }

    public static Set getStartToEnd(Date startDate, Date endDate) {
        Set set = new HashSet();
        Calendar start = Calendar.getInstance();
        start.setTime(startDate);
        Calendar end = Calendar.getInstance();
        end.setTime(endDate);
        while (start.before(end)) {
            set.add(start.getTime());
            start.add(Calendar.DAY_OF_MONTH, 1);
        }
        set.add(start.getTime());
        return set;
    }

    public static void main(String[] args) throws ParseException {
        Date startDate = getDateFromString("2007-08-30");
        Date endDate = getDateFromString("2007-08-31");
        System.out.println("Date size is: " + getStartToEnd(startDate, endDate).size());
        System.out.println(getStartToEnd(startDate, endDate).contains(startDate));
    }

    /**
 * @param str
 * @return
 */
    public static long converIPtoInteger(String str) {
        long cip = 0;
        String sip[] = str.split("\\.");
        cip = Integer.parseInt(sip[0]) * 256 * 256 * 256 + Integer.parseInt(sip[1]) * 256 * 256 + Integer.parseInt(sip[2]) * 256 + Integer.parseInt(sip[3]);
        return cip;
    }

    /**
 * ѭ����ȡ�ļ�����ƣ��޸�ʱ�����String[]
 * @param fileDir
 * @return ArrayList<String>
 */
    public static ArrayList<String> getFileNameAndCreateTime(String fileDir) {
        File f = new File(fileDir);
        File[] ff = f.listFiles();
        ArrayList<String> li = new ArrayList<String>();
        String s = "";
        for (int i = 0; i < ff.length; i++) {
            s = ff[i].getName() + "," + DateUtils.getLong2LongString(ff[i].lastModified()) + "," + ff[i].length();
            li.add(s);
        }
        return li;
    }
}

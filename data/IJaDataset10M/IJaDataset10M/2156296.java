package com.hotye.school.util;

import java.util.*;
import java.text.*;

public class GetDate {

    public GetDate() {
    }

    public static Date getNowDate() {
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateString = formatter.format(currentTime);
        ParsePosition pos = new ParsePosition(8);
        Date currentTime_2 = formatter.parse(dateString, pos);
        return currentTime_2;
    }

    public static Date getNowDateShort() {
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = formatter.format(currentTime);
        ParsePosition pos = new ParsePosition(8);
        Date currentTime_2 = formatter.parse(dateString, pos);
        return currentTime_2;
    }

    public static String getStringDate() {
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateString = formatter.format(currentTime);
        return dateString;
    }

    public static String getStringDateShort() {
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = formatter.format(currentTime);
        return dateString;
    }

    public static Date strToDate(String strDate) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        ParsePosition pos = new ParsePosition(0);
        Date strtodate = formatter.parse(strDate, pos);
        return strtodate;
    }

    public static String dateToStr(java.util.Date dateDate) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateString = formatter.format(dateDate);
        return dateString;
    }

    public static Date strToBirthday(String strDate) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        ParsePosition pos = new ParsePosition(0);
        Date strtodate = formatter.parse(strDate, pos);
        return strtodate;
    }

    public static Date getNow() {
        Date currentTime = new Date();
        return currentTime;
    }

    public static long getS(String strDate) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        ParsePosition pos = new ParsePosition(0);
        Date strtodate = formatter.parse(strDate, pos);
        return strtodate.getTime();
    }

    public static Date getLastDate(long day) {
        Date date = new Date();
        long date_3_hm = date.getTime() - 3600000 * 34 * day;
        Date date_3_hm_date = new Date(date_3_hm);
        return date_3_hm_date;
    }

    public static int getNowDay(String StrDate) {
        Date Time1 = strToDate(StrDate);
        Date Time2 = new Date();
        long day = Time1.getTime() - Time2.getTime();
        return (int) day / (24 * 60 * 60 * 1000);
    }

    public static int getNowDay(Date StrDate) {
        Date Time1 = StrDate;
        Date Time2 = new Date();
        long day = Time1.getTime() - Time2.getTime();
        return (int) day / (24 * 60 * 60 * 1000);
    }

    /**
     * ����String���͵��������ʽ��ţ���Date���ͷ�����Ҫ�ĸ�ʽ
     * @param date String ���͵�����
     * @param i ��ʽ���� �����������DateTool.yyyy_MM_dd
     * */
    public static Date getDateFormat(String date, int i) throws Exception {
        SimpleDateFormat simpledateformat = new SimpleDateFormat();
        switch(i) {
            case 1:
                simpledateformat.applyPattern("yyyy-MM-dd");
                break;
            case 2:
                simpledateformat.applyPattern("yyyy-M-d");
                break;
            case 3:
                simpledateformat.applyPattern("yy-MM-dd");
                break;
            case 4:
                simpledateformat.applyPattern("yy-M-d");
                break;
            case 5:
                simpledateformat.applyPattern("yyyy-MM-dd HH:mm:ss");
                break;
            case 6:
                simpledateformat.applyPattern("yyyy-M-d H:m:s");
                break;
            case 7:
                simpledateformat.applyPattern("yy-MM-dd HH:mm:ss");
                break;
            case 8:
                simpledateformat.applyPattern("yy-M-d H:m:s");
                break;
            case 9:
                simpledateformat.applyPattern("yyyy");
                break;
            case 10:
                simpledateformat.applyPattern("yyyy-MM");
                break;
            case 11:
                simpledateformat.applyPattern("yyyyMMdd");
                break;
            case 12:
                simpledateformat.applyPattern("yyyyMM");
                break;
        }
        return simpledateformat.parse(date);
    }

    /**
     * ����Date���͵��������ʽ��ţ���String���ͷ�����Ҫ�ĸ�ʽ
     * @param date Date ���͵�����
     * @param i ��ʽ���� �����������DateTool.yyyy_MM_dd
     * */
    public static String getStringDateFormat(Date date, int i) throws Exception {
        SimpleDateFormat simpledateformat = new SimpleDateFormat();
        switch(i) {
            case 1:
                simpledateformat.applyPattern("yyyy-MM-dd");
                break;
            case 2:
                simpledateformat.applyPattern("yyyy-M-d");
                break;
            case 3:
                simpledateformat.applyPattern("yy-MM-dd");
                break;
            case 4:
                simpledateformat.applyPattern("yy-M-d");
                break;
            case 5:
                simpledateformat.applyPattern("yyyy-MM-dd HH:mm:ss");
                break;
            case 6:
                simpledateformat.applyPattern("yyyy-M-d H:m:s");
                break;
            case 7:
                simpledateformat.applyPattern("yy-MM-dd HH:mm:ss");
                break;
            case 8:
                simpledateformat.applyPattern("yy-M-d H:m:s");
                break;
            case 9:
                simpledateformat.applyPattern("yyyy");
                break;
            case 10:
                simpledateformat.applyPattern("yyyy-MM");
                break;
            case 11:
                simpledateformat.applyPattern("yyyyMMdd");
                break;
            case 12:
                simpledateformat.applyPattern("yyyyMM");
                break;
        }
        return simpledateformat.format(date);
    }
}

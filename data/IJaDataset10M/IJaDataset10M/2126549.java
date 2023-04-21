package com.bayareasoftware.chartengine.ds.util;

import java.text.DateFormat;
import java.text.FieldPosition;
import java.text.ParseException;
import java.text.ParsePosition;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class QuaterDateFormat extends DateFormat {

    public QuaterDateFormat() {
        calendar = new GregorianCalendar();
    }

    @Override
    public StringBuffer format(Date date, StringBuffer toAppendTo, FieldPosition fieldPosition) {
        calendar.setTime(date);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int q = month / 3 + 1;
        return toAppendTo.append(year).append("-Q").append(q);
    }

    @Override
    public Date parse(String source, ParsePosition pos) {
        Date ret = null;
        if (source.length() >= 6) {
            int ind = source.indexOf('Q');
            int ind2 = source.indexOf('q');
            if (ind == -1 && ind2 == -1) {
                ind = source.indexOf(':');
            }
            ind = Math.max(ind, ind2);
            if (ind < 4) {
                return null;
            }
            String in = digits(source.substring(0, ind));
            int year;
            try {
                year = Integer.parseInt(in);
            } catch (NumberFormatException nfe) {
                return null;
            }
            in = digits(source.substring(ind + 1));
            if (in.length() != 1) {
                return null;
            }
            int q;
            try {
                q = Integer.parseInt(in);
            } catch (NumberFormatException nfe) {
                return null;
            }
            int month = (q - 1) * 3 + 2;
            calendar.clear();
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month);
            int date = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
            calendar.set(Calendar.DAY_OF_MONTH, date);
            ret = calendar.getTime();
            pos.setIndex(6);
        }
        return ret;
    }

    private static String digits(String in) {
        StringBuilder ret = new StringBuilder();
        for (int i = 0; i < in.length(); i++) {
            char c = in.charAt(i);
            if (Character.isDigit(c)) {
                ret.append(c);
            }
        }
        return ret.toString();
    }
}

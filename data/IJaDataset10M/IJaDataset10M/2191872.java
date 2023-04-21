package com.factorit.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateConverter {

    public static final String FORMATO_FECHA_HORA = "dd/MM/yyyy, HH:mm";

    public static final String FORMATO_FECHA = "dd/MM/yyyy";

    public static final String FORMATO_HORA = "HH:mm";

    public String convertDateToDateStringWithTime(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat(FORMATO_FECHA_HORA);
        String dateString = sdf.format(date);
        return dateString;
    }

    public String convertDateToString(Date date, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        String dateString = sdf.format(date);
        return dateString;
    }

    public String convertCalendarToDateStringWithTime(Calendar calendar) {
        SimpleDateFormat sdf = new SimpleDateFormat(FORMATO_FECHA_HORA);
        String dateString = sdf.format(calendar.getTime());
        return dateString;
    }

    public String convertCalendarToDateString(Calendar calendar) {
        SimpleDateFormat sdf = new SimpleDateFormat(FORMATO_FECHA);
        String dateString = sdf.format(calendar.getTime());
        return dateString;
    }

    public Calendar convertDateStringToCalendar(String dateString) throws ParseException {
        Date date;
        DateFormat formatter = new SimpleDateFormat(FORMATO_FECHA);
        date = (Date) formatter.parse(dateString);
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal;
    }

    public Date convertDateStringToDate(String dateString) throws ParseException {
        Date date;
        DateFormat formatter = new SimpleDateFormat(FORMATO_FECHA);
        date = (Date) formatter.parse(dateString);
        return date;
    }

    public Calendar convertDateStringToCalendar(String dateString, String timeString) throws ParseException {
        Date date;
        String dateStr = dateString + " " + timeString;
        DateFormat formatter = new SimpleDateFormat(FORMATO_FECHA_HORA);
        date = (Date) formatter.parse(dateStr);
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal;
    }

    public static boolean isToday(Date aDate) {
        Calendar aCal1 = Calendar.getInstance();
        aCal1.setTime(aDate);
        Date date1 = new Date();
        Calendar aCal2 = Calendar.getInstance();
        aCal2.setTime(date1);
        if ((aCal1.get(Calendar.DATE) == aCal2.get(Calendar.DATE)) && (aCal1.get(Calendar.YEAR) == aCal2.get(Calendar.YEAR)) && (aCal1.get(Calendar.MONTH) == aCal2.get(Calendar.MONTH))) {
            return true;
        } else {
            return false;
        }
    }
}

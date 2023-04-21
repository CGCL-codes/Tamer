package com.sns2Life.utils;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.StringTokenizer;
import org.apache.log4j.Logger;

public class DateFormat implements Serializable {

    private static Logger logger = Logger.getLogger(DateFormat.class);

    public static final String SDS_DATE_FORMAT = "dd/MM/yyyy";

    public DateFormat(Date dt) {
        cl = null;
        separatore_DateFormat = "/";
        separatore_ora = ":";
        cl = new GregorianCalendar();
        if (dt == null) {
            cl.setTime(new Date());
        } else cl.setTime(dt);
    }

    public DateFormat(String date, boolean slash) {
        cl = null;
        separatore_DateFormat = "/";
        separatore_ora = ":";
        int giorno = Integer.parseInt(date.substring(7, 8));
        int mese = Integer.parseInt(date.substring(5, 7));
        int anno = Integer.parseInt(date.substring(0, 4));
        cl = new GregorianCalendar();
        cl.set(anno, mese - 1, giorno, 0, 0);
    }

    public DateFormat(String date) {
        cl = null;
        separatore_DateFormat = "/";
        separatore_ora = ":";
        int giorno = Integer.parseInt(date.substring(0, 2));
        int mese = Integer.parseInt(date.substring(3, 5));
        int anno = Integer.parseInt(date.substring(6, 10));
        cl = new GregorianCalendar();
        cl.set(anno, mese - 1, giorno, 0, 0);
    }

    public DateFormat(String date, String hour, String minute) {
        cl = null;
        separatore_DateFormat = "/";
        separatore_ora = ":";
        int giorno = Integer.parseInt(date.substring(0, 2));
        int mese = Integer.parseInt(date.substring(3, 5));
        int anno = Integer.parseInt(date.substring(6, 10));
        int ora = Integer.parseInt(hour);
        int minuti = Integer.parseInt(minute);
        cl = new GregorianCalendar();
        cl.set(anno, mese - 1, giorno, ora, minuti);
    }

    public DateFormat(String date, String hour, String minute, String sec) {
        cl = null;
        separatore_DateFormat = "/";
        separatore_ora = ":";
        int giorno = Integer.parseInt(date.substring(0, 2));
        int mese = Integer.parseInt(date.substring(3, 5));
        int anno = Integer.parseInt(date.substring(6, 10));
        int ora = Integer.parseInt(hour);
        int minuti = Integer.parseInt(minute);
        int secondi = Integer.parseInt(sec);
        cl = new GregorianCalendar();
        cl.set(anno, mese - 1, giorno, ora, minuti, secondi);
    }

    public DateFormat(long data) {
        cl = null;
        separatore_DateFormat = "/";
        separatore_ora = ":";
        String tmp = "" + data;
        int anno = Integer.parseInt(tmp.substring(0, 4));
        int mese = Integer.parseInt(tmp.substring(4, 6));
        int giorno = Integer.parseInt(tmp.substring(6, 8));
        int ore = Integer.parseInt(tmp.substring(8, 10));
        int minuti = Integer.parseInt(tmp.substring(10, 12));
        int secondi = Integer.parseInt(tmp.substring(12, 14));
        cl = new GregorianCalendar();
        cl.set(anno, mese - 1, giorno, ore, minuti, secondi);
    }

    public DateFormat(int data) {
        cl = null;
        separatore_DateFormat = "/";
        separatore_ora = ":";
        String tmp = "" + data;
        int anno = Integer.parseInt(tmp.substring(0, 4));
        int mese = Integer.parseInt(tmp.substring(4, 6));
        int giorno = Integer.parseInt(tmp.substring(6, 8));
        cl = new GregorianCalendar();
        cl.set(anno, mese - 1, giorno, 0, 0);
    }

    public DateFormat(int anno, int mese, int giorno) {
        cl = null;
        separatore_DateFormat = "/";
        separatore_ora = ":";
        cl = new GregorianCalendar();
        cl.set(anno, mese - 1, giorno, 0, 0);
    }

    public DateFormat() {
        cl = null;
        separatore_DateFormat = "/";
        separatore_ora = ":";
        cl = new GregorianCalendar();
    }

    public String DateFormat_AAAAMMGG(boolean separatore) {
        if (separatore) return cl.get(1) + separatore_DateFormat + controlla(cl.get(2) + 1) + separatore_DateFormat + controlla(cl.get(5)); else return "" + cl.get(1) + controlla(cl.get(2) + 1) + controlla(cl.get(5));
    }

    public String DateFormat_AAAAMMGGHHMISS(boolean separatore) {
        if (separatore) return cl.get(1) + separatore_DateFormat + controlla(cl.get(2) + 1) + separatore_DateFormat + controlla(cl.get(5)) + separatore_DateFormat + controlla(cl.get(11)) + separatore_DateFormat + controlla(cl.get(12)) + separatore_DateFormat + controlla(cl.get(13)); else return "" + cl.get(1) + controlla(cl.get(2) + 1) + controlla(cl.get(5)) + controlla(cl.get(11)) + controlla(cl.get(12)) + controlla(cl.get(13));
    }

    public String DateFormat_GGMMAAAA(boolean separatore) {
        if (separatore) {
            logger.debug("End - using separator " + separatore_DateFormat);
            return controlla(cl.get(5)) + separatore_DateFormat + controlla(cl.get(2) + 1) + separatore_DateFormat + cl.get(1);
        } else {
            return "" + controlla(cl.get(5)) + controlla(cl.get(2) + 1) + cl.get(1);
        }
    }

    public String DateFormat_HHMMSS(boolean separatore) {
        if (separatore) return controlla(cl.get(11)) + separatore_ora + controlla(cl.get(12)) + separatore_ora + controlla(cl.get(13)); else return "" + controlla(cl.get(11)) + controlla(cl.get(12)) + controlla(cl.get(13));
    }

    public String DateFormat_HHMMSSmm(boolean separatore) {
        if (separatore) return controlla(cl.get(11)) + separatore_ora + controlla(cl.get(12)) + separatore_ora + controlla(cl.get(13)) + separatore_ora + controlla(cl.get(14)); else return "" + controlla(cl.get(11)) + controlla(cl.get(12)) + controlla(cl.get(13)) + controlla(cl.get(14));
    }

    public String DateFormat_MMGGAAAA(boolean separatore) {
        if (separatore) return controlla(cl.get(2) + 1) + separatore_DateFormat + controlla(cl.get(5)) + separatore_DateFormat + cl.get(1); else return "" + cl.get(1) + controlla(cl.get(2) + 1) + controlla(cl.get(5));
    }

    public int DateFormat_time_numsecondi() {
        return cl.get(11) * 3600 + cl.get(12) * 60 + cl.get(13);
    }

    public void addDays(int days) {
        cl.add(5, days);
    }

    public void addMinutes(int minutes) {
        cl.add(12, minutes);
    }

    public void addMonths(int months) {
        if (months < 0) months = 0;
        cl.add(2, months);
    }

    private String controlla(int dato) {
        if (dato < 10) return "0" + dato; else return "" + dato;
    }

    public Date getDate() {
        return cl.getTime();
    }

    public String getSeparatore_DateFormat() {
        return separatore_DateFormat;
    }

    public String getSeparatore_ora() {
        return separatore_ora;
    }

    public static void main(String arg[]) {
        DateFormat date = new DateFormat(0x1237e1481decL);
        date.addMinutes(39);
    }

    public void setDateFormat(Date DateFormat) {
        cl = new GregorianCalendar();
        cl.setTime(DateFormat);
    }

    public void setDateFormatGGMMAAAA(String DateFormat) {
        StringTokenizer st = new StringTokenizer(DateFormat, getSeparatore_DateFormat());
        int giorno = Integer.parseInt(st.nextToken());
        int mese = Integer.parseInt(st.nextToken());
        int anno = Integer.parseInt(st.nextToken());
        cl.set(anno, mese - 1, giorno, 0, 0);
    }

    public void setHourUtil(String sHour) {
        int ore = Integer.parseInt(sHour.substring(0, 2));
        int minuti = Integer.parseInt(sHour.substring(3, 5));
        int secondi = Integer.parseInt(sHour.substring(6, 8));
        cl.set(11, ore);
        cl.set(12, minuti);
        cl.set(13, secondi);
    }

    public void setHourUtil(int hour) {
        String tmp = "" + hour;
        if (tmp.length() < 6) tmp = "0" + hour;
        int ore = Integer.parseInt(tmp.substring(0, 2));
        int minuti = Integer.parseInt(tmp.substring(2, 4));
        int secondi = Integer.parseInt(tmp.substring(4, 6));
        cl.set(11, ore);
        cl.set(12, minuti);
        cl.set(13, secondi);
    }

    public void setSeparatore_DateFormat(String newSeparatore_DateFormat) {
        separatore_DateFormat = newSeparatore_DateFormat;
    }

    public void setSeparatore_ora(String newSeparatore_ora) {
        separatore_ora = newSeparatore_ora;
    }

    private Calendar cl;

    private String separatore_DateFormat;

    private String separatore_ora;
}

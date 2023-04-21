package gov.nasa.gsfc.visbard.ovt.datatype;

import java.util.*;

public class Time {

    public static final double Y2000 = Time.getMjd(2000, 1, 1, 0, 0);

    public static final double Y1970 = Time.getMjd(1970, 1, 1, 0, 0);

    public static final double Y1960 = Time.getMjd(1960, 1, 1, 0, 0);

    public static final int YEAR = 0;

    public static final int MONTH = 1;

    public static final int DAY = 2;

    public static final int HOUR = 3;

    public static final int MINUTE = 4;

    public static final int SECOND = 5;

    public static final int MILISECOND = 6;

    /** This value should be used in the case of error mjd 
   * This can heppen if you have to return some mjd, but something went wrong
   */
    public static final double ERROR_MJD = Double.MIN_VALUE;

    public static final int MINUTES_IN_DAY = 24 * 60;

    public static final int SECONDS_IN_DAY = 24 * 60 * 60;

    public static final double DAYS_IN_SECOND = 1. / (double) SECONDS_IN_DAY;

    int year = 0, month = 0, day = 0, hour = 0, mins = 0, sec = 0, msec = 0;

    public Time(String time) throws NumberFormatException {
        setTime(time);
    }

    public Time(double mjd) throws IllegalArgumentException {
        setTime(mjd);
    }

    public Time(int year, int month, int day, int hour, int mins, int sec, int msec) throws IllegalArgumentException {
        setTime(year, month, day, hour, mins, sec, msec);
    }

    public Time(int year, int month, int day, int hour, int mins) throws IllegalArgumentException {
        setTime(year, month, day, hour, mins);
    }

    public Time(Calendar date) {
        setTime(date.get(Calendar.YEAR), date.get(Calendar.MONTH) + 1, date.get(Calendar.DAY_OF_MONTH), date.get(Calendar.HOUR_OF_DAY), date.get(Calendar.MINUTE), date.get(Calendar.SECOND), 0);
    }

    public void setTime(int year, int month, int day, int hour, int mins, int sec, int msec) throws IllegalArgumentException {
        if (!isValid(year, month, day, hour, mins, sec, msec)) throw new IllegalArgumentException();
        this.year = year;
        this.month = month;
        this.day = day;
        this.hour = hour;
        this.mins = mins;
        this.sec = sec;
        this.msec = msec;
    }

    public void setTime(int year, int month, int day, int hour, int mins) throws IllegalArgumentException {
        setTime(year, month, day, hour, mins, 0, 0);
    }

    public void setTime(String time) throws NumberFormatException {
        try {
            year = Integer.valueOf(time.substring(0, 4)).intValue();
            month = Integer.valueOf(time.substring(5, 7)).intValue();
            day = Integer.valueOf(time.substring(8, 10)).intValue();
            hour = Integer.valueOf(time.substring(11, 13)).intValue();
            mins = Integer.valueOf(time.substring(14, 16)).intValue();
            if (time.length() >= 19) sec = Integer.valueOf(time.substring(17, 19)).intValue(); else sec = 0;
            msec = 0;
            if (!isValid(year, month, day, hour, mins, sec, msec)) throw new IllegalArgumentException();
            setTime(year, month, day, hour, mins, sec, msec);
        } catch (IndexOutOfBoundsException e2) {
            throw new NumberFormatException(e2.getMessage());
        } catch (IllegalArgumentException e3) {
            throw new NumberFormatException(e3.getMessage());
        }
    }

    public static double getMjd(String s) throws NumberFormatException {
        Time t = new Time(s);
        return t.getMjd();
    }

    public void setTime(double mjd) {
        int jday;
        double temp;
        int l, m, n, jj;
        temp = mjd + 5.7870370370370369e-9;
        jday = (int) temp;
        l = (jday + 18204) * 4000 / 1461001;
        n = jday - (l * 1461) / 4 + 18234;
        m = (n * 80) / 2447;
        this.day = n - (m * 2447) / 80;
        jj = m / 11;
        this.month = m + 2 - jj * 12;
        this.year = l + 1900 + jj;
        temp = (temp - jday) * 24.;
        this.hour = (int) temp;
        temp = (temp - hour) * 60.;
        this.mins = (int) temp;
        temp = (temp - mins) * 60.;
        this.sec = (int) temp;
        temp = (temp - sec) * 1000.;
        this.msec = (int) (temp + .5);
    }

    public void setTime(Time time) {
        this.year = time.year;
        this.month = time.month;
        this.day = time.day;
        this.hour = time.hour;
        this.mins = time.mins;
        this.sec = time.sec;
        this.msec = time.msec;
    }

    public static double getMjd(int year, int month, int day, int hour, int mins, int sec, int msec) throws NumberFormatException {
        int jj, l;
        double temp_mjd;
        jj = (14 - month) / 12;
        l = year % 1900 - jj;
        temp_mjd = day - 18234 + (l * 1461) / 4 + ((month - 2 + jj * 12) * 367) / 12;
        temp_mjd += ((hour * 3600 + mins * 60 + sec) * 1000 + msec) / 8.64e7;
        return temp_mjd;
    }

    public static double getMjd(int year, int month, int day, int hour, int mins) throws NumberFormatException {
        return getMjd(year, month, day, hour, mins, 0, 0);
    }

    public static int getYear(double mjd) {
        return new Time(mjd).year;
    }

    public int get(int what) throws NumberFormatException {
        switch(what) {
            case YEAR:
                return year;
            case MONTH:
                return month;
            case DAY:
                return day;
            case HOUR:
                return hour;
            case MINUTE:
                return mins;
            case SECOND:
                return sec;
            case MILISECOND:
                return msec;
            default:
                throw new IllegalArgumentException("Invalid type : " + what);
        }
    }

    public String getAsText(int what) throws NumberFormatException {
        int n = get(what);
        String res = "" + n;
        if (res.length() == 1) res = "0" + res;
        return res;
    }

    public int getYear() {
        return year;
    }

    public int getMonth() {
        return month;
    }

    public int getDay() {
        return day;
    }

    public int getHour() {
        return hour;
    }

    public int getMinutes() {
        return mins;
    }

    public void setMinutes(int mins) {
        this.mins = mins;
    }

    public int getSeconds() {
        return sec;
    }

    public void setSeconds(int sec) {
        this.sec = sec;
    }

    public int getMsec() {
        return msec;
    }

    /** This is a quick hack. There could be a better solution */
    public int getDayOfTheYear() {
        return (int) (getMjd() - new Time(year, 1, 1, 0, 0).getMjd());
    }

    public double getMjd() {
        return getMjd(year, month, day, hour, mins, sec, msec);
    }

    public String toString() {
        String yeart, montht, dayt, hourt, minst, sect;
        yeart = String.valueOf(year);
        montht = String.valueOf(month);
        dayt = String.valueOf(day);
        hourt = String.valueOf(hour);
        minst = String.valueOf(mins);
        sect = String.valueOf(sec);
        if (montht.length() == 1) {
            montht = "0" + montht;
        }
        if (dayt.length() == 1) {
            dayt = "0" + dayt;
        }
        if (hourt.length() == 1) {
            hourt = "0" + hourt;
        }
        if (minst.length() == 1) {
            minst = "0" + minst;
        }
        if (sect.length() == 1) {
            sect = "0" + sect;
        }
        return yeart + "-" + montht + "-" + dayt + " " + hourt + ":" + minst + ":" + sect;
    }

    public static String toString(double mjd) {
        Time t = new Time(mjd);
        return t.toString();
    }

    public String getLongTime() {
        return toString() + String.valueOf(sec) + ":" + String.valueOf(msec);
    }

    public static boolean isValid(String time) {
        int yeart, montht, dayt, hourt, minst, sect = 0;
        if (time.length() < 16) return false;
        if ((time.charAt(4) != '-') || (time.charAt(7) != '-') || (time.charAt(10) != ' ') || (time.charAt(13) != ':')) {
            return false;
        }
        try {
            yeart = Integer.valueOf(time.substring(0, 4)).intValue();
            montht = Integer.valueOf(time.substring(5, 7)).intValue();
            dayt = Integer.valueOf(time.substring(8, 10)).intValue();
            hourt = Integer.valueOf(time.substring(11, 13)).intValue();
            minst = Integer.valueOf(time.substring(14, 16)).intValue();
            if (time.length() >= 19) sect = Integer.valueOf(time.substring(17, 19)).intValue();
        } catch (IllegalArgumentException e) {
            return false;
        }
        if (!isValid(yeart, montht, dayt, hourt, minst, sect, 0)) return false;
        return true;
    }

    public static boolean isValid(int yeart, int montht, int dayt, int hourt, int minst, int sect, int msect) {
        if ((hourt > 23) || (hourt < 0) || (minst > 59) || (minst < 0)) {
            return false;
        }
        if ((yeart < 1900) || (yeart > 4000) || (montht < 1) || (montht > 12) || (dayt < 1)) {
            return false;
        }
        if ((montht == 1) && (dayt > 31)) {
            System.out.println("1");
            return false;
        }
        if ((montht == 2) && (yeart % 4 == 0) && (dayt > 29)) {
            System.out.println("Vysokosnyj god - 29 days");
            return false;
        }
        if ((montht == 2) && (dayt > 30)) {
            System.out.println("2a");
            return false;
        }
        if ((montht == 3) && (dayt > 31)) {
            System.out.println("3");
            return false;
        }
        if ((montht == 4) && (dayt > 30)) {
            System.out.println("4");
            return false;
        }
        if ((montht == 5) && (dayt > 31)) {
            System.out.println("5");
            return false;
        }
        if ((montht == 6) && (dayt > 30)) {
            System.out.println("6");
            return false;
        }
        if ((montht == 7) && (dayt > 31)) {
            System.out.println("7");
            return false;
        }
        if ((montht == 8) && (dayt > 31)) {
            System.out.println("8");
            return false;
        }
        if ((montht == 9) && (dayt > 30)) {
            System.out.println("9");
            return false;
        }
        if ((montht == 10) && (dayt > 31)) {
            System.out.println("10");
            return false;
        }
        if ((montht == 11) && (dayt > 30)) {
            System.out.println("11");
            return false;
        }
        if ((montht == 12) && (dayt > 31)) {
            System.out.println("12");
            return false;
        }
        return true;
    }

    public Object clone() {
        return new Time(year, month, day, hour, mins, sec, msec);
    }

    public static double getGSMTime(double mjd) {
        return new Julian(mjd).getGSMTime();
    }

    /** converts mjd to julian */
    public static Julian getJulian(double mjd) {
        return new Julian(mjd);
    }

    public static void main(String argv[]) {
        Time time = new Time("1980-02-29 00:00");
        Time newtime = new Time("2000-01-29 00:00");
        time.setTime(newtime);
        System.out.println(new Time(0, 0, 0, 1, 0).toString());
    }
}

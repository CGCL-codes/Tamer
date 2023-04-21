package org.aiotrade.util;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * 
 * @author Caoyuan Deng 
 */
public class NaturalCalendar {

    private static Calendar calendar = new GregorianCalendar();

    /**
     * The largest decimal literal of type int is 2,147,483,648 (2^31),
     * the total seconds of 1 year is about 366 * 24 * 60 * 60 = 31,622,400
     * the total seconds of 100 years is about 3,162,240,000
     * the total seconds of 68 years is about 2,150,323,200
     * So, if we choose the timeBaseDate as Jan 1, 1970,
     * plus 68 years to Jan 1, 2038, minus 50 years to Jan 1, 1902
     */
    private static final NaturalCalendar timeBaseDate = new NaturalCalendar(1970, 1, 1);

    public int YEAR;

    public int MONTH;

    public float FLOAT_DAY;

    public int DAY;

    public int HOUR;

    public int MINUTE;

    public int SECOND;

    /** Julian Date, the days from epoch: Greenwich mean noon of January 1, 4713 B.C. */
    public double JULIAN_DATE;

    private static final int ONE_SECOND = 1;

    private static final int ONE_MINUTE = 60 * ONE_SECOND;

    private static final int ONE_HOUR = 60 * ONE_MINUTE;

    private static final long ONE_DAY = 24 * ONE_HOUR;

    private static final long ONE_WEEK = 7 * ONE_DAY;

    /** Creat a new instance point to today
     */
    public NaturalCalendar() {
        Calendar now = new GregorianCalendar();
        set(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH));
    }

    public NaturalCalendar(int y, int m, float d) {
        set(y, m, d);
    }

    public NaturalCalendar(double julianDate) {
        set(julianDate);
    }

    public NaturalCalendar(Calendar date) {
        set(date.get(Calendar.YEAR), date.get(Calendar.MONTH) + 1, date.get(Calendar.DAY_OF_MONTH));
    }

    public static NaturalCalendar getTimeBaseDate() {
        return timeBaseDate;
    }

    public static float calcJulianDate(int y, int m, float d) {
        int year = y;
        int month = m;
        float float_day = d;
        int day = (int) float_day;
        int A, B, C, D;
        if (m == 1 | m == 2) {
            y -= 1;
            m += 12;
        }
        A = (int) (y / 100);
        B = 2 - A + (int) (A / 4);
        C = (int) (365.25 * y);
        D = (int) (30.6001 * (m + 1));
        return (float) (B + C + D + d + 1720994.5);
    }

    public NaturalCalendar set(int y, int m, float d) {
        YEAR = y;
        MONTH = m;
        FLOAT_DAY = d;
        DAY = (int) FLOAT_DAY;
        int A, B, C, D;
        if (m == 1 | m == 2) {
            y -= 1;
            m += 12;
        }
        A = (int) (y / 100);
        B = 2 - A + (int) (A / 4);
        C = (int) (365.25 * y);
        D = (int) (30.6001 * (m + 1));
        JULIAN_DATE = (double) (B + C + D + d + 1720994.5);
        return this;
    }

    public NaturalCalendar set(int y, int m, int dayOfMonth, int hours, int minutes, int seconds) {
        YEAR = y;
        MONTH = m;
        FLOAT_DAY = dayOfMonth + (float) (hours * ONE_HOUR + minutes * ONE_MINUTE + seconds) / (float) ONE_DAY;
        DAY = (int) FLOAT_DAY;
        HOUR = hours;
        MINUTE = minutes;
        SECOND = seconds;
        int A, B, C, D;
        if (m == 1 | m == 2) {
            y -= 1;
            m += 12;
        }
        A = (int) (y / 100);
        B = 2 - A + (int) (A / 4);
        C = (int) (365.25 * y);
        D = (int) (30.6001 * (m + 1));
        JULIAN_DATE = (double) (B + C + D + dayOfMonth + 1720994.5);
        return this;
    }

    public NaturalCalendar set(double julianDate) {
        this.JULIAN_DATE = julianDate;
        int I, A, B, C, D, E, G;
        float F;
        julianDate += 0.5;
        I = (int) (julianDate);
        F = (float) (I - julianDate);
        if (I > 2299160) {
            A = (int) ((I - 1867216.25) / 36524.25);
            B = I + 1 + A - (int) (A / 4);
        } else {
            B = I;
        }
        C = B + 1524;
        D = (int) ((C - 122.1) / 365.25);
        E = (int) (365.25 * D);
        G = (int) ((C - E) / 30.6001);
        FLOAT_DAY = C - E - F - (int) (30.6001 * G);
        MONTH = G < 13.5 ? G - 1 : G - 13;
        YEAR = MONTH > 2.5 ? D - 4716 : D - 4715;
        DAY = (int) FLOAT_DAY;
        return this;
    }

    private NaturalCalendar setByTimeInMillins(long time) {
        set(time + getTimeBaseDate().JULIAN_DATE);
        return this;
    }

    public NaturalCalendar add(float i) {
        JULIAN_DATE += i;
        set(JULIAN_DATE);
        return this;
    }

    /** Return period since timeBaseDate */
    private long getTime() {
        return (long) (JULIAN_DATE - getTimeBaseDate().JULIAN_DATE);
    }

    public boolean after(NaturalCalendar date) {
        return JULIAN_DATE > date.JULIAN_DATE ? true : false;
    }

    public boolean before(NaturalCalendar date) {
        return JULIAN_DATE < date.JULIAN_DATE ? true : false;
    }

    public boolean equals(NaturalCalendar date) {
        return JULIAN_DATE == date.JULIAN_DATE ? true : false;
    }

    public String toDateString() {
        String dateString;
        if (MONTH < 10 & FLOAT_DAY < 10) {
            dateString = "0" + MONTH + "-" + "0" + (int) FLOAT_DAY + "-" + YEAR;
        } else if (MONTH < 10 & FLOAT_DAY >= 10) {
            dateString = "0" + MONTH + "-" + (int) FLOAT_DAY + "-" + YEAR;
        } else if (MONTH >= 10 & FLOAT_DAY < 10) {
            dateString = MONTH + "-" + "0" + (int) FLOAT_DAY + "-" + YEAR;
        } else {
            dateString = MONTH + "-" + (int) FLOAT_DAY + "-" + YEAR;
        }
        return dateString;
    }

    public String toMonthDayString() {
        String dateString;
        if (MONTH < 10 & FLOAT_DAY < 10) {
            dateString = "0" + MONTH + "-" + "0" + (int) FLOAT_DAY;
        } else if (MONTH < 10 & FLOAT_DAY >= 10) {
            dateString = "0" + MONTH + "-" + (int) FLOAT_DAY;
        } else if (MONTH >= 10 & FLOAT_DAY < 10) {
            dateString = MONTH + "-" + "0" + (int) FLOAT_DAY;
        } else {
            dateString = MONTH + "-" + (int) FLOAT_DAY;
        }
        return dateString;
    }

    /** Calculate Chinese Month
     */
    public int getChineseMonth() {
        int m = -1;
        if (MONTH == 2 && FLOAT_DAY == 3) {
            m = 1;
        } else if (MONTH == 3 && FLOAT_DAY == 6) {
            m = 2;
        } else if (MONTH == 4 && FLOAT_DAY == 5) {
            m = 3;
        } else if (MONTH == 5 && FLOAT_DAY == 5) {
            m = 4;
        } else if (MONTH == 6 && FLOAT_DAY == 6) {
            m = 5;
        } else if (MONTH == 7 && FLOAT_DAY == 7) {
            m = 6;
        } else if (MONTH == 8 && FLOAT_DAY == 7) {
            m = 7;
        } else if (MONTH == 9 && FLOAT_DAY == 8) {
            m = 8;
        } else if (MONTH == 10 && FLOAT_DAY == 8) {
            m = 9;
        } else if (MONTH == 11 && FLOAT_DAY == 7) {
            m = 10;
        } else if (MONTH == 12 && FLOAT_DAY == 7) {
            m = 11;
        } else if (MONTH == 1 && FLOAT_DAY == 6) {
            m = 12;
        }
        return m;
    }

    /** Methods to calculate four seasons period
     */
    public int getSeasonPeriod() {
        int m = -1;
        NaturalCalendar today = this;
        NaturalCalendar startDate, endDate;
        startDate = new NaturalCalendar(today.YEAR, 3, 21);
        endDate = new NaturalCalendar(today.YEAR, 6, 21);
        if (today.JULIAN_DATE >= startDate.JULIAN_DATE && today.JULIAN_DATE < endDate.JULIAN_DATE) {
            m = 1;
        }
        startDate.set(today.YEAR, 6, 21);
        endDate.set(today.YEAR, 9, 22);
        if (today.JULIAN_DATE >= startDate.JULIAN_DATE && today.JULIAN_DATE < endDate.JULIAN_DATE) {
            m = 2;
        }
        startDate.set(today.YEAR, 9, 22);
        endDate.set(today.YEAR, 12, 21);
        if (today.JULIAN_DATE >= startDate.JULIAN_DATE && today.JULIAN_DATE < endDate.JULIAN_DATE) {
            m = 3;
        }
        startDate.set(today.YEAR, 12, 21);
        endDate.set(today.YEAR, 12, 31);
        if (today.JULIAN_DATE >= startDate.JULIAN_DATE && today.JULIAN_DATE < endDate.JULIAN_DATE) {
            m = 4;
        }
        startDate.set(today.YEAR - 1, 12, 31);
        endDate.set(today.YEAR, 3, 20);
        if (today.JULIAN_DATE >= startDate.JULIAN_DATE && today.JULIAN_DATE < endDate.JULIAN_DATE) {
            m = 4;
        }
        return m;
    }

    /** Method to calculate Gann important dates
     */
    public static int getGannImportantDate(Date date) {
        calendar.setTime(date);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int m = -1;
        switch(month) {
            case 1:
                if (day >= 7 && day <= 10 || day >= 19 && day <= 24) {
                    m = 1;
                }
                break;
            case 2:
                if (day >= 3 && day <= 10 || day >= 20 && day <= 25) {
                    m = 2;
                }
                break;
            case 3:
                if (day >= 20 && day <= 27) {
                    m = 3;
                }
                break;
            case 4:
                if (day >= 7 && day <= 12 || day >= 20 && day <= 25) {
                    m = 4;
                }
                break;
            case 5:
                if (day >= 3 && day <= 10 || day >= 21 && day <= 28) {
                    m = 5;
                }
                break;
            case 6:
                if (day >= 10 && day <= 15 || day >= 21 && day <= 27) {
                    m = 6;
                }
                break;
            case 7:
                if (day >= 7 && day <= 10 || day >= 21 && day <= 27) {
                    m = 7;
                }
                break;
            case 8:
                if (day >= 5 && day <= 8 || day >= 14 && day <= 20) {
                    m = 8;
                }
                break;
            case 9:
                if (day >= 3 && day <= 10 || day >= 21 && day <= 28) {
                    m = 9;
                }
                break;
            case 10:
                if (day >= 7 && day <= 14 || day >= 21 && day <= 30) {
                    m = 10;
                }
                break;
            case 11:
                if (day >= 5 && day <= 10 || day >= 20 && day <= 30) {
                    m = 11;
                }
                break;
            case 12:
                if (day >= 3 && day <= 10 || day >= 15 && day <= 24) {
                    m = 12;
                }
                break;
        }
        return m;
    }

    private static String DATE_FORMAT = "YYYY-MM-DD";

    public static void setDateFormat(String dateFormat) {
        DATE_FORMAT = dateFormat.toUpperCase().trim();
        int year_s = DATE_FORMAT.indexOf('Y');
        int year_e = DATE_FORMAT.lastIndexOf('Y');
        int month_s = DATE_FORMAT.indexOf('M');
        int month_e = DATE_FORMAT.lastIndexOf('M');
        int day_s = DATE_FORMAT.indexOf('D');
        int day_e = DATE_FORMAT.lastIndexOf('D');
        int month_l = month_e - month_s + 1;
        int year_l = year_e - year_s + 1;
    }

    public String parseDate(String dateString) {
        String dateStr = dateString.trim();
        int year, month, day;
        if (DATE_FORMAT.equalsIgnoreCase("YYYY-MM-DD")) {
            year = Integer.parseInt(dateStr.substring(0, 3));
            month = Integer.parseInt(dateStr.substring(5, 6));
            day = Integer.parseInt(dateStr.substring(8, 9));
        } else if (DATE_FORMAT.equalsIgnoreCase("YYYYMMDD")) {
            year = Integer.parseInt(dateStr.substring(0, 3));
            month = Integer.parseInt(dateStr.substring(4, 5));
            day = Integer.parseInt(dateStr.substring(6, 7));
        } else if (DATE_FORMAT.equalsIgnoreCase("YYMMDD")) {
            year = Integer.parseInt(dateStr.substring(0, 1));
            year = year > 20 ? year + 1900 : year + 2000;
            month = Integer.parseInt(dateStr.substring(2, 3));
            day = Integer.parseInt(dateStr.substring(4, 5));
        } else if (DATE_FORMAT.equalsIgnoreCase("YYYY-MMM-DD")) {
            year = Integer.parseInt(dateStr.substring(0, 1));
            year = year > 20 ? year + 1900 : year + 2000;
            month = Integer.parseInt(dateStr.substring(2, 3));
            day = Integer.parseInt(dateStr.substring(4, 5));
        }
        return dateString;
    }

    /** Convert JAN, FEB etc to numeric MONTH
     * @param String MONTH
     */
    private byte toNumericMonth(String month) {
        byte m = 0;
        if (month.toUpperCase().startsWith("JAN")) {
            m = 1;
        }
        if (month.toUpperCase().startsWith("FEB")) {
            m = 2;
        }
        if (month.toUpperCase().startsWith("MAR")) {
            m = 3;
        }
        if (month.toUpperCase().startsWith("APR")) {
            m = 4;
        }
        if (month.toUpperCase().startsWith("MAY")) {
            m = 5;
        }
        if (month.toUpperCase().startsWith("JUN")) {
            m = 6;
        }
        if (month.toUpperCase().startsWith("JUL")) {
            m = 7;
        }
        if (month.toUpperCase().startsWith("AUG")) {
            m = 8;
        }
        if (month.toUpperCase().startsWith("SEP")) {
            m = 9;
        }
        if (month.toUpperCase().startsWith("OCT")) {
            m = 10;
        }
        if (month.toUpperCase().startsWith("NOV")) {
            m = 11;
        }
        if (month.toUpperCase().startsWith("DEC")) {
            m = 12;
        }
        return m;
    }

    public static void main(String[] args) {
        NaturalCalendar date = new NaturalCalendar(1900, 1, 1);
        System.out.println("Julian Date: " + date.JULIAN_DATE);
    }
}

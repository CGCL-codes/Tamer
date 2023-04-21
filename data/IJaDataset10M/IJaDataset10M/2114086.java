package wdc.utils;

import java.io.*;

/**
The class is used to manipulate with dates.
*/
public class WDCDay implements Serializable, Cloneable {

    public static final String[] MONTHS = { "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec" };

    public static final long MILLISECONDS_PER_DAY = 24 * 3600000L;

    private int year;

    private int month;

    private int day;

    /**
   * Constructs an invalid date.
   * @see WDCDay#isValid
   */
    public WDCDay() {
        year = 0;
        month = 0;
        day = 0;
    }

    /**
   * Constructs a date using year, month, day.
   * @param year The year
   * @param month The month
   * @param day The day
   * @see WDCDay#isValid
   */
    public WDCDay(int year, int month, int day) {
        this.year = year;
        this.month = month;
        this.day = day;
    }

    /**
   * Constructs a date using its text representation. Use isValid() method to check that date is valid.
   * @param dateStr The text date represantation (must have the same length as dateFormat)
   * @param dateFormat The date format (see standard date formats):
   * d, dd - day, M, MM, MMM - month, y, yyyy - year
   * (Ex. 21-01-1999 or 01-01-1999 is dd-MM-yyyy, 21-Jan-1999 is dd-MMM-yyyy)
   * @see WDCDay#isValid
   */
    public WDCDay(String dateStr, String dateFormat) {
        year = 0;
        month = 0;
        day = 0;
        if (dateStr == null || dateFormat == null || dateStr.length() != dateFormat.length()) return;
        int ind = dateFormat.indexOf("yyyy");
        if (ind != -1) {
            try {
                year = Integer.parseInt(dateStr.substring(ind, ind + 4));
            } catch (NumberFormatException e) {
            }
            ;
        }
        ind = dateFormat.indexOf("MMM");
        if (ind != -1) {
            month = findMonthNum(dateStr.substring(ind, ind + 3));
        } else {
            ind = dateFormat.indexOf("MM");
            if (ind != -1) {
                try {
                    month = Integer.parseInt(dateStr.substring(ind, ind + 2));
                } catch (NumberFormatException e) {
                }
                ;
            }
        }
        ind = dateFormat.indexOf("dd");
        if (ind != -1) {
            try {
                day = Integer.parseInt(dateStr.substring(ind, ind + 2));
            } catch (NumberFormatException e) {
            }
            ;
        }
    }

    /** Constructs object from canonical date: yyyy-MM-dd, yyyy-M-d and so on.
  * Creates invalid date (0000-00-00) if some format errors occurred
  */
    public WDCDay(String dateStr) {
        try {
            int ind = dateStr.indexOf('-');
            year = Integer.parseInt(dateStr.substring(0, ind));
            int ind2 = dateStr.indexOf('-', ind + 1);
            month = Integer.parseInt(dateStr.substring(ind + 1, ind2));
            day = Integer.parseInt(dateStr.substring(ind2 + 1));
        } catch (Exception e) {
            year = month = day = 0;
        }
    }

    /** Constructs date using Julian date.
   * Alternate constructor for a date.  Currently, this constructor will
   * allow an invalid date to be created.  Invalid dates, however, can
   * be tested for with the 'isValid' method in this class after the object
   * is created.
   * Convert day of year to a date.
   * @param year year
   * @param jday Julian day (1<=jday<=366)
   * @see WDCDay#isValid
   */
    public WDCDay(int year, int jday) {
        this.year = year;
        int nDays = 0;
        for (int mon = 1; mon <= 12; mon++) {
            nDays += daysInMonth(year, mon);
            if (nDays >= jday) {
                this.day = jday - (nDays - daysInMonth(year, mon));
                this.month = mon;
                break;
            }
        }
    }

    /** Constructs object from dayId: yyyymmdd
  */
    public WDCDay(int dayId) {
        year = dayId / 10000;
        month = (dayId % 10000) / 100;
        day = dayId % 100;
    }

    /** Constructor of copy of object
  */
    public WDCDay(WDCDay d) {
        year = d.year;
        month = d.month;
        day = d.day;
    }

    /** Creates copy of object
  */
    public Object clone() {
        return new WDCDay(this);
    }

    /** It adds one day to the current date
  */
    public void nextDay() {
        day++;
        if (day > daysInMonth(year, month)) {
            day = 1;
            month++;
            if (month > 12) {
                month = 1;
                year++;
            }
        }
    }

    ;

    /** It remove one day from the current date
  */
    public void prevDay() {
        day--;
        if (day <= 0) {
            month--;
            if (month <= 0) {
                month = 12;
                year--;
            }
            day = daysInMonth(year, month);
        }
    }

    ;

    /** Compares two days
  */
    public boolean equals(Object d) {
        return d != null && year == ((WDCDay) d).year && month == ((WDCDay) d).month && day == ((WDCDay) d).day;
    }

    ;

    /**
   * Check to see if date is valid:
   * year >= 1
   * month in [1,12]
   * day in [1, daysInMonth]
   * @return true if valid, false if not
   */
    public boolean isValid() {
        if (year < 1 || month > 12 || month < 1 || day < 1 || day > daysInMonth(year, month)) return false; else return true;
    }

    /** Corrects date to the nearest valid one (year>=1, month in [1,12], day in [1, daysInMonth]).
  * It does nothing if date is correct
  * @return reference to this WDCDay object
  */
    public WDCDay correctDate() {
        if (year < 1) year = 1;
        if (month > 12) month = 12; else if (month < 1) month = 1;
        int numDays = daysInMonth(year, month);
        if (day > numDays) day = numDays; else if (day < 1) day = 1;
        return this;
    }

    /**
   * Accessor method for the year
   * @return year
   * @see WDCDay#getMonth
   * @see WDCDay#getDay
   */
    public int getYear() {
        return year;
    }

    /**
   * Accessor method for the month
   * @return month
   * @see WDCDay#getYear
   * @see WDCDay#getDay
   */
    public int getMonth() {
        return month;
    }

    /**
   * Accessor method for the day
   * @return day
   * @see WDCDay#getYear
   * @see WDCDay#getMonth
   */
    public int getDay() {
        return day;
    }

    /** Returns dayId: yyyymmdd
  */
    public int getDayId() {
        return year * 10000 + month * 100 + day;
    }

    /**
   * Determines if the year is a leap year
   * @param year year
   * @return true if it is a leap year, otherwise, false
   */
    public static boolean isLeapYear(int year) {
        if (year % 4 == 0 && year % 100 != 0 || year % 400 == 0) return true; else return false;
    }

    /**
   * Returns number of days in the given month of the year
   * @param year The year
   * @param month The month
   * @return Number of days in the month
   */
    public static int daysInMonth(int year, int month) {
        if (month <= 0 || month > 12) return 0;
        final int[] daysInMonth = { 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };
        if (month == 2 && isLeapYear(year)) return daysInMonth[month - 1] + 1; else return daysInMonth[month - 1];
    }

    /**
   * Returns number of days in the given year
   * @param year The year
   * @return the number of days in the year
   */
    public static int daysInYear(int year) {
        return (isLeapYear(year)) ? 366 : 365;
    }

    /**
   * Returns number of days in the given month of the year
   * @return Number of days in the month
   */
    public int daysInMonth() {
        return daysInMonth(year, month);
    }

    /**
   * Returns number of days in the given year
   * @return the number of days in the year
   */
    public int daysInYear() {
        return daysInYear(year);
    }

    /** Returns number of a month [1..12].
   * @param monthStr The short month name (like Jan, Feb, ...)
   * @return month number [1..12] or 0 in wrong name
   */
    public static int findMonthNum(String monthStr) {
        if (monthStr == null) return 0;
        for (int m = 0; m < MONTHS.length; m++) if (monthStr.equalsIgnoreCase(MONTHS[m])) return m + 1;
        return 0;
    }

    /** Returns Julian day.
   * @return Julian day
   */
    public int getJulianDay() {
        int nDays = 0;
        for (int m = 1; m < month; m++) nDays += daysInMonth(year, m);
        return nDays + day;
    }

    /** Calculetes number of days since 01/01/1970 up to the date (exclusive 01/01/1970)
  * It's valid since 01/01/0001
  * @return number of days between the epochtime and the date (<0 if date < 01/01/1970)
  */
    public int daysSinceETime() {
        final int YEAR0 = 1970;
        int nDays = (year - YEAR0) * 365 + ((year - 1) / 4 - (YEAR0 - 1) / 4) - ((year - 1) / 100 - (YEAR0 - 1) / 100) + ((year - 1) / 400 - (YEAR0 - 1) / 400);
        nDays += getJulianDay() - 1;
        return nDays;
    }

    /** Calculetes time in miliseconds since 01/01/1970, 00:00:00 GMT
  * It's valid since 01/01/0001
  * @return time in miliseconds since 01/01/1970, 00:00:00 GMT (<0 if date < 01/01/1970)
  */
    public long epochTime() {
        return daysSinceETime() * MILLISECONDS_PER_DAY;
    }

    /** Calculetes number of days between the dates (inclusive).
  * For example: Mar 1 to Mar 31 returns 31 days (not 30).
  * @param dateFrom start date
  * @param dateTo end date
  * @return number of days between the dates. This number is always positive:
  * min value = 1 (if dateFrom == dateTo)
  * @see WDCDay#daysUntil
  */
    public static int daysBetween(WDCDay dateFrom, WDCDay dateTo) {
        return Math.abs(dateTo.daysSinceETime() - dateFrom.daysSinceETime()) + 1;
    }

    /** Calculetes number of days between the dates (inclusive).
  * For example: Mar 1 to Mar 31 returns 31 days (not 30).
  * @param dateTo 'To date'
  * @return number of days between the dates.
  * positive if the dateFrom<=dateTo ; zero if the dateFrom==dateTo+1; negative otherwise.
  * @see WDCDay#daysBetween
  */
    public int daysUntil(WDCDay dateTo) {
        return dateTo.daysSinceETime() - this.daysSinceETime() + 1;
    }

    /** Compares the dates
  * @return true if the current date <= date and false otherwise
  */
    public boolean isLessEqual(WDCDay date) {
        return (this.getDayId() <= date.getDayId());
    }

    /** Compares the dates
  * @return true if the current date < date and false otherwise
  */
    public boolean isLess(WDCDay date) {
        return (this.getDayId() < date.getDayId());
    }

    /** Compares the dates
  * @return true if the current date >= date and false otherwise
  */
    public boolean isMoreEqual(WDCDay date) {
        return (this.getDayId() >= date.getDayId());
    }

    /** Compare the dates
  * @return true if the current date > date and false otherwise
  */
    public boolean isMore(WDCDay date) {
        return (this.getDayId() > date.getDayId());
    }

    /** Returns maximum of the dates.
  * @return maximum of the dates
  */
    public static WDCDay max(WDCDay date1, WDCDay date2) {
        return (date1.getDayId() >= date2.getDayId()) ? date1 : date2;
    }

    /** Returns minimum of the dates.
  * @return minimum of the dates
  */
    public static WDCDay min(WDCDay date1, WDCDay date2) {
        return (date1.getDayId() <= date2.getDayId()) ? date1 : date2;
    }

    /**
   * Return a list of dates in the date range. If dateFrom>dateTo return null
   * @param dateFrom start date
   * @param dateTo end date
   * @return array of dates between the dateFrom and dateTo (inclusive)
   * or null if dateFrom>dateTo
   */
    public static WDCDay[] getArrayOfDays(WDCDay dateFrom, WDCDay dateTo) {
        if (dateFrom.isMore(dateTo)) return null;
        WDCDay[] dates = new WDCDay[dateFrom.daysUntil(dateTo)];
        WDCDay date = new WDCDay(dateFrom);
        for (int k = 0; k < dates.length; k++) {
            dates[k] = new WDCDay(date);
            date.nextDay();
        }
        return dates;
    }

    /**
   * Return a WDCDay object for the Bartels' rotation number (must be >= 1).
   * @param brn The Bartel's rotation number (Jan 7, 1832 if equals 0)
   * @return WDCDay object
   */
    public static WDCDay brn2date(int brn) {
        if (brn <= 0) return null;
        int nDays = brn * 27 + 7;
        final int YEAR0 = 1832;
        int curYear = YEAR0 + nDays / 366;
        nDays -= (new WDCDay(YEAR0, 1, 1)).daysUntil(new WDCDay(curYear, 1, 1)) - 1;
        while (nDays - daysInYear(curYear) > 0) {
            nDays -= daysInYear(curYear);
            curYear++;
        }
        return new WDCDay(curYear, nDays);
    }

    /**
   * Return a WDCDay object for given Epoch time (valid since 01/01/400).
   * @param time The time in miliseconds since 01/01/1970, 00:00:00 GMT
   * @return WDCDay object
   */
    public static WDCDay etime2date(long time) {
        final int coef = 3652425;
        int nDays = (int) (time / MILLISECONDS_PER_DAY) + 1;
        int nDaysSince0 = WDCDay.daysBetween(new WDCDay(10101), new WDCDay(19691231)) + 366;
        int year = (int) ((nDays + nDaysSince0) * 10000L / coef);
        int jday = nDays - (new WDCDay(year, 1, 1)).daysSinceETime();
        if (jday <= 0) {
            year--;
            jday += WDCDay.daysInYear(year);
        }
        return new WDCDay(year, jday);
    }

    /**
   * Return a the Bartel's rotation number (0 for Jan 7, 1832) for the date (must be >= Jan 7, 1832).
   * @return the Bartel's rotation number or 0
   */
    public int getBrn() {
        int numDays = (new WDCDay(1832, 1, 7)).daysUntil(this);
        if (numDays <= 0) return 0;
        int brn = (numDays - 1) / 27;
        return brn;
    }

    /**
   * Convert the date to a formated String
   * @return Formatted date string as 'yyyy-mm-dd' or 'UNKNOWN' if date isn't valid
   * @see WDCDay#toNiceString
   */
    public String toString() {
        if (!isValid()) return "UNKNOWN";
        String str = "" + (100000000 + this.getDayId());
        return str.substring(1, 5) + "-" + str.substring(5, 7) + "-" + str.substring(7, 9);
    }

    /**
   * Convert the date to a nice formated String
   * @return Formatted date string as 'mmm dd, yyyy' or "UNKNOWN" if it is not valid
   * @see WDCDay#toString
   */
    public String toNiceString() {
        return (isValid()) ? (MONTHS[month - 1] + " " + day + ", " + year) : "UNKNOWN";
    }

    /**
   * Convert the date to a nice formated String
   * @return Formatted date string as 'mmm, yyyy' or "UNKNOWN" if it is not valid
   * @see WDCDay#toString
   */
    public String toNiceString2() {
        return (isValid()) ? (MONTHS[month - 1] + ", " + year) : "UNKNOWN";
    }

    /**
   * Display the date
   * @see WDCDay#println
   */
    public void print() {
        System.out.print(this.toString());
    }

    /**
   * Display the date
   * @see WDCDay#print
   */
    public void println() {
        System.out.println(this.toString());
    }

    /** Used to test this class
  */
    public static void main(String args[]) {
        System.out.println("Start");
        WDCDay d0 = new WDCDay(19010101);
        WDCDay d1 = new WDCDay(20000101);
        System.out.println("from: " + d0 + " to " + d1);
        while (!d0.equals(d1)) {
            if (!WDCDay.etime2date(d0.epochTime()).equals(d0)) {
                System.out.println("Error:");
                System.out.println(d0.daysSinceETime());
                System.out.println(d0);
                System.out.println(WDCDay.etime2date(d0.epochTime()).getDayId());
                return;
            }
            d0.nextDay();
        }
        System.out.println("Ok");
    }
}

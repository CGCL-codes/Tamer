package za.co.me23.chat;

import java.util.Calendar;

/**
 *
 * @author Jean-Pierre Jacobs
 */
public class Time {

    public Time() {
        Calendar c = Calendar.getInstance();
        year = String.valueOf(c.get(Calendar.YEAR)).substring(2);
        month = add0(c.get(Calendar.MONTH) + 1);
        day = add0(c.get(Calendar.DATE));
        hour = add0(c.get(Calendar.HOUR_OF_DAY));
        minute = add0(c.get(Calendar.MINUTE));
    }

    private static String add0(int iInt) {
        if (iInt < 10) return "0" + iInt;
        return String.valueOf(iInt);
    }

    public Time(String dbTime) {
        year = dbTime.substring(0, 2);
        month = dbTime.substring(2, 4);
        day = dbTime.substring(4, 6);
        hour = dbTime.substring(6, 8);
        minute = dbTime.substring(8, 10);
    }

    public String toString() {
        Time now = new Time();
        String returnString = hour + ":" + minute;
        if ((now.day.compareTo(day) > 0) & (now.month.compareTo(month) == 0) & (now.year.compareTo(year) == 0)) {
            return "(" + returnString + " " + day + "/" + month + ")";
        } else if ((now.month.compareTo(month) > 0) & (now.year.compareTo(year) == 0)) {
            return "(" + returnString + " " + day + "/" + month + ")";
        } else if (now.year.compareTo(year) > 0) {
            return "(" + returnString + " " + day + "/" + month + "/" + year + ")";
        }
        return "(" + returnString + ")";
    }

    public String toNumbers() {
        return year + month + day + hour + minute;
    }

    public String minute, hour, day, month, year;

    public static String[] monthArray = { "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec" };
}

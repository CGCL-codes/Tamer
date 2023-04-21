package lebah.planner;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.Hashtable;
import java.util.TimeZone;
import java.util.Vector;

/**
 * @author Shamsul Bahrin Abd Mutalib
 * @version 1.00
 */
public class CalendarMonth {

    private String dateString, monthYearText;

    private int day, month, year, dayOfWeek, maxDays, weeks;

    private CalendarMonth calendarMonthNow, calendarMonthBefore, calendarMonthAfter;

    private Hashtable calendarDays[][];

    private Vector errorMessages;

    public static void main(String[] args) {
        CalendarMonth t = new CalendarMonth();
        t.setCalendar("", 2006, 2);
    }

    /**
	 * @return Returns the dateString.
	 */
    public String getDateString() {
        return dateString;
    }

    /**
	 * @param dateString The dateString to set.
	 */
    public void setDateString(String dateString) {
        this.dateString = dateString;
    }

    /**
	 * @return Returns the day.
	 */
    public int getDay() {
        return day;
    }

    /**
	 * @param day The day to set.
	 */
    public void setDay(int day) {
        this.day = day;
    }

    /**
	 * @return Returns the dayOfWeek.
	 */
    public int getDayOfWeek() {
        return dayOfWeek;
    }

    /**
	 * @param dayOfWeek The dayOfWeek to set.
	 */
    public void setDayOfWeek(int dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    /**
	 * @return Returns the maxDays.
	 */
    public int getMaxDays() {
        return maxDays;
    }

    /**
	 * @param maxDays The maxDays to set.
	 */
    public void setMaxDays(int maxDays) {
        this.maxDays = maxDays;
    }

    /**
	 * @return Returns the month.
	 */
    public int getMonth() {
        return month;
    }

    /**
	 * @param month The month to set.
	 */
    public void setMonth(int month) {
        this.month = month;
    }

    /**
	 * @return Returns the year.
	 */
    public int getYear() {
        return year;
    }

    /**
	 * @param year The year to set.
	 */
    public void setYear(int year) {
        this.year = year;
    }

    /**
	 * @return Returns the calendarMonthAfter.
	 */
    public CalendarMonth getCalendarMonthAfter() {
        return calendarMonthAfter;
    }

    /**
	 * @param calendarMonthAfter The calendarMonthAfter to set.
	 */
    public void setCalendarMonthAfter(CalendarMonth calendarMonthAfter) {
        this.calendarMonthAfter = calendarMonthAfter;
    }

    /**
	 * @return Returns the calendarMonthBefore.
	 */
    public CalendarMonth getCalendarMonthBefore() {
        return calendarMonthBefore;
    }

    /**
	 * @param calendarMonthBefore The calendarMonthBefore to set.
	 */
    public void setCalendarMonthBefore(CalendarMonth calendarMonthBefore) {
        this.calendarMonthBefore = calendarMonthBefore;
    }

    public void setWeeks(int weeks) {
        this.weeks = weeks;
    }

    public int getWeeks() {
        return weeks;
    }

    public CalendarMonth getCalendarNow(int year, int month) {
        month = month - 1;
        Calendar calendar = new GregorianCalendar(year, month, 1);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int dayWeek = calendar.get(Calendar.DAY_OF_WEEK);
        int maxDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        int weeks = calendar.getActualMaximum(Calendar.WEEK_OF_MONTH);
        CalendarMonth cm = new CalendarMonth();
        cm.setMonthYearText(new SimpleDateFormat("MMM-yyyy").format(calendar.getTime()));
        cm.setDateString(new SimpleDateFormat("d-MMM-yyyy").format(calendar.getTime()));
        cm.setDay(day);
        cm.setMonth(month + 1);
        cm.setYear(year);
        cm.setDayOfWeek(dayWeek);
        cm.setMaxDays(maxDay);
        cm.setWeeks(weeks);
        return cm;
    }

    public void setCalendar() {
        setCalendar("", 0, 0);
    }

    public void setCalendar(String user, int year, int month) {
        errorMessages = new Vector();
        if (year == 0 || month == 0) {
            java.util.Calendar cal = new java.util.GregorianCalendar();
            year = year == 0 ? cal.get(java.util.Calendar.YEAR) : year;
            month = month == 0 ? cal.get(java.util.Calendar.MONTH) + 1 : month;
        }
        calendarMonthNow = getCalendarNow(year, month);
        calendarMonthBefore = getCalendarBefore(year, month);
        calendarMonthAfter = getCalendarAfter(year, month);
        int dayWeekNow = calendarMonthNow.getDayOfWeek();
        int maxDays = calendarMonthNow.getMaxDays();
        int lastDayPrev = calendarMonthBefore.getMaxDays();
        int weeks = calendarMonthNow.getWeeks();
        calendarDays = new Hashtable[weeks][7];
        int cntDay = 1;
        boolean now = false, before = false, after = false;
        if (dayWeekNow > 1) {
            cntDay = lastDayPrev - dayWeekNow + 2;
            before = true;
        } else {
            now = true;
        }
        int cyear = 0, cmonth = 0, cday = 0;
        String cmonthYearText = "";
        for (int week = 0; week < weeks; week++) {
            for (int day = 0; day < 7; day++) {
                if (before) {
                    cyear = calendarMonthBefore.getYear();
                    cmonth = calendarMonthBefore.getMonth();
                    cmonthYearText = calendarMonthBefore.getMonthYearText();
                } else if (now) {
                    cyear = calendarMonthNow.getYear();
                    cmonth = calendarMonthNow.getMonth();
                    cmonthYearText = calendarMonthNow.getMonthYearText();
                } else if (after) {
                    cyear = calendarMonthAfter.getYear();
                    cmonth = calendarMonthAfter.getMonth();
                    cmonthYearText = calendarMonthAfter.getMonthYearText();
                }
                calendarDays[week][day] = new Hashtable();
                calendarDays[week][day].put("day", new Integer(cntDay));
                calendarDays[week][day].put("month", new Integer(cmonth));
                calendarDays[week][day].put("year", new Integer(cyear));
                calendarDays[week][day].put("monthYearText", cmonthYearText);
                Collection eventList = null;
                try {
                    eventList = EventData.getEvent(user, cyear, cmonth, cntDay);
                    calendarDays[week][day].put("eventList", eventList);
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                    errorMessages.addElement("eventList: " + e.getMessage());
                }
                calendarDays[week][day].put("now", new Boolean(now));
                Vector taskList = null;
                try {
                    taskList = PlannerData.getInstance().getTaskVector(user, "", cyear, cmonth, cntDay);
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                    errorMessages.addElement("taskList: " + e.getMessage());
                }
                calendarDays[week][day].put("taskList", taskList);
                cntDay++;
                if (before && cntDay > lastDayPrev) {
                    cntDay = 1;
                    before = false;
                    now = true;
                }
                if (now && cntDay > maxDays) {
                    cntDay = 1;
                    after = true;
                    now = false;
                }
            }
        }
    }

    public CalendarMonth getCalendarBefore(int year, int month) {
        if (month == 1) {
            year = year - 1;
            month = 12;
        } else {
            month = month - 1;
        }
        return getCalendarNow(year, month);
    }

    public CalendarMonth getCalendarAfter(int year, int month) {
        if (month == 12) {
            year = year + 1;
            month = 1;
        } else {
            month = month + 1;
        }
        return getCalendarNow(year, month);
    }

    /**
	 * @return Returns the calendarMonthNow.
	 */
    public CalendarMonth getCalendarMonthNow() {
        return calendarMonthNow;
    }

    /**
	 * @param calendarMonthNow The calendarMonthNow to set.
	 */
    public void setCalendarMonthNow(CalendarMonth calendarMonthNow) {
        this.calendarMonthNow = calendarMonthNow;
    }

    /**
	 * @return Returns the calendarDays.
	 */
    public Hashtable[][] getCalendarDays() {
        return calendarDays;
    }

    /**
	 * @param calendarDays The calendarDays to set.
	 */
    public void setCalendarDays(Hashtable[][] calendarDays) {
        this.calendarDays = calendarDays;
    }

    /**
	 * @return Returns the monthYearText.
	 */
    public String getMonthYearText() {
        return monthYearText;
    }

    /**
	 * @param monthYearText The monthYearText to set.
	 */
    public void setMonthYearText(String monthYearText) {
        this.monthYearText = monthYearText;
    }

    public Vector getErrorMessages() {
        return errorMessages;
    }
}

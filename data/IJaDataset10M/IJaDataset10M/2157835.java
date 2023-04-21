package net.sf.jimex.project.util;

/**
 * andrew 19.02.2006 20:09:16
 */
public class ProjectCalendar {

    private int minutesPerDay;

    private int minutesPerWeek;

    private int daysPerMonth;

    public int getMinutesPerDay() {
        return minutesPerDay;
    }

    public void setMinutesPerDay(int minutesPerDay) {
        this.minutesPerDay = minutesPerDay;
    }

    public int getMinutesPerWeek() {
        return minutesPerWeek;
    }

    public void setMinutesPerWeek(int minutesPerWeek) {
        this.minutesPerWeek = minutesPerWeek;
    }

    public int getDaysPerMonth() {
        return daysPerMonth;
    }

    public void setDaysPerMonth(int daysPerMonth) {
        this.daysPerMonth = daysPerMonth;
    }
}

package com.myapp.tools.media.renamer.controller;

import java.util.Date;

/**
 * represents a time span between two timepoints.
 * 
 * @author andre
 * 
 */
public final class TimeSpan {

    private final int hours, minutes, seconds;

    private final long difference, days;

    /**
     * creates a timespan with the given span
     * 
     * @param days
     *            the days between the dates
     * @param hours
     *            the hours between the dates
     * @param minutes
     *            the minutes between the dates
     * @param seconds
     *            the seconds between the dates
     */
    public TimeSpan(int days, int hours, int minutes, int seconds) {
        this.days = days;
        this.hours = hours;
        this.minutes = minutes;
        this.seconds = seconds;
        difference = seconds + minutes * 60 + hours * 60 * 60 + days * 60 * 60 * 24;
    }

    /**
     * creates a new timespan with the given amount of seconds
     * 
     * @param seconds
     *            the seconds of the timespan
     */
    public TimeSpan(long seconds) {
        this.difference = seconds;
        days = seconds / (60 * 60 * 24);
        seconds = seconds - (days * 60 * 60 * 24);
        hours = (int) (seconds / (60 * 60));
        seconds = seconds - (hours * 60 * 60);
        minutes = (int) (seconds / 60);
        seconds = seconds - minutes * 60;
        this.seconds = (int) seconds;
    }

    /**
     * creates a new timespan with the amount of seconds between two dates.
     * 
     * @param c1
     *            a date
     * @param c2
     *            another date
     */
    public TimeSpan(Date c1, Date c2) {
        long diff = c1.getTime() - c2.getTime();
        diff = Math.abs(diff);
        TimeSpan foo = new TimeSpan(diff / 1000);
        days = foo.days;
        hours = foo.hours;
        minutes = foo.minutes;
        seconds = foo.seconds;
        difference = foo.difference;
    }

    @Override
    public String toString() {
        StringBuilder bui = new StringBuilder();
        if (days > 0) bui.append(days + " Tage, ");
        if (hours > 0) bui.append(hours + " Stunden, ");
        if (minutes > 0) bui.append(minutes + " Minuten, ");
        if (seconds > 0) bui.append(seconds + " Sekunden");
        if (bui.length() == 0) bui.append("kein Zeitunterschied");
        return bui.toString();
    }

    /**
     * returns the days contained in the timespan
     * 
     * @return the days contained in the timespan
     */
    long getDays() {
        return days;
    }

    /**
     * returns the rest of the hours contained in the timespan
     * 
     * @return the rest of the hours contained in the timespan
     */
    int getHours() {
        return hours;
    }

    /**
     * returns the rest of the minutes contained in the timespan
     * 
     * @return the rest of the minutes contained in the timespan
     */
    int getMinutes() {
        return minutes;
    }

    /**
     * returns the rest of the seconds contained in the timespan
     * 
     * @return the rest of the seconds contained in the timespan
     */
    int getSeconds() {
        return seconds;
    }

    /**
     * returns the seconds contained in the timespan
     * 
     * @return the seconds contained in the timespan
     */
    long getDifference() {
        return difference;
    }
}

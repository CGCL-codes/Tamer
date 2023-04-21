package com.protomatter.pas.cron;

import java.util.*;
import com.protomatter.syslog.Syslog;
import com.protomatter.pas.event.*;

/**
 *  A cron entry.  This class encapsulates a <tt>PASEvent</tt>,
 *  a list of event topics and a specification for when the
 *  given event should be delivered to the given topics.  The
 *  specification of when the event should be delivered is
 *  based on the UNIX cron facility.
 */
public class CronEntry {

    private Hashtable year = new Hashtable();

    private Hashtable month = new Hashtable();

    private Hashtable day = new Hashtable();

    private Hashtable weekday = new Hashtable();

    private Hashtable hour = new Hashtable();

    private Hashtable minute = new Hashtable();

    private Hashtable topics = new Hashtable();

    private PASEvent event = null;

    private Object value = new Object();

    private static String ALL_VALUE = "*";

    /**
   *  Create an empty CronEntry.
   */
    public CronEntry() {
        super();
    }

    /**
   *  Determines if this CronEntry applies to the given date.
   *  Seconds and milliseconds in the date are ignored.
   */
    public boolean appliesToDate(Date date) {
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(date);
        Integer theYear = new Integer(cal.get(Calendar.YEAR));
        if (!year.containsKey(ALL_VALUE) && !year.containsKey(theYear)) return false;
        Integer theMonth = new Integer(cal.get(Calendar.MONTH));
        if (!month.containsKey(ALL_VALUE) && !month.containsKey(theMonth)) return false;
        Integer theDay = new Integer(cal.get(Calendar.DAY_OF_MONTH));
        if (!day.containsKey(ALL_VALUE) && !day.containsKey(theDay)) return false;
        Integer theWeekDay = new Integer(cal.get(Calendar.DAY_OF_WEEK));
        if (!weekday.containsKey(ALL_VALUE) && !weekday.containsKey(theWeekDay)) return false;
        Integer theHour = new Integer(cal.get(Calendar.HOUR_OF_DAY));
        if (!hour.containsKey(ALL_VALUE) && !hour.containsKey(theHour)) return false;
        Integer theMinute = new Integer(cal.get(Calendar.MINUTE));
        if (!minute.containsKey(ALL_VALUE) && !minute.containsKey(theMinute)) return false;
        return true;
    }

    /**
   *  Add a year to the list of years this entry applies to.
   */
    public void addYear(int year) {
        this.year.put(new Integer(year), value);
    }

    /**
   *  Remove a year from the list of years this entry applies to.
   */
    public void removeYear(int year) {
        this.year.remove(new Integer(year));
    }

    /**
   *  Should the current year be taken into consideration when
   *  deciding if this entry is applicable?
   *  If this is set to false (the default) then the values set with
   *  the <tt>addYear()</tt> and <tt>removeYear()</tt> are taken
   *  into consideration.  If this is set to true then the current
   *  year is not taken into consideration.
   */
    public void setAllYears(boolean set) {
        if (set) this.year.put(ALL_VALUE, value); else this.year.remove(ALL_VALUE);
    }

    /**
   *  Add a month to the list of years this entry applies to.
   *  Month numbers are taken from the constants on the
   *  <tt>java.util.Calendar</tt> class.
   */
    public void addMonth(int month) {
        this.month.put(new Integer(month), value);
    }

    /**
   *  Remove a month from the list of years this entry applies to.
   *  Month numbers are taken from the constants on the
   *  <tt>java.util.Calendar</tt> class.
   */
    public void removeMonth(int month) {
        this.month.remove(new Integer(month));
    }

    /**
   *  Should the current month be taken into consideration when
   *  deciding if this entry is applicable?
   *  If this is set to false (the default) then the values set with
   *  the <tt>addMonth()</tt> and <tt>removeMonth()</tt> are taken
   *  into consideration.  If this is set to true then the current
   *  month is not taken into consideration.
   */
    public void setAllMonths(boolean set) {
        if (set) this.month.put(ALL_VALUE, value); else this.month.remove(ALL_VALUE);
    }

    /**
   *  Add a day of the month to the list of years this entry applies to.
   */
    public void addDay(int day) {
        this.day.put(new Integer(day), value);
    }

    /**
   *  Remove a day of the month from the list of years this entry applies to.
   */
    public void removeDay(int day) {
        this.day.remove(new Integer(day));
    }

    /**
   *  Should the current day of the month be taken into consideration when
   *  deciding if this entry is applicable?
   *  If this is set to false (the default) then the values set with
   *  the <tt>addDay()</tt> and <tt>removeDay()</tt> are taken
   *  into consideration.  If this is set to true then the current
   *  year is not taken into consideration.
   */
    public void setAllDays(boolean set) {
        if (set) this.day.put(ALL_VALUE, value); else this.day.remove(ALL_VALUE);
    }

    /**
   *  Add a weekday to the list of years this entry applies to.
   *  Weekday numbers are taken from the constants on the
   *  <tt>java.util.Calendar</tt> class.
   */
    public void addWeekday(int weekday) {
        this.weekday.put(new Integer(weekday), value);
    }

    /**
   *  Remove a weekday from the list of years this entry applies to.
   *  Weekday numbers are taken from the constants on the
   *  <tt>java.util.Calendar</tt> class.
   */
    public void removeWeekday(int weekday) {
        this.weekday.remove(new Integer(weekday));
    }

    /**
   *  Should the current weekday be taken into consideration when
   *  deciding if this entry is applicable?
   *  If this is set to false (the default) then the values set with
   *  the <tt>addWeekday()</tt> and <tt>removeWeekday()</tt> are taken
   *  into consideration.  If this is set to true then the current
   *  weekday is not taken into consideration.
   */
    public void setAllWeekdays(boolean set) {
        if (set) this.weekday.put(ALL_VALUE, value); else this.weekday.remove(ALL_VALUE);
    }

    /**
   *  Add an hour to the list of years this entry applies to.
   */
    public void addHour(int hour) {
        this.hour.put(new Integer(hour), value);
    }

    /**
   *  Remove an hour from the list of years this entry applies to.
   */
    public void removeHour(int hour) {
        this.hour.remove(new Integer(hour));
    }

    /**
   *  Should the current hour be taken into consideration when
   *  deciding if this entry is applicable?
   *  If this is set to false (the default) then the values set with
   *  the <tt>addHour()</tt> and <tt>removeHour()</tt> are taken
   *  into consideration.  If this is set to true then the current
   *  hour is not taken into consideration.
   */
    public void setAllHours(boolean set) {
        if (set) this.hour.put(ALL_VALUE, value); else this.hour.remove(ALL_VALUE);
    }

    /**
   *  Add a minute to the list of years this entry applies to.
   */
    public void addMinute(int minute) {
        this.minute.put(new Integer(minute), value);
    }

    /**
   *  Remove a minute from the list of years this entry applies to.
   */
    public void removeMinute(int minute) {
        this.minute.remove(new Integer(minute));
    }

    /**
   *  Should the current minute be taken into consideration when
   *  deciding if this entry is applicable?
   *  If this is set to false (the default) then the values set with
   *  the <tt>addMinute()</tt> and <tt>removeMinute()</tt> are taken
   *  into consideration.  If this is set to true then the current
   *  minute is not taken into consideration.
   */
    public void setAllMinutes(boolean set) {
        if (set) this.minute.put(ALL_VALUE, value); else this.minute.remove(ALL_VALUE);
    }

    /**
   *  Add a topic to the list of topics this entry will submit
   *  it's event to.
   */
    public void addTopic(String topic) {
        this.topics.put(topic, value);
    }

    /**
   *  Remove a topic from the list of topics this entry will submit
   *  it's event to.
   */
    public void removeTopic(String topic) {
        this.topics.remove(topic);
    }

    /**
   *  Get the list of topics this entry will submit it's
   *  event to.
   */
    public Enumeration getTopics() {
        return topics.keys();
    }

    /**
   *  Set this entry's event.
   */
    public void setEvent(PASEvent event) {
        this.event = event;
    }

    /**
   *  Get this entry's event.
   */
    public PASEvent getEvent() {
        return this.event;
    }
}

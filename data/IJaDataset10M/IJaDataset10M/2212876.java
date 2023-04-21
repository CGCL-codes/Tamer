package com.ariessoftpro.events.web;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import com.ariessoftpro.events.Event;

@SuppressWarnings("serial")
public class SaveEventCommand implements Serializable {

    private Calendar calendar;

    private Event event;

    private String id;

    private String title;

    private String date;

    private String day;

    private String month;

    private String year;

    private Integer[] days;

    private Integer[] months;

    private String[] monthNames = { "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec" };

    private Integer[] years;

    public SaveEventCommand() {
        calendar = Calendar.getInstance();
        event = new Event();
        initDays();
        initMonths();
        initYears();
    }

    public Event getEvent() {
        if (id == null) {
            return event;
        }
        event.setId((id.equals("") ? null : new Long(id)));
        event.setTitle(title);
        event.setDate(getTime());
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
        if (event != null && event.getId() != null) {
            id = event.getId().toString();
            title = event.getTitle();
            setTime(event.getDate());
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public Integer[] getDays() {
        return days;
    }

    public void setDays(Integer[] days) {
        this.days = days;
    }

    public Integer[] getMonths() {
        return months;
    }

    public void setMonths(Integer[] months) {
        this.months = months;
    }

    public String[] getMonthNames() {
        return monthNames;
    }

    public void setMonthNames(String[] monthNames) {
        this.monthNames = monthNames;
    }

    public Integer[] getYears() {
        return years;
    }

    public void setYears(Integer[] years) {
        this.years = years;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append(getClass().getName()).append("[");
        sb.append("event=").append(event);
        sb.append(",id=").append(id);
        sb.append(",title=").append(title);
        sb.append(",day=").append(day);
        sb.append(",month=").append(month);
        sb.append(",year=").append(year);
        sb.append("]");
        return sb.toString();
    }

    private Date getTime() {
        calendar.set(new Integer(year).intValue(), new Integer(month).intValue(), new Integer(day).intValue(), 0, 0, 0);
        return calendar.getTime();
    }

    private void setTime(Date date) {
        calendar.setTime(date);
        day = "" + calendar.get(Calendar.DAY_OF_MONTH);
        month = "" + calendar.get(Calendar.MONTH);
        year = "" + calendar.get(Calendar.YEAR);
    }

    private void initDays() {
        days = new Integer[31];
        for (int i = 0; i < 31; i++) {
            days[i] = i + 1;
        }
    }

    private void initMonths() {
        months = new Integer[12];
        for (int i = 0; i < 12; i++) {
            months[i] = i;
        }
    }

    private void initYears() {
        years = new Integer[10];
        calendar.setTime(new Date());
        int year = calendar.get(Calendar.YEAR);
        for (int i = 0; i < 10; i++, year++) {
            years[i] = year;
        }
    }
}

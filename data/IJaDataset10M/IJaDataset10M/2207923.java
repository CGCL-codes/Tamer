package net.sourceforge.recman.backend.manager.pojo;

import javax.xml.datatype.Duration;
import javax.xml.datatype.XMLGregorianCalendar;

/**
 * Timer
 * 
 * @author Marcus Kessel
 * 
 */
public class Timer implements RecmanObject {

    private int status;

    private String channel;

    private XMLGregorianCalendar day;

    private boolean multiple;

    private String days;

    private XMLGregorianCalendar start;

    private XMLGregorianCalendar stop;

    private Duration duration;

    private int priority;

    private int lifetime;

    private String title;

    private String shortText;

    /**
     * Constructor
     */
    public Timer() {
    }

    /**
     * @param status
     * @param channel
     * @param day
     * @param multiple
     * @param days
     * @param start
     * @param stop
     * @param duration
     * @param priority
     * @param lifetime
     * @param title
     * @param shortText
     */
    public Timer(int status, String channel, XMLGregorianCalendar day, boolean multiple, String days, XMLGregorianCalendar start, XMLGregorianCalendar stop, Duration duration, int priority, int lifetime, String title, String shortText) {
        super();
        this.status = status;
        this.channel = channel;
        this.day = day;
        this.multiple = multiple;
        this.days = days;
        this.start = start;
        this.stop = stop;
        this.duration = duration;
        this.priority = priority;
        this.lifetime = lifetime;
        this.title = title;
        this.shortText = shortText;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public XMLGregorianCalendar getDay() {
        return day;
    }

    public void setDay(XMLGregorianCalendar day) {
        this.day = day;
    }

    public boolean isMultiple() {
        return multiple;
    }

    public void setMultiple(boolean multiple) {
        this.multiple = multiple;
    }

    public String getDays() {
        return days;
    }

    public void setDays(String days) {
        this.days = days;
    }

    public XMLGregorianCalendar getStart() {
        return start;
    }

    public void setStart(XMLGregorianCalendar start) {
        this.start = start;
    }

    public XMLGregorianCalendar getStop() {
        return stop;
    }

    public void setStop(XMLGregorianCalendar stop) {
        this.stop = stop;
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public int getLifetime() {
        return lifetime;
    }

    public void setLifetime(int lifetime) {
        this.lifetime = lifetime;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getShortText() {
        return shortText;
    }

    public void setShortText(String shortText) {
        this.shortText = shortText;
    }

    public String toString() {
        String delimiter = "\n";
        StringBuffer sb = new StringBuffer();
        sb.append("Status: " + status + delimiter);
        sb.append("Channel: " + channel + delimiter);
        sb.append("Day: " + day + delimiter);
        sb.append("Start: " + start + delimiter);
        sb.append("Stop: " + stop + delimiter);
        sb.append("Priority: " + priority + delimiter);
        sb.append("Lifetime: " + lifetime + delimiter);
        sb.append("Title: " + title + delimiter);
        sb.append("Short text: " + shortText + delimiter);
        return sb.toString();
    }
}

package net.sourceforge.eclipsetrader.core.db;

import java.util.Calendar;
import java.util.Date;

public class Event extends PersistentObject {

    private Date date;

    private Security security;

    private String message = "";

    private String longMessage = "";

    public Event() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MILLISECOND, 0);
        date = calendar.getTime();
    }

    public Event(Integer id) {
        super(id);
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MILLISECOND, 0);
        date = calendar.getTime();
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Security getSecurity() {
        return security;
    }

    public void setSecurity(Security security) {
        this.security = security;
    }

    public String getLongMessage() {
        return longMessage;
    }

    public void setLongMessage(String longMessage) {
        this.longMessage = longMessage;
    }
}

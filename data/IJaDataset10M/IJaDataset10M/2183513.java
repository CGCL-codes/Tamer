package com.ariessoftpro.events;

import java.io.Serializable;
import java.util.Date;

@SuppressWarnings("serial")
public class Event implements Serializable {

    private Long id;

    private String title;

    private Date date;

    public Event() {
    }

    public Event(Long id) {
        this.id = id;
    }

    public Event(Long id, String title, Date date) {
        this.id = id;
        this.title = title;
        this.date = date;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append(getClass().getName()).append("[");
        sb.append("id=").append(id);
        sb.append(",title=").append(title);
        sb.append(",date=").append(date);
        sb.append("]");
        return sb.toString();
    }
}

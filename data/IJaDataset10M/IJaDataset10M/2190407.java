package com.friendfeed.api;

import java.util.Date;

public class Comment implements java.io.Serializable {

    private Date date;

    private String id;

    private From from;

    private String body;

    private Via via;

    private boolean clipped = false;

    public Comment() {
    }

    public Date getDate() {
        return date;
    }

    public String getId() {
        return id;
    }

    public String getBody() {
        return body;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public From getFrom() {
        return from;
    }

    public void setFrom(From f) {
        this.from = f;
    }

    public Via getVia() {
        return via;
    }

    public void setVia(Via v) {
        this.via = v;
    }

    public boolean isClipped() {
        return clipped;
    }

    public void setClipped(boolean clipped) {
        this.clipped = clipped;
    }

    public String toString() {
        return "Comment{" + "date=" + date + ", id='" + id + '\'' + ", body='" + body + '\'' + '}';
    }
}

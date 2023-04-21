package com.mucas;

public class Course {

    String courseTitle = null;

    private String desc = "";

    private String type = "";

    private int level = 0;

    private int duration = 0;

    private boolean hours = false;

    private String url = "";

    public Course() {
        this.courseTitle = null;
        this.desc = "";
        this.type = "";
        this.level = 0;
        this.duration = 0;
        this.hours = false;
        this.url = "";
    }

    public void setTitle(String text) {
        this.courseTitle = text;
    }

    public void setDesc(String text) {
        this.desc = text;
    }

    public void setDuration(String text) {
        this.duration = Integer.parseInt(text);
    }

    public void setMode(String text) {
        if (text.compareTo("Full Time") == 0) this.hours = false; else this.hours = true;
    }

    public void setURL(String text) {
        this.url = text;
    }

    public void setType(String text) {
        this.type = text;
    }

    public String getTitle() {
        return this.courseTitle;
    }

    public String getDesc() {
        return this.desc;
    }

    public String getType() {
        return this.type;
    }

    public int getLevel() {
        return this.level;
    }

    public int getDuration() {
        return this.duration;
    }

    public boolean getHours() {
        return this.hours;
    }

    public String toString() {
        return this.courseTitle;
    }

    public String getURL() {
        return this.url;
    }

    public String getDetails() {
        String out = "";
        out += this.getTitle() + "\n\n";
        out += "Description: " + this.getDesc() + "\n\n";
        out += "Type: " + this.getType() + "\n\n";
        out += ((!this.getHours()) ? "Full Time" : "Part Time") + "\n\n";
        out += "More: " + this.getURL() + "\n\n";
        return out;
    }
}

package com.TzTwork.awa.antworkagent.po;

public class Userinfo {

    private int userid;

    private String username;

    private String password;

    private int state;

    private String note;

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Userinfo(int userid, String username, String password, int state, String note) {
        super();
        this.userid = userid;
        this.username = username;
        this.password = password;
        this.state = state;
        this.note = note;
    }

    public Userinfo(String username, String password, int state) {
        super();
        this.username = username;
        this.password = password;
        this.state = state;
    }

    public Userinfo() {
        super();
    }
}

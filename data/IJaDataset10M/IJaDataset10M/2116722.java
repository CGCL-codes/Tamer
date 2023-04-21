package com.william.lifetraxer.utils;

public class User {

    String username = null;

    int userid = 0;

    int newTrackid = 0;

    String photoPath = null;

    public User(String username, int userid, int newTrackid, String photoPath) {
        this.username = username;
        this.userid = userid;
        this.newTrackid = newTrackid;
        this.photoPath = photoPath;
    }
}

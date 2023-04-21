package com.diipo.weibo.entity;

import java.io.Serializable;

public class TargetUser implements Serializable {

    private String username;

    private String full_name;

    private String profile_picture;

    private String id;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String fullName) {
        full_name = fullName;
    }

    public String getProfile_picture() {
        return profile_picture;
    }

    public void setProfile_picture(String profilePicture) {
        profile_picture = profilePicture;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}

package com.javaclimber.web20fundamentals.meetup.client;

import java.io.Serializable;

public class Profile implements Serializable {

    private String image;

    private String bio;

    private String groups;

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getGroups() {
        return groups;
    }

    public void setGroups(String groups) {
        this.groups = groups;
    }
}

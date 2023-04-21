package cn._2dland.uploader;

import java.io.Serializable;

@SuppressWarnings("serial")
public class UserPassPair implements Serializable {

    private String username;

    private String password;

    public UserPassPair(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }
}

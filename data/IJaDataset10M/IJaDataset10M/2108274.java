package com.google.code.restui.core;

/**
 *
 * @author Rohit
 */
public class AuthDetails {

    private boolean valueEntered;

    private String username;

    private String password;

    public AuthDetails(boolean valueEntered, String username, String password) {
        this.valueEntered = valueEntered;
        this.username = username;
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public boolean isValueEntered() {
        return valueEntered;
    }

    public void setValueEntered(boolean valueEntered) {
        this.valueEntered = valueEntered;
    }
}

package com.express.service.dto;

import java.io.Serializable;

public class LoginRequest implements Serializable {

    private static final long serialVersionUID = -1979102374351752949L;

    private String username;

    private String password;

    private boolean passwordReminderRequest;

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

    public boolean isPasswordReminderRequest() {
        return passwordReminderRequest;
    }

    public void setPasswordReminderRequest(boolean passwordReminderRequest) {
        this.passwordReminderRequest = passwordReminderRequest;
    }
}

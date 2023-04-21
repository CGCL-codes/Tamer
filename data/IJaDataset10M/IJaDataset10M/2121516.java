package com.ubx1.pdpscanner.shared.exceptions;

import java.io.Serializable;

/**
 * This exception is thrown if an incorrect username or password is detected
 * when a user tries to log in
 * 
 * @author wbraik
 * 
 */
public class BadLoginException extends Exception implements Serializable {

    private static final long serialVersionUID = 1L;

    private String username;

    private String password;

    public BadLoginException() {
    }

    public BadLoginException(String username, String password, String message) {
        super(message);
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}

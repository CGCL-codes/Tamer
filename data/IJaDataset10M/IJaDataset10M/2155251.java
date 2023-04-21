package com.cci.bmc.domain;

import java.util.Calendar;

/**
 * 
 * @author bjreath
 *
 */
public class LogEntry {

    private Long id;

    private String username;

    private Account account;

    private Calendar time;

    private String message;

    public LogEntry() {
    }

    public LogEntry(String message) {
        setMessage(message);
    }

    public LogEntry(String username, String message) {
        this(message);
        setUsername(username);
    }

    public LogEntry(String username, Account account, String message) {
        this(username, message);
        setAccount(account);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Calendar getTime() {
        return time;
    }

    public void setTime(Calendar time) {
        this.time = time;
    }
}

package org.timoconsult.timetracker.client;

import com.google.gwt.user.client.rpc.*;
import java.util.*;

public class TimeEntryRPC implements IsSerializable {

    public void setId(int i) {
        id = i;
    }

    public void setDescription(String d) {
        description = d;
    }

    public void setDate(Date d) {
        date = d;
    }

    public void setDuraction(double d) {
        duration = d;
    }

    public void setBillable(boolean b) {
        billable = b;
    }

    public void setProject(int p) {
        project = p;
    }

    public void setUser(int u) {
        user = u;
    }

    public int id() {
        return id;
    }

    public String description() {
        return description;
    }

    public Date date() {
        return date;
    }

    public double duration() {
        return duration;
    }

    public boolean billable() {
        return billable;
    }

    public int project() {
        return project;
    }

    public int user() {
        return user;
    }

    private int id;

    private String description;

    private Date date;

    private double duration;

    private boolean billable;

    private int project;

    private int user;
}

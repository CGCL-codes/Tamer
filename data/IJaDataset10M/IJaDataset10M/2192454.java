package com.mjs_svc.possibility.models;

import java.util.Date;

/**
 *
 * @author Matthew Scott
 * @version $Id: TimeClockEvent.java 19 2010-04-07 08:50:47Z matthew.joseph.scott $
 */
public class TimeClockEvent {

    private Long id;

    private Employee user;

    private Date ctime;

    private boolean clockin;

    public boolean isClockin() {
        return clockin;
    }

    public void setClockin(boolean clockin) {
        this.clockin = clockin;
    }

    public Date getCtime() {
        return ctime;
    }

    public void setCtime(Date ctime) {
        this.ctime = ctime;
    }

    public Long getId() {
        return id;
    }

    private void setId(Long id) {
        this.id = id;
    }

    public Employee getUser() {
        return user;
    }

    public void setUser(Employee user) {
        this.user = user;
    }
}

package com.itbs.aimcer.bean;

/**
 * Maintains a simple set of statuses.
 * Updates contact of any change.
 * Keeping this class separate allows for cleaneleness in implementation.
 *
 * @author Alex Rass
 * @since Feb 11, 2006
 */
public class StatusImpl implements Status {

    Contact parent;

    boolean online;

    boolean away;

    boolean wireless;

    int idleTime;

    public StatusImpl(Contact parent) {
        this.parent = parent;
    }

    public Contact getParent() {
        return parent;
    }

    public boolean isOnline() {
        return online;
    }

    public void setOnline(boolean online) {
        this.online = online;
        if (!online) {
            this.away = false;
            this.idleTime = 0;
        }
        parent.statusChanged();
    }

    public boolean isAway() {
        return away;
    }

    public void setAway(boolean away) {
        this.away = away;
        parent.statusChanged();
    }

    public boolean isWireless() {
        return wireless;
    }

    public void setWireless(boolean wireless) {
        this.wireless = wireless;
        parent.statusChanged();
    }

    public int getIdleTime() {
        return idleTime;
    }

    public void setIdleTime(int idleTime) {
        this.idleTime = idleTime;
        parent.statusChanged();
    }

    /**
     * Deel copy of the object, but shallow copy of the parent.
     * @return copy.
     */
    public Object clone() {
        StatusImpl newStatus = new StatusImpl(parent);
        newStatus.online = online;
        newStatus.away = away;
        newStatus.wireless = wireless;
        newStatus.idleTime = idleTime;
        return newStatus;
    }
}

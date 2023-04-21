package com.aionemu.loginserver.model;

import java.sql.Timestamp;

/**
 * Class for storing account time data (last login time,
 * last session duration time, accumulated online time today,
 * accumulated rest time today)
 *
 * @author EvilSpirit
 */
public class AccountTime {

    private Timestamp lastLoginTime;

    private Timestamp expirationTime;

    private Timestamp penaltyEnd;

    private long sessionDuration;

    private long accumulatedOnlineTime;

    private long accumulatedRestTime;

    /**
	 * Default constructor. Set the lastLoginTime to current time
	 */
    public AccountTime() {
        this.lastLoginTime = new Timestamp(System.currentTimeMillis());
    }

    public Timestamp getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(Timestamp lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public long getSessionDuration() {
        return sessionDuration;
    }

    public void setSessionDuration(long sessionDuration) {
        this.sessionDuration = sessionDuration;
    }

    public long getAccumulatedOnlineTime() {
        return accumulatedOnlineTime;
    }

    public void setAccumulatedOnlineTime(long accumulatedOnlineTime) {
        this.accumulatedOnlineTime = accumulatedOnlineTime;
    }

    public long getAccumulatedRestTime() {
        return accumulatedRestTime;
    }

    public void setAccumulatedRestTime(long accumulatedRestTime) {
        this.accumulatedRestTime = accumulatedRestTime;
    }

    public Timestamp getExpirationTime() {
        return expirationTime;
    }

    public void setExpirationTime(Timestamp expirationTime) {
        this.expirationTime = expirationTime;
    }

    public Timestamp getPenaltyEnd() {
        return penaltyEnd;
    }

    public void setPenaltyEnd(Timestamp penaltyEnd) {
        this.penaltyEnd = penaltyEnd;
    }
}

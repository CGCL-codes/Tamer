package com.sourceforge.oraclewicket.app.report.sessionlock;

import java.io.Serializable;

public class ReportRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    private int sessionId;

    private String username;

    private String lockType;

    private String modeHeld;

    private String modeRequested;

    private String lockId1;

    private String lockId2;

    private String currSql;

    private String prevSql;

    public ReportRecord(final int pSessionId, final String pUsername, final String pLockType, final String pModeHeld, final String pModeRequested, final String pLockId1, final String pLockId2, final String pCurrSql, final String pPrevSql) {
        sessionId = pSessionId;
        username = pUsername;
        lockType = pLockType;
        modeHeld = pModeHeld;
        modeRequested = pModeRequested;
        lockId1 = pLockId1;
        lockId2 = pLockId2;
        currSql = pCurrSql;
        prevSql = pPrevSql;
    }

    public final int getSessionId() {
        return sessionId;
    }

    public final void setSessionId(final int pSessionId) {
        sessionId = pSessionId;
    }

    public final String getLockType() {
        return lockType;
    }

    public final void setLockType(final String pLockType) {
        lockType = pLockType;
    }

    public final String getModeHeld() {
        return modeHeld;
    }

    public final void setModeHeld(final String pModeHeld) {
        modeHeld = pModeHeld;
    }

    public final String getModeRequested() {
        return modeRequested;
    }

    public final void setModeRequested(final String pModeRequested) {
        modeRequested = pModeRequested;
    }

    public final String getUsername() {
        return username;
    }

    public final void setUsername(final String pUsername) {
        username = pUsername;
    }

    public final String getLockId1() {
        return lockId1;
    }

    public final void setLockId1(final String pLockId1) {
        lockId1 = pLockId1;
    }

    public final String getLockId2() {
        return lockId2;
    }

    public final void setLockId2(final String pLockId2) {
        lockId2 = pLockId2;
    }

    public final String getCurrSql() {
        return currSql;
    }

    public final void setCurrSql(final String pCurrSql) {
        currSql = pCurrSql;
    }

    public final String getPrevSql() {
        return prevSql;
    }

    public final void setPrevSql(final String pPrevSql) {
        prevSql = pPrevSql;
    }
}

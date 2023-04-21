package com.sourceforge.oraclewicket.app.report.currentuseractivity;

import java.io.Serializable;
import java.util.Date;

final class ReportRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    private String username;

    private int sessionId;

    private int sessionSerialNum;

    private String clientInfo;

    private String clientIdentifier;

    private Date logonTime;

    private String sessionStatus;

    private String program;

    private String module;

    private String action;

    private String traceFileName;

    public ReportRecord(final String pUsername, final int pSessionId, final int pSessionSerialNum, final String pClientInfo, final String pClientIdentifier, final String pSessionStatus, final String pProgram, final String pModule, final String pAction, final String pTraceFileName) {
        setUsername(pUsername);
        setSessionId(pSessionId);
        setSessionSerialNum(pSessionSerialNum);
        setClientInfo(pClientInfo);
        setClientIdentifier(pClientIdentifier);
        setSessionStatus(pSessionStatus);
        setProgram(pProgram);
        setModule(pModule);
        setAction(pAction);
        setTraceFileName(pTraceFileName);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(final String pUsername) {
        username = pUsername;
    }

    public int getSessionId() {
        return sessionId;
    }

    public void setSessionId(final int pSessionId) {
        sessionId = pSessionId;
    }

    public int getSessionSerialNum() {
        return sessionSerialNum;
    }

    public void setSessionSerialNum(final int pSessionSerialNum) {
        sessionSerialNum = pSessionSerialNum;
    }

    public String getClientInfo() {
        return clientInfo;
    }

    public void setClientInfo(final String pClientInfo) {
        clientInfo = pClientInfo;
    }

    public String getClientIdentifier() {
        return clientIdentifier;
    }

    public void setClientIdentifier(final String pClientIdentifier) {
        clientIdentifier = pClientIdentifier;
    }

    public Date getLogonTime() {
        return logonTime;
    }

    public void setLogonTime(final Date pLogonTime) {
        logonTime = pLogonTime;
    }

    public String getSessionStatus() {
        return sessionStatus;
    }

    public void setSessionStatus(final String pSessionStatus) {
        sessionStatus = pSessionStatus;
    }

    public String getProgram() {
        return program;
    }

    public void setProgram(final String pProgram) {
        program = pProgram;
    }

    public String getModule() {
        return module;
    }

    public void setModule(final String pModule) {
        module = pModule;
    }

    public String getAction() {
        return action;
    }

    public void setAction(final String pAction) {
        action = pAction;
    }

    public String getTraceFileName() {
        return traceFileName;
    }

    public void setTraceFileName(final String pTraceFileName) {
        traceFileName = pTraceFileName;
    }
}

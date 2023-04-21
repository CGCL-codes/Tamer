package net.jsrb.runtime.impl.request;

public class GatewayRequestInfo {

    private int sessionId;

    private int msgsn;

    public GatewayRequestInfo() {
    }

    public int getMsgsn() {
        return msgsn;
    }

    public void setMsgsn(int msgsn) {
        this.msgsn = msgsn;
    }

    public int getSessionId() {
        return sessionId;
    }

    public void setSessionId(int sessionId) {
        this.sessionId = sessionId;
    }
}

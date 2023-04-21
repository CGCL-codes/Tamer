package com.google.code.openperfmon.service;

public class HostTestingResult {

    private boolean resolved;

    private boolean reached;

    private long reachTime;

    public boolean isResolved() {
        return resolved;
    }

    public void setResolved(boolean resolved) {
        this.resolved = resolved;
    }

    public boolean isReached() {
        return reached;
    }

    public void setReached(boolean reached) {
        this.reached = reached;
    }

    public long getReachTime() {
        return reachTime;
    }

    public void setReachTime(long reachTime) {
        this.reachTime = reachTime;
    }
}

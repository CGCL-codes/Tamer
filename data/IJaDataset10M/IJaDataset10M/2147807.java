package org.shake.lastfm.event;

public class SessionStartFailedEvent {

    private final String reason;

    public SessionStartFailedEvent(String reason) {
        this.reason = reason;
    }

    public String getReason() {
        return reason;
    }
}

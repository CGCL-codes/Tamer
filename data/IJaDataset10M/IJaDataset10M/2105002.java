package com.sts.webmeet.common;

public class MeetingLockedStatusMessage extends WebmeetMessage {

    private boolean bLocked;

    public MeetingLockedStatusMessage(boolean bLocked) {
        this.bLocked = bLocked;
    }

    public boolean isLocked() {
        return this.bLocked;
    }
}

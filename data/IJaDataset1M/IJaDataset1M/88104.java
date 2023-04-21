package com.googlecode.jamr.plug;

public class JamrDataConfig {

    private String accessCode;

    private int itemsInQueue;

    private int minutesBetween;

    public String getAccessCode() {
        return accessCode;
    }

    public void setAccessCode(String ac) {
        this.accessCode = ac;
    }

    public int getItemsInQueue() {
        return itemsInQueue;
    }

    public void setItemsInQueue(int i) {
        this.itemsInQueue = i;
    }

    public int getMinutesBetween() {
        return minutesBetween;
    }

    public void setMinutesBetween(int mb) {
        this.minutesBetween = mb;
    }
}

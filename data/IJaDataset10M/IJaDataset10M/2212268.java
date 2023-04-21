package org.t2framework.oneclick.model;

public class StationInfo {

    String lastTime;

    String fare;

    String takeTime;

    String requestUrlString;

    public StationInfo() {
    }

    public String getLastTime() {
        return lastTime;
    }

    public void setLastTime(String lastTime) {
        this.lastTime = lastTime;
    }

    public String getFare() {
        return fare;
    }

    public void setFare(String fare) {
        this.fare = fare;
    }

    public String getTakeTime() {
        return takeTime;
    }

    public void setTakeTime(String takeTime) {
        this.takeTime = takeTime;
    }

    public void setRequestUrlString(String format) {
        this.requestUrlString = format;
    }

    public String getRequestUrlString() {
        return requestUrlString;
    }
}

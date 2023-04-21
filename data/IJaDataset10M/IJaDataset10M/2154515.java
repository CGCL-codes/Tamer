package org.bissa.latencyApp;

public class Clock {

    private long startTime = 0;

    private long endTime = 0;

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        System.out.println("start time : " + startTime);
        if (this.startTime > 0) {
            throw new RuntimeException("you cant set the clocks start time twice....");
        } else {
            this.startTime = startTime;
        }
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        System.out.println("end time : " + endTime);
        if (this.endTime > endTime) {
        } else {
            this.endTime = endTime;
        }
    }

    public long getElapsedTime() {
        return (endTime - startTime);
    }
}

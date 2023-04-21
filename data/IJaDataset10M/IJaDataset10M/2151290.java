package com.myapp.util.timedate;

import java.util.Date;

/**
 *
 * @author andre
 */
public class StopWatch {

    public static final long NOT_SET = Long.MIN_VALUE;

    private long startTime = TimeDateUtil.now();

    private long stopTime = NOT_SET;

    private boolean isRunning = false;

    public void start() {
        startTime = TimeDateUtil.now();
        stopTime = NOT_SET;
        isRunning = true;
    }

    public boolean isStopped() {
        return stopTime != NOT_SET;
    }

    public boolean isRunning() {
        return isRunning;
    }

    public void stop() {
        stopTime = TimeDateUtil.now();
        isRunning = false;
    }

    public long duration() {
        if (stopTime == NOT_SET) throw new IllegalStateException("watch was not stopped yet!");
        return stopTime - startTime;
    }

    public long currentSplit() {
        return TimeDateUtil.now() - startTime;
    }

    public String startString() {
        return TimeDateUtil.formatTime(startTime);
    }

    public String stopString() {
        if (stopTime == NOT_SET) throw new IllegalStateException("watch was not stopped yet!");
        return TimeDateUtil.formatTime(stopTime);
    }

    public String currentSplitString() {
        return TimeDateUtil.formatTime(currentSplit());
    }

    public String durationString() {
        return TimeDateUtil.formatTime(duration());
    }

    public double durationSeconds() {
        return TimeDateUtil.secondsDouble(duration());
    }

    public double splitSeconds() {
        return TimeDateUtil.secondsDouble(currentSplit());
    }

    public Date getStartDate() {
        return new Date(startTime);
    }

    public Date getStopDate() {
        if (stopTime == NOT_SET) throw new IllegalStateException("watch was not stopped yet!");
        return new Date(stopTime);
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getStopTime() {
        return stopTime;
    }

    public void setStopTime(long stopTime) {
        this.stopTime = stopTime;
    }
}

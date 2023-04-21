package com.rise.rois.server;

import java.lang.Thread.State;
import java.util.Date;
import com.rise.rois.server.model.Session;

public class SessionTimerWrapper {

    private SessionTimer timer = null;

    public SessionTimerWrapper(long duration, Session session) {
        super();
        timer = new SessionTimer(duration, session);
        ThreadQueueManager.addTimer(this);
    }

    public void start() {
        timer.start();
    }

    public State getState() {
        return timer.getState();
    }

    public void interrupt() {
        timer.requestStop();
        ThreadQueueManager.removeTimer(getKey());
    }

    public void addTime(long timeToAdd) {
        timer.addTime(timeToAdd);
    }

    public void run() {
        timer.run();
    }

    public long getDuration() {
        return timer.getDuration();
    }

    public long getActualDuration() {
        return timer.getActualDuration();
    }

    public String getKey() {
        return timer.toString();
    }

    public Date getStartDate() {
        return timer.getStartDate();
    }

    public Date getEndDate() {
        return timer.getEndDate();
    }
}

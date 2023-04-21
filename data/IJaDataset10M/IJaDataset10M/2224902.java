package com.rhythm.commons.ejb.timer;

/**
 *
 * @author Michael J. Lee
 */
public interface JobTimer {

    String start(long interval, Jobs jobs);

    void cancel(String name);
}

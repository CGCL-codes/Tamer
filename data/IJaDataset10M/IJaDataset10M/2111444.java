package net.sourceforge.xconf.toolbox;

import java.util.Date;

/**
 * Provides the actual system date and time.
 * 
 * @author Tom Czarniecki
 */
public final class SystemClock implements Clock {

    private static SystemClock instance;

    private SystemClock() {
    }

    public static SystemClock getInstance() {
        if (instance == null) {
            instance = new SystemClock();
        }
        return instance;
    }

    public long getCurrentTimeMillis() {
        return System.currentTimeMillis();
    }

    public Date getCurrentDate() {
        return new Date();
    }
}

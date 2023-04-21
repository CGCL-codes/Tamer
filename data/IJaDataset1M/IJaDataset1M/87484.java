package org.jmove.core.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Simple stop watch to measure durations in seconds.
 *
 * @author Michael Juergens
 */
public class StopWatch {

    private static SimpleDateFormat theTimeFormat = new SimpleDateFormat("HH:mm:ss");

    private Calendar myStartTime = null;

    private Calendar myStopTime = null;

    public StopWatch() {
        start();
    }

    public void start() {
        myStartTime = GregorianCalendar.getInstance();
    }

    public void stop() {
        myStopTime = GregorianCalendar.getInstance();
    }

    public void logStart(String anActionDescription) {
        Log.info(this, "Starting " + anActionDescription + " at " + theTimeFormat.format(myStartTime.getTime()) + "...");
    }

    public void logStop(String anActionDescription) {
        if (myStopTime == null) {
            stop();
        }
        long duration = myStopTime.getTimeInMillis() - myStartTime.getTimeInMillis();
        Log.info(this, "Finished " + anActionDescription + " at " + theTimeFormat.format(myStopTime.getTime()) + ", duration is " + duration / 1000 + " s.");
    }
}

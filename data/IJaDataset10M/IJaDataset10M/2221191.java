package org.epistasis;

import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * Used for timing things, just like a real stopwatch!
 */
public class StopWatch {

    private long start;

    /**
	 * Calls start.
	 */
    public StopWatch() {
        start();
    }

    /**
	 * Get's the total elapsed time in the format H:mm:ss.SSS
	 */
    public String getElapsedTime() {
        final int MILLIS = 1;
        final int SECONDS = 1000 * MILLIS;
        final int MINUTES = 60 * SECONDS;
        final int HOURS = 60 * MINUTES;
        final long time = System.currentTimeMillis() - start;
        final long h = time / HOURS;
        final long m = (time - h * HOURS) / MINUTES;
        final long s = (time - h * HOURS - m * MINUTES) / SECONDS;
        final long S = (time - h * HOURS - m * MINUTES - s * SECONDS);
        final NumberFormat fmt_h = new DecimalFormat("0");
        final NumberFormat fmt_minsec = new DecimalFormat("00");
        final NumberFormat fmt_S = new DecimalFormat("000");
        return fmt_h.format(h) + ":" + fmt_minsec.format(m) + ":" + fmt_minsec.format(s) + "." + fmt_S.format(S);
    }

    /**
	 * Gets the total elapsed time in milliseconds.
	 */
    public long getElapsedTimeMillis() {
        return System.currentTimeMillis() - start;
    }

    /**
	 * Just calls start, but I like writing reset better.
	 */
    public void reset() {
        start();
    }

    /**
	 * Starts and resets the StopWatch.
	 */
    public void start() {
        start = System.currentTimeMillis();
    }
}

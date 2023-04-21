package eu.easyedu.jnetwalk.utils;

import java.util.logging.Logger;

/**
 *
 * @author hlavki
 */
public final class ExecuteTimer {

    private static final Logger log = Logger.getLogger(ExecuteTimer.class.getName());

    private long startTime;

    private long stopTime;

    private long lastSnapshot;

    private boolean running;

    private boolean usedOnce;

    private String name;

    public ExecuteTimer(String name) {
        this.name = name;
    }

    public ExecuteTimer() {
        this.name = Integer.toString(System.identityHashCode(this));
    }

    /**
     * Start the stopwatch.
     *
     * @throws IllegalStateException if the stopwatch is already running.
     */
    public void start() {
        if (running) {
            throw new IllegalStateException("Must stop before calling start again.");
        }
        startTime = System.currentTimeMillis();
        lastSnapshot = startTime;
        stopTime = 0;
        running = true;
        usedOnce = true;
        log.fine("Timer[" + name + "] started ");
    }

    /**
     * Stop the stopwatch.
     *
     * @throws IllegalStateException if the stopwatch is not already running.
     */
    public void stop() {
        if (!running) {
            throw new IllegalStateException("Cannot stop if not currently running.");
        }
        stopTime = System.currentTimeMillis();
        running = false;
    }

    public long snapshot() {
        if (running) {
            long tmp = System.currentTimeMillis();
            long result = tmp - lastSnapshot;
            lastSnapshot = tmp;
            return result;
        } else {
            throw new IllegalStateException("Cannot create snapshot when timer is not running.");
        }
    }

    public String snapshotToString(String actionName) {
        return "Timer[" + this.name + "] on [" + actionName + "] " + snapshot() + " ms";
    }

    public void logSnapshot(String actionName, String uuid) {
        log.info((uuid != null ? uuid : "") + " SNAPSHOT: " + snapshotToString(actionName));
    }

    /**
     * Express the "reading" on the stopwatch.
     *
     * @throws IllegalStateException if the Stopwatch has never been used,
     * or if the stopwatch is still running.
     */
    public String toString() {
        validateIsReadable();
        StringBuffer result = new StringBuffer();
        result.append(stopTime - startTime);
        result.append(" ms");
        return result.toString();
    }

    public String toString(String actionName) {
        return "Timer[" + this.name + "] on [" + actionName + "] " + toString();
    }

    /**
     * Express the "reading" on the stopwatch as a numeric type.
     *
     * @throws IllegalStateException if the Stopwatch has never been used,
     * or if the stopwatch is still running.
     */
    public long getTimeInMillis() {
        validateIsReadable();
        return stopTime - startTime;
    }

    /**
     * Throws IllegalStateException if the watch has never been started,
     * or if the watch is still running.
     */
    private void validateIsReadable() {
        if (running) {
            String message = "Cannot read a stopwatch which is still running.";
            throw new IllegalStateException(message);
        }
        if (!usedOnce) {
            String message = "Cannot read a stopwatch which has never been started.";
            throw new IllegalStateException(message);
        }
    }

    public boolean isRunning() {
        return running;
    }

    public void reset() {
        boolean restart = isRunning();
        if (isRunning()) {
            stop();
        }
        startTime = System.currentTimeMillis();
        if (restart) {
            start();
        }
    }
}

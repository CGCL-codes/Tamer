package net.sourceforge.squirrel_sql.client.util;

import java.awt.AWTEvent;
import java.awt.EventQueue;
import java.awt.Toolkit;
import java.io.PrintStream;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Monitors the AWT event dispatch thread for events that take longer than
 * a certain time to be dispatched.
 * 
 * The principle is to record the time at which we start processing an event,
 * and have another thread check frequently to see if we're still processing.
 * If the other thread notices that we've been processing a single event for
 * too long, it prints a stack trace showing what the event dispatch thread
 * is doing, and continues to time it until it finally finishes.
 * 
 * This is useful in determining what code is causing your Java application's
 * GUI to be unresponsive.
 * 
 * @author Elliott Hughes <enh@jessies.org>
 */
public final class EventDispatchThreadHangMonitor extends EventQueue {

    private static final EventQueue INSTANCE = new EventDispatchThreadHangMonitor();

    private static final long CHECK_INTERVAL_MS = 100;

    private static final long UNREASONABLE_DISPATCH_DURATION_MS = 500;

    private static final long NO_CURRENT_EVENT = 0;

    private long startedLastEventDispatchAt = NO_CURRENT_EVENT;

    private boolean reportedHang = false;

    private Thread eventDispatchThread = null;

    private EventDispatchThreadHangMonitor() {
        initTimer();
    }

    /**
     * Sets up a timer to check for hangs frequently.
     */
    private void initTimer() {
        final long initialDelayMs = 0;
        final boolean isDaemon = true;
        Timer timer = new Timer("EventDispatchThreadHangMonitor", isDaemon);
        timer.schedule(new HangChecker(), initialDelayMs, CHECK_INTERVAL_MS);
    }

    private class HangChecker extends TimerTask {

        @Override
        public void run() {
            synchronized (INSTANCE) {
                checkForHang();
            }
        }

        private void checkForHang() {
            if (startedLastEventDispatchAt == NO_CURRENT_EVENT) {
                return;
            }
            if (timeSoFar() > UNREASONABLE_DISPATCH_DURATION_MS) {
                reportHang();
            }
        }

        private void reportHang() {
            if (reportedHang) {
                return;
            }
            reportedHang = true;
            System.out.println("--- event dispatch thread stuck processing event for " + timeSoFar() + " ms:");
            StackTraceElement[] stackTrace = eventDispatchThread.getStackTrace();
            printStackTrace(System.out, stackTrace);
        }

        private void printStackTrace(PrintStream out, StackTraceElement[] stackTrace) {
            final String ourEventQueueClassName = EventDispatchThreadHangMonitor.class.getName();
            for (StackTraceElement stackTraceElement : stackTrace) {
                if (stackTraceElement.getClassName().equals(ourEventQueueClassName)) {
                    return;
                }
                out.println("    " + stackTraceElement);
            }
        }
    }

    /**
     * Returns how long we've been processing the current event (in
     * milliseconds).
     */
    private long timeSoFar() {
        long currentTime = System.currentTimeMillis();
        return (currentTime - startedLastEventDispatchAt);
    }

    /**
     * Sets up hang detection for the event dispatch thread.
     */
    public static void initMonitoring() {
        Toolkit.getDefaultToolkit().getSystemEventQueue().push(INSTANCE);
    }

    /**
     * Overrides EventQueue.dispatchEvent to call our pre and post hooks either
     * side of the system's event dispatch code.
     */
    @Override
    protected void dispatchEvent(AWTEvent event) {
        preDispatchEvent();
        super.dispatchEvent(event);
        postDispatchEvent();
    }

    /**
     * Stores the time at which we started processing the current event.
     */
    private synchronized void preDispatchEvent() {
        if (eventDispatchThread == null) {
            eventDispatchThread = Thread.currentThread();
        }
        reportedHang = false;
        startedLastEventDispatchAt = System.currentTimeMillis();
    }

    /**
     * Reports the end of any ongoing hang, and notes that we're no longer
     * processing an event.
     */
    private synchronized void postDispatchEvent() {
        if (reportedHang) {
            System.out.println("--- event dispatch thread unstuck after " + timeSoFar() + " ms.");
        }
        startedLastEventDispatchAt = NO_CURRENT_EVENT;
    }
}

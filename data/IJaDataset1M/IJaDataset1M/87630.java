package com.sleepycat.je.utilint;

import java.io.PrintStream;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Internal class used for transient event tracing.  Subclass this with
 * specific events.  Subclasses should have toString methods for display and
 * events should be added by calling EventTrace.addEvent();
 */
public class EventTrace {

    private static int MAX_EVENTS = 100;

    public static final boolean TRACE_EVENTS = false;

    static AtomicInteger currentEvent = new AtomicInteger(0);

    static final EventTrace[] events = new EventTrace[MAX_EVENTS];

    static final int[] threadIdHashes = new int[MAX_EVENTS];

    static volatile boolean disableEvents = false;

    protected String comment;

    public EventTrace(String comment) {
        this.comment = comment;
    }

    public EventTrace() {
        comment = null;
    }

    @Override
    public String toString() {
        return comment;
    }

    /**
     * Always return true so this method can be used with asserts:
     * i.e. assert addEvent(xxx);
     */
    public static boolean addEvent(EventTrace event) {
        if (disableEvents) {
            return true;
        }
        int nextEventIdx = currentEvent.getAndIncrement() % MAX_EVENTS;
        events[nextEventIdx] = event;
        threadIdHashes[nextEventIdx] = System.identityHashCode(Thread.currentThread());
        return true;
    }

    public static boolean addEvent(String comment) {
        if (disableEvents) {
            return true;
        }
        return addEvent(new EventTrace(comment));
    }

    public static void dumpEvents() {
        dumpEvents(System.out);
    }

    public static void dumpEvents(PrintStream out) {
        if (disableEvents) {
            return;
        }
        out.println("----- Event Dump -----");
        EventTrace[] oldEvents = events;
        int[] oldThreadIdHashes = threadIdHashes;
        disableEvents = true;
        int j = 0;
        for (int i = currentEvent.get(); j < MAX_EVENTS; i++) {
            EventTrace ev = oldEvents[i % MAX_EVENTS];
            if (ev != null) {
                int thisEventIdx = i % MAX_EVENTS;
                out.print(oldThreadIdHashes[thisEventIdx] + " ");
                out.println(j + "(" + thisEventIdx + "): " + ev);
            }
            j++;
        }
    }

    public static class ExceptionEventTrace extends EventTrace {

        private Exception event;

        public ExceptionEventTrace() {
            event = new Exception();
        }

        @Override
        public String toString() {
            return LoggerUtils.getStackTrace(event);
        }
    }
}

package com.ivis.xprocess.util;

import java.util.Stack;

/**
 * Timer class often used in tests to determine the speed of certain operations.
 * It uses a stack to place created TimingEvents on.
 * On start the TimingEvent is pushed to the top of the Stack. Multiple starts
 * can occur.
 * On stop it pops the TimingEvent off the Stack and stops it.
 *
 */
public class Timer {

    private static Stack<TimingEvent> timingEvents = new Stack<TimingEvent>();

    public static void start() {
        timingEvents.push(new TimingEvent(null));
    }

    /**
     * Start timing with a message, usually the component that has made the call.
     * This message is automatically used when the TimingEvent is stopped so
     * it is easy to see the strat/stop pairs in the output.
     *
     * @param msg
     */
    public static void start(String msg) {
        timingEvents.push(new TimingEvent(msg));
    }

    /**
     * Stop the TimingEvent at the top of the Stack.
     *
     * @return the time elapsed during the TimingEvent
     */
    public static long stop() {
        if (!timingEvents.empty()) {
            TimingEvent timingEvent = timingEvents.pop();
            return timingEvent.stop();
        }
        return 0;
    }

    /**
     * Stop all timingEvents.
     */
    public static void stopAll() {
        while (!timingEvents.empty()) {
            TimingEvent timingEvent = timingEvents.pop();
            timingEvent.stop();
        }
    }

    private static class TimingEvent {

        private long startTime = 0;

        private String msg;

        public TimingEvent(String msg) {
            startTime = System.currentTimeMillis();
            this.msg = msg;
            if (msg != null) {
                System.out.println("TimerStart: " + msg);
            }
        }

        public long stop() {
            long elapsedTime = System.currentTimeMillis() - startTime;
            if (msg != null) {
                System.out.println("TimerStop : " + msg + " : " + elapsedTime + "\n");
            }
            return elapsedTime;
        }
    }
}

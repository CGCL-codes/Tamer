package foxtrot.pumps;

import java.awt.AWTEvent;
import java.awt.EventQueue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import sun.awt.AppContext;
import sun.awt.SunToolkit;

/**
 * Specialized ConditionalEventPump for Sun's JDK 7.0.
 * Differently from previous releases, the locking involved is now based
 * on {@link Lock}s instead of using the synchronized keyword.
 */
public class SunJDK17ConditionalEventPump extends SunJDK141ConditionalEventPump {

    protected AWTEvent waitForEvent() {
        EventQueue queue = getEventQueue();
        AWTEvent nextEvent = peekEvent(queue);
        if (nextEvent != null) return nextEvent;
        Lock pushPopLock = (Lock) AppContext.getAppContext().get(AppContext.EVENT_QUEUE_LOCK_KEY);
        Condition pushPopCond = (Condition) AppContext.getAppContext().get(AppContext.EVENT_QUEUE_COND_KEY);
        while (true) {
            SunToolkit.flushPendingEvents();
            pushPopLock.lock();
            try {
                nextEvent = peekEvent(queue);
                if (nextEvent != null) return nextEvent;
                if (debug) System.out.println("[SunJDK17ConditionalEventPump] Waiting for events...");
                try {
                    pushPopCond.await();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return null;
                }
            } finally {
                pushPopLock.unlock();
            }
        }
    }
}

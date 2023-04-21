package iwork.manager.core.monitor;

import iwork.manager.core.ApplicationMonitor;
import iwork.eheap2.*;
import java.io.PrintStream;

/**
* Monitor for monitoring the EventHeap. It simply sends an event to the
 * EventHeap and sees whether the same event comes back.
 *
 * @author Ulf Ochsenfahrt (ulfjack@stanford.edu)
 */
public class EventHeapMonitor extends ApplicationMonitor {

    /**
    * Check interval in milliseconds.
     */
    public static final int INTERVAL = 3 * 1000;

    public static final String PREFIX = "[EHMonitor] ";

    private static final boolean DEBUG = true;

    EventHeap myHeap;

    private void sendEvent() throws Exception {
        err.println(PREFIX + "Sending monitor event");
        Event myEvent = new Event("MonitorEvent");
        myEvent.addField("Payload", "" + System.currentTimeMillis(), Event.FORMAL);
        myEvent.setPostValue("TimeToLive", new Integer(100));
        myHeap.putEvent(myEvent);
    }

    public void run() {
        try {
            myHeap = new EventHeap("localhost");
            Event myEvent = new Event("MonitorEvent");
            myEvent.addField("Payload", String.class, Event.VIRTUAL, Event.FORMAL);
            while (waitForInterval(INTERVAL)) {
                sendEvent();
                Event retEvent = myHeap.waitForEvent(myEvent);
                notifyAlive();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public EventHeapMonitor() {
        super(System.err, INTERVAL);
    }

    public String getPrefix() {
        return PREFIX;
    }
}

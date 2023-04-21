package net.assimilator.utility.eventbus.test_set_1;

import net.assimilator.utility.eventbus.GenericEventListener;
import net.assimilator.utility.eventbus.event.Event;
import net.assimilator.utility.task.AbstractService;
import java.util.UUID;
import java.util.logging.Logger;

/**
 * Test event receiver.
 *
 * @author Larry Mitchell
 * @version $Id$
 */
public class Receiver2 implements AbstractService, GenericEventListener {

    /**
     * The Logger for this class
     */
    private static final Logger logger = Logger.getLogger("net.assimilator.utility.eventbus.test_set_1");

    private int eventCount;

    private final Object accessLock = new Object();

    /**
     * the seriveId of this service as  it was created
     */
    protected UUID serviceId;

    public Receiver2() {
        eventCount = 0;
        serviceId = UUID.randomUUID();
    }

    public void notify(Event event) {
        synchronized (accessLock) {
            logger.info("Receiver2(" + eventCount + "): " + event);
            eventCount++;
        }
    }

    /**
     * get the service name
     *
     * @return the service name
     */
    public String getName() {
        return "Receiver2";
    }

    public Object getServiceId() {
        return serviceId;
    }

    public void clearEventCount() {
        synchronized (accessLock) {
            eventCount = 0;
        }
    }

    public int getEventCount() {
        synchronized (accessLock) {
            return eventCount;
        }
    }
}

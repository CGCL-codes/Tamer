package net.sf.unruly.mock;

import net.sf.unruly.connection.Connection;
import net.sf.unruly.connection.ConnectionFactory;
import net.sf.unruly.event.RulesPropertyEvent;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * @author Jeff Drost
 */
public class ExpectTestConnection extends TestConnection {

    private static final Log LOG = LogFactory.getLog(ExpectTestConnection.class);

    private Queue<RulesPropertyEvent> expectedEventQueue = new LinkedList<RulesPropertyEvent>();

    public void expect(RulesPropertyEvent event) {
        expectedEventQueue.add(event);
    }

    public void process(final Queue<RulesPropertyEvent> eventQueue) {
        try {
            processing = true;
            if (eventQueue.size() != expectedEventQueue.size()) {
                fail("the number of collected events is unexpected", eventQueue, expectedEventQueue);
            }
            List<RulesPropertyEvent> eventList = new ArrayList<RulesPropertyEvent>();
            List<RulesPropertyEvent> expectedEventList = new ArrayList<RulesPropertyEvent>();
            eventList.addAll(eventQueue);
            expectedEventList.addAll(expectedEventQueue);
            for (int i = 0; i < eventList.size(); i++) {
                RulesPropertyEvent event = eventList.get(i);
                RulesPropertyEvent expectedEvent = expectedEventList.get(i);
                if (event.equals(expectedEvent)) {
                    LOG.info("found expected event >>> " + event);
                } else {
                    fail("event[" + i + "] did not match the expected event", eventQueue, expectedEventQueue);
                }
            }
            LOG.info("clearing queue");
            eventQueue.clear();
            expectedEventQueue.clear();
        } finally {
            processing = false;
        }
    }

    public void fail(String message, Collection<RulesPropertyEvent> eventQueue, Collection<RulesPropertyEvent> expectedEventQueue) {
        LOG.error("event queue is not populated as expected");
        LOG.error("eventQueue.size         = " + eventQueue.size());
        LOG.error("expectedEventQueue.size = " + expectedEventQueue.size());
        log(eventQueue, "actual");
        log(expectedEventQueue, "expected");
        throw new RuntimeException(message);
    }

    private void log(Collection<RulesPropertyEvent> events, String name) {
        int i = 0;
        for (RulesPropertyEvent event : events) {
            LOG.info(name + "[" + i + "] = " + event);
            i++;
        }
    }

    public static class Factory implements ConnectionFactory {

        public Connection openConnection() {
            return new ExpectTestConnection();
        }
    }
}

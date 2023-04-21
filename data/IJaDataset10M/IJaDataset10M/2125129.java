package com.mindquarry.events;

import junit.framework.TestCase;
import com.mindquarry.events.types.TestEvent;
import com.mindquarry.events.types.TestListener;
import com.mindquarry.events.types.TestSource;

/**
 * Add summary documentation here.
 * 
 * @author <a href="mailto:alexander(dot)saar(at)mindquarry(dot)com">Alexander
 *         Saar</a>
 */
public class EventBrokerTest extends TestCase {

    public void testBroker() throws Exception {
        TestSource source = new TestSource();
        Event event = new TestEvent(source, "a test event");
        EventListener listener = new TestListener();
        EventBroker broker = new EventBroker();
        broker.registerEvent(TestEvent.ID);
        broker.registerEventListener(listener, TestEvent.ID);
        broker.publishEvent(event, true);
        TestCase.assertTrue(event.isConsumed());
    }
}

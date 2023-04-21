package net.borderwars.simulator.test;

import junit.framework.TestCase;
import net.borderwars.simulator.EventQueue;
import net.borderwars.simulator.events.BEvent;
import java.util.Date;

public class EventQueueTest extends TestCase {

    EventQueue fEventQueue;

    public void testGetModel() throws Exception {
        EventQueue eq = new EventQueue();
        assertTrue("Model available", eq.getModel() != null);
    }

    public void testPlaceEvent() throws Exception {
        EventQueue eq = new EventQueue();
        TestEvent te = new TestEvent(100);
        eq.placeEvent(te);
        BEvent e = eq.take();
        assertTrue("Same event came off queue that I expected", e.equals(te));
    }

    public void testTake() throws Exception {
        EventQueue eq = new EventQueue();
        TestEvent te = new TestEvent(100);
        eq.placeEvent(te);
        BEvent e = eq.take();
        assertTrue("Same event came off queue that I expected", e.equals(te));
    }

    public class TestEvent extends BEvent {

        public TestEvent(long l) {
            super(l);
        }

        public TestEvent(Date d) {
            super(d);
        }

        public void process() {
        }

        public String getName() {
            return ("TEst Event");
        }
    }
}

package org.gcreator.pineapple.managers;

import org.gcreator.pineapple.managers.EventManager;
import org.gcreator.pineapple.plugins.DefaultEventTypes;
import org.gcreator.pineapple.plugins.Event;
import org.gcreator.pineapple.plugins.EventHandler;
import org.gcreator.pineapple.plugins.EventPriority;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author luis
 */
public class EventManagerTest {

    public EventManagerTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    private int calledHandler1 = 0;

    private int calledHandler2 = 0;

    /**
     * Test of addEventHandler method, of class EventManager.
     */
    @Test
    public void testEventHandlers() {
        System.out.println("addEventHandler");
        EventHandler handler1 = new EventHandler() {

            @Override
            public void handleEvent(Event e) {
                if (e.getEventType().equals("unit-test-type1")) {
                    calledHandler1++;
                } else {
                    fail("Event handler being called for events it did not ask for.");
                }
            }
        };
        EventHandler handler2 = new EventHandler() {

            @Override
            public void handleEvent(Event e) {
                calledHandler2++;
            }
        };
        EventHandler handler3 = new EventHandler() {

            @Override
            public void handleEvent(Event e) {
                if (calledHandler1 == 0) {
                    fail("LOW priority handler being called before HIGH priority.");
                }
            }
        };
        EventManager.addEventHandler(handler3, "unit-test-type1", EventPriority.LOW);
        EventManager.addEventHandler(handler1, "unit-test-type1", EventPriority.HIGH);
        EventManager.addEventHandler(handler2, DefaultEventTypes.ALL);
        EventManager.fireEvent(this, "unit-test-type1");
        EventManager.fireEvent(this, "unit-test-allonly");
        try {
            Thread.sleep(100);
        } catch (Exception e) {
            fail("Could not run unit test");
        }
        if (calledHandler1 == 0) {
            fail("Handlers for specific events not being called or taking too long.");
        } else if (calledHandler1 != 1) {
            fail("Handlers being called too many times.");
        }
        if (calledHandler2 == 0) {
            fail("Handlers for ALL events not being called or taking too long.");
        }
    }
}

package org.yjchun.hanghe.util.event;

import static org.junit.Assert.*;
import java.lang.ref.WeakReference;
import org.junit.Test;

/**
 * @author yjchun
 *
 */
public class EventDispatcherTest {

    int count = 0;

    int fireCount = 10;

    @Test
    public void testThreadedEventDispatcherFire() throws InterruptedException {
        ThreadedEventDispatcher dispatcher = new ThreadedEventDispatcher();
        dispatcher.start();
        Object o = new Object();
        WeakReference<Object> ref = new WeakReference<Object>(o);
        o = null;
        EventListener innerListener = new TestListener();
        dispatcher.addListener("event1", innerListener, innerListener);
        dispatcher.addListener("event1", new EventListener() {

            public void onEvent(EventObject arg) {
                count++;
            }
        });
        dispatcher.addListener("event1", new EventListener() {

            public void onEvent(EventObject arg) {
                count++;
            }
        }, innerListener);
        dispatcher.addListener("event1", new EventListener() {

            public void onEvent(EventObject arg) {
                count++;
            }
        }, this);
        dispatcher.addListener("event1", new EventListener() {

            public void onEvent(EventObject arg) {
                count++;
            }
        }, "listener ID");
        for (int i = 0; i < fireCount; i++) {
            dispatcher.fire("event1").join();
        }
        assertTrue(count == fireCount * 5);
        count = 50;
        innerListener = null;
        Runtime.getRuntime().gc();
        for (int i = 0; i < fireCount; i++) {
            dispatcher.fire("event1").join();
        }
        assertTrue(ref.get() == null);
        assertTrue(count == fireCount * 7);
    }

    @Test
    public void testThreadedEventDispatcherRemoval() throws InterruptedException {
        ThreadedEventDispatcher dispatcher = new ThreadedEventDispatcher(100);
        dispatcher.start();
        TestListener li = new TestListener();
        count = 0;
        dispatcher.addListener("event1", li, "test1");
        dispatcher.addListener("event1", li, "test1");
        assertNotNull(dispatcher.addListener("event1", li, "test2"));
        dispatcher.fire("event1").join();
        assertEquals(3, count);
        dispatcher.removeListener("event1", "test1");
        count = 0;
        dispatcher.fire("event1").join();
        assertTrue(count == 1);
        dispatcher.addListener("event1", li, "test1");
        dispatcher.addListener("event1", li, "test2");
        dispatcher.removeListener("test2");
        count = 0;
        dispatcher.fire("event1").join();
        assertTrue(count == 1);
        EventObject e = null;
        count = 0;
        for (int i = 0; i < 1000; i++) {
            e = dispatcher.fire("event1");
        }
        dispatcher.shutdownNow();
        count = 0;
        for (int i = 0; i < 100; i++) {
            e = dispatcher.fire("event1");
        }
        e.join();
        assertTrue(count == 0 || count == 1);
    }

    @Test
    public void testThreadedEventDispatcherMulti() throws InterruptedException {
        ThreadedEventDispatcher dispatcher = new ThreadedEventDispatcher(100);
        dispatcher.start(1);
        EventObject e = null;
        TestListener li = new TestListener();
        count = 0;
        dispatcher.addListener("event1", li, "test1");
        dispatcher.addListener("event1", li, "test1");
        assertNotNull(dispatcher.addListener("event1", li, "test2"));
        for (int i = 0; i < 1000; i++) {
            e = dispatcher.fire("event1");
        }
        e.join();
        assertEquals(3 * 1000, count);
    }

    public class TestListener implements EventListener {

        public void onEvent(EventObject arg) {
            EventDispatcherTest.this.count++;
        }
    }
}

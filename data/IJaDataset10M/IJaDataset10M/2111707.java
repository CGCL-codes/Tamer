package org.sourceforge.jemm.client.id;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.sourceforge.jemm.types.ID;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class TrackedIDFactoryImplTest {

    TrackedIDFactory factory;

    ID id1;

    @Before
    public void setUp() {
        factory = new TrackedIDFactory();
        id1 = new ID(1);
    }

    @After
    public void tearDown() {
        factory.shutdown();
        factory = null;
        id1 = null;
    }

    @Test
    public void createNoId() {
        TrackedID tId = factory.get(id1);
        assertTrue(factory.contains(id1));
        assertEquals(tId, factory.get(id1));
        assertTrue(factory.contains(tId));
    }

    @Test
    public void createExistingId() {
        TrackedID tId = factory.get(id1);
        TrackedID tId2 = factory.get(new ID(1));
        assertTrue(tId == tId2);
        assertEquals(tId, factory.get(id1));
        assertTrue(factory.contains(tId));
    }

    @Test
    public void gcClearsID() throws InterruptedException {
        factory.get(id1);
        System.gc();
        Thread.sleep(10);
        assertFalse(factory.contains(id1));
    }

    @Test
    public void gcDoesNotClearIDHeld() throws InterruptedException {
        TrackedID tId = factory.get(id1);
        assertNotNull(tId);
        System.gc();
        Thread.sleep(10);
        assertTrue(factory.contains(id1));
    }

    @Test
    public void getExists() {
        TrackedID tId = factory.get(id1);
        TrackedID found = factory.get(id1);
        assertTrue(tId == found);
    }

    @Test
    public void idRemovedOnReferenceCollection() throws InterruptedException {
        factory.get(id1);
        System.gc();
        Thread.sleep(10);
        assertFalse(factory.contains(id1));
        assertEquals(0, factory.size());
    }

    @Test(timeout = 1000)
    public void listenToGC() throws InterruptedException {
        final CountDownLatch latch = new CountDownLatch(1);
        final List<ID> found = new ArrayList<ID>();
        TrackedIDListener listener = new TrackedIDListener() {

            @Override
            public void created(ID id) {
            }

            @Override
            public void expired(ID id) {
                found.add(id);
                latch.countDown();
            }
        };
        factory.addListener(listener);
        factory.get(id1);
        System.gc();
        latch.await();
        assertEquals(1, found.size());
    }

    @Test
    public void removeListenerTest() throws InterruptedException {
        final AtomicBoolean seen = new AtomicBoolean(false);
        TrackedIDListener listener = new TrackedIDListener() {

            @Override
            public void created(ID id) {
            }

            @Override
            public void expired(ID id) {
                seen.set(true);
            }
        };
        factory.addListener(listener);
        factory.removeListener(listener);
        factory.get(id1);
        System.gc();
        Object lockObj = new Object();
        synchronized (lockObj) {
            lockObj.wait(500);
        }
        assertFalse(seen.get());
    }
}

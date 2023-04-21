package org.sourceforge.jemm.database;

import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;
import org.sourceforge.jemm.types.ID;

/**
 * Lock handling tests for the <code>BaseDatabase</code> database implementation.
 * <P>
 * See {@link BaseDatabaseTest} for more information.
 * 
 * @author Rory Graves
 */
public class BaseDatabaseLockTest extends BaseDatabaseTest {

    EventComparator expected = new EventComparator();

    ClassId classId;

    ID object1Id;

    ClientId client1;

    ClientThreadId client1Thread1;

    ClientThreadId client1Thread2;

    ClientId client2;

    ClientThreadId client2Thread1;

    @Before
    public void setupLockData() throws Exception {
        client1 = new ClientId("1");
        client1Thread1 = new ClientThreadId(client1, "t1");
        client1Thread2 = new ClientThreadId(client1, "t2");
        client2 = new ClientId("2");
        client2Thread1 = new ClientThreadId(client2, "t1");
        ClassInfo classInfo = DatabaseTestUtilities.createClassInfo("com.foo.Bar", new FieldInfo("a", DUMMY_CLASS, FieldType.BOOLEAN), new FieldInfo("b", DUMMY_CLASS, FieldType.DOUBLE), new FieldInfo("c", DUMMY_CLASS, FieldType.OBJECT));
        classId = database.registerClass(client1, classInfo);
        object1Id = database.newObject(client1, classId, null);
    }

    @Test
    public void basicLockAcquireTest() {
        ClientId client1 = new ClientId("1");
        ClientThreadId client1Thread1 = new ClientThreadId(client1, "t1");
        TestLockAcquiredListener listener = new TestLockAcquiredListener();
        database.setClientLockAcquiredListener(client1, listener);
        database.acquireLock(client1Thread1, object1Id);
        expected.add("ACQUIRE", client1Thread1, object1Id);
        expected.assertMatches(listener.seenEvents);
        sanityCheck();
    }

    private void sanityCheck() {
        int count = debug.noLocksHeld();
        if (count > 0) Assert.fail("Locks held after test complete");
    }

    @Test
    public void sameClientQueuedLockAcquireTest() {
        TestLockAcquiredListener listener = new TestLockAcquiredListener();
        database.setClientLockAcquiredListener(client1, listener);
        database.acquireLock(client1Thread1, object1Id);
        database.acquireLock(client1Thread2, object1Id);
        expected.add("ACQUIRE", client1Thread1, object1Id);
        expected.assertMatches(listener.seenEvents);
        database.releaseLock(client1Thread1, object1Id);
        expected.add("ACQUIRE", client1Thread2, object1Id);
        expected.assertMatches(listener.seenEvents);
        if (debug.noLocksHeld() > 0) try {
            System.out.println("Extra lock release wait");
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        sanityCheck();
    }

    @Test
    public void differentClientQueuedLockAcquireTest() {
        TestLockAcquiredListener listener1 = new TestLockAcquiredListener();
        database.setClientLockAcquiredListener(client1, listener1);
        TestLockAcquiredListener listener2 = new TestLockAcquiredListener();
        database.setClientLockAcquiredListener(client2, listener2);
        database.acquireLock(client1Thread1, object1Id);
        database.acquireLock(client2Thread1, object1Id);
        expected.add("ACQUIRE", client1Thread1, object1Id);
        expected.assertMatches(listener1.seenEvents);
        EventComparator expected2 = new EventComparator();
        expected2.assertMatches(listener2.seenEvents);
        database.releaseLock(client1Thread1, object1Id);
        expected.assertMatches(listener1.seenEvents);
        expected2.add("ACQUIRE", client2Thread1, object1Id);
        expected2.assertMatches(listener2.seenEvents);
        sanityCheck();
    }

    @Test
    public void removeListenerTest() {
        TestLockAcquiredListener listener1 = new TestLockAcquiredListener();
        database.setClientLockAcquiredListener(client1, listener1);
        database.acquireLock(client1Thread1, object1Id);
        expected.add("ACQUIRE", client1Thread1, object1Id);
        database.releaseLock(client1Thread1, object1Id);
        database.removeLockAcquiredListener(client1);
        database.acquireLock(client1Thread1, object1Id);
        database.releaseLock(client1Thread1, object1Id);
        expected.assertMatches(listener1.seenEvents);
        sanityCheck();
    }

    @Test
    public void clientDisconnectTest1() {
        TestLockAcquiredListener listener1 = new TestLockAcquiredListener();
        database.setClientLockAcquiredListener(client1, listener1);
        database.acquireLock(client2Thread1, object1Id);
        database.acquireLock(client1Thread1, object1Id);
        expected.assertMatches(listener1.seenEvents);
        database.clientDisconnect(client2);
        expected.add("ACQUIRE", client1Thread1, object1Id);
        expected.assertMatches(listener1.seenEvents);
        sanityCheck();
    }

    @Test
    public void clientDisconnectTest2() {
        TestLockAcquiredListener listener1 = new TestLockAcquiredListener();
        database.setClientLockAcquiredListener(client1, listener1);
        database.acquireLock(client2Thread1, object1Id);
        database.acquireLock(client1Thread1, object1Id);
        expected.assertMatches(listener1.seenEvents);
        database.clientDisconnect(client1);
        expected.assertMatches(listener1.seenEvents);
        sanityCheck();
    }
}

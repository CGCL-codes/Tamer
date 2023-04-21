package org.jaffa.persistence.blackboxtests;

import java.util.*;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.jaffa.metadata.StringFieldMetaData;
import org.jaffa.persistence.*;
import org.jaffa.persistence.exceptions.*;
import org.jaffa.persistence.domainobjects.*;
import org.jaffa.exceptions.ApplicationExceptions;
import org.jaffa.persistence.engines.jdbcengine.LockedApplicationException;

/**
 *
 * @author  GautamJ
 * @version
 */
public class ConcurrencyTest extends TestCase {

    int m_succesfulUowCreationCounter;

    /** Assembles and returns a test suite containing all known tests.
     * @return A test suite.
     */
    public static Test suite() {
        return new Wrapper(new TestSuite(ConcurrencyTest.class));
    }

    /** Creates new QueryTest */
    public ConcurrencyTest(String name) {
        super(name);
    }

    /** This test will create 50 UOW objects concurrently. The test fails, if any exception is raised.
     */
    public void testConcurrentUowCreation() {
        m_succesfulUowCreationCounter = 0;
        int threadLimit = 50;
        ThreadGroup tg = new ThreadGroup("" + (new java.util.Date()).getTime());
        try {
            for (int i = 0; i < threadLimit; i++) {
                Thread t = new Thread(tg, new UowCreationThread());
                t.start();
            }
        } finally {
            try {
                while (tg.activeCount() > 1) Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
                fail("Error in thread execution");
            }
            if (m_succesfulUowCreationCounter != threadLimit) fail(m_succesfulUowCreationCounter + " UOW object(s) were created out of the expected " + threadLimit);
        }
    }

    private synchronized void incrementSuccesfulUowCreationCounter() {
        ++m_succesfulUowCreationCounter;
    }

    private class UowCreationThread implements Runnable {

        /** This will create a UOW object and then perform a rollback on it.
         * It will update the SuccesfulUowCreationCounter on the calling class, if everything goes smoothly.
         */
        public void run() {
            try {
                UOW uow = new UOW();
                uow.rollback();
                incrementSuccesfulUowCreationCounter();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /** This is a test for Optimistic locking.
     * It will retrieve the condition 'Z-TESTSYCD-01' in Optimistic locking-mode and modify a field. The condition will remain unlocked.
     * It'll then retrieve the same condition in another UOW, but in Paranoid locking mode. The other UOW should obtain a lock on the condition object.
     * It'll then try to retrieve the same condition in another UOW, but in Pessimistic locking mode. It should be possible to modify it.
     * Finally retrieve the same object in another UOW in Optimistic locking mode and try to update it. It should be possible to modify it.
     * Note: In the case of Optimistic locking, a lock is acquired on the object only when the UOW is commited.
     */
    public void testOptimisticLocking() {
        UOW uow1 = null;
        UOW uow2 = null;
        try {
            uow1 = new UOW();
            Criteria c = new Criteria();
            c.setTable(ConditionMeta.getName());
            c.addCriteria(ConditionMeta.CONDITION, "Z-TESTSYCD-01");
            c.setLocking(Criteria.LOCKING_OPTIMISTIC);
            Iterator i = uow1.query(c).iterator();
            assertTrue("Condition not retrieved", i.hasNext());
            Condition condition1 = (Condition) i.next();
            assertTrue("Only one Condition should have been retrieved", !i.hasNext());
            assertTrue("Condition should not have been locked because we doing Optimistic locking", !condition1.isLocked());
            condition1.setDescription(condition1.getDescription() + 'Z');
            assertTrue("Condition should not have been locked because we doing Optimistic locking", !condition1.isLocked());
            uow2 = new UOW();
            c = new Criteria();
            c.setTable(ConditionMeta.getName());
            c.addCriteria(ConditionMeta.CONDITION, "Z-TESTSYCD-01");
            c.setLocking(Criteria.LOCKING_PARANOID);
            i = uow2.query(c).iterator();
            assertTrue("Condition not retrieved", i.hasNext());
            Condition condition2 = (Condition) i.next();
            assertTrue("Only one Condition should have been retrieved", !i.hasNext());
            assertTrue("Condition should have been locked because we are doing Paranoid locking", condition2.isLocked());
            uow2.rollback();
            uow2 = new UOW();
            c = new Criteria();
            c.setTable(ConditionMeta.getName());
            c.addCriteria(ConditionMeta.CONDITION, "Z-TESTSYCD-01");
            c.setLocking(Criteria.LOCKING_PESSIMISTIC);
            i = uow2.query(c).iterator();
            assertTrue("Condition not retrieved", i.hasNext());
            condition2 = (Condition) i.next();
            assertTrue("Only one Condition should have been retrieved", !i.hasNext());
            assertTrue("Condition should not have been initially locked because we doing Pessimistic locking", !condition2.isLocked());
            condition2.setDescription(condition2.getDescription() + "Z");
            assertTrue("Condition should have been locked because we are doing Pessimistic locking", condition2.isLocked());
            uow2.rollback();
            uow2 = new UOW();
            c = new Criteria();
            c.setTable(ConditionMeta.getName());
            c.addCriteria(ConditionMeta.CONDITION, "Z-TESTSYCD-01");
            i = uow2.query(c).iterator();
            assertTrue("Condition not retrieved", i.hasNext());
            condition2 = (Condition) i.next();
            assertTrue("Only one Condition should have been retrieved", !i.hasNext());
            assertTrue("Condition should not have been initially locked because we doing Optimistic locking", !condition2.isLocked());
            condition2.setDescription(condition2.getDescription() + "Z");
            uow2.update(condition2);
            uow2.commit();
        } catch (Exception e) {
            e.printStackTrace();
            fail(e.getMessage());
        } finally {
            try {
                if (uow1 != null) uow1.rollback();
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                if (uow2 != null) uow2.rollback();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /** This is a test for Pessimistic locking.
     * It will retrieve the condition 'Z-TESTSYCD-01' in Pessimistic locking-mode and modify a field. The condition will get locked.
     * It'll then try to retrieve the same condition in another UOW, but in Paranoid locking mode. The retrieve should fail, since the object has been locked by the 1st UOW
     * It'll then try to retrieve the same condition in another UOW, but in Pessimistic locking mode. The retrieve will work, but it'll not be possible to modify it, since the object has been locked by the 1st UOW
     * Finally retrieve the same object in another UOW in Optimistic locking mode and try to update it. The update will fail.
     * Note: In the case of Pessimistic locking, a lock is acquired on the object when any field is updated.
     */
    public void testPessimisticLocking() {
        UOW uow1 = null;
        UOW uow2 = null;
        try {
            uow1 = new UOW();
            Criteria c = new Criteria();
            c.setTable(ConditionMeta.getName());
            c.addCriteria(ConditionMeta.CONDITION, "Z-TESTSYCD-01");
            c.setLocking(Criteria.LOCKING_PESSIMISTIC);
            Iterator i = uow1.query(c).iterator();
            assertTrue("Condition not retrieved", i.hasNext());
            Condition condition1 = (Condition) i.next();
            assertTrue("Only one Condition should have been retrieved", !i.hasNext());
            assertTrue("Condition should not have been initially locked because we doing Pessimistic locking", !condition1.isLocked());
            condition1.setDescription(condition1.getDescription() + 'Z');
            assertTrue("Condition should have been locked because we doing Pessimistic locking", condition1.isLocked());
            uow2 = new UOW();
            c = new Criteria();
            c.setTable(ConditionMeta.getName());
            c.addCriteria(ConditionMeta.CONDITION, "Z-TESTSYCD-01");
            c.setLocking(Criteria.LOCKING_PARANOID);
            try {
                uow2.query(c).iterator();
                fail("We expected a QueryFailedException to be raised while trying to obtain a lock on an already locked object");
            } catch (QueryFailedException e) {
            }
            c = new Criteria();
            c.setTable(ConditionMeta.getName());
            c.addCriteria(ConditionMeta.CONDITION, "Z-TESTSYCD-01");
            c.setLocking(Criteria.LOCKING_PESSIMISTIC);
            i = uow2.query(c).iterator();
            assertTrue("Condition not retrieved", i.hasNext());
            Condition condition2 = (Condition) i.next();
            assertTrue("Only one Condition should have been retrieved", !i.hasNext());
            assertTrue("Condition should not have been initially locked because we doing Pessimistic locking", !condition2.isLocked());
            try {
                condition2.setDescription(condition2.getDescription() + "Z");
                fail("We expected an AlreadyLockedObjectException to be raised while trying to modify an already locked object");
            } catch (AlreadyLockedObjectException e) {
            }
            c = new Criteria();
            c.setTable(ConditionMeta.getName());
            c.addCriteria(ConditionMeta.CONDITION, "Z-TESTSYCD-01");
            i = uow2.query(c).iterator();
            assertTrue("Condition not retrieved", i.hasNext());
            condition2 = (Condition) i.next();
            assertTrue("Only one Condition should have been retrieved", !i.hasNext());
            assertTrue("Condition should not have been initially locked because we doing Optimistic locking", !condition2.isLocked());
            condition2.setDescription(condition2.getDescription() + "Z");
            try {
                uow2.update(condition2);
                uow2.commit();
                fail("We expected a LockedApplicationException to be raised while trying to update an already locked object");
            } catch (CommitFailedException e) {
                if (e.getCause() == null || !(e.getCause() instanceof ApplicationExceptions) || !(((ApplicationExceptions) e.getCause()).iterator().next() instanceof LockedApplicationException)) {
                    e.printStackTrace();
                    this.fail("We expected a LockedApplicationException to be raised while trying to update an already locked object");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            fail(e.getMessage());
        } finally {
            try {
                if (uow1 != null) uow1.rollback();
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                if (uow2 != null) uow2.rollback();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /** This is a test for Paranoid locking.
     * It will retrieve the condition 'Z-TESTSYCD-01' in Paranoid locking-mode. The condition will get locked.
     * It'll then try to retrieve the same condition in another UOW, in Paranoid locking mode. The retrieve should fail, since the object has been locked by the 1st UOW
     * It'll then try to retrieve the same condition in another UOW, but in Pessimistic locking mode. The retrieve will work, but it'll not be possible to modify it, since the object has been locked by the 1st UOW
     * Finally retrieve the same object in another UOW in Optimistic locking mode and try to update it. The update will fail.
     * Note: In the case of Paranoid locking, a lock is acquired on the object when retrieving it.
     */
    public void testParanoidLocking() {
        UOW uow1 = null;
        UOW uow2 = null;
        try {
            uow1 = new UOW();
            Criteria c = new Criteria();
            c.setTable(ConditionMeta.getName());
            c.addCriteria(ConditionMeta.CONDITION, "Z-TESTSYCD-01");
            c.setLocking(Criteria.LOCKING_PARANOID);
            Iterator i = uow1.query(c).iterator();
            assertTrue("Condition not retrieved", i.hasNext());
            Condition condition1 = (Condition) i.next();
            assertTrue("Only one Condition should have been retrieved", !i.hasNext());
            assertTrue("Condition should have been locked because we are doing Paranoid locking", condition1.isLocked());
            uow2 = new UOW();
            c = new Criteria();
            c.setTable(ConditionMeta.getName());
            c.addCriteria(ConditionMeta.CONDITION, "Z-TESTSYCD-01");
            c.setLocking(Criteria.LOCKING_PARANOID);
            try {
                uow2.query(c).iterator();
                fail("We expected a QueryFailedException to be raised while trying to obtain a lock on an already locked object");
            } catch (QueryFailedException e) {
            }
            c = new Criteria();
            c.setTable(ConditionMeta.getName());
            c.addCriteria(ConditionMeta.CONDITION, "Z-TESTSYCD-01");
            c.setLocking(Criteria.LOCKING_PESSIMISTIC);
            i = uow2.query(c).iterator();
            assertTrue("Condition not retrieved", i.hasNext());
            Condition condition2 = (Condition) i.next();
            assertTrue("Only one Condition should have been retrieved", !i.hasNext());
            assertTrue("Condition should not have been initially locked because we doing Pessimistic locking", !condition2.isLocked());
            try {
                condition2.setDescription(condition2.getDescription() + "Z");
                fail("We expected an AlreadyLockedObjectException to be raised while trying to modify an already locked object");
            } catch (AlreadyLockedObjectException e) {
            }
            c = new Criteria();
            c.setTable(ConditionMeta.getName());
            c.addCriteria(ConditionMeta.CONDITION, "Z-TESTSYCD-01");
            i = uow2.query(c).iterator();
            assertTrue("Condition not retrieved", i.hasNext());
            condition2 = (Condition) i.next();
            assertTrue("Only one Condition should have been retrieved", !i.hasNext());
            assertTrue("Condition should not have been initially locked because we doing Optimistic locking", !condition2.isLocked());
            condition2.setDescription(condition2.getDescription() + "Z");
            try {
                uow2.update(condition2);
                uow2.commit();
                fail("We expected a LockedApplicationException to be raised while trying to update an already locked object");
            } catch (CommitFailedException e) {
                if (e.getCause() == null || !(e.getCause() instanceof ApplicationExceptions) || !(((ApplicationExceptions) e.getCause()).iterator().next() instanceof LockedApplicationException)) {
                    e.printStackTrace();
                    this.fail("We expected a LockedApplicationException to be raised while trying to update an already locked object");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            fail(e.getMessage());
        } finally {
            try {
                if (uow1 != null) uow1.rollback();
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                if (uow2 != null) uow2.rollback();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /** This is a test for ReadOnly locking.
     * It will retrieve the condition 'Z-TESTSYCD-01' in ReadOnly mode.
     * An exception should be raised while trying to update any of its fields.
     */
    public void testReadOnlyLocking() {
        UOW uow = null;
        try {
            uow = new UOW();
            Criteria c = new Criteria();
            c.setTable(ConditionMeta.getName());
            c.addCriteria(ConditionMeta.CONDITION, "Z-TESTSYCD-01");
            c.setLocking(Criteria.LOCKING_READ_ONLY);
            Iterator i = uow.query(c).iterator();
            assertTrue("Condition not retrieved", i.hasNext());
            Condition condition = (Condition) i.next();
            assertTrue("Only one Condition should have been retrieved", !i.hasNext());
            assertTrue("Condition should not have been locked because we doing ReadOnly locking", !condition.isLocked());
            try {
                condition.setDescription(condition.getDescription() + 'Z');
                fail("We expected an ReadOnlyObjectException to be raised while trying to update ReadOnly object");
            } catch (ReadOnlyObjectException e) {
            }
        } catch (Exception e) {
            e.printStackTrace();
            fail(e.getMessage());
        } finally {
            try {
                if (uow != null) uow.rollback();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * This is a test for dirty-reads. It performs the following:
     * Retrieves condition 'Z-TESTSYCD-01' and updates it's description in UOW1. The transaction is not committed.
     * Re-retrieves the condition in UOW1. The new description should be retrieved.
     * Retrieves condition 'Z-TESTSYCD-01' in UOW2 and should see the original description only.
     * UOW1 is committed. Subsequent retrieve in UOW2 should be able to see the new description.
     */
    public void testDirtyRead() {
        org.apache.log4j.BasicConfigurator.configure();
        UOW uow1 = null;
        UOW uow2 = null;
        final String conditionCode = "Z-TESTSYCD-01";
        String oldDescription = null;
        String newDescription = null;
        try {
            uow1 = new UOW();
            Condition condition1 = Condition.findByPK(uow1, conditionCode);
            assertNotNull("Condition not retrieved", condition1);
            assertEquals(conditionCode, condition1.getCondition());
            oldDescription = condition1.getDescription();
            assertNotNull("Old description", oldDescription);
            newDescription = oldDescription.length() < ((StringFieldMetaData) ConditionMeta.META_DESCRIPTION).getLength() ? oldDescription + 'Z' : oldDescription.substring(0, oldDescription.length() - 1);
            condition1.setDescription(newDescription);
            uow1.update(condition1);
            condition1 = Condition.findByPK(uow1, conditionCode);
            assertNotNull("Condition not re-retrieved", condition1);
            assertEquals(conditionCode, condition1.getCondition());
            assertEquals("New description", newDescription, condition1.getDescription());
            uow2 = new UOW();
            Condition condition2 = Condition.findByPK(uow2, conditionCode);
            assertNotNull("Condition not retrieved", condition2);
            assertEquals(conditionCode, condition2.getCondition());
            assertEquals("Old description", oldDescription, condition2.getDescription());
            uow1.commit();
            uow2.flush();
            condition2 = Condition.findByPK(uow2, conditionCode);
            assertNotNull("Condition not retrieved", condition2);
            assertEquals(conditionCode, condition2.getCondition());
            assertEquals("New description", newDescription, condition2.getDescription());
            condition2.setDescription(oldDescription);
            uow2.update(condition2);
            uow2.commit();
        } catch (Exception e) {
            e.printStackTrace();
            fail(e.getMessage());
        } finally {
            try {
                if (uow1 != null) uow1.rollback();
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                if (uow2 != null) uow2.rollback();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

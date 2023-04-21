package org.enerj.core;

import junit.framework.Test;
import junit.framework.TestSuite;
import org.enerj.annotations.Persist;
import org.odmg.Database;
import org.odmg.Implementation;

/**
 * Tests EnerJTransaction. This class does not repeat the basic ODMG Database tests
 * performed in BasicODMGTest.
 *
 * @version $Id: EnerJTransactionTest.java,v 1.3 2006/06/09 02:39:23 dsyrstad Exp $
 * @author <a href="mailto:dsyrstad@ener-j.org">Dan Syrstad</a>
 */
public class EnerJTransactionTest extends DatabaseTestCase {

    private Exception mThreadException;

    public EnerJTransactionTest(String aTestName) {
        super(aTestName);
    }

    public static Test suite() {
        return new TestSuite(EnerJTransactionTest.class);
    }

    /**
     * Test that a Transaction cannot be simulatenously shared between threads while it is open.
     */
    public void testNotSharableWhileOpen() throws Exception {
        Implementation impl = EnerJImplementation.getInstance();
        EnerJDatabase db = (EnerJDatabase) impl.newDatabase();
        db.open(DATABASE_URI, Database.OPEN_READ_WRITE);
        EnerJTransaction txn = (EnerJTransaction) impl.newTransaction();
        txn.begin(db);
        final EnerJTransaction txnRef = txn;
        Thread testThread = new Thread("Test") {

            public void run() {
                try {
                    txnRef.checkpoint();
                } catch (org.odmg.TransactionNotInProgressException e) {
                    mThreadException = e;
                }
            }
        };
        try {
            mThreadException = null;
            testThread.start();
            testThread.join();
            assertNotNull("Thread Exception was expected", mThreadException);
        } finally {
            txn.commit();
            db.close();
        }
    }

    /**
     * Test leave/join thread (and implicit leave via join).
     */
    public void testLeaveJoin() throws Exception {
        Implementation impl = EnerJImplementation.getInstance();
        EnerJDatabase db = (EnerJDatabase) impl.newDatabase();
        db.open(DATABASE_URI, Database.OPEN_READ_WRITE);
        EnerJTransaction txn = (EnerJTransaction) impl.newTransaction();
        txn.begin(db);
        final EnerJTransaction txnRef = txn;
        Thread testThread = new Thread("Test") {

            public void run() {
                try {
                    txnRef.join();
                    txnRef.checkpoint();
                    txnRef.leave();
                } catch (Exception e) {
                    mThreadException = e;
                    e.printStackTrace();
                }
            }
        };
        try {
            mThreadException = null;
            testThread.start();
            testThread.join();
            assertNull("Thread Exception was not expected", mThreadException);
            txn.join();
            txn.checkpoint();
        } finally {
            txn.commit();
            db.close();
        }
    }

    /**
     * Test that abort() rolls back in database.
     */
    public void testAbort() throws Exception {
        Implementation impl = EnerJImplementation.getInstance();
        EnerJDatabase db = (EnerJDatabase) impl.newDatabase();
        db.open(DATABASE_URI, Database.OPEN_READ_WRITE);
        EnerJTransaction txn = (EnerJTransaction) impl.newTransaction();
        txn.begin(db);
        db.bind(new TestClass2(null), "AbortNull");
        txn.commit();
        txn.setRestoreValues(true);
        txn.begin(db);
        try {
            TestClass2 testClass2Obj = (TestClass2) db.lookup("AbortNull");
            testClass2Obj.setValue(new TestClass1(383));
            txn.abort();
            txn.begin(db);
            testClass2Obj = (TestClass2) db.lookup("AbortNull");
            System.out.println("value=" + testClass2Obj.getValue());
            assertNull("Referenece should be null, as originally committed", testClass2Obj.getValue());
        } finally {
            if (txn.isOpen()) {
                txn.commit();
            }
        }
        txn.begin(db);
        try {
            for (int i = 0; i < 100; i++) {
                db.bind(new TestClass1(22), "Abort" + i);
            }
            txn.abort();
            txn.begin(db);
            for (int i = 0; i < 100; i++) {
                try {
                    db.lookup("Abort" + i);
                    fail("Exception Expected: " + i);
                } catch (org.odmg.ObjectNameNotFoundException e) {
                }
            }
        } finally {
            txn.commit();
            db.close();
        }
    }

    @Persist
    private static class TestClass1 {

        private int mValue;

        TestClass1(int aValue) {
            mValue = aValue;
        }

        int getValue() {
            return mValue;
        }

        void setValue(int aValue) {
            mValue = aValue;
        }
    }

    @Persist
    private static class TestClass2 {

        private TestClass1 mValue;

        TestClass2(TestClass1 aValue) {
            mValue = aValue;
        }

        TestClass1 getValue() {
            return mValue;
        }

        void setValue(TestClass1 aValue) {
            mValue = aValue;
        }
    }
}

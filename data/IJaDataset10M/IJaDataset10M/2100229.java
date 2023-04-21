package org.xorm.test;

import junit.framework.TestCase;
import java.util.logging.Logger;
import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import org.xorm.XORM;

/**
 *  This class serves as the base class for all test cases
 *  module.  Subclasses of this class will get initialized
 *  with a package level specific logger and persistence
 *  manager for use in running tests.  If desired, each 
 *  specific test case may do test case specific set up
 *  and tear down as usual.
 *
 *@author     <a href="mailto:doug@dseifert.net">Doug Seifert</a>
 */
public class BaseTestCase extends TestCase {

    protected Logger mLog;

    protected PersistenceManagerFactory mFactory;

    protected PersistenceManager mManager;

    /**
     *  Creates a Test Case of the given name.
     *
     * @param lSuite The suite this test is a member of
     * @param pName  Name of the Test Case
     */
    public BaseTestCase(String pName) {
        super(pName);
    }

    public void setLogger(Logger log) {
        mLog = log;
    }

    public void setPersistenceManagerFactory(PersistenceManagerFactory fact) {
        mFactory = fact;
    }

    /**
     * Test case specific set up.
     *@see                   junit.framework.TestCase#setup()
     */
    public void setUp() throws Exception {
        mManager = mFactory.getPersistenceManager();
        mLog.fine("setUp(): Using PM=" + mManager);
    }

    /**
     * Test case specific tear down.
     *@see                   junit.framework.TestCase#tearDown()
     */
    public void tearDown() throws Exception {
        mLog.fine("tearDown()");
    }

    /**
     *  This method wraps a fine log message around the run of the test to mark the
     *  begin and end of the test.
     *
     *@exception  Throwable  rethrow any exception from super.runTest
     */
    protected void runTest() throws Throwable {
        mLog.fine("====> START Test: " + getName());
        try {
            super.runTest();
        } finally {
            mLog.fine("====> END Test: " + getName());
        }
    }

    /** 
     * Starts a tx using the test case's persistence manager 
     */
    protected void begin() {
        mManager.currentTransaction().begin();
    }

    /** 
     * Commits an active tx 
     */
    protected void commit() {
        mManager.currentTransaction().commit();
    }

    /** 
     * Rolls back an active tx 
     */
    protected void rollback() {
        if (mManager.currentTransaction().isActive()) {
            mManager.currentTransaction().rollback();
        }
    }

    /** 
     * Lookup the PO identified by the specified class and pk value
     * from the default persistence manager.
     * 
     * @param clazz The class of the PO
     * @param id The PO's db pk value
     * @return The PO
     */
    protected Object lookupPersistentObject(Class clazz, int id) {
        return lookupPersistentObject(mManager, clazz, id);
    }

    /** 
     * Lookup the PO identified by the specified class and pk value
     * from the specified persistence manager. 
     * 
     * @param mgr The PM to use to look up the PO
     * @param clazz The class of the PO
     * @param id The PO's db pk value
     * @return The PO
     */
    protected Object lookupPersistentObject(PersistenceManager mgr, Class clazz, int id) {
        return mgr.getObjectById(XORM.newObjectId(clazz, new Integer(id)), true);
    }

    /** 
     * Create an instance of the given PO class using the default
     * persistent manager.
     * 
     * @param clazz The class of the desired PO
     * @param makePersistent Whether or not to mark the PO as persistent
     * @return Newly created instance
     */
    protected Object createPersistentObject(Class clazz, boolean makePersistent) {
        return createPersistentObject(mManager, clazz, makePersistent);
    }

    /** 
     * Create an instance of the given PO class using the specified
     * persistent manager.
     * 
     * @param mgr The PM to use to create the PO
     * @param clazz The class of the desired PO
     * @param makePersistent Whether or not to mark the PO as persistent
     * @return Newly created instance
     */
    protected Object createPersistentObject(PersistenceManager mgr, Class clazz, boolean makePersistent) {
        Object po = XORM.newInstance(mgr, clazz);
        if (makePersistent) {
            mgr.makePersistent(po);
        }
        return po;
    }

    /** 
     * Returns the po's object id as an int, as long as the object is
     * backed by a table with and integer primary key.  If the object
     * is not persistent, -1 is returned.
     * 
     * @param po The persistent object.
     * @return The po's pk as an int.
     */
    protected int getObjectIdAsInt(Object po) {
        return getObjectIdAsInt(mManager, po);
    }

    protected int getObjectIdAsInt(PersistenceManager mgr, Object po) {
        Object oid = JDOHelper.getObjectId(po);
        if (oid == null) {
            return -1;
        }
        return XORM.extractPrimaryKeyAsInt(oid);
    }
}

package org.xactor.test.dtm.test;

import javax.naming.Context;
import javax.rmi.PortableRemoteObject;
import javax.transaction.RollbackException;
import javax.transaction.UserTransaction;
import junit.extensions.TestSetup;
import junit.framework.Test;
import junit.framework.TestSuite;
import org.jboss.test.JBossTestCase;
import org.jboss.test.JBossTestSetup;
import org.xactor.test.dtm.interfaces.FrontEnd;
import org.xactor.test.dtm.interfaces.FrontEndHome;

public class T01DTMUnitTestCase extends JBossTestCase {

    public T01DTMUnitTestCase(String name) throws java.io.IOException {
        super(name);
    }

    public void testCommittedTx() throws Exception {
        getLog().debug("+++ testCommittedTx");
        Context ctx = getInitialContext();
        getLog().debug("Obtain UserTransaction instance");
        UserTransaction userTx = (UserTransaction) ctx.lookup("UserTransaction");
        getLog().debug("Obtain home interface");
        Object objref = ctx.lookup("dtmtest/FrontEndEJB");
        FrontEndHome home = (FrontEndHome) PortableRemoteObject.narrow(objref, FrontEndHome.class);
        getLog().debug("Create FrontEnd bean");
        FrontEnd frontEnd = home.create("testCommittedTx");
        getLog().debug("Set balances");
        userTx.begin();
        frontEnd.setBalances(101, 102, 103);
        userTx.commit();
        int balances[];
        getLog().debug("Get balances");
        userTx.begin();
        balances = frontEnd.getBalances();
        userTx.commit();
        assertTrue("first balance == 101", balances[0] == 101);
        assertTrue("second balance == 102", balances[1] == 102);
        assertTrue("third balance == 103", balances[2] == 103);
        getLog().debug("Update balances");
        userTx.begin();
        frontEnd.addToBalances(100);
        userTx.commit();
        getLog().debug("Check updated balances");
        userTx.begin();
        balances = frontEnd.getBalances();
        userTx.commit();
        assertTrue("first balance == 201", balances[0] == 201);
        assertTrue("second balance == 202", balances[1] == 202);
        assertTrue("third balance == 203", balances[2] == 203);
        getLog().debug("Remove FrontEnd bean");
        frontEnd.remove();
    }

    public void testRolledbackTx() throws Exception {
        getLog().debug("+++ testRolledbackTx");
        Context ctx = getInitialContext();
        getLog().debug("Obtain UserTransaction instance");
        UserTransaction userTx = (UserTransaction) ctx.lookup("UserTransaction");
        getLog().debug("Obtain home interface");
        Object objref = ctx.lookup("dtmtest/FrontEndEJB");
        FrontEndHome home = (FrontEndHome) PortableRemoteObject.narrow(objref, FrontEndHome.class);
        getLog().debug("Create FrontEnd bean");
        FrontEnd frontEnd = home.create("testRolledbackTx");
        getLog().debug("Set balances");
        userTx.begin();
        frontEnd.setBalances(101, 102, 103);
        userTx.commit();
        int balances[];
        getLog().debug("Get balances");
        userTx.begin();
        balances = frontEnd.getBalances();
        userTx.commit();
        assertTrue("first balance == 101", balances[0] == 101);
        assertTrue("second balance == 102", balances[1] == 102);
        assertTrue("third balance == 103", balances[2] == 103);
        getLog().debug("Update balances and rollback");
        userTx.begin();
        frontEnd.addToBalances(100);
        userTx.rollback();
        getLog().debug("Check that all balances remain the same");
        userTx.begin();
        balances = frontEnd.getBalances();
        userTx.commit();
        assertTrue("first balance == 101", balances[0] == 101);
        assertTrue("second balance == 102", balances[1] == 102);
        assertTrue("third balance == 103", balances[2] == 103);
        frontEnd.remove();
    }

    public void testSetRollbackOnly() throws Exception {
        getLog().debug("+++ testSetRollbackOnly");
        Context ctx = getInitialContext();
        getLog().debug("Obtain UserTransaction instance");
        UserTransaction userTx = (UserTransaction) ctx.lookup("UserTransaction");
        getLog().debug("Obtain home interface");
        Object objref = ctx.lookup("dtmtest/FrontEndEJB");
        FrontEndHome home = (FrontEndHome) PortableRemoteObject.narrow(objref, FrontEndHome.class);
        getLog().debug("Create FrontEnd bean");
        FrontEnd frontEnd = home.create("testSetRollbackOnly");
        getLog().debug("Set balances");
        userTx.begin();
        frontEnd.setBalances(101, 102, 103);
        userTx.commit();
        int balances[];
        getLog().debug("Get balances");
        userTx.begin();
        balances = frontEnd.getBalances();
        userTx.commit();
        assertTrue("first balance == 101", balances[0] == 101);
        assertTrue("second balance == 102", balances[1] == 102);
        assertTrue("third balance == 103", balances[2] == 103);
        getLog().debug("Update balances, set rollback only and try to commit");
        try {
            userTx.begin();
            frontEnd.addToBalances(100);
            userTx.setRollbackOnly();
            userTx.commit();
            fail("RollbackException should have been thrown");
        } catch (RollbackException e) {
            getLog().debug("Expected exception: " + e);
        }
        getLog().debug("Check that all balances remain the same");
        userTx.begin();
        balances = frontEnd.getBalances();
        userTx.commit();
        assertTrue("first balance == 101", balances[0] == 101);
        assertTrue("second balance == 102", balances[1] == 102);
        assertTrue("third balance == 103", balances[2] == 103);
        getLog().debug("Update balances, set rollback only and try to commit");
        try {
            userTx.begin();
            frontEnd.addToBalances(100);
            userTx.setRollbackOnly();
            frontEnd.addToBalances(50);
            userTx.commit();
            fail("RollbackException should have been thrown");
        } catch (RollbackException e) {
            getLog().debug("Expected exception: " + e);
        }
        getLog().debug("Check that all balances remain the same");
        userTx.begin();
        balances = frontEnd.getBalances();
        userTx.commit();
        assertTrue("first balance == 101", balances[0] == 101);
        assertTrue("second balance == 102", balances[1] == 102);
        assertTrue("third balance == 103", balances[2] == 103);
        frontEnd.remove();
    }

    public void testSetRollbackOnlyAtTheFrontEnd() throws Exception {
        getLog().debug("+++ testSetRollbackOnlyAtTheFrontEnd");
        Context ctx = getInitialContext();
        getLog().debug("Obtain UserTransaction instance");
        UserTransaction userTx = (UserTransaction) ctx.lookup("UserTransaction");
        getLog().debug("Obtain home interface");
        Object objref = ctx.lookup("dtmtest/FrontEndEJB");
        FrontEndHome home = (FrontEndHome) PortableRemoteObject.narrow(objref, FrontEndHome.class);
        getLog().debug("Create FrontEnd bean");
        FrontEnd frontEnd = home.create("testSetRollbackOnlyAtTheFrontEnd");
        getLog().debug("Set balances");
        userTx.begin();
        frontEnd.setBalances(101, 102, 103);
        userTx.commit();
        int balances[];
        getLog().debug("Get balances");
        userTx.begin();
        balances = frontEnd.getBalances();
        userTx.commit();
        assertTrue("first balance == 101", balances[0] == 101);
        assertTrue("second balance == 102", balances[1] == 102);
        assertTrue("third balance == 103", balances[2] == 103);
        getLog().debug("Update balances, set rollback only at the front end, " + "and try to commit");
        try {
            userTx.begin();
            frontEnd.addToBalances(100);
            frontEnd.setRollbackOnly();
            frontEnd.addToBalances(50);
            userTx.commit();
            fail("RollbackException should have been thrown");
        } catch (RollbackException e) {
            getLog().debug("Expected exception: " + e);
        }
        getLog().debug("Check that all balances remain the same");
        userTx.begin();
        balances = frontEnd.getBalances();
        userTx.commit();
        assertTrue("first balance == 101", balances[0] == 101);
        assertTrue("second balance == 102", balances[1] == 102);
        assertTrue("third balance == 103", balances[2] == 103);
        frontEnd.remove();
    }

    public void testSetRollbackOnlyAtTheFirstAccount() throws Exception {
        getLog().debug("+++ testSetRollbackOnlyAtTheFirstAccount");
        Context ctx = getInitialContext();
        getLog().debug("Obtain UserTransaction instance");
        UserTransaction userTx = (UserTransaction) ctx.lookup("UserTransaction");
        getLog().debug("Obtain home interface");
        Object objref = ctx.lookup("dtmtest/FrontEndEJB");
        FrontEndHome home = (FrontEndHome) PortableRemoteObject.narrow(objref, FrontEndHome.class);
        getLog().debug("Create FrontEnd bean");
        FrontEnd frontEnd = home.create("testSetRollbackOnlyAtTheFirstAccount");
        getLog().debug("Set balances");
        userTx.begin();
        frontEnd.setBalances(101, 102, 103);
        userTx.commit();
        int balances[];
        getLog().debug("Get balances");
        userTx.begin();
        balances = frontEnd.getBalances();
        userTx.commit();
        assertTrue("first balance == 101", balances[0] == 101);
        assertTrue("second balance == 102", balances[1] == 102);
        assertTrue("third balance == 103", balances[2] == 103);
        getLog().debug("Update balances, set rollback only at " + "the first account, and try to commit");
        try {
            userTx.begin();
            frontEnd.addToBalances(100);
            frontEnd.tellFirstAccountToSetRollbackOnly();
            frontEnd.addToBalances(50);
            userTx.commit();
            fail("RollbackException should have been thrown");
        } catch (RollbackException e) {
            getLog().debug("Expected exception: " + e);
        }
        getLog().debug("Check that all balances remain the same");
        userTx.begin();
        balances = frontEnd.getBalances();
        userTx.commit();
        assertTrue("first balance == 101", balances[0] == 101);
        assertTrue("second balance == 102", balances[1] == 102);
        assertTrue("third balance == 103", balances[2] == 103);
        frontEnd.remove();
    }

    public static Test suite() throws Exception {
        java.net.URL url = ClassLoader.getSystemResource("host0.jndi.properties");
        java.util.Properties host0JndiProps = new java.util.Properties();
        host0JndiProps.load(url.openStream());
        java.util.Properties systemProps = System.getProperties();
        systemProps.putAll(host0JndiProps);
        System.setProperties(systemProps);
        TestSuite suite = new TestSuite();
        suite.addTest(new TestSuite(T01DTMUnitTestCase.class));
        TestSetup wrapper = new JBossTestSetup(suite) {

            protected void setUp() throws Exception {
                super.setUp();
                deploy("dtmfrontend.jar");
            }

            protected void tearDown() throws Exception {
                undeploy("dtmfrontend.jar");
                super.tearDown();
            }
        };
        return wrapper;
    }
}

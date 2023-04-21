package org.springframework.transaction;

import javax.transaction.Status;
import javax.transaction.TransactionManager;
import javax.transaction.UserTransaction;
import junit.framework.TestCase;
import org.easymock.MockControl;
import org.springframework.mock.jndi.ExpectedLookupTemplate;
import org.springframework.transaction.jta.JtaTransactionManager;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.transaction.support.TransactionTemplate;

/**
 * @author Juergen Hoeller
 * @since 05.08.2005
 */
public class JndiJtaTransactionManagerTests extends TestCase {

    public void testJtaTransactionManagerWithDefaultJndiLookups1() throws Exception {
        doTestJtaTransactionManagerWithDefaultJndiLookups("java:comp/TransactionManager", true);
    }

    public void testJtaTransactionManagerWithDefaultJndiLookups2() throws Exception {
        doTestJtaTransactionManagerWithDefaultJndiLookups("java:/TransactionManager", true);
    }

    public void testJtaTransactionManagerWithDefaultJndiLookupsAndToTmFound() throws Exception {
        doTestJtaTransactionManagerWithDefaultJndiLookups("java:/tm", false);
    }

    private void doTestJtaTransactionManagerWithDefaultJndiLookups(String tmName, boolean tmFound) throws Exception {
        MockControl utControl = MockControl.createControl(UserTransaction.class);
        UserTransaction ut = (UserTransaction) utControl.getMock();
        ut.getStatus();
        utControl.setReturnValue(Status.STATUS_NO_TRANSACTION, 1);
        ut.getStatus();
        utControl.setReturnValue(Status.STATUS_ACTIVE, 1);
        ut.begin();
        utControl.setVoidCallable(1);
        ut.commit();
        utControl.setVoidCallable(1);
        utControl.replay();
        MockControl tmControl = MockControl.createControl(TransactionManager.class);
        TransactionManager tm = (TransactionManager) tmControl.getMock();
        JtaTransactionManager ptm = new JtaTransactionManager();
        ExpectedLookupTemplate jndiTemplate = new ExpectedLookupTemplate();
        jndiTemplate.addObject("java:comp/UserTransaction", ut);
        jndiTemplate.addObject(tmName, tm);
        ptm.setJndiTemplate(jndiTemplate);
        ptm.afterPropertiesSet();
        assertEquals(ut, ptm.getUserTransaction());
        if (tmFound) {
            assertEquals(tm, ptm.getTransactionManager());
        } else {
            assertNull(ptm.getTransactionManager());
        }
        TransactionTemplate tt = new TransactionTemplate(ptm);
        assertTrue(!TransactionSynchronizationManager.isSynchronizationActive());
        assertFalse(TransactionSynchronizationManager.isCurrentTransactionReadOnly());
        tt.execute(new TransactionCallbackWithoutResult() {

            protected void doInTransactionWithoutResult(TransactionStatus status) {
                assertTrue(TransactionSynchronizationManager.isSynchronizationActive());
                assertFalse(TransactionSynchronizationManager.isCurrentTransactionReadOnly());
            }
        });
        assertTrue(!TransactionSynchronizationManager.isSynchronizationActive());
        assertFalse(TransactionSynchronizationManager.isCurrentTransactionReadOnly());
        utControl.verify();
    }

    public void testJtaTransactionManagerWithCustomJndiLookups() throws Exception {
        MockControl utControl = MockControl.createControl(UserTransaction.class);
        UserTransaction ut = (UserTransaction) utControl.getMock();
        ut.getStatus();
        utControl.setReturnValue(Status.STATUS_NO_TRANSACTION, 1);
        ut.getStatus();
        utControl.setReturnValue(Status.STATUS_ACTIVE, 1);
        ut.begin();
        utControl.setVoidCallable(1);
        ut.commit();
        utControl.setVoidCallable(1);
        utControl.replay();
        MockControl tmControl = MockControl.createControl(TransactionManager.class);
        TransactionManager tm = (TransactionManager) tmControl.getMock();
        JtaTransactionManager ptm = new JtaTransactionManager();
        ptm.setUserTransactionName("jndi-ut");
        ptm.setTransactionManagerName("jndi-tm");
        ExpectedLookupTemplate jndiTemplate = new ExpectedLookupTemplate();
        jndiTemplate.addObject("jndi-ut", ut);
        jndiTemplate.addObject("jndi-tm", tm);
        ptm.setJndiTemplate(jndiTemplate);
        ptm.afterPropertiesSet();
        assertEquals(ut, ptm.getUserTransaction());
        assertEquals(tm, ptm.getTransactionManager());
        TransactionTemplate tt = new TransactionTemplate(ptm);
        assertTrue(!TransactionSynchronizationManager.isSynchronizationActive());
        assertFalse(TransactionSynchronizationManager.isCurrentTransactionReadOnly());
        tt.execute(new TransactionCallbackWithoutResult() {

            protected void doInTransactionWithoutResult(TransactionStatus status) {
                assertTrue(TransactionSynchronizationManager.isSynchronizationActive());
                assertFalse(TransactionSynchronizationManager.isCurrentTransactionReadOnly());
            }
        });
        assertTrue(!TransactionSynchronizationManager.isSynchronizationActive());
        assertFalse(TransactionSynchronizationManager.isCurrentTransactionReadOnly());
        utControl.verify();
    }

    public void testJtaTransactionManagerWithNotCacheUserTransaction() throws Exception {
        MockControl utControl = MockControl.createControl(UserTransaction.class);
        UserTransaction ut = (UserTransaction) utControl.getMock();
        ut.getStatus();
        utControl.setReturnValue(Status.STATUS_NO_TRANSACTION, 1);
        ut.getStatus();
        utControl.setReturnValue(Status.STATUS_ACTIVE, 1);
        ut.begin();
        utControl.setVoidCallable(1);
        ut.commit();
        utControl.setVoidCallable(1);
        utControl.replay();
        MockControl ut2Control = MockControl.createControl(UserTransaction.class);
        UserTransaction ut2 = (UserTransaction) ut2Control.getMock();
        ut2.getStatus();
        ut2Control.setReturnValue(Status.STATUS_NO_TRANSACTION, 1);
        ut2.getStatus();
        ut2Control.setReturnValue(Status.STATUS_ACTIVE, 1);
        ut2.begin();
        ut2Control.setVoidCallable(1);
        ut2.commit();
        ut2Control.setVoidCallable(1);
        ut2Control.replay();
        JtaTransactionManager ptm = new JtaTransactionManager();
        ptm.setJndiTemplate(new ExpectedLookupTemplate("java:comp/UserTransaction", ut));
        ptm.setCacheUserTransaction(false);
        ptm.afterPropertiesSet();
        assertEquals(ut, ptm.getUserTransaction());
        TransactionTemplate tt = new TransactionTemplate(ptm);
        assertEquals(JtaTransactionManager.SYNCHRONIZATION_ALWAYS, ptm.getTransactionSynchronization());
        assertTrue(!TransactionSynchronizationManager.isSynchronizationActive());
        assertFalse(TransactionSynchronizationManager.isCurrentTransactionReadOnly());
        tt.execute(new TransactionCallbackWithoutResult() {

            protected void doInTransactionWithoutResult(TransactionStatus status) {
                assertTrue(TransactionSynchronizationManager.isSynchronizationActive());
                assertFalse(TransactionSynchronizationManager.isCurrentTransactionReadOnly());
            }
        });
        ptm.setJndiTemplate(new ExpectedLookupTemplate("java:comp/UserTransaction", ut2));
        tt.execute(new TransactionCallbackWithoutResult() {

            protected void doInTransactionWithoutResult(TransactionStatus status) {
                assertTrue(TransactionSynchronizationManager.isSynchronizationActive());
                assertFalse(TransactionSynchronizationManager.isCurrentTransactionReadOnly());
            }
        });
        assertTrue(!TransactionSynchronizationManager.isSynchronizationActive());
        assertFalse(TransactionSynchronizationManager.isCurrentTransactionReadOnly());
        utControl.verify();
        ut2Control.verify();
    }

    /**
	 * Prevent any side-effects due to this test modifying ThreadLocals that might
	 * affect subsequent tests when all tests are run in the same JVM, as with Eclipse.
	 */
    protected void tearDown() {
        assertTrue(TransactionSynchronizationManager.getResourceMap().isEmpty());
        assertFalse(TransactionSynchronizationManager.isSynchronizationActive());
        assertNull(TransactionSynchronizationManager.getCurrentTransactionName());
        assertFalse(TransactionSynchronizationManager.isCurrentTransactionReadOnly());
        assertFalse(TransactionSynchronizationManager.isActualTransactionActive());
    }
}

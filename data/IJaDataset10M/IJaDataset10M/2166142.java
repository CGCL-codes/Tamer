package org.xactor.test.txtimer.test;

import java.util.List;
import javax.management.MBeanServerInvocationHandler;
import javax.naming.InitialContext;
import junit.extensions.TestSetup;
import junit.framework.Test;
import junit.framework.TestSuite;
import org.jboss.ejb.txtimer.DatabasePersistencePolicyMBean;
import org.jboss.test.JBossTestCase;
import org.jboss.test.JBossTestSetup;
import org.xactor.test.txtimer.interfaces.TimerTest;
import org.xactor.test.txtimer.interfaces.TimerTestHome;

/**
 * Test the Tx timer creation/cancelation for every tx setting
 * 
 * @author Dimitris.Andreadis@jboss.org
 * @version $Revision: 37991 $
 */
public class CreateCancelTestCase extends JBossTestCase {

    DatabasePersistencePolicyMBean pp;

    public CreateCancelTestCase(String name) {
        super(name);
    }

    protected void setUp() throws Exception {
        super.setUp();
        pp = (DatabasePersistencePolicyMBean) MBeanServerInvocationHandler.newProxyInstance(getServer(), DatabasePersistencePolicyMBean.OBJECT_NAME, DatabasePersistencePolicyMBean.class, false);
    }

    protected int getTimerCount() {
        List timerHandles = pp.listTimerHandles();
        return timerHandles.size();
    }

    public static Test suite() throws Exception {
        TestSuite suite = new TestSuite();
        suite.addTest(new TestSuite(CreateCancelTestCase.class));
        TestSetup wrapper = new JBossTestSetup(suite) {

            protected void setUp() throws Exception {
                super.setUp();
                deploy("ejb-txtimer.jar");
            }

            protected void tearDown() throws Exception {
                undeploy("ejb-txtimer.jar");
                super.tearDown();
            }
        };
        return wrapper;
    }

    public void testCreateRequiredCancelRequired() throws Exception {
        InitialContext iniCtx = getInitialContext();
        TimerTestHome home = (TimerTestHome) iniCtx.lookup(TimerTestHome.JNDI_NAME);
        TimerTest session = home.create();
        try {
            int initialTimerCount;
            int createdTimerCount;
            int canceledTimerCount;
            initialTimerCount = getTimerCount();
            session.startTimerInTxRequired();
            createdTimerCount = getTimerCount();
            assertEquals("Timer not created", initialTimerCount + 1, createdTimerCount);
            session.cancelTimerInTxRequired();
            canceledTimerCount = getTimerCount();
            assertEquals("Timer not canceled", createdTimerCount, canceledTimerCount + 1);
        } finally {
            session.remove();
        }
    }

    public void testCreateRequiredCancelRequiresNew() throws Exception {
        InitialContext iniCtx = getInitialContext();
        TimerTestHome home = (TimerTestHome) iniCtx.lookup(TimerTestHome.JNDI_NAME);
        TimerTest session = home.create();
        try {
            int initialTimerCount;
            int createdTimerCount;
            int canceledTimerCount;
            initialTimerCount = getTimerCount();
            session.startTimerInTxRequired();
            createdTimerCount = getTimerCount();
            assertEquals("Timer not created", initialTimerCount + 1, createdTimerCount);
            session.cancelTimerInTxRequiresNew();
            canceledTimerCount = getTimerCount();
            assertEquals("Timer not canceled", createdTimerCount, canceledTimerCount + 1);
        } finally {
            session.remove();
        }
    }

    public void testCreateRequiredCancelNotSupported() throws Exception {
        InitialContext iniCtx = getInitialContext();
        TimerTestHome home = (TimerTestHome) iniCtx.lookup(TimerTestHome.JNDI_NAME);
        TimerTest session = home.create();
        try {
            int initialTimerCount;
            int createdTimerCount;
            int canceledTimerCount;
            initialTimerCount = getTimerCount();
            session.startTimerInTxRequired();
            createdTimerCount = getTimerCount();
            assertEquals("Timer not created", initialTimerCount + 1, createdTimerCount);
            session.cancelTimerInTxNotSupported();
            canceledTimerCount = getTimerCount();
            assertEquals("Timer not canceled", createdTimerCount, canceledTimerCount + 1);
        } finally {
            session.remove();
        }
    }

    public void testCreateRequiredCancelNever() throws Exception {
        InitialContext iniCtx = getInitialContext();
        TimerTestHome home = (TimerTestHome) iniCtx.lookup(TimerTestHome.JNDI_NAME);
        TimerTest session = home.create();
        try {
            int initialTimerCount;
            int createdTimerCount;
            int canceledTimerCount;
            initialTimerCount = getTimerCount();
            session.startTimerInTxRequired();
            createdTimerCount = getTimerCount();
            assertEquals("Timer not created", initialTimerCount + 1, createdTimerCount);
            session.cancelTimerInTxNever();
            canceledTimerCount = getTimerCount();
            assertEquals("Timer not canceled", createdTimerCount, canceledTimerCount + 1);
        } finally {
            session.remove();
        }
    }

    public void testCreateRequiresNewCancelRequired() throws Exception {
        InitialContext iniCtx = getInitialContext();
        TimerTestHome home = (TimerTestHome) iniCtx.lookup(TimerTestHome.JNDI_NAME);
        TimerTest session = home.create();
        try {
            int initialTimerCount;
            int createdTimerCount;
            int canceledTimerCount;
            initialTimerCount = getTimerCount();
            session.startTimerInTxRequiresNew();
            createdTimerCount = getTimerCount();
            assertEquals("Timer not created", initialTimerCount + 1, createdTimerCount);
            session.cancelTimerInTxRequired();
            canceledTimerCount = getTimerCount();
            assertEquals("Timer not canceled", createdTimerCount, canceledTimerCount + 1);
        } finally {
            session.remove();
        }
    }

    public void testCreateRequiresNewCancelRequiresNew() throws Exception {
        InitialContext iniCtx = getInitialContext();
        TimerTestHome home = (TimerTestHome) iniCtx.lookup(TimerTestHome.JNDI_NAME);
        TimerTest session = home.create();
        try {
            int initialTimerCount;
            int createdTimerCount;
            int canceledTimerCount;
            initialTimerCount = getTimerCount();
            session.startTimerInTxRequiresNew();
            createdTimerCount = getTimerCount();
            assertEquals("Timer not created", initialTimerCount + 1, createdTimerCount);
            session.cancelTimerInTxRequiresNew();
            canceledTimerCount = getTimerCount();
            assertEquals("Timer not canceled", createdTimerCount, canceledTimerCount + 1);
        } finally {
            session.remove();
        }
    }

    public void testCreateRequiresNewCancelNotSupported() throws Exception {
        InitialContext iniCtx = getInitialContext();
        TimerTestHome home = (TimerTestHome) iniCtx.lookup(TimerTestHome.JNDI_NAME);
        TimerTest session = home.create();
        try {
            int initialTimerCount;
            int createdTimerCount;
            int canceledTimerCount;
            initialTimerCount = getTimerCount();
            session.startTimerInTxRequiresNew();
            createdTimerCount = getTimerCount();
            assertEquals("Timer not created", initialTimerCount + 1, createdTimerCount);
            session.cancelTimerInTxNotSupported();
            canceledTimerCount = getTimerCount();
            assertEquals("Timer not canceled", createdTimerCount, canceledTimerCount + 1);
        } finally {
            session.remove();
        }
    }

    public void testCreateRequiresNewCancelNever() throws Exception {
        InitialContext iniCtx = getInitialContext();
        TimerTestHome home = (TimerTestHome) iniCtx.lookup(TimerTestHome.JNDI_NAME);
        TimerTest session = home.create();
        try {
            int initialTimerCount;
            int createdTimerCount;
            int canceledTimerCount;
            initialTimerCount = getTimerCount();
            session.startTimerInTxRequiresNew();
            createdTimerCount = getTimerCount();
            assertEquals("Timer not created", initialTimerCount + 1, createdTimerCount);
            session.cancelTimerInTxNever();
            canceledTimerCount = getTimerCount();
            assertEquals("Timer not canceled", createdTimerCount, canceledTimerCount + 1);
        } finally {
            session.remove();
        }
    }

    public void testCreateNotSupportedCancelRequired() throws Exception {
        InitialContext iniCtx = getInitialContext();
        TimerTestHome home = (TimerTestHome) iniCtx.lookup(TimerTestHome.JNDI_NAME);
        TimerTest session = home.create();
        try {
            int initialTimerCount;
            int createdTimerCount;
            int canceledTimerCount;
            initialTimerCount = getTimerCount();
            session.startTimerInTxNotSupported();
            createdTimerCount = getTimerCount();
            assertEquals("Timer not created", initialTimerCount + 1, createdTimerCount);
            session.cancelTimerInTxRequired();
            canceledTimerCount = getTimerCount();
            assertEquals("Timer not canceled", createdTimerCount, canceledTimerCount + 1);
        } finally {
            session.remove();
        }
    }

    public void testCreateNotSupportedCancelRequiresNew() throws Exception {
        InitialContext iniCtx = getInitialContext();
        TimerTestHome home = (TimerTestHome) iniCtx.lookup(TimerTestHome.JNDI_NAME);
        TimerTest session = home.create();
        try {
            int initialTimerCount;
            int createdTimerCount;
            int canceledTimerCount;
            initialTimerCount = getTimerCount();
            session.startTimerInTxNotSupported();
            createdTimerCount = getTimerCount();
            assertEquals("Timer not created", initialTimerCount + 1, createdTimerCount);
            session.cancelTimerInTxRequiresNew();
            canceledTimerCount = getTimerCount();
            assertEquals("Timer not canceled", createdTimerCount, canceledTimerCount + 1);
        } finally {
            session.remove();
        }
    }

    public void testCreateNotSupportedCancelNotSupported() throws Exception {
        InitialContext iniCtx = getInitialContext();
        TimerTestHome home = (TimerTestHome) iniCtx.lookup(TimerTestHome.JNDI_NAME);
        TimerTest session = home.create();
        try {
            int initialTimerCount;
            int createdTimerCount;
            int canceledTimerCount;
            initialTimerCount = getTimerCount();
            session.startTimerInTxNotSupported();
            createdTimerCount = getTimerCount();
            assertEquals("Timer not created", initialTimerCount + 1, createdTimerCount);
            session.cancelTimerInTxNotSupported();
            canceledTimerCount = getTimerCount();
            assertEquals("Timer not canceled", createdTimerCount, canceledTimerCount + 1);
        } finally {
            session.remove();
        }
    }

    public void testCreateNotSupportedCancelNever() throws Exception {
        InitialContext iniCtx = getInitialContext();
        TimerTestHome home = (TimerTestHome) iniCtx.lookup(TimerTestHome.JNDI_NAME);
        TimerTest session = home.create();
        try {
            int initialTimerCount;
            int createdTimerCount;
            int canceledTimerCount;
            initialTimerCount = getTimerCount();
            session.startTimerInTxNotSupported();
            createdTimerCount = getTimerCount();
            assertEquals("Timer not created", initialTimerCount + 1, createdTimerCount);
            session.cancelTimerInTxNever();
            canceledTimerCount = getTimerCount();
            assertEquals("Timer not canceled", createdTimerCount, canceledTimerCount + 1);
        } finally {
            session.remove();
        }
    }

    public void testCreateNeverCancelRequired() throws Exception {
        InitialContext iniCtx = getInitialContext();
        TimerTestHome home = (TimerTestHome) iniCtx.lookup(TimerTestHome.JNDI_NAME);
        TimerTest session = home.create();
        try {
            int initialTimerCount;
            int createdTimerCount;
            int canceledTimerCount;
            initialTimerCount = getTimerCount();
            session.startTimerInTxNever();
            createdTimerCount = getTimerCount();
            assertEquals("Timer not created", initialTimerCount + 1, createdTimerCount);
            session.cancelTimerInTxRequired();
            canceledTimerCount = getTimerCount();
            assertEquals("Timer not canceled", createdTimerCount, canceledTimerCount + 1);
        } finally {
            session.remove();
        }
    }

    public void testCreateNeverCancelRequiresNew() throws Exception {
        InitialContext iniCtx = getInitialContext();
        TimerTestHome home = (TimerTestHome) iniCtx.lookup(TimerTestHome.JNDI_NAME);
        TimerTest session = home.create();
        try {
            int initialTimerCount;
            int createdTimerCount;
            int canceledTimerCount;
            initialTimerCount = getTimerCount();
            session.startTimerInTxNever();
            createdTimerCount = getTimerCount();
            assertEquals("Timer not created", initialTimerCount + 1, createdTimerCount);
            session.cancelTimerInTxRequiresNew();
            canceledTimerCount = getTimerCount();
            assertEquals("Timer not canceled", createdTimerCount, canceledTimerCount + 1);
        } finally {
            session.remove();
        }
    }

    public void testCreateNeverCancelNotSupported() throws Exception {
        InitialContext iniCtx = getInitialContext();
        TimerTestHome home = (TimerTestHome) iniCtx.lookup(TimerTestHome.JNDI_NAME);
        TimerTest session = home.create();
        try {
            int initialTimerCount;
            int createdTimerCount;
            int canceledTimerCount;
            initialTimerCount = getTimerCount();
            session.startTimerInTxNever();
            createdTimerCount = getTimerCount();
            assertEquals("Timer not created", initialTimerCount + 1, createdTimerCount);
            session.cancelTimerInTxNotSupported();
            canceledTimerCount = getTimerCount();
            assertEquals("Timer not canceled", createdTimerCount, canceledTimerCount + 1);
        } finally {
            session.remove();
        }
    }

    public void testCreateNeverCancelNever() throws Exception {
        InitialContext iniCtx = getInitialContext();
        TimerTestHome home = (TimerTestHome) iniCtx.lookup(TimerTestHome.JNDI_NAME);
        TimerTest session = home.create();
        try {
            int initialTimerCount;
            int createdTimerCount;
            int canceledTimerCount;
            initialTimerCount = getTimerCount();
            session.startTimerInTxNever();
            createdTimerCount = getTimerCount();
            assertEquals("Timer not created", initialTimerCount + 1, createdTimerCount);
            session.cancelTimerInTxNever();
            canceledTimerCount = getTimerCount();
            assertEquals("Timer not canceled", createdTimerCount, canceledTimerCount + 1);
        } finally {
            session.remove();
        }
    }
}

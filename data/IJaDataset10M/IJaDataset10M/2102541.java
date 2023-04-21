package org.xactor.test.recover.test;

import javax.management.Attribute;
import javax.management.MBeanServerConnection;
import javax.management.ObjectName;
import javax.rmi.PortableRemoteObject;
import javax.transaction.TransactionRolledbackException;
import junit.framework.Test;
import org.jboss.logging.Logger;
import org.xactor.test.recover.interfaces.DummyXAResource;
import org.xactor.test.recover.interfaces.XAResourceEnlisterCaller;
import org.xactor.test.recover.interfaces.XAResourceEnlisterCallerHome;

/**
 * A CrashOfFirstRemoteResourceBeforeItAnswersPrepareSOAPTestCase.
 * 
 * @author <a href="reverbel@ime.usp.br">Francisco Reverbel</a>
 * @author <a href="mailto:ivanneto@gmail.com">Ivan Neto</a>
 * @version $Revision: 37406 $
 */
public class CrashOfFirstRemoteResourceBeforeItAnswersPrepareSOAPTestCase extends JBossCrashRecoveryTestCase {

    private Logger log = Logger.getLogger(this.getClass());

    public CrashOfFirstRemoteResourceBeforeItAnswersPrepareSOAPTestCase(String name) throws Exception {
        super(name);
    }

    public void test() throws Exception {
        log.info("*** starting " + getUnqualifiedClassName() + " ***");
        DummyXAResource xaRes1 = getXAResource("DummyRecoverableProxy1");
        DummyXAResource xaRes2 = getXAResource("DummyRecoverableProxy2");
        DummyXAResource xaRes3 = getXAResource("DummyRecoverableProxy3");
        xaRes1.clear();
        xaRes2.clear();
        xaRes3.clear();
        Object obj;
        XAResourceEnlisterCallerHome home;
        obj = getInitialContext().lookup("dtmrectest/XAResourceEnlisterCallerEJB");
        home = (XAResourceEnlisterCallerHome) PortableRemoteObject.narrow(obj, XAResourceEnlisterCallerHome.class);
        XAResourceEnlisterCaller xaResEnlisterCaller = home.create();
        for (int i = 0; i < N; i++) xaResEnlisterCaller.callXAResourceEnlistersOverSOAP();
        int xa1CommitsBefore = xaRes1.getCommittedCount();
        int xa2CommitsBefore = xaRes2.getCommittedCount();
        int xa3CommitsBefore = xaRes3.getCommittedCount();
        log.info("Commits before crash of remote resource: " + xa1CommitsBefore + ", " + xa2CommitsBefore + ", " + xa3CommitsBefore);
        ObjectName logger = new ObjectName("xactor:service=RecoveryLogger");
        MBeanServerConnection firstResource = getResource1Server();
        try {
            firstResource.setAttribute(logger, new Attribute("CrashOnFailure", Boolean.TRUE));
            firstResource.setAttribute(logger, new Attribute("FailAfterPreparing", Boolean.TRUE));
            firstResource.setAttribute(logger, new Attribute("FailAfter", new Integer(0)));
            xaResEnlisterCaller.callXAResourceEnlistersOverSOAP();
            fail("TransactionRolledbackException expected.");
        } catch (TransactionRolledbackException e) {
            log.info("Expected exception: ", e);
        } catch (Throwable e) {
            log.info("Unexpected throwable:", e);
            fail("Unexpected throwable: " + e);
        }
        int xa1CommitsAfter = xaRes1.getCommittedCount();
        int xa2CommitsAfter = xaRes2.getCommittedCount();
        int xa3CommitsAfter = xaRes3.getCommittedCount();
        int xa1Rollbacks = xaRes1.getRollbackCount();
        int xa2Rollbacks = xaRes2.getRollbackCount();
        int xa3Rollbacks = xaRes3.getRollbackCount();
        log.info("Commits after crash of remote resource: " + xa1CommitsAfter + ", " + xa2CommitsAfter + ", " + xa3CommitsAfter);
        log.info("Rollbacks after crash of remote resource: " + xa1CommitsAfter + ", " + xa2CommitsAfter + ", " + xa3CommitsAfter);
        assertEquals(3 * N, xa1CommitsBefore);
        assertEquals(3 * N, xa2CommitsBefore);
        assertEquals(2 * N, xa3CommitsBefore);
        assertEquals(3 * N, xa1CommitsAfter);
        assertEquals(3 * N, xa2CommitsAfter);
        assertEquals(2 * N, xa3CommitsAfter);
        assertEquals(2, xa1Rollbacks);
        assertEquals(2, xa2Rollbacks);
        assertEquals(1, xa3Rollbacks);
    }

    public static Test suite() throws Exception {
        return suite(CrashOfFirstRemoteResourceBeforeItAnswersPrepareSOAPTestCase.class);
    }
}

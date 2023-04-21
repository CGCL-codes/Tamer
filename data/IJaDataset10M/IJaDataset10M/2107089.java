package org.xactor.test.recover.test;

import javax.rmi.PortableRemoteObject;
import junit.framework.Test;
import org.jboss.logging.Logger;
import org.xactor.tm.recovery.RecoveryTestingException;
import org.xactor.test.recover.interfaces.DummyXAResource;
import org.xactor.test.recover.interfaces.XAResourceEnlisterCaller;
import org.xactor.test.recover.interfaces.XAResourceEnlisterCallerHome;

/**
 * A CrashOfFirstRemoteResourceAfterPrepareJBRemSOAPIIOPTestCase.
 * 
 * @author <a href="reverbel@ime.usp.br">Francisco Reverbel</a>
 * @author <a href="mailto:ivanneto@gmail.com">Ivan Neto</a>
 * @version $Revision: 37406 $
 */
public class CrashOfFirstRemoteResourceAfterPrepareJBRemSOAPIIOPTestCase extends JBossCrashRecoveryTestCase {

    private Logger log = Logger.getLogger(this.getClass());

    public CrashOfFirstRemoteResourceAfterPrepareJBRemSOAPIIOPTestCase(String name) throws Exception {
        super(name);
    }

    public void test() throws Exception {
        log.info("*** starting " + getUnqualifiedClassName() + " ***");
        DummyXAResource xaRes1 = getXAResource("DummyRecoverableProxy1");
        DummyXAResource xaRes2 = getXAResource("DummyRecoverableProxy2");
        DummyXAResource xaRes3 = getXAResource("DummyRecoverableProxy3");
        DummyXAResource xaRes4 = getXAResource("DummyRecoverableProxy4");
        xaRes1.clear();
        xaRes2.clear();
        xaRes3.clear();
        xaRes4.clear();
        Object obj;
        XAResourceEnlisterCallerHome home;
        obj = getInitialContext().lookup("dtmrectest/XAResourceEnlisterCallerEJB");
        home = (XAResourceEnlisterCallerHome) PortableRemoteObject.narrow(obj, XAResourceEnlisterCallerHome.class);
        XAResourceEnlisterCaller xaResEnlisterCaller = home.create();
        for (int i = 0; i < N; i++) xaResEnlisterCaller.enlistOneXAResourcePerAppServerOverJBRemAndSOAPAndIIOP();
        int xa1CommitsBefore = xaRes1.getCommittedCount();
        int xa2CommitsBefore = xaRes2.getCommittedCount();
        int xa3CommitsBefore = xaRes3.getCommittedCount();
        int xa4CommitsBefore = xaRes4.getCommittedCount();
        log.info("Commits before crash of remote resource: " + xa1CommitsBefore + ", " + xa2CommitsBefore + ", " + xa3CommitsBefore + ", " + xa4CommitsBefore);
        try {
            xaRes2.setCommitException(new RecoveryTestingException("FAILURE WHEN XA2 WAS ABOUT TO COMMIT"));
            xaResEnlisterCaller.enlistOneXAResourcePerAppServerOverJBRemAndSOAPAndIIOP();
            xaRes2.clearCommitException();
        } catch (Throwable e) {
            log.info("Unexpected throwable:", e);
            fail("Unexpected throwable: " + e);
        }
        int xa1CommitsAfter = xaRes1.getCommittedCount();
        int xa2CommitsAfter = xaRes2.getCommittedCount();
        int xa3CommitsAfter = xaRes3.getCommittedCount();
        int xa4CommitsAfter = xaRes4.getCommittedCount();
        int xa1Rollbacks = xaRes1.getRollbackCount();
        int xa2Rollbacks = xaRes2.getRollbackCount();
        int xa3Rollbacks = xaRes3.getRollbackCount();
        int xa4Rollbacks = xaRes4.getRollbackCount();
        log.info("Commits after crash of remote resource: " + xa1CommitsAfter + ", " + xa2CommitsAfter + ", " + xa3CommitsAfter + ", " + xa4CommitsAfter);
        log.info("Rollbacks after crash of remote resource: " + xa1Rollbacks + ", " + xa2Rollbacks + ", " + xa3Rollbacks + ", " + xa4Rollbacks);
        assertEquals(N, xa1CommitsBefore);
        assertEquals(N, xa2CommitsBefore);
        assertEquals(N, xa3CommitsBefore);
        assertEquals(N, xa4CommitsBefore);
        assertEquals(N + 1, xa1CommitsAfter);
        assertEquals(N, xa2CommitsAfter);
        assertEquals(N + 1, xa3CommitsAfter);
        assertEquals(N + 1, xa4CommitsAfter);
        assertEquals(0, xa1Rollbacks);
        assertEquals(0, xa2Rollbacks);
        assertEquals(0, xa3Rollbacks);
        assertEquals(0, xa4Rollbacks);
    }

    public static Test suite() throws Exception {
        return suite(CrashOfFirstRemoteResourceAfterPrepareJBRemSOAPIIOPTestCase.class);
    }
}

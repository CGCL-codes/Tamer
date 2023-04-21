package org.xactor.test.recover.test;

import javax.management.Attribute;
import javax.management.ObjectName;
import javax.rmi.PortableRemoteObject;
import junit.framework.Test;
import org.jboss.logging.Logger;
import org.xactor.test.recover.interfaces.DummyXAResource;
import org.xactor.test.recover.interfaces.XAResourceEnlisterCaller;
import org.xactor.test.recover.interfaces.XAResourceEnlisterCallerHome;

/**
 * A CrashAfterDistributedTxCommittedJBRemIIOPSOAPTestCase.
 * 
 * @author <a href="reverbel@ime.usp.br">Francisco Reverbel</a>
 * @author <a href="mailto:ivanneto@gmail.com">Ivan Neto</a>
 * @version $Revision: 37854 $
 */
public class CrashAfterDistributedTxCommittedJBRemIIOPSOAPTestCase extends JBossCrashRecoveryTestCase {

    private Logger log = Logger.getLogger(this.getClass());

    public CrashAfterDistributedTxCommittedJBRemIIOPSOAPTestCase(String name) throws Exception {
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
        for (int i = 0; i < N; i++) xaResEnlisterCaller.callXAResourceEnlistersOverJBRemAndIIOPAndSOAP();
        int xa1CommitsBefore = xaRes1.getCommittedCount();
        int xa2CommitsBefore = xaRes2.getCommittedCount();
        int xa3CommitsBefore = xaRes3.getCommittedCount();
        log.info("Commits before crash: " + xa1CommitsBefore + ", " + xa2CommitsBefore + ", " + xa3CommitsBefore);
        ObjectName logger = new ObjectName("xactor:service=RecoveryLogger");
        try {
            server.setAttribute(logger, new Attribute("CrashOnFailure", Boolean.TRUE));
            server.setAttribute(logger, new Attribute("FailAfterCommitting", Boolean.TRUE));
            server.setAttribute(logger, new Attribute("FailAfter", new Integer(0)));
            xaResEnlisterCaller.callXAResourceEnlistersOverJBRemAndIIOPAndSOAP();
            fail("Server crash expected.");
        } catch (Throwable e) {
            if (isExpectedThrowable(e)) {
                log.info("Expected throwable:", e);
            } else {
                log.info("Unexpected throwable:", e);
                fail("Unexpected throwable: " + e);
            }
        }
        int xa1CommitsAfter = xaRes1.getCommittedCount();
        int xa2CommitsAfter = xaRes2.getCommittedCount();
        int xa3CommitsAfter = xaRes3.getCommittedCount();
        log.info("Commits after crash: " + xa1CommitsAfter + ", " + xa2CommitsAfter + ", " + xa3CommitsAfter);
        assertEquals(4 * N, xa1CommitsBefore);
        assertEquals(4 * N, xa2CommitsBefore);
        assertEquals(3 * N, xa3CommitsBefore);
        assertEquals(4 * N, xa1CommitsAfter);
        assertEquals(4 * N, xa2CommitsAfter);
        assertEquals(3 * N, xa3CommitsAfter);
    }

    public static Test suite() throws Exception {
        return suite(CrashAfterDistributedTxCommittedJBRemIIOPSOAPTestCase.class);
    }
}

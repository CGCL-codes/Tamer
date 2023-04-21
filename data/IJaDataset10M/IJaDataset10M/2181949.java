package org.xactor.test.recover.test;

import junit.framework.Test;
import org.jboss.logging.Logger;
import org.xactor.test.recover.interfaces.DummyXAResource;

/**
 * A RecoveryFromCrashAfterOneResourceButNotAllHavePreparedTestCase.
 * 
 * @author <a href="reverbel@ime.usp.br">Francisco Reverbel</a>
 * @version $Revision: 37406 $
 */
public class RecoveryFromCrashAfterOneResourceButNotAllHavePreparedTestCase extends JBossCrashRecoveryTestCase {

    private Logger log = Logger.getLogger(this.getClass());

    public RecoveryFromCrashAfterOneResourceButNotAllHavePreparedTestCase(String name) {
        super(name);
    }

    public void test() throws Exception {
        log.info("*** starting " + getUnqualifiedClassName() + " ***");
        DummyXAResource xaRes1 = getXAResource("DummyRecoverableProxy1");
        DummyXAResource xaRes2 = getXAResource("DummyRecoverableProxy2");
        DummyXAResource xaRes3 = getXAResource("DummyRecoverableProxy3");
        int xa1Commits = xaRes1.getCommittedCount();
        int xa2Commits = xaRes2.getCommittedCount();
        int xa3Commits = xaRes3.getCommittedCount();
        int xa1Prepares = xaRes1.getPreparedCount();
        int xa2Prepares = xaRes2.getPreparedCount();
        int xa3Prepares = xaRes3.getPreparedCount();
        int xa1Rollbacks = xaRes1.getRollbackCount();
        int xa2Rollbacks = xaRes2.getRollbackCount();
        int xa3Rollbacks = xaRes3.getRollbackCount();
        log.info("Commits after crash: " + xa1Commits + ", " + xa2Commits + ", " + xa3Commits);
        log.info("Prepares after crash: " + xa1Prepares + ", " + xa2Prepares + ", " + xa3Prepares);
        log.info("Rollbacks after crash: " + xa1Rollbacks + ", " + xa2Rollbacks + ", " + xa3Rollbacks);
        assertEquals(N, xa1Commits);
        assertEquals(N, xa2Commits);
        assertEquals(N, xa3Commits);
        assertEquals(N + 1, xa1Prepares);
        assertEquals(N, xa2Prepares);
        assertEquals(N, xa3Prepares);
        assertEquals(1, xa1Rollbacks);
        assertEquals(0, xa2Rollbacks);
        assertEquals(0, xa3Rollbacks);
    }

    public static Test suite() throws Exception {
        return suite(RecoveryFromCrashAfterOneResourceButNotAllHavePreparedTestCase.class);
    }
}

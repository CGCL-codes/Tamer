package org.xactor.test.recover.test;

import javax.naming.Context;
import javax.rmi.PortableRemoteObject;
import javax.transaction.UserTransaction;
import junit.framework.Test;
import org.xactor.tm.recovery.RecoveryTestingException;
import org.xactor.test.recover.interfaces.DummyXAResource;
import org.xactor.test.recover.interfaces.FrontEnd;
import org.xactor.test.recover.interfaces.FrontEndHome;
import org.xactor.test.recover.interfaces.XAResourceEnlisterCaller;
import org.xactor.test.recover.interfaces.XAResourceEnlisterCallerHome;

/**
 * A CrashOfSecondRemoteResourceWithXADatabaseAfterPrepareSOAPTestCase.
 * 
 * @author <a href="reverbel@ime.usp.br">Francisco Reverbel</a>
 * @author <a href="mailto:ivanneto@gmail.com">Ivan Neto</a>
 * @version $Revision: 37406 $
 */
public class CrashOfSecondRemoteResourceWithXADatabaseAfterPrepareSOAPTestCase extends JBossCrashRecoveryTestCase {

    public CrashOfSecondRemoteResourceWithXADatabaseAfterPrepareSOAPTestCase(String name) throws java.io.IOException {
        super(name);
    }

    public void test() throws Exception {
        getLog().info("*** starting " + getUnqualifiedClassName() + " ***");
        Context ctx = getInitialContext();
        DummyXAResource xaRes1 = getXAResource("DummyRecoverableProxy1");
        DummyXAResource xaRes2 = getXAResource("DummyRecoverableProxy2");
        DummyXAResource xaRes3 = getXAResource("DummyRecoverableProxy3");
        xaRes1.clear();
        xaRes2.clear();
        xaRes3.clear();
        Object obj;
        getLog().debug("Obtain FrontEnd home interface");
        obj = ctx.lookup("dtmrecoverytest/FrontEndEJB");
        FrontEndHome frontEndHome = (FrontEndHome) PortableRemoteObject.narrow(obj, FrontEndHome.class);
        getLog().debug("Create FrontEnd bean");
        FrontEnd frontEnd = frontEndHome.create();
        getLog().debug("Obtain XAResourceEnlisterCaller home interface");
        obj = ctx.lookup("dtmrectest/XAResourceEnlisterCallerEJB");
        XAResourceEnlisterCallerHome xaResEnlisterCallerHome = (XAResourceEnlisterCallerHome) PortableRemoteObject.narrow(obj, XAResourceEnlisterCallerHome.class);
        getLog().debug("Create XAResourceEnlisterCaller bean");
        XAResourceEnlisterCaller xaResEnlisterCaller = xaResEnlisterCallerHome.create();
        getLog().debug("Obtain UserTransaction instance");
        UserTransaction userTx = (UserTransaction) ctx.lookup("UserTransaction");
        getLog().debug("Set balances");
        userTx.begin();
        frontEnd.setBalancesOverSOAP(101, 102);
        userTx.commit();
        int[] balances;
        getLog().debug("Get balances");
        userTx.begin();
        balances = frontEnd.getBalancesOverSOAP();
        userTx.commit();
        assertTrue("first balance == 101", balances[0] == 101);
        assertTrue("second balance == 102", balances[1] == 102);
        getLog().debug("Update balances");
        xaRes3.setCommitException(new RecoveryTestingException("FAILURE WHEN XA2 WAS ABOUT TO COMMIT"));
        try {
            userTx.begin();
            xaResEnlisterCaller.enlistOneXAResourcePerAppServerOverSOAP();
            frontEnd.addToBalancesOverSOAP(100);
            userTx.commit();
        } finally {
            xaRes3.clearCommitException();
        }
    }

    public static Test suite() throws Exception {
        return suite(CrashOfSecondRemoteResourceWithXADatabaseAfterPrepareSOAPTestCase.class);
    }
}

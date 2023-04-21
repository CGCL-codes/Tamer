package org.xactor.test.recover.test;

import javax.naming.Context;
import javax.rmi.PortableRemoteObject;
import javax.transaction.UserTransaction;
import junit.framework.Test;
import org.xactor.test.recover.interfaces.FrontEnd;
import org.xactor.test.recover.interfaces.FrontEndHome;

/**
 * A RecoveryFromCrashOfThirdRemoteResourceWithXADatabaseAfterPrepareSOAPJBRemIIOPTestCase.
 * 
 * @author <a href="reverbel@ime.usp.br">Francisco Reverbel</a>
 * @author <a href="mailto:ivanneto@gmail.com">Ivan Neto</a>
 * @version $Revision: 37406 $
 */
public class RecoveryFromCrashOfThirdRemoteResourceWithXADatabaseAfterPrepareSOAPJBRemIIOPTestCase extends JBossCrashRecoveryTestCase {

    public RecoveryFromCrashOfThirdRemoteResourceWithXADatabaseAfterPrepareSOAPJBRemIIOPTestCase(String name) {
        super(name);
    }

    public void test() throws Exception {
        log.info("*** starting " + getUnqualifiedClassName() + " ***");
        Context ctx = getInitialContext();
        getLog().debug("Obtain UserTransaction instance");
        UserTransaction userTx = (UserTransaction) ctx.lookup("UserTransaction");
        getLog().debug("Obtain home interface");
        Object objref = ctx.lookup("dtmrecoverytest/FrontEndEJB");
        FrontEndHome home = (FrontEndHome) PortableRemoteObject.narrow(objref, FrontEndHome.class);
        getLog().debug("Create FrontEnd bean");
        FrontEnd frontEnd = home.create();
        getLog().debug("Check updated balances");
        userTx.begin();
        int[] balances = frontEnd.getBalancesOverSOAPAndJBRemAndIIOP();
        userTx.commit();
        assertTrue("first balance == 201", balances[0] == 201);
        assertTrue("second balance == 202", balances[1] == 202);
        assertTrue("third balance == 203", balances[2] == 203);
        getLog().debug("Remove FrontEnd bean");
        frontEnd.remove();
    }

    public static Test suite() throws Exception {
        return suite(RecoveryFromCrashOfThirdRemoteResourceWithXADatabaseAfterPrepareSOAPJBRemIIOPTestCase.class);
    }
}

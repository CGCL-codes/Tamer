package org.signserver.mailsigner.core;

import java.rmi.RemoteException;
import java.util.Properties;
import junit.framework.TestCase;
import org.signserver.common.GlobalConfiguration;
import org.signserver.common.InvalidWorkerIdException;
import org.signserver.common.MailSignerConfig;
import org.signserver.common.MailSignerStatus;
import org.signserver.common.SignServerUtil;
import org.signserver.common.CryptoTokenAuthenticationFailureException;
import org.signserver.common.CryptoTokenOfflineException;
import org.signserver.common.SignerStatus;
import org.signserver.common.WorkerConfig;
import org.signserver.common.WorkerStatus;
import org.signserver.server.PropertyFileStore;
import org.signserver.server.cryptotokens.ICryptoToken;

public class TestMailSignerContainerMailet extends TestCase {

    private static MailSignerContainerMailet mc = null;

    protected void setUp() throws Exception {
        super.setUp();
        SignServerUtil.installBCProvider();
        PropertyFileStore.getInstance("tmp/testproperties.properties");
        mc = new MailSignerContainerMailet();
    }

    public void test00SetupDatabase() throws Exception {
        mc.setGlobalProperty(GlobalConfiguration.SCOPE_GLOBAL, "WORKER3.CLASSPATH", "org.signserver.mailsigner.mailsigners.DummyMailSigner");
        mc.setGlobalProperty(GlobalConfiguration.SCOPE_GLOBAL, "WORKER3.CRYPTOTOKEN.CLASSPATH", "org.signserver.server.cryptotokens.HardCodedCryptoToken");
        mc.setWorkerProperty(3, "NAME", "testWorker");
        mc.reloadConfiguration(3);
    }

    public void testActivateSigner() throws RemoteException, CryptoTokenOfflineException, InvalidWorkerIdException {
        try {
            mc.activateCryptoToken(3, "9876");
            fail();
        } catch (CryptoTokenAuthenticationFailureException e) {
        }
    }

    public void testDeactivateSigner() throws RemoteException, CryptoTokenOfflineException, InvalidWorkerIdException {
        assertTrue(mc.deactivateCryptoToken(3));
    }

    public void testDestroyKey() throws RemoteException, InvalidWorkerIdException {
        assertTrue(mc.destroyKey(3, ICryptoToken.PURPOSE_SIGN));
    }

    public void testGenCertificateRequest() throws RemoteException, CryptoTokenOfflineException, InvalidWorkerIdException {
        assertNull(mc.genCertificateRequest(3, null));
    }

    public void testGetSignerId() throws RemoteException {
        int id = mc.getWorkerId("testWorker");
        assertTrue("" + id, id == 3);
    }

    public void testGetStatus() throws RemoteException, InvalidWorkerIdException {
        assertTrue(((MailSignerStatus) mc.getStatus(3)).getTokenStatus() == SignerStatus.STATUS_ACTIVE || ((MailSignerStatus) mc.getStatus(3)).getTokenStatus() == SignerStatus.STATUS_OFFLINE);
    }

    public void testReloadConfiguration() throws RemoteException {
        mc.reloadConfiguration(0);
    }

    public void testGetCurrentSignerConfig() throws RemoteException, InvalidWorkerIdException {
        mc.removeWorkerProperty(3, "TESTKEY");
        WorkerStatus ws = mc.getStatus(3);
        assertNull(ws.getActiveSignerConfig().getProperties().getProperty("TESTKEY"));
        mc.setWorkerProperty(3, "TESTKEY", "TESTVAL");
        WorkerConfig wc = mc.getCurrentWorkerConfig(3);
        MailSignerConfig msc = new MailSignerConfig(wc);
        assertTrue(msc.getWorkerConfig().getProperties().getProperty("TESTKEY").equals("TESTVAL"));
        ws = mc.getStatus(3);
        assertNull(ws.getActiveSignerConfig().getProperties().getProperty("TESTKEY"));
        mc.reloadConfiguration(3);
        ws = mc.getStatus(3);
        assertNotNull(ws.getActiveSignerConfig().getProperties().getProperty("TESTKEY"));
        mc.removeWorkerProperty(3, "TESTKEY");
    }

    public void testSetWorkerProperty() {
        mc.setWorkerProperty(3, "test", "Hello World");
        Properties props = mc.getCurrentWorkerConfig(3).getProperties();
        assertTrue(props.getProperty("TEST").equals("Hello World"));
    }

    public void testRemoveWorkerProperty() {
        mc.removeWorkerProperty(3, "test");
        Properties props = mc.getCurrentWorkerConfig(3).getProperties();
        assertNull(props.getProperty("test"));
    }

    public void test99TearDownDatabase() throws Exception {
        mc.removeWorkerProperty(3, "NAME");
        mc.removeGlobalProperty(GlobalConfiguration.SCOPE_GLOBAL, "WORKER3.CLASSPATH");
        mc.removeGlobalProperty(GlobalConfiguration.SCOPE_GLOBAL, "WORKER3.SIGNERTOKEN.CLASSPATH");
        mc.reloadConfiguration(3);
    }
}

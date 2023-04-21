package up2p.core.test;

import junit.framework.TestCase;
import up2p.core.NetworkAdapterManager;

/**
 * Tests the NetworkAdapterManager class.
 * 
 * @author Neal Arthorne
 * @version 1.0
 */
public class TestNetworkAdapterManager extends TestCase {

    protected void setUp() throws Exception {
        AllUp2pTests.setupLogging();
    }

    public void testScanAdapters() throws Throwable {
        System.setProperty("up2p.home", System.getProperty("user.dir"));
        NetworkAdapterManager nam = NetworkAdapterManager.getInstance();
        nam.scanAdapterDirectory();
        assertTrue(nam.getNetworkAdapterCount() >= 1);
    }
}

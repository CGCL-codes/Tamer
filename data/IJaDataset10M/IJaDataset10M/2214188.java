package net.sourceforge.cruisecontrol.distributed.util;

import junit.framework.TestCase;
import net.sourceforge.cruisecontrol.builders.DistributedMasterBuilderTest;

public class MulticastDiscoveryTest extends TestCase {

    private Process jiniProcess;

    protected void setUp() throws Exception {
        jiniProcess = DistributedMasterBuilderTest.startJini();
    }

    protected void tearDown() throws Exception {
        DistributedMasterBuilderTest.killJini(jiniProcess);
    }

    public void testMulticastDiscovery() throws Exception {
        MulticastDiscovery discovery = new MulticastDiscovery(null);
        try {
            assertNull(discovery.findMatchingService());
        } finally {
            discovery.terminate();
        }
    }
}

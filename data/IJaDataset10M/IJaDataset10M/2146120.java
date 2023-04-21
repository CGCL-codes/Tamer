package org.personalsmartspace.spm.test;

import junit.framework.*;
import org.osgi.framework.BundleContext;
import org.osgi.util.tracker.ServiceTracker;
import org.personalsmartspace.spm.privacy.api.IPrivacyMgmt;
import org.personalsmartspace.spm.identity.api.ITransaction;
import org.personalsmartspace.spm.identity.api.TransactionID;
import org.personalsmartspace.spm.privacy.api.PrivacyManagementException;

public class SecurityPrivacyManagementTests extends TestSuite {

    public static Test suite() {
        return new SecurityPrivacyManagementTests(null);
    }

    SecurityPrivacyManagementTests(BundleContext bc) {
        addTest(new PrivacyManagerTest(bc));
    }

    private class PrivacyManagerTest extends TestCase {

        BundleContext bundleContext = null;

        ServiceTracker srtPrivacyManager = null;

        private String negotiationAgentEndpoint = "";

        public PrivacyManagerTest(BundleContext bc) {
            bundleContext = bc;
        }

        public void setUp() throws Exception {
            super.setUp();
            assertNotNull("No BundleContext!", bundleContext);
            srtPrivacyManager = new ServiceTracker(bundleContext, IPrivacyMgmt.class.getName(), null);
            assertNotNull("No PrivacyManager!", srtPrivacyManager);
            srtPrivacyManager.open();
        }

        public void tearDown() throws Exception {
            srtPrivacyManager.close();
            srtPrivacyManager = null;
            super.tearDown();
        }

        public void runTest() {
            IPrivacyMgmt pm = (IPrivacyMgmt) srtPrivacyManager.getService();
            assertNotNull("PrivacyManager away!", pm);
            try {
                TransactionID transid = pm.initiateTransaction(negotiationAgentEndpoint);
                pm.stopTransaction(transid);
                pm.startTransaction(transid);
                ITransaction trans = pm.getTransaction(transid);
            } catch (PrivacyManagementException e) {
                e.printStackTrace();
            }
        }
    }
}

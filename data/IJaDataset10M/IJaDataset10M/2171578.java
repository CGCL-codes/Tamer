package com.fddtool.pd.bug;

import java.util.List;
import junit.framework.Test;
import junit.framework.TestSuite;
import com.fddtool.si.system.BaseTestSystem;
import com.fddtool.si.system.BugSystemBugzilla;
import com.fddtool.si.system.BugSystemPTS;
import com.fddtool.si.system.SystemIntegrationFactory;

public class TestResolutionDescription extends BaseTestSystem {

    private void doTestListAll() throws Exception {
        List<ResolutionDescription> resolutions = ResolutionDescription.listAll();
        assertTrue("Null returned", resolutions != null);
        for (int i = 0; i < resolutions.size(); i++) {
            ResolutionDescription sd = resolutions.get(i);
            assertTrue(sd.getId() != null);
            assertTrue(sd.getName() != null);
            ResolutionDescription sd1 = ResolutionDescription.findById(sd.getId());
            assertTrue(sd1 != null);
            assertTrue(sd.equals(sd1));
        }
    }

    public void testListAll() throws Exception {
        IBugSystem bs = SystemIntegrationFactory.getBugSystem();
        try {
            SystemIntegrationFactory.setBugSystem(BugSystemBugzilla.getIBugSystemInstance());
            doTestListAll();
            SystemIntegrationFactory.setBugSystem(BugSystemPTS.getIBugSystemInstance());
            doTestListAll();
        } finally {
            SystemIntegrationFactory.setBugSystem(bs);
        }
    }

    /**
     *  Provides a way to run just this test by itself.
     */
    public static void main(String args[]) {
        junit.textui.TestRunner.run(suite());
    }

    /**
     * Provides a way to include this test into test suite.
     */
    public static Test suite() {
        return new TestSuite(TestResolutionDescription.class);
    }
}

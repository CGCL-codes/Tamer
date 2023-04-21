package com.fddtool.pd.bug;

import java.util.List;
import junit.framework.Test;
import junit.framework.TestSuite;
import com.fddtool.si.system.BaseTestSystem;
import com.fddtool.si.system.BugSystemBugzilla;
import com.fddtool.si.system.BugSystemPTS;
import com.fddtool.si.system.SystemIntegrationFactory;

public class TestOsDescription extends BaseTestSystem {

    private void doTestListAll() throws Exception {
        List<OsDescription> oses = OsDescription.listAll();
        assertTrue("Null returned", oses != null);
        for (int i = 0; i < oses.size(); i++) {
            OsDescription pd = (OsDescription) oses.get(i);
            assertTrue(pd.getId() != null);
            assertTrue(pd.getName() != null);
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
     * Provides a way to run just this test by itself.
     */
    public static void main(String args[]) {
        junit.textui.TestRunner.run(suite());
    }

    /**
     * Provides a way to include this test into test suite.
     */
    public static Test suite() {
        return new TestSuite(TestOsDescription.class);
    }
}

package org.eclipse.swordfish.tooling.target.platform.test;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * Class collects all system tests.
 * @author yshamin
 *
 */
public class AllTests {

    /**
	 * Creates test suite == all system tests.
	 * @return test suite.
	 */
    public static Test suite() {
        TestSuite suite = new TestSuite("Test for org.eclipse.swordfish.tooling.target.platform.test");
        suite.addTestSuite(TestDownloadTargetPlatform.class);
        suite.addTestSuite(TestExistsAndActiveTargetPlatform.class);
        suite.addTestSuite(TestCheckPreferences.class);
        suite.addTestSuite(TestCreateJAXWSServiceFromWSDL.class);
        suite.addTestSuite(TestCreateNewProjectWizard.class);
        suite.addTestSuite(TestUploadToSR.class);
        suite.addTestSuite(TestRunProvider.class);
        return suite;
    }
}

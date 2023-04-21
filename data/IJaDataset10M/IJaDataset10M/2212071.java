package com.codestreet.bugunit;

import com.codestreet.bugunit.process.ProcessExecutorThreadTest;
import com.codestreet.bugunit.process.ProcessInstanceTest;
import com.codestreet.bugunit.process.ProcessUtilTest;
import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * @author apodehl
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class AllTests {

    public static void main(String[] args) {
    }

    public static Test suite() {
        TestSuite suite = new TestSuite("Test for com.codestreet.bugunit");
        suite.addTest(CountingTestCaseTest.suite());
        suite.addTestSuite(ProcessUtilTest.class);
        suite.addTestSuite(SetupTeardownTest.class);
        suite.addTestSuite(HtmlReporterTestWithErrors.class);
        suite.addTestSuite(IntervalTimerTest.class);
        suite.addTest(BugTestSetupTest.suite());
        suite.addTestSuite(ProcessInstanceTest.class);
        suite.addTestSuite(FileUtilsTest.class);
        suite.addTestSuite(ProcessExecutorThreadTest.class);
        suite.addTestSuite(TrackedAssertionTest.class);
        suite.addTestSuite(IssuesTest.class);
        return suite;
    }
}

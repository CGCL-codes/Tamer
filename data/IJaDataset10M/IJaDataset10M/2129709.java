package org.eclipse.mylyn.monitor.tests;

import junit.framework.Test;
import junit.framework.TestSuite;
import org.eclipse.mylyn.monitor.reports.tests.AllMonitorReportTests;
import org.eclipse.mylyn.monitor.tests.usage.tests.AllMonitorUsageTests;

/**
 * @author Mik Kersten
 */
public class AllMonitorTests {

    public static Test suite() {
        TestSuite suite = new TestSuite("Test for org.eclipse.mylyn.monitor.ui.tests");
        suite.addTest(AllMonitorUsageTests.suite());
        suite.addTest(AllMonitorReportTests.suite());
        suite.addTestSuite(InteractionLoggerTest.class);
        suite.addTestSuite(ActiveTimerTest.class);
        suite.addTestSuite(StatisticsLoggingTest.class);
        suite.addTestSuite(MonitorTest.class);
        suite.addTestSuite(InteractionEventExternalizationTest.class);
        suite.addTestSuite(MonitorPackagingTest.class);
        suite.addTestSuite(MultiWindowMonitorTest.class);
        return suite;
    }
}

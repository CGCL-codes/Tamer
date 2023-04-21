package org.approvaltests.reporters.tests;

import junit.framework.TestCase;
import org.approvaltests.UseReporter;
import org.approvaltests.reporters.JunitReporter;

@UseReporter(JunitReporter.class)
public class FailedTextApprovalReportTest extends TestCase {

    /**
   * This test should fail when run. 
   * It is suppose to create a meaning full error message in the 
   * JUnit window.
   */
    public void testMeaningFullJunit() throws Exception {
    }
}

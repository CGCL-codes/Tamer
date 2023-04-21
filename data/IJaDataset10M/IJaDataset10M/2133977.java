package com.codestreet.bugunit;

/**
 * Tests the JIRA BugTracker.
 */
public class HtmlReporterTestWithErrors extends BugTestCase {

    protected void setUp() throws Exception {
        super.setUp();
        TrackedAssertionImpl.s_testing = true;
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public int add(int a, int b) {
        return a - b;
    }

    public void testMethodSuccess() {
        assertEqualsBug("RSRV-122", "we expect that 6 is 6", 6, 6);
    }

    public void testMethodSuccessAndFailure() {
        assertEqualsBug("RSRV-123", "we expect that 5 is 5", 5, 5);
        assertEqualsBug("RSRV-123", "we expect that 5 is not 6", 5, 6);
    }

    public void testAdd1() {
        assertEqualsBug("RSRV-124", "adding 1 + 2", 3, add(1, 2));
    }

    public void testAdd2() {
        assertEqualsBug("RSRV-124", "adding 3 + 4", 7, add(3, 4));
    }

    public void testAdd3() {
        assertEqualsBug("RSRV-124", "adding 5 + 2", 7, add(5, 2));
    }

    public void testLongMsgs() {
        assertEqualsBug("RSRV-125", "we expect that 5 is 5 but sometimes it could also be 6, isn't it ?", 5, 5);
        assertEqualsBug("RSRV-125", "we expect that 5 is 5 but sometimes it could also be 6, isn't it ?", 5, 5);
        assertEqualsBug("RSRV-125", "we expect that 5 is 5 but sometimes it could also be 6, isn't it ?", 5, 5);
        assertEqualsBug("RSRV-125", "we expect that 5 is 5 but sometimes it could also be 6, isn't it ?", 5, 5);
        assertEqualsBug("RSRV-125", "we expect that 5 is 5 but sometimes it could also be 6, isn't it ?", 5, 5);
        assertEqualsBug("RSRV-125", "we expect that 5 is 5 but sometimes it could also be 6, isn't it ?", 5, 5);
        assertEqualsBug("RSRV-125", "we expect that 5 is 5 but sometimes it could also be 6, isn't it ?", 5, 5);
        assertEqualsBug("RSRV-125", "we expect that 5 is 5 but sometimes it could also be 6, isn't it ?", 5, 5);
        assertEqualsBug("RSRV-125", "we expect that 5 is 5 but sometimes it could also be 6, isn't it ?", 5, 5);
        assertEqualsBug("RSRV-125", "we expect that 5 is 5 but sometimes it could also be 6, isn't it ?", 5, 5);
        assertEqualsBug("RSRV-125", "we expect that 5 is 5 but sometimes it could also be 6, isn't it ?", 5, 5);
        assertEqualsBug("RSRV-125", "we expect that 5 is 5 but sometimes it could also be 6, isn't it ?", 5, 5);
        assertEqualsBug("RSRV-125", "we expect that 5 is 5 but sometimes it could also be 6, isn't it ?", 5, 5);
        assertEqualsBug("RSRV-125", "we expect that 5 is not 6", 5, 6);
    }
}

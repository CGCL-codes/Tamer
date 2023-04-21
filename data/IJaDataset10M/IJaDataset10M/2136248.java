package com.thoughtworks.turtlemock.internal;

import junit.framework.AssertionFailedError;
import junit.framework.TestCase;

public class PairRollbackableInvokeLogTest extends TestCase {

    private RollbackableInvokeLog log;

    private Object[] params;

    private OnceInvokeMethodResultLog bottom;

    private OnceInvokeMethodResultLog top;

    protected void setUp() throws Exception {
        params = new Object[] { "param" };
        bottom = new OnceInvokeMethodResultLog("o", params);
        top = new OnceInvokeMethodResultLog("o", params);
        log = new PairRollbackableInvokeLog(bottom, top);
    }

    public void testShouldSumBottomAndTopLogTimes() throws Exception {
        assertEquals(1 + 1, log.timesNotAsserted());
        log.twice();
        log.rollback();
        log.twice();
    }

    public void testShouldFailIfExpectedTimesIsZeroButLogTimesIsNotZero() throws Exception {
        assertTimesZeroFailed();
        top.times(1);
        assertTimesZeroFailed();
        top.rollback();
        bottom.times(1);
        assertTimesZeroFailed();
        top.times(1);
        log.times(0);
    }

    private void assertTimesZeroFailed() {
        boolean failed;
        try {
            log.times(0);
            failed = true;
        } catch (AssertionFailedError e) {
            failed = false;
        }
        assertFalse(failed);
    }
}

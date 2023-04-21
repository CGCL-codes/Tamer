package com.thoughtworks.turtlemock.internal;

import com.thoughtworks.turtlemock.constraint.CheckResult;
import junit.framework.AssertionFailedError;
import junit.framework.TestCase;

public class DefaultInvokeWithArgsResultLogTest extends TestCase {

    private DefaultInvokeWithArgsResultLog defaultInvokeResultLog;

    private OnceInvokeMethodResultLog advanceInvokeLog;

    protected void setUp() throws Exception {
        advanceInvokeLog = new OnceInvokeMethodResultLog("operation", null);
        defaultInvokeResultLog = new DefaultInvokeWithArgsResultLog(advanceInvokeLog, CheckResult.PASS);
    }

    public void testShouldFailWhenExpectedTimesIsZeroButTheLogTimesIsNotZero() throws Exception {
        boolean failed;
        try {
            defaultInvokeResultLog.times(0);
            failed = true;
        } catch (AssertionFailedError e) {
            failed = false;
        }
        assertFalse(failed);
    }

    public void testShouldDelegateTimes() throws Exception {
        defaultInvokeResultLog.times(1);
        assertEquals(0, defaultInvokeResultLog.timesNotAsserted());
        assertEquals(0, advanceInvokeLog.timesNotAsserted());
    }
}

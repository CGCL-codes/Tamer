package com.rcreations.timeout;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.apache.log4j.BasicConfigurator;

/**
 *
 */
public class TimeoutTemplateTest extends TestCase {

    public TimeoutTemplateTest(String name) {
        super(name);
    }

    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public static Test suite() {
        return new TestSuite(TimeoutTemplateTest.class);
    }

    protected void setUp() {
        BasicConfigurator.configure();
    }

    protected void tearDown() {
    }

    public static class TimeoutTest extends TimeoutTemplate {

        protected long m_lMsSleep = 0;

        protected Exception m_exception = null;

        public TimeoutTest(long lMsSleep) {
            m_lMsSleep = lMsSleep;
        }

        public TimeoutTest(Exception e) {
            m_exception = e;
        }

        public void runInBackground() throws Exception {
            if (m_exception != null) {
                LogUtils.getLogger().debug("throw exception");
                throw m_exception;
            }
            LogUtils.getLogger().debug("sleep " + m_lMsSleep);
            Thread.sleep(m_lMsSleep);
            LogUtils.getLogger().debug("sleep done");
        }

        public static TimeoutTest sleepWithTimeout(long lMsSleep, long lMsTimeout) {
            TimeoutTest tTest = new TimeoutTest(lMsSleep);
            tTest.runWithTimeout(lMsTimeout);
            return tTest;
        }

        public static TimeoutTest exceptionWithTimeout(Exception e, long lMsTimeout) {
            TimeoutTest tTest = new TimeoutTest(e);
            tTest.runWithTimeout(lMsTimeout);
            return tTest;
        }
    }

    public void testAlwaysTimeout() {
        TimeoutTest tTest = TimeoutTest.sleepWithTimeout(5 * 1000, (long) (.1 * 1000));
        assertTrue("testAlwaysTimeout wasExceptioned", tTest.wasExceptioned() == null);
        assertTrue("testAlwaysTimeout wasTimedOut", tTest.wasTimedOut() == true);
    }

    public void testNoTimeout() {
        TimeoutTest tTest = TimeoutTest.sleepWithTimeout((long) (.1 * 1000), 5 * 1000);
        assertTrue("testAlwaysTimeout wasExceptioned", tTest.wasExceptioned() == null);
        assertTrue("testAlwaysTimeout wasTimedOut", tTest.wasTimedOut() == false);
    }

    public void testException() {
        Exception e = new Exception("dummy exception");
        TimeoutTest tTest = TimeoutTest.exceptionWithTimeout(e, 5 * 1000);
        assertTrue("testAlwaysTimeout wasExceptioned", tTest.wasExceptioned() == e);
        assertTrue("testAlwaysTimeout wasTimedOut", tTest.wasTimedOut() == false);
    }
}

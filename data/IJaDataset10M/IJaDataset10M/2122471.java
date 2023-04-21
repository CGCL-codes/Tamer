package com.volantis.mps.message.store;

import com.volantis.synergetics.testtools.TestCaseAbstract;
import java.util.TimerTask;

/**
 * This provides a unit test of some of the functionality of the generic
 * message store daemon.  A sample task is used to ensure that it operates
 * every so often reliably.
 */
public class MessageStoreDaemonTestCase extends TestCaseAbstract {

    /**
     * Used by the test timer task to indicate its activity which can then be
     * checked in the test case methods.
     */
    protected int count = 0;

    /**
     * Initialise a new instance of this test case.
     */
    public MessageStoreDaemonTestCase() {
    }

    /**
     * Initialise a new named instance of this test case.
     *
     * @param s The name of the test case.
     */
    public MessageStoreDaemonTestCase(String s) {
        super(s);
    }

    protected void setUp() throws Exception {
        super.setUp();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * This tests the functioning of the daemon with a simple task to
     * record activity.  The cleaning daemon task as defined in
     * {@link com.volantis.mps.servlet.MessageStoreServlet} is tested within
     * the unit test for that class.
     */
    public void testDaemon() throws Exception {
        count = 0;
        MessageStoreDaemon daemon = new MessageStoreDaemon(1, createTimerTask());
        daemon.delayAndPeriod = 1000;
        daemon.start();
        Thread.sleep(5000);
        assertTrue("Task should have run", count > 0);
        int currentCount = count;
        daemon.stop();
        Thread.sleep(5000);
        assertEquals("Task should not have run anymore", count, currentCount);
    }

    /**
     * Create a very simple timer task to be executed every so often.
     *
     * @return An initialised and ready to run task.
     */
    private TimerTask createTimerTask() {
        return new TimerTask() {

            public void run() {
                count++;
            }
        };
    }
}

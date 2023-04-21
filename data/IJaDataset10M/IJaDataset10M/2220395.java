package com.the_eventhorizon.actionQueue;

import org.junit.Test;
import org.junit.Assert;
import com.the_eventhorizon.actionQueue.data.LoggingRunnable;
import com.the_eventhorizon.actionQueue.data.TestAction;
import com.the_eventhorizon.actionQueue.data.TestActionWithSubActions;
import com.the_eventhorizon.actionQueue.data.BaseOneQueueTestCase;

/**
 * @author <a href="mailto:pkrupets@yahoo.com">Pavel Krupets</a>
 */
public class LockTest extends BaseOneQueueTestCase {

    @Test
    public void simple() throws Exception {
        LockData ld = new LockData();
        StringBuffer sb = new StringBuffer();
        new TestAction(getQueue(), new LoggingRunnable(sb, "a")).schedule();
        getQueue().lock(ld);
        try {
            new TestAction(getQueue(), new LoggingRunnable(sb, "b")).schedule();
            Assert.fail();
        } catch (ActionRejectedException e) {
        }
        getQueue().lock(null);
        new TestAction(getQueue(), new LoggingRunnable(sb, "c")).schedule();
        Thread.sleep(1000);
        Assert.assertEquals("ac", sb.toString());
    }

    @Test
    public void subAction() throws Exception {
        StringBuffer sb = new StringBuffer();
        QueueAction a1 = new TestAction(getQueue(), new LoggingRunnable(sb, "b"));
        QueueAction testAction = new TestActionWithSubActions(getQueue(), new LoggingRunnable(sb, "a"), a1);
        getQueue().lock(new LockData());
        try {
            testAction.schedule();
            Assert.fail();
        } catch (ActionRejectedException e) {
        }
        getQueue().lock(null);
        testAction.schedule();
        Thread.sleep(1000);
        Assert.assertEquals("ab", sb.toString());
    }

    @Test
    public void subActionWithLock() throws Exception {
        StringBuffer sb = new StringBuffer();
        QueueAction a1 = new TestAction(getQueue(), new LoggingRunnable(sb, "b"));
        TestActionWithSubActions testAction = new TestActionWithSubActions(getQueue(), new LoggingRunnable(sb, "a"), a1);
        testAction.setLockBeforeSubActions(true);
        testAction.schedule();
        Thread.sleep(1000);
        Assert.assertEquals("ab", sb.toString());
    }
}

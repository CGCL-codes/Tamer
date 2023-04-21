package net.sourceforge.basher;

import java.util.UUID;
import static org.easymock.EasyMock.expectLastCall;

/**
 * @author Johan Lindquist
 * @version 1.0
 */
public class TestTaskExecutionContext extends BasherTestCase {

    private TaskConfiguration _taskConfiguration;

    private Task _task;

    private UUID _uuid = UUID.randomUUID();

    public void testInvokeTaskSuccessfully() {
        trainInvokeTask(false, false);
        trainRecalculateWeight();
        replayAll();
        final TaskExecutionContext taskExecutionContext = new TaskExecutionContext(_uuid, _task, _taskConfiguration);
        try {
            taskExecutionContext.executeTask();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            fail(throwable.getMessage());
        }
        verifyCounts(taskExecutionContext, 1, 0, 0);
        verifyAll();
    }

    public void testInvokeTaskThrowsNotRunException() {
        trainInvokeTask(true, false);
        replayAll();
        TaskExecutionContext taskExecutionContext = new TaskExecutionContext(UUID.randomUUID(), _task, _taskConfiguration);
        try {
            taskExecutionContext.executeTask();
            fail("could execute successfully when supposed to fail");
        } catch (Throwable throwable) {
            assertTrue("bad exception", throwable instanceof TaskNotRunException);
            assertEquals("bad exception message", "emulateNotRunException", throwable.getMessage());
        }
        verifyCounts(taskExecutionContext, 0, 1, 0);
        verifyAll();
    }

    public void testInvokeTaskThrowsThrowable() {
        trainInvokeTask(false, true);
        replayAll();
        TaskExecutionContext taskExecutionContext = new TaskExecutionContext(UUID.randomUUID(), _task, _taskConfiguration);
        try {
            taskExecutionContext.executeTask();
            fail("could execute successfully when supposed to fail");
        } catch (Throwable throwable) {
            assertTrue("bad exception", throwable instanceof NullPointerException);
            assertEquals("bad exception message", "emulateThrowable" + "", throwable.getMessage());
        }
        verifyCounts(taskExecutionContext, 0, 0, 1);
        verifyAll();
    }

    private void verifyCounts(final TaskExecutionContext taskExecutionContext, final int successCount, final int notRuncount, final int failureCount) {
        assertEquals("bad success", successCount, taskExecutionContext.getSuccesses());
        assertEquals("bad notRun", notRuncount, taskExecutionContext.getNotRun());
        assertEquals("bad failure", failureCount, taskExecutionContext.getFailures());
    }

    private void trainRecalculateWeight() {
        _taskConfiguration.reCalculateWeight();
    }

    private void trainInvokeTask(final boolean emulateNotRunException, final boolean emulateThrowable) {
        try {
            _task.executeTask();
            if (emulateNotRunException) {
                expectLastCall().andThrow(new TaskNotRunException("emulateNotRunException"));
            }
            if (emulateThrowable) {
                expectLastCall().andThrow(new NullPointerException("emulateThrowable"));
            }
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            fail(throwable.getMessage());
        }
    }

    public void testIt() {
    }

    protected void setUp() throws Exception {
        super.setUp();
        _taskConfiguration = createMock(TaskConfiguration.class);
        _task = createMock(Task.class);
    }
}

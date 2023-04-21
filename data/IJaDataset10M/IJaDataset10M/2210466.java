package com.sun.star.lib.uno.environments.remote;

import com.sun.star.lib.uno.typedesc.MethodDescription;
import com.sun.star.lib.uno.typedesc.TypeDescription;
import complexlib.ComplexTestCase;

public final class JobQueue_Test extends ComplexTestCase {

    public String getTestObjectName() {
        return getClass().getName();
    }

    public String[] getTestMethodNames() {
        return new String[] { "testThreadLeavesJobQueueOnDispose0", "testThreadLeavesJobQueueOnDispose5000", "testThreadLeavesJobQueueOnReply0", "testThreadLeavesJobQueueOnReply5000", "testStaticThreadExecutesJobs0", "testStaticThreadExecutesJobs5000", "testDynamicThreadExecutesJob", "testStaticThreadExecutesAsyncs", "testDynamicThreadExecutesAsyncs" };
    }

    public void testThreadLeavesJobQueueOnDispose0() throws InterruptedException {
        testThreadLeavesJobQueueOnDispose(0);
    }

    public void testThreadLeavesJobQueueOnDispose5000() throws InterruptedException {
        testThreadLeavesJobQueueOnDispose(5000);
    }

    private void testThreadLeavesJobQueueOnDispose(int waitTime) throws InterruptedException {
        TestThread t = new TestThread(waitTime);
        t.waitToStart();
        String msg = "xcxxxxxxxx";
        t._jobQueue.dispose(t._disposeId, new RuntimeException(msg));
        t.waitToTerminate();
        assure("", t._message.equals(msg));
    }

    public void testThreadLeavesJobQueueOnReply0() throws InterruptedException {
        testThreadLeavesJobQueueOnReply(0);
    }

    public void testThreadLeavesJobQueueOnReply5000() throws InterruptedException {
        testThreadLeavesJobQueueOnReply(5000);
    }

    private void testThreadLeavesJobQueueOnReply(int waitTime) throws InterruptedException {
        TestThread t = new TestThread(waitTime);
        t.waitToStart();
        t._jobQueue.putJob(new Job(null, __iReceiver, new Message(null, false, "oid", __workAt_td, null, false, null, false, null, null)), null);
        t.waitToTerminate();
        assure("", true);
    }

    public void testStaticThreadExecutesJobs0() throws InterruptedException {
        testStaticThreadExecutesJobs(0);
    }

    public void testStaticThreadExecutesJobs5000() throws InterruptedException {
        testStaticThreadExecutesJobs(5000);
    }

    private void testStaticThreadExecutesJobs(int waitTime) throws InterruptedException {
        TestThread t = new TestThread(waitTime);
        t.waitToStart();
        testExecuteJobs(t._jobQueue);
        t._jobQueue.dispose(t._disposeId, new RuntimeException("xxxxxxxxxxxxx"));
        t.waitToTerminate();
    }

    public void testDynamicThreadExecutesJob() throws InterruptedException {
        testExecuteJobs(new JobQueue(__javaThreadPoolFactory, ThreadId.createFresh(), true));
    }

    public void testStaticThreadExecutesAsyncs() throws InterruptedException {
        TestThread t = new TestThread();
        JobQueue async_jobQueue = new JobQueue(__javaThreadPoolFactory, t._threadId);
        assure("", async_jobQueue._ref_count == 1);
        t._jobQueue = __javaThreadPoolFactory.getJobQueue(t._threadId);
        assure("", t._jobQueue._ref_count == 1);
        t.waitToStart();
        TestWorkAt workAt = new TestWorkAt();
        testAsyncJobQueue(workAt, async_jobQueue, t._threadId);
        t._jobQueue.dispose(t._disposeId, new RuntimeException("xxxxxxxxxxxxx"));
        t.waitToTerminate();
        assure("", workAt._async_counter == TestWorkAt.MESSAGES);
        assure("", workAt._sync_counter == TestWorkAt.MESSAGES);
    }

    public void testDynamicThreadExecutesAsyncs() throws InterruptedException {
        ThreadId threadId = ThreadId.createFresh();
        JobQueue async_jobQueue = new JobQueue(__javaThreadPoolFactory, threadId);
        TestWorkAt workAt = new TestWorkAt();
        testAsyncJobQueue(workAt, async_jobQueue, threadId);
        assure("", workAt._async_counter == TestWorkAt.MESSAGES);
        assure("", workAt._sync_counter == TestWorkAt.MESSAGES);
    }

    private void testExecuteJobs(JobQueue jobQueue) throws InterruptedException {
        TestWorkAt workAt = new TestWorkAt();
        testSendRequests(workAt, "increment", jobQueue);
        synchronized (workAt) {
            jobQueue.putJob(new Job(workAt, __iReceiver, new Message(null, true, "oid", __workAt_td, ((MethodDescription) __workAt_td.getMethodDescription("notifyme")), true, null, false, null, null)), null);
            while (!workAt._notified) {
                workAt.wait();
            }
        }
        assure("", workAt._counter == TestWorkAt.MESSAGES);
    }

    private void testAsyncJobQueue(TestWorkAt workAt, JobQueue async_jobQueue, ThreadId threadId) throws InterruptedException {
        testSendRequests(workAt, "asyncCall", async_jobQueue);
        testSendRequests(workAt, "syncCall", __javaThreadPoolFactory.getJobQueue(threadId));
        synchronized (workAt) {
            async_jobQueue._sync_jobQueue.putJob(new Job(workAt, __iReceiver, new Message(null, true, "oid", __workAt_td, ((MethodDescription) __workAt_td.getMethodDescription("notifyme")), true, null, false, null, null)), null);
            while (!workAt._notified) {
                workAt.wait();
            }
        }
        assure("", workAt.passedAsyncTest());
    }

    private void testSendRequests(TestWorkAt workAt, String operation, JobQueue jobQueue) {
        Message iMessage = new Message(null, true, "oid", __workAt_td, (MethodDescription) __workAt_td.getMethodDescription(operation), true, null, false, null, null);
        for (int i = 0; i < TestWorkAt.MESSAGES; ++i) {
            Thread.yield();
            jobQueue.putJob(new Job(workAt, __iReceiver, iMessage), new Object());
        }
    }

    private static final class TestThread extends Thread {

        public final ThreadId _threadId = JavaThreadPoolFactory.getThreadId();

        public final Object _disposeId = new Object();

        public JobQueue _jobQueue = null;

        public String _message;

        public TestThread(int waitTime) {
            this.waitTime = waitTime;
            _jobQueue = new JobQueue(__javaThreadPoolFactory, _threadId, false);
        }

        public TestThread() {
            waitTime = 0;
        }

        public void run() {
            synchronized (lock) {
                state = STATE_STARTED;
                lock.notifyAll();
            }
            try {
                if (waitTime != 0) {
                    Thread.sleep(waitTime);
                }
                _jobQueue.enter(_disposeId);
            } catch (Throwable e) {
                _message = e.getMessage();
            }
            synchronized (lock) {
                state = STATE_DONE;
                lock.notifyAll();
            }
        }

        public void waitToStart() throws InterruptedException {
            start();
            synchronized (lock) {
                while (state == STATE_INITIAL) {
                    lock.wait();
                }
            }
        }

        public void waitToTerminate() throws InterruptedException {
            synchronized (lock) {
                while (state != STATE_DONE) {
                    lock.wait();
                }
            }
            join();
        }

        private final int waitTime;

        private final Object lock = new Object();

        private int state = STATE_INITIAL;

        private static final int STATE_INITIAL = 0;

        private static final int STATE_STARTED = 1;

        private static final int STATE_DONE = 2;
    }

    private static final JavaThreadPoolFactory __javaThreadPoolFactory = new JavaThreadPoolFactory();

    private static final IReceiver __iReceiver = new TestReceiver();

    private static final TypeDescription __workAt_td = TypeDescription.getTypeDescription(TestIWorkAt.class);
}

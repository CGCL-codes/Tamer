package org.pixory.pxfoundation.concurrent;

import junit.framework.TestCase;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class PXSemaphoreTestCase extends TestCase {

    private static final Log LOG = LogFactory.getLog(PXSemaphoreTestCase.class);

    private static final int NOT_STARTED_STATUS = 0;

    private static final int STARTED_STATUS = 1;

    private static final int SUCESS_STATUS = 2;

    private static final int FAILURE_STATUS = 3;

    private static final int INTERRUPTED_STATUS = 4;

    private int _testThreadStatus = NOT_STARTED_STATUS;

    public PXSemaphoreTestCase(String name) {
        super(name);
    }

    public void testAcquisition() throws InterruptedException {
        PXSemaphore aSemaphore = new PXSemaphore(1);
        TestThread aTestThread = new TestThread(aSemaphore, 1, true, 0);
        _testThreadStatus = NOT_STARTED_STATUS;
        aTestThread.start();
        while (_testThreadStatus == NOT_STARTED_STATUS) {
            Thread.sleep(100);
        }
        while (_testThreadStatus == STARTED_STATUS) {
            Thread.sleep(100);
        }
        assertTrue(_testThreadStatus == SUCESS_STATUS);
        assertTrue(!aSemaphore.attempt(1, 100));
    }

    public void testBlock() throws InterruptedException {
        PXSemaphore aSemaphore = new PXSemaphore(1);
        TestThread aTestThread = new TestThread(aSemaphore, 1, true, 0);
        aSemaphore.acquire(1);
        _testThreadStatus = NOT_STARTED_STATUS;
        aTestThread.start();
        while (_testThreadStatus == NOT_STARTED_STATUS) {
            Thread.sleep(10);
        }
        assertTrue(_testThreadStatus == STARTED_STATUS);
        Thread.sleep(200);
        assertTrue(_testThreadStatus == STARTED_STATUS);
        aSemaphore.release(1);
        while (_testThreadStatus == STARTED_STATUS) {
            Thread.sleep(100);
        }
        assertTrue(_testThreadStatus == SUCESS_STATUS);
    }

    public void testTimeout() throws InterruptedException {
        PXSemaphore aSemaphore = new PXSemaphore(1);
        TestThread aTestThread = new TestThread(aSemaphore, 1, false, 100);
        aSemaphore.acquire(1);
        _testThreadStatus = NOT_STARTED_STATUS;
        aTestThread.start();
        while (_testThreadStatus == NOT_STARTED_STATUS) {
            Thread.sleep(10);
        }
        assertTrue(_testThreadStatus == STARTED_STATUS);
        while (_testThreadStatus == STARTED_STATUS) {
            Thread.sleep(10);
        }
        assertTrue(_testThreadStatus == FAILURE_STATUS);
    }

    public void testTimein() throws InterruptedException {
        PXSemaphore aSemaphore = new PXSemaphore(1);
        TestThread aTestThread = new TestThread(aSemaphore, 1, false, 1000);
        aSemaphore.acquire(1);
        _testThreadStatus = NOT_STARTED_STATUS;
        aTestThread.start();
        while (_testThreadStatus == NOT_STARTED_STATUS) {
            Thread.sleep(10);
        }
        aSemaphore.release(1);
        while (_testThreadStatus == STARTED_STATUS) {
            Thread.sleep(10);
        }
        assertTrue(_testThreadStatus == SUCESS_STATUS);
    }

    public void testAttempt() throws InterruptedException {
        PXSemaphore aSemaphore = new PXSemaphore(1);
        TestThread aTestThread = new TestThread(aSemaphore, 1, false, 100);
        aSemaphore.acquire(1);
        _testThreadStatus = NOT_STARTED_STATUS;
        aTestThread.start();
        aSemaphore.release(1);
        while (_testThreadStatus == NOT_STARTED_STATUS) {
            Thread.sleep(10);
        }
        while (_testThreadStatus == STARTED_STATUS) {
            Thread.sleep(10);
        }
        assertTrue(_testThreadStatus == SUCESS_STATUS);
    }

    public void testInterrupt() throws InterruptedException {
        PXSemaphore aSemaphore = new PXSemaphore(1);
        TestThread aTestThread = new TestThread(aSemaphore, 1, true, 1000);
        aSemaphore.acquire(1);
        _testThreadStatus = NOT_STARTED_STATUS;
        aTestThread.start();
        while (_testThreadStatus == NOT_STARTED_STATUS) {
            Thread.sleep(10);
        }
        aTestThread.interrupt();
        while (_testThreadStatus != INTERRUPTED_STATUS) {
            Thread.sleep(10);
        }
        assertTrue(_testThreadStatus == INTERRUPTED_STATUS);
    }

    public void testMultiPermits() throws InterruptedException {
        PXSemaphore aSemaphore = new PXSemaphore(3);
        TestThread aTestThread = new TestThread(aSemaphore, 3, true, 0);
        aSemaphore.acquire(3);
        _testThreadStatus = NOT_STARTED_STATUS;
        aTestThread.start();
        while (_testThreadStatus == NOT_STARTED_STATUS) {
            Thread.sleep(10);
        }
        assertTrue(_testThreadStatus == STARTED_STATUS);
        aSemaphore.release(1);
        Thread.sleep(100);
        assertTrue(_testThreadStatus == STARTED_STATUS);
        aSemaphore.release(1);
        Thread.sleep(100);
        assertTrue(_testThreadStatus == STARTED_STATUS);
        aSemaphore.release(1);
        while (_testThreadStatus == STARTED_STATUS) {
            Thread.sleep(10);
        }
        assertTrue(_testThreadStatus == SUCESS_STATUS);
    }

    private class TestThread extends Thread {

        private PXSemaphore _semaphore;

        private int _acquisitionCount;

        private boolean _block;

        private long _timeout;

        public TestThread(PXSemaphore semaphore, int acquisitionCount, boolean block, long timeout) {
            _semaphore = semaphore;
            _acquisitionCount = acquisitionCount;
            _block = block;
            _timeout = timeout;
        }

        public void run() {
            try {
                PXSemaphoreTestCase.this._testThreadStatus = STARTED_STATUS;
                if (_block) {
                    _semaphore.acquire(_acquisitionCount);
                    PXSemaphoreTestCase.this._testThreadStatus = SUCESS_STATUS;
                } else {
                    if (_semaphore.attempt(1, _timeout)) {
                        PXSemaphoreTestCase.this._testThreadStatus = SUCESS_STATUS;
                    } else {
                        PXSemaphoreTestCase.this._testThreadStatus = FAILURE_STATUS;
                    }
                }
            } catch (InterruptedException anException) {
                PXSemaphoreTestCase.this._testThreadStatus = INTERRUPTED_STATUS;
            } catch (Exception anException) {
                PXSemaphoreTestCase.this._testThreadStatus = FAILURE_STATUS;
                fail(anException.toString());
            }
        }
    }
}

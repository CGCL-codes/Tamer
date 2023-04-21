package net.sourceforge.ricep.nircc.threads.tests;

import junit.framework.TestCase;
import net.sourceforge.ricep.nircc.threads.IObjectQueue;
import net.sourceforge.ricep.nircc.threads.ISync;
import net.sourceforge.ricep.nircc.threads.IThreadsafeObjectQueueFactory;
import net.sourceforge.ricep.nircc.threads.ThreadsafeObjectQueue;
import net.sourceforge.ricep.nircc.threads.mocks.MockSemaphore;
import net.sourceforge.ricep.utils.ObjectQueue;

/**
 * @author sandro
 *
 * The Test for the ThreadsafeObjectQueue. 
 */
public class ThreadsafeObjectQueueTest extends TestCase {

    protected ThreadsafeObjectQueue fQueue;

    protected MockSemaphore fFull;

    protected MockSemaphore fEmpty;

    protected MockSemaphore fMutex;

    protected void setUp() {
        fFull = new MockSemaphore();
        fEmpty = new MockSemaphore();
        fMutex = new MockSemaphore();
        fQueue = new ThreadsafeObjectQueue(new IThreadsafeObjectQueueFactory() {

            public ISync getFull() {
                return fFull;
            }

            public ISync getEmpty() {
                return fEmpty;
            }

            public ISync getMutex() {
                return fMutex;
            }

            public IObjectQueue getObjectQueue() {
                return new ObjectQueue();
            }
        });
    }

    public void testAddObject() {
        fEmpty.addExpectation("acquire()");
        fMutex.addExpectation("acquire()");
        fMutex.addExpectation("release()");
        fFull.addExpectation("release()");
        fQueue.add(new Object());
        verifyMocks();
    }

    public void testGetObject() {
        fFull.addExpectation("acquire()");
        fMutex.addExpectation("acquire()");
        fMutex.addExpectation("release()");
        fEmpty.addExpectation("release()");
        fQueue.getObject();
        verifyMocks();
    }

    public void testInterupt() {
        fFull.addExpectation("release()");
        fEmpty.addExpectation("release()");
        fQueue.interupt();
        verifyMocks();
    }

    /**
	 * Verify all the Sync Mocks. 
	 */
    private void verifyMocks() {
        fEmpty.verify();
        fFull.verify();
        fMutex.verify();
    }
}

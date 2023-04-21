package net.sf.sasl.distributed.lock.impl;

import java.lang.Thread.UncaughtExceptionHandler;
import java.util.concurrent.TimeUnit;
import junit.framework.Assert;
import net.sf.sasl.distributed.lock.DeadlockException;
import net.sf.sasl.distributed.lock.ILockProvider;
import net.sf.sasl.distributed.lock.ILockSequence;
import net.sf.sasl.distributed.lock.IMutex;
import net.sf.sasl.distributed.lock.LockOperationException;
import org.easymock.classextension.EasyMock;
import org.easymock.classextension.IMocksControl;
import org.junit.Before;
import org.junit.Test;

/**
 * Test cases for the {@link net.sf.sasl.distributed.lock.impl.DefaultLockSession DefaultLockSession} class.
 * 
 * @author Philipp Förmer
 * 
 */
public class DefaultLockSessionTest {

    /**
	 * The unit under test.
	 */
    private DefaultLockSession underTest;

    /**
	 * Used for creating mocks.
	 */
    private IMocksControl mockControl;

    /**
	 * 
	 */
    private ILockProvider lockProviderMock;

    /**
	 * 
	 */
    private LockSessionFactory lockSessionFactoryMock;

    @Before
    public void setUp() {
        mockControl = EasyMock.createControl();
        lockProviderMock = mockControl.createMock(ILockProvider.class);
        lockSessionFactoryMock = mockControl.createMock(LockSessionFactory.class);
        underTest = new DefaultLockSession(lockSessionFactoryMock, Thread.currentThread(), true);
    }

    /**
	 * Test cases for the {@link net.sf.sasl.distributed.lock.impl.DefaultLockSession#getLockSessionFactory() getLockSessionFactory()} method.
	 * @throws Exception
	 */
    @Test
    public void testGetLockSessionFactory() throws Exception {
        Assert.assertEquals(lockSessionFactoryMock, underTest.getLockSessionFactory());
    }

    /**
	 * Test cases for the {@link net.sf.sasl.distributed.lock.impl.DefaultLockSession#supportsNestedLockSequences() supportsNestedLockSequences()} method.
	 * @throws Exception
	 */
    @Test
    public void testSupportesNestedLockSequences() throws Exception {
        Assert.assertTrue(underTest.supportsNestedLockSequences());
    }

    /**
	 * Test cases for the {@link net.sf.sasl.distributed.lock.impl.DefaultLockSession#getCurrentLockSequence() getCurrentLockSequence()} method.
	 * @throws Exception
	 */
    @Test
    public void testGetCurrentLockSequence() throws Exception {
        Assert.assertNull(underTest.getCurrentLockSequence());
        ILockSequence lockSequenceOne = underTest.beginLockSequence("abc");
        Assert.assertEquals(lockSequenceOne, underTest.getCurrentLockSequence());
        ILockSequence lockSequenceTwo = underTest.beginLockSequence("asd");
        Assert.assertEquals(lockSequenceTwo, underTest.getCurrentLockSequence());
        lockSequenceTwo.release();
        Assert.assertEquals(lockSequenceOne, underTest.getCurrentLockSequence());
    }

    /**
	 * Test cases for the {@link net.sf.sasl.distributed.lock.impl.DefaultLockSession#release() release()} method.
	 * @throws Exception
	 */
    @Test
    public void testRelease() throws Exception {
        ILockSequence lockSequence = underTest.beginLockSequence("abc");
        ILockSequence nestedSequence = underTest.beginLockSequence("nested");
        underTest.release();
        Assert.assertFalse(lockSequence.isActive());
        Assert.assertFalse(nestedSequence.isActive());
        Assert.assertFalse(underTest.isActive());
        Assert.assertFalse(underTest.isInvalid());
        testThrowsException(new Runnable() {

            public void run() {
                underTest.beginLockSequence("jsd");
            }
        }, LockOperationException.class);
        testThrowsException(new Runnable() {

            public void run() {
                underTest.getCurrentLockSequence();
            }
        }, LockOperationException.class);
        underTest.getLockSessionFactory();
        underTest.getName();
    }

    /**
	 * Test cases for the {@link net.sf.sasl.distributed.lock.impl.DefaultLockSession#beginLockSequence(String) beginLockSequence(String)} method.
	 * @throws Exception
	 */
    @Test
    public void testBeginLockSequence() throws Exception {
        ILockSequence lockSequence = underTest.beginLockSequence("abc");
        Assert.assertTrue(lockSequence.isActive());
        Assert.assertEquals("abc", lockSequence.getName());
    }

    /**
	 * Test cases for the {@link net.sf.sasl.distributed.lock.impl.DefaultLockSession#clear() clear()} method.
	 * @throws Exception
	 */
    @Test
    public void testClear() throws Exception {
        ILockSequence lockSequenceOne = underTest.beginLockSequence("123");
        underTest.clear();
        Assert.assertFalse(lockSequenceOne.isActive());
        Assert.assertTrue(underTest.isActive());
        Assert.assertFalse(underTest.isInvalid());
        underTest.beginLockSequence(null);
        underTest.getCurrentLockSequence();
    }

    /**
	 * Tests if all public vulnerable methods of {@link net.sf.sasl.distributed.lock.impl.DefaultLockSession DefaultLockSession} validate if the current thread equals the owner thread, if thread checking is enabled.
	 * 
	 * @throws Exception
	 */
    @Test
    public void testThreadCheckForSession() throws Exception {
        Thread thread = new Thread(new Runnable() {

            public void run() {
                testThrowsException(new Runnable() {

                    public void run() {
                        underTest.getCurrentLockSequence();
                    }
                }, LockOperationException.class);
                testThrowsException(new Runnable() {

                    public void run() {
                        underTest.beginLockSequence(null);
                    }
                }, LockOperationException.class);
                testThrowsException(new Runnable() {

                    public void run() {
                        underTest.isLocalLockHeldByASequence("a");
                    }
                }, LockOperationException.class);
                testThrowsException(new Runnable() {

                    public void run() {
                        underTest.isLockKeyOwnedByASequence("a");
                    }
                }, LockOperationException.class);
                testThrowsException(new Runnable() {

                    public void run() {
                        underTest.clear();
                    }
                }, LockOperationException.class);
                underTest.getName();
                underTest.isActive();
                underTest.isInvalid();
                testThrowsException(new Runnable() {

                    public void run() {
                        underTest.release();
                    }
                }, LockOperationException.class);
            }
        });
        ExceptionHandler exceptionHandler = new ExceptionHandler();
        thread.setUncaughtExceptionHandler(exceptionHandler);
        thread.start();
        thread.join();
        Assert.assertNull(exceptionHandler.getOccurredException());
    }

    /**
	 * Test cases for the {@link net.sf.sasl.distributed.lock.impl.DefaultLockSession#isLocalLockHeldByASequence(Object) isLocalLockHeldByASequence(Object)} and 
	 * {@link net.sf.sasl.distributed.lock.impl.DefaultLockSession#isLockKeyOwnedByASequence(Object) isLockKeyOwnedByASequence(Object)} methods.
	 * @throws Exception
	 */
    @Test
    public void testIsLocalLockHeldByASequenceAndIsLockKeyOwnedByASequence() throws Exception {
        ILockSequence lockSequenceOne = underTest.beginLockSequence("asd");
        Assert.assertFalse(underTest.isLocalLockHeldByASequence("asd"));
        Assert.assertFalse(underTest.isLockKeyOwnedByASequence("asd"));
        IMutex mutexMockOne = mockControl.createMock(IMutex.class);
        EasyMock.expect(lockSessionFactoryMock.getLockProvider()).andReturn(lockProviderMock);
        EasyMock.expect(lockProviderMock.acquireMutex("asd")).andReturn(mutexMockOne);
        mutexMockOne.lock();
        mockControl.replay();
        IMutex mutex = lockSequenceOne.acquireMutex("asd");
        Assert.assertTrue(underTest.isLockKeyOwnedByASequence("asd"));
        Assert.assertFalse(underTest.isLocalLockHeldByASequence("asd"));
        mutex.lock();
        Assert.assertTrue(underTest.isLockKeyOwnedByASequence("asd"));
        Assert.assertTrue(underTest.isLocalLockHeldByASequence("asd"));
        mockControl.verify();
        mockControl.reset();
        ILockSequence lockSequenceTwo = underTest.beginLockSequence("#2");
        IMutex mutexMockTwo = mockControl.createMock(IMutex.class);
        EasyMock.expect(lockSessionFactoryMock.getLockProvider()).andReturn(lockProviderMock);
        EasyMock.expect(lockProviderMock.acquireMutex("qwe")).andReturn(mutexMockTwo);
        mutexMockTwo.lock();
        mockControl.replay();
        mutex = lockSequenceTwo.acquireMutex("qwe");
        Assert.assertTrue(underTest.isLockKeyOwnedByASequence("asd"));
        Assert.assertTrue(underTest.isLocalLockHeldByASequence("asd"));
        Assert.assertTrue(underTest.isLockKeyOwnedByASequence("qwe"));
        Assert.assertFalse(underTest.isLocalLockHeldByASequence("qwe"));
        mutex.lock();
        Assert.assertTrue(underTest.isLocalLockHeldByASequence("qwe"));
        mockControl.verify();
        mockControl.reset();
    }

    /**
	 * Test cases for the {@link net.sf.sasl.distributed.lock.impl.DefaultLockSession#isActive() isActive()} and {@link net.sf.sasl.distributed.lock.impl.DefaultLockSession#isInvalid() isInvalid()} methods.
	 * 
	 * @throws Exception
	 */
    @Test
    public void testIsActive() throws Exception {
        Assert.assertTrue(underTest.isActive());
        Assert.assertFalse(underTest.isInvalid());
        underTest.clear();
        Assert.assertTrue(underTest.isActive());
        Assert.assertFalse(underTest.isInvalid());
        IMutex mutexMockOne = mockControl.createMock(IMutex.class);
        EasyMock.expect(lockSessionFactoryMock.getLockProvider()).andReturn(lockProviderMock);
        EasyMock.expect(lockProviderMock.acquireMutex("asd")).andReturn(mutexMockOne);
        EasyMock.expect(mutexMockOne.tryLock()).andThrow(new DeadlockException());
        mockControl.replay();
        try {
            ILockSequence lockSequence = underTest.beginLockSequence("");
            lockSequence.tryLock("asd");
            Assert.fail("Should never come to this point.");
        } catch (DeadlockException exception) {
        }
        Assert.assertTrue(underTest.isActive());
        Assert.assertTrue(underTest.isActive());
        mockControl.verify();
        mockControl.reset();
        EasyMock.expect(mutexMockOne.getLockKey()).andReturn("asd");
        lockSessionFactoryMock.removeFromCache(underTest);
        mockControl.replay();
        underTest.release();
        Assert.assertFalse(underTest.isActive());
        Assert.assertFalse(underTest.isInvalid());
        mockControl.verify();
        mockControl.reset();
    }

    /**
	 * Test cases for the {@link net.sf.sasl.distributed.lock.impl.DefaultLockSession.LockSequence#lock(Object) lock(Object)}.
	 * 
	 * @throws Exception
	 */
    @Test
    public void testLock() throws Exception {
        ILockSequence lockSequence = underTest.beginLockSequence("");
        IMutex mutexMockOne = mockControl.createMock(IMutex.class);
        EasyMock.expect(lockSessionFactoryMock.getLockProvider()).andReturn(lockProviderMock);
        EasyMock.expect(lockProviderMock.acquireMutex("m")).andReturn(mutexMockOne);
        mutexMockOne.lock();
        mockControl.replay();
        lockSequence.lock("m");
        Assert.assertTrue(underTest.isLocalLockHeldByASequence("m"));
        lockSequence.lock("m");
        mockControl.verify();
        mockControl.reset();
    }

    /**
	 * Test cases for {@link net.sf.sasl.distributed.lock.impl.DefaultLockSession.LockSequence#tryLock(Object) tryLock(Object)} method.
	 * @throws Exception
	 */
    @Test
    public void testTryLockNoArguments() throws Exception {
        ILockSequence lockSequence = underTest.beginLockSequence("");
        IMutex mutexMockOne = mockControl.createMock(IMutex.class);
        EasyMock.expect(lockSessionFactoryMock.getLockProvider()).andReturn(lockProviderMock);
        EasyMock.expect(lockProviderMock.acquireMutex("m")).andReturn(mutexMockOne);
        EasyMock.expect(mutexMockOne.tryLock()).andReturn(true);
        mockControl.replay();
        lockSequence.tryLock("m");
        Assert.assertTrue(underTest.isLocalLockHeldByASequence("m"));
        lockSequence.tryLock("m");
        mockControl.verify();
        mockControl.reset();
    }

    /**
	 * Test cases for {@link net.sf.sasl.distributed.lock.impl.DefaultLockSession.LockSequence#tryLock(Object, long, java.util.concurrent.TimeUnit) tryLock(Object, long, TimeUnit)} method.
	 * @throws Exception
	 */
    @Test
    public void testTryLockWithArguments() throws Exception {
        ILockSequence lockSequence = underTest.beginLockSequence("");
        IMutex mutexMockOne = mockControl.createMock(IMutex.class);
        EasyMock.expect(lockSessionFactoryMock.getLockProvider()).andReturn(lockProviderMock);
        EasyMock.expect(lockProviderMock.acquireMutex("m")).andReturn(mutexMockOne);
        EasyMock.expect(mutexMockOne.tryLock(EasyMock.eq(1L), EasyMock.eq(TimeUnit.MILLISECONDS))).andReturn(true);
        mockControl.replay();
        lockSequence.tryLock("m", 1, TimeUnit.MILLISECONDS);
        Assert.assertTrue(underTest.isLocalLockHeldByASequence("m"));
        lockSequence.tryLock("m", 5, TimeUnit.MILLISECONDS);
        mockControl.verify();
        mockControl.reset();
    }

    /**
	 * Test cases for {@link net.sf.sasl.distributed.lock.impl.DefaultLockSession.LockSequence#unlock(Object) unlock(Object)} method.
	 * @throws Exception
	 */
    @Test
    public void testUnlock() throws Exception {
        ILockSequence lockSequence = underTest.beginLockSequence("");
        IMutex mutexMockOne = mockControl.createMock(IMutex.class);
        EasyMock.expect(lockSessionFactoryMock.getLockProvider()).andReturn(lockProviderMock);
        EasyMock.expect(lockProviderMock.acquireMutex("m")).andReturn(mutexMockOne);
        mutexMockOne.lock();
        mutexMockOne.unlock();
        mockControl.replay();
        lockSequence.lock("m");
        lockSequence.unlock("m");
        Assert.assertTrue(underTest.isLockKeyOwnedByASequence("m"));
        Assert.assertFalse(underTest.isLocalLockHeldByASequence("m"));
        mockControl.verify();
        mockControl.reset();
    }

    /**
	 * Test cases for the {@link net.sf.sasl.distributed.lock.impl.DefaultLockSession.LockSequence#lockInterruptibly(Object) lockInterruptibly(Object)} method.
	 * @throws Exception
	 */
    @Test
    public void testLockInterruptibly() throws Exception {
        ILockSequence lockSequence = underTest.beginLockSequence("");
        IMutex mutexMockOne = mockControl.createMock(IMutex.class);
        EasyMock.expect(lockSessionFactoryMock.getLockProvider()).andReturn(lockProviderMock);
        EasyMock.expect(lockProviderMock.acquireMutex("m")).andReturn(mutexMockOne);
        mutexMockOne.lockInterruptibly();
        mockControl.replay();
        lockSequence.lockInterruptibly("m");
        Assert.assertTrue(underTest.isLocalLockHeldByASequence("m"));
        lockSequence.lockInterruptibly("m");
        mockControl.verify();
        mockControl.reset();
    }

    /**
	 * Test cases for the {@link net.sf.sasl.distributed.lock.impl.DefaultLockSession.LockSequence#detach(IMutex) detach(IMutex)} method.
	 * 
	 * @throws Exception
	 */
    @Test
    public void testDetach() throws Exception {
        ILockSequence lockSequence = underTest.beginLockSequence("");
        IMutex mutexMockOne = mockControl.createMock(IMutex.class);
        EasyMock.expect(lockSessionFactoryMock.getLockProvider()).andReturn(lockProviderMock);
        EasyMock.expect(lockProviderMock.acquireMutex("m")).andReturn(mutexMockOne);
        mutexMockOne.lock();
        EasyMock.expect(mutexMockOne.getLockKey()).andReturn("m");
        mutexMockOne.unlock();
        mockControl.replay();
        IMutex mutex = lockSequence.lock("m");
        lockSequence.detach(mutex);
        Assert.assertFalse(underTest.isLocalLockHeldByASequence("m"));
        Assert.assertFalse(underTest.isLockKeyOwnedByASequence("m"));
        mockControl.verify();
        mockControl.reset();
    }

    /**
	 * Test cases for the {@link net.sf.sasl.distributed.lock.impl.DefaultLockSession.LockSequence#getMutex(Object) getMutex(Object)} method.
	 * 
	 * @throws Exception
	 */
    @Test
    public void testGetMutex() throws Exception {
        ILockSequence lockSequence = underTest.beginLockSequence("");
        IMutex mutexMockOne = mockControl.createMock(IMutex.class);
        EasyMock.expect(lockSessionFactoryMock.getLockProvider()).andReturn(lockProviderMock);
        EasyMock.expect(lockProviderMock.acquireMutex("m")).andReturn(mutexMockOne);
        mockControl.replay();
        Assert.assertNull(lockSequence.getMutex("m"));
        IMutex mutex = lockSequence.acquireMutex("m");
        Assert.assertEquals(mutex, lockSequence.getMutex("m"));
        mockControl.verify();
        mockControl.reset();
    }

    private void testThrowsException(Runnable runnable, Class<?> expectedExceptionClass) {
        try {
            runnable.run();
        } catch (Throwable throwable) {
            Assert.assertEquals(expectedExceptionClass, throwable.getClass());
        }
    }

    private class ExceptionHandler implements UncaughtExceptionHandler {

        private Throwable occurredException;

        public Throwable getOccurredException() {
            return occurredException;
        }

        /**
		 * @see java.lang.Thread.UncaughtExceptionHandler#uncaughtException(java.lang.Thread,
		 *      java.lang.Throwable)
		 */
        public void uncaughtException(Thread thread, Throwable throwable) {
            occurredException = throwable;
        }
    }
}

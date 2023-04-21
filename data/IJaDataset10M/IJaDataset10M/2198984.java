package com.cerner.system.rest.retry;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.util.concurrent.Callable;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * Unit tests for the {@link FaultBarrier} class.
 * 
 * @author Alex Horn
 */
@RunWith(MockitoJUnitRunner.class)
public class FaultBarrierTest {

    @Mock
    private FaultBarrierLogger loggerMock;

    @Mock
    private ReadWriteLock lockMock;

    @Mock
    private Lock readLockMock;

    @Mock
    private Lock writeLockMock;

    @Mock
    private Callable<Object> callableMock;

    @Mock
    private RetryHandler<Object> retryHandlerMock;

    private FaultBarrier barrier;

    @Before
    public void setup() {
        this.barrier = new FaultBarrier(this.lockMock, this.loggerMock);
        when(this.lockMock.readLock()).thenReturn(this.readLockMock);
        when(this.lockMock.writeLock()).thenReturn(this.writeLockMock);
    }

    @Test(expected = IllegalArgumentException.class)
    public void nullCallable() throws Exception {
        this.barrier.execute(null);
    }

    @Test
    public void normal() throws Exception {
        this.barrier.execute(this.callableMock, null);
        verify(this.readLockMock).lock();
        verify(this.readLockMock).unlock();
        verify(this.writeLockMock, never()).lock();
        verify(this.loggerMock, never()).recover((Exception) anyObject());
        verify(this.loggerMock, never()).abort((Exception) anyObject());
    }

    @Test
    public void recover() throws Exception {
        final Exception recover = new RuntimeException();
        when(this.callableMock.call()).thenThrow(recover);
        when(this.retryHandlerMock.retry(this.callableMock, recover)).thenReturn("42");
        assertEquals("42", this.barrier.execute(this.callableMock, this.retryHandlerMock));
        final InOrder inOrder = inOrder(this.callableMock, this.retryHandlerMock, this.loggerMock, this.readLockMock, this.writeLockMock);
        inOrder.verify(this.readLockMock).lock();
        inOrder.verify(this.callableMock).call();
        inOrder.verify(this.loggerMock).recover(recover);
        inOrder.verify(this.readLockMock).unlock();
        inOrder.verify(this.writeLockMock).lock();
        inOrder.verify(this.retryHandlerMock).retry(this.callableMock, recover);
        inOrder.verify(this.writeLockMock).unlock();
        verify(this.loggerMock, never()).abort((Exception) anyObject());
    }

    @Test
    public void recoverWithoutRetryHandler() throws Exception {
        final Exception recover = new RuntimeException();
        when(this.callableMock.call()).thenThrow(recover).thenReturn("42");
        assertEquals("42", this.barrier.execute(this.callableMock));
        final InOrder inOrder = inOrder(this.callableMock, this.loggerMock, this.readLockMock, this.writeLockMock);
        inOrder.verify(this.readLockMock).lock();
        inOrder.verify(this.callableMock).call();
        inOrder.verify(this.loggerMock).recover(recover);
        inOrder.verify(this.readLockMock).unlock();
        inOrder.verify(this.writeLockMock).lock();
        inOrder.verify(this.callableMock).call();
        inOrder.verify(this.writeLockMock).unlock();
        verify(this.loggerMock, never()).abort((Exception) anyObject());
    }

    @Test(expected = ArithmeticException.class)
    public void fatal() throws Exception {
        final Exception cause = new UnsupportedOperationException();
        final Exception fatal = new ArithmeticException();
        when(this.callableMock.call()).thenThrow(cause);
        when(this.retryHandlerMock.retry(this.callableMock, cause)).thenThrow(fatal);
        try {
            this.barrier.execute(this.callableMock, this.retryHandlerMock);
        } finally {
            final InOrder inOrder = inOrder(this.callableMock, this.retryHandlerMock, this.loggerMock, this.readLockMock, this.writeLockMock);
            inOrder.verify(this.readLockMock).lock();
            inOrder.verify(this.callableMock).call();
            inOrder.verify(this.loggerMock).recover(cause);
            inOrder.verify(this.readLockMock).unlock();
            inOrder.verify(this.writeLockMock).lock();
            inOrder.verify(this.retryHandlerMock).retry(this.callableMock, cause);
            inOrder.verify(this.loggerMock).abort(fatal);
            inOrder.verify(this.writeLockMock).unlock();
        }
    }

    @Test(expected = ArithmeticException.class)
    public void fatalWithRetryHandler() throws Exception {
        final Exception cause = new UnsupportedOperationException();
        final Exception fatal = new ArithmeticException();
        when(this.callableMock.call()).thenThrow(cause).thenThrow(fatal);
        try {
            this.barrier.execute(this.callableMock);
        } finally {
            final InOrder inOrder = inOrder(this.callableMock, this.loggerMock, this.readLockMock, this.writeLockMock);
            inOrder.verify(this.readLockMock).lock();
            inOrder.verify(this.callableMock).call();
            inOrder.verify(this.loggerMock).recover(cause);
            inOrder.verify(this.readLockMock).unlock();
            inOrder.verify(this.writeLockMock).lock();
            inOrder.verify(this.callableMock).call();
            inOrder.verify(this.loggerMock).abort(fatal);
            inOrder.verify(this.writeLockMock).unlock();
        }
    }
}

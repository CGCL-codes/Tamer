package jaxlib.thread.lock;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import javax.annotation.Nonnull;

/**
 * @author  <a href="mailto:joerg.wassmer@web.de">Joerg Wassmer</a>
 * @since   JaXLib 1.0
 * @version $Id: UnfairReentrantReadWriteLock.java 2791 2010-03-29 08:45:11Z joerg_wassmer $
 */
public class UnfairReentrantReadWriteLock extends SimpleReadWriteLock implements ReadWriteLock {

    /**
   * @since JaXLib 1.0
   */
    private static final long serialVersionUID = 1L;

    /**
   * @serial
   * @since JaXLib 1.0
   */
    public final UnfairReentrantReadWriteLock.ReadLock readLock;

    /**
   * @serial
   * @since JaXLib 1.0
   */
    public final UnfairReentrantReadWriteLock.WriteLock writeLock;

    public UnfairReentrantReadWriteLock() {
        super();
        this.readLock = new ReadLock(this);
        this.writeLock = new WriteLock(this);
    }

    @Override
    @Nonnull
    public final UnfairReentrantReadWriteLock.ReadLock readLock() {
        return this.readLock;
    }

    @Override
    @Nonnull
    public final UnfairReentrantReadWriteLock.WriteLock writeLock() {
        return this.writeLock;
    }

    public static final class ReadLock extends Object implements Lock, Serializable {

        /**
     * @since JaXLib 1.0
     */
        private static final long serialVersionUID = 1L;

        final UnfairReentrantReadWriteLock lock;

        ReadLock(final UnfairReentrantReadWriteLock lock) {
            super();
            this.lock = lock;
        }

        /**
     * Acquires the read lock. 
     *
     * <p>Acquires the read lock if the write lock is not held by
     * another thread and returns immediately.
     *
     * <p>If the write lock is held by another thread then
     * the current thread becomes disabled for thread scheduling
     * purposes and lies dormant until the read lock has been acquired.
     */
        @Override
        public final void lock() {
            this.lock.acquireShared(1);
        }

        /**
     * Acquires the read lock unless the current thread is 
     * {@link Thread#interrupt interrupted}.
     *
     * <p>Acquires the read lock if the write lock is not held
     * by another thread and returns immediately.
     *
     * <p>If the write lock is held by another thread then the
     * current thread becomes disabled for thread scheduling 
     * purposes and lies dormant until one of two things happens:
     *
     * <ul>
     *
     * <li>The read lock is acquired by the current thread; or
     *
     * <li>Some other thread {@link Thread#interrupt interrupts}
     * the current thread.
     *
     * </ul>
     *
     * <p>If the current thread:
     *
     * <ul>
     *
     * <li>has its interrupted status set on entry to this method; or 
     *
     * <li>is {@link Thread#interrupt interrupted} while acquiring 
     * the read lock,
     *
     * </ul>
     *
     * then {@link InterruptedException} is thrown and the current
     * thread's interrupted status is cleared.
     *
     * <p>In this implementation, as this method is an explicit
     * interruption point, preference is given to responding to
     * the interrupt over normal or reentrant acquisition of the
     * lock.
     *
     * @throws InterruptedException if the current thread is interrupted
     */
        @Override
        public final void lockInterruptibly() throws InterruptedException {
            this.lock.acquireSharedInterruptibly(1);
        }

        /**
     * Throws <tt>UnsupportedOperationException</tt> because <tt>ReadLocks</tt> do not support conditions.
     *
     * @throws UnsupportedOperationException always
    */
        @Override
        public final Condition newCondition() {
            throw new UnsupportedOperationException();
        }

        @Override
        public String toString() {
            return super.toString() + "[Read locks = " + this.lock.getReadLockCount() + "]";
        }

        /**
     * Acquires the read lock only if the write lock is not held by
     * another thread at the time of invocation.
     *
     * <p>Acquires the read lock if the write lock is not held by
     * another thread and returns immediately with the value
     * <tt>true</tt>. Even when this lock has been set to use a
     * fair ordering policy, a call to <tt>tryLock()</tt>
     * <em>will</em> immediately acquire the read lock if it is
     * available, whether or not other threads are currently
     * waiting for the read lock.  This &quot;barging&quot; behavior
     * can be useful in certain circumstances, even though it
     * breaks fairness. If you want to honor the fairness setting
     * for this lock, then use {@link #tryLock(long, TimeUnit)
     * tryLock(0, TimeUnit.SECONDS) } which is almost equivalent
     * (it also detects interruption).
     *
     * <p>If the write lock is held by another thread then
     * this method will return immediately with the value
     * <tt>false</tt>.
     *
     * @return <tt>true</tt> if the read lock was acquired.
     */
        @Override
        public final boolean tryLock() {
            return this.lock.nonfairTryAcquireShared(1) >= 0;
        }

        /**
     * Acquires the read lock if the write lock is not held by
     * another thread within the given waiting time and the
     * current thread has not been {@link Thread#interrupt
     * interrupted}.
     *
     * <p>Acquires the read lock if the write lock is not held by
     * another thread and returns immediately with the value
     * <tt>true</tt>. If this lock has been set to use a fair
     * ordering policy then an available lock <em>will not</em> be
     * acquired if any other threads are waiting for the
     * lock. This is in contrast to the {@link #tryLock()}
     * method. If you want a timed <tt>tryLock</tt> that does
     * permit barging on a fair lock then combine the timed and
     * un-timed forms together:
     *
     * <pre>if (lock.tryLock() || lock.tryLock(timeout, unit) ) { ... }
     * </pre>
     *
     * <p>If the write lock is held by another thread then the
     * current thread becomes disabled for thread scheduling 
     * purposes and lies dormant until one of three things happens:
     *
     * <ul>
     *
     * <li>The read lock is acquired by the current thread; or
     *
     * <li>Some other thread {@link Thread#interrupt interrupts} the current
     * thread; or
     *
     * <li>The specified waiting time elapses
     *
     * </ul>
     *
     * <p>If the read lock is acquired then the value <tt>true</tt> is
     * returned.
     *
     * <p>If the current thread:
     *
     * <ul>
     *
     * <li>has its interrupted status set on entry to this method; or 
     *
     * <li>is {@link Thread#interrupt interrupted} while acquiring
     * the read lock,
     *
     * </ul> then {@link InterruptedException} is thrown and the
     * current thread's interrupted status is cleared.
     *
     * <p>If the specified waiting time elapses then the value
     * <tt>false</tt> is returned.  If the time is less than or
     * equal to zero, the method will not wait at all.
     *
     * <p>In this implementation, as this method is an explicit
     * interruption point, preference is given to responding to
     * the interrupt over normal or reentrant acquisition of the
     * lock, and over reporting the elapse of the waiting time.
     *
     * @param timeout the time to wait for the read lock
     * @param unit the time unit of the timeout argument
     *
     * @return <tt>true</tt> if the read lock was acquired.
     *
     * @throws InterruptedException if the current thread is interrupted
     * @throws NullPointerException if unit is null
     *
     */
        @Override
        public final boolean tryLock(final long timeout, final TimeUnit unit) throws InterruptedException {
            return this.lock.tryAcquireSharedNanos(1, unit.toNanos(timeout));
        }

        /**
     * Attempts to release this lock.  
     *
     * <p> If the number of readers is now zero then the lock
     * is made available for write lock attempts.
     */
        @Override
        public final void unlock() {
            this.lock.releaseShared(1);
        }
    }

    public static final class WriteLock extends Object implements Lock, Serializable {

        /**
     * @since JaXLib 1.0
     */
        private static final long serialVersionUID = 1L;

        final UnfairReentrantReadWriteLock lock;

        WriteLock(final UnfairReentrantReadWriteLock lock) {
            super();
            this.lock = lock;
        }

        /**
     * Acquire the write lock. 
     *
     * <p>Acquires the write lock if neither the read nor write lock
     * are held by another thread
     * and returns immediately, setting the write lock hold count to
     * one.
     *
     * <p>If the current thread already holds the write lock then the
     * hold count is incremented by one and the method returns
     * immediately.
     *
     * <p>If the lock is held by another thread then the current
     * thread becomes disabled for thread scheduling purposes and
     * lies dormant until the write lock has been acquired, at which
     * time the write lock hold count is set to one.
     */
        @Override
        public final void lock() {
            this.lock.beginWrite();
        }

        /**
     * Acquires the write lock unless the current thread is {@link
     * Thread#interrupt interrupted}.
     *
     * <p>Acquires the write lock if neither the read nor write lock
     * are held by another thread
     * and returns immediately, setting the write lock hold count to
     * one.
     *
     * <p>If the current thread already holds this lock then the
     * hold count is incremented by one and the method returns
     * immediately.
     *
     * <p>If the lock is held by another thread then the current
     * thread becomes disabled for thread scheduling purposes and
     * lies dormant until one of two things happens:
     *
     * <ul>
     *
     * <li>The write lock is acquired by the current thread; or
     *
     * <li>Some other thread {@link Thread#interrupt interrupts}
     * the current thread.
     *
     * </ul>
     *
     * <p>If the write lock is acquired by the current thread then the
     * lock hold count is set to one.
     *
     * <p>If the current thread:
     *
     * <ul>
     *
     * <li>has its interrupted status set on entry to this method;
     * or
     *
     * <li>is {@link Thread#interrupt interrupted} while acquiring
     * the write lock,
     *
     * </ul>
     *
     * then {@link InterruptedException} is thrown and the current
     * thread's interrupted status is cleared.
     *
     * <p>In this implementation, as this method is an explicit
     * interruption point, preference is given to responding to
     * the interrupt over normal or reentrant acquisition of the
     * lock.
     *
     * @throws InterruptedException if the current thread is interrupted
     */
        @Override
        public final void lockInterruptibly() throws InterruptedException {
            this.lock.acquireInterruptibly(1);
        }

        /**
     * Returns a {@link Condition} instance for use with this
     * {@link Lock} instance. 
     * <p>The returned {@link Condition} instance supports the same
     * usages as do the {@link Object} monitor methods ({@link
     * Object#wait() wait}, {@link Object#notify notify}, and {@link
     * Object#notifyAll notifyAll}) when used with the built-in
     * monitor lock.
     *
     * <ul>
     *
     * <li>If this write lock is not held when any {@link
     * Condition} method is called then an {@link
     * IllegalMonitorStateException} is thrown.  (Read locks are
     * held independently of write locks, so are not checked or
     * affected. However it is essentially always an error to
     * invoke a condition waiting method when the current thread
     * has also acquired read locks, since other threads that
     * could unblock it will not be able to acquire the write
     * lock.)
     *
     * <li>When the condition {@link Condition#await() waiting}
     * methods are called the write lock is released and, before
     * they return, the write lock is reacquired and the lock hold
     * count restored to what it was when the method was called.
     *
     * <li>If a thread is {@link Thread#interrupt interrupted} while
     * waiting then the wait will terminate, an {@link
     * InterruptedException} will be thrown, and the thread's
     * interrupted status will be cleared.
     *
     * <li> Waiting threads are signalled in FIFO order.
     *
     * <li>The ordering of lock reacquisition for threads returning
     * from waiting methods is the same as for threads initially
     * acquiring the lock, which is in the default case not specified,
     * but for <em>fair</em> locks favors those threads that have been
     * waiting the longest.
     * 
     * </ul>
     * @return the Condition object
     */
        @Override
        public final AbstractQueuedSynchronizer.ConditionObject newCondition() {
            return this.lock.createCondition();
        }

        /**
     * Returns a string identifying this lock, as well as its lock
     * state.  The state, in brackets includes either the String
     * &quot;Unlocked&quot; or the String &quot;Locked by&quot;
     * followed by the {@link Thread#getName} of the owning thread.
     * @return a string identifying this lock, as well as its lock state.
     */
        @Override
        public final String toString() {
            Thread owner = this.lock.getExclusiveOwnerThread();
            return super.toString() + ((owner == null) ? "[Unlocked]" : "[Locked by thread " + owner.getName() + "]");
        }

        /**
     * Acquires the write lock only if it is not held by another thread
     * at the time of invocation.
     *
     * <p>Acquires the write lock if neither the read nor write lock
     * are held by another thread
     * and returns immediately with the value <tt>true</tt>,
     * setting the write lock hold count to one. Even when this lock has
     * been set to use a fair ordering policy, a call to
     * <tt>tryLock()</tt> <em>will</em> immediately acquire the
     * lock if it is available, whether or not other threads are
     * currently waiting for the write lock.  This &quot;barging&quot;
     * behavior can be useful in certain circumstances, even
     * though it breaks fairness. If you want to honor the
     * fairness setting for this lock, then use {@link
     * #tryLock(long, TimeUnit) tryLock(0, TimeUnit.SECONDS) }
     * which is almost equivalent (it also detects interruption).
     *
     * <p> If the current thread already holds this lock then the
     * hold count is incremented by one and the method returns
     * <tt>true</tt>.
     *
     * <p>If the lock is held by another thread then this method
     * will return immediately with the value <tt>false</tt>.
     *
     * @return <tt>true</tt> if the lock was free and was acquired
     * by the current thread, or the write lock was already held
     * by the current thread; and <tt>false</tt> otherwise.
     */
        @Override
        public final boolean tryLock() {
            return this.lock.nonfairTryAcquire(1);
        }

        /**
     * Acquires the write lock if it is not held by another thread
     * within the given waiting time and the current thread has
     * not been {@link Thread#interrupt interrupted}.
     *
     * <p>Acquires the write lock if neither the read nor write lock
     * are held by another thread
     * and returns immediately with the value <tt>true</tt>,
     * setting the write lock hold count to one. If this lock has been
     * set to use a fair ordering policy then an available lock
     * <em>will not</em> be acquired if any other threads are
     * waiting for the write lock. This is in contrast to the {@link
     * #tryLock()} method. If you want a timed <tt>tryLock</tt>
     * that does permit barging on a fair lock then combine the
     * timed and un-timed forms together:
     *
     * <pre>if (lock.tryLock() || lock.tryLock(timeout, unit) ) { ... }
     * </pre>
     *
     * <p>If the current thread already holds this lock then the
     * hold count is incremented by one and the method returns
     * <tt>true</tt>.
     *
     * <p>If the lock is held by another thread then the current
     * thread becomes disabled for thread scheduling purposes and
     * lies dormant until one of three things happens:
     *
     * <ul>
     *
     * <li>The write lock is acquired by the current thread; or
     *
     * <li>Some other thread {@link Thread#interrupt interrupts}
     * the current thread; or
     *
     * <li>The specified waiting time elapses
     *
     * </ul>
     *
     * <p>If the write lock is acquired then the value <tt>true</tt> is
     * returned and the write lock hold count is set to one.
     *
     * <p>If the current thread:
     *
     * <ul>
     *
     * <li>has its interrupted status set on entry to this method;
     * or
     *
     * <li>is {@link Thread#interrupt interrupted} while acquiring
     * the write lock,
     *
     * </ul> 
     *
     * then {@link InterruptedException} is thrown and the current
     * thread's interrupted status is cleared.
     *
     * <p>If the specified waiting time elapses then the value
     * <tt>false</tt> is returned.  If the time is less than or
     * equal to zero, the method will not wait at all.
     *
     * <p>In this implementation, as this method is an explicit
     * interruption point, preference is given to responding to
     * the interrupt over normal or reentrant acquisition of the
     * lock, and over reporting the elapse of the waiting time.
     *
     * @param timeout the time to wait for the write lock
     * @param unit the time unit of the timeout argument
     *
     * @return <tt>true</tt> if the lock was free and was acquired
     * by the current thread, or the write lock was already held by the
     * current thread; and <tt>false</tt> if the waiting time
     * elapsed before the lock could be acquired.
     *
     * @throws InterruptedException if the current thread is interrupted
     * @throws NullPointerException if unit is null
     *
     */
        @Override
        public final boolean tryLock(final long timeout, final TimeUnit unit) throws InterruptedException {
            return this.lock.tryAcquireNanos(1, unit.toNanos(timeout));
        }

        /**
     * Attempts to release this lock.  
     *
     * <p>If the current thread is the holder of this lock then
     * the hold count is decremented. If the hold count is now
     * zero then the lock is released.  If the current thread is
     * not the holder of this lock then {@link
     * IllegalMonitorStateException} is thrown.
     * @throws IllegalMonitorStateException if the current thread does not
     * hold this lock.
     */
        @Override
        public final void unlock() {
            this.lock.release(1);
        }
    }
}

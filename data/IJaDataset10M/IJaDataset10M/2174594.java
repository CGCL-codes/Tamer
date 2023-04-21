package java.util.concurrent;

import java.util.concurrent.locks.*;
import java.util.concurrent.atomic.*;
import java.util.*;

/**
 * A {@linkplain BlockingQueue blocking queue} in which each insert
 * operation must wait for a corresponding remove operation by another
 * thread, and vice versa.  A synchronous queue does not have any
 * internal capacity, not even a capacity of one.  You cannot
 * <tt>peek</tt> at a synchronous queue because an element is only
 * present when you try to remove it; you cannot insert an element
 * (using any method) unless another thread is trying to remove it;
 * you cannot iterate as there is nothing to iterate.  The
 * <em>head</em> of the queue is the element that the first queued
 * inserting thread is trying to add to the queue; if there is no such
 * queued thread then no element is available for removal and
 * <tt>poll()</tt> will return <tt>null</tt>.  For purposes of other
 * <tt>Collection</tt> methods (for example <tt>contains</tt>), a
 * <tt>SynchronousQueue</tt> acts as an empty collection.  This queue
 * does not permit <tt>null</tt> elements.
 *
 * <p>Synchronous queues are similar to rendezvous channels used in
 * CSP and Ada. They are well suited for handoff designs, in which an
 * object running in one thread must sync up with an object running
 * in another thread in order to hand it some information, event, or
 * task.
 *
 * <p> This class supports an optional fairness policy for ordering
 * waiting producer and consumer threads.  By default, this ordering
 * is not guaranteed. However, a queue constructed with fairness set
 * to <tt>true</tt> grants threads access in FIFO order.
 *
 * <p>This class and its iterator implement all of the
 * <em>optional</em> methods of the {@link Collection} and {@link
 * Iterator} interfaces.
 *
 * <p>This class is a member of the
 * <a href="{@docRoot}/../technotes/guides/collections/index.html">
 * Java Collections Framework</a>.
 *
 * @since 1.5
 * @author Doug Lea and Bill Scherer and Michael Scott
 * @param <E> the type of elements held in this collection
 */
public class SynchronousQueue<E> extends AbstractQueue<E> implements BlockingQueue<E>, java.io.Serializable {

    private static final long serialVersionUID = -3223113410248163686L;

    /**
     * Shared internal API for dual stacks and queues.
     */
    abstract static class Transferer {

        /**
         * Performs a put or take.
         *
         * @param e if non-null, the item to be handed to a consumer;
         *          if null, requests that transfer return an item
         *          offered by producer.
         * @param timed if this operation should timeout
         * @param nanos the timeout, in nanoseconds
         * @return if non-null, the item provided or received; if null,
         *         the operation failed due to timeout or interrupt --
         *         the caller can distinguish which of these occurred
         *         by checking Thread.interrupted.
         */
        abstract Object transfer(Object e, boolean timed, long nanos);
    }

    /** The number of CPUs, for spin control */
    static final int NCPUS = Runtime.getRuntime().availableProcessors();

    /**
     * The number of times to spin before blocking in timed waits.
     * The value is empirically derived -- it works well across a
     * variety of processors and OSes. Empirically, the best value
     * seems not to vary with number of CPUs (beyond 2) so is just
     * a constant.
     */
    static final int maxTimedSpins = (NCPUS < 2) ? 0 : 32;

    /**
     * The number of times to spin before blocking in untimed waits.
     * This is greater than timed value because untimed waits spin
     * faster since they don't need to check times on each spin.
     */
    static final int maxUntimedSpins = maxTimedSpins * 16;

    /**
     * The number of nanoseconds for which it is faster to spin
     * rather than to use timed park. A rough estimate suffices.
     */
    static final long spinForTimeoutThreshold = 1000L;

    /** Dual stack */
    static final class TransferStack extends Transferer {

        /** Node represents an unfulfilled consumer */
        static final int REQUEST = 0;

        /** Node represents an unfulfilled producer */
        static final int DATA = 1;

        /** Node is fulfilling another unfulfilled DATA or REQUEST */
        static final int FULFILLING = 2;

        /** Return true if m has fulfilling bit set */
        static boolean isFulfilling(int m) {
            return (m & FULFILLING) != 0;
        }

        /** Node class for TransferStacks. */
        static final class SNode {

            volatile SNode next;

            volatile SNode match;

            volatile Thread waiter;

            Object item;

            int mode;

            SNode(Object item) {
                this.item = item;
            }

            static final AtomicReferenceFieldUpdater<SNode, SNode> nextUpdater = AtomicReferenceFieldUpdater.newUpdater(SNode.class, SNode.class, "next");

            boolean casNext(SNode cmp, SNode val) {
                return (cmp == next && nextUpdater.compareAndSet(this, cmp, val));
            }

            static final AtomicReferenceFieldUpdater<SNode, SNode> matchUpdater = AtomicReferenceFieldUpdater.newUpdater(SNode.class, SNode.class, "match");

            /**
             * Tries to match node s to this node, if so, waking up thread.
             * Fulfillers call tryMatch to identify their waiters.
             * Waiters block until they have been matched.
             *
             * @param s the node to match
             * @return true if successfully matched to s
             */
            boolean tryMatch(SNode s) {
                if (match == null && matchUpdater.compareAndSet(this, null, s)) {
                    Thread w = waiter;
                    if (w != null) {
                        waiter = null;
                        LockSupport.unpark(w);
                    }
                    return true;
                }
                return match == s;
            }

            /**
             * Tries to cancel a wait by matching node to itself.
             */
            void tryCancel() {
                matchUpdater.compareAndSet(this, null, this);
            }

            boolean isCancelled() {
                return match == this;
            }
        }

        /** The head (top) of the stack */
        volatile SNode head;

        static final AtomicReferenceFieldUpdater<TransferStack, SNode> headUpdater = AtomicReferenceFieldUpdater.newUpdater(TransferStack.class, SNode.class, "head");

        boolean casHead(SNode h, SNode nh) {
            return h == head && headUpdater.compareAndSet(this, h, nh);
        }

        /**
         * Creates or resets fields of a node. Called only from transfer
         * where the node to push on stack is lazily created and
         * reused when possible to help reduce intervals between reads
         * and CASes of head and to avoid surges of garbage when CASes
         * to push nodes fail due to contention.
         */
        static SNode snode(SNode s, Object e, SNode next, int mode) {
            if (s == null) s = new SNode(e);
            s.mode = mode;
            s.next = next;
            return s;
        }

        /**
         * Puts or takes an item.
         */
        Object transfer(Object e, boolean timed, long nanos) {
            SNode s = null;
            int mode = (e == null) ? REQUEST : DATA;
            for (; ; ) {
                SNode h = head;
                if (h == null || h.mode == mode) {
                    if (timed && nanos <= 0) {
                        if (h != null && h.isCancelled()) casHead(h, h.next); else return null;
                    } else if (casHead(h, s = snode(s, e, h, mode))) {
                        SNode m = awaitFulfill(s, timed, nanos);
                        if (m == s) {
                            clean(s);
                            return null;
                        }
                        if ((h = head) != null && h.next == s) casHead(h, s.next);
                        return mode == REQUEST ? m.item : s.item;
                    }
                } else if (!isFulfilling(h.mode)) {
                    if (h.isCancelled()) casHead(h, h.next); else if (casHead(h, s = snode(s, e, h, FULFILLING | mode))) {
                        for (; ; ) {
                            SNode m = s.next;
                            if (m == null) {
                                casHead(s, null);
                                s = null;
                                break;
                            }
                            SNode mn = m.next;
                            if (m.tryMatch(s)) {
                                casHead(s, mn);
                                return (mode == REQUEST) ? m.item : s.item;
                            } else s.casNext(m, mn);
                        }
                    }
                } else {
                    SNode m = h.next;
                    if (m == null) casHead(h, null); else {
                        SNode mn = m.next;
                        if (m.tryMatch(h)) casHead(h, mn); else h.casNext(m, mn);
                    }
                }
            }
        }

        /**
         * Spins/blocks until node s is matched by a fulfill operation.
         *
         * @param s the waiting node
         * @param timed true if timed wait
         * @param nanos timeout value
         * @return matched node, or s if cancelled
         */
        SNode awaitFulfill(SNode s, boolean timed, long nanos) {
            long lastTime = (timed) ? System.nanoTime() : 0;
            Thread w = Thread.currentThread();
            SNode h = head;
            int spins = (shouldSpin(s) ? (timed ? maxTimedSpins : maxUntimedSpins) : 0);
            for (; ; ) {
                if (w.isInterrupted()) s.tryCancel();
                SNode m = s.match;
                if (m != null) return m;
                if (timed) {
                    long now = System.nanoTime();
                    nanos -= now - lastTime;
                    lastTime = now;
                    if (nanos <= 0) {
                        s.tryCancel();
                        continue;
                    }
                }
                if (spins > 0) spins = shouldSpin(s) ? (spins - 1) : 0; else if (s.waiter == null) s.waiter = w; else if (!timed) LockSupport.park(); else if (nanos > spinForTimeoutThreshold) LockSupport.parkNanos(nanos);
            }
        }

        /**
         * Returns true if node s is at head or there is an active
         * fulfiller.
         */
        boolean shouldSpin(SNode s) {
            SNode h = head;
            return (h == s || h == null || isFulfilling(h.mode));
        }

        /**
         * Unlinks s from the stack.
         */
        void clean(SNode s) {
            s.item = null;
            s.waiter = null;
            SNode past = s.next;
            if (past != null && past.isCancelled()) past = past.next;
            SNode p;
            while ((p = head) != null && p != past && p.isCancelled()) casHead(p, p.next);
            while (p != null && p != past) {
                SNode n = p.next;
                if (n != null && n.isCancelled()) p.casNext(n, n.next); else p = n;
            }
        }
    }

    /** Dual Queue */
    static final class TransferQueue extends Transferer {

        /** Node class for TransferQueue. */
        static final class QNode {

            volatile QNode next;

            volatile Object item;

            volatile Thread waiter;

            final boolean isData;

            QNode(Object item, boolean isData) {
                this.item = item;
                this.isData = isData;
            }

            static final AtomicReferenceFieldUpdater<QNode, QNode> nextUpdater = AtomicReferenceFieldUpdater.newUpdater(QNode.class, QNode.class, "next");

            boolean casNext(QNode cmp, QNode val) {
                return (next == cmp && nextUpdater.compareAndSet(this, cmp, val));
            }

            static final AtomicReferenceFieldUpdater<QNode, Object> itemUpdater = AtomicReferenceFieldUpdater.newUpdater(QNode.class, Object.class, "item");

            boolean casItem(Object cmp, Object val) {
                return (item == cmp && itemUpdater.compareAndSet(this, cmp, val));
            }

            /**
             * Tries to cancel by CAS'ing ref to this as item.
             */
            void tryCancel(Object cmp) {
                itemUpdater.compareAndSet(this, cmp, this);
            }

            boolean isCancelled() {
                return item == this;
            }

            /**
             * Returns true if this node is known to be off the queue
             * because its next pointer has been forgotten due to
             * an advanceHead operation.
             */
            boolean isOffList() {
                return next == this;
            }
        }

        /** Head of queue */
        transient volatile QNode head;

        /** Tail of queue */
        transient volatile QNode tail;

        /**
         * Reference to a cancelled node that might not yet have been
         * unlinked from queue because it was the last inserted node
         * when it cancelled.
         */
        transient volatile QNode cleanMe;

        TransferQueue() {
            QNode h = new QNode(null, false);
            head = h;
            tail = h;
        }

        static final AtomicReferenceFieldUpdater<TransferQueue, QNode> headUpdater = AtomicReferenceFieldUpdater.newUpdater(TransferQueue.class, QNode.class, "head");

        /**
         * Tries to cas nh as new head; if successful, unlink
         * old head's next node to avoid garbage retention.
         */
        void advanceHead(QNode h, QNode nh) {
            if (h == head && headUpdater.compareAndSet(this, h, nh)) h.next = h;
        }

        static final AtomicReferenceFieldUpdater<TransferQueue, QNode> tailUpdater = AtomicReferenceFieldUpdater.newUpdater(TransferQueue.class, QNode.class, "tail");

        /**
         * Tries to cas nt as new tail.
         */
        void advanceTail(QNode t, QNode nt) {
            if (tail == t) tailUpdater.compareAndSet(this, t, nt);
        }

        static final AtomicReferenceFieldUpdater<TransferQueue, QNode> cleanMeUpdater = AtomicReferenceFieldUpdater.newUpdater(TransferQueue.class, QNode.class, "cleanMe");

        /**
         * Tries to CAS cleanMe slot.
         */
        boolean casCleanMe(QNode cmp, QNode val) {
            return (cleanMe == cmp && cleanMeUpdater.compareAndSet(this, cmp, val));
        }

        /**
         * Puts or takes an item.
         */
        Object transfer(Object e, boolean timed, long nanos) {
            QNode s = null;
            boolean isData = (e != null);
            for (; ; ) {
                QNode t = tail;
                QNode h = head;
                if (t == null || h == null) continue;
                if (h == t || t.isData == isData) {
                    QNode tn = t.next;
                    if (t != tail) continue;
                    if (tn != null) {
                        advanceTail(t, tn);
                        continue;
                    }
                    if (timed && nanos <= 0) return null;
                    if (s == null) s = new QNode(e, isData);
                    if (!t.casNext(null, s)) continue;
                    advanceTail(t, s);
                    Object x = awaitFulfill(s, e, timed, nanos);
                    if (x == s) {
                        clean(t, s);
                        return null;
                    }
                    if (!s.isOffList()) {
                        advanceHead(t, s);
                        if (x != null) s.item = s;
                        s.waiter = null;
                    }
                    return (x != null) ? x : e;
                } else {
                    QNode m = h.next;
                    if (t != tail || m == null || h != head) continue;
                    Object x = m.item;
                    if (isData == (x != null) || x == m || !m.casItem(x, e)) {
                        advanceHead(h, m);
                        continue;
                    }
                    advanceHead(h, m);
                    LockSupport.unpark(m.waiter);
                    return (x != null) ? x : e;
                }
            }
        }

        /**
         * Spins/blocks until node s is fulfilled.
         *
         * @param s the waiting node
         * @param e the comparison value for checking match
         * @param timed true if timed wait
         * @param nanos timeout value
         * @return matched item, or s if cancelled
         */
        Object awaitFulfill(QNode s, Object e, boolean timed, long nanos) {
            long lastTime = (timed) ? System.nanoTime() : 0;
            Thread w = Thread.currentThread();
            int spins = ((head.next == s) ? (timed ? maxTimedSpins : maxUntimedSpins) : 0);
            for (; ; ) {
                if (w.isInterrupted()) s.tryCancel(e);
                Object x = s.item;
                if (x != e) return x;
                if (timed) {
                    long now = System.nanoTime();
                    nanos -= now - lastTime;
                    lastTime = now;
                    if (nanos <= 0) {
                        s.tryCancel(e);
                        continue;
                    }
                }
                if (spins > 0) --spins; else if (s.waiter == null) s.waiter = w; else if (!timed) LockSupport.park(); else if (nanos > spinForTimeoutThreshold) LockSupport.parkNanos(nanos);
            }
        }

        /**
         * Gets rid of cancelled node s with original predecessor pred.
         */
        void clean(QNode pred, QNode s) {
            s.waiter = null;
            while (pred.next == s) {
                QNode h = head;
                QNode hn = h.next;
                if (hn != null && hn.isCancelled()) {
                    advanceHead(h, hn);
                    continue;
                }
                QNode t = tail;
                if (t == h) return;
                QNode tn = t.next;
                if (t != tail) continue;
                if (tn != null) {
                    advanceTail(t, tn);
                    continue;
                }
                if (s != t) {
                    QNode sn = s.next;
                    if (sn == s || pred.casNext(s, sn)) return;
                }
                QNode dp = cleanMe;
                if (dp != null) {
                    QNode d = dp.next;
                    QNode dn;
                    if (d == null || d == dp || !d.isCancelled() || (d != t && (dn = d.next) != null && dn != d && dp.casNext(d, dn))) casCleanMe(dp, null);
                    if (dp == pred) return;
                } else if (casCleanMe(null, pred)) return;
            }
        }
    }

    /**
     * The transferer. Set only in constructor, but cannot be declared
     * as final without further complicating serialization.  Since
     * this is accessed only at most once per public method, there
     * isn't a noticeable performance penalty for using volatile
     * instead of final here.
     */
    private transient volatile Transferer transferer;

    /**
     * Creates a <tt>SynchronousQueue</tt> with nonfair access policy.
     */
    public SynchronousQueue() {
        this(false);
    }

    /**
     * Creates a <tt>SynchronousQueue</tt> with the specified fairness policy.
     *
     * @param fair if true, waiting threads contend in FIFO order for
     *        access; otherwise the order is unspecified.
     */
    public SynchronousQueue(boolean fair) {
        transferer = (fair) ? new TransferQueue() : new TransferStack();
    }

    /**
     * Adds the specified element to this queue, waiting if necessary for
     * another thread to receive it.
     *
     * @throws InterruptedException {@inheritDoc}
     * @throws NullPointerException {@inheritDoc}
     */
    public void put(E o) throws InterruptedException {
        if (o == null) throw new NullPointerException();
        if (transferer.transfer(o, false, 0) == null) {
            Thread.interrupted();
            throw new InterruptedException();
        }
    }

    /**
     * Inserts the specified element into this queue, waiting if necessary
     * up to the specified wait time for another thread to receive it.
     *
     * @return <tt>true</tt> if successful, or <tt>false</tt> if the
     *         specified waiting time elapses before a consumer appears.
     * @throws InterruptedException {@inheritDoc}
     * @throws NullPointerException {@inheritDoc}
     */
    public boolean offer(E o, long timeout, TimeUnit unit) throws InterruptedException {
        if (o == null) throw new NullPointerException();
        if (transferer.transfer(o, true, unit.toNanos(timeout)) != null) return true;
        if (!Thread.interrupted()) return false;
        throw new InterruptedException();
    }

    /**
     * Inserts the specified element into this queue, if another thread is
     * waiting to receive it.
     *
     * @param e the element to add
     * @return <tt>true</tt> if the element was added to this queue, else
     *         <tt>false</tt>
     * @throws NullPointerException if the specified element is null
     */
    public boolean offer(E e) {
        if (e == null) throw new NullPointerException();
        return transferer.transfer(e, true, 0) != null;
    }

    /**
     * Retrieves and removes the head of this queue, waiting if necessary
     * for another thread to insert it.
     *
     * @return the head of this queue
     * @throws InterruptedException {@inheritDoc}
     */
    public E take() throws InterruptedException {
        Object e = transferer.transfer(null, false, 0);
        if (e != null) return (E) e;
        Thread.interrupted();
        throw new InterruptedException();
    }

    /**
     * Retrieves and removes the head of this queue, waiting
     * if necessary up to the specified wait time, for another thread
     * to insert it.
     *
     * @return the head of this queue, or <tt>null</tt> if the
     *         specified waiting time elapses before an element is present.
     * @throws InterruptedException {@inheritDoc}
     */
    public E poll(long timeout, TimeUnit unit) throws InterruptedException {
        Object e = transferer.transfer(null, true, unit.toNanos(timeout));
        if (e != null || !Thread.interrupted()) return (E) e;
        throw new InterruptedException();
    }

    /**
     * Retrieves and removes the head of this queue, if another thread
     * is currently making an element available.
     *
     * @return the head of this queue, or <tt>null</tt> if no
     *         element is available.
     */
    public E poll() {
        return (E) transferer.transfer(null, true, 0);
    }

    /**
     * Always returns <tt>true</tt>.
     * A <tt>SynchronousQueue</tt> has no internal capacity.
     *
     * @return <tt>true</tt>
     */
    public boolean isEmpty() {
        return true;
    }

    /**
     * Always returns zero.
     * A <tt>SynchronousQueue</tt> has no internal capacity.
     *
     * @return zero.
     */
    public int size() {
        return 0;
    }

    /**
     * Always returns zero.
     * A <tt>SynchronousQueue</tt> has no internal capacity.
     *
     * @return zero.
     */
    public int remainingCapacity() {
        return 0;
    }

    /**
     * Does nothing.
     * A <tt>SynchronousQueue</tt> has no internal capacity.
     */
    public void clear() {
    }

    /**
     * Always returns <tt>false</tt>.
     * A <tt>SynchronousQueue</tt> has no internal capacity.
     *
     * @param o the element
     * @return <tt>false</tt>
     */
    public boolean contains(Object o) {
        return false;
    }

    /**
     * Always returns <tt>false</tt>.
     * A <tt>SynchronousQueue</tt> has no internal capacity.
     *
     * @param o the element to remove
     * @return <tt>false</tt>
     */
    public boolean remove(Object o) {
        return false;
    }

    /**
     * Returns <tt>false</tt> unless the given collection is empty.
     * A <tt>SynchronousQueue</tt> has no internal capacity.
     *
     * @param c the collection
     * @return <tt>false</tt> unless given collection is empty
     */
    public boolean containsAll(Collection<?> c) {
        return c.isEmpty();
    }

    /**
     * Always returns <tt>false</tt>.
     * A <tt>SynchronousQueue</tt> has no internal capacity.
     *
     * @param c the collection
     * @return <tt>false</tt>
     */
    public boolean removeAll(Collection<?> c) {
        return false;
    }

    /**
     * Always returns <tt>false</tt>.
     * A <tt>SynchronousQueue</tt> has no internal capacity.
     *
     * @param c the collection
     * @return <tt>false</tt>
     */
    public boolean retainAll(Collection<?> c) {
        return false;
    }

    /**
     * Always returns <tt>null</tt>.
     * A <tt>SynchronousQueue</tt> does not return elements
     * unless actively waited on.
     *
     * @return <tt>null</tt>
     */
    public E peek() {
        return null;
    }

    static class EmptyIterator<E> implements Iterator<E> {

        public boolean hasNext() {
            return false;
        }

        public E next() {
            throw new NoSuchElementException();
        }

        public void remove() {
            throw new IllegalStateException();
        }
    }

    /**
     * Returns an empty iterator in which <tt>hasNext</tt> always returns
     * <tt>false</tt>.
     *
     * @return an empty iterator
     */
    public Iterator<E> iterator() {
        return new EmptyIterator<E>();
    }

    /**
     * Returns a zero-length array.
     * @return a zero-length array
     */
    public Object[] toArray() {
        return new Object[0];
    }

    /**
     * Sets the zeroeth element of the specified array to <tt>null</tt>
     * (if the array has non-zero length) and returns it.
     *
     * @param a the array
     * @return the specified array
     * @throws NullPointerException if the specified array is null
     */
    public <T> T[] toArray(T[] a) {
        if (a.length > 0) a[0] = null;
        return a;
    }

    /**
     * @throws UnsupportedOperationException {@inheritDoc}
     * @throws ClassCastException            {@inheritDoc}
     * @throws NullPointerException          {@inheritDoc}
     * @throws IllegalArgumentException      {@inheritDoc}
     */
    public int drainTo(Collection<? super E> c) {
        if (c == null) throw new NullPointerException();
        if (c == this) throw new IllegalArgumentException();
        int n = 0;
        E e;
        while ((e = poll()) != null) {
            c.add(e);
            ++n;
        }
        return n;
    }

    /**
     * @throws UnsupportedOperationException {@inheritDoc}
     * @throws ClassCastException            {@inheritDoc}
     * @throws NullPointerException          {@inheritDoc}
     * @throws IllegalArgumentException      {@inheritDoc}
     */
    public int drainTo(Collection<? super E> c, int maxElements) {
        if (c == null) throw new NullPointerException();
        if (c == this) throw new IllegalArgumentException();
        int n = 0;
        E e;
        while (n < maxElements && (e = poll()) != null) {
            c.add(e);
            ++n;
        }
        return n;
    }

    static class WaitQueue implements java.io.Serializable {
    }

    static class LifoWaitQueue extends WaitQueue {

        private static final long serialVersionUID = -3633113410248163686L;
    }

    static class FifoWaitQueue extends WaitQueue {

        private static final long serialVersionUID = -3623113410248163686L;
    }

    private ReentrantLock qlock;

    private WaitQueue waitingProducers;

    private WaitQueue waitingConsumers;

    /**
     * Save the state to a stream (that is, serialize it).
     *
     * @param s the stream
     */
    private void writeObject(java.io.ObjectOutputStream s) throws java.io.IOException {
        boolean fair = transferer instanceof TransferQueue;
        if (fair) {
            qlock = new ReentrantLock(true);
            waitingProducers = new FifoWaitQueue();
            waitingConsumers = new FifoWaitQueue();
        } else {
            qlock = new ReentrantLock();
            waitingProducers = new LifoWaitQueue();
            waitingConsumers = new LifoWaitQueue();
        }
        s.defaultWriteObject();
    }

    private void readObject(final java.io.ObjectInputStream s) throws java.io.IOException, ClassNotFoundException {
        s.defaultReadObject();
        if (waitingProducers instanceof FifoWaitQueue) transferer = new TransferQueue(); else transferer = new TransferStack();
    }
}

package org.dbunit.util.concurrent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A rendezvous channel, similar to those used in CSP and Ada. Each
 * put must wait for a take, and vice versa. Synchronous channels
 * are well suited for handoff designs, in which an object running in
 * one thread must synch up with an object running in another thread
 * in order to hand it some information, event, or task. 
 * <p> If you only need threads to synch up without
 * exchanging information, consider using a Barrier. If you need
 * bidirectional exchanges, consider using a Rendezvous.  <p>
 *
 * <p>Read the
 * <a href="http://gee.cs.oswego.edu/dl/classes/EDU/oswego/cs/dl/util/concurrent/intro.html">introduction to this package</a> 
 * for more details.
 * 
 * @author Doug Lea
 * @author Last changed by: $Author: gommma $
 * @version $Revision: 766 $ $Date: 2008-08-01 07:05:20 -0400 (Fri, 01 Aug 2008) $
 * @since ? (pre 2.1)
 * @see <a href="http://gee.cs.oswego.edu/dl/classes/EDU/oswego/cs/dl/util/concurrent/Rendezvous.html">Rendezvous</a>
 * @see <a href="http://gee.cs.oswego.edu/dl/classes/EDU/oswego/cs/dl/util/concurrent/CyclicBarrier.html">CyclicBarrier</a>
 */
public class SynchronousChannel implements BoundedChannel {

    /**
     * Logger for this class
     */
    private static final Logger logger = LoggerFactory.getLogger(SynchronousChannel.class);

    /** 
   * Special marker used in queue nodes to indicate that
   * the thread waiting for a change in the node has timed out
   * or been interrupted.
   **/
    protected static final Object CANCELLED = new Object();

    /**
   * Simple FIFO queue class to hold waiting puts/takes.
   **/
    protected static class Queue {

        /**
         * Logger for this class
         */
        private static final Logger logger = LoggerFactory.getLogger(Queue.class);

        protected LinkedNode head;

        protected LinkedNode last;

        protected void enq(LinkedNode p) {
            logger.debug("enq(p={}) - start", p);
            if (last == null) last = head = p; else last = last.next = p;
        }

        protected LinkedNode deq() {
            logger.debug("deq() - start");
            LinkedNode p = head;
            if (p != null && (head = p.next) == null) last = null;
            return p;
        }
    }

    protected final Queue waitingPuts = new Queue();

    protected final Queue waitingTakes = new Queue();

    /**
   * @return zero --
   * Synchronous channels have no internal capacity.
   **/
    public int capacity() {
        logger.debug("capacity() - start");
        return 0;
    }

    /**
   * @return null --
   * Synchronous channels do not hold contents unless actively taken
   **/
    public Object peek() {
        logger.debug("peek() - start");
        return null;
    }

    public void put(Object x) throws InterruptedException {
        logger.debug("put(x={}) - start", x);
        if (x == null) throw new IllegalArgumentException();
        for (; ; ) {
            if (Thread.interrupted()) throw new InterruptedException();
            LinkedNode slot;
            LinkedNode item = null;
            synchronized (this) {
                slot = waitingTakes.deq();
                if (slot == null) waitingPuts.enq(item = new LinkedNode(x));
            }
            if (slot != null) {
                synchronized (slot) {
                    if (slot.value != CANCELLED) {
                        slot.value = x;
                        slot.notify();
                        return;
                    }
                }
            } else {
                synchronized (item) {
                    try {
                        while (item.value != null) item.wait();
                        return;
                    } catch (InterruptedException ie) {
                        if (item.value == null) {
                            Thread.currentThread().interrupt();
                            return;
                        } else {
                            item.value = CANCELLED;
                            throw ie;
                        }
                    }
                }
            }
        }
    }

    public Object take() throws InterruptedException {
        logger.debug("take() - start");
        for (; ; ) {
            if (Thread.interrupted()) throw new InterruptedException();
            LinkedNode item;
            LinkedNode slot = null;
            synchronized (this) {
                item = waitingPuts.deq();
                if (item == null) waitingTakes.enq(slot = new LinkedNode());
            }
            if (item != null) {
                synchronized (item) {
                    Object x = item.value;
                    if (x != CANCELLED) {
                        item.value = null;
                        item.next = null;
                        item.notify();
                        return x;
                    }
                }
            } else {
                synchronized (slot) {
                    try {
                        for (; ; ) {
                            Object x = slot.value;
                            if (x != null) {
                                slot.value = null;
                                slot.next = null;
                                return x;
                            } else slot.wait();
                        }
                    } catch (InterruptedException ie) {
                        Object x = slot.value;
                        if (x != null) {
                            slot.value = null;
                            slot.next = null;
                            Thread.currentThread().interrupt();
                            return x;
                        } else {
                            slot.value = CANCELLED;
                            throw ie;
                        }
                    }
                }
            }
        }
    }

    public boolean offer(Object x, long msecs) throws InterruptedException {
        if (logger.isDebugEnabled()) logger.debug("offer(x={}, msecs={}) - start", x, String.valueOf(msecs));
        if (x == null) throw new IllegalArgumentException();
        long waitTime = msecs;
        long startTime = 0;
        for (; ; ) {
            if (Thread.interrupted()) throw new InterruptedException();
            LinkedNode slot;
            LinkedNode item = null;
            synchronized (this) {
                slot = waitingTakes.deq();
                if (slot == null) {
                    if (waitTime <= 0) return false; else waitingPuts.enq(item = new LinkedNode(x));
                }
            }
            if (slot != null) {
                synchronized (slot) {
                    if (slot.value != CANCELLED) {
                        slot.value = x;
                        slot.notify();
                        return true;
                    }
                }
            }
            long now = System.currentTimeMillis();
            if (startTime == 0) startTime = now; else waitTime = msecs - (now - startTime);
            if (item != null) {
                synchronized (item) {
                    try {
                        for (; ; ) {
                            if (item.value == null) return true;
                            if (waitTime <= 0) {
                                item.value = CANCELLED;
                                return false;
                            }
                            item.wait(waitTime);
                            waitTime = msecs - (System.currentTimeMillis() - startTime);
                        }
                    } catch (InterruptedException ie) {
                        if (item.value == null) {
                            Thread.currentThread().interrupt();
                            return true;
                        } else {
                            item.value = CANCELLED;
                            throw ie;
                        }
                    }
                }
            }
        }
    }

    public Object poll(long msecs) throws InterruptedException {
        if (logger.isDebugEnabled()) logger.debug("poll(msecs={}) - start", String.valueOf(msecs));
        long waitTime = msecs;
        long startTime = 0;
        for (; ; ) {
            if (Thread.interrupted()) throw new InterruptedException();
            LinkedNode item;
            LinkedNode slot = null;
            synchronized (this) {
                item = waitingPuts.deq();
                if (item == null) {
                    if (waitTime <= 0) return null; else waitingTakes.enq(slot = new LinkedNode());
                }
            }
            if (item != null) {
                synchronized (item) {
                    Object x = item.value;
                    if (x != CANCELLED) {
                        item.value = null;
                        item.next = null;
                        item.notify();
                        return x;
                    }
                }
            }
            long now = System.currentTimeMillis();
            if (startTime == 0) startTime = now; else waitTime = msecs - (now - startTime);
            if (slot != null) {
                synchronized (slot) {
                    try {
                        for (; ; ) {
                            Object x = slot.value;
                            if (x != null) {
                                slot.value = null;
                                slot.next = null;
                                return x;
                            }
                            if (waitTime <= 0) {
                                slot.value = CANCELLED;
                                return null;
                            }
                            slot.wait(waitTime);
                            waitTime = msecs - (System.currentTimeMillis() - startTime);
                        }
                    } catch (InterruptedException ie) {
                        Object x = slot.value;
                        if (x != null) {
                            slot.value = null;
                            slot.next = null;
                            Thread.currentThread().interrupt();
                            return x;
                        } else {
                            slot.value = CANCELLED;
                            throw ie;
                        }
                    }
                }
            }
        }
    }
}

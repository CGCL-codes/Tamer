package org.jcyclone.ext.asocket.nio;

import org.jcyclone.core.queue.IElement;
import org.jcyclone.ext.asocket.SelectSourceIF;
import java.io.IOException;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;

/**
 * A NIOSelectSource is an implementation of SourceIF which pulls events from
 * the operating system via the NIO Selector interface. This can be thought
 * of as a 'shim' which turns a Selector into a SourceIF.
 * <p/>
 * <p>NIOSelectSource also "balances" the set of events returned on
 * subsequent calls to dequeue, in order to avoid biasing the servicing of
 * underlying O/S events to a particular order. This feature can be
 * disabled by creating a SelectSource with the boolean flag 'do_balance'.
 * <p/>
 * <p><b>Important note:</b> This class is not threadsafe with respect
 * to multiple threads calling dequeue() or blocking_dequeue() at once.
 * Clients must synchronize their access to this class.
 *
 * @author Matt Welsh
 */
public class NIOSelectSource implements SelectSourceIF {

    private static final boolean DEBUG = false;

    private Selector selector;

    private SelectionKey ready[];

    private int ready_offset, ready_size;

    private boolean do_balance;

    private final int BALANCER_SEQUENCE_SIZE = 10000;

    private int balancer_seq[];

    private int balancer_seq_off;

    private final Object blocker;

    private String name = "(unknown)";

    public Selector getSelector() {
        return selector;
    }

    /**
	 * Create a new empty SelectSource. This SelectSource will perform
	 * event balancing.
	 */
    public NIOSelectSource() {
        this(true);
    }

    /**
	 * Create a new empty SelectSource.
	 *
	 * @param do_balance Indicates whether this SelectSource should perform
	 *                   event balancing.
	 */
    public NIOSelectSource(boolean do_balance) {
        blocker = new Object();
        try {
            selector = Selector.open();
        } catch (IOException e) {
            System.err.println("NIOSelectSource (" + name + "): error creating selector: " + e);
        }
        ready = null;
        ready_offset = ready_size = 0;
        this.do_balance = do_balance;
        if (DEBUG) System.err.println("NIOSelectSource created, do_balance = " + do_balance);
        if (do_balance) initBalancer();
    }

    /**
	 * Register a SelectItem with the SelectSource. The SelectItem should
	 * generally correspond to a Selectable along with a set of event flags
	 * that we wish this SelectSource to test for.
	 * <p/>
	 * <p>The user is allowed to modify the event flags in the SelectItem
	 * directly (say, to cause the SelectSource ignore a given SelectItem for
	 * the purposes of future calls to one of the dequeue methods). However,
	 * modifying the event flags may not be synchronous with calls to dequeue -
	 * generally because SelectSource maintains a cache of recently-received
	 * events.
	 */
    public Object register(Object nio_sc_obj, int ops) {
        if (DEBUG) System.err.println("NIOSelectSource (" + name + "): register " + nio_sc_obj + " : " + this);
        if (!(nio_sc_obj instanceof SelectableChannel)) {
            System.err.println("register() called with non SelectableChannel argument.  " + "Should not happen!!");
            return null;
        }
        SelectableChannel nio_sc = (SelectableChannel) nio_sc_obj;
        synchronized (blocker) {
            SelectionKey ret;
            try {
                ret = nio_sc.register(selector, ops);
            } catch (ClosedChannelException cce) {
                System.err.println("Closed Channel Exception: " + cce);
                ret = null;
            }
            if (DEBUG) System.err.println("returning " + ret);
            if (DEBUG) System.err.println("numactive = " + numActive());
            blocker.notify();
            return ret;
        }
    }

    public void register(Object sel) {
        System.err.println("Single argument register() called on NIOSelectSource.  " + "Should not happen!!");
        return;
    }

    /**
	 * Deregister a SelectItem with this SelectSource.
	 * Note that after calling deregister, subsequent calls to dequeue
	 * may in fact return this SelectItem as a result. This is because
	 * the SelectQueue internally caches results.
	 */
    public void deregister(Object selkey_obj) {
        if (DEBUG) System.err.println("NIOSelectSource (" + name + "): deregister " + selkey_obj);
        if (!(selkey_obj instanceof SelectionKey)) {
            System.err.println("deregister() called on NIOSelectSource with non SelectionKey " + "argument.  Should not happen!!");
            return;
        }
        synchronized (blocker) {
            SelectionKey selkey = (SelectionKey) selkey_obj;
            if (DEBUG) System.err.println("NIOSelectSource (" + name + "): cancel " + selkey);
            selkey.cancel();
            try {
                if (DEBUG) System.err.println("NIOSelectSource (" + name + "): selectNow");
                selector.wakeup();
                selector.selectNow();
                if (DEBUG) System.err.println("NIOSelectSource (" + name + "): selectNow returned");
            } catch (IOException ioe) {
            }
            blocker.notify();
        }
    }

    /**
	 * Must be called if the 'events' mask of any SelectItem registered
	 * with this SelectSource changes. Pushes event mask changes down to
	 * the underlying event-dispatch mechanism.
	 */
    public void update() {
    }

    /**
	 * Must be called if the 'events' mask of this SelectItem (which
	 * must be registered with this SelectSource) changes. Pushes
	 * event mask changes down to the underlying event-dispatch mechanism.
	 */
    public void update(Object sel) {
        System.err.println("update() called on NIOSelectSource with argument.  " + "should not happen!!");
        return;
    }

    /**
	 * Return the number of SelectItems registered with the SelectSource.
	 */
    public int numRegistered() {
        Set keys = selector.keys();
        synchronized (keys) {
            return keys.size();
        }
    }

    /**
	 * Return the number of active SelectItems registered with the SelectSource.
	 * An active SelectItem is one defined as having a non-zero events
	 * interest mask.
	 */
    public int numActive() {
        Set keys = selector.keys();
        synchronized (blocker) {
            synchronized (keys) {
                Iterator key_iter = keys.iterator();
                SelectionKey sk;
                int n_active = 0;
                while (key_iter.hasNext()) {
                    sk = (SelectionKey) key_iter.next();
                    if (sk.isValid() && sk.interestOps() != 0) n_active++;
                }
                return n_active;
            }
        }
    }

    /**
	 * Return the number of elements waiting in the queue (that is,
	 * which don't require a SelectSet poll operation to retrieve).
	 */
    public int size() {
        synchronized (this) {
            return (ready_size - ready_offset);
        }
    }

    /**
	 * Dequeues the next element from the SelectSource without blocking.
	 * Returns null if no entries available.
	 */
    public IElement dequeue() {
        if (numRegistered() == 0) return null;
        if ((ready_size == 0) || (ready_offset == ready_size)) {
            doPoll(0);
        }
        if (ready_size == 0) return null;
        return new NIOSelectorQueueElement(ready[ready_offset++]);
    }

    /**
	 * Dequeues all elements which are ready from the SelectSource.
	 * Returns null if no entries available.
	 */
    public IElement[] dequeue_all() {
        if (numRegistered() == 0) return null;
        if ((ready_size == 0) || (ready_offset == ready_size)) {
            doPoll(0);
        }
        if (ready_size == 0) return null;
        NIOSelectorQueueElement ret[] = new NIOSelectorQueueElement[ready_size - ready_offset];
        for (int i = 0; i < ret.length; i++) {
            ret[i] = new NIOSelectorQueueElement(ready[ready_offset++]);
        }
        return ret;
    }

    /**
	 * Dequeues all elements which are ready from the SelectSource.
	 * Returns zero if no entries available.
	 */
    public int dequeueAll(List list) {
        if (numRegistered() == 0) return 0;
        if ((ready_size == 0) || (ready_offset == ready_size)) {
            doPoll(0);
        }
        if (ready_size == 0) return 0;
        int size = ready_size - ready_offset;
        for (int i = 0; i < size; i++) {
            list.add(new NIOSelectorQueueElement(ready[ready_offset++]));
        }
        return size;
    }

    /**
	 * Dequeues at most <tt>maxElements</tt> elements which are ready from the
	 * SelectSource. Returns null if no entries available.
	 */
    public IElement[] dequeue(int maxElements) {
        if (numRegistered() == 0) return null;
        if ((ready_size == 0) || (ready_offset == ready_size)) {
            doPoll(0);
        }
        if (ready_size == 0) return null;
        int numtoret = Math.min(ready_size - ready_offset, maxElements);
        NIOSelectorQueueElement ret[] = new NIOSelectorQueueElement[numtoret];
        for (int i = 0; i < numtoret; i++) {
            ret[i] = new NIOSelectorQueueElement(ready[ready_offset++]);
        }
        return ret;
    }

    /**
	 * Dequeues at most <tt>maxElements</tt> elements which are ready from the
	 * SelectSource. Returns zero if no entries available.
	 */
    public int dequeue(List list, int maxElements) {
        if (numRegistered() == 0) return 0;
        if ((ready_size == 0) || (ready_offset == ready_size)) {
            doPoll(0);
        }
        if (ready_size == 0) return 0;
        int numtoret = Math.min(ready_size - ready_offset, maxElements);
        for (int i = 0; i < numtoret; i++) {
            list.add(new NIOSelectorQueueElement(ready[ready_offset++]));
        }
        return numtoret;
    }

    /**
	 * Dequeue the next element from the SelectSource. Blocks up to
	 * timeout_millis milliseconds; returns null if no entries available
	 * after that time. A timeout of -1 blocks forever.
	 */
    public IElement blockingDequeue(int timeout_millis) throws InterruptedException {
        if (DEBUG) System.err.println("NIOSelectSource (" + name + "): blocking_dequeue called");
        synchronized (blocker) {
            if (numRegistered() == 0) {
                if (DEBUG) System.err.println("No keys in selector");
                if (timeout_millis == 0) return null;
                try {
                    if (timeout_millis == -1) {
                        blocker.wait();
                    } else {
                        blocker.wait(timeout_millis);
                    }
                } catch (InterruptedException ie) {
                    blocker.notify();
                    throw ie;
                }
            }
        }
        if ((ready_size == 0) || (ready_offset == ready_size)) {
            doPoll(timeout_millis);
        }
        if (ready_size == 0) {
            if (DEBUG) System.err.println("still no ready");
            return null;
        }
        return new NIOSelectorQueueElement(ready[ready_offset++]);
    }

    /**
	 * Dequeue a set of elements from the SelectSource. Blocks up to
	 * timeout_millis milliseconds; returns null if no entries available
	 * after that time. A timeout of -1 blocks forever.
	 */
    public IElement[] blocking_dequeue_all(int timeout_millis) throws InterruptedException {
        if (DEBUG) System.err.println("NIOSelectSource (" + name + "): blocking_dequeue_all called");
        synchronized (blocker) {
            if (numRegistered() == 0) {
                if (DEBUG) System.err.println("!!!!no keys");
                if (timeout_millis == 0) return null;
                try {
                    if (timeout_millis == -1) {
                        blocker.wait();
                    } else {
                        blocker.wait(timeout_millis);
                    }
                } catch (InterruptedException ie) {
                    blocker.notify();
                    throw ie;
                }
            }
        }
        if ((ready_size == 0) || (ready_offset == ready_size)) {
            doPoll(timeout_millis);
        }
        if (DEBUG) System.err.println("!!!!ready_size=" + ready_size);
        if (ready_size == 0) return null;
        if (DEBUG) System.err.println("!!!!ready_size-ready_offset=" + (ready_size - ready_offset));
        NIOSelectorQueueElement ret[] = new NIOSelectorQueueElement[ready_size - ready_offset];
        for (int i = 0; i < ret.length; i++) {
            if (DEBUG) System.err.println("ret[" + i + "] = " + ready[ready_offset]);
            ret[i] = new NIOSelectorQueueElement(ready[ready_offset++]);
        }
        return ret;
    }

    /**
	 * Dequeue a set of elements from the SelectSource. Blocks up to
	 * timeout_millis milliseconds; returns null if no entries available
	 * after that time. A timeout of -1 blocks forever.
	 */
    public int blockingDequeueAll(List list, int timeout_millis) throws InterruptedException {
        if (DEBUG) System.err.println("NIOSelectSource (" + name + "): blocking_dequeue_all called");
        synchronized (blocker) {
            if (numRegistered() == 0) {
                if (DEBUG) System.err.println("!!!!no keys");
                if (timeout_millis == 0) return 0;
                try {
                    if (timeout_millis == -1) {
                        blocker.wait();
                    } else {
                        blocker.wait(timeout_millis);
                    }
                } catch (InterruptedException ie) {
                    blocker.notify();
                    throw ie;
                }
            }
        }
        if ((ready_size == 0) || (ready_offset == ready_size)) {
            doPoll(timeout_millis);
        }
        if (DEBUG) System.err.println("!!!!ready_size=" + ready_size);
        if (ready_size == 0) return 0;
        if (DEBUG) System.err.println("!!!!ready_size-ready_offset=" + (ready_size - ready_offset));
        int size = ready_size - ready_offset;
        for (int i = 0; i < size; i++) {
            if (DEBUG) System.err.println("ret[" + i + "] = " + ready[ready_offset]);
            list.add(new NIOSelectorQueueElement(ready[ready_offset++]));
        }
        return size;
    }

    /**
	 * Dequeue a set of elements from the SelectSource. Blocks up to
	 * timeout_millis milliseconds; returns null if no entries available
	 * after that time. A timeout of -1 blocks forever.
	 */
    public IElement[] blocking_dequeue(int timeout_millis, int num) throws InterruptedException {
        if (DEBUG) System.err.println("NIOSelectSource (" + name + "): blocking_dequeue called");
        synchronized (blocker) {
            if (numRegistered() == 0) {
                if (timeout_millis == 0) return null;
                try {
                    if (timeout_millis == -1) {
                        blocker.wait();
                    } else {
                        blocker.wait(timeout_millis);
                    }
                } catch (InterruptedException ie) {
                    blocker.notify();
                    throw ie;
                }
            }
        }
        if ((ready_size == 0) || (ready_offset == ready_size)) {
            doPoll(timeout_millis);
        }
        if (ready_size == 0) return null;
        int numtoret = Math.min(ready_size - ready_offset, num);
        NIOSelectorQueueElement ret[] = new NIOSelectorQueueElement[numtoret];
        for (int i = 0; i < numtoret; i++) {
            ret[i] = new NIOSelectorQueueElement(ready[ready_offset++]);
        }
        return ret;
    }

    /**
	 * Dequeue a set of elements from the SelectSource. Blocks up to
	 * timeout_millis milliseconds; returns null if no entries available
	 * after that time. A timeout of -1 blocks forever.
	 */
    public int blockingDequeue(List list, int timeout_millis, int maxElements) throws InterruptedException {
        if (DEBUG) System.err.println("NIOSelectSource (" + name + "): blocking_dequeue called");
        synchronized (blocker) {
            if (numRegistered() == 0) {
                if (timeout_millis == 0) return 0;
                try {
                    if (timeout_millis == -1) {
                        blocker.wait();
                    } else {
                        blocker.wait(timeout_millis);
                    }
                } catch (InterruptedException ie) {
                    blocker.notify();
                    throw ie;
                }
            }
        }
        if ((ready_size == 0) || (ready_offset == ready_size)) {
            doPoll(timeout_millis);
        }
        if (ready_size == 0) return 0;
        int numtoret = Math.min(ready_size - ready_offset, maxElements);
        for (int i = 0; i < numtoret; i++) {
            list.add(new NIOSelectorQueueElement(ready[ready_offset++]));
        }
        return numtoret;
    }

    private void doPoll(int timeout) {
        if (DEBUG) System.err.println("NIOSelectSource (" + name + "): Doing poll, timeout " + timeout);
        int c = 0;
        try {
            if (timeout == 0) {
                c = selector.selectNow();
            } else {
                if (timeout == -1) timeout = 0;
                c = selector.select(timeout);
            }
        } catch (IOException e) {
            if (DEBUG) System.err.println("NIOSelectSource (" + name + "): Error doing select: " + e);
        }
        if (DEBUG) System.err.println("NIOSelectSource (" + name + "): poll returned " + c);
        if (c == 0) {
            ready = null;
            ready_offset = ready_size = 0;
            return;
        }
        Set skeys = selector.selectedKeys();
        synchronized (skeys) {
            if (skeys.size() > 0) {
                SelectionKey ret[] = new SelectionKey[skeys.size()];
                Iterator key_iter = skeys.iterator();
                int j = 0;
                while (key_iter.hasNext() && j < ret.length) {
                    ret[j] = (SelectionKey) key_iter.next();
                    key_iter.remove();
                    j++;
                }
                if (ret.length != 0) {
                    ready_offset = 0;
                    ready_size = ret.length;
                    balance(ret);
                    return;
                }
            }
        }
        ready = null;
        ready_offset = ready_size = 0;
    }

    private void balance(SelectionKey selarr[]) {
        if (DEBUG) System.err.println("NIOSelectSource (" + name + "): balance called, selarr size=" + selarr.length);
        for (int i = 0; i < selarr.length; i++) if (DEBUG) System.err.println("!!!!selar[" + i + "] = " + selarr[i]);
        if ((!do_balance) || (selarr.length < 2)) {
            ready = selarr;
        } else {
            SelectionKey a;
            ready = new SelectionKey[selarr.length];
            for (int i = 0; i < ready.length; i++) {
                if (balancer_seq_off == BALANCER_SEQUENCE_SIZE) {
                    balancer_seq_off = 0;
                }
                int n = balancer_seq[balancer_seq_off++] % selarr.length;
                int c = 0;
                while (selarr[n] == null) {
                    n++;
                    c++;
                    if (n == selarr.length) n = 0;
                    if (c == selarr.length) {
                        System.err.println("WARNING: NIOSelectSource.balance(): All items in selarr are null (n=" + n + ", c=" + c + ", len=" + selarr.length);
                        for (int k = 0; k < ready.length; k++) {
                            System.err.println("[" + k + "] ready:" + ready[k] + " selarr:" + selarr[k]);
                        }
                        throw new IllegalArgumentException("balance: All items in selarr are null! This is a bug - please contact mdw@cs.berkeley.edu");
                    }
                }
                if (DEBUG) System.err.println("NIOSelectSource (" + name + "): balance: " + n + "->" + i);
                a = selarr[n];
                selarr[n] = null;
                ready[i] = a;
            }
        }
    }

    private void initBalancer() {
        balancer_seq = new int[BALANCER_SEQUENCE_SIZE];
        Random r = new Random();
        for (int i = 0; i < BALANCER_SEQUENCE_SIZE; i++) {
            balancer_seq[i] = Math.abs(r.nextInt());
        }
        balancer_seq_off = 0;
    }

    void setName(String thename) {
        this.name = thename;
    }

    public String toString() {
        return "NIOSS(" + name + ")";
    }

    public void interruptSelect() {
        selector.wakeup();
    }
}

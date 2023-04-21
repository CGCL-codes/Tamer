package org.amino.ds.lockfree;

import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;

/**
 * Internal anchor type used to record the current status of deque. This class
 * contains all important information about Deque:
 * <ol>
 * <li>pointer to the the 1st node from left</li>
 * <li>pointer to the 1st node from right</li>
 * <li>size of Deque</li>
 * </ol>
 * 
 * @param <E>
 *            type of element on node
 */
class AnchorType<E> {

    /**
	 * Refer to {@link LockFreeDeque} for explanation of these three states.
	 * Enumeration is inconvenient to be operated as integer and not used here.
	 */
    private static final int STABLE = 0;

    /**
	 * This is a flag variable to indicate the status of deque. It can be one of
	 * the STABLE, RPUSH, LPUSH. This field has default access privilege since
	 * Deque will visit it directly.
	 */
    volatile int status;

    /**
	 * This variable will be used to update <code>status</code> in an atomic
	 * approach.
	 */
    private static final AtomicIntegerFieldUpdater<AnchorType> STATUS_UPDATER = AtomicIntegerFieldUpdater.newUpdater(AnchorType.class, "status");

    /**
	 * Right or tail node in the deque.
	 */
    DequeNode<E> right;

    /**
	 * Left or head node in the deque.
	 */
    DequeNode<E> left;

    /**
	 * Number of elements inside the Deque.
	 */
    int numElements;

    /**
	 * Try to CAS <code>status</code> from oldStatus to STABLE. Do nothing if
	 * CAS fails.
	 * 
	 * @param oldStatus
	 *            old status expected
	 */
    public void stableStatus(int oldStatus) {
        STATUS_UPDATER.compareAndSet(this, oldStatus, STABLE);
    }

    /**
	 * default constructor.
	 */
    public AnchorType() {
    }

    /**
	 * @param r
	 *            right node
	 * @param l
	 *            left node
	 * @param st
	 *            status
	 * @param ne
	 *            number of element
	 */
    public AnchorType(DequeNode<E> r, DequeNode<E> l, int st, int ne) {
        setup(r, l, st, ne);
    }

    /**
	 * Setup the anchor with the input parameters.
	 * 
	 * @param r
	 *            right node
	 * @param l
	 *            left node
	 * @param st
	 *            status of deque
	 * @param ne
	 *            number of element
	 */
    void setup(DequeNode<E> r, DequeNode<E> l, int st, int ne) {
        right = r;
        left = l;
        status = st;
        numElements = ne;
    }

    /**
	 * Return size of Deque.
	 * 
	 * @return size of Deque
	 */
    public int getSize() {
        return this.numElements;
    }
}

package org.opennms.core.queue;

/**
 * </p>
 * This interface defines a queue that uses <em>F</em> irst <em>I</em>n,
 * <em>F</em> irst <em>O</em> ut semantics when adding and removing objects.
 * Each object that is added to the queue is effectively placed at the end of
 * the list of previous elements. Each call to <code>remove</code> will result
 * in the removal of the next element, or the oldest element in the queue.
 * </p>
 * 
 * @author <a href="mailto:weave@oculan.com">Brian Weaver </a>
 * @author <a href="http://www.opennms.org/">OpenNMS </a>
 * 
 */
public interface FifoQueue<T> {

    /**
     * Inserts a new element into the queue.
     * 
     * @param element
     *            The object to append to the queue.
     * 
     * @exception org.opennms.core.queue.FifoQueueException
     *                Thrown if a queue error occurs.
     * @exception java.lang.InterruptedException
     *                Thrown if the thread is interrupted.
     */
    public void add(T element) throws FifoQueueException, InterruptedException;

    /**
     * Inserts a new element into the queue. If the queue has reached an
     * implementation limit and the <code>
     * timeout</code> expires, then a false
     * value is returned to the caller.
     * 
     * @param element
     *            The object to append to the queue.
     * @param timeout
     *            The time to wait on the insertion to succeed.
     * 
     * @exception org.opennms.core.queue.FifoQueueException
     *                Thrown if a queue error occurs.
     * @exception java.lang.InterruptedException
     *                Thrown if the thread is interrupted.
     * @exception java.lang.UnsupportedOperationException
     *                Thrown if the method is not supported.
     * 
     * @return True if the element was successfully added to the queue before
     *         the timeout expired, false otherwise.
     */
    public boolean add(T element, long timeout) throws FifoQueueException, InterruptedException;

    /**
     * Removes the oldest element from the queue.
     * 
     * @exception org.opennms.core.queue.FifoQueueException
     *                Thrown if a queue error occurs.
     * @exception java.lang.InterruptedException
     *                Thrown if the thread is interrupted.
     * 
     * @return The oldest object in the queue.
     */
    public T remove() throws FifoQueueException, InterruptedException;

    /**
     * Removes the next element from the queue if one becomes available before
     * the timeout expires. If the timeout expires before an element is
     * available then a <code>null</code> reference is returned to the caller.
     * 
     * @param timeout
     *            The time to wait on an object to be available.
     * 
     * @exception org.opennms.core.queue.FifoQueueException
     *                Thrown if a queue error occurs.
     * @exception java.lang.InterruptedException
     *                Thrown if the thread is interrupted.
     * @exception java.lang.UnsupportedOperationException
     *                Thrown if the method is not supported.
     * 
     * @return The oldest object in the queue, or <code>null</code> if one is
     *         not available.
     */
    public T remove(long timeout) throws FifoQueueException, InterruptedException;

    /**
     * Returns the current number of elements that are in the queue.
     * 
     * @return The number of elements in the queue.
     */
    public int size();

    /**
     * Used to test if the current queue has no stored elements.
     * 
     * @return True if the queue is empty.
     */
    public boolean isEmpty();
}

package org.jseda.queue;

/**
 * Queue is a generic interface that specifies basic queue I/O operations.
 * @author "John McNair" <john@mcnair.org>
 *
 * @param <T> the type of object to be held by the Queue
 */
public interface Queue<T> {

    /**
   * Determine whether or not the queue is empty.
   *
   * @return true if the queue is empty, false otherwise
   */
    boolean isEmpty();

    /**
   * Return the number of elements in the queue.
   *
   * @return the number of elements in the queue
   */
    int size();

    /**
   * Add an element of type T to the back of the queue.
   *
   * @param element the element to add
   */
    void pushBack(T element);

    /**
   * Pop an element of type T from the front of the queue.
   *
   * @return the next element at the front of the queue or null if none is
   * available
   */
    T popFront();
}

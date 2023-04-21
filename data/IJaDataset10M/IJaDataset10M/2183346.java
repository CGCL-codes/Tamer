package org.zoolib.compat.java.util;

/**
 * Exception that is thrown by the collections classes when it is detected that
 * a modification has been made to a data structure when this is not allowed,
 * such as when a collection is structurally modified while an Iterator is
 * operating over it. In cases where this can be detected, a
 * ConcurrentModificationException will be thrown. An Iterator that detects
 * this condition is referred to as fail-fast. Notice that this can occur
 * even in single-threaded designs, if you call methods out of order.
 *
 * @author Warren Levy (warrenl@cygnus.com)
 * @author Eric Blake (ebb9@email.byu.edu)
 * @see Collection
 * @see Iterator
 * @see ListIterator
 * @see Vector
 * @see LinkedList
 * @see HashSet
 * @see Hashtable
 * @see TreeMap
 * @see AbstractList
 * @since 1.2
 * @status updated to 1.4
 */
public class ConcurrentModificationException extends RuntimeException {

    /**
   * Compatible with JDK 1.2.
   */
    private static final long serialVersionUID = -3666751008965953603L;

    /**
   * Constructs a ConcurrentModificationException with no detail message.
   */
    public ConcurrentModificationException() {
    }

    /**
   * Constructs a ConcurrentModificationException with a detail message.
   *
   * @param detail the detail message for the exception
   */
    public ConcurrentModificationException(String detail) {
        super(detail);
    }
}

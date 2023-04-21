package org.apache.jdo.util;

import java.util.Stack;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
* A general purpose pooling class.
*
* @author Dave Bristor
*/
public class Pool {

    private final Stack stack = new Stack();

    private final int size;

    private int count = 0;

    /** Number of millis to wait for a free entry
     * Currently fixed; might be made configurable in future.
     */
    private int waitMillis = 1000;

    /** Number of times to wait for a free entry
     * Currently fixed; might be made configurable in future.
     */
    private int waitNumber = 5;

    /** I18N */
    private static final I18NHelper msg = I18NHelper.getInstance("org.apache.jdo.util.Bundle");

    static final Log test = LogFactory.getFactory().getInstance("org.apache.jdo.util");

    /**
     * Constructs a pool that will limit the number of objects which it can
     * contain.
     * @param size The maximum number of items that can be put into the pool.
     */
    public Pool(int size) {
        this.size = size;
    }

    /**
     * Puts the given object into the pool, if there the pool has fewer than
     * the number of elements specifed when created.  If the pool is full,
     * blocks until an element is removed. 
     * @param o Object to be put in the pool.
     * @throws InterruptedException
     */
    public synchronized void put(Object o) throws InterruptedException {
        boolean debug = test.isDebugEnabled();
        if (debug) {
            test.debug("Pool.put: " + o);
        }
        if (count > size || count < 0) {
            if (debug) {
                test.debug("Pool: count " + count + " out of range 0-" + size);
            }
            throw new RuntimeException(msg.msg("EXC_CountOutOfRange", new Integer(count).toString(), new Integer(size).toString()));
        }
        if (stack.contains(o)) {
            if (debug) {
                test.debug("Pool: duplicate object");
            }
            throw new RuntimeException(msg.msg("EXC_DuplicateObject", o));
        }
        while (count == size) {
            if (debug) {
                test.debug("Pool.put: block");
            }
            wait();
        }
        stack.push(o);
        ++count;
        notify();
    }

    /**
     * Gets an object from the pool, if one is available.  If an object is not
     * available, waits until one is.  The waiting is governed by two
     * variables, which are currently fixed: waitMillis and waitNumber.
     * If no object is available from the pool within (waitNumber) times
     * (waitMillis) milliseconds, then a RuntimeException is thrown.
     * In future, the waitMillis and waitNumber should be configurable.
     * @return An object from the pool.
     */
    public synchronized Object get() throws InterruptedException {
        boolean debug = test.isDebugEnabled();
        Object rc = null;
        if (count > size || count < 0) {
            if (debug) {
                test.debug("Pool: count " + count + " out of range 0-" + size);
            }
            throw new RuntimeException(msg.msg("EXC_CountOutOfRange", new Integer(count).toString(), new Integer(size).toString()));
        }
        int timeouts = 0;
        while (count == 0 && timeouts++ < waitNumber) {
            if (debug) {
                test.debug("Pool.get: block " + timeouts);
            }
            wait(waitMillis);
        }
        if (timeouts >= waitNumber) {
            throw new RuntimeException(msg.msg("EXC_PoolGetTimeout"));
        }
        rc = stack.pop();
        --count;
        notify();
        if (debug) {
            test.debug("Pool.get: " + rc);
        }
        return rc;
    }
}

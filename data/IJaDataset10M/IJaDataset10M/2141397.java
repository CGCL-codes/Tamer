package org.sourceforge.jemm.collections;

import org.sourceforge.jemm.collections.internal.atomic.ALRequest;
import org.sourceforge.jemm.collections.internal.atomic.ALResponse;
import org.sourceforge.jemm.collections.internal.atomic.AtomicNumAction;
import org.sourceforge.jemm.lifecycle.ShadowTypeObject;
import org.sourceforge.jemm.util.JEMMType;

public class JemmAtomicLong extends JEMMType {

    public JemmAtomicLong() {
        this(0);
    }

    public JemmAtomicLong(long initialValue) {
        super(Long.valueOf(initialValue));
    }

    protected JemmAtomicLong(ShadowTypeObject shadow) {
        super(shadow);
    }

    /**
     * Gets the current value.
     *
     * @return the current value
     */
    public final long get() {
        return ((ALResponse) jemmOIF.processRequest(new ALRequest(AtomicNumAction.GET, 0, 0))).getResult();
    }

    /**
     * Sets to the given value.
     *
     * @param newValue the new value
     */
    public final void set(long newValue) {
        jemmOIF.processRequest(new ALRequest(AtomicNumAction.SET, newValue, 0));
    }

    /**
     * Atomically sets to the given value and returns the old value.
     *
     * @param newValue the new value
     * @return the previous value
     */
    public final long getAndSet(long newValue) {
        return ((ALResponse) jemmOIF.processRequest(new ALRequest(AtomicNumAction.GET_AND_SET, newValue, 0))).getResult();
    }

    /**
     * Atomically increments by one the current value.
     *
     * @return the previous value
     */
    public final long getAndIncrement() {
        return getAndAdd(1);
    }

    /**
     * Atomically decrements by one the current value.
     *
     * @return the previous value
     */
    public final long getAndDecrement() {
        return getAndAdd(-1);
    }

    /**
     * Atomically adds the given value to the current value.
     *
     * @param delta the value to add
     * @return the previous value
     */
    public final long getAndAdd(long delta) {
        return ((ALResponse) jemmOIF.processRequest(new ALRequest(AtomicNumAction.GET_AND_ADD, 0, delta))).getResult();
    }

    /**
     * Atomically increments by one the current value.
     *
     * @return the updated value
     */
    public final long incrementAndGet() {
        return addAndGet(1);
    }

    /**
     * Atomically decrements by one the current value.
     *
     * @return the updated value
     */
    public final long decrementAndGet() {
        return addAndGet(-1);
    }

    /**
     * Atomically adds the given value to the current value.
     *
     * @param delta the value to add
     * @return the updated value
     */
    public final long addAndGet(long delta) {
        return ((ALResponse) jemmOIF.processRequest(new ALRequest(AtomicNumAction.ADD_AND_GET, 0, delta))).getResult();
    }

    /**
     * Atomically sets the value to the given updated value
     * if the current value {@code ==} the expected value.
     *
     * @param expected the expected value
     * @param newValue the new value
     * @return true if successful. False return indicates that
     * the actual value was not equal to the expected value.
     */
    public final boolean compareAndSet(long expected, long newValue) {
        return ((ALResponse) jemmOIF.processRequest(new ALRequest(AtomicNumAction.COMPARE_AND_SET, expected, newValue))).isChanged();
    }

    /**
     * Returns the String representation of the current value.
     * @return the String representation of the current value.
     */
    @Override
    public String toString() {
        return Long.toString(get());
    }

    public int intValue() {
        return (int) get();
    }

    public long longValue() {
        return get();
    }

    public float floatValue() {
        return (float) get();
    }

    public double doubleValue() {
        return (double) get();
    }

    @Override
    public int hashCode() {
        long value = get();
        return (int) (value ^ (value >>> 32));
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        JemmAtomicLong other = (JemmAtomicLong) obj;
        if (this.get() != other.get()) return false;
        return true;
    }
}

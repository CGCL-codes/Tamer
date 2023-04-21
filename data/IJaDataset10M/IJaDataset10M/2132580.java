package net.sourceforge.javagg.dataio.types;

/**
 * A <code>Record</code> to wrap a <code>long</code> array.
 */
public interface LongArrayRecord extends Record {

    /**
     * Returns a copy of the wrapped array.
     * @return a copy of the wrapped array
     */
    public long[] getLongArray();
}

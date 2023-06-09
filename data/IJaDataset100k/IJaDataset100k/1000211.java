package org.xtreemfs.babudb.index.writer;

public interface BlockWriter {

    /**
     * Adds a new key-value pair to the writer.
     * 
     * @param key
     * @param value
     */
    public abstract void add(Object key, Object value);

    /**
     * Returns a serialized representation of all data previously added to the
     * block writer. Implementations may assume that this method will be only
     * invoked once, and that no more key-value-pairs will be added afterwards.
     * 
     * @return a serialized representation of all data previously added to the
     *         block writer
     */
    public abstract SerializedBlock serialize();

    /**
     * Returns the block key, i.e. the first key in the block.
     * 
     * @return the block key
     */
    public abstract Object getBlockKey();
}

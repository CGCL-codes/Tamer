package org.xtreemfs.babudb.api.database;

import java.util.Iterator;
import java.util.Map.Entry;

/**
 * A result set for prefix and range queries.
 * 
 * <p>
 * In addition to the methods inherited from the <code>java.util.Iterator</code>
 * interface, it defines an extra method to explicitly free any buffers bound to
 * the iterator. Applications that use prefix or range queries should invoke
 * <code>free()</code> once the result set is no longer needed, so as to keep
 * the memory footprint of BabuDB as low as possible.
 * </p>
 * 
 * @author stender
 * 
 */
public interface ResultSet<K, V> extends Iterator<Entry<K, V>> {

    /**
     * Frees any resources attached to the iterator.
     */
    public void free();
}

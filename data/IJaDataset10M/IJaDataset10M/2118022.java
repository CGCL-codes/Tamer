package org.enerj.apache.commons.collections.iterators;

import java.util.Iterator;
import org.enerj.apache.commons.collections.ResettableIterator;

/** 
 * Provides an implementation of an empty iterator.
 * <p>
 * This class provides an implementation of an empty iterator.
 * This class provides for binary compatability between Commons Collections
 * 2.1.1 and 3.1 due to issues with <code>IteratorUtils</code>.
 *
 * @since Commons Collections 2.1.1 and 3.1
 * @version $Revision: 155406 $ $Date: 2005-02-26 12:55:26 +0000 (Sat, 26 Feb 2005) $
 * 
 * @author Stephen Colebourne
 */
public class EmptyIterator extends AbstractEmptyIterator implements ResettableIterator {

    /**
     * Singleton instance of the iterator.
     * @since Commons Collections 3.1
     */
    public static final ResettableIterator RESETTABLE_INSTANCE = new EmptyIterator();

    /**
     * Singleton instance of the iterator.
     * @since Commons Collections 2.1.1 and 3.1
     */
    public static final Iterator INSTANCE = RESETTABLE_INSTANCE;

    /**
     * Constructor.
     */
    protected EmptyIterator() {
        super();
    }
}

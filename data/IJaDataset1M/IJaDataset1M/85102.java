package org.jivesoftware.smack.util.collections;

/**
 * Provides an implementation of an empty map iterator.
 *
 * @author Matt Hall, John Watkinson, Stephen Colebourne
 * @version $Revision: 1.1 $ $Date: 2005/10/11 17:05:24 $
 * @since Commons Collections 3.1
 */
public class EmptyMapIterator extends AbstractEmptyIterator implements MapIterator, ResettableIterator {

    /**
     * Singleton instance of the iterator.
     *
     * @since Commons Collections 3.1
     */
    public static final MapIterator INSTANCE = new EmptyMapIterator();

    /**
     * Constructor.
     */
    protected EmptyMapIterator() {
        super();
    }
}

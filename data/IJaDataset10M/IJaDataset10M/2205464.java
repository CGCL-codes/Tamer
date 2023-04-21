package org.enerj.apache.commons.collections.iterators;

import java.util.Enumeration;
import java.util.Iterator;

/** 
 * Adapter to make an {@link Iterator Iterator} instance appear to be
 * an {@link Enumeration Enumeration} instance.
 *
 * @since Commons Collections 1.0
 * @version $Revision: 155406 $ $Date: 2005-02-26 12:55:26 +0000 (Sat, 26 Feb 2005) $
 * 
 * @author <a href="mailto:jstrachan@apache.org">James Strachan</a>
 */
public class IteratorEnumeration implements Enumeration {

    /** The iterator being decorated. */
    private Iterator iterator;

    /**
     * Constructs a new <code>IteratorEnumeration</code> that will not 
     * function until {@link #setIterator(Iterator) setIterator} is  
     * invoked.
     */
    public IteratorEnumeration() {
        super();
    }

    /**
     * Constructs a new <code>IteratorEnumeration</code> that will use
     * the given iterator. 
     * 
     * @param iterator  the iterator to use
     */
    public IteratorEnumeration(Iterator iterator) {
        super();
        this.iterator = iterator;
    }

    /**
     *  Returns true if the underlying iterator has more elements.
     *
     *  @return true if the underlying iterator has more elements
     */
    public boolean hasMoreElements() {
        return iterator.hasNext();
    }

    /**
     *  Returns the next element from the underlying iterator.
     *
     *  @return the next element from the underlying iterator.
     *  @throws java.util.NoSuchElementException  if the underlying iterator has no
     *    more elements
     */
    public Object nextElement() {
        return iterator.next();
    }

    /**
     *  Returns the underlying iterator.
     * 
     *  @return the underlying iterator
     */
    public Iterator getIterator() {
        return iterator;
    }

    /**
     *  Sets the underlying iterator.
     *
     *  @param iterator  the new underlying iterator
     */
    public void setIterator(Iterator iterator) {
        this.iterator = iterator;
    }
}

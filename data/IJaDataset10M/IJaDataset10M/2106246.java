package org.softnetwork.util.collections;

import java.util.Arrays;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * @author $Author: smanciot $
 *
 * @version $Revision: 87 $
 * 
 * Adapter class that wraps an <code>Enumeration</code> around a Java2
 * collection classes object <code>Iterator</code> so that existing APIs
 * returning Enumerations can easily run on top of the new collections.
 * Constructors are provided to easliy create such wrappers.
 */
public final class Enumerator implements Enumeration {

    public Enumerator() {
        this(new Object[0]);
    }

    /**
	 * Return an Enumeration over the values of the specified array of Object.
	 * 
	 * @param array
	 *            Object[] whose values should be enumerated
	 */
    public Enumerator(Object[] array) {
        this(Arrays.asList(array).iterator());
    }

    /**
	 * Return an Enumeration over the values of the specified Collection.
	 * 
	 * @param collection
	 *            Collection whose values should be enumerated
	 */
    public Enumerator(Collection collection) {
        this(collection.iterator());
    }

    /**
	 * Return an Enumeration over the values returned by the specified Iterator.
	 * 
	 * @param iterator
	 *            Iterator to be wrapped
	 */
    public Enumerator(Iterator iterator) {
        this.iterator = iterator;
    }

    /**
	 * The <code>Iterator</code> over which the <code>Enumeration</code>
	 * represented by this class actually operates.
	 */
    private Iterator iterator = null;

    /**
	 * Tests if this enumeration contains more elements.
	 * 
	 * @return <code>true</code> if and only if this enumeration object
	 *         contains at least one more element to provide, <code>false</code>
	 *         otherwise
	 */
    public boolean hasMoreElements() {
        return (iterator.hasNext());
    }

    /**
	 * Returns the next element of this enumeration if this enumeration has at
	 * least one more element to provide.
	 * 
	 * @return the next element of this enumeration
	 * 
	 * @exception NoSuchElementException
	 *                if no more elements exist
	 */
    public Object nextElement() throws NoSuchElementException {
        return (iterator.next());
    }
}

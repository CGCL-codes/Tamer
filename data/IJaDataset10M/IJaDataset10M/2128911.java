package org.enerj.apache.commons.collections.collection;

import java.util.Collection;
import java.util.Iterator;
import org.enerj.apache.commons.collections.Unmodifiable;
import org.enerj.apache.commons.collections.iterators.UnmodifiableIterator;

/**
 * Decorates another <code>Collection</code> to ensure it can't be altered.
 * <p>
 * This class is Serializable from Commons Collections 3.1.
 *
 * @since Commons Collections 3.0
 * @version $Revision: 155406 $ $Date: 2005-02-26 12:55:26 +0000 (Sat, 26 Feb 2005) $
 * 
 * @author Stephen Colebourne
 */
public final class UnmodifiableCollection extends AbstractSerializableCollectionDecorator implements Unmodifiable {

    /** Serialization version */
    private static final long serialVersionUID = -239892006883819945L;

    /**
     * Factory method to create an unmodifiable collection.
     * <p>
     * If the collection passed in is already unmodifiable, it is returned.
     * 
     * @param coll  the collection to decorate, must not be null
     * @return an unmodifiable collection
     * @throws IllegalArgumentException if collection is null
     */
    public static Collection decorate(Collection coll) {
        if (coll instanceof Unmodifiable) {
            return coll;
        }
        return new UnmodifiableCollection(coll);
    }

    /**
     * Constructor that wraps (not copies).
     * 
     * @param coll  the collection to decorate, must not be null
     * @throws IllegalArgumentException if collection is null
     */
    private UnmodifiableCollection(Collection coll) {
        super(coll);
    }

    public Iterator iterator() {
        return UnmodifiableIterator.decorate(getCollection().iterator());
    }

    public boolean add(Object object) {
        throw new UnsupportedOperationException();
    }

    public boolean addAll(Collection coll) {
        throw new UnsupportedOperationException();
    }

    public void clear() {
        throw new UnsupportedOperationException();
    }

    public boolean remove(Object object) {
        throw new UnsupportedOperationException();
    }

    public boolean removeAll(Collection coll) {
        throw new UnsupportedOperationException();
    }

    public boolean retainAll(Collection coll) {
        throw new UnsupportedOperationException();
    }
}

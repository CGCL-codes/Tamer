package org.apache.commons.collections.primitives.adapters;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Iterator;
import org.apache.commons.collections.primitives.ShortCollection;

/**
 * @since Commons Primitives 1.0
 * @version $Revision: 1.1.4.3 $ $Date: 2008/04/26 11:00:55 $
 * @author Rodney Waldhoff
 */
abstract class AbstractShortCollectionCollection implements Collection {

    public boolean add(final Object element) {
        return getShortCollection().add(((Number) element).shortValue());
    }

    public boolean addAll(final Collection c) {
        return getShortCollection().addAll(CollectionShortCollection.wrap(c));
    }

    public void clear() {
        getShortCollection().clear();
    }

    public boolean contains(final Object element) {
        return getShortCollection().contains(((Number) element).shortValue());
    }

    public boolean containsAll(final Collection c) {
        return getShortCollection().containsAll(CollectionShortCollection.wrap(c));
    }

    @Override
    public String toString() {
        return getShortCollection().toString();
    }

    public boolean isEmpty() {
        return getShortCollection().isEmpty();
    }

    /**
	 * {@link ShortIteratorIterator#wrap wraps} the
	 * {@link org.apache.commons.collections.primitives.ShortIterator ShortIterator}
	 * returned by my underlying {@link ShortCollection ShortCollection}, if
	 * any.
	 */
    public Iterator iterator() {
        return ShortIteratorIterator.wrap(getShortCollection().iterator());
    }

    public boolean remove(final Object element) {
        return getShortCollection().removeElement(((Number) element).shortValue());
    }

    public boolean removeAll(final Collection c) {
        return getShortCollection().removeAll(CollectionShortCollection.wrap(c));
    }

    public boolean retainAll(final Collection c) {
        return getShortCollection().retainAll(CollectionShortCollection.wrap(c));
    }

    public int size() {
        return getShortCollection().size();
    }

    public Object[] toArray() {
        final short[] a = getShortCollection().toArray();
        final Object[] A = new Object[a.length];
        for (int i = 0; i < a.length; i++) {
            A[i] = new Short(a[i]);
        }
        return A;
    }

    public Object[] toArray(Object[] A) {
        final short[] a = getShortCollection().toArray();
        if (A.length < a.length) {
            A = (Object[]) (Array.newInstance(A.getClass().getComponentType(), a.length));
        }
        for (int i = 0; i < a.length; i++) {
            A[i] = new Short(a[i]);
        }
        if (A.length > a.length) {
            A[a.length] = null;
        }
        return A;
    }

    protected abstract ShortCollection getShortCollection();
}

package org.apache.commons.collections.primitives.adapters;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Iterator;
import org.apache.commons.collections.primitives.CharCollection;

/**
 * @since Commons Primitives 1.0
 * @version $Revision: 1.1.4.2 $ $Date: 2008/02/24 14:34:13 $
 * @author Rodney Waldhoff
 */
abstract class AbstractCharCollectionCollection implements Collection {

    public boolean add(final Object element) {
        return getCharCollection().add(((Character) element).charValue());
    }

    public boolean addAll(final Collection c) {
        return getCharCollection().addAll(CollectionCharCollection.wrap(c));
    }

    public void clear() {
        getCharCollection().clear();
    }

    public boolean contains(final Object element) {
        return getCharCollection().contains(((Character) element).charValue());
    }

    public boolean containsAll(final Collection c) {
        return getCharCollection().containsAll(CollectionCharCollection.wrap(c));
    }

    @Override
    public String toString() {
        return getCharCollection().toString();
    }

    public boolean isEmpty() {
        return getCharCollection().isEmpty();
    }

    /**
	 * {@link CharIteratorIterator#wrap wraps} the
	 * {@link org.apache.commons.collections.primitives.CharIterator CharIterator}
	 * returned by my underlying {@link CharCollection CharCollection}, if any.
	 */
    public Iterator iterator() {
        return CharIteratorIterator.wrap(getCharCollection().iterator());
    }

    public boolean remove(final Object element) {
        return getCharCollection().removeElement(((Character) element).charValue());
    }

    public boolean removeAll(final Collection c) {
        return getCharCollection().removeAll(CollectionCharCollection.wrap(c));
    }

    public boolean retainAll(final Collection c) {
        return getCharCollection().retainAll(CollectionCharCollection.wrap(c));
    }

    public int size() {
        return getCharCollection().size();
    }

    public Object[] toArray() {
        final char[] a = getCharCollection().toArray();
        final Object[] A = new Object[a.length];
        for (int i = 0; i < a.length; i++) {
            A[i] = new Character(a[i]);
        }
        return A;
    }

    public Object[] toArray(Object[] A) {
        final char[] a = getCharCollection().toArray();
        if (A.length < a.length) {
            A = (Object[]) (Array.newInstance(A.getClass().getComponentType(), a.length));
        }
        for (int i = 0; i < a.length; i++) {
            A[i] = new Character(a[i]);
        }
        if (A.length > a.length) {
            A[a.length] = null;
        }
        return A;
    }

    protected abstract CharCollection getCharCollection();
}

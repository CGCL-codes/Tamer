package maze.commons.adv_shared.generic_impl;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import maze.commons.generic.EmptyCheckableIterable;
import maze.commons.shared.util.CollectionSharedUtils;

/**
 * 
 * @author Normunds Mazurs
 */
public class EmptyCheckableIterableImpl<T> implements EmptyCheckableIterable<T> {

    protected final Collection<T> actualCollection;

    public EmptyCheckableIterableImpl(final Collection<T> actualCollection) {
        this.actualCollection = actualCollection;
    }

    @Override
    public int hashCode() {
        return actualCollection.hashCode();
    }

    @Override
    public boolean equals(final Object another) {
        return another instanceof Iterable && CollectionSharedUtils.equals((Iterable<?>) another, actualCollection);
    }

    @Override
    public Iterator<T> iterator() {
        return actualCollection.iterator();
    }

    @Override
    public boolean isEmpty() {
        return actualCollection.isEmpty();
    }

    public static <T> EmptyCheckableIterable<T> create(final Collection<T> actualCollection) {
        return new EmptyCheckableIterableImpl<T>(actualCollection);
    }

    public static <T> EmptyCheckableIterable<T> createUnmodifiable(final Collection<T> actualCollection) {
        return create(Collections.unmodifiableCollection(actualCollection));
    }
}

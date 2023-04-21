package pl.org.minions.utils.collections;

import java.util.Iterator;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Thread-safe iterator. Does not allow to modify
 * collection.
 * @param <Y>
 *            type stored in collection
 */
public class ThreadSafeIterator<Y> implements Iterator<Y> {

    private Iterator<Y> innerIterator;

    private AtomicInteger referenceCouter;

    /**
     * Default operator, hidden to scope of package -
     * Iterator creation by function iterator() of specific
     * collection.
     * @param collection
     *            collection for which iterator will be
     *            created
     */
    ThreadSafeIterator(ThreadSafeCollection<Y> collection) {
        this.innerIterator = collection.unsafeIterator();
        this.referenceCouter = collection.getInnerCollection().getReferenceCounterReference();
        checkHasNext();
    }

    /**
     * Default operator, hidden to scope of package -
     * Iterator creation by function iterator() of specific
     * collection.
     * @param set
     *            collection for which iterator will be
     *            created
     */
    ThreadSafeIterator(ThreadSafeSet<Y> set) {
        this.innerIterator = set.unsafeIterator();
        this.referenceCouter = set.getInnerSet().getReferenceCounterReference();
        checkHasNext();
    }

    private void checkHasNext() {
        if (!innerIterator.hasNext()) {
            referenceCouter.decrementAndGet();
            innerIterator = null;
        }
    }

    /** {@inheritDoc} */
    @Override
    public boolean hasNext() {
        return innerIterator != null;
    }

    /** {@inheritDoc} */
    @Override
    public Y next() {
        if (innerIterator == null) {
            throw new UnsupportedOperationException("Reached end of collection");
        }
        Y result = innerIterator.next();
        checkHasNext();
        return result;
    }

    /** {@inheritDoc} */
    @Override
    public void remove() {
        throw new UnsupportedOperationException("Not allowed to modify collection");
    }
}

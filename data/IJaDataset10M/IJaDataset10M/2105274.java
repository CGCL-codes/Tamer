package net.conquiris.api.search;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.Iterator;
import java.util.List;
import com.google.common.base.Objects;
import com.google.common.collect.ImmutableList;

/**
 * Result representing a page of search results. Results are 0-indexed.
 * @author Andres Rodriguez
 */
public final class PageResult<T> extends Result implements Iterable<T> {

    /** Serial UID. */
    private static final long serialVersionUID = -1123023188407973900L;

    /** Empty page. */
    private static final PageResult<Object> EMPTY = new PageResult<Object>(0, 0.0f, 0L, 0, ImmutableList.of());

    /** First result. */
    private final int firstResult;

    /** Items returned by the query. */
    private final ImmutableList<T> items;

    /**
	 * Returns the empty page.
	 * @param <T> Item type.
	 * @return The empty apge.
	 */
    @SuppressWarnings("unchecked")
    public static <T> PageResult<T> empty() {
        return (PageResult<T>) EMPTY;
    }

    /**
	 * Returns a page with no results.
	 * @param totalHits Number of hits.
	 * @param maxScore Maximum score.
	 * @param time Time taken by the query (ms).
	 * @param firstResult First requested result index.
	 */
    public static <T> PageResult<T> notFound(int totalHits, float maxScore, long time, int firstResult) {
        return new PageResult<T>(totalHits, maxScore, time, firstResult, ImmutableList.<T>of());
    }

    /**
	 * Returns a page with no results in a query with no results.
	 * @param time Time taken by the query (ms).
	 * @param firstResult First requested result index.
	 */
    public static <T> PageResult<T> notFound(long time, int firstResult) {
        return notFound(0, 0, time, firstResult);
    }

    /**
	 * Constructor.
	 * @param totalHits Number of hits.
	 * @param maxScore Maximum score.
	 * @param time Time taken by the query (ms).
	 * @param firstResult First result index.
	 * @param items Found items.
	 */
    public static <T> PageResult<T> found(int totalHits, float maxScore, long time, int firstResult, List<T> items) {
        return new PageResult<T>(totalHits, maxScore, time, firstResult, items);
    }

    /**
	 * Constructor.
	 * @param totalHits Number of hits.
	 * @param maxScore Maximum score.
	 * @param time Time taken by the query (ms).
	 * @param firstResult First result index.
	 * @param items Found items.
	 */
    private PageResult(final int totalHits, final float maxScore, final long time, final int firstResult, final List<T> items) {
        super(totalHits, maxScore, time);
        checkNotNull(items, "The items list must be provided");
        checkArgument(firstResult >= 0, "The first result must be >= 0");
        this.items = ImmutableList.copyOf(items);
        int n = this.items.size();
        checkArgument(n == 0 || totalHits >= firstResult + n, "The total number of hits must be >= first result + number of items");
        this.firstResult = firstResult;
    }

    private void checkNotEmpty() {
        checkState(!isEmpty(), "The result is empty");
    }

    /**
	 * Returns the first requested result index. Will the equal to the first result iff the page is
	 * not empty.
	 */
    public int getFirstRequestedResult() {
        return firstResult;
    }

    /**
	 * Returns the first result index.
	 * @throws IllegalStateException if the result is empty.
	 */
    public int getFirstResult() {
        checkNotEmpty();
        return firstResult;
    }

    public int getLastResult() {
        checkNotEmpty();
        return firstResult + items.size() - 1;
    }

    /** Returns whether the result is empty. */
    public boolean isEmpty() {
        return items.isEmpty();
    }

    /** Returns the number of elements in the page. */
    public int size() {
        return items.size();
    }

    /** Returns the items in the page (never {@code null}. */
    public List<T> getItems() {
        return items;
    }

    /**
	 * Return the element in the requested index (relative to the page).
	 * @throws IndexOutOfBoundsException if the index is out of range (index < 0 || index >= size())
	 */
    public T get(int index) {
        return items.get(firstResult + index);
    }

    public Iterator<T> iterator() {
        return items.iterator();
    }

    @Override
    public boolean equals(Object obj) {
        final PageResult<?> other = equalsResult(obj, PageResult.class);
        if (other != null) {
            return this.firstResult == other.firstResult && Objects.equal(this.items, other.items);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(super.hashCode(), firstResult, items);
    }

    private static class SerializationProxy<T> implements Serializable {

        /** Serial UID. */
        private static final long serialVersionUID = 3709651284308359760L;

        /** Total hits of the query. */
        private final int totalHits;

        /** Maximum score. */
        private final float maxScore;

        /** Time taken by the query (ms). */
        private final long time;

        /** First result. */
        private final int firstResult;

        /** Items returned by the query. */
        private final ImmutableList<T> items;

        public SerializationProxy(PageResult<T> r) {
            this.totalHits = r.getTotalHits();
            this.maxScore = r.getMaxScore();
            this.time = r.getTime();
            this.firstResult = r.getFirstRequestedResult();
            this.items = r.items;
        }

        private Object readResolve() {
            return new PageResult<T>(totalHits, maxScore, time, firstResult, items);
        }
    }

    private Object writeReplace() {
        return new SerializationProxy<T>(this);
    }

    private void readObject(ObjectInputStream stream) throws InvalidObjectException {
        throw new InvalidObjectException("Proxy required");
    }
}

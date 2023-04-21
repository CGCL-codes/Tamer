package fitgoodies.file;

import java.util.Iterator;

/**
 * Wrapper class which encapsulates an <code>Iterator</code> so that it can be
 * used as an <code>Iterable</code>.
 *
 * @param <T> type of elements returned by the iterator
 *
 * @version $Id: IteratorHelper.java 185 2009-08-17 13:47:24Z jwierum $
 * @author jwierum
 */
public class IteratorHelper<T> implements Iterable<T> {

    private final Iterator<T> it;

    /**
	 * Constructs a new wrapper.
	 * @param iterator iterator to encapsulate
	 */
    public IteratorHelper(final Iterator<T> iterator) {
        this.it = iterator;
    }

    /**
	 * Returns the encapsulated iterator.
	 * @return the encapsulated iterator
	 */
    @Override
    public final Iterator<T> iterator() {
        return it;
    }
}

package joe.collect;

import static com.google.common.collect.Iterators.unmodifiableIterator;
import java.util.AbstractList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;
import java.util.RandomAccess;
import com.google.common.base.Preconditions;
import com.google.common.collect.Iterables;
import com.google.common.primitives.Ints;

/**
 * Unmodifiable list view over multiple lists, indexing transparently.
 * 
 * @author Joe Kearney
 */
public final class ConcatenatedList<E> extends AbstractList<E> {

    /**
	 * Returns a view over the lists, indexing through them transparently.
	 * <p>
	 * The returned list does not implement {@link RandomAccess} since lookups do not take constant time; for {@code m}
	 * {@code RandomAccess} lists of any length, lookup takes {@code O(m)} time.
	 * <p>
	 * The returned list is unmodifiable; modification operations throw {@link UnsupportedOperationException}.
	 * 
	 * @param lists lists to concatenate
	 * @return a concatenated view over the lists
	 */
    public static <E> List<E> concat(final Iterable<? extends List<? extends E>> lists) {
        return new ConcatenatedList<E>(lists);
    }

    private class IndexBasedListIterator implements ListIterator<E> {

        /**
		 * Index of element to be returned by subsequent call to next.
		 */
        private int cursor;

        public IndexBasedListIterator(int index) {
            Preconditions.checkPositionIndex(index, ConcatenatedList.this.size(), "Iterator index");
            this.cursor = index;
        }

        @Override
        public boolean hasNext() {
            return ConcatenatedList.this.size() > cursor;
        }

        @Override
        public E next() {
            final E ret;
            try {
                ret = ConcatenatedList.this.get(cursor);
            } catch (IndexOutOfBoundsException e) {
                throw newNoSuchElementException(e);
            }
            ++cursor;
            return ret;
        }

        @Override
        public boolean hasPrevious() {
            return cursor > 0;
        }

        @Override
        public E previous() {
            int index = cursor - 1;
            E ret;
            try {
                ret = ConcatenatedList.this.get(index);
            } catch (IndexOutOfBoundsException e) {
                throw newNoSuchElementException(e);
            }
            cursor = index;
            return ret;
        }

        private NoSuchElementException newNoSuchElementException(IndexOutOfBoundsException e) {
            NoSuchElementException nsee = new NoSuchElementException();
            nsee.initCause(e);
            return nsee;
        }

        @Override
        public int nextIndex() {
            return cursor;
        }

        @Override
        public int previousIndex() {
            return cursor - 1;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }

        @Override
        public void set(E e) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void add(E e) {
            throw new UnsupportedOperationException();
        }
    }

    private final Iterable<? extends List<? extends E>> lists;

    private ConcatenatedList(Iterable<? extends List<? extends E>> lists) {
        this.lists = lists;
    }

    @Override
    public E get(final int index) {
        if (index < 0) {
            throw new IndexOutOfBoundsException("Invalid list index: " + index);
        }
        int currentIndex = 0;
        int nextIndex = 0;
        Iterator<? extends List<? extends E>> iter = lists.iterator();
        while (iter.hasNext()) {
            List<? extends E> list = iter.next();
            nextIndex += list.size();
            if (nextIndex > index) {
                return list.get(index - currentIndex);
            }
            currentIndex = nextIndex;
        }
        throw new IndexOutOfBoundsException("Invalid list index: " + index);
    }

    @Override
    public Iterator<E> iterator() {
        return unmodifiableIterator(Iterables.concat(lists).iterator());
    }

    @Override
    public ListIterator<E> listIterator(int index) {
        return new IndexBasedListIterator(index);
    }

    @Override
    public int size() {
        long size = 0;
        for (List<? extends E> list : lists) {
            size += list.size();
        }
        return Ints.saturatedCast(size);
    }
}

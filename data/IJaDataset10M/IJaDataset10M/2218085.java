package org.torweg.pulse.util;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.AbstractSet;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * additional {@code Collection} related utilities not provided by
 * {@code java.util.Collections}.
 * 
 * @author Thomas Weber
 * @version $Revision$
 */
public final class CollectionUtils {

    /**
	 * hidden constructor.
	 */
    private CollectionUtils() {
        super();
    }

    /**
	 * Returns a set backed by the specified map. The resulting set displays the
	 * same ordering, concurrency, and performance characteristics as the
	 * backing map. In essence, this factory method provides a {@link Set}
	 * implementation corresponding to any {@link Map} implementation. There is
	 * no need to use this method on a {@link Map} implementation that already
	 * has a corresponding {@link Set} implementation (such as {@link HashMap}
	 * or {@link TreeMap}).
	 * 
	 * <p>
	 * Each method invocation on the set returned by this method results in
	 * exactly one method invocation on the backing map or its <tt>keySet</tt>
	 * view, with one exception. The <tt>addAll</tt> method is implemented as a
	 * sequence of <tt>put</tt> invocations on the backing map.
	 * 
	 * <p>
	 * The specified map must be empty at the time this method is invoked, and
	 * should not be accessed directly after this method returns. These
	 * conditions are ensured if the map is created empty, passed directly to
	 * this method, and no reference to the map is retained, as illustrated in
	 * the following code fragment:
	 * 
	 * <pre>
	 * Set&lt;Object&gt; identityHashSet = Sets
	 * 		.newSetFromMap(new IdentityHashMap&lt;Object, Boolean&gt;());
	 * </pre>
	 * 
	 * This method has the same behavior as the JDK 6 method
	 * {@code Collections.newSetFromMap()}. The returned set is serializable if
	 * the backing map is.
	 * 
	 * @param <E>
	 *            the type of the {@code Set}
	 * @param map
	 *            the backing map
	 * @return the set backed by the map
	 * @throws IllegalArgumentException
	 *             if <tt>map</tt> is not empty
	 */
    protected static <E> Set<E> newSetFromMap(final Map<E, Boolean> map) {
        return new SetFromMap<E>(map);
    }

    /**
	 * returns a a concurrent {@code Set<E>}.
	 * 
	 * @param <E>
	 *            the type of the {@code Set}
	 * @return a concurrent {@code Set<E>}
	 */
    public static <E> Set<E> newConcurrentSet() {
        return new SetFromMap<E>(new ConcurrentHashMap<E, Boolean>());
    }

    /**
	 * the {@code Set} wrapper for the {@code Map}.
	 * 
	 * @param <E>
	 *            the type of the {@code Set}
	 */
    private static class SetFromMap<E> extends AbstractSet<E> implements Set<E>, Serializable {

        /**
		 * serialVersionUID.
		 */
        private static final long serialVersionUID = 6531039244984504007L;

        /**
		 * the backing map.
		 */
        private final Map<E, Boolean> m;

        /**
		 * its key set.
		 */
        private transient Set<E> s;

        /**
		 * builds the wrapper.
		 * 
		 * @param map
		 *            the map to wrap
		 */
        protected SetFromMap(final Map<E, Boolean> map) {
            if (!map.isEmpty()) {
                throw new IllegalArgumentException("Map is non-empty");
            }
            this.m = map;
            this.s = map.keySet();
        }

        /**
		 * {@inheritDoc}
		 */
        @Override
        public void clear() {
            this.m.clear();
        }

        /**
		 * {@inheritDoc}
		 */
        @Override
        public int size() {
            return this.m.size();
        }

        /**
		 * {@inheritDoc}
		 */
        @Override
        public boolean isEmpty() {
            return this.m.isEmpty();
        }

        /**
		 * {@inheritDoc}
		 */
        @Override
        public boolean contains(final Object o) {
            return this.m.containsKey(o);
        }

        /**
		 * {@inheritDoc}
		 */
        @Override
        public boolean remove(final Object o) {
            return this.m.remove(o) != null;
        }

        /**
		 * {@inheritDoc}
		 */
        @Override
        public boolean add(final E e) {
            return this.m.put(e, Boolean.TRUE) == null;
        }

        /**
		 * {@inheritDoc}
		 */
        @Override
        public Iterator<E> iterator() {
            return this.s.iterator();
        }

        /**
		 * {@inheritDoc}
		 */
        @Override
        public Object[] toArray() {
            return this.s.toArray();
        }

        /**
		 * {@inheritDoc}
		 */
        @Override
        public <T> T[] toArray(final T[] a) {
            return this.s.toArray(a);
        }

        /**
		 * {@inheritDoc}
		 */
        @Override
        public String toString() {
            return this.s.toString();
        }

        /**
		 * {@inheritDoc}
		 */
        @Override
        public int hashCode() {
            return this.s.hashCode();
        }

        /**
		 * {@inheritDoc}
		 */
        @Override
        public boolean equals(final Object object) {
            return this == object || this.s.equals(object);
        }

        /**
		 * {@inheritDoc}
		 */
        @Override
        public boolean containsAll(final Collection<?> c) {
            return this.s.containsAll(c);
        }

        /**
		 * {@inheritDoc}
		 */
        @Override
        public boolean removeAll(final Collection<?> c) {
            return this.s.removeAll(c);
        }

        /**
		 * {@inheritDoc}
		 */
        @Override
        public boolean retainAll(final Collection<?> c) {
            return this.s.retainAll(c);
        }

        /**
		 * read object implementation.
		 * 
		 * @param stream
		 *            the stream
		 * @throws IOException
		 *             on errors
		 * @throws ClassNotFoundException
		 *             on errors
		 */
        private void readObject(final ObjectInputStream stream) throws IOException, ClassNotFoundException {
            stream.defaultReadObject();
            this.s = this.m.keySet();
        }
    }
}

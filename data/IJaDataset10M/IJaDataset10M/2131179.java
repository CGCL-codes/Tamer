package jezuch.utils;

import java.util.AbstractSet;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

/**
 * This is a wrapper {@link Collection} that converts any two {@link Set}s to a
 * logical intersection (as in set theory) of them. Any object belongs to
 * {@link IntersectSet} if and only if it belongs to both of the underlying
 * {@link Set}s.
 * FIXME: tests
 * 
 * @author ksobolewski
 * @param <E>
 *            the common suptype of elements held by the underlying {@link Set}s
 */
public class IntersectSet<E> extends AbstractSet<E> {

    private final Set<? extends E> s1;

    private final Set<? extends E> s2;

    public IntersectSet(Set<? extends E> s1, Set<? extends E> s2) {
        this.s1 = s1;
        this.s2 = s2;
    }

    @Override
    public int size() {
        boolean swap = (s1 instanceof IntersectSet<?> && !(s2 instanceof IntersectSet<?>)) || (s1 instanceof IntersectSet<?> == s2 instanceof IntersectSet<?> && s1.size() > s2.size());
        Set<? extends E> c1 = swap ? s2 : s1;
        Set<? extends E> c2 = swap ? s1 : s2;
        int ret = 0;
        for (E e : c1) if (c2.contains(e)) ret++;
        return ret;
    }

    @Override
    public boolean isEmpty() {
        return !iterator().hasNext();
    }

    @Override
    public boolean contains(Object o) {
        return s1.contains(o) && s2.contains(o);
    }

    @Override
    public Iterator<E> iterator() {
        final Set<? extends E> s1 = this.s1, s2 = this.s2;
        return new AbstractIterator<E>() {

            private final Iterator<? extends E> iter = s1.iterator();

            @Override
            protected E fetchNext() {
                while (iter.hasNext()) {
                    E next = iter.next();
                    if (s2.contains(next)) return next;
                }
                close();
                return null;
            }
        };
    }
}

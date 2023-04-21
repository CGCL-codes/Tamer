package xcordion.util;

import java.util.Iterator;

public abstract class WrappingIterable<B, R> implements Iterable<R> {

    private Iterable<B> base;

    public WrappingIterable(Iterable<B> base) {
        this.base = base;
    }

    public Iterator<R> iterator() {
        final Iterator<B> iterator = base.iterator();
        return new Iterator<R>() {

            public boolean hasNext() {
                return iterator.hasNext();
            }

            public R next() {
                return wrap(iterator.next());
            }

            public void remove() {
                iterator.remove();
            }
        };
    }

    protected abstract R wrap(B base);
}

package radius.chain.impl;

import radius.chain.Filter;
import radius.chain.FilterChain;

/**
 * @author <a href="mailto:zzzhc0508@hotmail.com">zzzhc</a>
 * 
 */
public class FilterChainImpl implements FilterChain {

    private Filter[] filters;

    private Object lock;

    private volatile int size;

    public FilterChainImpl() {
        this(10);
    }

    public FilterChainImpl(int initFilterSize) {
        assert initFilterSize > 0 : "initFilterSize must >0";
        filters = new Filter[initFilterSize];
        lock = new Object();
    }

    private void ensureCapacity() {
        if (size >= filters.length) {
            Filter[] temp = new Filter[filters.length * 2];
            System.arraycopy(filters, 0, temp, 0, filters.length);
            filters = temp;
        }
    }

    public void addFilter(Filter filter) {
        synchronized (lock) {
            ensureCapacity();
            filters[size++] = filter;
        }
    }

    public void removeFilter(int index) {
        synchronized (lock) {
            assert index >= 0 && index < size : "index out of bounds";
            filters[index].destroy();
            filters[index] = null;
            System.arraycopy(filters, index + 1, filters, index, size - index - 1);
            size--;
        }
    }

    public void doFilter(Object context) {
        new MockFilterChain(this).doFilter(context);
    }

    private static class MockFilterChain implements FilterChain {

        private int currentFilter;

        private FilterChainImpl impl;

        public MockFilterChain(FilterChainImpl impl) {
            this.impl = impl;
            currentFilter = 0;
        }

        public void addFilter(Filter filter) {
            throw new UnsupportedOperationException();
        }

        public void removeFilter(int index) {
            throw new UnsupportedOperationException();
        }

        public void doFilter(Object context) {
            if (currentFilter < impl.filters.length) {
                Filter filter = impl.filters[currentFilter++];
                if (filter != null) {
                    filter.doFilter(context, this);
                }
            }
        }
    }
}

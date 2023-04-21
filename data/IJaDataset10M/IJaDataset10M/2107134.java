package org.vous.facelib.filters;

import java.util.Iterator;
import java.util.LinkedList;
import org.vous.facelib.bitmap.Bitmap;

public class FilterSequence implements IFilter, Iterable<IFilter> {

    private LinkedList<IFilter> mFilters = null;

    protected long mOverhead;

    public FilterSequence() {
        mFilters = new LinkedList<IFilter>();
        mOverhead = 0;
    }

    public FilterSequence(IFilter filter) {
        this();
        addFilter(filter);
    }

    public int size() {
        return mFilters.size();
    }

    public void addFilter(IFilter filter) {
        mFilters.add(filter);
    }

    public boolean removeFilter(IFilter filter) {
        return mFilters.remove(filter);
    }

    /**
	 * 
	 * @return Computational overhead in milliseconds
	 */
    public long getOverhead() {
        return mOverhead;
    }

    public Bitmap apply(Bitmap source) {
        Bitmap dest = null;
        long before = System.currentTimeMillis();
        if (mFilters.isEmpty()) {
            mOverhead = 0;
            return source;
        }
        for (IFilter filter : mFilters) {
            dest = filter.apply(source);
        }
        mOverhead = (System.currentTimeMillis() - before);
        return dest;
    }

    public Iterator<IFilter> iterator() {
        return mFilters.iterator();
    }
}

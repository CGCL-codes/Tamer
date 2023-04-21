package com.ibm.safe.internal.filtering;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import com.ibm.wala.util.collections.Filter;

public final class AndFilter<T> implements Filter<T> {

    public AndFilter() {
    }

    public AndFilter(final Filter<T> leftFilter, final Filter<T> rightFilter) {
        this.classFilterList.add(leftFilter);
        this.classFilterList.add(rightFilter);
    }

    public boolean accepts(final T clazz) {
        for (Iterator<Filter<T>> iter = this.classFilterList.iterator(); iter.hasNext(); ) {
            if (!iter.next().accepts(clazz)) {
                return false;
            }
        }
        return true;
    }

    public void addFilter(final Filter<T> classFilter) {
        this.classFilterList.add(classFilter);
    }

    private final List<Filter<T>> classFilterList = new LinkedList<Filter<T>>();
}

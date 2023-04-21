package org.piuframework.samples.common;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * This file is based on the original IBatis implementation and was only
 * modified to be used in conjunction with Piu Framework and Hibernate.
 * 
 * The IBatis Framework uses this Class to encapsulate Cursor Functionality,
 * while we fetch the whole List and just simulate Cursor-Style Behaviour,
 * cursors can be done in Hibernate as well, but for clarity reasons we decided
 * not to use them here. 
 * 
 *  Performance is obviously going to suffer.  
 * 
 * Please be aware that this implementation might not represent current best practice patterns.
 * Piu and Hibernate integration done by:
 * - Alexander Kainz
 * - Eugene Klymenko
 */
public class PaginatedList implements Collection, Serializable {

    protected int pageSize;

    protected List delegate;

    protected List pageDelegate;

    protected int currentPage;

    public PaginatedList(int pageSize) {
        this(Collections.EMPTY_LIST, pageSize);
    }

    /**
	 * Use exisiting collection via pass by value semantics
	 * (creates a copy of the original collection)
	 * @param c
	 * @param pageSize
	 */
    public PaginatedList(Collection sourceCollection) {
        if (sourceCollection == null) {
            this.delegate = new ArrayList();
        } else {
            this.delegate = new ArrayList(sourceCollection);
        }
        this.pageSize = this.delegate.size();
    }

    /**
	 * Use exisiting collection via pass by value semantics
	 * (creates a copy of the original collection)
	 * @param c
	 * @param pageSize
	 */
    public PaginatedList(Collection sourceCollection, int pageSize) {
        this(sourceCollection);
        this.pageSize = pageSize;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void gotoPage(int pageNumber) {
        if (currentPage != pageNumber || pageDelegate == null) {
            checkBoundary(pageNumber);
            currentPage = pageNumber;
            initPage();
        }
    }

    /**
	 * @param pageNumber
	 */
    private void initPage() {
        ArrayList subCollection = new ArrayList();
        int offset = currentPage * pageSize;
        int length = Math.min(pageSize, delegate.size() - offset);
        for (int i = 0; i < length; i++) {
            subCollection.add(delegate.get(i + offset));
        }
        pageDelegate = subCollection;
    }

    public boolean isFirstPage() {
        return currentPage == 0;
    }

    public boolean isLastPage() {
        return !isNextPageAvailable();
    }

    public boolean isMiddlePage() {
        return !isFirstPage() ^ !isLastPage();
    }

    public boolean isNextPageAvailable() {
        return (currentPage + 1) * pageSize < delegate.size();
    }

    public boolean isPreviousPageAvailable() {
        return currentPage > 0;
    }

    public boolean nextPage() {
        gotoPage(currentPage + 1);
        return isLastPage();
    }

    public boolean previousPage() {
        gotoPage(currentPage - 1);
        return isFirstPage();
    }

    private void checkBoundary(int pageNumber) {
        if (pageNumber * pageSize > delegate.size()) throw new ArrayIndexOutOfBoundsException("page (" + pageNumber + ") does not exist");
    }

    public List getCurrentPage() {
        if (pageDelegate == null) {
            gotoPage(currentPage);
        }
        return pageDelegate;
    }

    public Iterator iterator() {
        return getCurrentPage().iterator();
    }

    public List getAllPages() {
        return delegate;
    }

    public int size() {
        return getCurrentPage().size();
    }

    public void clear() {
        delegate.clear();
    }

    public boolean isEmpty() {
        return getCurrentPage().isEmpty();
    }

    public boolean contains(Object o) {
        return getCurrentPage().contains(o);
    }

    public boolean containsAll(Collection c) {
        return getCurrentPage().contains(c);
    }

    public Object[] toArray() {
        return getCurrentPage().toArray();
    }

    public Object[] toArray(Object[] a) {
        return getCurrentPage().toArray(a);
    }

    public boolean add(Object o) {
        boolean result = delegate.add(o);
        initPage();
        return result;
    }

    public boolean addAll(Collection c) {
        boolean result = delegate.addAll(c);
        initPage();
        return result;
    }

    public boolean remove(Object o) {
        boolean result = delegate.remove(o);
        initPage();
        return result;
    }

    public boolean removeAll(Collection c) {
        boolean result = delegate.removeAll(c);
        initPage();
        return result;
    }

    public boolean retainAll(Collection c) {
        boolean result = delegate.retainAll(c);
        initPage();
        return result;
    }
}

package com.versant.core.jdo;

import java.util.*;

/**
 * This is a QueryResult that represent multiple datastore queries as one result.
 * This is used for queries against an horisontal baseclass.
 *
 * This class can have 3 states.
 * - sparse state: This is when only the 'get' method has been used.
 * - resolved state: If all the results is available in a underlying result list.
 * - not intialised: If there is no query state asociated whith this result.
 */
public class MultiPartQueryResult extends QueryResultBase {

    int size = -1;

    private QueryResult[] queryResults;

    private List totalResult;

    private QueryResult curRes;

    private int curIndex;

    public MultiPartQueryResult(Set queryResultSet) {
        queryResults = new QueryResult[queryResultSet.size()];
        queryResultSet.toArray(queryResults);
    }

    public void close() {
        for (int i = 0; i < queryResults.length; i++) {
            queryResults[i].close();
        }
    }

    public void setParams(Object[] params) {
    }

    public int size() {
        if (totalResult == null) {
            totalResult = new ArrayList();
            for (int i = 0; i < queryResults.length; i++) {
                totalResult.addAll(queryResults[i]);
            }
            size = totalResult.size();
        }
        return size;
    }

    public boolean isEmpty() {
        return size() == 0;
    }

    public Iterator createInternalIterNoFlush() {
        return null;
    }

    public Iterator iterator() {
        return new Iter(queryResults, false);
    }

    public Object[] toArray() {
        size();
        return totalResult.toArray();
    }

    public Object[] toArray(Object a[]) {
        size();
        return totalResult.toArray(a);
    }

    public Object get(int index) {
        size();
        return totalResult.get(index);
    }

    public boolean contains(Object o) {
        size();
        return totalResult.contains(o);
    }

    public boolean containsAll(Collection c) {
        size();
        return totalResult.containsAll(c);
    }

    public int indexOf(Object o) {
        size();
        return totalResult.indexOf(o);
    }

    public int lastIndexOf(Object o) {
        size();
        return totalResult.lastIndexOf(o);
    }

    public ListIterator listIterator() {
        return null;
    }

    public ListIterator listIterator(int index) {
        return null;
    }

    public List subList(int fromIndex, int toIndex) {
        return null;
    }

    class Iter implements Iterator {

        private QueryResult[] queryResults;

        private int nextIterIndex = 1;

        private Iterator curIterator;

        private boolean noFlush;

        public Iter(QueryResult[] queryResults, boolean noFlush) {
            this.queryResults = queryResults;
            curIterator = queryResults[0].iterator();
            this.noFlush = noFlush;
        }

        private Iterator getIter(int index) {
            if (noFlush) return queryResults[index].createInternalIterNoFlush(); else return queryResults[index].iterator();
        }

        public void remove() {
            curIterator.remove();
        }

        public boolean hasNext() {
            if (curIterator.hasNext()) return true;
            if (nextIterIndex == queryResults.length) return false;
            curIterator = queryResults[nextIterIndex++].iterator();
            return curIterator.hasNext();
        }

        public Object next() {
            return curIterator.next();
        }
    }
}

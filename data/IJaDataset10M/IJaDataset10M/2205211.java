package cn.edu.thss.iise.beehivez.server.index.petrinetindex.invertedindex.queryParser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;

public class Result {

    ArrayList<QueryResultItem> result = new ArrayList<QueryResultItem>();

    int size;

    public void rank() {
        Collections.sort(result, new Rank());
    }

    public int getSize() {
        return result.size();
    }

    public void add(QueryResultItem item) {
        result.add(item);
    }

    public Iterator<QueryResultItem> getIterator() {
        return result.iterator();
    }

    protected class Rank implements Comparator<QueryResultItem> {

        public int compare(QueryResultItem a, QueryResultItem b) {
            return QueryResultItem.rank(a, b);
        }
    }
}

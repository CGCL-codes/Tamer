package org.middleheaven.text.indexing;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class ListSearchHits<T> implements SearchHits<T> {

    List<SearchHit<T>> items;

    public ListSearchHits(int size) {
        this.items = new ArrayList<SearchHit<T>>(size);
    }

    public ListSearchHits() {
        this.items = new LinkedList<SearchHit<T>>();
    }

    @Override
    public int getSize() {
        return items.size();
    }

    @Override
    public boolean isEmpty() {
        return items.isEmpty();
    }

    @Override
    public Iterator<SearchHit<T>> iterator() {
        return (Iterator<SearchHit<T>>) items.iterator();
    }

    public void addHit(T document, HitScore score) {
        this.items.add(new SimpleSearchHit<T>(document, score));
    }
}

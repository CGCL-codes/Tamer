package es.optsicom.lib.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class RandomIterator<T> implements Iterator<T> {

    List<? extends T> list;

    static Random r = RandomManager.getRandom();

    public RandomIterator(Collection<? extends T> list) {
        this.list = new ArrayList<T>(list);
    }

    public boolean hasNext() {
        return !list.isEmpty();
    }

    public T next() {
        return list.remove(r.nextInt(list.size()));
    }

    public void remove() {
        throw new UnsupportedOperationException();
    }
}

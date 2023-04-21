package net.sourceforge.freejava.collection.comparator;

import java.util.Comparator;

public class ReverseComparator<T> implements Comparator<T> {

    private final Comparator<T> comparator;

    public ReverseComparator(Comparator<T> comparator) {
        if (comparator == null) throw new NullPointerException("comparator");
        this.comparator = comparator;
    }

    @Override
    public int compare(T o1, T o2) {
        return -comparator.compare(o1, o2);
    }
}

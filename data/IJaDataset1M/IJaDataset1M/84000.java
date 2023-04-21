package net.sourceforge.freejava.collection.comparator;

public class NaturalComparator<T extends Comparable<? super T>> implements NonNullComparator<T> {

    @Override
    public int compare(T o1, T o2) {
        return o1.compareTo(o2);
    }
}

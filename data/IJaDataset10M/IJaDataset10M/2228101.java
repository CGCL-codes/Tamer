package net.sourceforge.agsyslib.util;

import java.io.Serializable;

public class Pair<T1 extends Serializable & Comparable<T1>, T2 extends Serializable & Comparable<T2>> implements Serializable, Comparable<Pair<T1, T2>> {

    private static final long serialVersionUID = 1L;

    private final T1 first;

    private final T2 second;

    public Pair(T1 first, T2 second) {
        this.first = first;
        this.second = second;
    }

    public T1 getFirst() {
        return first;
    }

    public T2 getSecond() {
        return second;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((first == null) ? 0 : first.hashCode());
        result = prime * result + ((second == null) ? 0 : second.hashCode());
        return result;
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        Pair<T1, T2> other = (Pair<T1, T2>) obj;
        if (first == null) {
            if (other.first != null) return false;
        } else if (!first.equals(other.first)) return false;
        if (second == null) {
            if (other.second != null) return false;
        } else if (!second.equals(other.second)) return false;
        return true;
    }

    @Override
    public int compareTo(Pair<T1, T2> pair) {
        if (pair == null) throw new NullPointerException("Compare with null");
        int result = first.compareTo(pair.first);
        if (result == 0) {
            result = second.compareTo(pair.second);
        }
        return result;
    }

    @Override
    public String toString() {
        return "(" + first + ", " + second + ")";
    }
}

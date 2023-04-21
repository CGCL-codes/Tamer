package org.igsl.algorithm.auxiliary;

public class Pair<T, C> {

    T first;

    C second;

    public Pair(T first, C second) {
        this.first = first;
        this.second = second;
    }

    public T getFirst() {
        return first;
    }

    public C getSecond() {
        return second;
    }
}

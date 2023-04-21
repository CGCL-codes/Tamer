package ru.spbu.math.ontologycomparison.zhukova.util.impl;

import ru.spbu.math.ontologycomparison.zhukova.util.ITriple;

/**
 * @author Anna Zhukova
 */
public class Triple<F, S, T> extends Pair<F, S> implements ITriple<F, S, T> {

    private final T third;

    public Triple(F first, S second, T third) {
        super(first, second);
        this.third = third;
    }

    public T getThird() {
        return this.third;
    }

    public String toString() {
        return String.format("(%s, %s, %s)", this.getFirst(), this.getSecond(), this.getThird());
    }
}

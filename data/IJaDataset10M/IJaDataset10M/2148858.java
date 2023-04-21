package de.oklemenz.meta.ann.api;

public interface IntParameter<I extends Comparable<I>, E extends Comparable<E>, O extends Comparable<O>, A extends Comparable<A>> extends Parameter<I, E, O, A> {

    public int getIntValue();
}

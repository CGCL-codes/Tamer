package de.oklemenz.meta.ga.api;

public interface BooleanParameter<F extends Comparable<F>, L extends Comparable<L>, A extends Comparable<A>> extends Parameter<F, L, A> {

    public boolean getBooleanValue();
}

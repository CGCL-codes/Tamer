package de.oklemenz.meta.ann.api;

import java.util.List;

public interface ErrorFunction<I extends Comparable<I>, E extends Comparable<E>, O extends Comparable<O>, A extends Comparable<A>> extends Function<I, E, O, A> {

    public Output<I, E, O, A> evaluate(List<Output<I, E, O, A>> expectedOutputs, List<Output<I, E, O, A>> actualOutput);
}

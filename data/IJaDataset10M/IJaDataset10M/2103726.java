package jbeagle.core.select;

import jbeagle.core.Individual;
import jbeagle.core.Population;

public interface Selector {

    public <I extends Individual<G>, G> Population<I, G> apply(Population<I, G> pop);
}

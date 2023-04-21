package org.opt4j.operator.crossover;

import org.opt4j.genotype.PermutationGenotype;
import com.google.inject.ImplementedBy;

/**
 * Interface for the {@link Crossover} operator for the
 * {@link PermutationGenotype}.
 * 
 * @author lukasiewycz
 * 
 */
@ImplementedBy(CrossoverPermutationDefault.class)
public interface CrossoverPermutation extends Crossover<PermutationGenotype<?>> {
}

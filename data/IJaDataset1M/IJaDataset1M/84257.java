package jbeagle.core;

import java.util.Collection;

/**
 * An {@code Individual} (also called chromosome sometimes) is the central unit on which
 * the {@link GeneticAlgorithm} operates. It represents solutions to the problem at hand, and is
 * assigned a fitness score by the {@link FitnessFunction}. {@code Individual}s with a higher
 * fitness score have a greater chance of being selected by the {@link Selector} for breeding
 * a good solution to the problem.
 * <p>
 * Since {@code Individual}s are basically collections of genes, the {@code Individual}
 * interface extends the Java {@link Collection} interface. {@code Individual}s are further
 * comparable to other {@code Individual}s. This comparison should happen purely based on
 * fitness in order for {@code Individual}s to be sorted correctly and in the relevant
 * way for the genetic algorithm to work. For comparison purposes, each implementing class
 * should provide for a way to set the precision of the comparison in fitness values; for
 * example, a precision of 2 means that the comparison detects fitness differences of up
 * to 2 (rounded) decimal places. In this case, an {@code Individual} with fitness 0.95
 * is different (higher) from an {@code Individual} with fitness 0.90, but is considered
 * the same as an {@code Individual} with fitness 0.952. (See {@link AbstractIndividual#compareTo(Individual<?>)}.)
 * 
 * @author Matthijs Snel
 * @see GeneticAlgorithm
 * @see FitnessFunction
 * @see Selector
 * @see Collection
 * @see Comparable
 * @see AbstractIndividual
 * @since 0.1
 */
public interface Individual<G> extends Collection<G>, Comparable<Individual<?>> {

    /**
	 * The default precision used for comparison purposes.
	 */
    int DEFAULT_PRECISION = 3;

    /**
	 * Creates a copy of this {@code Individual}.
	 * 
	 * @return a copy of this {@code Individual}
	 */
    Individual<G> copy();

    /**
	 * Sets the fitness score of the {@code Individual} to the specified value.
	 * 
	 * @param fitness the desired fitness score
	 */
    void setFitness(double fitness);

    /**
	 * 
	 * @return the current fitness score of this {@code Individual}
	 */
    double getFitness();
}

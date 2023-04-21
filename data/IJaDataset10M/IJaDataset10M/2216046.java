package engine;

import java.util.List;

/**
 * Evaluator that computes values of objective functions for given population.
 * It is implemented as an operator so that it can be easily added at any step
 * in the algorithm.
 *
 * @author Marcin Brodziak (marcin.brodziak@gmail.com)
 *
 * @param <T> Type of individuals being evaluated.
 */
public abstract class PopulationEvaluator<T> implements Operator<T> {

    protected final List<CachedObjectiveFunction<T>> objectiveFunctions;

    /**
   * Creates population evaluator.
   * @param objectiveFunctions List of objective functions
   *    that will be evaluated.
   */
    protected PopulationEvaluator(List<CachedObjectiveFunction<T>> objectiveFunctions) {
        this.objectiveFunctions = objectiveFunctions;
    }

    /** {@inheritDoc} */
    public Population<T> apply(Population<T> populationInternal) {
        evaluatePopulation(populationInternal);
        return populationInternal;
    }

    /**
   * Returns list of objective functions to be evaluated.
   * @return List of objective functions to be evaluated.
   */
    public List<CachedObjectiveFunction<T>> getObjectiveFunctions() {
        return objectiveFunctions;
    }

    /**
   * Evaluates given population.
   * @param populationInternal Population to be evaluated.
   */
    public abstract void evaluatePopulation(Population<T> populationInternal);
}

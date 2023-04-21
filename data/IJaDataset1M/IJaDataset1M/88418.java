package jbeagle.core;

import java.util.ArrayList;
import java.util.List;
import jbeagle.core.ops.GeneticOperator;
import jbeagle.core.select.Selector;

/**
 * The core class implementing the genetic algorithm routine.
 * <p>
 * A genetic algorithm operates on a {@link Population} of {@link Indvidual}s that represent
 * solutions to the problem at hand. The "goodness" of individuals, i.e. their proximity
 * to the optimal solution to the problem, is indicated by a fitness score that is stored
 * on the {@code Individual}. The fitness score is assigned by a {@link FitnessFunction}.
 * A developer will most likely have to provide her own implementation of the {@code FitnessFunction}
 * interface as these evaluation functions are highly problem-dependent. However, a number of
 * example fitness functions have been provided in the {@link jbeagle.examples} package. The
 * {@link #setFitnessFunction(FitnessFunction)} method should be used to provide the fitness function.
 * <p>
 * The principle of a genetic algorithm is that it works according to the principles of
 * natural selection, i.e. good ("fit") individuals have a higher chance of reproducing than
 * less fit individuals. By repeatedly selecting and interbreeding fit individuals, the hope
 * is that eventually (a) satisfactory solution(s) to the problem will be reached.
 * <p>
 * Individuals are selected by a {@link Selector} for reproduction. A {@code Selector} takes
 * the current population, selects individuals according to their fitness score, and returns
 * an "intermediate" population that will be used for breeding. A number of common {@code Selector}
 * implementations are provided in the {@link jbeagle.core.select} package. The
 * {@link #setSelectionMethod(Selector)} method should be used to provide a {@code Selector}.
 * <p>
 * The "breeding" phase consists of applying a number of {@link GeneticOperator}s to the
 * intermediate population. The most common operators are the mutation (represented by {@link Mutator})
 * and crossover (represented by {@link CrossoverOperator}) operators. A number of common {@code Mutator}
 * and {@code CrossoverOperator} implementations are provided in the {@link jbeagle.core.ops} package.
 * Genetic operators can be added by using the {@link #addOperator(GeneticOperator)} method.
 * Operators will be applied in the order in which they have been added to the {@code GeneticAlgorithm}.
 * <p>
 * After breeding, the resulting population is evaluated for fitness and the whole process
 * repeats itself, until a {@link StopCondition} has been satisfied.
 * <p>
 * Since {@code GeneticAlgorithm} implements {@code Runnable}, it can easily be run in a separate
 * thread, which among others allows for pausing the GA and polling for intermediate results.
 * <p>
 * Results can be obtained by calling {@link #getResults()}, which returns a {@code List} of
 * arrays. Each array contains two {@code double} values; the first corresponds to the average
 * population fitness, the second to the fitness of the best individual. The list index
 * corresponds to the GA generation: results for the first generation are at index 0, for the
 * 100th generation at index 99, etc.
 * 
 * @author Matthijs Snel
 * @see jbeagle.core.Population
 * @see jbeagle.core.Individual
 * @see jbeagle.core.FitnessFunction
 * @see jbeagle.core.select.Selector
 * @see jbeagle.core.ops.GeneticOperator
 * @see jbeagle.core.ops.Mutator
 * @see jbeagle.core.ops.CrossoverOperator
 * @see jbeagle.core.GeneticAlgorithm.StopCondition
 * @see java.lang.Runnable
 * @since 0.1	
 */
public class GeneticAlgorithm<I extends Individual<G>, G> implements Runnable {

    protected FitnessFunction<I, G> fitnessf;

    protected Population<I, G> pop;

    protected ArrayList<GeneticOperator<I, G>> operators;

    protected Selector selector;

    protected StopCondition stopCriterion;

    protected ArrayList<double[]> results;

    private int currGeneration;

    public GeneticAlgorithm() {
        currGeneration = 0;
        operators = new ArrayList<GeneticOperator<I, G>>(2);
        results = new ArrayList<double[]>(200);
    }

    public final void run() {
        if (stopCriterion == null) throw new IllegalStateException("A StopCondition must be provided before running the GA.");
        fitnessf.evaluate(pop);
        pop.updateFitness();
        updateResults();
        while (!stopCriterion.satisfied()) {
            pop = selector.apply(pop);
            for (GeneticOperator<I, G> op : operators) pop = op.apply(pop);
            currGeneration++;
            fitnessf.evaluate(pop);
            pop.updateFitness();
            updateResults();
        }
    }

    public void addOperator(GeneticOperator<I, G> op) {
        operators.add(op);
    }

    public int getCurrentGeneration() {
        return currGeneration;
    }

    public List<double[]> getResults() {
        return results;
    }

    public void setFitnessFunction(FitnessFunction<I, G> f) {
        fitnessf = f;
    }

    public void setPopulation(Population<I, G> p) {
        pop = p;
    }

    public void setSelectionMethod(Selector method) {
        selector = method;
    }

    public void setStopCondition(StopCondition sc) {
        stopCriterion = sc;
    }

    protected void updateResults() {
        double result[] = new double[2];
        result[0] = pop.getAvgFitness();
        result[1] = pop.getFittest().getFitness();
        results.add(result);
    }

    public class StopCondition {

        /**
		 * Runs the GA until the best individual in the population equals or exceeds the fitness
		 * specified in the constructor.
		 */
        public static final int SOLUTION_FOUND = 1;

        /**
		 * Runs the GA until the average population fitness equals or exceeds the fitness
		 * specified in the constructor.
		 */
        public static final int FITNESS_REACHED = 2;

        /**
		 * Runs the GA until the the maximum number of generations specified in the constructor
		 * have been reached.
		 */
        public static final int MAX_GEN = 3;

        protected int type, maxGen;

        protected double fitness;

        /**
		 * Default constructor. Runs the GA until the best individual in the population
		 * has a fitness of 1.0, or until the GA has run for 2^31 - 1 generations. Use
		 * of this constructor is therefore not recommended.
		 */
        public StopCondition() {
            this(SOLUTION_FOUND, 1.0);
        }

        /**
		 * Runs the GA for the number of generations specified by maxGen.
		 * 
		 * @param maxGen the number of generations for which the GA should run.
		 */
        public StopCondition(int maxGen) {
            this(MAX_GEN, maxGen, 0);
        }

        /**
		 * 
		 * @param type
		 * @param fitness
		 */
        public StopCondition(int type, double fitness) {
            this(type, Integer.MAX_VALUE, fitness);
        }

        /**
		 * Recommended constructor. Allows to run the GA until the best or average
		 * fitness satisfy the fitness criterion, OR until a maximum number of
		 * generations have been reached.
		 * 
		 * @param type The stop condition type ({@code SOLUTION_FOUND} or
		 * {@code FITNESS_REACHED}, {@code MAX_GEN} doesn't make sense here)
		 * @param maxGen the maximum number of generations that the GA is allowed to run for
		 * @param fitness the fitness level the best or average fitness should reach
		 */
        public StopCondition(int type, int maxGen, double fitness) {
            this.type = type;
            this.maxGen = maxGen;
            this.fitness = fitness;
        }

        /**
		 * Checks whether the specified stopping condition(s) have been satisfied.
		 * 
		 * @return true if the conditions have been satisfied. This will cause the GA to stop.
		 */
        public boolean satisfied() {
            if (currGeneration >= maxGen) return true;
            if (type != MAX_GEN) {
                double toCheck = (type == SOLUTION_FOUND ? pop.getFittest().getFitness() : pop.getAvgFitness());
                return toCheck >= fitness;
            }
            return false;
        }

        /**
		 * 
		 * @return the fitness level that should be reached before the GA stops
		 */
        public double getFitness() {
            return fitness;
        }

        /**
		 * 
		 * @param fitness the fitness level that should be reached before the GA stops
		 */
        public void setFitness(double fitness) {
            this.fitness = fitness;
        }

        /**
		 * 
		 * @return the type of stop condition
		 */
        public int getType() {
            return type;
        }
    }
}

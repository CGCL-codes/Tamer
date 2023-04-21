package com.dhb.optimizing;

import java.util.Random;

public abstract class ChromosomeManager {

    /**
	 * Population size.
	 */
    private int populationSize = 100;

    /**
	 * Rate of mutation. 
	 */
    private double rateOfMutation = 0.1;

    /**
	 * Rate of crossover. 
	 */
    private double rateOfCrossover = 0.1;

    /**
	 * Random generator. 
	 */
    private Random generator = new Random();

    /**
 * ChromosomeManager constructor comment.
 */
    public ChromosomeManager() {
        super();
    }

    /**
 * This method was created in VisualAge.
 * @param n int
 * @param mRate double
 * @param cRate double
 */
    public ChromosomeManager(int n, double mRate, double cRate) {
        populationSize = n;
        rateOfMutation = mRate;
        rateOfCrossover = cRate;
    }

    /**
 * @param x java.lang.Object
 */
    public abstract void addCloneOf(Object x);

    /**
 * @param x java.lang.Object
 */
    public abstract void addCrossoversOf(Object x, Object y);

    /**
 * @param x java.lang.Object
 */
    public abstract void addMutationOf(Object x);

    /**
 * This method was created in VisualAge.
 */
    public abstract void addRandomChromosome();

    /**
 * @return int	the current size of the population
 */
    public abstract int getCurrentPopulationSize();

    /**
 * @return int	desired population size.
 */
    public int getPopulationSize() {
        return populationSize;
    }

    /**
 * This method was created in VisualAge.
 * @return java.lang.Object
 * @param n int
 */
    public abstract Object individualAt(int n);

    /**
 * This method was created in VisualAge.
 * @return boolean
 */
    public boolean isFullyPopulated() {
        return getCurrentPopulationSize() >= populationSize;
    }

    /**
 * This method was created in VisualAge.
 * @return double
 */
    public double nextDouble() {
        return generator.nextDouble();
    }

    /**
 * @param x java.lang.Object
 * @param y java.lang.Object
 */
    public void process(Object x, Object y) {
        double roll = generator.nextDouble();
        if (roll < rateOfCrossover) addCrossoversOf(x, y); else if (roll < rateOfCrossover + rateOfMutation) {
            addMutationOf(x);
            addMutationOf(y);
        } else {
            addCloneOf(x);
            addCloneOf(y);
        }
    }

    /**
 * Create a random population.
 */
    public void randomnizePopulation() {
        reset();
        while (!isFullyPopulated()) addRandomChromosome();
    }

    /**
 * Reset the population of the receiver.
 */
    public abstract void reset();

    /**
 * @param n int	desired population size.
 */
    public void setPopulationSize(int n) {
        populationSize = n;
    }

    /**
 * @param n int	desired rate of crossover
 */
    public void setRateOfCrossover(int cRate) {
        rateOfCrossover = cRate;
    }

    /**
 * @param n int	desired rate of mutation
 */
    public void setRateOfMutation(int mRate) {
        rateOfMutation = mRate;
    }
}

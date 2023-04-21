package org.rakiura.cirrus;

import java.lang.reflect.Constructor;
import java.util.Properties;

/**
 * Represents a generic Individual entity.
 * 
 * <br>
 * <br>
 * Created: Fri Jun 18 16:12:30 1999 <br>
 * 
 * @author Mariusz Nowostawski
 * @version @version@ $Revision: 1.10 $ $Date: 2006/06/13 00:25:45 $
 */
public class Individual implements Comparable<Individual>, Cloneable {

    double fitness;

    Genome genome;

    FitnessFunction fitness_function;

    boolean fitnessCorrect;

    public final Properties properties = new Properties();

    /**
	 * Creates Individual with particular genome.
	 * @param geno genome for this individual
	 */
    public Individual(Genome geno) {
        this.genome = geno;
        this.fitnessCorrect = false;
        this.fitness_function = null;
    }

    /**
	 * Creates Individual with particular genome and fitness function.
	 * @param geno genome for this individual
	 * @param fun fitness finction for this individual
	 */
    public Individual(Genome geno, FitnessFunction fun) {
        this(geno);
        this.fitness_function = fun;
    }

    /**
	 * Sets a fitness function for this individual.
	 * @param fun fitness function
	 */
    public void setFitnessFunction(FitnessFunction fun) {
        this.fitness_function = fun;
    }

    /**
	 * Retruns the handle to the fitness function.
	 * @return reference to the fitness function 
	 * handle this individual is using.
	 */
    public FitnessFunction getFitnessFunction() {
        return this.fitness_function;
    }

    /**
	 * Sets the mutation ratio for this individual.
	 * @param r mutation ratio
	 */
    public void setMutationRatio(double r) {
        this.genome.setMutationRatio(r);
    }

    /**
	 * Get the current fitness value. If the fitness is not recalculated an
	 * exception will be thrown. If you need to recalculate fitness value when
	 * needed, use fitness() method call instead.
	 * 
	 * @return current fitness value
	 * @exception IllegalAccessException
	 *                thrown when the current value of the individual's fitness
	 *                is not valid and should not be read.
	 */
    public double getFitness() throws IllegalAccessException {
        if (this.fitnessCorrect) return this.fitness;
        throw new IllegalAccessException("Trying to access uncorrect fitness value!");
    }

    /**
	 * Returns the current value of the fitness. If the fitness is invalidated
	 * this method will recalculate it.
	 * 
	 * @return current fitness value
	 */
    public double fitness() {
        if (!this.fitnessCorrect) {
            this.fitness = this.fitness_function.execute(this);
            this.fitnessCorrect = true;
        }
        return this.fitness;
    }

    /**
	 * Sets the current fitness to the given value, and sets that it is valid.
	 * Use this call with care. Should be used only in special cases for direct
	 * fitness manipulation.
	 * @param fit fitness value.
	 */
    public void setFitness(double fit) {
        this.fitness = fit;
        this.fitnessCorrect = true;
    }

    /**
	 * @return the genome of the individual.
	 */
    public Genome getGenome() {
        return this.genome;
    }

    /**
	 * Performs a crossover with the given individual using the given crossover
	 * method. The crossover method defines the probability.
	 * 
	 * @param ind individual to make crossover with
	 * @param cross crossover method to be used
	 */
    public void crossover(Individual ind, Crossover cross) {
        this.genome.crossover(ind.getGenome(), cross);
        this.fitnessCorrect = false;
    }

    /**
	 * Invalidates the fitness value.
	 */
    public void invalidateFitness() {
        this.fitnessCorrect = false;
    }

    /**
	 * Performes mutation on the genome.
	 */
    public void mutation() {
        this.genome.mutation();
        this.fitnessCorrect = false;
    }

    /**
	 * Randomizes the genome.
	 */
    public void randomize() {
        this.genome.randomize();
        this.fitnessCorrect = false;
    }

    /**
	 * Represents this individual in a human readable string.
	 * @return human readable representation of this individual.
	 */
    public String toString() {
        StringBuffer buf = new StringBuffer("Individual: ");
        buf.append(" fitness: ").append(this.fitness).append(" ");
        buf.append(this.genome.toString());
        return buf.toString();
    }

    /**
	 * Compares two individual's fintess values. Returns -1 if this individual
	 * fitness is greater than given parameter, -1 if smaller, and 0 if the
	 * fitness values of both individuals are the same. This method will throw a
	 * ClassCastException if a parameter is different than individual. Only
	 * Individuals can be compared with one another.
	 * 
	 * @param o individual object to compare with.
	 * @return -1 if this individual
	 * fitness is greater than given parameter, -1 if smaller, and 0 if the
	 * fitness values of both individuals are the same
	 */
    public int compareTo(Individual o) {
        if (fitness() > o.fitness()) return -1;
        if (fitness() < o.fitness()) return 1;
        return 0;
    }

    public Object clone() {
        try {
            Individual ind = (Individual) super.clone();
            ind.genome = (Genome) this.genome.clone();
            ind.fitness = this.fitness;
            ind.fitnessCorrect = this.fitnessCorrect;
            return ind;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
	 * Utility method for dynamically creating a given instance with constractor
	 * that takes single argument: Properties.
	 * 
	 * @param className class to be instantiated
	 * @param props Properties instance to be passed to the constructor
	 * @return new instance
	 */
    public static final Object createInstance(String className, Properties props) {
        if (className == null) throw new RuntimeException("Empty class name.");
        try {
            Class clazz = Class.forName(className);
            Constructor c = clazz.getConstructor(new Class[] { Properties.class });
            Object inst = c.newInstance(new Object[] { props });
            return inst;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

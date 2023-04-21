package Individuals.FitnessPackage;

import Individuals.*;

/**
 * Interface for Fitness. Contains methods for getting and setting the max and min
 * fitness measurements for Integer and Double types.
 * @author Blip
 */
public interface Fitness extends Comparable<Fitness> {

    /**
     * Get the individual that is refered to by the fitness
     * @return individual to which the fitness belongs
     */
    public Individual getIndividual();

    /**
     * Set individual to which the fitness belongs
     * @param i individual to which the fitness belongs
     */
    public void setIndividual(Individual i);

    public double getDouble();

    public void setDouble(double f);

    public int getInt();

    public void setInt(int f);

    public double getMaxDoubleFitness();

    public void setMaxDoubleFitness(double d);

    public double getMinDoubleFitness();

    public void setMinDoubleFitness(double d);

    public int getMaxIntFitness();

    public void setMaxIntFitness(int d);

    public int getMinIntFitness();

    public void setMinIntFitness(int d);

    /**
     * Set the default value of fitness. This can be given to
     * unevaluated individuals, such as newly created or invalids
     */
    public void setDefault();
}

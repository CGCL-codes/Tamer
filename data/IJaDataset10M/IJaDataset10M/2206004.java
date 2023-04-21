package org.rakiura.cirrus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;
import org.rakiura.cirrus.crossover.OnePoint;
import org.rakiura.cirrus.crossover.PairUniformMating;
import org.rakiura.cirrus.genome.StringChromosome;

/**
 * Population class. Represents the entire population in a single population
 * based algorithm or a subpopulation within the multi-deme genetic algorithm.
 * 
 * <br>
 * <br>
 * Created: Fri Jun 18 11:46:48 1999 <br>
 * 
 * @author Mariusz Nowostawski
 * @version @version@ $Revision: 1.7 $
 */
public class Population {

    public boolean resizable;

    private List<Individual> population;

    private double worst;

    private double best;

    private double avr;

    public Population() {
        this.resizable = true;
        this.population = new ArrayList<Individual>();
    }

    /**
	 * Creates the new population with the given size and Individual prototype.
	 * @param size size of the population.
	 * @param ind individual prototype.
	 */
    public Population(int size, Individual ind) {
        this(size, ind, true);
    }

    public Population(int size, Individual ind, boolean resizable) {
        this.population = new ArrayList<Individual>();
        for (int i = 0; i < size; i++) this.population.add((Individual) ind.clone());
        randomize();
        this.resizable = resizable;
    }

    public final synchronized void add(Individual ind) {
        if (this.resizable) this.population.add(ind); else throw new RuntimeException("Population non-resizable");
    }

    /**
	 * Remove the individual at the given index.
	 * 
	 * @param i index of the individual to be removed.
	 * @return removed individual object
	 */
    public final synchronized Individual remove(int i) {
        if (!this.resizable) new RuntimeException("Population non-resizable");
        final Individual ind = this.population.get(i);
        this.population.remove(i);
        return ind;
    }

    /**
	 * Removes the first element that match a particular Individual object.
	 * @param ind Individual object to be removed
	 */
    public synchronized void remove(Individual ind) {
        if (!this.resizable) new RuntimeException("Population non-resizable");
        this.population.remove(ind);
    }

    /**
	 * Sets the individual at the specified index to a particular one.
	 * @param index The index.
	 * @param ind The individual to be pleced at the given position.
	 */
    public final synchronized void replace(int index, Individual ind) {
        this.population.set(index, ind);
    }

    public final synchronized Individual getIndividual(int i) {
        return this.population.get(i);
    }

    public final int size() {
        return this.population.size();
    }

    public void setIndividualMutationRatio(double r) {
        final Iterator<Individual> i = individuals();
        Individual ind;
        while (i.hasNext()) {
            ind = i.next();
            ind.setMutationRatio(r);
        }
    }

    /**
	 * Sorts the population according to fintess values. DOES NOT work YET fully -
	 * evaluates only max/min/avr in the population for now.
	 */
    public final synchronized void sort() {
        ((ArrayList) this.population).trimToSize();
        int size = this.population.size();
        int j = 0;
        Individual[] arr = new Individual[size];
        final Iterator<Individual> i = individuals();
        Individual ind;
        double max = Double.MIN_VALUE;
        double min = Double.MAX_VALUE;
        double sum = 0, f, counter = 1;
        while (i.hasNext()) {
            ind = i.next();
            f = ind.fitness();
            if (f < min) min = f;
            if (f > max) max = f;
            sum += f;
            counter++;
            arr[j++] = ind;
        }
        this.worst = min;
        this.best = max;
        this.avr = sum / counter;
        Collections.sort(this.population);
    }

    public final void fitness() {
        final Iterator<Individual> i = individuals();
        Individual ind;
        double max = Double.MIN_VALUE, min = Double.MAX_VALUE, sum = 0, f, counter = 1;
        if (i.hasNext()) {
            ind = i.next();
            f = ind.fitness();
            max = min = sum = f;
        }
        while (i.hasNext()) {
            ind = i.next();
            f = ind.fitness();
            if (f < min) min = f;
            if (f > max) max = f;
            sum += f;
            counter++;
        }
        this.worst = min;
        this.best = max;
        this.avr = sum / counter;
    }

    /**
	 * @return the sum of all individuals fitness values.
	 */
    public final double fitnessSum() {
        final Iterator<Individual> i = individuals();
        Individual ind;
        double max = Double.MIN_VALUE, min = Double.MAX_VALUE, sum = 0, f, counter = 1;
        try {
            if (i.hasNext()) {
                ind = i.next();
                f = ind.getFitness();
                max = min = sum = f;
            }
            while (i.hasNext()) {
                ind = (Individual) i.next();
                f = ind.getFitness();
                if (f < min) min = f;
                if (f > max) max = f;
                sum += f;
                counter++;
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        this.worst = min;
        this.best = max;
        this.avr = (sum / counter);
        return sum;
    }

    /**
	 * Performs a mutation on each individual.
	 */
    public final void mutation() {
        for (int i = 0; i < this.population.size(); i++) this.population.get(i).mutation();
    }

    /**
	 * Randomizes all individuals' genomes.
	 */
    public final void randomize() {
        final Iterator<Individual> i = individuals();
        while (i.hasNext()) {
            i.next().randomize();
        }
    }

    /**
	 * Shuffles the order of individuals in this population.
	 */
    public final void shuffle() {
        Collections.shuffle(this.population, new Random(System.currentTimeMillis()));
    }

    public double getWorstFitness() {
        return this.worst;
    }

    public double getBestFitness() {
        return this.best;
    }

    public double getAvrFitness() {
        return this.avr;
    }

    public Iterator<Individual> individuals() {
        return this.population.listIterator();
    }

    /**
	 * Returns array of individuals of this population.
	 * @return array with idividuals of this population.
	 */
    public Object[] toArray() {
        return this.population.toArray(new Individual[this.population.size()]);
    }

    /**
	 * Returns textual representation of this population.
	 * @return string representation of this population.
	 */
    public String toString() {
        StringBuffer buf = new StringBuffer();
        final Iterator<Individual> i = individuals();
        while (i.hasNext()) buf.append(i.next().toString()).append("\n");
        return buf.toString();
    }

    /** 
	 * TEST 
	 * @param argv arguments. 
	 **/
    public static void main(String[] argv) {
        Set<String> a = new HashSet<String>();
        for (int i = 0; i < 10; i++) {
            String s = String.valueOf(i);
            a.add(s);
        }
        StringChromosome chr = new StringChromosome(a, 5);
        Population pop = new Population(10, new Individual(new Genome(chr)));
        System.out.println("Got native    start: \n" + pop);
        pop.randomize();
        System.out.println("Got random  start: \n" + pop);
        pop.shuffle();
        System.out.println("Got after shufle start: \n" + pop);
        PairUniformMating mate = new PairUniformMating(new OnePoint(0.6));
        Population pop2 = mate.execute(pop);
        System.out.println("Got after X: " + pop2);
    }
}

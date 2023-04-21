package org.vizzini.ai.geneticalgorithm;

import java.util.Arrays;
import java.util.Random;

/**
 * Provides base functionality for a collection which holds <code>
 * IChromosome</code> s.
 *
 * @author   Jeffrey M. Thompson
 * @version  v0.4
 * @since    v0.3
 */
public abstract class AbstractPopulation implements IPopulation {

    /** Average fitness. */
    private double _averageFitness;

    /** Chromosomes. */
    private IChromosome[] _chromosomes;

    /** Current size of the population. */
    private int _currentSize;

    /** Maximum fitness in the population. */
    private int _maxFitness;

    /** Minimum fitness in the population. */
    private int _minFitness;

    /** Sum of the chromosome's fitnesses. */
    private int _sumFitness;

    /**
     * Construct this object using the given parameter.
     *
     * @param  capacity  Capacity of this population.
     *
     * @since  v0.3
     */
    public AbstractPopulation(int capacity) {
        if (capacity <= 0) {
            throw new IllegalArgumentException("capacity <= 0");
        }
        _chromosomes = new IChromosome[capacity];
        _currentSize = 0;
        resetAverageFitness();
    }

    /**
     * @see  org.vizzini.ai.geneticalgorithm.IPopulation#add(org.vizzini.ai.geneticalgorithm.IChromosome)
     */
    public void add(IChromosome chromosome) {
        if (chromosome == null) {
            throw new IllegalArgumentException("chromosome == null");
        }
        if (_currentSize >= _chromosomes.length) {
            throw new IllegalStateException("population is full");
        }
        _chromosomes[_currentSize] = chromosome;
        _currentSize++;
    }

    /**
     * @see  org.vizzini.ai.geneticalgorithm.IPopulation#addAll(org.vizzini.ai.geneticalgorithm.IPopulation)
     */
    public void addAll(IPopulation population) {
        int size = population.size();
        for (int i = 0; i < size; i++) {
            IChromosome chromosome = population.get(i);
            add(chromosome);
        }
    }

    /**
     * @see  org.vizzini.ai.geneticalgorithm.IPopulation#capacity()
     */
    public int capacity() {
        return _chromosomes.length;
    }

    /**
     * @see  org.vizzini.ai.geneticalgorithm.IPopulation#clearFitnesses()
     */
    public void clearFitnesses() {
        for (int i = 0; i < _chromosomes.length; i++) {
            _chromosomes[i].setFitnessUnknown();
        }
    }

    /**
     * @see  org.vizzini.ai.geneticalgorithm.IPopulation#contains(org.vizzini.ai.geneticalgorithm.IChromosome)
     */
    public boolean contains(IChromosome chromosome) {
        return contains(chromosome, 0, _currentSize);
    }

    /**
     * @see  org.vizzini.ai.geneticalgorithm.IPopulation#contains(org.vizzini.ai.geneticalgorithm.IChromosome,
     *       int, int)
     */
    public boolean contains(IChromosome chromosome, int start, int end) {
        boolean answer = false;
        for (int i = start; !answer && (i < end); i++) {
            answer = (_chromosomes[i].equals(chromosome));
        }
        return answer;
    }

    /**
     * @see  org.vizzini.ai.geneticalgorithm.IPopulation#first()
     */
    public IChromosome first() {
        return _chromosomes[0];
    }

    /**
     * @see  org.vizzini.ai.geneticalgorithm.IPopulation#get(int)
     */
    public IChromosome get(int i) {
        return _chromosomes[i];
    }

    /**
     * @see  org.vizzini.ai.geneticalgorithm.IPopulation#getAverageFitness()
     */
    public double getAverageFitness() {
        if (_averageFitness == IChromosome.UNKNOWN_FITNESS) {
            _averageFitness = (double) getSumFitness() / _chromosomes.length;
        }
        return _averageFitness;
    }

    /**
     * @see  org.vizzini.ai.geneticalgorithm.IPopulation#getMaxFitness()
     */
    public int getMaxFitness() {
        getSumFitness();
        return _maxFitness;
    }

    /**
     * @see  org.vizzini.ai.geneticalgorithm.IPopulation#getMinFitness()
     */
    public int getMinFitness() {
        getSumFitness();
        return _minFitness;
    }

    /**
     * @see  org.vizzini.ai.geneticalgorithm.IPopulation#getSumFitness()
     */
    public int getSumFitness() {
        if (_sumFitness == IChromosome.UNKNOWN_FITNESS) {
            _sumFitness = 0;
            _minFitness = Integer.MAX_VALUE;
            _maxFitness = Integer.MIN_VALUE;
            for (int i = 0; i < _chromosomes.length; i++) {
                int fitness = _chromosomes[i].getFitness();
                if (_sumFitness < (Integer.MAX_VALUE - fitness)) {
                    _sumFitness += fitness;
                }
                if (fitness < _minFitness) {
                    _minFitness = fitness;
                }
                if (fitness > _maxFitness) {
                    _maxFitness = fitness;
                }
            }
        }
        return _sumFitness;
    }

    /**
     * @see  org.vizzini.ai.geneticalgorithm.IPopulation#indexOf(org.vizzini.ai.geneticalgorithm.IChromosome)
     */
    public int indexOf(IChromosome chromosome) {
        int answer = -1;
        for (int i = 0; (answer < 0) && (i < _currentSize); i++) {
            if (_chromosomes[i].equals(chromosome)) {
                answer = i;
            }
        }
        return answer;
    }

    /**
     * @see  org.vizzini.ai.geneticalgorithm.IPopulation#isEmpty()
     */
    public boolean isEmpty() {
        return _currentSize == 0;
    }

    /**
     * @see  org.vizzini.ai.geneticalgorithm.IPopulation#resetAverageFitness()
     */
    public void resetAverageFitness() {
        _sumFitness = IChromosome.UNKNOWN_FITNESS;
        _averageFitness = IChromosome.UNKNOWN_FITNESS;
    }

    /**
     * @see  org.vizzini.ai.geneticalgorithm.IPopulation#setFitnesses(int)
     */
    public void setFitnesses(int fitness) {
        for (int i = 0; i < _chromosomes.length; i++) {
            _chromosomes[i].setFitness(fitness);
        }
    }

    /**
     * @see  org.vizzini.ai.geneticalgorithm.IPopulation#shuffle()
     */
    public void shuffle() {
        int size = _chromosomes.length;
        Random rnd = new Random();
        for (int i = size; i > 1; i--) {
            int r0 = i - 1;
            int r1 = rnd.nextInt(i);
            IChromosome temp = _chromosomes[r0];
            _chromosomes[r0] = _chromosomes[r1];
            _chromosomes[r1] = temp;
        }
    }

    /**
     * @see  org.vizzini.ai.geneticalgorithm.IPopulation#size()
     */
    public int size() {
        return _currentSize;
    }

    /**
     * @see  org.vizzini.ai.geneticalgorithm.IPopulation#sort()
     */
    public void sort() {
        Arrays.sort(_chromosomes);
    }

    /**
     * @see  java.lang.Object#toString()
     */
    @Override
    public String toString() {
        String lineSeparator = System.getProperty("line.separator");
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < _chromosomes.length; i++) {
            sb.append(i).append(": ");
            sb.append(_chromosomes[i].toString());
            if (i < (_chromosomes.length - 1)) {
                sb.append(lineSeparator);
            }
        }
        return sb.toString();
    }
}

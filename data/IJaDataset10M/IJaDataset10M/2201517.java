package org.saiko.ai.genetics.tsp.engines.jgapCrossover;

import java.util.Random;
import org.jgap.Chromosome;
import org.jgap.Configuration;
import org.jgap.DefaultFitnessEvaluator;
import org.jgap.FitnessFunction;
import org.jgap.Gene;
import org.jgap.Genotype;
import org.jgap.InvalidConfigurationException;
import org.jgap.Population;
import org.jgap.RandomGenerator;
import org.jgap.event.EventManager;
import org.jgap.impl.BestChromosomesSelector;
import org.jgap.impl.ChromosomePool;
import org.jgap.impl.GreedyCrossover;
import org.jgap.impl.IntegerGene;
import org.jgap.impl.StockRandomGenerator;
import org.jgap.impl.SwappingMutationOperator;
import org.saiko.ai.genetics.tsp.City;
import org.saiko.ai.genetics.tsp.TSPChromosome;
import org.saiko.ai.genetics.tsp.TSPConfiguration;
import org.saiko.ai.genetics.tsp.TSPEngine;
import org.saiko.ai.genetics.tsp.engines.simpleUnisexMutator.SimpleUnisexMutatorEngine;

/**
 * @author Dusan Saiko (dusan@saiko.cz)
 * Last change $Date: 2005/08/24 12:33:13 $
 * 
 * Implements solving the Traveling Salesman Problem using JGap libraries
 * and connecting this to TSPEngine interface so it can be used in the TSP gui
 * 
 * For the population modification, GreeadyCrossover algorithm is used
 * 
 * @see org.saiko.ai.genetics.tsp.engines.simpleUnisexMutatorHibrid2Opt.SimpleUnisexMutatorHibrid2OptEngine
 * @see org.jgap.impl.GreedyCrossover
 * @see org.saiko.ai.genetics.tsp.TSPEngine
 * @see org.jgap.impl.salesman.Salesman
 * @see examples.salesman.TravellingSalesman
 */
public class JGapGreedyCrossoverEngine implements TSPEngine {

    /** String containing the CVS revision. **/
    public static final String CVS_REVISION = "$Revision: 1.4 $";

    /**
    * Population of all chromosomes
    */
    protected Genotype population = null;

    /**
    * Reference to cities for converting JGap Gene/Chromosome to City/City[]
    */
    protected City originalCities[] = null;

    /**
    * Application configuration parameters.
    * @see TSPConfiguration 
    */
    protected TSPConfiguration configuration;

    /**
    * Initializes the engine for population size and set of cities
    * @see TSPEngine
    */
    public void initialize(TSPConfiguration appConfiguration, City[] cities) {
        try {
            this.originalCities = cities;
            this.configuration = appConfiguration;
            Genotype.setConfiguration(createConfiguration());
            Genotype.getConfiguration().setFitnessFunction(createFitnessFunction());
            Genotype.getConfiguration().setSampleChromosome(new Chromosome(cities2gene(cities)));
            Genotype.getConfiguration().setPopulationSize(configuration.getInitialPopulationSize());
            Chromosome chromosomes[] = new Chromosome[Genotype.getConfiguration().getPopulationSize()];
            for (int i = 0; i < chromosomes.length; i++) {
                City[] c = new City[cities.length];
                for (int k = 0; k < c.length; k++) {
                    c[k] = cities[k];
                }
                randomize(c);
                chromosomes[i] = new Chromosome(cities2gene(c));
            }
            population = new Genotype(Genotype.getConfiguration(), new Population(chromosomes));
        } catch (Throwable e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    /**
    * @param from  
    * @param to 
    * @return cost of traveling between two cities
    * @see City#cost(City)
    * @see City#distance(City)
    */
    public double cost(Gene from, Gene to) {
        IntegerGene g1 = (IntegerGene) from;
        IntegerGene g2 = (IntegerGene) to;
        City a = getCityById(g1.intValue());
        City b = getCityById(g2.intValue());
        return a.cost(b);
    }

    /**
    * Creates fitness function which evaluates the chromosome.
    * @see org.jgap.impl.salesman.Salesman
    * @see examples.salesman.TravellingSalesman
    * @return FitnessFunction
    */
    public FitnessFunction createFitnessFunction() {
        return new FitnessFunction() {

            private static final long serialVersionUID = -4530787614587664634L;

            @Override
            protected double evaluate(Chromosome a_subject) {
                double s = 0;
                Gene[] genes = a_subject.getGenes();
                for (int i = 0; i < genes.length - 1; i++) {
                    s += cost(genes[i], genes[i + 1]);
                }
                s += cost(genes[genes.length - 1], genes[0]);
                return Long.MAX_VALUE - s;
            }
        };
    }

    /**
    * @return JGap configuration
    * @see org.jgap.impl.salesman.Salesman
    * @see examples.salesman.TravellingSalesman
    */
    public Configuration createConfiguration() {
        try {
            Configuration config = new Configuration();
            BestChromosomesSelector bestChromsSelector = new BestChromosomesSelector(1.0d);
            bestChromsSelector.setDoubletteChromosomesAllowed(false);
            config.addNaturalSelector(bestChromsSelector, true);
            config.setRandomGenerator(new StockRandomGenerator());
            config.setMinimumPopSizePercent(0);
            config.setEventManager(new EventManager());
            config.setFitnessEvaluator(new DefaultFitnessEvaluator());
            config.setChromosomePool(new ChromosomePool());
            config.setKeepPopulationSizeConstant(false);
            GreedyCrossover o1 = new GreedyCrossover();
            o1.setStartOffset(0);
            config.addGeneticOperator(o1);
            SwappingMutationOperator o2 = new SwappingMutationOperator((int) (1 / configuration.getMutationRatio()));
            o2.setStartOffset(0);
            config.addGeneticOperator(o2);
            return config;
        } catch (InvalidConfigurationException e) {
            throw new RuntimeException(e);
        }
    }

    /**
    * Converts array of cities into array of integer genes
    * IntegerGene is JGap gene holding the city id
    * @param cities
    * @return array of IntegerGene
    * @see City#getId()
    */
    protected IntegerGene[] cities2gene(City cities[]) {
        IntegerGene[] gene = new IntegerGene[cities.length];
        for (int i = 0; i < cities.length; i++) {
            gene[i] = new IntegerGene();
            gene[i].setAllele(cities[i].getId());
        }
        return gene;
    }

    /**
    * Converts array of integer genes into array of cities
    * IntegerGene is JGap gene holding the city id
    * @param genes
    * @return array of IntegerGene
    * @see City#getId()
    */
    protected City[] genes2cities(Gene genes[]) {
        City[] c = new City[genes.length];
        for (int i = 0; i < originalCities.length; i++) {
            c[i] = getCityById((Integer) genes[i].getAllele());
        }
        return c;
    }

    /**
    * @param id
    * @return city from id
    */
    protected City getCityById(int id) {
        return originalCities[id];
    }

    /**
    * @see TSPEngine
    */
    public int getPopulationSize() {
        return population.getPopulation().size();
    }

    /**
    * @see TSPEngine
    */
    public TSPChromosome getBestChromosome() {
        return new TSPChromosome(genes2cities(population.getFittestChromosome().getGenes()));
    }

    /**
    * @see TSPEngine
    */
    public void nextGeneration() {
        population.evolve();
    }

    /**
    * Randomizes the array of cities
    * @param cities 
    */
    void randomize(City[] cities) {
        RandomGenerator rnd = Genotype.getConfiguration().getRandomGenerator();
        SimpleUnisexMutatorEngine.randomize((Random) rnd, cities);
    }
}

package gelations;

import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;
import java.util.Random;

/**
 * @author conrada
 *
 */
public class PopulationTest {

    CaseTest ct1, ct2, ct3;

    Chromosome chrom1, chrom2, chrom3;

    Individual indi1, indi2, indi3, indi4;

    ArrayList<Chromosome> chroms1, chroms2, chroms3;

    ArrayList<Individual> individuals;

    Population population;

    double ct1Time, ct2Time, ct3Time;

    int ct1Id, ct2Id, ct3Id;

    int[] ct1Reqs, ct2Reqs, ct3Reqs;

    double indi1Fitness, indi2Fitness, indi3Fitness;

    /**
	 * @throws java.lang.Exception
	 */
    @Before
    public void setUp() throws Exception {
        ct1Time = 1.2345;
        ct2Time = 9.8765;
        ct3Time = 3.1415;
        ct1Id = 0;
        ct2Id = 1;
        ct3Id = 2;
        ct1Reqs = new int[] { 1, 2, 3 };
        ct2Reqs = new int[] { 4, 5, 6 };
        ct3Reqs = new int[] { 1, 3, 5, 7 };
        indi1Fitness = 0.9876;
        indi2Fitness = 0.5;
        indi3Fitness = 0.4321;
        ct1 = new CaseTest(ct1Time, ct1Reqs, ct1Id);
        ct2 = new CaseTest(ct2Time, ct2Reqs, ct2Id);
        ct3 = new CaseTest(ct3Time, ct3Reqs, ct3Id);
        chrom1 = new Chromosome(ct1);
        chrom2 = new Chromosome(ct2);
        chrom3 = new Chromosome(ct3);
        chroms1 = new ArrayList<Chromosome>();
        chroms1.add(chrom1);
        chroms1.add(chrom2);
        chroms1.add(chrom3);
        indi1 = new Individual(chroms1, indi1Fitness);
        chroms2 = new ArrayList<Chromosome>();
        chroms2.add(chrom3);
        chroms2.add(chrom2);
        chroms2.add(chrom1);
        indi2 = new Individual(chroms2, indi2Fitness);
        chroms3 = new ArrayList<Chromosome>();
        chroms3.add(chrom2);
        chroms3.add(chrom1);
        indi3 = new Individual(chroms3, indi3Fitness);
        individuals = new ArrayList<Individual>();
        individuals.add(indi1);
        individuals.add(indi2);
        individuals.add(indi3);
        population = new Population(individuals);
    }

    /**
	 * @throws java.lang.Exception
	 */
    @After
    public void tearDown() throws Exception {
    }

    /**
	 * Test method for {@link gelations.Population#getIndividuals()}.
	 */
    @Test
    public void testGetIndividuals() {
        ArrayList<Individual> tempIndividuals = population.getIndividuals();
        for (int i = 0; i < tempIndividuals.size(); i++) {
            assertEquals(individuals.get(i), population.getIndividuals().get(i));
        }
    }

    /**
	 * Test method for {@link gelations.Population#addIndividual(gelations.Individual)}.
	 */
    @Test
    public void testAddIndividual() {
        Individual individual = new Individual();
        population.addIndividual(individual);
        assertEquals(population.getIndividuals().get(population.getIndividuals().size() - 1), individual);
    }

    /**
	 * Test method for {@link gelations.Population#getMaxFitness()}.
	 */
    @Test
    public void testGetMaxFitness() {
        double maxFitness = indi1Fitness;
        assertEquals(maxFitness, population.getMaxFitness(), 0.001);
    }

    /**
	 * Test method for {@link gelations.Population#getMinFitness()}.
	 */
    @Test
    public void testGetMinFitness() {
        double minFitness = indi3Fitness;
        assertEquals(minFitness, population.getMinFitness(), 0.001);
    }

    /**
	 * Test method for {@link gelations.Population#getPopSize()}.
	 */
    @Test
    public void testGetPopSize() {
        assertEquals(3, population.getPopSize());
        population.addIndividual(new Individual());
        assertEquals(4, population.getPopSize());
        population.removeIndividual(0);
        population.removeIndividual(0);
        assertEquals(2, population.getPopSize());
    }

    /**
	 * Test method for {@link gelations.Population#getMaxPopSize()}.
	 */
    @Test
    public void testGetMaxPopSize() {
        assertEquals(3, population.getMaxPopSize());
        population.removeIndividual(1);
        assertEquals(3, population.getMaxPopSize());
        population.addIndividual(new Individual());
        assertEquals(3, population.getMaxPopSize());
    }

    /**
	 * Test method for {@link gelations.Population#removeIndividual(int)}.
	 */
    @Test
    public void testRemoveIndividual() {
        assertEquals(individuals.remove(2), population.removeIndividual(2));
        assertEquals(individuals.remove(0), population.removeIndividual(0));
        assertEquals(individuals.remove(0), population.removeIndividual(0));
    }

    /**
	 * Test method for {@link gelations.Population#getBestIndividual()}.
	 */
    @Test
    public void testGetBestIndividual() {
        assertEquals(indi1, population.getBestIndividual());
    }

    /**
	 * Test method for {@link gelations.Population#copy(gelations.Population)}.
	 */
    @Test
    public void testCopy() {
        assertNotSame(population, Population.copy(population));
    }

    /**
	 * Test method for {@link gelations.Population#randomlyReducePopulation(gelations.Population, int, java.util.Random)}.
	 */
    @Test
    public void testRandomlyReducePopulation() {
        Random rng = new Random();
        Population pop3 = Population.randomlyReducePopulation(population, 3, rng);
        Population pop2 = Population.randomlyReducePopulation(population, 2, rng);
        Population pop1 = Population.randomlyReducePopulation(population, 1, rng);
        Population pop4 = Population.randomlyReducePopulation(population, 4, rng);
        assertEquals(3, pop3.getPopSize());
        assertEquals(3, pop4.getPopSize());
        assertEquals(2, pop2.getPopSize());
        assertEquals(1, pop1.getPopSize());
    }

    /**
	 * Test method for {@link gelations.Population#randomlyCombinePopulations(gelations.Population, gelations.Population, java.util.Random)}.
	 */
    @Test
    public void testRandomlyCombinePopulations() {
        Random rng = new Random();
        Population pop2 = Population.copy(population);
        Population popComb = Population.randomlyCombinePopulations(population, pop2, rng);
        assertEquals(6, popComb.getPopSize());
    }
}

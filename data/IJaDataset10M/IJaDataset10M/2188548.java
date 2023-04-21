package org.vizzini.ai.geneticalgorithm;

import junit.framework.TestCase;
import junit.textui.TestRunner;

/**
 * Provides unit tests for the <code>AbstractPopulation</code> class.
 *
 * @author   Jeffrey M. Thompson
 * @version  v0.4
 * @since    v0.1
 */
public class AbstractPopulationTest extends TestCase {

    /** First size. */
    private static final int SIZE0 = 3;

    /** Chromosome size. */
    private static final int CHROMOSOME_SIZE = 4;

    /** First chromosome. */
    private static final IChromosome CHROMOSOME0 = new ChromosomeDouble(CHROMOSOME_SIZE);

    /** Second chromosome. */
    private static final IChromosome CHROMOSOME1 = new ChromosomeDouble(CHROMOSOME_SIZE);

    /** Third chromosome. */
    private static final IChromosome CHROMOSOME2 = new ChromosomeDouble(CHROMOSOME_SIZE);

    static {
        CHROMOSOME0.setDescription("0");
        ((ChromosomeDouble) CHROMOSOME0).set(0, 0.0);
        ((ChromosomeDouble) CHROMOSOME0).set(1, 1.0);
        ((ChromosomeDouble) CHROMOSOME0).set(2, 2.0);
        CHROMOSOME1.setDescription("1");
        ((ChromosomeDouble) CHROMOSOME1).set(0, 1.0);
        ((ChromosomeDouble) CHROMOSOME1).set(1, 2.0);
        ((ChromosomeDouble) CHROMOSOME1).set(2, 3.0);
        CHROMOSOME2.setDescription("2");
        ((ChromosomeDouble) CHROMOSOME2).set(0, 2.0);
        ((ChromosomeDouble) CHROMOSOME2).set(1, 3.0);
        ((ChromosomeDouble) CHROMOSOME2).set(2, 4.0);
    }

    /** First population. */
    private IPopulation _population0;

    /**
     * Application method.
     *
     * @param  args  Application arguments.
     *
     * @since  v0.1
     */
    public static void main(String[] args) {
        TestRunner.run(AbstractPopulationTest.class);
    }

    /**
     * Test the <code>add()</code> method.
     *
     * @since  v0.1
     */
    public void testAdd() {
        IPopulation population = new DefaultPopulation(1);
        assertEquals(0, population.size());
        population.add(CHROMOSOME0);
        assertEquals(1, population.size());
        Exception exception = null;
        try {
            population.add(CHROMOSOME1);
        } catch (IllegalStateException e) {
            exception = e;
        }
        assertNotNull(exception);
    }

    /**
     * Test the <code>addAll()</code> method.
     *
     * @since  v0.3
     */
    public void testAddAll() {
        int size = _population0.size();
        IPopulation population = new DefaultPopulation(size);
        assertTrue(population.isEmpty());
        population.addAll(_population0);
        assertFalse(population.isEmpty());
        _population0.sort();
        population.sort();
        for (int i = 0; i < size; i++) {
            assertEquals(_population0.get(i), population.get(i));
        }
    }

    /**
     * Test the <code>capacity()</code> method.
     *
     * @since  v0.1
     */
    public void testCapacity() {
        assertEquals(SIZE0, _population0.capacity());
    }

    /**
     * Test the <code>clearFitnesses()</code> method.
     *
     * @since  v0.1
     */
    public void testClearFitnesses() {
        CHROMOSOME0.setFitness(12);
        _population0.clearFitnesses();
        int size = _population0.size();
        for (int i = 0; i < size; i++) {
            assertTrue(_population0.get(i).isFitnessUnknown());
        }
    }

    /**
     * Test the constructor.
     *
     * @since  v0.1
     */
    public void testConstructor() {
        Exception exception = null;
        try {
            new DefaultPopulation(12);
        } catch (IllegalArgumentException e) {
            exception = e;
        }
        assertNull(exception);
        exception = null;
        try {
            new DefaultPopulation(-1);
        } catch (IllegalArgumentException e) {
            exception = e;
        }
        assertNotNull(exception);
    }

    /**
     * Test the <code>contains()</code> method.
     *
     * @since  v0.2
     */
    public void testContains() {
        assertTrue(_population0.contains(CHROMOSOME0));
        assertTrue(_population0.contains(CHROMOSOME1));
        assertTrue(_population0.contains(CHROMOSOME2));
        ChromosomeDouble chromosome = new ChromosomeDouble(CHROMOSOME_SIZE);
        chromosome.setDescription("blah");
        chromosome.set(0, 11.0);
        chromosome.set(1, 12.0);
        chromosome.set(2, 13.0);
        assertFalse(_population0.contains(chromosome));
    }

    /**
     * Test the <code>first()</code> method.
     *
     * @since  v0.1
     */
    public void testFirst() {
        assertEquals(CHROMOSOME0, _population0.first());
        _population0.sort();
        assertEquals(CHROMOSOME1, _population0.first());
    }

    /**
     * Test the <code>get()</code> method.
     *
     * @since  v0.1
     */
    public void testGet() {
        assertEquals(CHROMOSOME0, _population0.get(0));
        assertEquals(CHROMOSOME1, _population0.get(1));
        assertEquals(CHROMOSOME2, _population0.get(2));
    }

    /**
     * Test the <code>getAverageFitness()</code> method.
     *
     * @since  v0.1
     */
    public void testGetAverageFitness() {
        assertEquals(20.0, _population0.getAverageFitness(), 0.0);
    }

    /**
     * Test the <code>isEmpty()</code> method.
     *
     * @since  v0.1
     */
    public void testIsEmpty() {
        assertFalse(_population0.isEmpty());
        IPopulation population = new DefaultPopulation(12);
        assertTrue(population.isEmpty());
    }

    /**
     * Test the <code>clearFitnesses()</code> method.
     *
     * @since  v0.1
     */
    public void testSetFitnesses() {
        int value = 42;
        _population0.setFitnesses(value);
        int size = _population0.size();
        for (int i = 0; i < size; i++) {
            assertEquals(value, _population0.get(i).getFitness());
        }
    }

    /**
     * Test the <code>shuffle()</code> method.
     *
     * @since  v0.1
     */
    public void testShuffle() {
        System.out.println("before shuffle:\n" + _population0);
        _population0.shuffle();
        System.out.println("after shuffle:\n" + _population0);
    }

    /**
     * Test the <code>size()</code> method.
     *
     * @since  v0.1
     */
    public void testSize() {
        assertEquals(SIZE0, _population0.size());
        IPopulation population = new DefaultPopulation(12);
        assertEquals(0, population.size());
        population.add(CHROMOSOME0);
        assertEquals(1, population.size());
    }

    /**
     * Test the <code>sort()</code> method.
     *
     * @since  v0.1
     */
    public void testSort() {
        _population0.sort();
        assertEquals(CHROMOSOME1, _population0.get(0));
        assertEquals(CHROMOSOME2, _population0.get(1));
        assertEquals(CHROMOSOME0, _population0.get(2));
    }

    /**
     * Test the <code>toString()</code> method.
     *
     * @since  v0.1
     */
    public void testToString() {
        String lineSeparator = System.getProperty("line.separator");
        String expected = "0: ChromosomeDouble [_fitness=10,_description=0] [0.0,1.0,2.0,0.0]" + lineSeparator + "1: ChromosomeDouble [_fitness=30,_description=1] [1.0,2.0,3.0,0.0]" + lineSeparator + "2: ChromosomeDouble [_fitness=20,_description=2] [2.0,3.0,4.0,0.0]";
        String result = _population0.toString();
        assertEquals(expected, result);
    }

    /**
     * @see  junit.framework.TestCase#setUp()
     */
    @Override
    protected void setUp() {
        _population0 = new DefaultPopulation(SIZE0);
        _population0.add(CHROMOSOME0);
        _population0.add(CHROMOSOME1);
        _population0.add(CHROMOSOME2);
        CHROMOSOME0.setFitness(10);
        CHROMOSOME1.setFitness(30);
        CHROMOSOME2.setFitness(20);
    }

    /**
     * @see  junit.framework.TestCase#tearDown()
     */
    @Override
    protected void tearDown() {
        _population0 = null;
    }
}

package net.sf.filosof.test;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;
import java.util.TreeSet;
import junit.framework.TestCase;
import net.sf.filosof.framework.problem.fitness.BigIntegerFitnessAspectFactory;
import net.sf.filosof.framework.problem.fitness.Fitness;
import net.sf.filosof.framework.problem.fitness.FitnessAspect;
import net.sf.filosof.framework.problem.fitness.FitnessAspectComposer;
import net.sf.filosof.framework.problem.fitness.LexicographicalOrderingAspectComposer;
import net.sf.filosof.framework.problem.fitness.LongFitnessAspectFactory;
import net.sf.filosof.framework.problem.fitness.WeightedArithmeticAverageAspectComposer;
import net.sf.filosof.framework.problem.fitness.WeightedGeometricAverageAspectComposer;
import net.sf.filosof.library.problem.knapsack.SimpleKnapsack;

/**
 * Tests {@link Fitness} Implementations.
 * 
 * @author Wisser
 */
public class FitnessTest extends TestCase {

    /**
     * Tests {@link Fitness} Implementations.
     */
    public void testFitnesses() throws Exception {
        BigIntegerFitnessAspectFactory f = new BigIntegerFitnessAspectFactory(BigInteger.ONE, BigInteger.TEN);
        Fitness fitness1 = new Fitness(f.create(BigInteger.ONE.add(BigInteger.ONE)));
        assertTrue(0.0 < fitness1.getQuality().getNormalizedValue());
        assertTrue(1.0 > fitness1.getQuality().getNormalizedValue());
        Fitness fitness2 = new Fitness(new LongFitnessAspectFactory(1, 10).create(2));
        assertTrue(0.0 < fitness2.getQuality().getNormalizedValue());
        assertTrue(1.0 > fitness2.getQuality().getNormalizedValue());
    }

    /**
     * Tests {@link FitnessComposer} Implementations.
     */
    public void testCompositeFitnesses() throws Exception {
        SimpleKnapsack pp = new SimpleKnapsack(new TreeSet<Integer>(Arrays.asList(new Integer[] { 1, 10, 100 })), 80);
        Fitness f1 = pp.getFitnessFromSum(80);
        System.out.println(f1 + " " + f1.getValidity().getNormalizedValue());
        Fitness f2 = pp.getFitnessFromSum(81);
        System.out.println(f2 + " " + f2.getValidity().getNormalizedValue());
        assertTrue(f1.betterThan(f2));
        f1 = new Fitness(new LongFitnessAspectFactory(0, 10).create(1));
        f2 = new Fitness(new LongFitnessAspectFactory(0, 10).create(2));
        Fitness f3 = new Fitness(new LongFitnessAspectFactory(1, 10).create(3));
        Fitness f4 = new Fitness(new LongFitnessAspectFactory(1, 10).create(4));
        FitnessAspectComposer composer = new LexicographicalOrderingAspectComposer();
        List<FitnessAspect> c1 = Arrays.asList(new FitnessAspect[] { f1.getQuality(), f2.getQuality() });
        List<FitnessAspect> c2 = Arrays.asList(new FitnessAspect[] { f3.getQuality(), f4.getQuality() });
        assertNotBetterThan(composer.compose(c1), composer.compose(c1));
        assertNotBetterThan(composer.compose(c2), composer.compose(c2));
        assertNotBetterThan(composer.compose(c1), composer.compose(c2));
        assertBetterThan(composer.compose(c2), composer.compose(c1));
        c1 = Arrays.asList(new FitnessAspect[] { f1.getQuality(), f2.getQuality() });
        c2 = Arrays.asList(new FitnessAspect[] { f1.getQuality(), f3.getQuality() });
        assertNotBetterThan(composer.compose(c1), composer.compose(c1));
        assertNotBetterThan(composer.compose(c2), composer.compose(c2));
        assertNotBetterThan(composer.compose(c1), composer.compose(c2));
        assertBetterThan(composer.compose(c2), composer.compose(c1));
        c1 = Arrays.asList(new FitnessAspect[] { f2.getQuality(), f1.getQuality() });
        c2 = Arrays.asList(new FitnessAspect[] { f3.getQuality(), f1.getQuality() });
        assertNotBetterThan(composer.compose(c1), composer.compose(c1));
        assertNotBetterThan(composer.compose(c2), composer.compose(c2));
        assertNotBetterThan(composer.compose(c1), composer.compose(c2));
        assertBetterThan(composer.compose(c2), composer.compose(c1));
        composer = new WeightedArithmeticAverageAspectComposer(new double[] { 1, 3 });
        c1 = Arrays.asList(new FitnessAspect[] { f1.getQuality(), f4.getQuality() });
        c2 = Arrays.asList(new FitnessAspect[] { f2.getQuality(), f1.getQuality() });
        assertNotBetterThan(composer.compose(c1), composer.compose(c1));
        assertNotBetterThan(composer.compose(c2), composer.compose(c2));
        assertBetterThan(composer.compose(c1), composer.compose(c2));
        assertNotBetterThan(composer.compose(c2), composer.compose(c1));
        composer = new WeightedArithmeticAverageAspectComposer(new double[] { 1, 1, 1, 1 });
        c1 = Arrays.asList(new FitnessAspect[] { f1.getQuality(), f2.getQuality(), f4.getQuality(), f4.getQuality() });
        c2 = Arrays.asList(new FitnessAspect[] { f1.getQuality(), f2.getQuality(), f3.getQuality(), f4.getQuality() });
        assertNotBetterThan(composer.compose(c1), composer.compose(c1));
        assertNotBetterThan(composer.compose(c2), composer.compose(c2));
        assertBetterThan(composer.compose(c1), composer.compose(c2));
        assertNotBetterThan(composer.compose(c2), composer.compose(c1));
        composer = new WeightedGeometricAverageAspectComposer(new double[] { 1, 1, 1, 1 });
        c1 = Arrays.asList(new FitnessAspect[] { f1.getQuality(), f2.getQuality(), f4.getQuality(), f4.getQuality() });
        c2 = Arrays.asList(new FitnessAspect[] { f1.getQuality(), f2.getQuality(), f3.getQuality(), f4.getQuality() });
        assertNotBetterThan(composer.compose(c1), composer.compose(c1));
        assertNotBetterThan(composer.compose(c2), composer.compose(c2));
        assertBetterThan(composer.compose(c1), composer.compose(c2));
        assertNotBetterThan(composer.compose(c2), composer.compose(c1));
    }

    private void assertNotBetterThan(FitnessAspect f1, FitnessAspect f2) {
        assertNotBetterThan(new Fitness(f1), new Fitness(f2));
    }

    private void assertBetterThan(FitnessAspect f1, FitnessAspect f2) {
        assertBetterThan(new Fitness(f1), new Fitness(f2));
    }

    private void assertNotBetterThan(Fitness f1, Fitness f2) {
        System.out.println("f1 " + f1);
        System.out.println("f2 " + f2);
        assertTrue(!f1.betterThan(f2));
        assertTrue(!f1.betterThan(f2, 0.5));
    }

    private void assertBetterThan(Fitness f1, Fitness f2) {
        System.out.println("f1 " + f1);
        System.out.println("f2 " + f2);
        assertTrue(f1.betterThan(f2));
        assertTrue(f1.betterThan(f2, 0.5));
    }
}

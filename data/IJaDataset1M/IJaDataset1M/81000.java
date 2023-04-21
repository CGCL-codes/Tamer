package org.jheuristics.ga.operators.selectors;

import org.easymock.MockControl;
import org.jheuristics.Individual;
import org.jheuristics.ga.Fitness;
import org.jheuristics.ga.GAConfig;
import org.jheuristics.ga.Population;
import org.jheuristics.ga.scalers.FitnessScaler;
import org.jheuristics.util.RandomGenerator;
import junit.framework.TestCase;

public class StochasticUniversalSampingSelectorTest extends TestCase {

    private StochasticUniversalSampingSelector selector;

    private MockControl controlPopulation;

    private MockControl controlPopulationOther;

    private MockControl controlConfig;

    private MockControl controlTransformation;

    private MockControl controlIndividuals[];

    private MockControl controlFitness[];

    private MockControl controlRandom;

    private Population population;

    private Population otherPop;

    private Individual[] individuals;

    private Fitness[] fitness;

    private GAConfig config;

    private RandomGenerator random;

    private FitnessScaler transformation;

    protected void setUp() throws Exception {
        super.setUp();
        controlPopulation = MockControl.createStrictControl(Population.class);
        controlPopulationOther = MockControl.createStrictControl(Population.class);
        controlConfig = MockControl.createStrictControl(GAConfig.class);
        controlTransformation = MockControl.createStrictControl(FitnessScaler.class);
        controlIndividuals = new MockControl[5];
        controlFitness = new MockControl[5];
        for (int i = 0; i < controlIndividuals.length; i++) {
            controlIndividuals[i] = MockControl.createStrictControl(Individual.class);
            controlFitness[i] = MockControl.createStrictControl(Fitness.class);
        }
        controlRandom = MockControl.createStrictControl(RandomGenerator.class);
        population = (Population) controlPopulation.getMock();
        otherPop = (Population) controlPopulationOther.getMock();
        config = (GAConfig) controlConfig.getMock();
        transformation = (FitnessScaler) controlTransformation.getMock();
        random = (RandomGenerator) controlRandom.getMock();
        individuals = new Individual[controlIndividuals.length];
        fitness = new Fitness[controlFitness.length];
        for (int i = 0; i < individuals.length; i++) {
            individuals[i] = (Individual) controlIndividuals[i].getMock();
            fitness[i] = (Fitness) controlFitness[i].getMock();
        }
        selector = new StochasticUniversalSampingSelector(transformation);
    }

    public final void testConstructor() {
        try {
            new StochasticUniversalSampingSelector(null);
            fail();
        } catch (NullPointerException e) {
        }
        assertNull(selector.getMinimalValue());
        selector = new StochasticUniversalSampingSelector(transformation, null);
        assertNull(selector.getMinimalValue());
        selector = new StochasticUniversalSampingSelector(transformation, Double.valueOf("0.5"));
        assertEquals(Double.valueOf("0.5"), selector.getMinimalValue());
    }

    public final void testSelectorRequiriments() {
        try {
            selector.select(null, 0, null, null);
            fail();
        } catch (NullPointerException e) {
        }
        try {
            controlPopulation.replay();
            selector.select(population, -1, null, null);
            fail();
        } catch (IllegalArgumentException e) {
            controlPopulation.verify();
            controlPopulation.reset();
        }
        try {
            controlPopulation.replay();
            selector.select(population, 0, null, null);
            fail();
        } catch (NullPointerException e) {
            controlPopulation.verify();
            controlPopulation.reset();
        }
        population.size();
        controlPopulation.setReturnValue(0);
        controlPopulation.replay();
        controlConfig.replay();
        try {
            selector.select(population, 0, null, config);
        } catch (IllegalArgumentException e) {
            controlPopulation.verify();
            controlConfig.verify();
        }
    }

    public final void testGetSetTransformation() {
        MockControl controlTransTemp = MockControl.createStrictControl(FitnessScaler.class);
        FitnessScaler trans1 = (FitnessScaler) controlTransTemp.getMock();
        assertEquals(transformation, selector.getTransformation());
        selector.setTransformation(trans1);
        assertEquals(trans1, selector.getTransformation());
        try {
            selector.setTransformation(null);
            fail();
        } catch (NullPointerException e) {
        }
        assertEquals(trans1, selector.getTransformation());
    }

    public final void testGetSetMinimal() {
        assertNull(selector.getMinimalValue());
        selector.setMinimalValue(Double.valueOf("0.1"));
        assertEquals(Double.valueOf("0.1"), selector.getMinimalValue());
        selector.setMinimalValue(null);
        assertNull(selector.getMinimalValue());
    }

    public final void testSelect1() {
        for (int i = 0; i < individuals.length; i++) {
            individuals[i].getFitness();
            controlIndividuals[i].setReturnValue(fitness[i], MockControl.ZERO_OR_MORE);
            controlIndividuals[i].replay();
        }
        population.size();
        controlPopulation.setReturnValue(individuals.length);
        population.toIndividualArray();
        controlPopulation.setReturnValue(individuals);
        population.getIndividual(2);
        controlPopulation.setReturnValue(individuals[2]);
        transformation.scale(fitness);
        controlTransformation.setDefaultMatcher(MockControl.ARRAY_MATCHER);
        controlTransformation.setReturnValue(new double[] { 1, 2, 3, 4, 5 });
        config.getRandomGenerator();
        controlConfig.setReturnValue(random);
        random.nextDouble();
        controlRandom.setReturnValue(0.2);
        controlPopulation.replay();
        controlTransformation.replay();
        controlConfig.replay();
        controlRandom.replay();
        Individual[] result = selector.select(population, 1, null, config);
        assertEquals(1, result.length);
        assertEquals(individuals[2], result[0]);
        controlPopulation.verify();
        controlTransformation.verify();
        controlConfig.verify();
        controlRandom.verify();
    }

    public final void testSelect2() {
        for (int i = 0; i < individuals.length; i++) {
            individuals[i].getFitness();
            controlIndividuals[i].setReturnValue(fitness[i], MockControl.ZERO_OR_MORE);
            controlIndividuals[i].replay();
        }
        population.size();
        controlPopulation.setReturnValue(individuals.length);
        population.toIndividualArray();
        controlPopulation.setReturnValue(individuals);
        population.getIndividual(2);
        controlPopulation.setReturnValue(individuals[2]);
        population.getIndividual(4);
        controlPopulation.setReturnValue(individuals[4]);
        transformation.scale(fitness);
        controlTransformation.setDefaultMatcher(MockControl.ARRAY_MATCHER);
        controlTransformation.setReturnValue(new double[] { 1, 2, 3, 4, 5 });
        config.getRandomGenerator();
        controlConfig.setReturnValue(random);
        random.nextDouble();
        controlRandom.setReturnValue(0.2);
        controlPopulation.replay();
        controlTransformation.replay();
        controlConfig.replay();
        controlRandom.replay();
        Individual[] result = selector.select(population, 2, null, config);
        assertEquals(2, result.length);
        assertEquals(individuals[2], result[0]);
        assertEquals(individuals[4], result[1]);
        controlPopulation.verify();
        controlTransformation.verify();
        controlConfig.verify();
        controlRandom.verify();
    }

    public final void testSelect3() {
        for (int i = 0; i < individuals.length; i++) {
            individuals[i].getFitness();
            controlIndividuals[i].setReturnValue(fitness[i], MockControl.ZERO_OR_MORE);
            controlIndividuals[i].replay();
        }
        population.size();
        controlPopulation.setReturnValue(individuals.length);
        population.toIndividualArray();
        controlPopulation.setReturnValue(individuals);
        population.getIndividual(3);
        controlPopulation.setReturnValue(individuals[3]);
        population.getIndividual(4);
        controlPopulation.setReturnValue(individuals[4]);
        population.getIndividual(1);
        controlPopulation.setReturnValue(individuals[1]);
        transformation.scale(fitness);
        controlTransformation.setDefaultMatcher(MockControl.ARRAY_MATCHER);
        controlTransformation.setReturnValue(new double[] { 1, 2, 3, 4, 5 });
        config.getRandomGenerator();
        controlConfig.setReturnValue(random);
        random.nextDouble();
        controlRandom.setReturnValue(0.4);
        controlPopulation.replay();
        controlTransformation.replay();
        controlConfig.replay();
        controlRandom.replay();
        Individual[] result = selector.select(population, 3, null, config);
        assertEquals(3, result.length);
        assertEquals(individuals[3], result[0]);
        assertEquals(individuals[4], result[1]);
        assertEquals(individuals[1], result[2]);
        controlPopulation.verify();
        controlTransformation.verify();
        controlConfig.verify();
        controlRandom.verify();
    }

    public final void testSelect1IndividualEachTime() {
        for (int i = 0; i < individuals.length; i++) {
            individuals[i].getFitness();
            controlIndividuals[i].setReturnValue(fitness[i], MockControl.ZERO_OR_MORE);
            controlIndividuals[i].replay();
        }
        population.size();
        controlPopulation.setReturnValue(individuals.length);
        population.toIndividualArray();
        controlPopulation.setReturnValue(individuals);
        population.getIndividual(2);
        controlPopulation.setReturnValue(individuals[2]);
        population.size();
        controlPopulation.setReturnValue(individuals.length);
        population.toIndividualArray();
        controlPopulation.setReturnValue(individuals);
        population.getIndividual(4);
        controlPopulation.setReturnValue(individuals[4]);
        transformation.scale(fitness);
        controlTransformation.setDefaultMatcher(MockControl.ARRAY_MATCHER);
        controlTransformation.setReturnValue(new double[] { 1, 2, 3, 4, 5 });
        config.getRandomGenerator();
        controlConfig.setReturnValue(random);
        random.nextDouble();
        controlRandom.setReturnValue(0.2);
        controlRandom.setReturnValue(0.65);
        controlPopulation.replay();
        controlTransformation.replay();
        controlConfig.replay();
        controlRandom.replay();
        Individual[] result = selector.select(population, 1, null, config);
        assertEquals(1, result.length);
        assertEquals(individuals[2], result[0]);
        result = selector.select(population, 1, null, config);
        assertEquals(1, result.length);
        assertEquals(individuals[4], result[0]);
        controlPopulation.verify();
        controlTransformation.verify();
        controlConfig.verify();
        controlRandom.verify();
    }

    public final void testSelect2IndividualsEachTimeButChangingPopulation() {
        for (int i = 0; i < individuals.length; i++) {
            individuals[i].getFitness();
            controlIndividuals[i].setReturnValue(fitness[i], MockControl.ZERO_OR_MORE);
            controlIndividuals[i].replay();
        }
        population.size();
        controlPopulation.setReturnValue(individuals.length);
        population.toIndividualArray();
        controlPopulation.setReturnValue(individuals);
        population.getIndividual(2);
        controlPopulation.setReturnValue(individuals[2]);
        population.size();
        controlPopulation.setReturnValue(individuals.length);
        population.toIndividualArray();
        MockControl controlOtherIndividuals[] = new MockControl[2];
        Individual[] otherIndividuals = new Individual[2];
        for (int i = 0; i < controlOtherIndividuals.length; i++) {
            controlOtherIndividuals[i] = MockControl.createStrictControl(Individual.class);
            otherIndividuals[i] = (Individual) controlOtherIndividuals[i].getMock();
            otherIndividuals[i].getFitness();
            controlOtherIndividuals[i].setReturnValue(fitness[i], MockControl.ZERO_OR_MORE);
            controlOtherIndividuals[i].replay();
        }
        controlPopulation.setReturnValue(otherIndividuals);
        population.getIndividual(1);
        controlPopulation.setReturnValue(otherIndividuals[1]);
        controlTransformation.setDefaultMatcher(MockControl.ARRAY_MATCHER);
        transformation.scale(fitness);
        controlTransformation.setReturnValue(new double[] { 1, 2, 3, 4, 5 });
        transformation.scale(new Fitness[] { fitness[0], fitness[1] });
        controlTransformation.setReturnValue(new double[] { 1, 2 });
        config.getRandomGenerator();
        controlConfig.setReturnValue(random, 2);
        random.nextDouble();
        controlRandom.setReturnValue(0.2, 2);
        controlPopulation.replay();
        controlTransformation.replay();
        controlConfig.replay();
        controlRandom.replay();
        Individual[] result = selector.select(population, 1, null, config);
        assertEquals(1, result.length);
        assertEquals(individuals[2], result[0]);
        controlIndividuals = null;
        individuals = null;
        System.gc();
        System.runFinalization();
        result = selector.select(population, 1, null, config);
        assertEquals(1, result.length);
        assertEquals(otherIndividuals[1], result[0]);
        controlPopulation.verify();
        controlTransformation.verify();
        controlConfig.verify();
        controlRandom.verify();
    }

    public final void testSelect2EachTimeFromDiferentPopulationButSameIndividuals() {
        for (int i = 0; i < individuals.length; i++) {
            individuals[i].getFitness();
            controlIndividuals[i].setReturnValue(fitness[i], MockControl.ZERO_OR_MORE);
            controlIndividuals[i].replay();
        }
        population.size();
        controlPopulation.setReturnValue(individuals.length);
        population.toIndividualArray();
        controlPopulation.setReturnValue(individuals);
        population.getIndividual(2);
        controlPopulation.setReturnValue(individuals[2]);
        population.size();
        controlPopulation.setReturnValue(individuals.length);
        population.toIndividualArray();
        controlPopulation.setReturnValue(individuals);
        population.getIndividual(4);
        controlPopulation.setReturnValue(individuals[4]);
        otherPop.size();
        controlPopulationOther.setReturnValue(individuals.length);
        otherPop.toIndividualArray();
        controlPopulationOther.setReturnValue(individuals);
        otherPop.getIndividual(3);
        controlPopulationOther.setReturnValue(individuals[3]);
        transformation.scale(fitness);
        controlTransformation.setReturnValue(new double[] { 1, 2, 3, 4, 5 }, 2);
        controlTransformation.setDefaultMatcher(MockControl.ARRAY_MATCHER);
        config.getRandomGenerator();
        controlConfig.setReturnValue(random, 2);
        random.nextDouble();
        controlRandom.setReturnValue(0.2);
        controlRandom.setReturnValue(0.65);
        controlRandom.setReturnValue(0.4);
        controlPopulation.replay();
        controlPopulationOther.replay();
        controlTransformation.replay();
        controlConfig.replay();
        controlRandom.replay();
        Individual[] result = selector.select(population, 1, null, config);
        assertEquals(1, result.length);
        assertEquals(individuals[2], result[0]);
        result = selector.select(population, 1, null, config);
        assertEquals(1, result.length);
        assertEquals(individuals[4], result[0]);
        controlPopulation.verify();
        population = null;
        controlPopulation = null;
        System.gc();
        System.runFinalization();
        result = selector.select(otherPop, 1, null, config);
        assertEquals(1, result.length);
        assertEquals(individuals[3], result[0]);
        controlPopulationOther.verify();
        controlTransformation.verify();
        controlConfig.verify();
        controlRandom.verify();
    }

    public final void testSelect3WithMinimalValue0() {
        for (int i = 0; i < individuals.length; i++) {
            individuals[i].getFitness();
            controlIndividuals[i].setReturnValue(fitness[i], MockControl.ZERO_OR_MORE);
            controlIndividuals[i].replay();
        }
        population.size();
        controlPopulation.setReturnValue(individuals.length);
        population.toIndividualArray();
        controlPopulation.setReturnValue(individuals);
        population.getIndividual(2);
        controlPopulation.setReturnValue(individuals[2]);
        population.getIndividual(3);
        controlPopulation.setReturnValue(individuals[3]);
        population.getIndividual(0);
        controlPopulation.setReturnValue(individuals[0]);
        transformation.scale(fitness);
        controlTransformation.setReturnValue(new double[] { 1, 2, 3, 4 });
        controlTransformation.setDefaultMatcher(MockControl.ARRAY_MATCHER);
        config.getRandomGenerator();
        controlConfig.setReturnValue(random);
        random.nextDouble();
        controlRandom.setReturnValue(0.4);
        controlPopulation.replay();
        controlTransformation.replay();
        controlConfig.replay();
        controlRandom.replay();
        selector = new StochasticUniversalSampingSelector(transformation);
        selector.setMinimalValue(Double.valueOf("0."));
        Individual[] result = selector.select(population, 3, null, config);
        assertEquals(3, result.length);
        assertEquals(individuals[2], result[0]);
        assertEquals(individuals[3], result[1]);
        assertEquals(individuals[0], result[2]);
        controlPopulation.verify();
        controlTransformation.verify();
        controlConfig.verify();
        controlRandom.verify();
    }
}

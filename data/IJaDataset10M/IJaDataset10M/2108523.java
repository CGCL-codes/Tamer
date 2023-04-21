package org.jheuristics.ga;

import org.easymock.MockControl;
import org.jheuristics.Individual;
import org.jheuristics.util.Condition;
import junit.framework.TestCase;

public class SimpleEvolverTest extends TestCase {

    private MockControl controlConfig;

    private MockControl controlOtherConfig;

    private MockControl controlIndividual1;

    private MockControl controlIndividual2;

    private MockControl controlIndividual3;

    private MockControl controlIndividual4;

    private MockControl controlIndividual5;

    private MockControl controlIndividualFactory;

    private MockControl controlBulk;

    private MockControl controlPop1;

    private MockControl controlPop2;

    private MockControl controlPop3;

    private MockControl controlOperator;

    private MockControl controlCondition;

    private BulkFitnessEvaluator bulk;

    private IndividualsFactory individualFactory;

    private Individual individual1;

    private Individual individual2;

    private Individual individual3;

    private Individual individual4;

    private Individual individual5;

    private Operator operator;

    private Condition condition;

    private Population pop1;

    private Population pop2;

    private Population pop3;

    private GAConfig config;

    private GAConfig otherConfig;

    private SimpleEvolver evolver;

    protected void setUp() throws Exception {
        super.setUp();
        controlConfig = MockControl.createStrictControl(GAConfig.class);
        controlOtherConfig = MockControl.createStrictControl(GAConfig.class);
        controlBulk = MockControl.createStrictControl(BulkFitnessEvaluator.class);
        controlIndividualFactory = MockControl.createStrictControl(IndividualsFactory.class);
        controlIndividual1 = MockControl.createNiceControl(Individual.class);
        controlIndividual2 = MockControl.createNiceControl(Individual.class);
        controlIndividual3 = MockControl.createNiceControl(Individual.class);
        controlIndividual4 = MockControl.createNiceControl(Individual.class);
        controlIndividual5 = MockControl.createNiceControl(Individual.class);
        controlPop1 = MockControl.createStrictControl(Population.class);
        controlPop2 = MockControl.createStrictControl(Population.class);
        controlPop3 = MockControl.createStrictControl(Population.class);
        controlOperator = MockControl.createStrictControl(Operator.class);
        controlCondition = MockControl.createStrictControl(Condition.class);
        config = (GAConfig) controlConfig.getMock();
        otherConfig = (GAConfig) controlOtherConfig.getMock();
        bulk = (BulkFitnessEvaluator) controlBulk.getMock();
        individualFactory = (IndividualsFactory) controlIndividualFactory.getMock();
        operator = (Operator) controlOperator.getMock();
        condition = (Condition) controlCondition.getMock();
        individual1 = (Individual) controlIndividual1.getMock();
        individual2 = (Individual) controlIndividual2.getMock();
        individual3 = (Individual) controlIndividual3.getMock();
        individual4 = (Individual) controlIndividual4.getMock();
        individual5 = (Individual) controlIndividual5.getMock();
        pop1 = (Population) controlPop1.getMock();
        pop2 = (Population) controlPop2.getMock();
        pop3 = (Population) controlPop3.getMock();
        evolver = new SimpleEvolver(config, 5);
    }

    public final void testSimpleEvolver() {
        try {
            new SimpleEvolver(null, 5);
            fail();
        } catch (NullPointerException e) {
        }
        try {
            new SimpleEvolver(config, -1);
            fail();
        } catch (IllegalArgumentException e) {
        }
    }

    public final void testInitialize() {
        config.getIndividualsFactory();
        controlConfig.setReturnValue(individualFactory, 5);
        individualFactory.createRandomIndividual(config);
        controlIndividualFactory.setReturnValue(individual1);
        controlIndividualFactory.setReturnValue(individual2);
        controlIndividualFactory.setReturnValue(individual3);
        controlIndividualFactory.setReturnValue(individual4);
        controlIndividualFactory.setReturnValue(individual5);
        config.getBulkFitnessEvaluator();
        controlConfig.setReturnValue(bulk);
        Population pop = new DefaultPopulation();
        pop.addIndividual(individual1);
        pop.addIndividual(individual2);
        pop.addIndividual(individual3);
        pop.addIndividual(individual4);
        pop.addIndividual(individual5);
        bulk.evaluateFitness(pop, null, config);
        controlBulk.replay();
        controlConfig.replay();
        controlIndividualFactory.replay();
        controlIndividual1.replay();
        controlIndividual2.replay();
        controlIndividual3.replay();
        controlIndividual4.replay();
        controlIndividual5.replay();
        assertNull(evolver.getEvolutionStatus());
        evolver.initialize();
        GAStatus status = evolver.getEvolutionStatus();
        assertEquals(0, status.getEvolutionAge());
        assertEquals(individual1, status.getCurrentPopulation().getIndividual(0));
        assertEquals(individual2, status.getCurrentPopulation().getIndividual(1));
        assertEquals(individual3, status.getCurrentPopulation().getIndividual(2));
        assertEquals(individual4, status.getCurrentPopulation().getIndividual(3));
        assertEquals(individual5, status.getCurrentPopulation().getIndividual(4));
    }

    public final void testCreateGAStatus() {
        try {
            evolver.createGAStatus(-1, pop1, pop2, pop3);
            fail();
        } catch (IllegalArgumentException e) {
        }
        try {
            evolver.createGAStatus(0, null, pop2, pop3);
            fail();
        } catch (NullPointerException e) {
        }
        try {
            evolver.createGAStatus(0, new DefaultPopulation(), null, null);
            fail();
        } catch (NullPointerException e) {
        }
        try {
            evolver.createGAStatus(0, new DefaultPopulation(), new DefaultPopulation(), null);
            fail();
        } catch (NullPointerException e) {
        }
        evolver.createGAStatus(0, new DefaultPopulation(), new DefaultPopulation(), new DefaultPopulation());
    }

    public final void testCreateEmptyPopulation() {
        assertEquals(new DefaultPopulation(), evolver.createEmptyPopulation());
    }

    public final void testEvolve() {
        config.getIndividualsFactory();
        controlConfig.setReturnValue(individualFactory, 5);
        individualFactory.createRandomIndividual(config);
        controlIndividualFactory.setReturnValue(individual1);
        controlIndividualFactory.setReturnValue(individual2);
        controlIndividualFactory.setReturnValue(individual3);
        controlIndividualFactory.setReturnValue(individual4);
        controlIndividualFactory.setReturnValue(individual5);
        config.getBulkFitnessEvaluator();
        controlConfig.setReturnValue(bulk);
        Population pop = new DefaultPopulation();
        pop.addIndividual(individual1);
        pop.addIndividual(individual2);
        pop.addIndividual(individual3);
        pop.addIndividual(individual4);
        pop.addIndividual(individual5);
        bulk.evaluateFitness(pop, null, config);
        config.getOperator();
        controlConfig.setReturnValue(operator);
        operator.operate(null, config);
        controlOperator.setDefaultMatcher(MockControl.ALWAYS_MATCHER);
        config.getBulkFitnessEvaluator();
        controlConfig.setReturnValue(bulk);
        controlBulk.setDefaultMatcher(MockControl.ALWAYS_MATCHER);
        bulk.evaluateFitness(new DefaultPopulation(), null, config);
        controlBulk.replay();
        controlConfig.replay();
        controlIndividualFactory.replay();
        controlIndividual1.replay();
        controlIndividual2.replay();
        controlIndividual3.replay();
        controlIndividual4.replay();
        controlIndividual5.replay();
        assertNull(evolver.getEvolutionStatus());
        evolver.evolve();
        GAStatus status = evolver.getEvolutionStatus();
        assertEquals(1, status.getEvolutionAge());
        assertEquals(0, status.getCurrentPopulation().size());
    }

    public final void testEvolveint() {
        config.getIndividualsFactory();
        controlConfig.setReturnValue(individualFactory, 5);
        individualFactory.createRandomIndividual(config);
        controlIndividualFactory.setReturnValue(individual1);
        controlIndividualFactory.setReturnValue(individual2);
        controlIndividualFactory.setReturnValue(individual3);
        controlIndividualFactory.setReturnValue(individual4);
        controlIndividualFactory.setReturnValue(individual5);
        config.getBulkFitnessEvaluator();
        controlConfig.setReturnValue(bulk);
        Population pop = new DefaultPopulation();
        pop.addIndividual(individual1);
        pop.addIndividual(individual2);
        pop.addIndividual(individual3);
        pop.addIndividual(individual4);
        pop.addIndividual(individual5);
        bulk.evaluateFitness(pop, null, config);
        for (int i = 0; i < 5; i++) {
            config.getOperator();
            controlConfig.setReturnValue(operator);
            operator.operate(null, config);
            controlOperator.setMatcher(MockControl.ALWAYS_MATCHER);
            config.getBulkFitnessEvaluator();
            controlConfig.setReturnValue(bulk);
            bulk.evaluateFitness(new DefaultPopulation(), null, config);
            controlBulk.setMatcher(MockControl.ALWAYS_MATCHER);
        }
        controlBulk.replay();
        controlConfig.replay();
        controlIndividualFactory.replay();
        controlIndividual1.replay();
        controlIndividual2.replay();
        controlIndividual3.replay();
        controlIndividual4.replay();
        controlIndividual5.replay();
        assertNull(evolver.getEvolutionStatus());
        evolver.evolve(5);
        GAStatus status = evolver.getEvolutionStatus();
        assertEquals(5, status.getEvolutionAge());
        assertEquals(0, status.getCurrentPopulation().size());
    }

    public final void testEvolveCondition() {
        config.getIndividualsFactory();
        controlConfig.setReturnValue(individualFactory, 5);
        individualFactory.createRandomIndividual(config);
        controlIndividualFactory.setReturnValue(individual1);
        controlIndividualFactory.setReturnValue(individual2);
        controlIndividualFactory.setReturnValue(individual3);
        controlIndividualFactory.setReturnValue(individual4);
        controlIndividualFactory.setReturnValue(individual5);
        config.getBulkFitnessEvaluator();
        controlConfig.setReturnValue(bulk);
        Population pop = new DefaultPopulation();
        pop.addIndividual(individual1);
        pop.addIndividual(individual2);
        pop.addIndividual(individual3);
        pop.addIndividual(individual4);
        pop.addIndividual(individual5);
        bulk.evaluateFitness(pop, null, config);
        controlCondition.setDefaultMatcher(MockControl.ALWAYS_MATCHER);
        condition.check(null);
        controlCondition.setReturnValue(true, 5);
        controlCondition.setReturnValue(false);
        for (int i = 0; i < 5; i++) {
            config.getOperator();
            controlConfig.setReturnValue(operator);
            operator.operate(null, config);
            controlOperator.setMatcher(MockControl.ALWAYS_MATCHER);
            config.getBulkFitnessEvaluator();
            controlConfig.setReturnValue(bulk);
            bulk.evaluateFitness(new DefaultPopulation(), null, config);
            controlBulk.setMatcher(MockControl.ALWAYS_MATCHER);
        }
        controlBulk.replay();
        controlConfig.replay();
        controlIndividualFactory.replay();
        controlIndividual1.replay();
        controlIndividual2.replay();
        controlIndividual3.replay();
        controlIndividual4.replay();
        controlIndividual5.replay();
        assertNull(evolver.getEvolutionStatus());
        evolver.evolve(5);
        GAStatus status = evolver.getEvolutionStatus();
        assertEquals(5, status.getEvolutionAge());
        assertEquals(0, status.getCurrentPopulation().size());
    }

    public final void testGetEvolutionStatus() {
        assertNull(evolver.getEvolutionStatus());
    }

    public final void testSetEvolutionStatus() {
        GAStatus status = new DefaultGAStatus(0, new DefaultPopulation(), new DefaultPopulation(), new DefaultPopulation());
        evolver.setEvolutionStatus(status);
        assertSame(status, evolver.getEvolutionStatus());
        try {
            evolver.setEvolutionStatus(null);
            fail();
        } catch (NullPointerException e) {
        }
    }

    public final void testGetGAConfiguration() {
        assertSame(config, evolver.getGAConfiguration());
    }

    public final void testSetGAConfiguration() {
        try {
            evolver.setGAConfiguration(null);
            fail();
        } catch (NullPointerException e) {
        }
        assertSame(config, evolver.getGAConfiguration());
        evolver.setGAConfiguration(otherConfig);
        assertSame(otherConfig, evolver.getGAConfiguration());
    }
}

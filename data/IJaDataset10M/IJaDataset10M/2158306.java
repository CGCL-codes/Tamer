package gpl.lonelysingleton.sleepwalker.tests.genetic;

import gpl.lonelysingleton.sleepwalker.genetic.Individual;
import gpl.lonelysingleton.sleepwalker.genetic.Population;
import gpl.lonelysingleton.sleepwalker.genetic.Problem;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import junit.framework.TestCase;

public class AbstractPopulationImplementorPositiveTest extends TestCase {

    private static final int INITIAL_INDIVIDUALS_AMOUNT = 10;

    private static final int GENERATIONS_AMOUNT = 50;

    private static final int RUNS_FRACTION = 5;

    private Problem Problem;

    private Population PopulationToTest;

    protected void setUp() throws Exception {
        final boolean STOPPING_INVARIANT = false;
        super.setUp();
        Problem = new MockProblem(STOPPING_INVARIANT, RUNS_FRACTION, INITIAL_INDIVIDUALS_AMOUNT, new MockIndividualFactory());
        PopulationToTest = new AbstractPopulationImplementor(Problem);
    }

    public void testGetInitialIndividualsAmount() throws CloneNotSupportedException {
        for (int i = 0; i < GENERATIONS_AMOUNT; ++i) {
            assertEquals("Generation " + (i + 1) + ": " + "initial individuals amount: incorrect value", INITIAL_INDIVIDUALS_AMOUNT, PopulationToTest.getInitialIndividualsAmount());
            assertEquals("Generation " + (i + 1) + ": " + "initial individuals amount: different values", PopulationToTest.getInitialIndividualsAmount(), PopulationToTest.getInitialIndividualsAmount());
            PopulationToTest.changeGeneration();
        }
    }

    public void testGetCurrentGenerationNumber() throws CloneNotSupportedException {
        for (int i = 0; i < GENERATIONS_AMOUNT; ++i) {
            assertEquals("Generation " + i + ": " + "current generation number: incorrect value", i, PopulationToTest.getCurrentGenerationNumber());
            assertEquals("Generation " + i + ": " + "current generation number: different values", PopulationToTest.getCurrentGenerationNumber(), PopulationToTest.getCurrentGenerationNumber());
            PopulationToTest.changeGeneration();
        }
    }

    public void testGetIndividuals() throws CloneNotSupportedException {
        PopulationToTest.changeGeneration();
        for (int i = 1; i < GENERATIONS_AMOUNT; ++i) {
            assertNotSame("Generation " + i + ": " + "individuals: correspond to the same list", PopulationToTest.cloneIndividuals(), PopulationToTest.cloneIndividuals());
            assertEquals("Generation " + i + ": " + "individuals: different values", PopulationToTest.cloneIndividuals(), PopulationToTest.cloneIndividuals());
            PopulationToTest.changeGeneration();
        }
    }

    public void testGetMinIndividual() throws CloneNotSupportedException {
        Individual ExpectedMinIndividual_L;
        for (int i = 0; i < GENERATIONS_AMOUNT; ++i) {
            ExpectedMinIndividual_L = (Individual) Collections.min(PopulationToTest.cloneIndividuals());
            assertEquals("Generation " + i + ": " + "min individual: incorrect value", ExpectedMinIndividual_L, PopulationToTest.getMinIndividual());
            assertEquals("Generation " + i + ": " + "min individual: different values", PopulationToTest.getMinIndividual(), PopulationToTest.getMinIndividual());
            PopulationToTest.changeGeneration();
        }
    }

    public void testGetMaxIndividual() throws CloneNotSupportedException {
        Individual ExpectedMaxIndividual_L;
        for (int i = 0; i < GENERATIONS_AMOUNT; ++i) {
            ExpectedMaxIndividual_L = (Individual) Collections.max(PopulationToTest.cloneIndividuals());
            assertEquals("Generation " + i + ": " + "max individual: incorrect value", ExpectedMaxIndividual_L, PopulationToTest.getMaxIndividual());
            assertEquals("Generation " + i + ": " + "max individual: different values", PopulationToTest.getMaxIndividual(), PopulationToTest.getMaxIndividual());
            PopulationToTest.changeGeneration();
        }
    }

    public void testGetMeanEfficiency() throws CloneNotSupportedException {
        final double DELTA = 0.00001;
        double efficiencySum_L;
        double expectedMeanEfficiency_L;
        List Individuals_L;
        Individuals_L = Collections.synchronizedList(new ArrayList());
        for (int i = 0; i < GENERATIONS_AMOUNT; ++i) {
            efficiencySum_L = 0.0;
            expectedMeanEfficiency_L = 0.0;
            Individuals_L.clear();
            Individuals_L.addAll(PopulationToTest.cloneIndividuals());
            for (int j = 0; j < Individuals_L.size(); ++j) {
                efficiencySum_L = efficiencySum_L + ((Individual) Individuals_L.get(j)).getEfficiency();
            }
            expectedMeanEfficiency_L = efficiencySum_L / Individuals_L.size();
            assertEquals("Generation " + i + ": " + "mean efficiency: incorrect value", expectedMeanEfficiency_L, PopulationToTest.getMeanEfficiency(), DELTA);
            assertEquals("Generation " + i + ": " + "mean efficiency: different values", PopulationToTest.getMeanEfficiency(), PopulationToTest.getMeanEfficiency(), DELTA);
            PopulationToTest.changeGeneration();
        }
    }

    public void testGetPreviousMinIndividual() throws CloneNotSupportedException {
        Individual ExpectedPreviousMinIndividual_L;
        for (int i = 0; i < GENERATIONS_AMOUNT; ++i) {
            ExpectedPreviousMinIndividual_L = (Individual) Collections.min(PopulationToTest.cloneIndividuals());
            PopulationToTest.changeGeneration();
            assertEquals("Generation " + i + ": " + "previous min individual: incorrect value", ExpectedPreviousMinIndividual_L, PopulationToTest.getPreviousMinIndividual());
            assertEquals("Generation " + i + ": " + "previous min individual: different values", PopulationToTest.getPreviousMinIndividual(), PopulationToTest.getPreviousMinIndividual());
        }
    }

    public void testGetPreviousMaxIndividual() throws CloneNotSupportedException {
        Individual ExpectedPreviousMaxIndividual_L;
        for (int i = 0; i < GENERATIONS_AMOUNT; ++i) {
            ExpectedPreviousMaxIndividual_L = (Individual) Collections.max(PopulationToTest.cloneIndividuals());
            PopulationToTest.changeGeneration();
            assertEquals("Generation " + i + ": " + "previous max individual: incorrect value", ExpectedPreviousMaxIndividual_L, PopulationToTest.getPreviousMaxIndividual());
            assertEquals("Generation " + i + ": " + "previous max individual: different values", PopulationToTest.getPreviousMaxIndividual(), PopulationToTest.getPreviousMaxIndividual());
        }
    }

    public void testGetMutatedIndividualsAmount() throws CloneNotSupportedException {
        int expectedIndividualsAmountForMutation_L;
        for (int i = 0; i < GENERATIONS_AMOUNT; ++i) {
            expectedIndividualsAmountForMutation_L = ((MockRunsFractionMutationCriterion) Problem.getMutationCriterion()).getMeetingIndividualsAmount();
            assertEquals("Generation " + i + ": " + "Individuals amount: " + PopulationToTest.getIndividualsAmount() + "; " + "individuals amount for mutation: incorrect value", expectedIndividualsAmountForMutation_L, PopulationToTest.getIndividualsAmountForMutation());
            assertEquals("Generation " + i + ": " + "individuals amount for mutation: different values", PopulationToTest.getIndividualsAmountForMutation(), PopulationToTest.getIndividualsAmountForMutation());
            PopulationToTest.changeGeneration();
        }
    }

    public void testGetProducedByCrossingoverIndividualsAmount() throws CloneNotSupportedException {
        int expectedIndividualsAmountForCrossingover_L;
        for (int i = 0; i < GENERATIONS_AMOUNT; ++i) {
            expectedIndividualsAmountForCrossingover_L = ((MockRunsFractionCrossingoverCriterion) Problem.getCrossingoverCriterion()).getMeetingIndividualsAmount();
            assertEquals("Generation " + i + ": " + "Individuals amount: " + PopulationToTest.getIndividualsAmount() + "; " + "individuals amount for crossingover: " + "incorrect value", expectedIndividualsAmountForCrossingover_L, PopulationToTest.getIndividualsAmountForCrossingover());
            assertEquals("Generation " + i + ": " + "individuals amount for crossingover: " + "different values", PopulationToTest.getIndividualsAmountForCrossingover(), PopulationToTest.getIndividualsAmountForCrossingover());
            PopulationToTest.changeGeneration();
        }
    }

    public void testGetRejectedIndividualsAmount() throws CloneNotSupportedException {
        int expectedIndividualsAmountForRejection_L;
        for (int i = 0; i < GENERATIONS_AMOUNT; ++i) {
            expectedIndividualsAmountForRejection_L = ((MockRunsFractionRejectionCriterion) Problem.getRejectionCriterion()).getMeetingIndividualsAmount();
            assertEquals("Generation " + i + ": " + "Individuals amount: " + PopulationToTest.getIndividualsAmount() + "; " + "individuals amount for rejection: incorrect value", expectedIndividualsAmountForRejection_L, PopulationToTest.getIndividualsAmountForRejection());
            assertEquals("Generation " + i + ": " + "individuals amount for rejection: different values", PopulationToTest.getIndividualsAmountForRejection(), PopulationToTest.getIndividualsAmountForRejection());
            PopulationToTest.changeGeneration();
        }
    }
}

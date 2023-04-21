package gpl.lonelysingleton.sleepwalker.tests.genetic;

import gpl.lonelysingleton.sleepwalker.genetic.AbstractIndividualsPercentCriterion;
import junit.framework.TestCase;

public final class AbstractIndividualsPercentCriterionNegativeTest extends TestCase {

    private static final int INDIVIDUALS_PERCENT = 1;

    private AbstractIndividualsPercentCriterion PercentCriterionToTest;

    protected void setUp() throws Exception {
        super.setUp();
        PercentCriterionToTest = new AbstractIndividualsPercentCriterionImplementor(INDIVIDUALS_PERCENT);
    }

    public void testAbstractIndividualsPercentCriterion() {
        try {
            final int TOO_SMALL_INDIVIDUALS_PERCENT = -1;
            new AbstractIndividualsPercentCriterionImplementor(TOO_SMALL_INDIVIDUALS_PERCENT);
            fail("Too small individuals percent: " + "IllegalArgumentException was not thrown");
        } catch (IllegalArgumentException Exception_P) {
        }
        try {
            final int TOO_LARGE_INDIVIDUALS_PERCENT = 101;
            new AbstractIndividualsPercentCriterionImplementor(TOO_LARGE_INDIVIDUALS_PERCENT);
            fail("Too large individuals percent: " + "IllegalArgumentException was not thrown");
        } catch (IllegalArgumentException Exception_P) {
        }
    }

    public void testGetIndividualsAmountToPerformOperation() {
        try {
            PercentCriterionToTest.getIndividualsAmountToPerformOperation(null);
            fail("Population == null: IllegalArgumentException was not thrown");
        } catch (IllegalArgumentException Exception_P) {
        }
    }

    public void testMeets() {
        try {
            PercentCriterionToTest.meets(null);
            fail("Population == null: IllegalArgumentException was not thrown");
        } catch (IllegalArgumentException Exception_P) {
        }
    }
}

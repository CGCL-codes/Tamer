package gpl.lonelysingleton.sleepwalker.tests.genetic;

import gpl.lonelysingleton.sleepwalker.genetic.Population;
import gpl.lonelysingleton.sleepwalker.genetic.StoppingCriterion;

public final class MockInvariantStoppingCriterion implements StoppingCriterion {

    private boolean meets;

    public MockInvariantStoppingCriterion(boolean meets_P) {
        meets = meets_P;
    }

    public boolean meets(Population Population_P) {
        return meets;
    }

    public void setMeets(boolean meets_P) {
        meets = meets_P;
    }
}

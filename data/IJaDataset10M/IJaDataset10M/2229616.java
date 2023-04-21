package de.denkselbst.niffler;

import de.denkselbst.niffler.refinement.State;
import de.denkselbst.niffler.search.niffler.IoCompletenessCalculator;

public class MockIoCompletenessCalculator extends IoCompletenessCalculator {

    private int needed = 0;

    public MockIoCompletenessCalculator() {
        super(null);
    }

    @Override
    public int getMinimumNumberOfLiteralsRequiredForIoCompleteness(State s) {
        return this.needed;
    }

    public void setMinimumNumberOfLiteralsRequiredForIoCompleteness(int n) {
        this.needed = n;
    }
}

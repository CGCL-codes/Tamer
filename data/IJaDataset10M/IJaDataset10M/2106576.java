package org.vanderbilt.cs278.jbracket.test.dummies;

import org.vanderbilt.cs278.jbracket.model.Competitor;

public class CompetitorDummy implements Competitor {

    private int seed;

    public int getSeed() {
        return seed;
    }

    public void setSeed(int seed) {
        this.seed = seed;
    }
}

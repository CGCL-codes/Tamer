package net.sourceforge.jabm.prng;

import cern.jet.random.engine.MersenneTwister;
import cern.jet.random.engine.RandomEngine;

public class MT32 extends PRNGFactory {

    public RandomEngine create() {
        return new MersenneTwister();
    }

    public RandomEngine create(long seed) {
        return new MersenneTwister((int) seed);
    }

    public String getDescription() {
        return "32-bit Mersenne Twister (Matsumoto and Nishimura)";
    }
}

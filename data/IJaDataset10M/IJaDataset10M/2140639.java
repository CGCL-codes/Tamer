package com.ryanm.trace.game.ai;

import com.ryanm.trace.game.ai.behaviours.AvoidBoundary;
import com.ryanm.trace.game.ai.behaviours.AvoidTrace;
import com.ryanm.trace.game.ai.behaviours.SeekPowerup;

/**
 * seeks powerups
 * 
 * @author ryanm
 */
public class GreedyBot extends GeneBot {

    /***/
    public GreedyBot() {
        super("glutton", "loves the taste of upgrades", new AvoidBoundary(), new AvoidTrace(), new SeekPowerup());
    }
}

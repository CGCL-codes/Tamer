package com.ryanm.trace.game.ai;

import com.ryanm.trace.game.ai.behaviours.AvoidBoundary;
import com.ryanm.trace.game.ai.behaviours.AvoidTrace;
import com.ryanm.trace.game.ai.behaviours.SeekGap;

/**
 * @author ryanm
 */
public class GapHoundBot extends GeneBot {

    /***/
    public GapHoundBot() {
        super("gaphound", "insatiable thirst for points", new AvoidBoundary(), new AvoidTrace(), new SeekGap());
    }
}

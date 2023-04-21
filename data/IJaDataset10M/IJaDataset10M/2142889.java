package com.bytopia.abalone.mechanics;

/**
 * Weak configuration of AI. Uses two-step depth analysis, 25% of the time
 * performs stupid moves. Level "Normal".
 * 
 * @author Bytopia
 */
public class AiBeatrice extends ArtificialIntilligence {

    public Move requestMove(Game g) {
        return findNextMove(g.getBoard(), g.getSide(), 2, STUPID, false);
    }
}

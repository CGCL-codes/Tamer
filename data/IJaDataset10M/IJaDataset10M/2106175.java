package deadend.ai.dog.monteCarloStateSingle;

import deadend.globalenum.Directions;

/**
 *
 * @author Yang JiaJian
 */
public abstract class MCSimStrategy {

    /**
     *
     * @param game
     * @param selfPos
     * @return
     */
    public abstract Directions nextDir(MSimGame game, java.awt.Point selfPos);
}

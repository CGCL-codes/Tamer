package ai.modes;

import objects.basics.entities.Ship;
import ai.Mode;

/**
 * Follow a target.
 * 
 * @author Tom Arnold.
 */
public class Follow extends Mode {

    /**
	 * Take action.
	 * 
	 * @param delta Time since last update.
	 * @param e The entity we're controlling.
	 */
    public void act(int delta, Ship e) {
        new Face().act(delta, e);
        e.updateMovementVector();
        if (e.location.distance(e.target.location) <= 15) {
            new MatchSpeed().act(delta, e);
        } else {
            e.accelerate(delta);
        }
        if (e.location.distance(e.target.location) <= 6) {
            e.decelerate(delta);
        }
    }
}

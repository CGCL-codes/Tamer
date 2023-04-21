package org.icehockeymanager.ihm.game.match.simpleagentengine.fieldplayer;

import org.icehockeymanager.ihm.game.match.simpleagentengine.MatchMessage;
import org.icehockeymanager.ihm.game.match.simpleagentengine.MatchState;
import org.icehockeymanager.ihm.game.match.simpleagentengine.math.Vector2D;
import org.icehockeymanager.ihm.lib.IhmLogging;

/**
 * The Class FieldPlayerStateWait.
 * 
 * @author Chris
 */
public class FieldPlayerStateWait implements MatchState<MatchFieldPlayer>, IhmLogging {

    /** The state. */
    private static FieldPlayerStateWait state;

    /**
   * Instantiates a new field player state wait.
   */
    private FieldPlayerStateWait() {
    }

    /**
   * Gets the single instance of FieldPlayerStateWait.
   * 
   * @return single instance of FieldPlayerStateWait
   */
    public static FieldPlayerStateWait getInstance() {
        if (state == null) {
            state = new FieldPlayerStateWait();
        }
        return state;
    }

    public void enter(MatchFieldPlayer owner) {
        if (!owner.getRink().isGameOn()) {
            owner.getSteering().setTarget(owner.getHomeRegion().getCenter());
        }
    }

    public void execute(MatchFieldPlayer owner) {
        if (!owner.isAtTarget()) {
            owner.getSteering().setTarget(owner.getHomeRegion().getCenter());
            owner.getSteering().arriveOn();
            return;
        } else {
            owner.getSteering().arriveOff();
            owner.setVelocity(new Vector2D());
            owner.trackPuck();
        }
        if (owner.getTeam().isInControl() && !owner.isControllingPlayer() && owner.isAheadOfAttacker()) {
            owner.getTeam().requestPass(owner);
            return;
        }
        if (owner.getRink().isGameOn()) {
            if (owner.isClosestPlayerToPuck() && owner.getTeam().getReceiver() == null && !owner.getRink().getGoalieHasPuck()) {
                owner.getFsm().changeState(FieldPlayerStateChasePuck.getInstance());
                return;
            }
        }
    }

    public void exit(MatchFieldPlayer owner) {
        if (owner.getSteering().arriveIsOn()) owner.getSteering().arriveOff();
    }

    public boolean onMessage(MatchFieldPlayer owner, MatchMessage msg) {
        return false;
    }
}

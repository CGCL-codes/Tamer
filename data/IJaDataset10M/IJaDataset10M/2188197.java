package supaplex;

import supaplex.enums.*;

/**
 * 
 * @author DutchRemco
 * 
 * Creation date: 13-07-2009
 * Use: This holds data to record the play, or to play-back
 */
public class Action {

    ActionType type;

    long duration;

    public Action(int duration, ActionType type) {
        this.duration = duration;
        this.type = type;
    }
}

package com.momosw.games.engine;

import java.util.LinkedList;
import java.util.List;
import com.momosw.games.engine.player.Player;

/**
 * <b>Project:</b> JerichoCardGame<br />
 * <b>Package:</b> com.momosw.games.engine<br />
 * <b>Class:</b> TurnSequence.java<br />
 * <br />
 * <i>GSI 2011</i><br />
 *
 * @author Miguel Coronado (miguelcb84@gmail.com)
 * @version	Aug 17, 2011
 *
 */
public class TurnSequence {

    /** The turn sequence as a list of nodes */
    private List<TurnSequenceNode> turnSequence;

    /**
     * Constructor
     * @param turnSequence
     */
    public TurnSequence(List<TurnSequenceNode> turnSequence) {
        this.turnSequence = turnSequence;
    }

    /**
     * List the available actions. 
     * @return List of actions
     */
    public List<Action> availableActions(Player player) {
        List<Action> res = new LinkedList<Action>();
        res.addAll(this.turnSequence.get(0).actions.keySet());
        return res;
    }

    /**
     * This is not fully generic. It is coded for being used
     * as a turnSequence where any of the possible actions in a 
     * turnSequenceNode make the turnSequence go foward in the 
     * turnSequence chain. 
     * 
     * @param action
     * @throws IllegalArgumentException
     */
    void doAction(Action action) throws IllegalArgumentException {
        TurnSequenceNode node = turnSequence.get(0);
        if (!node.actions.keySet().contains(action)) {
            throw new IllegalArgumentException();
        }
        turnSequence.remove(0);
    }
}

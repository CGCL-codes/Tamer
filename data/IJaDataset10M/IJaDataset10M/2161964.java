package game;

import exceptions.PolisGameRunningException;

/** Class that manage game turns */
public class Turn {

    private GameAction firstAction;

    private GameAction secondAction;

    private static Integer turnCount = 0;

    public Turn() {
        firstAction = null;
        secondAction = null;
        turnCount++;
    }

    /** This method adds an action to the turn */
    public void addGameAction(GameAction action) {
        if (action == null) {
            throw new IllegalArgumentException("Invalid parameter for addGameAction(), cannot be null");
        }
        if (firstAction == null && secondAction == null) {
            firstAction = action;
        } else {
            if ((firstAction instanceof GameAction) && (secondAction == null)) {
                secondAction = action;
            } else {
                throw new PolisGameRunningException("Cannot add more than two actions in a Turn");
            }
        }
    }

    /**
	 * Getters methods for this class
	 */
    public GameAction getFirstAction() {
        return firstAction;
    }

    public GameAction getSecondAction() {
        return secondAction;
    }

    public static Integer getTurnCount() {
        return turnCount;
    }
}

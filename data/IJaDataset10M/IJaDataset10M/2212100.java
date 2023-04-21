package net.sourceforge.huntforgold.logic;

import net.sourceforge.huntforgold.gui.SailorsGUI;
import org.apache.log4j.Logger;

/**
 * This GameState is used when some sailors wants to join the players fleet.
 */
public class SailorsState extends GameState {

    /** The logger */
    private static Logger log = Logger.getLogger(SailorsState.class);

    /** The crew result */
    private int numberOfCrew;

    /**
   * Creates a new SailorsState 
   */
    public SailorsState() {
        super(SAILORS);
        setRenderer(new SailorsGUI(this));
    }

    /**
   * Called whenever the game enters this state.
   */
    public void enter() {
        keyboard.setStickyMode(true);
        Integer value = (Integer) getValue();
        numberOfCrew = value.intValue();
        ((SailorsGUI) getRenderer()).setResult(numberOfCrew);
        if (numberOfCrew == 0) {
            game.setCurrentState(SAILING);
        }
    }

    /**
   * Goes to the next state
   * @param state The next state
   */
    public void nextState(int state) {
        if (state == 0) {
            player.setCrewNumber(player.getCrewNumber() + numberOfCrew);
            game.setCurrentState(SAILING);
        } else if (state == 1) {
            game.setCurrentState(SAILING);
        } else {
            log.fatal("Unknown option: " + state);
            System.exit(1);
        }
    }

    /**
   * Called whenever the game leaves this state.
   */
    public void leave() {
        keyboard.setStickyMode(false);
    }
}

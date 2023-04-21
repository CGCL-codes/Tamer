package net.sourceforge.huntforgold.logic;

import net.sourceforge.huntforgold.gui.RowdyCrewGUI;
import java.util.Random;
import org.apache.log4j.Logger;

/**
 * This GameState is used when a rowdy crew wants to join the players fleet.
 */
public class RowdyCrewState extends GameState {

    /** The logger */
    private static Logger log = Logger.getLogger(RowdyCrewState.class);

    /** The crew result */
    private int numberOfCrew;

    /**
   * Creates a new RowdyCrewState 
   */
    public RowdyCrewState() {
        super(ROWDY_CREW);
        setRenderer(new RowdyCrewGUI(this));
    }

    /**
   * Called whenever the game enters this state.
   */
    public void enter() {
        keyboard.setStickyMode(true);
        int max = player.getFleet().getMaxCrew() - player.getCrewNumber();
        if (max > 50) {
            max = 50;
        }
        Random random = new Random(System.currentTimeMillis());
        numberOfCrew = (int) (max * random.nextDouble());
        ((RowdyCrewGUI) getRenderer()).setResult(numberOfCrew);
        if (numberOfCrew == 0) {
            game.setCurrentState(INSIDE_TOWN);
        }
    }

    /**
   * Goes to the next state
   * @param state The next state
   */
    public void nextState(int state) {
        if (state == 0) {
            player.setCrewNumber(player.getCrewNumber() + numberOfCrew);
            game.setCurrentState(INSIDE_TOWN);
        } else if (state == 1) {
            game.setCurrentState(INSIDE_TOWN);
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

package net.sourceforge.huntforgold.logic;

import net.sourceforge.huntforgold.graphics.renders.Option;
import net.sourceforge.huntforgold.gui.ShipOHoyGUI;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;

/**
 * The player see a ship near by.
 */
public class ShipOHoyState extends GameState {

    /** The logger */
    private static Logger log = Logger.getLogger(ShipOHoyState.class);

    /**
   * Creates a new ShipOHoyState.
   */
    public ShipOHoyState() {
        super(SHIP_OHOY);
        setRenderer(new ShipOHoyGUI(this));
    }

    /**
   * Called whenever the game enters this state.
   */
    public void enter() {
        keyboard.setStickyMode(true);
        List options = new ArrayList();
        options.add(new Option(0, "Investigate"));
        options.add(new Option(1, "Sail away"));
        ((ShipOHoyGUI) getRenderer()).setOptions(options);
    }

    /**
   * Goes to the next state
   * @param state The next state
   */
    public void nextState(int state) {
        if (state == 0) {
            game.setCurrentState(INVESTIGATE_SHIP);
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

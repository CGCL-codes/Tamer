package net.sourceforge.dragonchess.connectivity;

import net.sourceforge.dragonchess.main.DCFrontEnd;
import net.sourceforge.dragonchess.main.DCOptions;

/**
 * This class serves as a client to connect spectators to an existing game.
 * As this behaviour is very much like that of the {@link DCGameClient}, it is
 * likely that this class will not be implemented.
 *
 * <p>This class is not yet implemented.
 *
 * @author Christophe Hertigers
 * @author Davy Herben
 * @version 021208
 */
public class DCGameSpectator extends DCGame {

    /**
	 * Class constructor. Does not do anuthing yet.
	 */
    public DCGameSpectator(DCFrontEnd gui) {
        super(gui);
        if (DCOptions.getDebugNetworking()) {
            System.out.println("****** DCCGAMESPECTATOR INITIALISED ******");
        }
    }
}

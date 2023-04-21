package maggames.core;

import maggames.core.loader.VisualGameLoader;
import org.apache.log4j.Logger;

/**
 * Main entry point for MagGames application.
 * <br>TODO add usage and options for GUI and other interface options
 * <br>TODO refactor to lower package
 * 
 * @author BenjaminPLee
 * @version 1.0
 */
public class MagGames {

    private static final Logger log = Logger.getLogger(MagGames.class);

    /**
	 * Main entry point for MagGames application
	 * <br>TODO add usage information
	 * 
	 * @param args program arguments used to identify the game and interface
	 * the user wishes to use.
	 */
    public static void main(String[] args) {
        log.debug("main() method called.");
        VisualGameLoader vgl = new VisualGameLoader();
        vgl.setVisible(true);
        log.debug("main() method exiting.");
    }
}

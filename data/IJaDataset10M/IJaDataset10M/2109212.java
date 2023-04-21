package runner;

import logic.GameLogic;

/**
 * Runner fuer Testzwecke
 * @author Christian
 *
 */
public class Player4 {

    /**
	 * Neuen Client starten, Parameter Name und Spiel ID
	 * @param args args Wird nur fuer Start vom Webserver aus benoetigt
	 */
    public static void main(String[] args) {
        GameLogic logic = new GameLogic("d", 1969076342);
    }
}

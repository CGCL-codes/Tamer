package runner;

/**
 * Klasse um neues Spiel auf dem Server zu erzeugen
 * @author Christian
 *
 */
public class InitNewGame {

    /**
	 * Namen der teilnehmende Spieler
	 */
    private static String[] players = { "a", "b", "c", "d" };

    /**
	 * Spiel erzeugen
	 * @param args arguments
	 */
    public static void main(String[] args) {
        Client client = new Client();
        client.initNewGame(4, players);
    }
}

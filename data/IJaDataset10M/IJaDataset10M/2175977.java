package soc.message;

/**
 * This message means that all players are done placing
 * their initial settlements.
 *
 * @author Robert S. Thomas
 */
public class SOCSetupDone extends SOCMessage {

    /**
     * Name of game
     */
    private String game;

    /**
     * Create a SetupDone message.
     *
     * @param ga  the name of the game
     */
    public SOCSetupDone(String ga) {
        messageType = SETUPDONE;
        game = ga;
    }

    /**
     * @return the name of the game
     */
    public String getGame() {
        return game;
    }

    /**
     * SETUPDONE sep game
     *
     * @return the command string
     */
    public String toCmd() {
        return toCmd(game);
    }

    /**
     * SETUPDONE sep game
     *
     * @param ga  the name of the game
     * @return the command string
     */
    public static String toCmd(String ga) {
        return SETUPDONE + sep + ga;
    }

    /**
     * Parse the command String into a SetupDone message
     *
     * @param s   the String to parse
     * @return    a SetupDone message, or null of the data is garbled
     */
    public static SOCSetupDone parseDataStr(String s) {
        return new SOCSetupDone(s);
    }

    /**
     * @return a human readable form of the message
     */
    public String toString() {
        return "SOCSetupDone:game=" + game;
    }
}

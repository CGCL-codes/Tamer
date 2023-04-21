package clubmixer.server.console;

/**
 *
 * @author Alexander Schindler
 */
public class InvalidCommandException extends Exception {

    public InvalidCommandException(String commandname) {
        super("Invalid command '" + commandname + "'");
    }
}

package cz.zcu.fav.os.exception;

/**
 * Class for reporting parse errors.
 * @author wishi
 *
 */
public class ConsoleParseException extends Exception {

    /**
	 * Creates a new parse error exception.
	 * @param msg - te message
	 */
    public ConsoleParseException(String msg) {
        super(msg);
    }
}

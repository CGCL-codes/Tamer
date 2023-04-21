package jhomenet.server.console.command;

/**
 * TODO: Class description.
 * <p>
 * Id: $Id: $
 * 
 * @author Dave Irwin (david.irwin@jhu.edu)
 */
public class CommandException extends Exception {

    /**
	 * @param desc
	 */
    public CommandException(String desc) {
        super(desc);
    }

    /**
	 * @param t
	 */
    public CommandException(Throwable t) {
        super(t);
    }

    /**
	 * @param desc
	 * @param t
	 */
    public CommandException(String desc, Throwable t) {
        super(desc, t);
    }
}

package xbird.client.command;

import xbird.util.cmdline.CommandException;

/**
 * 
 * <DIV lang="en"></DIV>
 * <DIV lang="ja"></DIV>
 * 
 * @author Makoto YUI (yuin405+xbird@gmail.com)
 */
public class CommandFailedException extends CommandException {

    private static final long serialVersionUID = -523688214685012624L;

    public CommandFailedException() {
    }

    public CommandFailedException(String message) {
        super(message);
    }

    public CommandFailedException(Throwable cause) {
        super(cause);
    }

    public CommandFailedException(String message, Throwable cause) {
        super(message, cause);
    }
}

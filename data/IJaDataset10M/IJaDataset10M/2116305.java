package org.xith3d.utility.commands;

/**
 * A CommandException is a simple Exception, that is handled a little differently.
 * If a CommandException is thrown by the {@link Command#execute(Boolean, Object[])} method,
 * then only its info text is displayed instead of the whole stack trace.
 * 
 * @author Marvin Froehlich (aka Qudus)
 */
public class CommandException extends Exception {

    private static final long serialVersionUID = -8809791951561358374L;

    public CommandException(String message, Throwable cause) {
        super(message, cause);
    }

    public CommandException(String message) {
        super(message);
    }

    public CommandException(Throwable cause) {
        super(cause);
    }
}

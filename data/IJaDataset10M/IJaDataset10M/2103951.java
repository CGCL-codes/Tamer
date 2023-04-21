package net.sf.brightside.travelsystem.core.command;

public class CommandException extends Exception {

    /**
	 * 
	 */
    String message;

    private static final long serialVersionUID = 1L;

    public CommandException(String message) {
        super();
        this.message = message;
    }

    public String toString() {
        return this.getCause() + "\n" + message;
    }
}

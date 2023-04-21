package br.com.etex.exceptions;

public class GeneralError extends Error {

    private static final long serialVersionUID = 1937312442231919401L;

    public static final String NO_FILE_PROJECT = "no exists file \"configuration.properties\" in �project ";

    /**
	 * @uml.property  name="message"
	 */
    private String message;

    public GeneralError(String message) {
        setMessage(message);
    }

    /**
	 * @return
	 * @uml.property  name="message"
	 */
    @Override
    public String getMessage() {
        return message;
    }

    /**
	 * @param message
	 * @uml.property  name="message"
	 */
    private void setMessage(String message) {
        this.message = message;
    }
}

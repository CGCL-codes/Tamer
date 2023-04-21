package org.minions.stigma.databases.script.server;

/**
 * ScriptParseException is exception thrown by ScriptDB when
 * DB finds error in parsed script XML file.
 */
public class ScriptParseException extends Exception {

    private static final long serialVersionUID = 1L;

    /**
     * Empty constructor.
     */
    public ScriptParseException() {
        super();
    }

    /**
     * Constructor with message parameter.
     * @param message
     *            message to pass with exception
     */
    public ScriptParseException(String message) {
        super(message);
    }

    /**
     * Constructor with message and cause parameter.
     * @param message
     *            message to pass with exception
     * @param cause
     *            throwable to pass with exception
     */
    public ScriptParseException(String message, Throwable cause) {
        super(message, cause);
    }
}

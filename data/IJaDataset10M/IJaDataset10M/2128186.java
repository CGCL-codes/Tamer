package com.android.cts;

/**
 * Throw out CommandNotFoundException if no command action.
 */
public class CommandNotFoundException extends Exception {

    private static final long serialVersionUID = -9101779063189977021L;

    public CommandNotFoundException() {
        super();
    }

    public CommandNotFoundException(String message) {
        super(message);
    }
}

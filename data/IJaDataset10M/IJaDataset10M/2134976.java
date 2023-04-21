package de.uni_muenster.cs.sev.lethal.script.exceptions;

/**
 * Exception raised when an runtime error occurs in the script interpreter.
 * @author Philipp
 *
 */
public class ScriptRuntimeError extends RuntimeException {

    /**
	 * Creates a new ScriptRuntimeError instance.
	 * @param message error message for the error
	 */
    public ScriptRuntimeError(String message) {
        super(message);
    }
}

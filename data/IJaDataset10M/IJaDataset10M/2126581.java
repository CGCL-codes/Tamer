package jargs;

/**
 * Base class for exceptions that may be thrown when options are parsed
 */
public abstract class OptionException extends Exception {

    private static final long serialVersionUID = -2679562572613616748L;

    OptionException(String msg) {
        super(msg);
    }
}

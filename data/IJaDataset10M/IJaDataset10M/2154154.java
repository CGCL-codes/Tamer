package net.sourceforge.ondex.taverna;

/**
 *
 * @author Christian
 */
public class TavernaException extends Exception {

    /**
     * Constructs an instance of <code>TavernaException</code> with the specified detail message.
     * @param msg the detail message.
     */
    public TavernaException(String msg) {
        super(msg);
    }

    public TavernaException(String msg, Exception inner) {
        super(msg, inner);
    }
}

package net.fdukedom.epicurus.tools.mail;

/**
 * Occurs when message cannot be sent.
 *
 * @author Dmitry Y. Kochelaev
 */
public class MessageNotSentException extends Exception {

    /**
     * Constructs <code>MessageNotSendException</code> using message string.
     * @param message message.
     */
    public MessageNotSentException(String message) {
        super(message);
    }

    /**
     * Constructs <code>MessageNotSendException</code> using message string and cause.
     *
     * @param message message.
     * @param cause cause.
     */
    public MessageNotSentException(String message, Exception cause) {
        super(message, cause);
    }
}

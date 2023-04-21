package net.afternoonsun.imaso.exceptions;

/**
 * Thrown when the destination folder does not exist.
 *
 * @author Sergey Pisarenko aka drseergio (drseergio AT gmail DOT com)
 */
public class NoSuchDestinationException extends AbstractImasoException {

    /**
     * Default constructor for exceptions without messages.
     */
    public NoSuchDestinationException() {
    }

    /**
     * Returns the exception message.
     *
     * @return exception message
     */
    @Override
    public String getMessage() {
        return "Specified final destination does not exist";
    }
}

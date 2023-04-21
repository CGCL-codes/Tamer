package ca.uhn.hunit.compare;

import ca.uhn.hunit.ex.UnexpectedTestFailureException;
import ca.uhn.hunit.iface.TestMessage;

/**
 * Implementors of this class are able to compare two messages (of a given type they
 * are able to handle) and return a structured comparison.
 */
public interface ICompare<T> {

    /**
     * Triggers the comparison. This method is expected to be called once,
     * before any other method
     *
     * @param theExpected The expected message
     * @param theActual The actual message
     */
    void compare(T theExpectMessage, T theActualMessage) throws UnexpectedTestFailureException;

    /**
     * @return Returns a string describing the difference
     */
    String describeDifference();

    /**
     * Returns true if the messages are the same
     */
    boolean isSame();
}

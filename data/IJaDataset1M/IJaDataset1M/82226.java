package org.okkam.core.match.query.exceptions;

/**
 * This exception indicates that the value chosen for the Relevance of a Condition has been set out of bounds.
 * 
 * @author stoermer
 *
 */
public class RelevanceOutOfBoundsException extends Exception {

    public RelevanceOutOfBoundsException(String message) {
        super(message);
    }
}

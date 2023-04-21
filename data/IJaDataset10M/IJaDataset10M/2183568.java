package com.w20e.socrates.model;

/**
 * Thrown when a duplicate of an already existing Node in an Instance is
 * encountered.
 *
 * @author W.G.Helmantel
 */
public class DuplicateNodeException extends RuntimeException {

    /**
   * UID.
   */
    private static final long serialVersionUID = 845094453898749441L;

    /**
   * See doc above.
   *
   * @param message some cognitive message
   */
    public DuplicateNodeException(final String message) {
        super(message);
    }
}

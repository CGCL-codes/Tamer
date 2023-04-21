package net.sf.beatrix.core.exceptions;

import net.sf.beatrix.core.Detector;

/**
 * An {@link IllegalStateException} that indicated that a
 * {@link Detector} is not running anymore.
 * 
 * @author Christian Wressnegger <chwress@users.sourceforge.net>
 */
public class DetectorAlreadyRunningException extends IllegalStateException {

    /** The serial version UID. */
    private static final long serialVersionUID = 8858099529555050241L;

    /**
   * The default constructor.
   */
    public DetectorAlreadyRunningException() {
        super("This detector instance gets executed at the moment. " + "You may neither start it another time nor alter the underlying object");
    }
}

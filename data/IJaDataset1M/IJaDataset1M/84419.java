package de.fzi.mappso.align.impl;

import org.semanticweb.owl.align.AlignmentException;

/**
 * Base exception for all problems that might occur in the MapPSO system.
 * 
 * @author bock
 *
 */
public class MapPSOException extends AlignmentException {

    /**
     * Auto-generated serial version ID. 
     */
    private static final long serialVersionUID = -242845500691961618L;

    /**
     * Creates a new MapPSO related exception.
     * 
     * @param message Error message.
     */
    public MapPSOException(String message) {
        super(message);
    }

    /**
     * Creates a new MapPSO related exception.
     * 
     * @param message Error message.
     * @param cause Causing exception.
     */
    public MapPSOException(String message, Exception cause) {
        super(message, cause);
    }
}

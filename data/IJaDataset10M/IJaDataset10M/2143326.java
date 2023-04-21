package org.aitools.programd.util;

/**
 * Thrown when a file cannot be created.
 * 
 * @author <a href="mailto:noel@aitools.org">Noel Bush</a>
 * @since 4.2
 */
public class CouldNotCreateFileException extends Exception {

    /**
     * Creates a new CouldNotCreateFileException.
     * 
     * @param filename the filename for which a file could not be created.
     */
    public CouldNotCreateFileException(String filename) {
        super("Could not create \"" + filename + "\".");
    }
}

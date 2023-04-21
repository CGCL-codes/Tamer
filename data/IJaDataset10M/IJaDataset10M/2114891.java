package org.diffxml.patchxml;

/**
 * Indicates a formatting error in a delta.
 * 
 * @author Adrian Mouat
 *
 */
public class PatchFormatException extends Exception {

    /**
     * Serial ID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Constructor.
     * 
     * @param e Chained exception
     */
    public PatchFormatException(final Exception e) {
        super(e);
    }

    /**
     * Constructor.
     * 
     * @param s Description of error
     */
    public PatchFormatException(final String s) {
        super(s);
    }

    /**
     * Constructor.
     * 
     * @param s Description of error
     * @param e Chained exception
     */
    public PatchFormatException(final String s, final Exception e) {
        super(s, e);
    }
}

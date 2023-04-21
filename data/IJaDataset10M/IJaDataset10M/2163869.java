package org.jgap.distr.grid.common;

/**
 * Signals that no work results are available up to now resp. that none could
 * be found.
 *
 * @author Klaus Meffert
 * @since 3.3.3
 */
public class NoWorkResultsFoundException extends Exception {

    /** String containing the CVS revision. Read out via reflection!*/
    private static final String CVS_REVISION = "$Revision: 1.1 $";

    public NoWorkResultsFoundException() {
    }
}

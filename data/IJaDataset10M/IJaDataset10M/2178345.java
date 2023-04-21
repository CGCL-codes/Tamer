package org.qedeq.kernel.utility;

/**
 * A collection of useful static methods for equality.
 *
 * @version $Revision: 1.5 $
 * @author  Michael Meyling
 */
public final class EqualsUtility {

    /**
     * Constructor, should never be called.
     */
    private EqualsUtility() {
    }

    /**
     * Compare two objects, each of them could be <code>null</code>.
     *
     * @param   a   First parameter.
     * @param   b   Second parameter.
     * @return  Are <code>a</code> and <code>b</code> equal?
     */
    public static boolean equals(final Object a, final Object b) {
        if (a == null) {
            if (b == null) {
                return true;
            }
            return false;
        }
        return a.equals(b);
    }
}

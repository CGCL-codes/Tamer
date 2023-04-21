package org.qedeq.kernel.bo.logic;

import org.qedeq.kernel.base.list.Element;

/**
 * Encapsulates a logical formula.
 * LATER mime 20050205: what for is this anyway???
 *
 * @version $Revision: 1.6 $
 * @author  Michael Meyling
 */
final class EqualFormula {

    /** Element that defines the logical content. */
    private final Element formula;

    /**
     * Constructor.
     *
     * @param   formula     Element that defines the logical content.
     */
    EqualFormula(final Element formula) {
        this.formula = formula;
    }

    public String toString() {
        return formula.toString();
    }
}

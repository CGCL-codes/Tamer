package org.openscience.cdk.isomorphism.matchers.smarts;

import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.CDKConstants;

/**
 * This matcher any aromatic atom. This assumes that aromaticity in the molecule
 * has been perceived.
 *
 * @cdk.module  isomorphism
 * @cdk.githash
 * @cdk.keyword SMARTS
 */
public class AromaticAtom extends SMARTSAtom {

    private static final long serialVersionUID = -3345204886992669829L;

    /**
     * Creates a new instance
     *
     */
    public AromaticAtom() {
        setFlag(CDKConstants.ISAROMATIC, true);
    }

    public boolean matches(IAtom atom) {
        return atom.getFlag(CDKConstants.ISAROMATIC);
    }

    public String toString() {
        return "AromaticAtom()";
    }
}

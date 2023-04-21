package joelib2.algo.morgan;

import joelib2.molecule.Molecule;

/**
 * Interface for resolving renumbering ties.
 *
 * @.author     wegnerj
 * @.license    GPL
 * @.cvsversion    $Revision: 1.6 $, $Date: 2005/02/17 16:48:29 $
 */
public interface SingleTieResolver {

    public double getResolvingValue(AtomDoubleParent ap, Molecule mol);

    public boolean init(Molecule mol);
}

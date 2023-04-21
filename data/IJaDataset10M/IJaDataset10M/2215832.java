package joelib2.util.iterator;

import joelib2.molecule.Bond;

/**
 * @.author wegnerj
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public interface BondIterator extends ListIterator {

    public Bond nextBond();
}

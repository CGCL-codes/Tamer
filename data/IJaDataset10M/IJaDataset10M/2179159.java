package joelib2.feature.types.atomlabel;

import joelib2.molecule.Atom;

/**
 * TODO description.
 *
 * @.author     wegnerj
 * @.license    GPL
 * @.cvsversion $Revision: 1.3 $, $Date: 2005/02/17 16:48:31 $
 */
public class AbstractValence {

    public static int calculate(Atom atom) {
        return atom.getValence();
    }
}

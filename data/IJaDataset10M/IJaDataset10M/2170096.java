package joelib.util.iterator;

import java.util.*;
import joelib.molecule.*;

/**
 * Gets an iterator over all neighbour atoms in a atom.
 *
 * <blockquote><pre>
 * NbrAtomIterator nait = atom.nbrAtomIterator();
 * JOEBond bond;
 * JOEAtom nbrAtom;
 * while (nait.hasNext())
 * {
 *          nbrAtom=nait.nextNbrAtom();
 *   bond = nait.actualBond();
 *
 * }
 * </pre></blockquote>
 *
 * @author     wegnerj
 * @license GPL
 * @cvsversion    $Revision: 1.9 $, $Date: 2004/08/28 21:33:26 $
 * @see VectorIterator
 * @see joelib.molecule.JOEAtom#nbrAtomIterator()
 */
public class NbrAtomIterator extends ListIterator {

    private JOEAtom atom;

    /**
     *  Constructor for the NbrAtomIterator object
     *
     * @param  bonds  Description of the Parameter
     * @param  atom   Description of the Parameter
     */
    public NbrAtomIterator(List bonds, JOEAtom atom) {
        super(bonds);
        this.atom = atom;
    }

    /**
     *  Description of the Method
     *
     * @return    Description of the Return Value
     */
    public Object actual() {
        JOEBond bond = (JOEBond) super.actual();
        return bond.getNbrAtom(atom);
    }

    /**
     *  Description of the Method
     *
     * @return    Description of the Return Value
     */
    public JOEBond actualBond() {
        return (JOEBond) super.actual();
    }

    /**
     *  Description of the Method
     *
     * @return    Description of the Return Value
     */
    public Object next() {
        JOEBond bond = (JOEBond) super.next();
        return bond.getNbrAtom(atom);
    }

    /**
     *  Description of the Method
     *
     * @return    Description of the Return Value
     */
    public JOEAtom nextNbrAtom() {
        return (JOEAtom) next();
    }
}

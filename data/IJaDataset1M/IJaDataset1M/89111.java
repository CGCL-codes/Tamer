package joelib2.algo.morgan;

/**
 * Helper class for resolving renumbering ties.
 *
 * @.author     wegnerj
 * @.license    GPL
 * @.cvsversion    $Revision: 1.7 $, $Date: 2005/02/17 16:48:29 $
 */
public class AtomDouble {

    /**
     * Atom index of the atom.
     */
    public int atomIdx;

    /**
     * Temporary and new atom index stored as double value to handle huge
     * temporary values. The Morgan algorithm can cause really huge
     * values.
     */
    public double tmpAtomIdx;

    /**
     *  Constructor for the IntInt object
     */
    public AtomDouble() {
    }

    /**
     *  Constructor for the IntInt object
     *
     * @param  _i1  Description of the Parameter
     * @param  _i2  Description of the Parameter
     */
    public AtomDouble(int _atomIdx, double _tmpAtomIdx) {
        atomIdx = _atomIdx;
        tmpAtomIdx = _tmpAtomIdx;
    }

    public boolean equals(Object otherObj) {
        if (otherObj instanceof AtomDouble) {
            AtomDouble ai = (AtomDouble) otherObj;
            if ((ai.atomIdx == this.atomIdx) && (ai.tmpAtomIdx == this.tmpAtomIdx)) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public int hashCode() {
        return atomIdx;
    }

    public String toString() {
        return "<atomIdx:" + atomIdx + ", tmpAtomIdx:" + tmpAtomIdx + ">";
    }
}

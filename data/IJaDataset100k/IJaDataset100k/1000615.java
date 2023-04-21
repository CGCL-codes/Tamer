package joelib2.smiles;

import joelib2.molecule.Atom;
import joelib2.molecule.Bond;
import java.util.List;
import java.util.Vector;

/**
 * SMILES node holding atom and bond informations.
 *
 * @.author     wegnerj
 * @.wikipedia  Simplified molecular input line entry specification
 * @.license GPL
 * @.cvsversion    $Revision: 1.7 $, $Date: 2005/02/17 16:48:40 $
 */
public class SMILESNode implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

    private Atom _atom;

    /**
     * {@link java.util.List} of <tt>Bond</tt>
     */
    private List _nextbond;

    /**
     * {@link java.util.List} of <tt>JOESmiNode</tt>
     */
    private List _nextnode;

    private Atom _parent;

    /**
     *Constructor for the JOESmiNode object
     *
     * @param atom  Description of the Parameter
     */
    public SMILESNode(Atom atom) {
        _atom = atom;
        _parent = null;
        _nextnode = new Vector();
        _nextbond = new Vector();
    }

    /**
     * Gets the atom attribute of the JOESmiNode object
     *
     * @return   The atom value
     */
    public Atom getAtom() {
        return (_atom);
    }

    /**
     * Gets the nextAtom attribute of the JOESmiNode object
     *
     * @param i  Description of the Parameter
     * @return   The nextAtom value
     */
    public Atom getNextAtom(int i) {
        return ((SMILESNode) _nextnode.get(i)).getAtom();
    }

    /**
     * Gets the nextBond attribute of the JOESmiNode object
     *
     * @param i  Description of the Parameter
     * @return   The nextBond value
     */
    public Bond getNextBond(int i) {
        return ((Bond) _nextbond.get(i));
    }

    /**
     * Gets the nextNode attribute of the JOESmiNode object
     *
     * @param i  Description of the Parameter
     * @return   The nextNode value
     */
    public SMILESNode getNextNode(int i) {
        return ((SMILESNode) _nextnode.get(i));
    }

    /**
     * Gets the parent attribute of the JOESmiNode object
     *
     * @return   The parent value
     */
    public Atom getParent() {
        return (_parent);
    }

    /**
     * Sets the nextNode attribute of the JOESmiNode object
     *
     * @param node  The new nextNode value
     * @param bond  The new nextNode value
     */
    public void setNextNode(SMILESNode node, Bond bond) {
        _nextnode.add(node);
        _nextbond.add(bond);
    }

    /**
     * Sets the parent attribute of the JOESmiNode object
     *
     * @param a  The new parent value
     */
    public void setParent(Atom a) {
        _parent = a;
    }

    /**
     * Description of the Method
     *
     * @return   Description of the Return Value
     */
    public int size() {
        return _nextnode.size();
    }

    /**
     * Description of the Method
     */
    protected void finalize() throws Throwable {
        _nextnode.clear();
        super.finalize();
    }
}

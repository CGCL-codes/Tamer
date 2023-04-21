package joelib2.molecule;

/**
 * Chemical bond interface.
 *
 * @.author       wegner
 * @.wikipedia Chemical bond
 * @.license      GPL
 * @.cvsversion   $Revision: 1.7 $, $Date: 2005/02/17 16:48:36 $
 */
public interface Bond extends Edge {

    void clear();

    Object clone();

    /**
     * Checks if two bonds are equal.
     *
     * Compares order, flags and equality of begin and end atom.
     * AND the bond index number AND the full equality check of
     * the begin and end atom.
     *
     * @param type
     * @return
     */
    boolean equals(Object obj);

    /**
     * Checks if two bonds are equal.
     *
     * Compares order, flags and equality of begin and end atom.
     * When <tt>fullComparison</tt> is set to <tt>false</tt> the bond index number
     * is ignored. The atom are compared also using the <tt>fullComparison</tt> flag.
     *
     * @param type
     * @return
     */
    boolean equals(Bond type, boolean compareAll);

    /**
     *  Gets the beginAtom attribute of the <tt>Bond</tt> object
     *
     * @return    The beginAtom value
     */
    Atom getBegin();

    /**
     * Gets the bond order for this bond.
     *
     * Please remember that you can check the aromaticity also via the
     * aromaticity flag, which is the better way.
     *
     * The Bond.JOE_AROMATIC_BOND_ORDER which can be assigned to the bond
     * order is only needed for some awkward import/export methods.
     *
     * Please remember that the aromaticity typer JOEAromaticTyper.assignAromaticFlags(Molecule)
     * assign ONLY aromaticity flags and NOT the internal aromatic bond order Bond.JOE_AROMATIC_BOND_ORDER.
     *
     * @return    The bond order
     */
    int getBondOrder();

    /**
     *  Gets the endAtom attribute of the <tt>Bond</tt> object
     *
     * @return    The endAtom value
     */
    Atom getEnd();

    int getFlags();

    /**
     *  Gets the nbrAtom attribute of the <tt>Bond</tt> object
     *
     * @param  ptr  Description of the Parameter
     * @return      The nbrAtom value
     */
    Atom getNeighbor(Atom ptr);

    /**
     *  Gets the nbrAtomIdx attribute of the <tt>Bond</tt> object
     *
     * @param  ptr  Description of the Parameter
     * @return      The nbrAtomIdx value
     */
    int getNeighborIndex(Atom ptr);

    /**
     *  Gets the parent attribute of the <tt>Atom</tt> object
     *
     * @return    The parent value
     */
    Molecule getParent();

    boolean hasFlag(int flag);

    /**
     * Calculates the hashcode for a bond.
     *
     * Includes order, flags and equality of begin and end atom.
     * Excludes the bond index number. The begin and end atom are included
     * using their hashcode methods.
     *
     * @param type
     * @return
     */
    int hashCode();

    boolean isBondOrderAromatic();

    /**
     *  Gets the double attribute of the <tt>Bond</tt> object
     *
     * @return    The double value
     */
    boolean isDouble();

    /**
     *  Gets the down attribute of the <tt>Bond</tt> object
     *
     * @return    The down value
     */
    boolean isDown();

    /**
     *  Gets the hash attribute of the <tt>Bond</tt> object
     *
     * @return    The hash value
     */
    boolean isHash();

    /**
     *  Gets the single attribute of the <tt>Bond</tt> object
     *
     * @return    The single value
     */
    boolean isSingle();

    boolean isTriple();

    /**
     *  Gets the up attribute of the <tt>Bond</tt> object
     *
     * @return    The up value
     */
    boolean isUp();

    /**
     *  Gets the wedge attribute of the <tt>Bond</tt> object
     *
     * @return    The wedge value
     */
    boolean isWedge();

    int reHash();

    /**
     *  Description of the Method
     *
     * @param  idx    Description of the Parameter
     * @param  begin  Description of the Parameter
     * @param  end    Description of the Parameter
     * @param  order  Description of the Parameter
     * @param  flags  Description of the Parameter
     */
    void set(int idx, Atom begin, Atom end, int order, int flags);

    /**
     *  Sets the begin attribute of the <tt>Bond</tt> object
     *
     * @param  begin  The new begin value
     */
    void setBegin(Atom begin);

    /**
     *  Sets the bond order for this bond.
     *
     * This causes no change in the aromaticity flag for this bond.
     *
     * Please remember that the aromaticity typer JOEAromaticTyper.assignAromaticFlags(Molecule)
     * assign ONLY aromaticity flags and NOT the internal aromatic bond order Bond.JOE_AROMATIC_BOND_ORDER.
     *
     * @param  order  the new bond order
     */
    void setBondOrder(int order);

    void setBondOrderAromatic();

    /**
     *  Sets the down attribute of the <tt>Bond</tt> object
     */
    void setDown();

    /**
     *  Sets the end attribute of the <tt>Bond</tt> object
     *
     * @param  end  The new end value
     */
    void setEnd(Atom end);

    void setFlags(int flag);

    /**
     *  Sets the parent attribute of the <tt>Bond</tt> object
     *
     * @param  ptr  The new parent value
     */
    void setParent(Molecule ptr);

    /**
     *  The JUnit setup method
     */
    void setUp();

    String toString();
}

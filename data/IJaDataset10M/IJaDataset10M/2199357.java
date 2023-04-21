package org.openscience.cdk.interfaces;

/**
 * Represents a set of Molecules.
 * 
 * @cdk.module  interfaces
 * @cdk.githash
 * @author      egonw
 * @cdk.created 2005-08-25
 */
public interface IMoleculeSet extends IAtomContainerSet {

    /**
     * Adds an IMolecule to this container.
     *
     * @param  molecule  The molecule to be added to this container 
     */
    public void addMolecule(IMolecule molecule);

    /**
     * Adds all molecules in the MoleculeSet to this container.
     *
     * @param  moleculeSet  The MoleculeSet to add
     */
    public void add(IMoleculeSet moleculeSet);

    /**
     * Sets the molecules in the IMoleculeSet, removing previously added
     * IMolecule's.
     * 
     * @param molecules New set of molecules
     * @see             #molecules()
     */
    public void setMolecules(IMolecule[] molecules);

    /**
     * Returns the array of Molecules of this container.
     *
     * @return    The array of Molecules of this container 
     * @see       #setMolecules(IMolecule[])
     */
    public Iterable<IAtomContainer> molecules();

    /**
     * Returns the Molecule at position <code>number</code> in the
     * container.
     *
     * @param  number  The position of the Molecule to be returned. 
     * @return         The Molecule at position <code>number</code> . 
     */
    public IMolecule getMolecule(int number);

    /**
     * Returns the number of Molecules in this Container.
     *
     * @return     The number of Molecules in this Container
     */
    public int getMoleculeCount();
}

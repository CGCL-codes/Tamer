package org.openscience.cdk.interfaces;

/**
 * Represents a set of Molecules.
 * 
 * @cdk.module  interfaces
 * @author      egonw
 * @cdk.created 2005-08-25
 */
public interface SetOfMolecules extends SetOfAtomContainers {

    /**
     *  Adds an molecule to this container.
     *
     * @param  molecule  The molecule to be added to this container 
     */
    public void addMolecule(Molecule molecule);

    /**
     *  Adds all molecules in the SetOfMolecules to this container.
     *
     * @param  moleculeSet  The SetOfMolecules 
     */
    public void add(SetOfMolecules moleculeSet);

    public void setMolecules(Molecule[] molecules);

    /**
     *  Returns the array of Molecules of this container.
     *
     * @return    The array of Molecules of this container 
     */
    public Molecule[] getMolecules();

    /**
     * Returns the Molecule at position <code>number</code> in the
     * container.
     *
     * @param  number  The position of the Molecule to be returned. 
     * @return         The Molecule at position <code>number</code> . 
     */
    public Molecule getMolecule(int number);

    /**
     * Returns the number of Molecules in this Container.
     *
     * @return     The number of Molecules in this Container
     */
    public int getMoleculeCount();
}

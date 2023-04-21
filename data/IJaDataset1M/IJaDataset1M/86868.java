package org.openscience.cdk.interfaces;

/** 
 * An object containig multiple MoleculeSet and 
 * the other lower level concepts like rings, sequences, 
 * fragments, etc.
 *
 * @cdk.module interfaces
 * @cdk.githash
 */
public interface IChemModel extends IChemObject {

    /**
	 * Returns the MoleculeSet of this ChemModel.
	 *
	 * @return   The MoleculeSet of this ChemModel
     * @see      #setMoleculeSet
	 */
    public IMoleculeSet getMoleculeSet();

    /**
	 * Sets the MoleculeSet of this ChemModel.
	 *
	 * @param   setOfMolecules  the content of this model
     * @see      #getMoleculeSet
	 */
    public void setMoleculeSet(IMoleculeSet setOfMolecules);

    /**
	 * Returns the RingSet of this ChemModel.
	 *
	 * @return the ringset of this model
     * @see      #setRingSet
	 */
    public IRingSet getRingSet();

    /**
	 * Sets the RingSet of this ChemModel.
	 *
	 * @param   ringSet         the content of this model
     * @see      #getRingSet
	 */
    public void setRingSet(IRingSet ringSet);

    /**
     * Gets the Crystal contained in this ChemModel.
     *
     * @return The crystal in this model
     * @see      #setCrystal
     */
    public ICrystal getCrystal();

    /**
     * Sets the Crystal contained in this ChemModel.
     *
     * @param   crystal  the Crystal to store in this model
     * @see      #getCrystal
     */
    public void setCrystal(ICrystal crystal);

    /**
     * Gets the ReactionSet contained in this ChemModel.
     *
     * @return The ReactionSet in this model
     * @see      #setReactionSet
     */
    public IReactionSet getReactionSet();

    /**
     * Sets the ReactionSet contained in this ChemModel.
     *
     * @param sor the ReactionSet to store in this model
     * @see       #getReactionSet
     */
    public void setReactionSet(IReactionSet sor);
}

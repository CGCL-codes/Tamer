package org.openscience.cdk.interfaces;

/**
 * A Single Electron is an orbital which is occupied by only one electron.
 * A radical in CDK is represented by an AtomContainer that contains an Atom
 * and a SingleElectron type ElectronContainer:
 * <pre>
 *   AtomContainer radical = new AtomContainer();
 *   Atom carbon = new Atom("C");
 *   carbon.setImplicitHydrogens(3);
 *   radical.addElectronContainer(new SingleElectron(carbon));
 * </pre> 
 *
 * @cdk.module interfaces
 * @cdk.githash
 *
 * @cdk.keyword radical
 * @cdk.keyword electron, unpaired
 */
public interface ISingleElectron extends IElectronContainer {

    /**
     * Returns the associated Atom.
     *
     * @return the associated Atom.
     * @see    #setAtom
	 */
    public IAtom getAtom();

    /**
	 * Sets the associated Atom.
	 *
	 * @param atom the Atom this SingleElectron will be associated with
     * @see    #getAtom
	 */
    public void setAtom(IAtom atom);

    /**
     * Returns true if the given atom participates in this SingleElectron.
     *
     * @param   atom  The atom to be tested if it participates in this bond
     * @return     true if this SingleElectron is associated with the atom
     */
    public boolean contains(IAtom atom);
}

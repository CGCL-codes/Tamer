package org.openscience.cdk.interfaces;

import java.util.Collection;

/**
 * Subclass of Molecule to store Polymer specific attributes that a Polymer has.
 *
 * @cdk.module  interfaces
 * @cdk.githash
 *
 * @author      Edgar Luttmann <edgar@uni-paderborn.de>
 * @author      Martin Eklund <martin.eklund@farmbio.uu.se>
 * @cdk.created 2001-08-06
 * @cdk.keyword polymer
 */
public interface IPolymer extends IMolecule {

    /**
	 * Adds the atom oAtom without specifying a Monomer. Therefore the
	 * atom to this AtomContainer, but not to a certain Monomer (intended
	 * e.g. for HETATMs).
	 *
	 * @param oAtom  The atom to add
	 */
    public void addAtom(IAtom oAtom);

    /**
	 * Adds the atom oAtom to a specified Monomer.
	 *
	 * @param oAtom  The atom to add
	 * @param oMonomer  The monomer the atom belongs to
	 */
    public void addAtom(IAtom oAtom, IMonomer oMonomer);

    /**
	 * Return the number of monomers present in the Polymer.
	 *
	 * @return number of monomers
	 */
    public int getMonomerCount();

    /**
	 * Retrieve a Monomer object by specifying its name.
	 *
	 * @param cName  The name of the monomer to look for
	 * @return The Monomer object which was asked for
	 */
    public IMonomer getMonomer(String cName);

    /**
	 * Returns a collection of the names of all <code>Monomer</code>s in this
	 * polymer.
	 *
	 * @return a <code>Collection</code> of all the monomer names.
	 */
    public Collection<String> getMonomerNames();

    /**
	 * Removes a particular monomer, specified by its name.
	 * 
	 * @param name The name of the monomer to be removed
	 */
    public void removeMonomer(String name);
}

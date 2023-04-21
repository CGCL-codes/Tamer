package org.openscience.cdk.interfaces;

import java.util.Vector;
import java.util.List;

/**
 * Maintains a set of Ring objects.
 *
 * @cdk.module  interfaces
 *
 * @cdk.keyword ring, set of
 */
public interface RingSet extends List {

    /**
	 * Checks - and returns 'true' - if a certain ring is already
	 * stored in this setOfRings.
	 *
	 * @param   newRing  The ring to be tested if it is already stored here
	 * @return     true if it is already stored
	 */
    public boolean ringAlreadyInSet(Ring newRing);

    /**
	 * Returns a vector of all rings that this bond is part of.
	 *
	 * @param   bond  The bond to be checked
	 * @return   A vector of all rings that this bond is part of  
	 */
    public Vector getRings(Bond bond);

    /**
	 * Returns a vector of all rings that this atom is part of.
	 *
	 * @param   atom  The atom to be checked
	 * @return   A vector of all rings that this bond is part of  
	 */
    public RingSet getRings(Atom atom);

    /**
	 * Returns all the rings in the RingSet that share
	 * one or more atoms with a given ring.
	 *
	 * @param   ring  A ring with which all return rings must share one or more atoms
	 * @return  All the rings that share one or more atoms with a given ring.   
	 */
    public Vector getConnectedRings(Ring ring);

    /**
	 * Adds all rings of another RingSet if they are not allready part of this ring set.
	 *
	 * @param   ringSet  the ring set to be united with this one.
	 */
    public void add(RingSet ringSet);

    /**
	 * True, if at least one of the rings in the ringset cotains
	 * the given atom.
	 *
     * @param  atom Atom to check
	 * @return      true, if the ringset contains the atom
	 */
    public boolean contains(Atom atom);
}

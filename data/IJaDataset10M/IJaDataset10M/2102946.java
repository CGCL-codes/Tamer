package org.gjt.universe;

import java.io.Serializable;

/**
 * The base class of all database items. The database items are assumed to 
 * have a small set of basic attributes, which are accessible through the 
 * interface. For some of the subtypes, the Owner and Location attributes are 
 * nonsensical, and standard fail-safe methods have been provided.
 */
public abstract class DBItem implements Serializable {

    public abstract VectorDisplayReturn specificDisplayDebug();

    /**
	 * See getID() in the subclasses for a typed version.
	 */
    public abstract Index getIndex();

    public abstract String getName();

    public CivID getOwner() {
        return new CivID();
    }

    /**
	 * @throws NotLocatableException if the DBItem type is a non-localized
	 * phenomenon.
	 */
    public Location getLocation() throws NotLocatableException {
        throw new NotLocatableException();
    }
}

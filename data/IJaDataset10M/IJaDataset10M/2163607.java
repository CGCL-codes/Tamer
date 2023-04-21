package org.nakedobjects.metamodel.facets.object.callbacks;

/**
 * Represents the mechanism to inform the object that it is about to be persisted to the object store for the
 * first time.
 * 
 * <p>
 * In the standard Naked Objects Programming Model, this is represented by a <tt>saving</tt> method. Called
 * only if the object is known to be in a valid state.
 * 
 * <p>
 * 
 * @see PersistedCallbackFacet
 */
public interface PersistingCallbackFacet extends CallbackFacet {
}

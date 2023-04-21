package org.nakedobjects.metamodel.facets.object.notpersistable;

import org.nakedobjects.metamodel.facets.SingleValueFacet;
import org.nakedobjects.metamodel.interactions.DisablingInteractionAdvisor;

/**
 * Indicates that the instances of this class are not persistable either by the user (through the viewer) or
 * at all (either by the user or programmatically).
 * 
 * <p>
 * In the standard Naked Objects Programming Model, typically corresponds to applying the
 * <tt>@NotPersistable</tt> annotation at the class level.
 */
public interface NotPersistableFacet extends SingleValueFacet, DisablingInteractionAdvisor {

    public InitiatedBy value();
}

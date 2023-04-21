package org.nakedobjects.metamodel.specloader.internal.peer;

import org.nakedobjects.applib.Identifier;
import org.nakedobjects.commons.debug.DebugString;
import org.nakedobjects.commons.filters.Filter;
import org.nakedobjects.metamodel.facets.Facet;
import org.nakedobjects.metamodel.spec.NakedObjectSpecification;

public abstract class NakedObjectAssociationPeerDecorator implements NakedObjectAssociationPeer {

    private final NakedObjectAssociationPeer decorated;

    public NakedObjectAssociationPeerDecorator(final NakedObjectAssociationPeer decorated) {
        this.decorated = decorated;
    }

    public void debugData(final DebugString debugString) {
        decorated.debugData(debugString);
    }

    public Class<? extends Facet>[] getFacetTypes() {
        return decorated.getFacetTypes();
    }

    public Facet[] getFacets(final Filter<Facet> filter) {
        return decorated.getFacets(filter);
    }

    public Identifier getIdentifier() {
        return decorated.getIdentifier();
    }

    public NakedObjectSpecification getSpecification() {
        return decorated.getSpecification();
    }
}

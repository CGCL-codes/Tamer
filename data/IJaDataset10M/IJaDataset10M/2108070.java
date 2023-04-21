package org.nakedobjects.metamodel.facets.actions.prototype;

import org.nakedobjects.metamodel.facets.Facet;
import org.nakedobjects.metamodel.facets.FacetHolder;
import org.nakedobjects.metamodel.facets.MarkerFacetAbstract;

public abstract class PrototypeFacetAbstract extends MarkerFacetAbstract implements PrototypeFacet {

    public static Class<? extends Facet> type() {
        return PrototypeFacet.class;
    }

    public PrototypeFacetAbstract(final FacetHolder holder) {
        super(type(), holder);
    }
}

package org.nakedobjects.noa.facets.object.value;

import org.nakedobjects.noa.facets.FacetHolder;
import org.nakedobjects.noa.facets.MarkerFacetAbstract;
import org.nakedobjects.noa.facets.object.aggregated.AggregatedFacet;

/**
 */
public class AggregatedFacetViaValueSemantics extends MarkerFacetAbstract {

    public AggregatedFacetViaValueSemantics(FacetHolder holder) {
        super(AggregatedFacet.class, holder);
    }
}

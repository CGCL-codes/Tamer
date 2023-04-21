package org.starobjects.jpa.metamodel.facets;

import javax.persistence.FetchType;
import org.nakedobjects.metamodel.facets.Facet;
import org.nakedobjects.metamodel.facets.FacetAbstract;
import org.nakedobjects.metamodel.facets.FacetHolder;

public abstract class JpaFetchTypeFacetAbstract extends FacetAbstract implements JpaFetchTypeFacet {

    public static Class<? extends Facet> type() {
        return JpaFetchTypeFacet.class;
    }

    private final FetchType fetchType;

    public JpaFetchTypeFacetAbstract(FacetHolder holder, final FetchType fetchType) {
        super(type(), holder, false);
        this.fetchType = fetchType;
    }

    public FetchType getFetchType() {
        return fetchType;
    }
}

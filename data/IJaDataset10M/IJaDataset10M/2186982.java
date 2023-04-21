package org.starobjects.jpa.metamodel.facets.object.discriminator;

import org.nakedobjects.metamodel.facets.Facet;
import org.nakedobjects.metamodel.facets.FacetHolder;
import org.nakedobjects.metamodel.facets.SingleStringValueFacetAbstract;

public abstract class JpaDiscriminatorValueFacetAbstract extends SingleStringValueFacetAbstract implements JpaDiscriminatorValueFacet {

    public static Class<? extends Facet> type() {
        return JpaDiscriminatorValueFacet.class;
    }

    public JpaDiscriminatorValueFacetAbstract(final String value, final FacetHolder holder) {
        super(type(), holder, value);
    }
}

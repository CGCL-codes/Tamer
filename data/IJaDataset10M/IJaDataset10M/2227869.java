package org.nakedobjects.metamodel.facets.help;

import org.nakedobjects.metamodel.facets.Facet;
import org.nakedobjects.metamodel.facets.FacetHolder;
import org.nakedobjects.metamodel.facets.SingleStringValueFacetAbstract;

public abstract class HelpFacetAbstract extends SingleStringValueFacetAbstract implements HelpFacet {

    public static Class<? extends Facet> type() {
        return HelpFacet.class;
    }

    public HelpFacetAbstract(final String value, final FacetHolder holder) {
        super(type(), holder, value);
    }
}

package org.nakedobjects.metamodel.facets.properties.modify;

import org.nakedobjects.metamodel.facets.Facet;
import org.nakedobjects.metamodel.facets.FacetAbstract;
import org.nakedobjects.metamodel.facets.FacetHolder;

public abstract class PropertySetterFacetAbstract extends FacetAbstract implements PropertySetterFacet {

    public static Class<? extends Facet> type() {
        return PropertySetterFacet.class;
    }

    public PropertySetterFacetAbstract(final FacetHolder holder) {
        super(type(), holder, false);
    }
}

package org.nakedobjects.noa.facets.properties.modify;

import org.nakedobjects.noa.facets.FacetAbstract;
import org.nakedobjects.noa.facets.FacetHolder;

public abstract class PropertyClearFacetAbstract extends FacetAbstract implements PropertyClearFacet {

    public static Class type() {
        return PropertyClearFacet.class;
    }

    public PropertyClearFacetAbstract(final FacetHolder holder) {
        super(type(), holder);
    }
}

package org.nakedobjects.noa.facets.object.callbacks;

import org.nakedobjects.noa.facets.FacetHolder;

public abstract class UpdatingCallbackFacetAbstract extends CallbackFacetAbstract implements UpdatingCallbackFacet {

    public static Class type() {
        return UpdatingCallbackFacet.class;
    }

    public UpdatingCallbackFacetAbstract(final FacetHolder holder) {
        super(type(), holder);
    }
}

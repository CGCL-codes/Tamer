package org.nakedobjects.noa.facets.object.dirty;

import org.nakedobjects.noa.facets.FacetHolder;

public abstract class MarkDirtyObjectFacetAbstract extends DirtyObjectFacetAbstract implements MarkDirtyObjectFacet {

    public static Class type() {
        return MarkDirtyObjectFacet.class;
    }

    public MarkDirtyObjectFacetAbstract(final FacetHolder holder) {
        super(type(), holder);
    }
}

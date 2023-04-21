package org.nakedobjects.metamodel.facets.object.value;

import org.nakedobjects.metamodel.facets.FacetHolder;
import org.nakedobjects.metamodel.facets.When;
import org.nakedobjects.metamodel.facets.object.immutable.ImmutableFacetImpl;

public class ImmutableFacetViaValueSemantics extends ImmutableFacetImpl {

    public ImmutableFacetViaValueSemantics(final FacetHolder holder) {
        super(When.ALWAYS, holder);
    }
}

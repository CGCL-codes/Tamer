package org.nakedobjects.metamodel.facets.disable;

import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.facets.FacetHolder;
import org.nakedobjects.metamodel.facets.When;

public class DisabledFacetNever extends DisabledFacetAbstract {

    public DisabledFacetNever(final FacetHolder holder) {
        super(When.NEVER, holder);
    }

    /**
     * Always returns <tt>null</tt>.
     */
    public String disabledReason(final NakedObject target) {
        return null;
    }

    @Override
    public boolean isNoop() {
        return true;
    }
}

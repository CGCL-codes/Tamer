package org.nakedobjects.metamodel.facets.disable;

import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.facets.FacetHolder;
import org.nakedobjects.metamodel.facets.When;

public class DisabledFacetImpl extends DisabledFacetAbstract {

    public DisabledFacetImpl(final When value, final FacetHolder holder) {
        super(value, holder);
    }

    public String disabledReason(final NakedObject targetAdapter) {
        if (value() == When.ALWAYS) {
            return "Always disabled";
        } else if (value() == When.NEVER) {
            return null;
        }
        if (targetAdapter == null) {
            return null;
        }
        if (value() == When.UNTIL_PERSISTED) {
            return targetAdapter.isTransient() ? "Disabled until persisted" : null;
        } else if (value() == When.ONCE_PERSISTED) {
            return targetAdapter.isPersistent() ? "Disabled once persisted" : null;
        }
        return null;
    }
}

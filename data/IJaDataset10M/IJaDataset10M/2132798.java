package org.nakedobjects.noa.facets.hide;

import org.nakedobjects.noa.adapter.NakedObject;
import org.nakedobjects.noa.facets.FacetHolder;
import org.nakedobjects.noa.facets.When;

public class HiddenFacetImpl extends HiddenFacetAbstract {

    public HiddenFacetImpl(final When value, final FacetHolder holder) {
        super(value, holder);
    }

    public String hiddenReason(final NakedObject target) {
        if (value() == When.ALWAYS) {
            return "Always hidden";
        } else if (value() == When.NEVER) {
            return null;
        }
        if (target == null) {
            return null;
        }
        if (value() == When.UNTIL_PERSISTED) {
            return target.getResolveState().isTransient() ? "Hidden until persisted" : null;
        } else if (value() == When.ONCE_PERSISTED) {
            return target.getResolveState().isPersistent() ? "Hidden once persisted" : null;
        }
        return null;
    }
}

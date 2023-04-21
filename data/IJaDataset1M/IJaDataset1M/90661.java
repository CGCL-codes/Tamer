package org.nakedobjects.metamodel.facets.actions.defaults;

import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.facets.FacetHolder;

public class ActionParameterDefaultsFacetNone extends ActionParameterDefaultsFacetAbstract {

    public ActionParameterDefaultsFacetNone(final FacetHolder holder) {
        super(holder);
    }

    public Object getDefault(final NakedObject inObject) {
        return null;
    }

    @Override
    public boolean isNoop() {
        return true;
    }
}

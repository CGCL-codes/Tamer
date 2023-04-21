package org.nakedobjects.metamodel.facets.actions.choices;

import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.facets.FacetHolder;

public class ActionParameterChoicesFacetNone extends ActionParameterChoicesFacetAbstract {

    public ActionParameterChoicesFacetNone(final FacetHolder holder) {
        super(holder);
    }

    public Object[] getChoices(final NakedObject inObject) {
        return new NakedObject[0];
    }

    @Override
    public boolean isNoop() {
        return true;
    }
}

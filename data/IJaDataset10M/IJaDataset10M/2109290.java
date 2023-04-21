package org.nakedobjects.noa.facets.actions.choices;

import org.nakedobjects.noa.adapter.NakedObject;
import org.nakedobjects.noa.facets.Facet;

/**
 * Obtain choices for each of the parameters of the action.
 * 
 * <p>
 * In the standard Naked Objects Programming Model, corresponds to invoking the <tt>choicesXxx</tt> support
 * method for an action.
 */
public interface ActionParameterChoicesFacet extends Facet {

    public Object[] getOptions(NakedObject inObject);
}

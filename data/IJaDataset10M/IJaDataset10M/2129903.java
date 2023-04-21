package org.nakedobjects.metamodel.facets.naming.describedas;

import org.nakedobjects.metamodel.facets.FacetHolder;

/**
 * Has a description of the empty string.
 */
public class DescribedAsFacetNone extends DescribedAsFacetAbstract {

    public DescribedAsFacetNone(final FacetHolder holder) {
        super("", holder);
    }

    @Override
    public boolean isNoop() {
        return true;
    }
}

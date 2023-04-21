package org.nakedobjects.noa.facets.object.typicallength;

import org.nakedobjects.noa.facets.FacetHolder;

public class TypicalLengthFacetZero extends TypicalLengthFacetAbstract {

    public TypicalLengthFacetZero(final FacetHolder holder) {
        super(0, holder);
    }

    @Override
    public boolean isNoop() {
        return true;
    }
}

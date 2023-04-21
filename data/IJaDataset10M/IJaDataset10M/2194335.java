package org.nakedobjects.metamodel.facets.propparam.typicallength;

import org.nakedobjects.metamodel.facets.FacetHolder;
import org.nakedobjects.metamodel.facets.propparam.multiline.MultiLineFacet;

public class TypicalLengthFacetDerivedFromType extends TypicalLengthFacetAbstract {

    private final TypicalLengthFacet typicalLengthFacet;

    public TypicalLengthFacetDerivedFromType(final TypicalLengthFacet typicalLengthFacet, final FacetHolder holder) {
        super(holder, true);
        this.typicalLengthFacet = typicalLengthFacet;
    }

    public int value() {
        final MultiLineFacet facet = getFacetHolder().getFacet(MultiLineFacet.class);
        return facet.numberOfLines() * typicalLengthFacet.value();
    }
}

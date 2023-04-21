package org.nakedobjects.metamodel.facets.propparam.typicallength;

import org.nakedobjects.metamodel.facets.Facet;
import org.nakedobjects.metamodel.facets.FacetAbstract;
import org.nakedobjects.metamodel.facets.FacetHolder;

public abstract class TypicalLengthFacetAbstract extends FacetAbstract implements TypicalLengthFacet {

    public static Class<? extends Facet> type() {
        return TypicalLengthFacet.class;
    }

    public TypicalLengthFacetAbstract(final FacetHolder holder, boolean derived) {
        super(type(), holder, false);
    }

    public abstract int value();

    @Override
    protected String toStringValues() {
        final int val = value();
        return val == 0 ? "default" : String.valueOf(val);
    }
}

package org.nakedobjects.noa.facets;

public abstract class SingleStringValueFacetAbstract extends FacetAbstract implements SingleStringValueFacet {

    private final String value;

    public SingleStringValueFacetAbstract(final Class facetType, final FacetHolder holder, final String value) {
        super(facetType, holder);
        this.value = value;
    }

    public String value() {
        return value;
    }

    @Override
    protected String toStringValues() {
        if (value == null) {
            return "null";
        } else {
            return "\"" + value + "\"";
        }
    }
}

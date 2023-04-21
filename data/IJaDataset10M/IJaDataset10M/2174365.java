package org.nakedobjects.nof.reflect.java.value;

import org.nakedobjects.noa.adapter.value.DateValueFacet;
import org.nakedobjects.noa.facets.FacetHolder;
import org.nakedobjects.noa.facets.MethodRemover;
import java.util.Date;

public class JavaUtilDateValueTypeFacetFactory extends ValueUsingValueSemanticsProviderFacetFactory {

    public JavaUtilDateValueTypeFacetFactory() {
        super(DateValueFacet.class);
    }

    @Override
    public boolean process(final Class clazz, final MethodRemover methodRemover, final FacetHolder holder) {
        if (clazz != Date.class) {
            return false;
        }
        addFacets(new JavaUtilDateValueSemanticsProvider(holder));
        return true;
    }
}

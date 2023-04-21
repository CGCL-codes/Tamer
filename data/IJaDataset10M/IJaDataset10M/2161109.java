package org.nakedobjects.metamodel.value;

import org.nakedobjects.metamodel.facets.FacetHolder;
import org.nakedobjects.metamodel.facets.MethodRemover;
import org.nakedobjects.metamodel.facets.value.DateValueFacet;

public class JavaSqlTimeValueTypeFacetFactory extends ValueUsingValueSemanticsProviderFacetFactory {

    public JavaSqlTimeValueTypeFacetFactory() {
        super(DateValueFacet.class);
    }

    @Override
    public boolean process(final Class<?> type, final MethodRemover methodRemover, final FacetHolder holder) {
        if (type != java.sql.Time.class) {
            return false;
        }
        addFacets(new JavaSqlTimeValueSemanticsProvider(holder, getConfiguration(), getSpecificationLoader(), getRuntimeContext()));
        return true;
    }
}

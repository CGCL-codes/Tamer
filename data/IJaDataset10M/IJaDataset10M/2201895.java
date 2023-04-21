package org.nakedobjects.metamodel.value;

import org.nakedobjects.applib.adapters.EncoderDecoder;
import org.nakedobjects.applib.adapters.Parser;
import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.config.NakedObjectConfiguration;
import org.nakedobjects.metamodel.facets.FacetHolder;
import org.nakedobjects.metamodel.facets.properties.defaults.PropertyDefaultFacet;
import org.nakedobjects.metamodel.runtimecontext.RuntimeContext;
import org.nakedobjects.metamodel.specloader.SpecificationLoader;

public class IntPrimitiveValueSemanticsProvider extends IntValueSemanticsProviderAbstract implements PropertyDefaultFacet {

    private static final Object DEFAULT_VALUE = Integer.valueOf(0);

    static final Class<?> adaptedClass() {
        return int.class;
    }

    /**
     * Required because implementation of {@link Parser} and {@link EncoderDecoder}.
     */
    public IntPrimitiveValueSemanticsProvider() {
        this(null, null, null, null);
    }

    public IntPrimitiveValueSemanticsProvider(final FacetHolder holder, final NakedObjectConfiguration configuration, final SpecificationLoader specificationLoader, final RuntimeContext runtimeContext) {
        super(holder, adaptedClass(), DEFAULT_VALUE, configuration, specificationLoader, runtimeContext);
    }

    public NakedObject getDefault(final NakedObject inObject) {
        return createAdapter(int.class, Integer.valueOf(0));
    }
}

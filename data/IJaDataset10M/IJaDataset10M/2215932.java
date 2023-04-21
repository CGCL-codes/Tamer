package org.nakedobjects.metamodel.value;

import org.nakedobjects.applib.adapters.EncoderDecoder;
import org.nakedobjects.applib.adapters.Parser;
import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.config.NakedObjectConfiguration;
import org.nakedobjects.metamodel.facets.FacetHolder;
import org.nakedobjects.metamodel.facets.properties.defaults.PropertyDefaultFacet;
import org.nakedobjects.metamodel.runtimecontext.RuntimeContext;
import org.nakedobjects.metamodel.specloader.SpecificationLoader;

public class BooleanPrimitiveValueSemanticsProvider extends BooleanValueSemanticsProviderAbstract implements PropertyDefaultFacet {

    private static final Object DEFAULT_VALUE = Boolean.FALSE;

    static final Class<?> adaptedClass() {
        return boolean.class;
    }

    /**
     * Required because implementation of {@link Parser} and {@link EncoderDecoder}.
     */
    public BooleanPrimitiveValueSemanticsProvider() {
        this(null, null, null, null);
    }

    public BooleanPrimitiveValueSemanticsProvider(final FacetHolder holder, final NakedObjectConfiguration configuration, final SpecificationLoader specificationLoader, final RuntimeContext runtimeContext) {
        super(holder, adaptedClass(), DEFAULT_VALUE, configuration, specificationLoader, runtimeContext);
    }

    public NakedObject getDefault(final NakedObject inObject) {
        return createAdapter(boolean.class, Boolean.FALSE);
    }

    public void reset(final NakedObject object) {
        object.replacePojo(Boolean.FALSE);
    }

    public void set(final NakedObject object) {
        object.replacePojo(Boolean.TRUE);
    }

    public void toggle(final NakedObject object) {
        final boolean current = ((Boolean) object.getObject()).booleanValue();
        final boolean toggled = !current;
        object.replacePojo(Boolean.valueOf(toggled));
    }
}

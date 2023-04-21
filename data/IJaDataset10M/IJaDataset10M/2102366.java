package org.nakedobjects.metamodel.facets.object.encodeable;

import org.nakedobjects.applib.annotation.Encodable;
import org.nakedobjects.commons.lang.StringUtils;
import org.nakedobjects.metamodel.config.NakedObjectConfiguration;
import org.nakedobjects.metamodel.config.NakedObjectConfigurationAware;
import org.nakedobjects.metamodel.facets.FacetHolder;
import org.nakedobjects.metamodel.facets.FacetUtil;
import org.nakedobjects.metamodel.facets.MethodRemover;
import org.nakedobjects.metamodel.java5.AnnotationBasedFacetFactoryAbstract;
import org.nakedobjects.metamodel.runtimecontext.RuntimeContext;
import org.nakedobjects.metamodel.runtimecontext.RuntimeContextAware;
import org.nakedobjects.metamodel.spec.feature.NakedObjectFeatureType;

public class EncodableAnnotationFacetFactory extends AnnotationBasedFacetFactoryAbstract implements NakedObjectConfigurationAware, RuntimeContextAware {

    private NakedObjectConfiguration configuration;

    private RuntimeContext runtimeContext;

    public EncodableAnnotationFacetFactory() {
        super(NakedObjectFeatureType.OBJECTS_ONLY);
    }

    @Override
    public boolean process(final Class<?> cls, final MethodRemover methodRemover, final FacetHolder holder) {
        return FacetUtil.addFacet(create(cls, holder));
    }

    /**
     * Returns a {@link EncodableFacet} implementation.
     */
    private EncodableFacet create(final Class<?> cls, final FacetHolder holder) {
        final Encodable annotation = getAnnotation(cls, Encodable.class);
        if (annotation != null) {
            final EncodableFacetAnnotation facet = new EncodableFacetAnnotation(cls, getNakedObjectConfiguration(), holder, getRuntimeContext());
            if (facet.isValid()) {
                return facet;
            }
        }
        final String encoderDecoderName = EncoderDecoderUtil.encoderDecoderNameFromConfiguration(cls, getNakedObjectConfiguration());
        if (!StringUtils.isEmpty(encoderDecoderName)) {
            final EncodableFacetFromConfiguration facet = new EncodableFacetFromConfiguration(encoderDecoderName, holder, getRuntimeContext());
            if (facet.isValid()) {
                return facet;
            }
        }
        return null;
    }

    public NakedObjectConfiguration getNakedObjectConfiguration() {
        return configuration;
    }

    public void setNakedObjectConfiguration(final NakedObjectConfiguration configuration) {
        this.configuration = configuration;
    }

    private RuntimeContext getRuntimeContext() {
        return runtimeContext;
    }

    public void setRuntimeContext(final RuntimeContext runtimeContext) {
        this.runtimeContext = runtimeContext;
    }
}

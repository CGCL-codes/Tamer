package org.nakedobjects.metamodel.facets.actions.prototype;

import java.lang.reflect.Method;
import org.nakedobjects.applib.annotation.Prototype;
import org.nakedobjects.metamodel.facets.FacetHolder;
import org.nakedobjects.metamodel.facets.FacetUtil;
import org.nakedobjects.metamodel.facets.MethodRemover;
import org.nakedobjects.metamodel.java5.AnnotationBasedFacetFactoryAbstract;
import org.nakedobjects.metamodel.spec.feature.NakedObjectFeatureType;

public class PrototypeAnnotationFacetFactory extends AnnotationBasedFacetFactoryAbstract {

    public PrototypeAnnotationFacetFactory() {
        super(NakedObjectFeatureType.ACTIONS_ONLY);
    }

    @Override
    public boolean process(Class<?> cls, final Method method, final MethodRemover methodRemover, final FacetHolder holder) {
        final Prototype annotation = getAnnotation(method, Prototype.class);
        return FacetUtil.addFacet(create(annotation, holder));
    }

    private PrototypeFacet create(final Prototype annotation, final FacetHolder holder) {
        return annotation == null ? null : new PrototypeFacetAnnotation(holder);
    }
}

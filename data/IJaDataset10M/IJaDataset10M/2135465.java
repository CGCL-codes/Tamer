package org.nakedobjects.metamodel.facets.propparam.validate.mask;

import java.lang.reflect.Method;
import org.nakedobjects.applib.annotation.Mask;
import org.nakedobjects.metamodel.facets.FacetHolder;
import org.nakedobjects.metamodel.facets.FacetUtil;
import org.nakedobjects.metamodel.facets.MethodRemover;
import org.nakedobjects.metamodel.facets.object.ident.title.TitleFacet;
import org.nakedobjects.metamodel.java5.AnnotationBasedFacetFactoryAbstract;
import org.nakedobjects.metamodel.spec.NakedObjectSpecification;
import org.nakedobjects.metamodel.spec.feature.NakedObjectFeatureType;

public class MaskAnnotationFacetFactory extends AnnotationBasedFacetFactoryAbstract {

    public MaskAnnotationFacetFactory() {
        super(NakedObjectFeatureType.OBJECTS_PROPERTIES_AND_PARAMETERS);
    }

    /**
     * In readiness for supporting <tt>@Value</tt> in the future.
     */
    @Override
    public boolean process(final Class<?> cls, final MethodRemover methodRemover, final FacetHolder holder) {
        final Mask annotation = getAnnotation(cls, Mask.class);
        return FacetUtil.addFacet(createMaskFacet(annotation, holder));
    }

    @Override
    public boolean process(Class<?> cls, final Method method, final MethodRemover methodRemover, final FacetHolder holder) {
        if (method.getReturnType() == void.class) {
            return false;
        }
        final Mask annotation = getAnnotation(method, Mask.class);
        return addMaskFacetAndCorrespondingTitleFacet(holder, annotation, method.getReturnType());
    }

    @Override
    public boolean processParams(final Method method, final int paramNum, final FacetHolder holder) {
        final Class<?>[] parameterTypes = method.getParameterTypes();
        if (paramNum >= parameterTypes.length) {
            return false;
        }
        final java.lang.annotation.Annotation[] parameterAnnotations = getParameterAnnotations(method)[paramNum];
        for (int i = 0; i < parameterAnnotations.length; i++) {
            if (parameterAnnotations[i] instanceof Mask) {
                final Mask annotation = (Mask) parameterAnnotations[i];
                return addMaskFacetAndCorrespondingTitleFacet(holder, annotation, parameterTypes[i]);
            }
        }
        return false;
    }

    private MaskFacet createMaskFacet(final Mask annotation, final FacetHolder holder) {
        return annotation != null ? new MaskFacetAnnotation(annotation.value(), null, holder) : null;
    }

    private boolean addMaskFacetAndCorrespondingTitleFacet(final FacetHolder holder, final Mask annotation, Class<?> cls) {
        final MaskFacet maskFacet = createMaskFacet(annotation, holder);
        if (maskFacet == null) {
            return false;
        }
        FacetUtil.addFacet(maskFacet);
        NakedObjectSpecification type = getSpecificationLoader().loadSpecification(cls);
        final TitleFacet underlyingTitleFacet = type.getFacet(TitleFacet.class);
        if (underlyingTitleFacet != null) {
            final TitleFacet titleFacet = new TitleFacetBasedOnMask(maskFacet, underlyingTitleFacet);
            FacetUtil.addFacet(titleFacet);
        }
        return true;
    }
}

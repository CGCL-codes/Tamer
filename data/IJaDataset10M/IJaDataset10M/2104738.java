package org.starobjects.jpa.metamodel.facets.object.entity;

import org.hibernate.annotations.Entity;
import org.nakedobjects.metamodel.facets.FacetHolder;
import org.nakedobjects.metamodel.facets.FacetUtil;
import org.nakedobjects.metamodel.facets.MethodRemover;
import org.nakedobjects.metamodel.java5.AnnotationBasedFacetFactoryAbstract;
import org.nakedobjects.metamodel.spec.feature.NakedObjectFeatureType;

public class HibernateEntityAnnotationFacetFactory extends AnnotationBasedFacetFactoryAbstract {

    public HibernateEntityAnnotationFacetFactory() {
        super(NakedObjectFeatureType.OBJECTS_ONLY);
    }

    @Override
    public boolean process(final Class<?> cls, final MethodRemover methodRemover, final FacetHolder holder) {
        final Entity annotation = getAnnotation(cls, Entity.class);
        if (annotation == null) {
            return false;
        }
        boolean mutable = annotation.mutable();
        FacetUtil.addFacet(new HibernateEntityFacetAnnotation(holder));
        if (!mutable) {
            FacetUtil.addFacet(new ImmutableFacetDerivedFromHibernateEntityAnnotation(holder));
        }
        return true;
    }
}

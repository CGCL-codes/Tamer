package org.apache.isis.extensions.jpa.metamodel.facets.object.namedquery;

import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import org.apache.isis.metamodel.facets.FacetHolder;
import org.apache.isis.metamodel.facets.FacetUtil;
import org.apache.isis.metamodel.facets.MethodRemover;
import org.apache.isis.metamodel.java5.AnnotationBasedFacetFactoryAbstract;
import org.apache.isis.metamodel.spec.feature.ObjectFeatureType;

public class JpaNamedQueryAnnotationFacetFactory extends AnnotationBasedFacetFactoryAbstract {

    public JpaNamedQueryAnnotationFacetFactory() {
        super(ObjectFeatureType.OBJECTS_ONLY);
    }

    @Override
    public boolean process(final Class<?> cls, final MethodRemover methodRemover, final FacetHolder holder) {
        final NamedQueries namedQueriesAnnotation = getAnnotation(cls, NamedQueries.class);
        if (namedQueriesAnnotation != null) {
            FacetUtil.addFacet(new JpaNamedQueriesFacetAnnotation(namedQueriesAnnotation.value(), holder));
            return true;
        }
        final NamedQuery namedQueryAnnotation = getAnnotation(cls, NamedQuery.class);
        if (namedQueryAnnotation != null) {
            FacetUtil.addFacet(new JpaNamedQueryFacetAnnotation(namedQueryAnnotation, holder));
            return true;
        }
        return false;
    }
}

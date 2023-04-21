package org.starobjects.jpa.metamodel.facets.collection.ofelements;

import org.hibernate.annotations.CollectionOfElements;
import org.nakedobjects.metamodel.facets.FacetHolder;
import org.nakedobjects.metamodel.facets.actcoll.typeof.TypeOfFacetAbstract;
import org.nakedobjects.metamodel.specloader.SpecificationLoader;

/**
 * Derived from {@link CollectionOfElements#targetElement()}.
 */
public class TypeOfFacetDerivedFromHibernateCollectionOfElementsAnnotation extends TypeOfFacetAbstract {

    public TypeOfFacetDerivedFromHibernateCollectionOfElementsAnnotation(final FacetHolder holder, final Class<?> elementType, final SpecificationLoader specificationLoader) {
        super(elementType, holder, specificationLoader);
    }
}

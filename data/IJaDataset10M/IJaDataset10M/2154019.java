package org.apache.isis.extensions.jpa.metamodel.facets.collection.ofelements;

import org.apache.isis.metamodel.facets.FacetHolder;
import org.apache.isis.metamodel.facets.actcoll.typeof.TypeOfFacetAbstract;
import org.apache.isis.metamodel.specloader.SpecificationLoader;
import org.hibernate.annotations.CollectionOfElements;

/**
 * Derived from {@link CollectionOfElements#targetElement()}.
 */
public class TypeOfFacetDerivedFromHibernateCollectionOfElementsAnnotation extends TypeOfFacetAbstract {

    public TypeOfFacetDerivedFromHibernateCollectionOfElementsAnnotation(final FacetHolder holder, final Class<?> elementType, final SpecificationLoader specificationLoader) {
        super(elementType, holder, specificationLoader);
    }
}

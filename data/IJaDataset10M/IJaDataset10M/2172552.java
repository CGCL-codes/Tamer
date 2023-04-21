package org.starobjects.jpa.metamodel.facets.object.entity;

import org.hibernate.annotations.Entity;
import org.nakedobjects.metamodel.facets.AbstractFacetFactoryTest;
import org.nakedobjects.metamodel.facets.Facet;
import org.nakedobjects.metamodel.facets.object.immutable.ImmutableFacet;
import org.nakedobjects.metamodel.spec.feature.NakedObjectFeatureType;

public class GivenHibernateEntityAnnotationFacetFactoryTest extends AbstractFacetFactoryTest {

    private HibernateEntityAnnotationFacetFactory facetFactory;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        facetFactory = new HibernateEntityAnnotationFacetFactory();
    }

    @Override
    protected void tearDown() throws Exception {
        facetFactory = null;
        super.tearDown();
    }

    @Override
    public void testFeatureTypes() {
        final NakedObjectFeatureType[] featureTypes = facetFactory.getFeatureTypes();
        assertTrue(contains(featureTypes, NakedObjectFeatureType.OBJECT));
        assertFalse(contains(featureTypes, NakedObjectFeatureType.PROPERTY));
        assertFalse(contains(featureTypes, NakedObjectFeatureType.COLLECTION));
        assertFalse(contains(featureTypes, NakedObjectFeatureType.ACTION));
        assertFalse(contains(featureTypes, NakedObjectFeatureType.ACTION_PARAMETER));
    }

    public void testEntityAnnotationPickedUpOnClass() {
        @Entity
        class Customer {
        }
        facetFactory.process(Customer.class, methodRemover, facetHolder);
        final Facet facet = facetHolder.getFacet(HibernateEntityFacet.class);
        assertNotNull(facet);
        assertTrue(facet instanceof HibernateEntityFacetAnnotation);
    }

    public void testIfNoEntityAnnotationThenNoFacet() {
        class Customer {
        }
        facetFactory.process(Customer.class, methodRemover, facetHolder);
        assertNull(facetHolder.getFacet(HibernateEntityFacet.class));
    }

    public void testEntityAnnotationWithNoExplicitMutableAttributeDefaultsToMutable() {
        @Entity()
        class Customer {
        }
        facetFactory.process(Customer.class, methodRemover, facetHolder);
        assertNull(facetHolder.getFacet(ImmutableFacet.class));
    }

    public void testEntityAnnotationWithMutableAttributeSetToTrueDefaultsToMutable() {
        @Entity(mutable = true)
        class Customer {
        }
        facetFactory.process(Customer.class, methodRemover, facetHolder);
        assertNull(facetHolder.getFacet(ImmutableFacet.class));
    }

    public void testEntityAnnotationWithMutableAttributeSetToTruePickedUpAsImmutableFacet() {
        @Entity(mutable = false)
        class Customer {
        }
        facetFactory.process(Customer.class, methodRemover, facetHolder);
        final Facet facet = facetHolder.getFacet(ImmutableFacet.class);
        assertNotNull(facet);
        assertTrue(facet instanceof ImmutableFacetDerivedFromHibernateEntityAnnotation);
    }

    public void testNoMethodsRemoved() {
        @Entity
        class Customer {
        }
        facetFactory.process(Customer.class, methodRemover, facetHolder);
        assertNoMethodsRemoved();
    }
}

package org.starobjects.jpa.metamodel.facets.objcoll.immutable;

import java.lang.reflect.Method;
import org.nakedobjects.metamodel.facets.AbstractFacetFactoryTest;
import org.nakedobjects.metamodel.facets.Facet;
import org.nakedobjects.metamodel.facets.disable.DisabledFacet;
import org.nakedobjects.metamodel.facets.object.immutable.ImmutableFacet;
import org.nakedobjects.metamodel.spec.feature.NakedObjectFeatureType;

public class GivenHibernateImmutableAnnotationFacetFactory extends AbstractFacetFactoryTest {

    private HibernateImmutableAnnotationFacetFactory facetFactory;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        facetFactory = new HibernateImmutableAnnotationFacetFactory();
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
        assertTrue(contains(featureTypes, NakedObjectFeatureType.PROPERTY));
        assertTrue(contains(featureTypes, NakedObjectFeatureType.COLLECTION));
        assertFalse(contains(featureTypes, NakedObjectFeatureType.ACTION));
        assertFalse(contains(featureTypes, NakedObjectFeatureType.ACTION_PARAMETER));
    }

    public void testImmutableFacetPickedUpOnType() throws Exception {
        facetFactory.process(SimpleObjectDefinedAsImmutableType.class, methodRemover, facetHolder);
        final Facet facet = facetHolder.getFacet(ImmutableFacet.class);
        assertNotNull(facet);
        assertTrue(facet instanceof ImmutableFacetDerivedFromHibernateImmutableAnnotation);
    }

    public void testImmutableFacetPickedUpOnProperty() throws Exception {
        Method method = SimpleObjectWithImmutableProperty.class.getMethod("getObject");
        facetFactory.process(method, methodRemover, facetHolder);
        final Facet facet = facetHolder.getFacet(DisabledFacet.class);
        assertNotNull(facet);
        assertTrue(facet instanceof DisabledFacetDerivedFromHibernateImmutableAnnotation);
    }

    public void testImmutableFacetPickedUpOnCollection() throws Exception {
        Method method = SimpleObjectWithImmutableCollection.class.getMethod("getObjects");
        facetFactory.process(method, methodRemover, facetHolder);
        final Facet facet = facetHolder.getFacet(DisabledFacet.class);
        assertNotNull(facet);
        assertTrue(facet instanceof DisabledFacetDerivedFromHibernateImmutableAnnotation);
    }

    public void testIfNoImmutableAnnotationOnTypeThenNoFacets() throws Exception {
        facetFactory.process(SimpleObjectNotDefinedAsImmutableType.class, methodRemover, facetHolder);
        assertNull(facetHolder.getFacet(ImmutableFacet.class));
        assertNull(facetHolder.getFacet(HibernateImmutableFacet.class));
    }

    public void testNoImmutableAnnotationOnPropertyThenNoFacets() throws Exception {
        Method method = SimpleObjectWithImmutableProperty.class.getMethod("getOtherObject");
        facetFactory.process(method, methodRemover, facetHolder);
        assertNull(facetHolder.getFacet(DisabledFacet.class));
    }

    public void testNoImmutableAnnotationOnCollectionThenNoFacets() throws Exception {
        Method method = SimpleObjectWithImmutableCollection.class.getMethod("getOtherObjects");
        facetFactory.process(method, methodRemover, facetHolder);
        assertNull(facetHolder.getFacet(DisabledFacet.class));
    }

    public void testNoMethodsRemovedForType() throws Exception {
        facetFactory.process(SimpleObjectDefinedAsImmutableType.class, methodRemover, facetHolder);
        assertNoMethodsRemoved();
    }

    public void testNoMethodsRemovedForProperty() throws Exception {
        Method method = SimpleObjectWithImmutableProperty.class.getMethod("getObject");
        facetFactory.process(method, methodRemover, facetHolder);
        assertNoMethodsRemoved();
    }

    public void testNoMethodsRemovedForCollection() throws Exception {
        Method method = SimpleObjectWithImmutableCollection.class.getMethod("getObjects");
        facetFactory.process(method, methodRemover, facetHolder);
        assertNoMethodsRemoved();
    }
}

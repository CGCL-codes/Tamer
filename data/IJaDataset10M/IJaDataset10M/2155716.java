package org.starobjects.jpa.metamodel.facets.prop.transience;

import java.lang.reflect.Method;
import org.nakedobjects.metamodel.facets.AbstractFacetFactoryTest;
import org.nakedobjects.metamodel.facets.Facet;
import org.nakedobjects.metamodel.facets.propcoll.derived.DerivedFacet;
import org.nakedobjects.metamodel.spec.feature.NakedObjectFeatureType;

public class GivenJpaTransientAnnotationFacetFactory extends AbstractFacetFactoryTest {

    private JpaTransientAnnotationFacetFactory facetFactory;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        facetFactory = new JpaTransientAnnotationFacetFactory();
    }

    @Override
    protected void tearDown() throws Exception {
        facetFactory = null;
        super.tearDown();
    }

    @Override
    public void testFeatureTypes() {
        final NakedObjectFeatureType[] featureTypes = facetFactory.getFeatureTypes();
        assertFalse(contains(featureTypes, NakedObjectFeatureType.OBJECT));
        assertTrue(contains(featureTypes, NakedObjectFeatureType.PROPERTY));
        assertFalse(contains(featureTypes, NakedObjectFeatureType.COLLECTION));
        assertFalse(contains(featureTypes, NakedObjectFeatureType.ACTION));
        assertFalse(contains(featureTypes, NakedObjectFeatureType.ACTION_PARAMETER));
    }

    public void testTransientAnnotationPickedUpOnProperty() throws Exception {
        Method method = SimpleObjectWithTransientAnnotation.class.getMethod("getTransientColumn");
        facetFactory.process(method, methodRemover, facetHolder);
        final Facet facet = facetHolder.getFacet(JpaTransientFacet.class);
        assertNotNull(facet);
        assertTrue(facet instanceof JpaTransientFacetAnnotation);
    }

    public void testDerivedFacetDerivedForProperty() throws Exception {
        Method method = SimpleObjectWithTransientAnnotation.class.getMethod("getTransientColumn");
        facetFactory.process(method, methodRemover, facetHolder);
        final DerivedFacet facet = facetHolder.getFacet(DerivedFacet.class);
        assertNotNull(facet);
        assertTrue(facet instanceof DerivedFacetDerivedFromJpaTransientAnnotation);
    }

    public void testIfNoTransientAnnotationThenNoManyToOneColumnFacet() throws Exception {
        Method method = SimpleObjectWithNoTransientAnnotation.class.getMethod("getTransientColumn");
        facetFactory.process(method, methodRemover, facetHolder);
        assertNull(facetHolder.getFacet(JpaTransientFacet.class));
    }

    public void testIfNoTransientAnnotationThenNoDerivedFacet() throws Exception {
        Method method = SimpleObjectWithNoTransientAnnotation.class.getMethod("getTransientColumn");
        facetFactory.process(method, methodRemover, facetHolder);
        assertNull(facetHolder.getFacet(DerivedFacet.class));
    }

    public void testNoMethodsRemoved() throws Exception {
        Method method = SimpleObjectWithTransientAnnotation.class.getMethod("getTransientColumn");
        facetFactory.process(method, methodRemover, facetHolder);
        assertNoMethodsRemoved();
    }
}

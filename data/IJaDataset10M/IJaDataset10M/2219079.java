package org.starobjects.jpa.metamodel.facets.object.embeddable;

import java.lang.reflect.Method;
import org.nakedobjects.metamodel.facets.AbstractFacetFactoryTest;
import org.nakedobjects.metamodel.facets.Facet;
import org.nakedobjects.metamodel.facets.disable.DisabledFacet;
import org.nakedobjects.metamodel.facets.object.aggregated.AggregatedFacet;
import org.nakedobjects.metamodel.facets.object.immutable.ImmutableFacet;
import org.nakedobjects.metamodel.spec.feature.NakedObjectFeatureType;

public class GivenJpaEmbeddableAnnotationFacetFactory extends AbstractFacetFactoryTest {

    private JpaEmbeddableAnnotationFacetFactory facetFactory;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        facetFactory = new JpaEmbeddableAnnotationFacetFactory();
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

    public void testAggregatedFacetPickedUpOnType() throws Exception {
        facetFactory.process(SimpleObjectWithEmbeddable.class, methodRemover, facetHolder);
        final Facet facet = facetHolder.getFacet(AggregatedFacet.class);
        assertNotNull(facet);
        assertTrue(facet instanceof AggregatedFacetDerivedFromJpaEmbeddableAnnotation);
    }

    public void testJpaEmbeddableFacetPickedUpOnType() throws Exception {
        facetFactory.process(SimpleObjectWithEmbeddable.class, methodRemover, facetHolder);
        final Facet facet = facetHolder.getFacet(JpaEmbeddableFacet.class);
        assertNotNull(facet);
        assertTrue(facet instanceof JpaEmbeddableFacetAnnotation);
    }

    public void testNoMethodsRemovedForType() throws Exception {
        facetFactory.process(SimpleObjectWithEmbeddable.class, methodRemover, facetHolder);
        assertNoMethodsRemoved();
    }
}

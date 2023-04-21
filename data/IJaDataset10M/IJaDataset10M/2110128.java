package org.starobjects.jpa.metamodel.facets.object.namedquery;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.nakedobjects.metamodel.facets.AbstractFacetFactoryJUnit4TestCase;
import org.nakedobjects.metamodel.spec.feature.NakedObjectFeatureType;

public class GivenJpaNamedQueryAnnotationFacetFactoryWhenFeatureTypes extends AbstractFacetFactoryJUnit4TestCase {

    private JpaNamedQueryAnnotationFacetFactory facetFactory;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        facetFactory = new JpaNamedQueryAnnotationFacetFactory();
    }

    @After
    public void tearDown() throws Exception {
        facetFactory = null;
        super.tearDown();
    }

    @Test
    public void testFeatureTypes() {
        final NakedObjectFeatureType[] featureTypes = facetFactory.getFeatureTypes();
        assertTrue(contains(featureTypes, NakedObjectFeatureType.OBJECT));
        assertFalse(contains(featureTypes, NakedObjectFeatureType.PROPERTY));
        assertFalse(contains(featureTypes, NakedObjectFeatureType.COLLECTION));
        assertFalse(contains(featureTypes, NakedObjectFeatureType.ACTION));
        assertFalse(contains(featureTypes, NakedObjectFeatureType.ACTION_PARAMETER));
    }
}

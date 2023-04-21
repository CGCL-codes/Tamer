package org.nakedobjects.nof.reflect.java.facets.actions.exploration;

import org.nakedobjects.applib.annotation.Exploration;
import org.nakedobjects.noa.facets.Facet;
import org.nakedobjects.noa.facets.actions.exploration.ExplorationFacet;
import org.nakedobjects.noa.facets.actions.exploration.ExplorationFacetAbstract;
import org.nakedobjects.noa.reflect.NakedObjectFeatureType;
import org.nakedobjects.nof.reflect.java.facets.AbstractFacetFactoryTest;
import java.lang.reflect.Method;

public class ExplorationAnnotationFacetFactoryTest extends AbstractFacetFactoryTest {

    private ExplorationAnnotationFacetFactory facetFactory;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        facetFactory = new ExplorationAnnotationFacetFactory();
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
        assertFalse(contains(featureTypes, NakedObjectFeatureType.PROPERTY));
        assertFalse(contains(featureTypes, NakedObjectFeatureType.COLLECTION));
        assertTrue(contains(featureTypes, NakedObjectFeatureType.ACTION));
        assertFalse(contains(featureTypes, NakedObjectFeatureType.ACTION_PARAMETER));
    }

    public void testExplorationAnnotationPickedUp() {
        class Customer {

            @Exploration
            public void someAction() {
            }
        }
        final Method actionMethod = findMethod(Customer.class, "someAction");
        facetFactory.process(actionMethod, methodRemover, facetHolder);
        final Facet facet = facetHolder.getFacet(ExplorationFacet.class);
        assertNotNull(facet);
        assertTrue(facet instanceof ExplorationFacetAbstract);
        final ExplorationFacetAbstract executedFacetImpl = (ExplorationFacetAbstract) facet;
        assertNoMethodsRemoved();
    }
}

package org.nakedobjects.metamodel.facets.object.validate;

import java.lang.reflect.Method;
import org.nakedobjects.metamodel.facets.AbstractFacetFactoryTest;
import org.nakedobjects.metamodel.facets.Facet;
import org.nakedobjects.metamodel.spec.feature.NakedObjectFeatureType;

public class ObjectValidMethodFacetFactoryTest extends AbstractFacetFactoryTest {

    private ValidateObjectViaValidateMethodFacetFactory facetFactory;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        facetFactory = new ValidateObjectViaValidateMethodFacetFactory();
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

    public void testValidateMethodPickedUpAndMethodRemoved() {
        @edu.umd.cs.findbugs.annotations.SuppressWarnings("UMAC_UNCALLABLE_METHOD_OF_ANONYMOUS_CLASS")
        class Customer {

            @SuppressWarnings("unused")
            public String validate() {
                return null;
            }
        }
        final Method validateMethod = findMethod(Customer.class, "validate");
        facetFactory.process(Customer.class, methodRemover, facetHolder);
        final Facet facet = facetHolder.getFacet(ValidateObjectFacet.class);
        assertNotNull(facet);
        assertTrue(facet instanceof ValidateObjectFacetViaValidateMethod);
        assertTrue(methodRemover.getRemoveMethodMethodCalls().contains(validateMethod));
    }
}

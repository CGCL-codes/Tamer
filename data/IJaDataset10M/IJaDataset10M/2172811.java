package org.nakedobjects.metamodel.facets.naming.named;

import java.lang.reflect.Method;
import java.util.Collection;
import org.nakedobjects.applib.annotation.Named;
import org.nakedobjects.metamodel.facets.AbstractFacetFactoryTest;
import org.nakedobjects.metamodel.facets.Facet;
import org.nakedobjects.metamodel.spec.feature.NakedObjectFeatureType;

public class NamedAnnotationFacetFactoryTest extends AbstractFacetFactoryTest {

    private NamedAnnotationFacetFactory facetFactory;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        facetFactory = new NamedAnnotationFacetFactory();
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
        assertTrue(contains(featureTypes, NakedObjectFeatureType.ACTION));
        assertTrue(contains(featureTypes, NakedObjectFeatureType.ACTION_PARAMETER));
    }

    public void testNamedAnnotationPickedUpOnClass() {
        @Named("some name")
        class Customer {
        }
        facetFactory.process(Customer.class, methodRemover, facetHolder);
        final Facet facet = facetHolder.getFacet(NamedFacet.class);
        assertNotNull(facet);
        assertTrue(facet instanceof NamedFacetAbstract);
        final NamedFacetAbstract namedFacetAbstract = (NamedFacetAbstract) facet;
        assertEquals("some name", namedFacetAbstract.value());
        assertNoMethodsRemoved();
    }

    public void testNamedAnnotationPickedUpOnProperty() {
        @edu.umd.cs.findbugs.annotations.SuppressWarnings("UMAC_UNCALLABLE_METHOD_OF_ANONYMOUS_CLASS")
        class Customer {

            @Named("some name")
            public int getNumberOfOrders() {
                return 0;
            }
        }
        final Method actionMethod = findMethod(Customer.class, "getNumberOfOrders");
        facetFactory.process(Customer.class, actionMethod, methodRemover, facetHolder);
        final Facet facet = facetHolder.getFacet(NamedFacet.class);
        assertNotNull(facet);
        assertTrue(facet instanceof NamedFacetAbstract);
        final NamedFacetAbstract namedFacetAbstract = (NamedFacetAbstract) facet;
        assertEquals("some name", namedFacetAbstract.value());
        assertNoMethodsRemoved();
    }

    public void testNamedAnnotationPickedUpOnCollection() {
        @edu.umd.cs.findbugs.annotations.SuppressWarnings("UMAC_UNCALLABLE_METHOD_OF_ANONYMOUS_CLASS")
        class Customer {

            @Named("some name")
            public Collection getOrders() {
                return null;
            }
        }
        final Method actionMethod = findMethod(Customer.class, "getOrders");
        facetFactory.process(Customer.class, actionMethod, methodRemover, facetHolder);
        final Facet facet = facetHolder.getFacet(NamedFacet.class);
        assertNotNull(facet);
        assertTrue(facet instanceof NamedFacetAbstract);
        final NamedFacetAbstract namedFacetAbstract = (NamedFacetAbstract) facet;
        assertEquals("some name", namedFacetAbstract.value());
        assertNoMethodsRemoved();
    }

    public void testNamedAnnotationPickedUpOnAction() {
        @edu.umd.cs.findbugs.annotations.SuppressWarnings("UMAC_UNCALLABLE_METHOD_OF_ANONYMOUS_CLASS")
        class Customer {

            @Named("some name")
            public void someAction() {
            }
        }
        final Method actionMethod = findMethod(Customer.class, "someAction");
        facetFactory.process(Customer.class, actionMethod, methodRemover, facetHolder);
        final Facet facet = facetHolder.getFacet(NamedFacet.class);
        assertNotNull(facet);
        assertTrue(facet instanceof NamedFacetAbstract);
        final NamedFacetAbstract namedFacetAbstract = (NamedFacetAbstract) facet;
        assertEquals("some name", namedFacetAbstract.value());
        assertNoMethodsRemoved();
    }

    public void testNamedAnnotationPickedUpOnActionParameter() {
        @edu.umd.cs.findbugs.annotations.SuppressWarnings("UMAC_UNCALLABLE_METHOD_OF_ANONYMOUS_CLASS")
        class Customer {

            public void someAction(@Named("some name") final int x) {
            }
        }
        final Method actionMethod = findMethod(Customer.class, "someAction", new Class[] { int.class });
        facetFactory.processParams(actionMethod, 0, facetHolder);
        final Facet facet = facetHolder.getFacet(NamedFacet.class);
        assertNotNull(facet);
        assertTrue(facet instanceof NamedFacetAbstract);
        final NamedFacetAbstract namedFacetAbstract = (NamedFacetAbstract) facet;
        assertEquals("some name", namedFacetAbstract.value());
    }
}

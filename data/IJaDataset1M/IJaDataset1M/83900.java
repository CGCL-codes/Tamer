package org.starobjects.jpa.metamodel.facets.prop.basic;

import java.lang.reflect.Method;
import javax.persistence.FetchType;
import org.nakedobjects.metamodel.facets.AbstractFacetFactoryTest;
import org.nakedobjects.metamodel.facets.Facet;
import org.nakedobjects.metamodel.facets.propparam.validate.mandatory.MandatoryFacet;
import org.nakedobjects.metamodel.spec.feature.NakedObjectFeatureType;
import org.starobjects.jpa.metamodel.facets.JpaFetchTypeFacet;

public class GivenJpaBasicAnnotationFacetFactory extends AbstractFacetFactoryTest {

    private JpaBasicAnnotationFacetFactory facetFactory;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        facetFactory = new JpaBasicAnnotationFacetFactory();
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

    public void testBasicAnnotationPickedUpOnProperty() throws Exception {
        Method method = SimpleObjectWithBasicOptionalFalse.class.getMethod("getSomeColumn");
        facetFactory.process(method, methodRemover, facetHolder);
        final Facet facet = facetHolder.getFacet(JpaBasicFacet.class);
        assertNotNull(facet);
        assertTrue(facet instanceof JpaBasicFacetAnnotation);
    }

    public void testFetchTypeAnnotationDerivedForProperty() throws Exception {
        Method method = SimpleObjectWithBasicFetchTypeEager.class.getMethod("getSomeColumn");
        facetFactory.process(method, methodRemover, facetHolder);
        final JpaFetchTypeFacet facet = facetHolder.getFacet(JpaFetchTypeFacet.class);
        assertNotNull(facet);
        assertTrue(facet instanceof JpaFetchTypeFacetDerivedFromJpaBasicAnnotation);
    }

    public void testFetchTypeAttributeExplicitlySpecified() throws Exception {
        Method method = SimpleObjectWithBasicFetchTypeLazy.class.getMethod("getSomeColumn");
        facetFactory.process(method, methodRemover, facetHolder);
        final JpaFetchTypeFacet facet = facetHolder.getFacet(JpaFetchTypeFacet.class);
        assertEquals(FetchType.LAZY, facet.getFetchType());
    }

    public void testFetchTypeAttributeNotExplicitlySpecified() throws Exception {
        Method method = SimpleObjectWithBasicAnnotation.class.getMethod("getSomeColumn");
        facetFactory.process(method, methodRemover, facetHolder);
        final JpaFetchTypeFacet facet = facetHolder.getFacet(JpaFetchTypeFacet.class);
        assertEquals(FetchType.EAGER, facet.getFetchType());
    }

    public void testMandatoryFacetDerivedForProperty() throws Exception {
        Method method = SimpleObjectWithBasicAnnotation.class.getMethod("getSomeColumn");
        facetFactory.process(method, methodRemover, facetHolder);
        final MandatoryFacet facet = facetHolder.getFacet(MandatoryFacet.class);
        assertNotNull(facet);
    }

    public void testOptionalAttributeExplicitlySpecifiedAsFalseForBasicAnnotationProperty() throws Exception {
        Method method = SimpleObjectWithBasicOptionalFalse.class.getMethod("getSomeColumn");
        facetFactory.process(method, methodRemover, facetHolder);
        final MandatoryFacet facet = facetHolder.getFacet(MandatoryFacet.class);
        assertNotNull(facet);
        assertTrue(facet instanceof MandatoryFacetDerivedFromJpaBasicAnnotation);
        assertFalse(facet.isInvertedSemantics());
    }

    public void testOptionalAttributeExplicitlySpecifiedAsTrueForBasicAnnotationProperty() throws Exception {
        Method method = SimpleObjectWithBasicOptionalTrue.class.getMethod("getSomeColumn");
        facetFactory.process(method, methodRemover, facetHolder);
        final MandatoryFacet facet = facetHolder.getFacet(MandatoryFacet.class);
        assertNotNull(facet);
        assertTrue(facet instanceof OptionalFacetDerivedFromJpaBasicAnnotation);
        assertTrue(facet.isInvertedSemantics());
    }

    public void testOptionalAttributeNotSpecifiedForBasicAnnotationProperty() throws Exception {
        Method method = SimpleObjectWithBasicAnnotation.class.getMethod("getSomeColumn");
        facetFactory.process(method, methodRemover, facetHolder);
        final MandatoryFacet facet = facetHolder.getFacet(MandatoryFacet.class);
        assertNotNull(facet);
        assertTrue(facet instanceof OptionalFacetDerivedFromJpaBasicAnnotation);
    }

    public void testIfNoBasicAnnotationThenNoBasicFacet() throws Exception {
        Method method = SimpleObjectWithNoBasicAnnotation.class.getMethod("getSomeColumn");
        facetFactory.process(method, methodRemover, facetHolder);
        final Facet facet = facetHolder.getFacet(JpaBasicFacet.class);
        assertNull(facet);
    }

    public void testIfNoBasicAnnotationThenNoFetchTypeFacet() throws Exception {
        Method method = SimpleObjectWithNoBasicAnnotation.class.getMethod("getSomeColumn");
        facetFactory.process(method, methodRemover, facetHolder);
        final Facet facet = facetHolder.getFacet(JpaFetchTypeFacet.class);
        assertNull(facet);
    }

    public void testIfNoBasicAnnotationThenNoMandatoryFacet() throws Exception {
        Method method = SimpleObjectWithNoBasicAnnotation.class.getMethod("getSomeColumn");
        facetFactory.process(method, methodRemover, facetHolder);
        final Facet facet = facetHolder.getFacet(MandatoryFacet.class);
        assertNull(facet);
    }

    public void testNoMethodsRemoved() throws Exception {
        Method method = SimpleObjectWithBasicAnnotation.class.getMethod("getSomeColumn");
        facetFactory.process(method, methodRemover, facetHolder);
        assertNoMethodsRemoved();
    }
}

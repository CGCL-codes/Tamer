package org.starobjects.jpa.metamodel.facets.prop.column;

import java.lang.reflect.Method;
import org.nakedobjects.metamodel.facets.AbstractFacetFactoryTest;
import org.nakedobjects.metamodel.facets.Facet;
import org.nakedobjects.metamodel.facets.propparam.validate.mandatory.MandatoryFacet;
import org.nakedobjects.metamodel.facets.propparam.validate.maxlength.MaxLengthFacet;
import org.nakedobjects.metamodel.spec.feature.NakedObjectFeatureType;

public class GivenJpaColumnAnnotationFacetFactory extends AbstractFacetFactoryTest {

    private JpaColumnAnnotationFacetFactory facetFactory;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        facetFactory = new JpaColumnAnnotationFacetFactory();
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

    public void testColumnAnnotationPickedUpOnProperty() throws Exception {
        Method method = SimpleObjectWithColumnAnnotation.class.getMethod("getSomeColumn");
        facetFactory.process(method, methodRemover, facetHolder);
        final Facet facet = facetHolder.getFacet(JpaColumnFacet.class);
        assertNotNull(facet);
        assertTrue(facet instanceof JpaColumnFacetAnnotation);
    }

    public void testColumnAnnotationNameAttributeSet() throws Exception {
        Method method = SimpleObjectWithColumnName.class.getMethod("getSomeColumn");
        facetFactory.process(method, methodRemover, facetHolder);
        final JpaColumnFacet facet = facetHolder.getFacet(JpaColumnFacet.class);
        assertEquals("someCol", facet.name());
    }

    public void testMaxLengthFacetDerivedForProperty() throws Exception {
        Method method = SimpleObjectWithColumnAnnotation.class.getMethod("getSomeColumn");
        facetFactory.process(method, methodRemover, facetHolder);
        final MaxLengthFacet facet = facetHolder.getFacet(MaxLengthFacet.class);
        assertNotNull(facet);
        assertTrue(facet instanceof MaxLengthFacetDerivedFromJpaColumnAnnotation);
    }

    public void testMaxLengthAttributeExplicitlySpecified() throws Exception {
        Method method = SimpleObjectWithColumnMaxLength30.class.getMethod("getSomeColumn");
        facetFactory.process(method, methodRemover, facetHolder);
        final MaxLengthFacet facet = facetHolder.getFacet(MaxLengthFacet.class);
        assertEquals(30, facet.value());
    }

    public void testMaxLengthAttributeNotExplicitlySpecified() throws Exception {
        Method method = SimpleObjectWithColumnAnnotation.class.getMethod("getSomeColumn");
        facetFactory.process(method, methodRemover, facetHolder);
        final MaxLengthFacet facet = facetHolder.getFacet(MaxLengthFacet.class);
        assertEquals(255, facet.value());
    }

    public void testMandatoryFacetDerivedForProperty() throws Exception {
        Method method = SimpleObjectWithColumnAnnotation.class.getMethod("getSomeColumn");
        facetFactory.process(method, methodRemover, facetHolder);
        final MandatoryFacet facet = facetHolder.getFacet(MandatoryFacet.class);
        assertNotNull(facet);
    }

    public void testNullableAttributeExplicitlySpecifiedAsFalse() throws Exception {
        Method method = SimpleObjectWithColumnNullableFalse.class.getMethod("getSomeColumn");
        facetFactory.process(method, methodRemover, facetHolder);
        final MandatoryFacet facet = facetHolder.getFacet(MandatoryFacet.class);
        assertNotNull(facet);
        assertTrue(facet instanceof MandatoryFacetDerivedFromJpaColumnAnnotation);
        assertFalse(facet.isInvertedSemantics());
    }

    public void testNullableAttributeExplicitlySpecifiedAsTrue() throws Exception {
        Method method = SimpleObjectWithColumnNullableTrue.class.getMethod("getSomeColumn");
        facetFactory.process(method, methodRemover, facetHolder);
        final MandatoryFacet facet = facetHolder.getFacet(MandatoryFacet.class);
        assertNotNull(facet);
        assertTrue(facet instanceof OptionalFacetDerivedFromJpaColumnAnnotation);
        assertTrue(facet.isInvertedSemantics());
    }

    public void testNullableAttributeNotSpecifiedForColumnAnnotationProperty() throws Exception {
        Method method = SimpleObjectWithColumnAnnotation.class.getMethod("getSomeColumn");
        facetFactory.process(method, methodRemover, facetHolder);
        final MandatoryFacet facet = facetHolder.getFacet(MandatoryFacet.class);
        assertNotNull(facet);
        assertTrue(facet instanceof OptionalFacetDerivedFromJpaColumnAnnotation);
    }

    public void testIfNoColumnAnnotationThenNoColumnFacet() throws Exception {
        Method method = SimpleObjectWithNoColumnAnnotation.class.getMethod("getSomeColumn");
        facetFactory.process(method, methodRemover, facetHolder);
        final Facet facet = facetHolder.getFacet(JpaColumnFacet.class);
        assertNull(facet);
    }

    public void testIfNoColumnAnnotationThenNoMaxLengthFacet() throws Exception {
        Method method = SimpleObjectWithNoColumnAnnotation.class.getMethod("getSomeColumn");
        facetFactory.process(method, methodRemover, facetHolder);
        final Facet facet = facetHolder.getFacet(MaxLengthFacet.class);
        assertNull(facet);
    }

    public void testIfNoColumnAnnotationThenNoMandatoryFacet() throws Exception {
        Method method = SimpleObjectWithNoColumnAnnotation.class.getMethod("getSomeColumn");
        facetFactory.process(method, methodRemover, facetHolder);
        final Facet facet = facetHolder.getFacet(MandatoryFacet.class);
        assertNull(facet);
    }

    public void testNoMethodsRemoved() throws Exception {
        Method method = SimpleObjectWithColumnAnnotation.class.getMethod("getSomeColumn");
        facetFactory.process(method, methodRemover, facetHolder);
        assertNoMethodsRemoved();
    }
}

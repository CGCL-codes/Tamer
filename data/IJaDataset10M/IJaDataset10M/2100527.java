package org.nakedobjects.metamodel.facets.object.encodable;

import org.nakedobjects.applib.adapters.EncoderDecoder;
import org.nakedobjects.applib.annotation.Encodable;
import org.nakedobjects.metamodel.config.internal.PropertiesConfiguration;
import org.nakedobjects.metamodel.facets.AbstractFacetFactoryTest;
import org.nakedobjects.metamodel.facets.object.encodeable.EncodableAnnotationFacetFactory;
import org.nakedobjects.metamodel.facets.object.encodeable.EncodableFacet;
import org.nakedobjects.metamodel.facets.object.encodeable.EncodableFacetAbstract;
import org.nakedobjects.metamodel.facets.object.encodeable.EncoderDecoderUtil;
import org.nakedobjects.metamodel.spec.feature.NakedObjectFeatureType;

public class EncodableFacetFactoryTest extends AbstractFacetFactoryTest {

    private EncodableAnnotationFacetFactory facetFactory;

    private PropertiesConfiguration propertiesConfiguration;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        facetFactory = new EncodableAnnotationFacetFactory();
        propertiesConfiguration = new PropertiesConfiguration();
        facetFactory.setNakedObjectConfiguration(propertiesConfiguration);
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

    public void testFacetPickedUp() {
        facetFactory.process(MyEncodableUsingEncoderDecoderName.class, methodRemover, facetHolder);
        final EncodableFacet facet = facetHolder.getFacet(EncodableFacet.class);
        assertNotNull(facet);
        assertTrue(facet instanceof EncodableFacetAbstract);
    }

    public void testFacetFacetHolderStored() {
        facetFactory.process(MyEncodableUsingEncoderDecoderName.class, methodRemover, facetHolder);
        final EncodableFacetAbstract valueFacet = (EncodableFacetAbstract) facetHolder.getFacet(EncodableFacet.class);
        assertEquals(facetHolder, valueFacet.getFacetHolder());
    }

    public void testNoMethodsRemoved() {
        facetFactory.process(MyEncodableUsingEncoderDecoderName.class, methodRemover, facetHolder);
        assertNoMethodsRemoved();
    }

    abstract static class EncoderDecoderNoop<T> implements EncoderDecoder<T> {

        public T fromEncodedString(final String encodedString) {
            return null;
        }

        public String toEncodedString(final T toEncode) {
            return null;
        }
    }

    @Encodable(encoderDecoderName = "org.nakedobjects.metamodel.facets.object.encodable.EncodableFacetFactoryTest$MyEncodableUsingEncoderDecoderName")
    public static class MyEncodableUsingEncoderDecoderName extends EncoderDecoderNoop<MyEncodableUsingEncoderDecoderName> {

        /**
         * Required since is an EncoderDecoder
         */
        public MyEncodableUsingEncoderDecoderName() {
        }
    }

    public void testEncodeableUsingEncoderDecoderName() {
        facetFactory.process(MyEncodableUsingEncoderDecoderName.class, methodRemover, facetHolder);
        final EncodableFacetAbstract encodeableFacet = (EncodableFacetAbstract) facetHolder.getFacet(EncodableFacet.class);
        assertEquals(MyEncodableUsingEncoderDecoderName.class, encodeableFacet.getEncoderDecoderClass());
    }

    @Encodable(encoderDecoderClass = MyEncodeableUsingEncoderDecoderClass.class)
    public static class MyEncodeableUsingEncoderDecoderClass extends EncoderDecoderNoop<MyEncodeableUsingEncoderDecoderClass> {

        /**
         * Required since is a EncoderDecoder.
         */
        public MyEncodeableUsingEncoderDecoderClass() {
        }
    }

    public void testEncodeableUsingEncoderDecoderClass() {
        facetFactory.process(MyEncodeableUsingEncoderDecoderClass.class, methodRemover, facetHolder);
        final EncodableFacetAbstract encodeableFacet = (EncodableFacetAbstract) facetHolder.getFacet(EncodableFacet.class);
        assertEquals(MyEncodeableUsingEncoderDecoderClass.class, encodeableFacet.getEncoderDecoderClass());
    }

    public void testEncodeableMustBeAEncoderDecoder() {
    }

    @Encodable(encoderDecoderClass = MyEncodeableWithoutNoArgConstructor.class)
    public static class MyEncodeableWithoutNoArgConstructor extends EncoderDecoderNoop<MyEncodeableWithoutNoArgConstructor> {

        public MyEncodeableWithoutNoArgConstructor(final int value) {
        }
    }

    public void testEncodeableHaveANoArgConstructor() {
        facetFactory.process(MyEncodeableWithoutNoArgConstructor.class, methodRemover, facetHolder);
        final EncodableFacetAbstract encodeableFacet = (EncodableFacetAbstract) facetHolder.getFacet(EncodableFacet.class);
        assertNull(encodeableFacet);
    }

    @Encodable(encoderDecoderClass = MyEncodeableWithoutPublicNoArgConstructor.class)
    public static class MyEncodeableWithoutPublicNoArgConstructor extends EncoderDecoderNoop<MyEncodeableWithoutPublicNoArgConstructor> {

        MyEncodeableWithoutPublicNoArgConstructor() {
        }

        public MyEncodeableWithoutPublicNoArgConstructor(final int value) {
        }
    }

    public void testEncodeableHaveAPublicNoArgConstructor() {
        facetFactory.process(MyEncodeableWithoutPublicNoArgConstructor.class, methodRemover, facetHolder);
        final EncodableFacetAbstract encodeableFacet = (EncodableFacetAbstract) facetHolder.getFacet(EncodableFacet.class);
        assertNull(encodeableFacet);
    }

    @Encodable()
    public static class MyEncodableWithEncoderDecoderSpecifiedUsingConfiguration extends EncoderDecoderNoop<MyEncodableWithEncoderDecoderSpecifiedUsingConfiguration> {

        /**
         * Required since is a EncoderDecoder.
         */
        public MyEncodableWithEncoderDecoderSpecifiedUsingConfiguration() {
        }
    }

    public void testEncoderDecoderNameCanBePickedUpFromConfiguration() {
        final String className = "org.nakedobjects.metamodel.facets.object.encodable.EncodableFacetFactoryTest$MyEncodableWithEncoderDecoderSpecifiedUsingConfiguration";
        propertiesConfiguration.add(EncoderDecoderUtil.ENCODER_DECODER_NAME_KEY_PREFIX + canonical(className) + EncoderDecoderUtil.ENCODER_DECODER_NAME_KEY_SUFFIX, className);
        facetFactory.process(MyEncodableWithEncoderDecoderSpecifiedUsingConfiguration.class, methodRemover, facetHolder);
        final EncodableFacetAbstract facet = (EncodableFacetAbstract) facetHolder.getFacet(EncodableFacet.class);
        assertNotNull(facet);
        assertEquals(MyEncodableWithEncoderDecoderSpecifiedUsingConfiguration.class, facet.getEncoderDecoderClass());
    }

    public static class NonAnnotatedEncodeableEncoderDecoderSpecifiedUsingConfiguration extends EncoderDecoderNoop<NonAnnotatedEncodeableEncoderDecoderSpecifiedUsingConfiguration> {

        /**
         * Required since is a EncoderDecoder.
         */
        public NonAnnotatedEncodeableEncoderDecoderSpecifiedUsingConfiguration() {
        }
    }

    public void testNonAnnotatedEncodeableCanPickUpEncoderDecoderFromConfiguration() {
        final String className = "org.nakedobjects.metamodel.facets.object.encodable.EncodableFacetFactoryTest$NonAnnotatedEncodeableEncoderDecoderSpecifiedUsingConfiguration";
        propertiesConfiguration.add(EncoderDecoderUtil.ENCODER_DECODER_NAME_KEY_PREFIX + canonical(className) + EncoderDecoderUtil.ENCODER_DECODER_NAME_KEY_SUFFIX, className);
        facetFactory.process(NonAnnotatedEncodeableEncoderDecoderSpecifiedUsingConfiguration.class, methodRemover, facetHolder);
        final EncodableFacetAbstract facet = (EncodableFacetAbstract) facetHolder.getFacet(EncodableFacet.class);
        assertNotNull(facet);
        assertEquals(NonAnnotatedEncodeableEncoderDecoderSpecifiedUsingConfiguration.class, facet.getEncoderDecoderClass());
    }

    private String canonical(final String className) {
        return className.replace('$', '.');
    }
}

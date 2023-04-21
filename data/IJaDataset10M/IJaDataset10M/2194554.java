package org.nakedobjects.nof.reflect.java.facets.object.encodeable;

import org.nakedobjects.applib.annotation.Encodeable;
import org.nakedobjects.applib.capabilities.EncoderDecoder;
import org.nakedobjects.noa.facets.object.encodeable.EncodeableFacet;
import org.nakedobjects.noa.reflect.NakedObjectFeatureType;
import org.nakedobjects.nof.core.conf.PropertiesConfiguration;
import org.nakedobjects.nof.reflect.java.facets.AbstractFacetFactoryTest;

public class EncodeableFacetFactoryTest extends AbstractFacetFactoryTest {

    private EncodeableFacetFactory facetFactory;

    private PropertiesConfiguration propertiesConfiguration;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        facetFactory = new EncodeableFacetFactory();
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
        facetFactory.process(MyEncodeableUsingEncoderDecoderName.class, methodRemover, facetHolder);
        final EncodeableFacet facet = facetHolder.getFacet(EncodeableFacet.class);
        assertNotNull(facet);
        assertTrue(facet instanceof EncodeableFacetAbstract);
    }

    public void testFacetFacetHolderStored() {
        facetFactory.process(MyEncodeableUsingEncoderDecoderName.class, methodRemover, facetHolder);
        final EncodeableFacetAbstract valueFacet = (EncodeableFacetAbstract) facetHolder.getFacet(EncodeableFacet.class);
        assertEquals(facetHolder, valueFacet.getFacetHolder());
    }

    public void testNoMethodsRemoved() {
        facetFactory.process(MyEncodeableUsingEncoderDecoderName.class, methodRemover, facetHolder);
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

    @Encodeable(encoderDecoderName = "org.nakedobjects.nof.reflect.java.facets.object.encodeable.EncodeableFacetFactoryTest$MyEncodeableUsingEncoderDecoderName")
    public static class MyEncodeableUsingEncoderDecoderName extends EncoderDecoderNoop<MyEncodeableUsingEncoderDecoderName> {

        /**
         * Required since is an EncoderDecoder
         */
        public MyEncodeableUsingEncoderDecoderName() {
        }
    }

    public void testEncodeableUsingEncoderDecoderName() {
        facetFactory.process(MyEncodeableUsingEncoderDecoderName.class, methodRemover, facetHolder);
        final EncodeableFacetAbstract encodeableFacet = (EncodeableFacetAbstract) facetHolder.getFacet(EncodeableFacet.class);
        assertEquals(MyEncodeableUsingEncoderDecoderName.class, encodeableFacet.getEncoderDecoderClass());
    }

    @Encodeable(encoderDecoderClass = MyEncodeableUsingEncoderDecoderClass.class)
    public static class MyEncodeableUsingEncoderDecoderClass extends EncoderDecoderNoop<MyEncodeableUsingEncoderDecoderClass> {

        /**
         * Required since is a EncoderDecoder.
         */
        public MyEncodeableUsingEncoderDecoderClass() {
        }
    }

    public void testEncodeableUsingEncoderDecoderClass() {
        facetFactory.process(MyEncodeableUsingEncoderDecoderClass.class, methodRemover, facetHolder);
        final EncodeableFacetAbstract encodeableFacet = (EncodeableFacetAbstract) facetHolder.getFacet(EncodeableFacet.class);
        assertEquals(MyEncodeableUsingEncoderDecoderClass.class, encodeableFacet.getEncoderDecoderClass());
    }

    public void testEncodeableMustBeAEncoderDecoder() {
    }

    @Encodeable(encoderDecoderClass = MyEncodeableWithoutNoArgConstructor.class)
    public static class MyEncodeableWithoutNoArgConstructor extends EncoderDecoderNoop<MyEncodeableWithoutNoArgConstructor> {

        public MyEncodeableWithoutNoArgConstructor(final int value) {
        }
    }

    public void testEncodeableHaveANoArgConstructor() {
        facetFactory.process(MyEncodeableWithoutNoArgConstructor.class, methodRemover, facetHolder);
        final EncodeableFacetAbstract encodeableFacet = (EncodeableFacetAbstract) facetHolder.getFacet(EncodeableFacet.class);
        assertNull(encodeableFacet);
    }

    @Encodeable(encoderDecoderClass = MyEncodeableWithoutPublicNoArgConstructor.class)
    public static class MyEncodeableWithoutPublicNoArgConstructor extends EncoderDecoderNoop<MyEncodeableWithoutPublicNoArgConstructor> {

        MyEncodeableWithoutPublicNoArgConstructor() {
        }

        public MyEncodeableWithoutPublicNoArgConstructor(final int value) {
        }
    }

    public void testEncodeableHaveAPublicNoArgConstructor() {
        facetFactory.process(MyEncodeableWithoutPublicNoArgConstructor.class, methodRemover, facetHolder);
        final EncodeableFacetAbstract encodeableFacet = (EncodeableFacetAbstract) facetHolder.getFacet(EncodeableFacet.class);
        assertNull(encodeableFacet);
    }

    @Encodeable()
    public static class MyEncodeableWithEncoderDecoderSpecifiedUsingConfiguration extends EncoderDecoderNoop<MyEncodeableWithEncoderDecoderSpecifiedUsingConfiguration> {

        /**
         * Required since is a EncoderDecoder.
         */
        public MyEncodeableWithEncoderDecoderSpecifiedUsingConfiguration() {
        }
    }

    public void testEncoderDecoderNameCanBePickedUpFromConfiguration() {
        final String className = "org.nakedobjects.nof.reflect.java.facets.object.encodeable.EncodeableFacetFactoryTest$MyEncodeableWithEncoderDecoderSpecifiedUsingConfiguration";
        propertiesConfiguration.add(EncoderDecoderUtil.ENCODER_DECODER_NAME_KEY_PREFIX + canonical(className) + EncoderDecoderUtil.ENCODER_DECODER_NAME_KEY_SUFFIX, className);
        facetFactory.process(MyEncodeableWithEncoderDecoderSpecifiedUsingConfiguration.class, methodRemover, facetHolder);
        final EncodeableFacetAbstract facet = (EncodeableFacetAbstract) facetHolder.getFacet(EncodeableFacet.class);
        assertNotNull(facet);
        assertEquals(MyEncodeableWithEncoderDecoderSpecifiedUsingConfiguration.class, facet.getEncoderDecoderClass());
    }

    public static class NonAnnotatedEncodeableEncoderDecoderSpecifiedUsingConfiguration extends EncoderDecoderNoop<NonAnnotatedEncodeableEncoderDecoderSpecifiedUsingConfiguration> {

        /**
         * Required since is a EncoderDecoder.
         */
        public NonAnnotatedEncodeableEncoderDecoderSpecifiedUsingConfiguration() {
        }
    }

    public void testNonAnnotatedEncodeableCanPickUpEncoderDecoderFromConfiguration() {
        final String className = "org.nakedobjects.nof.reflect.java.facets.object.encodeable.EncodeableFacetFactoryTest$NonAnnotatedEncodeableEncoderDecoderSpecifiedUsingConfiguration";
        propertiesConfiguration.add(EncoderDecoderUtil.ENCODER_DECODER_NAME_KEY_PREFIX + canonical(className) + EncoderDecoderUtil.ENCODER_DECODER_NAME_KEY_SUFFIX, className);
        facetFactory.process(NonAnnotatedEncodeableEncoderDecoderSpecifiedUsingConfiguration.class, methodRemover, facetHolder);
        final EncodeableFacetAbstract facet = (EncodeableFacetAbstract) facetHolder.getFacet(EncodeableFacet.class);
        assertNotNull(facet);
        assertEquals(NonAnnotatedEncodeableEncoderDecoderSpecifiedUsingConfiguration.class, facet.getEncoderDecoderClass());
    }

    private String canonical(final String className) {
        return className.replace('$', '.');
    }
}

package org.nakedobjects.nof.reflect.java.value;

import org.nakedobjects.noa.adapter.NakedObject;
import org.nakedobjects.noa.facets.object.encodeable.EncodeableFacet;
import org.nakedobjects.noa.facets.object.parseable.ParseableFacet;
import org.nakedobjects.nof.reflect.java.facets.object.encodeable.EncodeableFacetUsingEncoderDecoder;
import org.nakedobjects.nof.reflect.java.facets.object.parseable.ParseableFacetUsingParser;
import org.nakedobjects.nof.testsystem.ProxyTestCase;
import org.nakedobjects.nof.testsystem.TestProxySpecification;

public abstract class ValueSemanticsProviderAbstractTestCase extends ProxyTestCase {

    private ValueSemanticsProviderAbstract value;

    private EncodeableFacetUsingEncoderDecoder encodeableFacet;

    private ParseableFacetUsingParser parseableFacet;

    protected void setValue(final ValueSemanticsProviderAbstract value) {
        this.value = value;
        this.encodeableFacet = new EncodeableFacetUsingEncoderDecoder(value, null);
        this.parseableFacet = new ParseableFacetUsingParser(value, null);
    }

    protected ValueSemanticsProviderAbstract getValue() {
        return value;
    }

    protected EncodeableFacet getEncodeableFacet() {
        return encodeableFacet;
    }

    protected ParseableFacet getParseableFacet() {
        return parseableFacet;
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    protected void setupSpecification(final Class cls) {
        final TestProxySpecification specification = system.getSpecification(cls);
        specification.setupHasNoIdentity(true);
    }

    protected NakedObject createAdapter(final Object object) {
        return system.createAdapterForTransient(object);
    }

    public void testParseNull() throws Exception {
        try {
            value.parseTextEntry(null, null);
            fail();
        } catch (final IllegalArgumentException expected) {
        }
    }

    public void testParseEmptyString() throws Exception {
        final Object newValue = value.parseTextEntry(null, "");
        assertNull(newValue);
    }

    public void testDecodeNULL() throws Exception {
        final Object newValue = encodeableFacet.fromEncodedString(EncodeableFacetUsingEncoderDecoder.ENCODED_NULL);
        assertNull(newValue);
    }

    public void testEmptyEncoding() {
        assertEquals(EncodeableFacetUsingEncoderDecoder.ENCODED_NULL, encodeableFacet.toEncodedString(null));
    }

    public void testTitleOfForNullObject() {
        assertEquals("", value.displayTitleOf(null));
    }
}

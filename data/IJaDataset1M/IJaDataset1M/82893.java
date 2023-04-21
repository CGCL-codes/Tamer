package org.nakedobjects.metamodel.value;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import org.jmock.Expectations;
import org.jmock.integration.junit4.JMock;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.nakedobjects.metamodel.adapter.TextEntryParseException;
import org.nakedobjects.metamodel.facets.FacetHolder;
import org.nakedobjects.metamodel.facets.FacetHolderImpl;

@RunWith(JMock.class)
public class LongValueSemanticsProviderTest extends ValueSemanticsProviderAbstractTestCase {

    private LongValueSemanticsProviderAbstract value;

    private Object longObj;

    private FacetHolder holder;

    @Before
    public void setUpObjects() throws Exception {
        longObj = new Long(367322);
        allowMockAdapterToReturn(longObj);
        holder = new FacetHolderImpl();
        mockery.checking(new Expectations() {

            {
                allowing(mockConfiguration).getString("nakedobjects.value.format.long");
                will(returnValue(null));
            }
        });
        setValue(value = new LongWrapperValueSemanticsProvider(holder, mockConfiguration, mockSpecificationLoader, mockRuntimeContext));
    }

    @Test
    public void testInvalidParse() throws Exception {
        try {
            value.parseTextEntry(null, "one");
            fail();
        } catch (final TextEntryParseException expected) {
        }
    }

    @Test
    public void testOutputAsString() {
        assertEquals("367,322", value.displayTitleOf(longObj));
    }

    @Test
    public void testParse() throws Exception {
        final Object parsed = value.parseTextEntry(null, "120");
        assertEquals("120", parsed.toString());
    }

    @Test
    public void testParseWithBadlyFormattedEntry() throws Exception {
        final Object parsed = value.parseTextEntry(null, "1,20.0");
        assertEquals("120", parsed.toString());
    }

    @Test
    public void testEncode() throws Exception {
        assertEquals("367322", value.toEncodedString(longObj));
    }

    @Test
    public void test() throws Exception {
        final Object parsed = value.fromEncodedString("234");
        assertEquals("234", parsed.toString());
    }
}

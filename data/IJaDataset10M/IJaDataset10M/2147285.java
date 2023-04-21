package org.nakedobjects.nof.reflect.java.value;

import org.nakedobjects.noa.adapter.NakedObject;
import org.nakedobjects.noa.adapter.TextEntryParseException;
import org.nakedobjects.noa.facets.FacetHolder;
import org.nakedobjects.noa.facets.FacetHolderImpl;
import java.math.BigDecimal;

public class BigDecimalValueSemanticsProviderTest extends ValueSemanticsProviderAbstractTestCase {

    private BigDecimalValueSemanticsProvider value;

    private BigDecimal bigDecimal;

    private NakedObject bigDecimalNO;

    private FacetHolder holder;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        bigDecimal = new BigDecimal("34132.199");
        bigDecimalNO = createAdapter(bigDecimal);
        holder = new FacetHolderImpl();
        setValue(value = new BigDecimalValueSemanticsProvider(holder));
    }

    public void testParseValidString() throws Exception {
        final Object newValue = value.parseTextEntry(null, "2142342334");
        assertEquals(new BigDecimal(2142342334L), newValue);
    }

    public void testParseInvalidString() throws Exception {
        try {
            value.parseTextEntry(null, "214xxx2342334");
            fail();
        } catch (final TextEntryParseException expected) {
        }
    }

    public void testTitleOf() {
        assertEquals("34,132.199", value.displayTitleOf(bigDecimal));
    }

    public void testEncode() {
        assertEquals("34132.199", value.toEncodedString(bigDecimal));
    }

    public void testDecode() throws Exception {
        final Object newValue = value.fromEncodedString("4322.89991");
        assertEquals(new BigDecimal("4322.89991"), newValue);
    }
}

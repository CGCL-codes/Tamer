package org.nakedobjects.metamodel.value;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import org.jmock.Expectations;
import org.jmock.integration.junit4.JMock;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.nakedobjects.applib.adapters.EncodingException;
import org.nakedobjects.applib.value.TestClock;
import org.nakedobjects.applib.value.Time;
import org.nakedobjects.metamodel.facets.FacetHolder;
import org.nakedobjects.metamodel.facets.FacetHolderImpl;

@RunWith(JMock.class)
public class TimeValueSemanticsProviderTest extends ValueSemanticsProviderAbstractTestCase {

    private TimeValueSemanticsProvider adapter;

    private Time time;

    private FacetHolder holder;

    @Before
    public void setUpObjects() throws Exception {
        mockery.checking(new Expectations() {

            {
                allowing(mockConfiguration).getString("nakedobjects.value.format.time");
                will(returnValue(null));
            }
        });
        TestClock.initialize();
        setupSpecification(Time.class);
        time = new Time(8, 13);
        holder = new FacetHolderImpl();
        setValue(adapter = new TimeValueSemanticsProvider(holder, mockConfiguration, mockSpecificationLoader, mockRuntimeContext));
    }

    @Test
    public void testTimeAsEncodedString() throws Exception {
        assertEquals("081300000", adapter.toEncodedString(time));
    }

    @Test
    public void testParseEntryOfHoursAfterTime() throws Exception {
        final Object parsed = adapter.parseTextEntry(time, "+5H");
        assertEquals(new Time(13, 13), parsed);
    }

    @Test
    public void testParseEntryOfHoursAfterNow() throws Exception {
        final Object parsed = adapter.parseTextEntry(null, "+5H");
        assertEquals(new Time(2, 30), parsed);
    }

    @Test
    public void testParseEntryOfHoursBeforeTime() throws Exception {
        final Object parsed = adapter.parseTextEntry(time, "-7H");
        assertEquals(new Time(1, 13), parsed);
    }

    @Test
    public void testParseEntryOfHoursBeforeToNow() throws Exception {
        final Object parsed = adapter.parseTextEntry(null, "-5H");
        assertEquals(new Time(16, 30), parsed);
    }

    @Test
    public void testParseEntryOfKeywordNow() throws Exception {
        final Object parsed = adapter.parseTextEntry(time, "now");
        assertEquals(new Time(), parsed);
    }

    @Test
    public void testRestoreTime() throws Exception {
        final Object parsed = adapter.fromEncodedString("213000000");
        assertEquals(new Time(21, 30), parsed);
    }

    @Test
    public void testRestoreOfInvalidDatal() throws Exception {
        try {
            adapter.fromEncodedString("two ten");
            fail();
        } catch (final EncodingException expected) {
        }
    }
}

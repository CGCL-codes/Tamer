package com.volantis.styling.integration.functions.counter;

import com.volantis.mcs.themes.StyleInteger;
import com.volantis.mcs.themes.StyleValue;
import com.volantis.mcs.themes.properties.ListStyleTypeKeywords;
import com.volantis.styling.impl.functions.counter.CounterFormatter;
import com.volantis.styling.impl.functions.counter.DecimalFormatter;
import com.volantis.synergetics.testtools.TestCaseAbstract;

/**
 * Test cases for {@link com.volantis.styling.impl.functions.counter.DecimalFormatter}.
 */
public class DecimalFormatterTestCase extends TestCaseAbstract {

    /**
     * Ensure that the decimal formatter returns a {@link StyleInteger}.
     *
     * @throws Exception
     */
    public void testReturnsStyleInteger() throws Exception {
        CounterFormatter formatter = new DecimalFormatter();
        StyleValue result = formatter.formatAsStyleValue(ListStyleTypeKeywords.DECIMAL, 4);
        assertTrue("Must be StyleInteger", result instanceof StyleInteger);
    }
}

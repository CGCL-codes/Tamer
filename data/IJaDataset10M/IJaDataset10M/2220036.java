package com.volantis.mcs.xdime.ticker;

import com.volantis.mcs.protocols.ticker.attributes.ChannelsCountAttributes;

/**
 * Test the widget:carousel element
 */
public class ChannelsCountTestCase extends TickerElementTestCaseAbstract {

    protected void setUp() throws Exception {
        super.setUp();
        addDefaultElementExpectations(ChannelsCountAttributes.class);
        fakeFeedPoller();
    }

    protected String getElementName() {
        return TickerElements.CHANNELS_COUNT.getLocalName();
    }

    public void testWidget() throws Exception {
        executeTest();
    }
}

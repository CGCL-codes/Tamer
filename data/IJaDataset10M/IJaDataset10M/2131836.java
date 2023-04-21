package com.volantis.mcs.xdime.ticker.response;

import com.volantis.mcs.protocols.ProtocolException;
import com.volantis.mcs.protocols.ticker.response.TickerResponseModule;
import com.volantis.mcs.protocols.ticker.response.attributes.FeedPollerAttributes;
import com.volantis.mcs.xdime.XDIMEContextInternal;
import com.volantis.mcs.xdime.ticker.TickerResponseElements;

/**
 * FeedPoller response element.  
 */
public class FeedPollerElement extends TickerResponseElement {

    public FeedPollerElement(XDIMEContextInternal context) {
        super(TickerResponseElements.FEED_POLLER, context);
        protocolAttributes = new FeedPollerAttributes();
    }

    public void callOpenOnModule(TickerResponseModule module) throws ProtocolException {
        module.openFeedPoller((FeedPollerAttributes) protocolAttributes);
    }

    public void callCloseOnModule(TickerResponseModule module) throws ProtocolException {
        module.closeFeedPoller((FeedPollerAttributes) protocolAttributes);
    }
}

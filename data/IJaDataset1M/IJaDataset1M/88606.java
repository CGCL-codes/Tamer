package com.volantis.mcs.protocols.ticker.renderers;

import java.io.IOException;
import java.io.StringWriter;
import com.volantis.mcs.protocols.MCSAttributes;
import com.volantis.mcs.protocols.ProtocolException;
import com.volantis.mcs.protocols.SpanAttributes;
import com.volantis.mcs.protocols.VolantisProtocol;
import com.volantis.mcs.protocols.ticker.attributes.ChannelsCountAttributes;

/**
 * Renderer for ChannelsCount element 
 */
public class ChannelsCountDefaultRenderer extends ElementDefaultRenderer {

    /**
     * Attributes of the rendered span element.
     */
    private SpanAttributes spanAttributes;

    public void doRenderOpen(VolantisProtocol protocol, MCSAttributes attributes) throws ProtocolException {
        if (!isWidgetSupported(protocol)) {
            return;
        }
        require(WIDGET_TICKER, protocol, attributes);
        spanAttributes = new SpanAttributes();
        spanAttributes.copy(attributes);
        if (spanAttributes.getId() == null) {
            spanAttributes.setId(protocol.getMarinerPageContext().generateUniqueFCID());
        }
        protocol.writeOpenSpan(spanAttributes);
    }

    public void doRenderClose(VolantisProtocol protocol, MCSAttributes attributes) throws ProtocolException {
        if (!isWidgetSupported(protocol)) {
            return;
        }
        protocol.writeCloseSpan(spanAttributes);
        ChannelsCountAttributes channelsCountAttributes = (ChannelsCountAttributes) attributes;
        StringWriter scriptWriter = new StringWriter();
        scriptWriter.write("Ticker.createChannelsCount({");
        scriptWriter.write("id:" + createJavaScriptString(spanAttributes.getId()));
        if (channelsCountAttributes.getRead() != null) {
            scriptWriter.write(",read:" + (channelsCountAttributes.getRead().equals("no") ? "false" : "true"));
        }
        if (channelsCountAttributes.getFollowed() != null) {
            scriptWriter.write(",followed:" + (channelsCountAttributes.getFollowed().equals("no") ? "false" : "true"));
        }
        scriptWriter.write("})");
        addUsedFeedPollerId(protocol);
        writeJavaScript(scriptWriter.toString());
    }
}

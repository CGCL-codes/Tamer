package com.volantis.mcs.xdime;

import com.volantis.mcs.context.ContextInternals;
import com.volantis.mcs.context.MarinerPageContext;
import com.volantis.mcs.protocols.DOMOutputBuffer;

/**
 * Concrete implementation of {@link DataHandlingStrategy} which stores any
 * data (character data or markup) encountered while processing.
 */
public class StoreDataStrategy implements DataHandlingStrategy {

    /**
     * The output buffer to which content will be directed.
     */
    private DOMOutputBuffer content = null;

    public void handleData(XDIMEContextInternal context) {
        MarinerPageContext pageContext = ContextInternals.getMarinerPageContext(context.getInitialRequestContext());
        content = (DOMOutputBuffer) pageContext.getProtocol().getOutputBufferFactory().createOutputBuffer();
        pageContext.pushOutputBuffer(content);
    }

    public String getCharacterData() {
        return content.getPCDATAValue();
    }

    public void stopHandlingData(XDIMEContextInternal context) {
        MarinerPageContext pageContext = ContextInternals.getMarinerPageContext(context.getInitialRequestContext());
        pageContext.popOutputBuffer(content);
    }
}

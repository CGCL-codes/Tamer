package com.volantis.mcs.papi;

import com.volantis.synergetics.log.LogDispatcher;
import com.volantis.mcs.context.MarinerRequestContext;
import java.io.Writer;

/**
 * This class provides the common code for delegating to a real PAPI implemntion class.
 * <p>This class must not be part of the public API</p>
 */
public class AbstractPAPIDelegatingElement implements DeprecatedPAPIElement {

    /**
     * The delegated PAPI element implementation
     */
    protected PAPIElement delegate;

    public AbstractPAPIDelegatingElement(PAPIElementFactory papiElementFactory) {
        delegate = papiElementFactory.createPAPIElement();
    }

    public int elementStart(MarinerRequestContext context, PAPIAttributes papiAttributes) throws PAPIException {
        return delegate.elementStart(context, papiAttributes);
    }

    public void elementContent(MarinerRequestContext context, PAPIAttributes papiAttributes, String content) throws PAPIException {
        delegate.elementContent(context, papiAttributes, content);
    }

    public Writer getContentWriter(MarinerRequestContext context, PAPIAttributes papiAttributes) throws PAPIException {
        return delegate.getContentWriter(context, papiAttributes);
    }

    public int elementEnd(MarinerRequestContext context, PAPIAttributes papiAttributes) throws PAPIException {
        return delegate.elementEnd(context, papiAttributes);
    }

    public void elementReset(MarinerRequestContext context) {
        delegate.elementReset(context);
    }

    public void elementDirect(MarinerRequestContext context, String content) throws PAPIException {
        ((DeprecatedPAPIElement) delegate).elementDirect(context, content);
    }

    public Writer getDirectWriter(MarinerRequestContext context) throws PAPIException {
        return ((DeprecatedPAPIElement) delegate).getDirectWriter(context);
    }
}

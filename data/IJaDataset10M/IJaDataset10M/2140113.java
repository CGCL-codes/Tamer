package com.volantis.mcs.papi.impl;

import com.volantis.mcs.context.ContextInternals;
import com.volantis.mcs.context.MarinerPageContext;
import com.volantis.mcs.context.MarinerRequestContext;
import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.papi.PAPIAttributes;
import com.volantis.mcs.papi.PAPIException;
import com.volantis.mcs.papi.SpanAttributes;
import com.volantis.mcs.protocols.MCSAttributes;
import com.volantis.mcs.protocols.ProtocolException;
import com.volantis.mcs.protocols.VolantisProtocol;
import com.volantis.mcs.protocols.assets.TextAssetReference;
import com.volantis.mcs.runtime.policies.PolicyReferenceResolver;
import com.volantis.synergetics.localization.ExceptionLocalizer;
import com.volantis.synergetics.log.LogDispatcher;

/**
 * The span element.
 */
public final class SpanElementImpl extends AttrsElementImpl {

    /**
     * Used for logging
     */
    private static final LogDispatcher logger = LocalizationFactory.createLogger(SpanElementImpl.class);

    /**
     * Used to retrieve localized exception messages.
     */
    private static final ExceptionLocalizer exceptionLocalizer = LocalizationFactory.createExceptionLocalizer(SpanElementImpl.class);

    /**
     * The attributes to pass to the protocol methods.
     */
    com.volantis.mcs.protocols.SpanAttributes pattributes;

    /**
     * Create a new <code>SpanElement</code>.
     */
    public SpanElementImpl() {
        pattributes = new com.volantis.mcs.protocols.SpanAttributes();
    }

    MCSAttributes getMCSAttributes() {
        return pattributes;
    }

    public int styleElementStart(MarinerRequestContext context, PAPIAttributes papiAttributes) throws PAPIException {
        MarinerPageContext pageContext = ContextInternals.getMarinerPageContext(context);
        SpanAttributes attributes = (SpanAttributes) papiAttributes;
        PolicyReferenceResolver resolver = pageContext.getPolicyReferenceResolver();
        TextAssetReference srcReference = resolver.resolveQuotedTextExpression(attributes.getSrc());
        if (srcReference != null && srcReference.isPolicy()) {
            pattributes.setSrc(srcReference);
        }
        int result = super.styleElementStart(context, (SpanAttributes) papiAttributes);
        if (result != SKIP_ELEMENT_BODY) {
            VolantisProtocol protocol = pageContext.getProtocol();
            result = protocol.skipElementBody() ? SKIP_ELEMENT_BODY : result;
        }
        return result;
    }

    void writeOpenMarkup(VolantisProtocol protocol) throws PAPIException {
        try {
            protocol.writeOpenSpan(pattributes);
        } catch (ProtocolException e) {
            logger.error("open-span-error", e);
            throw new PAPIException(exceptionLocalizer.format("open-span-error"), e);
        }
    }

    void writeCloseMarkup(VolantisProtocol protocol) {
        protocol.writeCloseSpan(pattributes);
    }

    public void elementReset(MarinerRequestContext context) {
        pattributes.resetAttributes();
        super.elementReset(context);
    }
}

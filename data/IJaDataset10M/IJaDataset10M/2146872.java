package com.volantis.mcs.xdime.xhtml2;

import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.protocols.ProtocolException;
import com.volantis.mcs.protocols.SpanAttributes;
import com.volantis.mcs.protocols.VolantisProtocol;
import com.volantis.mcs.xdime.XDIMEAttributes;
import com.volantis.mcs.xdime.XDIMEContextInternal;
import com.volantis.mcs.xdime.XDIMEException;
import com.volantis.mcs.xdime.XDIMEResult;
import com.volantis.synergetics.localization.ExceptionLocalizer;
import com.volantis.synergetics.log.LogDispatcher;

/**
 * XHTML V2 Code element object.
 *
 * Currently Just maps to a <span>.
 */
public class VariableElement extends XHTML2Element {

    /**
     * Used for logging.
     */
    private static final LogDispatcher logger = LocalizationFactory.createLogger(CodeElement.class);

    /**
     * Used to retrieve localized exception messages.
     */
    private static final ExceptionLocalizer exceptionLocalizer = LocalizationFactory.createExceptionLocalizer(CodeElement.class);

    public VariableElement(XDIMEContextInternal context) {
        super(XHTML2Elements.VAR, context);
        protocolAttributes = new SpanAttributes();
    }

    protected XDIMEResult callOpenOnProtocol(XDIMEContextInternal context, XDIMEAttributes attributes) throws XDIMEException {
        VolantisProtocol protocol = getProtocol(context);
        try {
            protocol.writeOpenSpan((SpanAttributes) protocolAttributes);
        } catch (ProtocolException e) {
            logger.error("rendering-error", getTagName(), e);
            throw new XDIMEException(exceptionLocalizer.format("rendering-error", getTagName()), e);
        }
        return XDIMEResult.PROCESS_ELEMENT_BODY;
    }

    protected void callCloseOnProtocol(XDIMEContextInternal context) {
        VolantisProtocol protocol = getProtocol(context);
        protocol.writeCloseSpan((SpanAttributes) protocolAttributes);
    }
}

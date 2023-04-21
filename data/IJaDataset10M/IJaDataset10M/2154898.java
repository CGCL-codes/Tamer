package com.volantis.mcs.xdime.xhtml2;

import com.volantis.mcs.context.MarinerPageContext;
import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.protocols.DOMOutputBuffer;
import com.volantis.mcs.xdime.UnstyledStrategy;
import com.volantis.mcs.xdime.XDIMEAttributes;
import com.volantis.mcs.xdime.XDIMEContextImpl;
import com.volantis.mcs.xdime.XDIMEContextInternal;
import com.volantis.mcs.xdime.XDIMEException;
import com.volantis.mcs.xdime.XDIMEResult;
import com.volantis.synergetics.log.LogDispatcher;
import java.util.Stack;

/**
 * XHTML V2 Title element object.
 */
public class TitleElement extends XHTML2Element {

    /**
     * Used for logging
     */
    private static final LogDispatcher logger = LocalizationFactory.createLogger(TitleElement.class);

    public TitleElement(XDIMEContextInternal context) {
        super(XHTML2Elements.TITLE, UnstyledStrategy.STRATEGY, context);
    }

    /**
     * Buffer to hold the content seen inside this object.
     */
    private DOMOutputBuffer bodyContentBuffer;

    protected XDIMEResult callOpenOnProtocol(XDIMEContextInternal context, XDIMEAttributes attributes) throws XDIMEException {
        bodyContentBuffer = (DOMOutputBuffer) createOutputBuffer(context);
        getPageContext(context).pushOutputBuffer(bodyContentBuffer);
        return XDIMEResult.PROCESS_ELEMENT_BODY;
    }

    protected void callCloseOnProtocol(XDIMEContextInternal context) {
        final MarinerPageContext pageContext = getPageContext(context);
        pageContext.popOutputBuffer(bodyContentBuffer);
        Stack elementStack = ((XDIMEContextImpl) context).getStack();
        int stackSize = elementStack.size();
        if (stackSize < 2) {
            throw new IllegalStateException("No html element found");
        }
        Object grandparent = elementStack.get(elementStack.size() - 2);
        if (!(grandparent instanceof HtmlElement)) {
            throw new IllegalStateException("No html element found");
        }
        HtmlElement htmlElement = (HtmlElement) grandparent;
        String title = bodyContentBuffer.getPCDATAValue();
        if (title != null) {
            if (logger.isDebugEnabled()) {
                logger.debug("Title: " + title);
            }
            htmlElement.setTitle(title);
        }
    }
}

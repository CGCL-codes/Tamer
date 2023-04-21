package com.volantis.mcs.xdime.diselect;

import com.volantis.mcs.expression.SelectState;
import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.xdime.XDIMEContextInternal;
import com.volantis.mcs.xdime.XDIMEException;
import com.volantis.mcs.xdime.XDIMEAttributes;
import com.volantis.mcs.xdime.XDIMEAttribute;
import com.volantis.mcs.xdime.schema.DISelectElements;
import com.volantis.synergetics.localization.ExceptionLocalizer;

/**
 * Implementation of an &lt;otherwise&gt; element.
 */
public class OtherwiseElementImpl extends XDIMESelectBodyElement {

    /**
     * Used to retrieve localized exception messages.
     */
    private static final ExceptionLocalizer EXCEPTION_LOCALIZER = LocalizationFactory.createExceptionLocalizer(OtherwiseElementImpl.class);

    public OtherwiseElementImpl(XDIMEContextInternal context) {
        super(DISelectElements.OTHERWISE, AttributeUsage.ILLEGAL, context);
    }

    protected boolean evaluateExpression(XDIMEContextInternal context, XDIMEAttributes attributes) throws XDIMEException {
        String expr = getAttribute(XDIMEAttribute.EXPR, attributes);
        if (expr != null) {
            throw new IllegalStateException("Expr attribute is illegal on " + getElementType());
        }
        SelectState state = getState(context);
        if (state.isOtherwiseExecuted()) {
            throw new XDIMEException(EXCEPTION_LOCALIZER.format("too-many-otherwises"));
        }
        final boolean matched = !state.isMatched();
        if (matched) {
            state.setOtherwiseExecuted();
        }
        return matched;
    }
}

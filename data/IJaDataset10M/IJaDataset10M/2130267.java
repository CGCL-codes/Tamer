package com.volantis.mcs.xdime.response;

import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.xdime.XDIMEContextInternal;
import com.volantis.mcs.xdime.widgets.WidgetElement;
import com.volantis.mcs.xml.schema.model.ElementType;
import com.volantis.synergetics.localization.ExceptionLocalizer;
import com.volantis.synergetics.log.LogDispatcher;

/**
 * Base class for all response elements, which uses renderers from Widget module.
 */
public class WidgetResponseElement extends WidgetElement {

    /**
     * Used for logging.
     */
    private static final LogDispatcher logger = LocalizationFactory.createLogger(WidgetResponseElement.class);

    /**
     * Used to retrieve localized exception messages.
     */
    private static final ExceptionLocalizer exceptionLocalizer = LocalizationFactory.createExceptionLocalizer(WidgetResponseElement.class);

    public WidgetResponseElement(ElementType type, XDIMEContextInternal context) {
        super(type, context);
    }
}

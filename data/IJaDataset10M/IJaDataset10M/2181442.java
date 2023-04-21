package com.volantis.mcs.marlin.sax;

import java.util.HashMap;
import java.util.Map;
import com.volantis.synergetics.log.LogDispatcher;
import com.volantis.mcs.localization.LocalizationFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import com.volantis.mcs.papi.PAPIAttributes;
import com.volantis.mcs.papi.PAPIElement;
import com.volantis.mcs.pickle.PickleInlineAttributes;
import com.volantis.mcs.pickle.impl.PickleInlineElementImpl;
import com.volantis.mcs.pickle.impl.PickleNativeElementImpl;
import com.volantis.mcs.pickle.impl.PickleInlineElementImpl;
import com.volantis.mcs.pickle.impl.PickleNativeElementImpl;

/**
 * PickleElementHandler implements MarlinElementHandler for Pickle elements.
 */
public class PickleElementHandler extends AbstractElementHandler {

    /**
     * The copyright statement.
     */
    private static String mark = "(c) Volantis Systems Ltd 2003.";

    /**
     * Used for logging
     */
    private static final LogDispatcher logger = LocalizationFactory.createLogger(PickleElementHandler.class);

    /**
     * The name of the pickle element
     */
    private String elementName;

    /**
     * Create a new <code>PickleElementHandler</code>.
     */
    public PickleElementHandler() {
    }

    public PAPIAttributes createPAPIAttributes(PAPIContentHandlerContext context) {
        return new PickleInlineAttributes();
    }

    public PAPIElement createPAPIElement(PAPIContentHandlerContext context) {
        PAPIElement element;
        if (context.getNativeMarkupDepth() == 0) {
            element = new PickleInlineElementImpl();
        } else {
            element = new PickleNativeElementImpl();
        }
        return element;
    }

    public void initializePAPIAttributes(PAPIContentHandlerContext context, Attributes saxAttributes, PAPIAttributes papiAttributes) throws SAXException {
        PickleInlineAttributes attributes = (PickleInlineAttributes) papiAttributes;
        attributes.setElementName(elementName);
        if (logger.isDebugEnabled()) {
            logger.debug("setElementName (" + elementName + ")");
        }
        Map attrsMap = new HashMap();
        for (int i = 0; i < saxAttributes.getLength(); i++) {
            String name = saxAttributes.getLocalName(i);
            String value = saxAttributes.getValue(i);
            attrsMap.put(name, value);
            if (logger.isDebugEnabled()) {
                logger.debug("setAttributes (" + name + "=" + value + ")");
            }
        }
        attributes.setAttributes(attrsMap);
    }

    public boolean canContainCharacterData() {
        return true;
    }

    /**
     * Sets the pickle element's name. We have to do this here as the element's
     * name cannot be obtained from org.xml.sax.Attributes object used by
     * initializePAPIAttributes();
     * @param localName
     */
    void setElementName(String localName) {
        elementName = localName;
    }
}

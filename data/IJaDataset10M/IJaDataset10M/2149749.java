package com.volantis.mcs.protocols.menu.shared.renderer;

import com.volantis.mcs.dom.DOMFactory;
import com.volantis.mcs.dom.Element;
import com.volantis.mcs.dom.debug.DOMUtilities;
import com.volantis.mcs.protocols.DOMOutputBuffer;
import com.volantis.mcs.protocols.EventAttributes;
import com.volantis.mcs.protocols.EventConstants;
import com.volantis.mcs.protocols.ImageAttributes;
import com.volantis.mcs.protocols.assets.ScriptAssetReference;
import com.volantis.shared.throwable.ExtendedRuntimeException;

/**
 * A test implementation of {@link DeprecatedImageOutput} which writes out
 * the image attributes as simply as possible.
 * <p>
 * This also allows retrieval of the last rendered image text to allow 
 * ease of constructing expected results for test cases which are not testing
 * usage of this interface directly.
 */
public class TestDeprecatedImageOutput extends AbstractTestMarkupOutput implements DeprecatedImageOutput {

    /**
     * Factory to use to create DOM objects.
     */
    protected DOMFactory domFactory = DOMFactory.getDefaultInstance();

    /**
     * The root element of the last content we created during rendering.
     */
    private Element lastElement;

    public void outputImage(DOMOutputBuffer dom, ImageAttributes attributes) {
        Element e = domFactory.createStyledElement(attributes.getStyles());
        e.setName("test-image");
        EventAttributes events = attributes.getEventAttributes(false);
        if (events != null) {
            for (int i = 0; i < EventConstants.MAX_EVENTS; i++) {
                ScriptAssetReference event = events.getEvent(i);
                if (event != null) {
                    e.setAttribute("event-" + i, event.getScript());
                }
            }
        }
        if (attributes.getSrc() != null) {
            e.setAttribute("src", attributes.getSrc());
        }
        addCoreAttributes(attributes, e);
        dom.addElement(e);
        lastElement = e;
    }

    /**
     * Returns the text of the last rendered image.
     */
    public String getLastRenderedText() {
        try {
            return DOMUtilities.toString(lastElement);
        } catch (Exception ex) {
            throw new ExtendedRuntimeException(ex);
        }
    }
}

package jgb.handlers.swing;

import org.xml.sax.SAXException;
import javax.swing.*;
import java.awt.*;
import java.util.Map;

/**
 * @since v0.3.1a
 * @author Francois Beausoleil, <a href="mailto:fbos@users.sourceforge.net">fbos@users.sourceforge.net</a>
 */
public class StrutTagHandler extends AbstractControlTagHandler {

    /**
     * The name of the width attribute.
     */
    protected static final String ATTR_WIDTH = "width";

    /**
     * The name of the height attribute.
     */
    protected static final String ATTR_HEIGHT = "height";

    protected void enterElement(Map atts) throws SAXException {
        String widthString = (String) atts.get(ATTR_WIDTH);
        String heightString = (String) atts.get(ATTR_HEIGHT);
        Component comp = null;
        if (widthString == null && heightString == null) {
            Exception cause = new IllegalArgumentException("One of width or " + "height must be mentionned on strut elements.");
            throwParsingException(cause);
        } else if (widthString != null && heightString == null) {
            int width = new Integer(widthString).intValue();
            comp = Box.createHorizontalStrut(width);
        } else if (widthString == null) {
            int height = new Integer(heightString).intValue();
            comp = Box.createVerticalStrut(height);
        } else {
            int width = new Integer(widthString).intValue();
            int height = new Integer(heightString).intValue();
            comp = Box.createRigidArea(new Dimension(width, height));
        }
        pushCurrentControl(comp);
    }

    protected void exitElement() throws SAXException {
        popCurrentControl();
    }

    protected String getDefaultPackagePrefix() {
        throw new IllegalStateException();
    }
}

package jgb.handlers.swing;

import org.xml.sax.SAXException;
import javax.swing.*;
import java.util.Map;

/**
 * @author Francois Beausoleil, <a href="mailto:fbos@users.sourceforge.net">fbos@users.sourceforge.net</a>
 */
public class MenuBarTagHandler extends AbstractControlTagHandler {

    protected void enterElement(Map atts) throws SAXException {
        final JFrame frame = (JFrame) getCurrentObject();
        final JMenuBar menubar = new JMenuBar();
        frame.setJMenuBar(menubar);
        if (atts.containsKey(ATTR_ID)) {
            putComponent((String) atts.get(ATTR_ID), menubar);
        }
        pushCurrentObject((String) atts.get(ATTR_ID), menubar);
    }

    protected void exitElement() throws SAXException {
        popCurrentObject();
    }

    protected String getDefaultPackagePrefix() {
        throw new IllegalStateException();
    }
}

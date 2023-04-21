package jgb.handlers.swing;

import org.xml.sax.SAXException;
import java.util.Map;

public class NullTagHandler extends AbstractValueTagHandler {

    protected void enterElement(Map atts) throws SAXException {
        String className = (String) atts.get(ATTR_CLASS);
        try {
            Class clazz = Class.forName(className);
            updateContextParameters(clazz, null);
        } catch (ClassNotFoundException cnfe) {
            throwParsingException("Declared class could not found: " + className, cnfe);
        }
    }

    protected void exitElement() throws SAXException {
    }
}

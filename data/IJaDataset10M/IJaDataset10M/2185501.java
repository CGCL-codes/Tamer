package gov.nasa.jpf;

import gov.nasa.jpf.Path;
import org.xml.sax.*;
import org.xml.sax.helpers.*;

/**
 * DOCUMENT ME!
 */
public class BootstrapXMLTraceHandler extends DefaultHandler {

    XMLTraceHandler realHandler;

    Path path;

    public Path getPath() {
        return path;
    }

    public void characters(char[] text, int start, int length) {
        if (realHandler != null) {
            realHandler.characters(text, start, length);
        }
    }

    public void endElement(String namespaceURI, String localName, String qualifiedName) throws SAXException {
        if (realHandler != null) {
            realHandler.endElement(namespaceURI, localName, qualifiedName);
            if (localName.equals("Trace")) {
                path = realHandler.getPath();
            }
        }
    }

    public void startElement(String namespaceURI, String localName, String qualifiedName, Attributes atts) throws SAXException {
        if (localName.equals("Trace")) {
            String clsName = atts.getValue("Handler");
            try {
                Class clazz = Class.forName(clsName);
                realHandler = (XMLTraceHandler) clazz.newInstance();
            } catch (Exception x) {
                throw new JPFException("cannot instantiate traceHandler class " + clsName + " : " + x);
            }
        }
        if (realHandler != null) {
            realHandler.startElement(namespaceURI, localName, qualifiedName, atts);
        }
    }
}

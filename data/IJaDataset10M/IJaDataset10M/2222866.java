package nu.staldal.xtree;

import org.xml.sax.*;

/**
 * Handle one XTree Element.
 */
public interface ElementHandler {

    public void processElement(Element el) throws SAXException;
}

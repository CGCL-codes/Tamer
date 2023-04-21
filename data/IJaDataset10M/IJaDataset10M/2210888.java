package fi.iki.asb.util.config.handler;

import java.io.*;
import java.net.*;
import java.util.*;
import java.lang.reflect.*;
import javax.xml.parsers.*;
import org.xml.sax.*;
import org.w3c.dom.*;
import fi.iki.asb.util.config.*;

/**
 * This class handled &lt;string&gt; elements.
 *
 * @author Antti S. Brax
 * @version 1.0
 */
public class ElementHandler_string extends ElementHandler {

    /**
     * Handle a &lt;string&gt; element.
     *
     * @param elem the &lt;string&gt; element
     * @param conf the configuration that is loaded
     */
    public Value handle(Node elem, Map conf) throws XmlConfigException {
        String value = getTextContent(elem, conf);
        return new Value(value, String.class);
    }
}

package org.gjt.sp.util;

import java.io.InputStream;
import java.io.BufferedInputStream;
import java.io.IOException;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

/**
 * XML utility methods that only depend on the JDK.
 *
 * @author Marcelo Vanzin
 * @version $Id: XMLUtilities.java 12684 2008-05-20 20:25:41Z kpouer $
 * @since 4.3pre6
 */
public class XMLUtilities {

    /**
	 * Converts &lt;, &gt;, &amp; in the string to their HTML entity
	 * equivalents.
	 *
	 * <p>If <code>xml11</code> is true, then character entities
	 * are used to convert illegal XML characters (mainly ASCII
	 * control characters).</p>
	 *
	 * @param str The string
	 * @param xml11 Whether to allow XML 1.1 constructs.
	 */
    public static String charsToEntities(String str, boolean xml11) {
        StringBuilder buf = new StringBuilder(str.length());
        for (int i = 0; i < str.length(); i++) {
            char ch = str.charAt(i);
            if (xml11 && ch < 32 && ch != '\r' && ch != '\n' && ch != '\t') {
                buf.append("&#").append((int) ch).append(';');
                continue;
            }
            switch(ch) {
                case '<':
                    buf.append("&lt;");
                    break;
                case '>':
                    buf.append("&gt;");
                    break;
                case '&':
                    buf.append("&amp;");
                    break;
                default:
                    buf.append(ch);
                    break;
            }
        }
        return buf.toString();
    }

    /**
	 * Convenience method for parsing an XML file. This method will
	 * wrap the resource in an InputSource and set the source's
	 * systemId to "jedit.jar" (so the source should be able to
	 * handle any external entities by itself).
	 *
	 * <p>SAX Errors are caught and are not propagated to the caller;
	 * instead, an error message is printed to jEdit's activity
	 * log. So, if you need custom error handling, <b>do not use
	 * this method</b>.
	 *
	 * <p>The given stream is closed before the method returns,
	 * regardless whether there were errors or not.</p>
	 *
	 * @return true if any error occured during parsing, false if success.
	 */
    public static boolean parseXML(InputStream in, DefaultHandler handler) throws IOException {
        try {
            XMLReader parser = XMLReaderFactory.createXMLReader();
            InputSource isrc = new InputSource(new BufferedInputStream(in));
            parser.setContentHandler(handler);
            parser.setDTDHandler(handler);
            parser.setEntityResolver(handler);
            parser.setErrorHandler(handler);
            parser.parse(isrc);
        } catch (SAXParseException se) {
            int line = se.getLineNumber();
            Log.log(Log.ERROR, XMLUtilities.class, "while parsing from " + in + ": SAXParseException: line " + line + ": ", se);
            return true;
        } catch (SAXException e) {
            Log.log(Log.ERROR, XMLUtilities.class, e);
            return true;
        } finally {
            try {
                if (in != null) in.close();
            } catch (IOException io) {
                Log.log(Log.ERROR, XMLUtilities.class, io);
            }
        }
        return false;
    }

    /**
	 * Tries to find the given systemId in the context of the given
	 * class. If the given systemId ends with the given test string,
	 * then try to load a resource using the Class's
	 * <code>getResourceAsStream()</code> method using the test string
	 * as the resource.
	 *
	 * <p>This is used a lot internally while parsing XML files used
	 * by jEdit, but anyone is free to use the method if it sounds
	 * usable.</p>
	 */
    public static InputSource findEntity(String systemId, String test, Class where) {
        if (systemId != null && systemId.endsWith(test)) {
            try {
                return new InputSource(new BufferedInputStream(where.getResourceAsStream(test)));
            } catch (Exception e) {
                Log.log(Log.ERROR, XMLUtilities.class, "Error while opening " + test + ':');
                Log.log(Log.ERROR, XMLUtilities.class, e);
            }
        }
        return null;
    }

    private XMLUtilities() {
    }
}

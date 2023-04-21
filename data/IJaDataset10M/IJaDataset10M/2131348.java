package org.cyberneko.html.parsers;

import org.apache.xerces.xni.Augmentations;
import org.apache.xerces.xni.XNIException;
import org.cyberneko.html.HTMLConfiguration;
import org.cyberneko.html.xercesbridge.XercesBridge;

/**
 * A DOM parser for HTML documents.
 *
 * @author Andy Clark
 *
 * @version $Id: DOMParser.java,v 1.5 2005/02/14 03:56:54 andyc Exp $
 */
public class DOMParser extends org.apache.xerces.parsers.DOMParser {

    /** Default constructor. */
    public DOMParser() {
        super(new HTMLConfiguration());
        try {
            setProperty("http://apache.org/xml/properties/dom/document-class-name", "org.apache.html.dom.HTMLDocumentImpl");
        } catch (org.xml.sax.SAXNotRecognizedException e) {
            throw new RuntimeException("http://apache.org/xml/properties/dom/document-class-name property not recognized");
        } catch (org.xml.sax.SAXNotSupportedException e) {
            throw new RuntimeException("http://apache.org/xml/properties/dom/document-class-name property not supported");
        }
    }

    /** Doctype declaration. */
    public void doctypeDecl(String root, String pubid, String sysid, Augmentations augs) throws XNIException {
        String VERSION = XercesBridge.getInstance().getVersion();
        boolean okay = true;
        if (VERSION.startsWith("Xerces-J 2.")) {
            okay = getParserSubVersion() > 5;
        } else if (VERSION.startsWith("XML4J")) {
            okay = false;
        }
        if (okay) {
            super.doctypeDecl(root, pubid, sysid, augs);
        }
    }

    /** Returns the parser's sub-version number. */
    private static int getParserSubVersion() {
        try {
            String VERSION = XercesBridge.getInstance().getVersion();
            int index1 = VERSION.indexOf('.') + 1;
            int index2 = VERSION.indexOf('.', index1);
            if (index2 == -1) {
                index2 = VERSION.length();
            }
            return Integer.parseInt(VERSION.substring(index1, index2));
        } catch (Exception e) {
            return -1;
        }
    }
}

package org.apache.wml.dom;

import org.apache.wml.WMLDOMImplementation;
import org.apache.xerces.dom.DOMImplementationImpl;
import org.apache.xerces.dom.DOMMessageFormatter;
import org.apache.xerces.dom.DocumentImpl;
import org.w3c.dom.DOMException;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;

/**
 * @xerces.internal
 * @version $Id: WMLDOMImplementationImpl.java 516291 2007-03-09 04:26:22Z mrglavas $
 * @author <a href="mailto:david@topware.com.tw">David Li</a>
 */
public class WMLDOMImplementationImpl extends DOMImplementationImpl implements WMLDOMImplementation {

    static final DOMImplementationImpl singleton = new WMLDOMImplementationImpl();

    /** NON-DOM: Obtain and return the single shared object */
    public static DOMImplementation getDOMImplementation() {
        return singleton;
    }

    /**
     * @see org.w3c.dom.DOMImplementation
     */
    public Document createDocument(String namespaceURI, String qualifiedName, DocumentType doctype) throws DOMException {
        if (doctype != null && doctype.getOwnerDocument() != null) {
            throw new DOMException(DOMException.WRONG_DOCUMENT_ERR, DOMMessageFormatter.formatMessage(DOMMessageFormatter.XML_DOMAIN, "WRONG_DOCUMENT_ERR", null));
        }
        DocumentImpl doc = new WMLDocumentImpl(doctype);
        if (qualifiedName != null || namespaceURI != null) {
            Element e = doc.createElementNS(namespaceURI, qualifiedName);
            doc.appendChild(e);
        }
        return doc;
    }
}

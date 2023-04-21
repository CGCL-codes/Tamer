package gnu.xml.dom;

import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.DOMException;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Element;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSInput;
import org.w3c.dom.ls.LSOutput;
import org.w3c.dom.ls.LSParser;
import org.w3c.dom.ls.LSSerializer;
import gnu.xml.dom.html2.DomHTMLImpl;
import gnu.xml.dom.ls.DomLSInput;
import gnu.xml.dom.ls.DomLSOutput;
import gnu.xml.dom.ls.DomLSParser;
import gnu.xml.dom.ls.DomLSSerializer;

/**
 * <p> "DOMImplementation" implementation. </p>
 *
 * <p> At this writing, the following features are supported:
 * "XML" (L1, L2, L3),
 * "Events" (L2), "MutationEvents" (L2), "USER-Events" (a conformant extension),
 * "HTMLEvents" (L2), "UIEvents" (L2), "Traversal" (L2), "XPath" (L3),
 * "LS" (L3) "LS-Async" (L3).
 * It is possible to compile the package so it doesn't support some of these
 * features (notably, Traversal).
 *
 * @author David Brownell 
 * @author <a href='mailto:dog@gnu.org'>Chris Burdess</a>
 */
public class DomImpl implements DOMImplementation, DOMImplementationLS {

    /**
   * Constructs a DOMImplementation object which supports
   * "XML" and other DOM Level 2 features.
   */
    public DomImpl() {
    }

    /**
   * <b>DOM L1</b>
   * Returns true if the specified feature and version are
   * supported.  Note that the case of the feature name is ignored.
   */
    public boolean hasFeature(String name, String version) {
        if (name.length() == 0) {
            return false;
        }
        name = name.toLowerCase();
        if (name.charAt(0) == '+') {
            name = name.substring(1);
        }
        if ("xml".equals(name) || "core".equals(name)) {
            return (version == null || "".equals(version) || "1.0".equals(version) || "2.0".equals(version) || "3.0".equals(version));
        } else if ("ls".equals(name) || "ls-async".equals(name)) {
            return (version == null || "".equals(version) || "3.0".equals(version));
        } else if ("events".equals(name) || "mutationevents".equals(name) || "uievents".equals(name) || "htmlevents".equals(name)) {
            return (version == null || "".equals(version) || "2.0".equals(version));
        } else if ("user-events".equals(name)) {
            return (version == null || "".equals(version) || "0.1".equals(version));
        } else if (DomNode.reportMutations && "traversal".equals(name)) {
            return (version == null || "".equals(version) || "2.0".equals(version));
        } else if ("xpath".equals(name)) {
            return (version == null || "".equals(version) || "3.0".equals(version));
        } else if ("html".equals(name) || "xhtml".equals(name)) {
            return (version == null || "".equals(version) || "2.0".equals(version));
        }
        return false;
    }

    /**
   * <b>DOM L2</b>
   * Creates and returns a DocumentType, associated with this
   * implementation.  This DocumentType can have no associated
   * objects(notations, entities) until the DocumentType is
   * first associated with a document.
   *
   * <p> Note that there is no implication that this DTD will
   * be parsed by the DOM, or ever have contents.  Moreover, the
   * DocumentType created here can only be added to a document by
   * the createDocument method(below).  <em>That means that the only
   * portable way to create a Document object is to start parsing,
   * queue comment and processing instruction (PI) nodes, and then only
   * create a DOM Document after <b>(a)</b> it's known if a DocumentType
   * object is needed, and <b>(b) the name and namespace of the root
   * element is known.  Queued comment and PI nodes would then be
   * inserted appropriately in the document prologue, both before and
   * after the DTD node, and additional attributes assigned to the
   * root element.</em>
   *(One hopes that the final DOM REC fixes this serious botch.)
   */
    public DocumentType createDocumentType(String rootName, String publicId, String systemId) {
        DomDocument.checkNCName(rootName, false);
        return new DomDoctype(this, rootName, publicId, systemId, null);
    }

    /**
   * <b>DOM L2</b>
   * Creates and returns a Document, populated only with a root element and
   * optionally a document type(if that was provided).
   */
    public Document createDocument(String namespaceURI, String rootName, DocumentType doctype) {
        Document doc = createDocument();
        Element root = null;
        if (rootName != null) {
            root = doc.createElementNS(namespaceURI, rootName);
            if (rootName.startsWith("xmlns:")) {
                throw new DomDOMException(DOMException.NAMESPACE_ERR, "xmlns is reserved", null, 0);
            }
        }
        if (doctype != null) {
            doc.appendChild(doctype);
        }
        if (root != null) {
            doc.appendChild(root);
        }
        return doc;
    }

    protected Document createDocument() {
        return new DomDocument(this);
    }

    public Object getFeature(String feature, String version) {
        if (hasFeature(feature, version)) {
            if ("html".equalsIgnoreCase(feature) || "xhtml".equalsIgnoreCase(feature)) {
                return new DomHTMLImpl();
            }
            return this;
        }
        return null;
    }

    public LSParser createLSParser(short mode, String schemaType) throws DOMException {
        return new DomLSParser(mode, schemaType);
    }

    public LSSerializer createLSSerializer() {
        return new DomLSSerializer();
    }

    public LSInput createLSInput() {
        return new DomLSInput();
    }

    public LSOutput createLSOutput() {
        return new DomLSOutput();
    }
}

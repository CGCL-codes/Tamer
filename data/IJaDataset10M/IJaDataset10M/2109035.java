package orgx.jdom.output;

import java.util.*;
import orgx.jdom.*;
import orgx.jdom.adapters.*;

/**
 * Outputs a JDOM {@link orgx.jdom.Document orgx.jdom.Document} as a DOM {@link
 * org.w3c.dom.Document org.w3c.dom.Document}.
 *
 * @version $Revision: 1.43 $, $Date: 2007/11/10 05:29:01 $
 * @author  Brett McLaughlin
 * @author  Jason Hunter
 * @author  Matthew Merlo
 * @author  Dan Schaffer
 * @author  Yusuf Goolamabbas
 * @author  Bradley S. Huffman
 */
public class DOMOutputter {

    private static final String CVS_ID = "@(#) $RCSfile: DOMOutputter.java,v $ $Revision: 1.43 $ $Date: 2007/11/10 05:29:01 $ $Name: jdom_1_1 $";

    /** Default adapter class */
    private static final String DEFAULT_ADAPTER_CLASS = "orgx.jdom.adapters.XercesDOMAdapter";

    /** Adapter to use for interfacing with the DOM implementation */
    private String adapterClass;

    /** Output a DOM with namespaces but just the empty namespace */
    private boolean forceNamespaceAware;

    /**
     * This creates a new DOMOutputter which will attempt to first locate
     * a DOM implementation to use via JAXP, and if JAXP does not exist or
     * there's a problem, will fall back to the default parser.
     */
    public DOMOutputter() {
    }

    /**
     * This creates a new DOMOutputter using the specified DOMAdapter
     * implementation as a way to choose the underlying parser.
     *
     * @param adapterClass <code>String</code> name of class
     *                     to use for DOM output
     */
    public DOMOutputter(String adapterClass) {
        this.adapterClass = adapterClass;
    }

    /**
     * Controls how NO_NAMESPACE nodes are handeled. If true the outputter
     * always creates a namespace aware DOM.
     * @param flag
     */
    public void setForceNamespaceAware(boolean flag) {
        this.forceNamespaceAware = flag;
    }

    /**
     * Returns whether DOMs will be constructed with namespaces even when
     * the source document has elements all in the empty namespace.
     * @return the forceNamespaceAware flag value
     */
    public boolean getForceNamespaceAware() {
        return forceNamespaceAware;
    }

    /**
     * This converts the JDOM <code>Document</code> parameter to a 
     * DOM Document, returning the DOM version.  The DOM implementation
     * is the one chosen in the constructor.
     *
     * @param document <code>Document</code> to output.
     * @return an <code>org.w3c.dom.Document</code> version
     */
    public org.w3c.dom.Document output(Document document) throws JDOMException {
        NamespaceStack namespaces = new NamespaceStack();
        org.w3c.dom.Document domDoc = null;
        try {
            DocType dt = document.getDocType();
            domDoc = createDOMDocument(dt);
            Iterator itr = document.getContent().iterator();
            while (itr.hasNext()) {
                Object node = itr.next();
                if (node instanceof Element) {
                    Element element = (Element) node;
                    org.w3c.dom.Element domElement = output(element, domDoc, namespaces);
                    org.w3c.dom.Element root = domDoc.getDocumentElement();
                    if (root == null) {
                        domDoc.appendChild(domElement);
                    } else {
                        domDoc.replaceChild(domElement, root);
                    }
                } else if (node instanceof Comment) {
                    Comment comment = (Comment) node;
                    org.w3c.dom.Comment domComment = domDoc.createComment(comment.getText());
                    domDoc.appendChild(domComment);
                } else if (node instanceof ProcessingInstruction) {
                    ProcessingInstruction pi = (ProcessingInstruction) node;
                    org.w3c.dom.ProcessingInstruction domPI = domDoc.createProcessingInstruction(pi.getTarget(), pi.getData());
                    domDoc.appendChild(domPI);
                } else if (node instanceof DocType) {
                } else {
                    throw new JDOMException("Document contained top-level content with type:" + node.getClass().getName());
                }
            }
        } catch (Throwable e) {
            throw new JDOMException("Exception outputting Document", e);
        }
        return domDoc;
    }

    private org.w3c.dom.Document createDOMDocument(DocType dt) throws JDOMException {
        if (adapterClass != null) {
            try {
                DOMAdapter adapter = (DOMAdapter) Class.forName(adapterClass).newInstance();
                return adapter.createDocument(dt);
            } catch (ClassNotFoundException e) {
            } catch (IllegalAccessException e) {
            } catch (InstantiationException e) {
            }
        } else {
            try {
                DOMAdapter adapter = (DOMAdapter) Class.forName("orgx.jdom.adapters.JAXPDOMAdapter").newInstance();
                return adapter.createDocument(dt);
            } catch (ClassNotFoundException e) {
            } catch (IllegalAccessException e) {
            } catch (InstantiationException e) {
            }
        }
        try {
            DOMAdapter adapter = (DOMAdapter) Class.forName(DEFAULT_ADAPTER_CLASS).newInstance();
            return adapter.createDocument(dt);
        } catch (ClassNotFoundException e) {
        } catch (IllegalAccessException e) {
        } catch (InstantiationException e) {
        }
        throw new JDOMException("No JAXP or default parser available");
    }

    private org.w3c.dom.Element output(Element element, org.w3c.dom.Document domDoc, NamespaceStack namespaces) throws JDOMException {
        try {
            int previouslyDeclaredNamespaces = namespaces.size();
            org.w3c.dom.Element domElement = null;
            if (element.getNamespace() == Namespace.NO_NAMESPACE) {
                domElement = forceNamespaceAware ? domDoc.createElementNS(null, element.getQualifiedName()) : domDoc.createElement(element.getQualifiedName());
            } else {
                domElement = domDoc.createElementNS(element.getNamespaceURI(), element.getQualifiedName());
            }
            Namespace ns = element.getNamespace();
            if (ns != Namespace.XML_NAMESPACE && !(ns == Namespace.NO_NAMESPACE && namespaces.getURI("") == null)) {
                String prefix = ns.getPrefix();
                String uri = namespaces.getURI(prefix);
                if (!ns.getURI().equals(uri)) {
                    namespaces.push(ns);
                    String attrName = getXmlnsTagFor(ns);
                    domElement.setAttribute(attrName, ns.getURI());
                }
            }
            Iterator itr = element.getAdditionalNamespaces().iterator();
            while (itr.hasNext()) {
                Namespace additional = (Namespace) itr.next();
                String prefix = additional.getPrefix();
                String uri = namespaces.getURI(prefix);
                if (!additional.getURI().equals(uri)) {
                    String attrName = getXmlnsTagFor(additional);
                    domElement.setAttribute(attrName, additional.getURI());
                    namespaces.push(additional);
                }
            }
            itr = element.getAttributes().iterator();
            while (itr.hasNext()) {
                Attribute attribute = (Attribute) itr.next();
                domElement.setAttributeNode(output(attribute, domDoc));
                Namespace ns1 = attribute.getNamespace();
                if ((ns1 != Namespace.NO_NAMESPACE) && (ns1 != Namespace.XML_NAMESPACE)) {
                    String prefix = ns1.getPrefix();
                    String uri = namespaces.getURI(prefix);
                    if (!ns1.getURI().equals(uri)) {
                        String attrName = getXmlnsTagFor(ns1);
                        domElement.setAttribute(attrName, ns1.getURI());
                        namespaces.push(ns1);
                    }
                }
                if (attribute.getNamespace() == Namespace.NO_NAMESPACE) {
                    if (forceNamespaceAware) {
                        domElement.setAttributeNS(null, attribute.getQualifiedName(), attribute.getValue());
                    } else {
                        domElement.setAttribute(attribute.getQualifiedName(), attribute.getValue());
                    }
                } else {
                    domElement.setAttributeNS(attribute.getNamespaceURI(), attribute.getQualifiedName(), attribute.getValue());
                }
            }
            itr = element.getContent().iterator();
            while (itr.hasNext()) {
                Object node = itr.next();
                if (node instanceof Element) {
                    Element e = (Element) node;
                    org.w3c.dom.Element domElt = output(e, domDoc, namespaces);
                    domElement.appendChild(domElt);
                } else if (node instanceof String) {
                    String str = (String) node;
                    org.w3c.dom.Text domText = domDoc.createTextNode(str);
                    domElement.appendChild(domText);
                } else if (node instanceof CDATA) {
                    CDATA cdata = (CDATA) node;
                    org.w3c.dom.CDATASection domCdata = domDoc.createCDATASection(cdata.getText());
                    domElement.appendChild(domCdata);
                } else if (node instanceof Text) {
                    Text text = (Text) node;
                    org.w3c.dom.Text domText = domDoc.createTextNode(text.getText());
                    domElement.appendChild(domText);
                } else if (node instanceof Comment) {
                    Comment comment = (Comment) node;
                    org.w3c.dom.Comment domComment = domDoc.createComment(comment.getText());
                    domElement.appendChild(domComment);
                } else if (node instanceof ProcessingInstruction) {
                    ProcessingInstruction pi = (ProcessingInstruction) node;
                    org.w3c.dom.ProcessingInstruction domPI = domDoc.createProcessingInstruction(pi.getTarget(), pi.getData());
                    domElement.appendChild(domPI);
                } else if (node instanceof EntityRef) {
                    EntityRef entity = (EntityRef) node;
                    org.w3c.dom.EntityReference domEntity = domDoc.createEntityReference(entity.getName());
                    domElement.appendChild(domEntity);
                } else {
                    throw new JDOMException("Element contained content with type:" + node.getClass().getName());
                }
            }
            while (namespaces.size() > previouslyDeclaredNamespaces) {
                namespaces.pop();
            }
            return domElement;
        } catch (Exception e) {
            throw new JDOMException("Exception outputting Element " + element.getQualifiedName(), e);
        }
    }

    private org.w3c.dom.Attr output(Attribute attribute, org.w3c.dom.Document domDoc) throws JDOMException {
        org.w3c.dom.Attr domAttr = null;
        try {
            if (attribute.getNamespace() == Namespace.NO_NAMESPACE) {
                if (forceNamespaceAware) {
                    domAttr = domDoc.createAttributeNS(null, attribute.getQualifiedName());
                } else {
                    domAttr = domDoc.createAttribute(attribute.getQualifiedName());
                }
            } else {
                domAttr = domDoc.createAttributeNS(attribute.getNamespaceURI(), attribute.getQualifiedName());
            }
            domAttr.setValue(attribute.getValue());
        } catch (Exception e) {
            throw new JDOMException("Exception outputting Attribute " + attribute.getQualifiedName(), e);
        }
        return domAttr;
    }

    /**
     * This will handle adding any <code>{@link Namespace}</code>
     * attributes to the DOM tree.
     *
     * @param ns <code>Namespace</code> to add definition of
     */
    private static String getXmlnsTagFor(Namespace ns) {
        String attrName = "xmlns";
        if (!ns.getPrefix().equals("")) {
            attrName += ":";
            attrName += ns.getPrefix();
        }
        return attrName;
    }
}

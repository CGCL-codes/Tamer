package tei.cr.utils.stax.jaxen;

import org.jaxen.DefaultNavigator;
import org.jaxen.FunctionCallException;
import org.jaxen.XPath;
import org.jaxen.UnsupportedAxisException;
import tei.cr.utils.stax.StAXNode;
import java.util.Iterator;
import java.util.logging.Logger;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.Comment;
import javax.xml.stream.events.Namespace;
import javax.xml.stream.events.ProcessingInstruction;
import javax.xml.stream.events.XMLEvent;

/** <p>Navigator over {@link StAXNode StAXNode} for Jaxen.</p>
 *
 * @author Sylvain Loiseau &lt;sloiseau@u-paris10.fr&gt;
 *
 * @version 0.1
 *
 * @see XPath
 *
 * @see tei.cr.utils.stax.StAXTree
 *
 * @see tei.cr.utils.stax.StAXNode
 *
 * @see <a href="http://jaxen.codehaus.org/apidocs/org/jaxen/Navigator.html">jaxen documentation</a>
 */
public class DocumentNavigator extends DefaultNavigator {

    /**
     * Constant: singleton navigator.
     */
    private static final DocumentNavigator SINGLETON = new DocumentNavigator();

    /**
     *  Debugging utility 
     */
    private static final boolean debugging = false;

    /**
     * Logger
     */
    private Logger log = Logger.getLogger(getClass().getName());

    private static final long serialVersionUID = 8460943068889528115L;

    /**
     * Constructor is protected on purpose: use {@link #getInstance()}.
     */
    protected DocumentNavigator() {
    }

    /**
     * Get a singleton DocumentNavigator for efficiency.
     *
     * @return A singleton instance of a DocumentNavigator.
     */
    public static DocumentNavigator getInstance() {
        return SINGLETON;
    }

    /**
     * Get an iterator over all of this node's ancestors.
     * 
     * @throws IllegalArgumentException if the context node is not a StartElement.
     * 
     * @param contextNode The context node for the child axis.
     * 
     * @return A possibly-empty iterator (not null).
     */
    public Iterator getAncestorAxisIterator(Object contextNode) throws UnsupportedAxisException {
        if (debugging) {
            log.info("[DocumentNavigator] ancestor axis");
        }
        return ((StAXNode) contextNode).getAncestorAxis();
    }

    /**
     * Get an iterator over all of this node's ancestors and self.
     * 
     * @throws IllegalArgumentException if the context node is not a StartElement.
     * 
     * @param contextNode The context node for the child axis.
     * 
     * @return A possibly-empty iterator (not null).
     */
    public Iterator getAncestorOrSelfAxisIterator(Object contextNode) {
        if (debugging) {
            log.info("[DocumentNavigator] ancestor or self axis");
        }
        return ((StAXNode) contextNode).getAncestorOrSelfAxis();
    }

    /**
     * Get an iterator over all attributes of the context node.
     *
     * @throws IllegalArgumentException if the context node is not a StartElement.
     *
     * @param contextNode The context node for the attribute axis.
     * 
     * @return A possibly-empty iterator (not null).
     */
    public Iterator getAttributeAxisIterator(Object contextNode) {
        if (debugging) {
            log.info("[DocumentNavigator] attribute axis");
        }
        return ((StAXNode) contextNode).getAttributeAxis();
    }

    /**
     * Get the local name of an attribute.
     *
     * @throws IllegalArgumentException if the context node is not an 
     * {@link javax.xml.stream.events.Attribute}.
     * 
     * @param contextNode the attribute
     * 
     * @return A string representing the unqualified local name.
     */
    public String getAttributeName(Object contextNode) {
        if (debugging) {
            log.info("[DocumentNavigator] attribute name");
        }
        if (!(contextNode instanceof Attribute)) {
            throw new IllegalStateException("Cannot retreive attribute name on non-attribute event.");
        }
        return ((Attribute) contextNode).getName().getLocalPart();
    }

    /**
     * Get the Namespace URI of an attribute.
     *
     * @throws IllegalArgumentException if the context node is not an 
     * {@link javax.xml.stream.events.Attribute}.
     * 
     * @param contextNode the attribute
     * 
     * @return A string representing the Namespace URI.
     */
    public String getAttributeNamespaceUri(Object contextNode) {
        if (debugging) {
            log.info("[DocumentNavigator] attribute NS uri");
        }
        if (!(contextNode instanceof Attribute)) {
            throw new IllegalStateException("Cannot retreive attribute name on non-attribute event.");
        }
        return ((Attribute) contextNode).getName().getNamespaceURI();
    }

    /**
     * Get the qualified name of an attribute.
     *
     * @throws IllegalArgumentException if the context node is not an 
     * {@link javax.xml.stream.events.Attribute}.
     * 
     * @param contextNode the attribute
     * 
     * @return A string representing the qualified (i.e. possibly
     * prefixed)
     */
    public String getAttributeQName(Object contextNode) {
        if (debugging) {
            log.info("[DocumentNavigator] attribute q name");
        }
        log.info("[DocumentNavigator] attribute q name");
        if (!(contextNode instanceof Attribute)) {
            throw new IllegalStateException("Cannot retreive attribute name on non-attribute event.");
        }
        String prefix = ((Attribute) contextNode).getName().getPrefix();
        String local = ((Attribute) contextNode).getName().getLocalPart();
        String qname;
        if (prefix != null && !prefix.equals("")) {
            qname = prefix + ":" + local;
        } else {
            qname = local;
        }
        System.out.println("[DocumentNavigator] attribute q name: " + qname);
        return qname;
    }

    /**
     * Get the string value of an attribute node.
     *
     * @throws IllegalArgumentException if the context node is not an 
     * {@link javax.xml.stream.events.Attribute}.
     * 
     * @param contextNode the attribute
     * 
     * @return The text of the attribute value
     */
    public String getAttributeStringValue(Object contextNode) {
        if (debugging) {
            log.info("[DocumentNavigator] att string value");
        }
        if (!(contextNode instanceof Attribute)) {
            throw new IllegalStateException("Cannot retreive attribute name on non-attribute event.");
        }
        return ((Attribute) contextNode).getValue();
    }

    /**
     * Get an iterator over all of this node's children.
     * 
     * @throws IllegalArgumentException if the context node is not a StartElement.
     *
     * @param contextNode The context node for the child axis.
     * 
     * @return A possibly-empty iterator (not null).
     */
    public Iterator getChildAxisIterator(Object contextNode) {
        if (debugging) {
            log.info("[DocumentNavigator] child axis");
        }
        if (!(contextNode instanceof StAXNode)) {
            throw new IllegalArgumentException("Unexpected type: " + contextNode.getClass().getName());
        }
        return ((StAXNode) contextNode).getChildAxis();
    }

    /**
     * Get the string value of a comment node.
     *
     * @throws IllegalArgumentException if the context node is not a comment.
     * 
     * @param contextNode The context node for the child axis.
     * 
     * @return The text of the comment.
     */
    public String getCommentStringValue(Object contextNode) {
        if (debugging) {
            log.info("[DocumentNavigator] comment string value");
        }
        XMLEvent xe = ((StAXNode) contextNode).getXMLEvent();
        if (xe.getEventType() != XMLStreamConstants.COMMENT) {
            throw new IllegalArgumentException("The context node must be a comment.");
        }
        return ((Comment) xe).getText();
    }

    /**
     * Get an iterator over all of this node's descendant.
     * 
     * @throws IllegalArgumentException if the context node is not a StartElement.
     *
     * @param contextNode The context node for the child axis.
     * 
     * @return A possibly-empty iterator (not null).
     */
    public Iterator getDescendantAxisIterator(Object contextNode) {
        if (debugging) {
            log.info("[DocumentNavigator] descendant axis");
        }
        return ((StAXNode) contextNode).getDescendantAxis();
    }

    /**
     * Get an iterator over all of this node's descendant or self.
     * 
     * @throws IllegalArgumentException if the context node is not a StartElement.
     *
     * @param contextNode The context node for the child axis.
     * 
     * @return A possibly-empty iterator (not null).
     */
    public Iterator getDescendantOrSelfAxisIterator(Object contextNode) {
        if (debugging) {
            log.info("[DocumentNavigator] descendant or self axis");
        }
        return ((StAXNode) contextNode).getDescendantOrSelfAxis();
    }

    /**
     * This method is not implemented.
     * 
     * @throws UnsupportedOperationException in any circonstance.
     */
    public Object getDocument(String uri) throws FunctionCallException {
        throw new UnsupportedOperationException("Method getDocument() not implemented.");
    }

    /**
     * Return the document node (a {@link javax.xml.stream.events.StartDocument} event).
     *
     * @param contextNode Any node in the document.
     * @return The root node 
     */
    public Object getDocumentNode(Object contextNode) {
        if (debugging) {
            log.info("[DocumentNavigator] document node");
        }
        return ((StAXNode) contextNode).getTreeRoot();
    }

    /**
     *  Returns the element whose ID is given by elementId.
     *  If no such element exists, returns null.
     *  
     *  @throws ClassCastException if <code>object</code> is not a <code>StAXNode</code> 
     */
    public Object getElementById(Object object, String elementId) {
        return ((StAXNode) object).getElementById(elementId);
    }

    /**
     * Get the name URI of an element.
     * 
     * @throws IllegalArgumentException if the context node is not a StartElement.
     *
     * @param object The element.
     * 
     * @return the namespace URI of the element.
     */
    public String getElementNamespaceUri(Object object) {
        if (debugging) {
            log.info("[DocumentNavigator] Element NS uri");
        }
        XMLEvent xe = ((StAXNode) object).getXMLEvent();
        if (!xe.isStartElement()) {
            throw new IllegalArgumentException("Cannot retreive namespace URI on non-element event.");
        }
        return xe.asStartElement().getName().getNamespaceURI();
    }

    /**
     * Get the name of an element.
     * 
     * @throws IllegalArgumentException if the context node is not a StartElement.
     *
     * @param object The element.
     * 
     * @return the name of the element.
     */
    public String getElementName(Object object) {
        if (debugging) {
            log.info("[DocumentNavigator] element name");
        }
        XMLEvent xe = ((StAXNode) object).getXMLEvent();
        if (!xe.isStartElement()) {
            throw new IllegalArgumentException("Cannot retreive name on non-element event.");
        }
        return xe.asStartElement().getName().getLocalPart();
    }

    /**
     * Get the qualified name of an element.
     * 
     * @throws IllegalArgumentException if the context node is not a StartElement.
     *
     * @param object The element.
     * 
     * @return A string representing the qualified (i.e. possibly
     * prefixed) name
     */
    public String getElementQName(Object object) {
        if (debugging) {
            log.info("[DocumentNavigator] element qualified name");
        }
        XMLEvent xe = ((StAXNode) object).getXMLEvent();
        if (!xe.isStartElement()) {
            throw new IllegalArgumentException("Cannot retreive Qname on non-element event.");
        }
        String prefix = xe.asStartElement().getName().getPrefix();
        String local = xe.asStartElement().getName().getLocalPart();
        String qname;
        if (prefix != null && !prefix.equals("")) {
            qname = prefix + ":" + local;
        } else {
            qname = local;
        }
        return qname;
    }

    /**
     * Get the string value of an element node.
     * 
     * @throws IllegalArgumentException if the context node is not a StartElement.
     *
     * @param object The element.
     * 
     * @return The text inside the node and its descendants
     */
    public String getElementStringValue(Object object) {
        if (debugging) {
            log.info("[DocumentNavigator] element string value");
        }
        XMLEvent xe = ((StAXNode) object).getXMLEvent();
        if (!xe.isStartElement()) {
            throw new IllegalArgumentException("Cannot retreive Qname on non-element event.");
        }
        return ((StAXNode) object).getElementStringValue();
    }

    /**
     * Get an iterator over all following nodes.
     * 
     * @throws IllegalArgumentException if the context node is not a StartElement.
     *
     * @param contextNode The context node for the axis.
     * 
     * @return A possibly-empty iterator (not null).
     */
    public Iterator getFollowingAxisIterator(Object contextNode) {
        if (debugging) {
            log.info("[DocumentNavigator] following axis");
        }
        return ((StAXNode) contextNode).getFollowingAxis();
    }

    /**
     * Get an iterator over all following sibling nodes.
     * 
     * @throws IllegalArgumentException if the context node is not a StartElement.
     *
     * @param contextNode The context node for the axis.
     * 
     * @return A possibly-empty iterator (not null).
     */
    public Iterator getFollowingSiblingAxisIterator(Object contextNode) {
        if (debugging) {
            log.info("[DocumentNavigator] following sibling axis");
        }
        return ((StAXNode) contextNode).getFollowingSiblingAxis();
    }

    /**
     * Get an iterator over namespace axis.
     * 
     * @throws IllegalArgumentException if the context node is not a StartElement.
     *
     * @param contextNode The context node for the axis.
     * 
     * @return A possibly-empty iterator (not null).
     */
    public Iterator getNamespaceAxisIterator(Object contextNode) {
        if (debugging) {
            log.info("[DocumentNavigator] NS axis");
        }
        return ((StAXNode) contextNode).getNamespaceAxis();
    }

    /**
     * Get the prefix value of a Namespace node.
     * 
     * @throws ClassCastException if the context node is not a {@link Namespace}.
     *
     * @param object The namespace.
     * 
     * @return the prefix.
     */
    public String getNamespacePrefix(Object object) {
        if (debugging) {
            log.info("[DocumentNavigator] NS prefix");
        }
        XMLEvent xe = ((StAXNode) object).getXMLEvent();
        return ((Namespace) xe).getPrefix();
    }

    /**
     * Get the string value of a Namespace node.
     * 
     * @throws ClassCastException if the context node is not a {@link Namespace}.
     *
     * @param object The namespace.
     * 
     * @return the namespace URI; may be an empty <code>String</code>
     * but not <code>null</code>.
     */
    public String getNamespaceStringValue(Object object) {
        if (debugging) {
            log.info("[DocumentNavigator] NS string value");
        }
        XMLEvent xe = ((StAXNode) object).getXMLEvent();
        return ((Namespace) xe).getNamespaceURI();
    }

    public short getNodeType(Object node) {
        throw new UnsupportedOperationException("Cannot convert between node type.");
    }

    /**
     * Get a (single-member) iterator over this node's parent.
     * 
     * @throws IllegalArgumentException if the context node is not a StartElement.
     *
     * @param contextNode the context node for the axis.
     * 
     * @return A possibly-empty iterator (not null).
     */
    public Iterator getParentAxisIterator(Object contextNode) {
        if (debugging) {
            log.info("[DocumentNavigator] parent axis");
        }
        return ((StAXNode) contextNode).getParentAxis();
    }

    /**
     * Get the parent node of a node.
     * 
     * @throws IllegalArgumentException if the context node is not a StartElement.
     *
     * @param contextNode The context node for the axis.
     * 
     * @return A possibly-empty iterator (not null).
     */
    public Object getParentNode(Object contextNode) {
        if (debugging) {
            log.info("[DocumentNavigator] parent node");
        }
        Iterator p = ((StAXNode) contextNode).getParentAxis();
        if (p.hasNext()) {
            return p.next();
        } else {
            throw new IllegalStateException("node without parent.");
        }
    }

    /**
     * Get an iterator over the preceding axis of a node.
     * 
     * @throws IllegalArgumentException if the context node is not a StartElement.
     *
     * @param contextNode the context node for the axis.
     * 
     * @return A possibly-empty iterator (not null).
     */
    public Iterator getPrecedingAxisIterator(Object contextNode) {
        if (debugging) {
            log.info("[DocumentNavigator] preceding axis");
        }
        return ((StAXNode) contextNode).getPrecedingAxis();
    }

    /**
     * Get an iterator over all preceding siblings.
     * 
     * @throws IllegalArgumentException if the context node is not a StartElement.
     *
     * @param contextNode the context node for the axis.
     * 
     * @return A possibly-empty iterator (not null).
     */
    public Iterator getPrecedingSiblingAxisIterator(Object contextNode) {
        if (debugging) {
            log.info("[DocumentNavigator] preceding sibling axis");
        }
        return ((StAXNode) contextNode).getPrecedingSiblingAxis();
    }

    /**
     * Get a processing instruction data.
     * 
     * @throws ClassCastException if the context node is not a {@link javax.xml.stream.events.ProcessingInstruction}.
     *
     * @param object the processing instruction.
     * 
     * @return the processing instruction data.
     */
    public String getProcessingInstructionData(Object object) {
        XMLEvent xe = ((StAXNode) object).getXMLEvent();
        return ((ProcessingInstruction) xe).getData();
    }

    /**
     * Get a processing instruction target.
     * 
     * @throws ClassCastException if the context node is not a {@link javax.xml.stream.events.ProcessingInstruction}.
     *
     * @param object the processing instruction.
     * 
     * @return the processing instruction target.
     */
    public String getProcessingInstructionTarget(Object object) {
        XMLEvent xe = ((StAXNode) object).getXMLEvent();
        return ((ProcessingInstruction) xe).getTarget();
    }

    /**
     * Get an iterator over the self axis
     * 
     * @throws IllegalArgumentException if the context node is not a StartElement.
     *
     * @param contextNode the context node for the axis.
     * 
     * @return A possibly-empty iterator (not null).
     */
    public Iterator getSelfAxisIterator(Object contextNode) {
        if (debugging) {
            log.info("[DocumentNavigator] self axis");
        }
        return ((StAXNode) contextNode).getSelfAxis();
    }

    /**
     * Get the string value of a text node.
     *
     * @throws ClassCastException if the context node is not a {@link javax.xml.stream.events.Characters}.
     *
     * @param object The text node.
     * 
     * @return The string of text.
     */
    public String getTextStringValue(Object object) {
        if (debugging) {
            log.info("[DocumentNavigator] text string value");
        }
        XMLEvent xe = ((StAXNode) object).getXMLEvent();
        return ((Characters) xe).getData();
    }

    /**
     * Test if a node is an attribute.
     *
     * @param object The target node.
     * 
     * @return true if the node is an attribute, false otherwise.
     */
    public boolean isAttribute(Object object) {
        if (debugging) {
            log.info("[DocumentNavigator] if attribute");
        }
        if (object instanceof Attribute) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Test if a node is a comment.
     *
     * @param object The target node.
     * 
     * @return true if the node is a comment, false otherwise.
     */
    public boolean isComment(Object object) {
        if (object instanceof XMLEvent) {
            return false;
        }
        if (!(object instanceof StAXNode)) {
            return false;
        }
        XMLEvent xe = ((StAXNode) object).getXMLEvent();
        return xe.getEventType() == javax.xml.stream.XMLStreamConstants.COMMENT;
    }

    /**
     * Test if a node is a document root.
     *
     * @param object The target node.
     * 
     * @return true if the node is the document root, false otherwise.
     */
    public boolean isDocument(Object object) {
        if (debugging) {
            log.info("[DocumentNavigator] if is document");
        }
        if (!(object instanceof StAXNode)) {
            return false;
        }
        XMLEvent xe = ((StAXNode) object).getXMLEvent();
        return xe.getEventType() == javax.xml.stream.XMLStreamConstants.START_DOCUMENT;
    }

    /**
     * Test if a node is an element.
     *
     * @param object The target node.
     * 
     * @return true if the node is an element, false otherwise.
     */
    public boolean isElement(Object object) {
        if (debugging) {
            log.info("[DocumentNavigator] if is Element");
        }
        if (!(object instanceof StAXNode)) {
            return false;
        }
        XMLEvent xe = ((StAXNode) object).getXMLEvent();
        return xe.isStartElement();
    }

    /**
     * Test if a node is a Namespace.
     *
     * @param object The target node.
     * 
     * @return true if the node is a Namespace, false otherwise.
     */
    public boolean isNamespace(Object object) {
        if (debugging) {
            log.info("[DocumentNavigator] if is namespace");
        }
        if (!(object instanceof StAXNode)) {
            return false;
        }
        XMLEvent xe = ((StAXNode) object).getXMLEvent();
        return (xe.getEventType() == javax.xml.stream.XMLStreamConstants.NAMESPACE);
    }

    /**
     * Test if a node is a processing instruction.
     *
     * @param object The target node.
     * 
     * @return true if the node is a processing instruction, false otherwise.
     */
    public boolean isProcessingInstruction(Object object) {
        if (debugging) {
            log.info("[DocumentNavigator] if is PI");
        }
        if (!(object instanceof StAXNode)) {
            return false;
        }
        XMLEvent xe = ((StAXNode) object).getXMLEvent();
        return (xe.isProcessingInstruction());
    }

    /**
     * Test if a node is a text node.
     *
     * @param object The target node.
     * 
     * @return true if the node is a text node, false otherwise.
     */
    public boolean isText(Object object) {
        if (debugging) {
            log.info("[DocumentNavigator] if is Characters");
        }
        if (!(object instanceof StAXNode)) {
            return false;
        }
        XMLEvent xe = ((StAXNode) object).getXMLEvent();
        return xe.isCharacters();
    }

    /** 
     * Returns a parsed form of the given xpath string, which will be 
     * suitable for queries on StAXTree documents.
     */
    public XPath parseXPath(String xpath) {
        try {
            return new StAXXPath(xpath);
        } catch (org.jaxen.JaxenException jE) {
            IllegalStateException e = new IllegalStateException("Failed to instanciate XPath expression");
            e.initCause(jE);
            throw e;
        }
    }

    /**
     * Translate a Namespace prefix to a URI.
     * 
     * @throws UnsupportedOperationException in any condition.
     */
    public String translateNamespacePrefixToUri(String prefix, Object element) {
        if (debugging) {
            log.info("[DocumentNavigator] translate NS prefix to uri");
        }
        throw new UnsupportedOperationException("Cannot translate prefix to URI");
    }
}

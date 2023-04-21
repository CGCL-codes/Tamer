package net.sourceforge.jsjavacomm.domImpl;

import netscape.javascript.JSException;
import netscape.javascript.JSObject;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.UserDataHandler;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventException;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;
import net.sourceforge.jsjavacomm.applet.JSApplet;
import net.sourceforge.jsjavacomm.dom.JSDOMException;
import net.sourceforge.jsjavacomm.dom.JSNode;

/**
 * TODO Describe this type here.
 *
 * @author <a href="mailto:mail@daniel.may.name">Daniel J. R. May</a>
 * @version 0.1, 12 Dec 2008
 */
public class JSNodeImpl implements JSNode, EventTarget {

    /**
	 * This <code>JSObject</code> represents the JavaScript DOM node which
	 * this class is a wrapper for.
	 */
    protected volatile JSObject node;

    /**
	 * Constructs.
	 *
	 * @param node the <code>JSObject</code> which represents the underlying node to 
	 * be wrapped by this class.
	 */
    protected JSNodeImpl(final JSObject node) {
        this.node = node;
    }

    /**
	 * @see net.sourceforge.jsjavacomm.JSObjectWrapper#getJSObject()
	 */
    @Override
    public JSObject getJSObject() {
        return node;
    }

    /**
	 * @see org.w3c.dom.Node#appendChild(org.w3c.dom.Node)
	 */
    @Override
    public synchronized Node appendChild(Node newChild) throws DOMException {
        try {
            return JSObjectBuilder.buildNode((JSObject) node.call("appendChild", new Object[] { JSUtils.getJSObject(newChild) }));
        } catch (JSException jse) {
            throw new JSDOMException(jse);
        }
    }

    /**
	 * @see org.w3c.dom.Node#cloneNode(boolean)
	 */
    @Override
    public Node cloneNode(boolean deep) {
        return JSObjectBuilder.buildNode((JSObject) node.call("cloneNode", new Object[] { deep }));
    }

    /**
	 * @see org.w3c.dom.Node#compareDocumentPosition(org.w3c.dom.Node)
	 */
    @Override
    public short compareDocumentPosition(Node other) throws DOMException {
        try {
            return JSUtils.getPrimitiveShort(node.call("compareDocumentPosition", new Object[] { JSUtils.getJSObject(other) }));
        } catch (JSException jse) {
            throw new JSDOMException(jse);
        }
    }

    /**
	 * @see org.w3c.dom.Node#getAttributes()
	 */
    @Override
    public NamedNodeMap getAttributes() {
        return new JSNamedNodeMapImpl((JSObject) node.getMember("attributes"));
    }

    /**
	 * @see org.w3c.dom.Node#getBaseURI()
	 */
    @Override
    public String getBaseURI() {
        return (String) node.getMember("baseURI");
    }

    /**
	 * @see org.w3c.dom.Node#getChildNodes()
	 */
    @Override
    public NodeList getChildNodes() {
        return new JSNodeListImpl((JSObject) node.getMember("childNodes"));
    }

    /**
	 * @see org.w3c.dom.Node#getFeature(java.lang.String, java.lang.String)
	 */
    @Override
    public Object getFeature(String feature, String version) {
        return node.call("getFeature", new Object[] { feature, version });
    }

    /**
	 * @see org.w3c.dom.Node#getFirstChild()
	 */
    @Override
    public Node getFirstChild() {
        return JSObjectBuilder.buildNode((JSObject) node.getMember("firstChild"));
    }

    /**
	 * @see org.w3c.dom.Node#getLastChild()
	 */
    @Override
    public Node getLastChild() {
        return JSObjectBuilder.buildNode((JSObject) node.getMember("lastChild"));
    }

    /**
	 * @see org.w3c.dom.Node#getLocalName()
	 */
    @Override
    public String getLocalName() {
        return (String) node.getMember("localName");
    }

    /**
	 * @see org.w3c.dom.Node#getNamespaceURI()
	 */
    @Override
    public String getNamespaceURI() {
        return (String) node.getMember("namespaceURI");
    }

    /**
	 * @see org.w3c.dom.Node#getNextSibling()
	 */
    @Override
    public Node getNextSibling() {
        return JSObjectBuilder.buildNode((JSObject) node.getMember("nextSibling"));
    }

    /**
	 * @see org.w3c.dom.Node#getNodeName()
	 */
    @Override
    public String getNodeName() {
        return (String) node.getMember("nodeName");
    }

    /**
	 * @see org.w3c.dom.Node#getNodeType()
	 */
    @Override
    public short getNodeType() {
        return JSUtils.getPrimitiveShort(node.getMember("nodeType"));
    }

    /**
	 * @see org.w3c.dom.Node#getNodeValue()
	 */
    @Override
    public String getNodeValue() throws DOMException {
        try {
            return (String) node.getMember("nodeValue");
        } catch (JSException jse) {
            throw new JSDOMException(jse);
        }
    }

    /**
	 * @see org.w3c.dom.Node#getOwnerDocument()
	 */
    @Override
    public Document getOwnerDocument() {
        return new JSDocumentImpl((JSObject) node.getMember("ownerDocument"));
    }

    /**
	 * @see org.w3c.dom.Node#getParentNode()
	 */
    @Override
    public Node getParentNode() {
        return JSObjectBuilder.buildNode((JSObject) node.getMember("parentNode"));
    }

    /**
	 * @see org.w3c.dom.Node#getPrefix()
	 */
    @Override
    public String getPrefix() {
        return (String) node.getMember("prefix");
    }

    /**
	 * @see org.w3c.dom.Node#getPreviousSibling()
	 */
    @Override
    public Node getPreviousSibling() {
        return JSObjectBuilder.buildNode((JSObject) node.getMember("previousSibling"));
    }

    /**
	 * @see org.w3c.dom.Node#getTextContent()
	 */
    @Override
    public String getTextContent() throws DOMException {
        return (String) node.getMember("textContent");
    }

    /**
	 * @see org.w3c.dom.Node#getUserData(java.lang.String)
	 */
    @Override
    public Object getUserData(String key) {
        return node.call("getUserData", new Object[] { key });
    }

    /**
	 * @see org.w3c.dom.Node#hasAttributes()
	 */
    @Override
    public boolean hasAttributes() {
        return (Boolean) node.call("hasAttributes", new Object[] {});
    }

    /**
	 * @see org.w3c.dom.Node#hasChildNodes()
	 */
    @Override
    public boolean hasChildNodes() {
        return (Boolean) node.call("hasChildNodes", new Object[] {});
    }

    /**
	 * @see org.w3c.dom.Node#insertBefore(org.w3c.dom.Node, org.w3c.dom.Node)
	 * </ul>
	 */
    @Override
    public synchronized Node insertBefore(Node newChild, Node refChild) throws DOMException {
        try {
            return JSObjectBuilder.buildNode((JSObject) node.call("insertBefore", new Object[] { JSUtils.getJSObject(newChild), JSUtils.getJSObject(refChild) }));
        } catch (JSException jse) {
            throw new JSDOMException(jse);
        }
    }

    /**
	 * @see org.w3c.dom.Node#isDefaultNamespace(java.lang.String)
	 */
    @Override
    public boolean isDefaultNamespace(String namespaceURI) {
        return (Boolean) node.call("isDefaultNamespace", new Object[] { namespaceURI });
    }

    /**
	 * @see org.w3c.dom.Node#isEqualNode(org.w3c.dom.Node)
	 */
    @Override
    public boolean isEqualNode(Node arg) {
        return (Boolean) node.call("isEqualNode", new Object[] { JSUtils.getJSObject(arg) });
    }

    /**
	 * @see org.w3c.dom.Node#isSameNode(org.w3c.dom.Node)
	 */
    @Override
    public boolean isSameNode(Node other) {
        return (Boolean) node.call("isSameNode", new Object[] { JSUtils.getJSObject(other) });
    }

    /**
	 * @see org.w3c.dom.Node#isSupported(java.lang.String, java.lang.String)
	 */
    @Override
    public boolean isSupported(String feature, String version) {
        return (Boolean) node.call("isSupported", new Object[] { feature, version });
    }

    /**
	 * @see org.w3c.dom.Node#lookupNamespaceURI(java.lang.String)
	 */
    @Override
    public String lookupNamespaceURI(String prefix) {
        return (String) node.call("lookupNamespaceURI", new Object[] { prefix });
    }

    /**
	 * @see org.w3c.dom.Node#lookupPrefix(java.lang.String)
	 */
    @Override
    public String lookupPrefix(String namespaceURI) {
        return (String) node.call("lookupPrefix", new Object[] { namespaceURI });
    }

    /**
	 * @see org.w3c.dom.Node#normalize()
	 */
    @Override
    public void normalize() {
        node.call("normalize", new Object[] {});
    }

    /**
	 * @see org.w3c.dom.Node#removeChild(org.w3c.dom.Node)
	 */
    @Override
    public synchronized Node removeChild(Node oldChild) throws DOMException {
        try {
            return JSObjectBuilder.buildNode((JSObject) node.call("removeChild", new Object[] { JSUtils.getJSObject(oldChild) }));
        } catch (JSException jse) {
            throw new JSDOMException(jse);
        }
    }

    /**
	 * @see org.w3c.dom.Node#replaceChild(org.w3c.dom.Node, org.w3c.dom.Node)
	 */
    @Override
    public synchronized Node replaceChild(Node newChild, Node oldChild) throws DOMException {
        try {
            return JSObjectBuilder.buildNode((JSObject) node.call("replaceChild", new Object[] { JSUtils.getJSObject(newChild), JSUtils.getJSObject(oldChild) }));
        } catch (JSException jse) {
            throw new JSDOMException(jse);
        }
    }

    /**
	 * @see org.w3c.dom.Node#setNodeValue(java.lang.String)
	 */
    @Override
    public synchronized void setNodeValue(String nodeValue) throws DOMException {
        try {
            node.setMember("nodeValue", nodeValue);
        } catch (JSException jse) {
            throw new JSDOMException(jse);
        }
    }

    /**
	 * @see org.w3c.dom.Node#setPrefix(java.lang.String)
	 */
    @Override
    public synchronized void setPrefix(String prefix) throws DOMException {
        try {
            node.setMember("prefix", prefix);
        } catch (JSException jse) {
            throw new JSDOMException(jse);
        }
    }

    /**
	 * @see org.w3c.dom.Node#setTextContent(java.lang.String)
	 */
    @Override
    public synchronized void setTextContent(String textContent) throws DOMException {
        try {
            node.setMember("textContent", textContent);
        } catch (JSException jse) {
            throw new JSDOMException(jse);
        }
    }

    /**
	 * @see org.w3c.dom.Node#setUserData(java.lang.String, java.lang.Object, org.w3c.dom.UserDataHandler)
	 */
    @Override
    public synchronized Object setUserData(String key, Object data, UserDataHandler handler) {
        try {
            return node.call("setUserData", new Object[] { key, JSUtils.getJSObject(data), JSUtils.getJSObject(handler) });
        } catch (JSException jse) {
            throw new JSDOMException(jse);
        }
    }

    /**
	 * @see org.w3c.dom.events.EventTarget#addEventListener(java.lang.String, org.w3c.dom.events.EventListener, boolean)
	 */
    @Override
    public void addEventListener(String type, EventListener listener, boolean useCapture) {
        int listenerId = JSApplet.addListener(listener);
        String javascript = "function getEl() { " + "el = function(e) { " + "document.getElementById('appletid').dispatchEvent(" + listenerId + ", e);" + " };" + "return el; };" + "getEl();";
        JSObject jsFunction = (JSObject) node.eval(javascript);
        node.call("addEventListener", new Object[] { type, jsFunction, useCapture });
    }

    @Override
    public void addEventListenerNS(String namespaceURI, String type, EventListener listener, boolean useCapture, Object evtGroup) {
    }

    @Override
    public boolean dispatchEvent(Event evt) throws EventException {
        return false;
    }

    @Override
    public boolean hasEventListenerNS(String namespaceURI, String type) {
        return (Boolean) node.call("hasEventListenerNS", new Object[] { namespaceURI, type });
    }

    @Override
    public void removeEventListener(String type, EventListener listener, boolean useCapture) {
        node.call("removeEventListener", new Object[] { type, JSUtils.getJSObject(listener), useCapture });
    }

    @Override
    public void removeEventListenerNS(String namespaceURI, String type, EventListener listener, boolean useCapture) {
    }

    @Override
    public boolean willTriggerNS(String namespaceURI, String type) {
        return (Boolean) node.call("willTriggerNS", new Object[] { namespaceURI, type });
    }
}

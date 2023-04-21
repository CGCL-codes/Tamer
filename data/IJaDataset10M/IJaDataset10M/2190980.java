package org.infoset.xml.dom;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import org.infoset.xml.Attribute;
import org.infoset.xml.Child;
import org.infoset.xml.Document;
import org.infoset.xml.Element;
import org.infoset.xml.Item;
import org.infoset.xml.Name;
import org.infoset.xml.Named;
import org.w3c.dom.DOMException;
import org.w3c.dom.Node;
import org.w3c.dom.UserDataHandler;

/**
 *
 * @author alex
 */
public abstract class DOMNodeProxy implements Node {

    Map<String, Object> userData;

    Item item;

    DOMDocumentProxy documentProxy;

    /** Creates a new instance of DOMNodeProxy */
    public DOMNodeProxy(Item item, DOMDocumentProxy documentProxy) {
        this.documentProxy = documentProxy;
        if (documentProxy != null && documentProxy.nodeMap.get(item) == null) {
            documentProxy.nodeMap.put(item, this);
        }
        this.item = item;
        this.userData = null;
    }

    public org.w3c.dom.Document getOwnerDocument() {
        return documentProxy;
    }

    public String getBaseURI() {
        URI baseURI = null;
        switch(item.getType()) {
            case ElementItem:
                baseURI = ((Element) item).getBaseURI();
            case DocumentItem:
                baseURI = ((Document) item).getBaseURI();
            case AttributeItem:
                baseURI = ((Attribute) item).getElement().getBaseURI();
            default:
                baseURI = ((Child) item).getParent().getBaseURI();
        }
        System.err.println("Base URI = " + baseURI);
        return baseURI == null ? null : baseURI.toString();
    }

    public short compareDocumentPosition(Node other) {
        throw new DOMException(DOMException.NOT_SUPPORTED_ERR, "Comparing document position is not supported.");
    }

    public boolean isSameNode(Node node) {
        return node == this;
    }

    public boolean isDefaultNamespace(String uri) {
        String prefix;
        switch(item.getType()) {
            case ElementItem:
                prefix = ((Element) item).getNamespaceScope().getNearestPrefix(URI.create(uri));
                break;
            case DocumentItem:
                return false;
            case AttributeItem:
                prefix = ((Attribute) item).getElement().getNamespaceScope().getNearestPrefix(URI.create(uri));
            default:
                prefix = ((Element) ((Child) item).getParent()).getNamespaceScope().getNearestPrefix(URI.create(uri));
        }
        return prefix == null ? false : prefix.length() == 0 ? true : false;
    }

    public String lookupNamespaceURI(String prefix) {
        URI ns = null;
        switch(item.getType()) {
            case ElementItem:
                ns = ((Element) item).getNamespaceScope().getNamespace(prefix);
            case DocumentItem:
                return null;
            case AttributeItem:
                ns = ((Attribute) item).getElement().getNamespaceScope().getNamespace(prefix);
            default:
                ns = ((Element) ((Child) item).getParent()).getNamespaceScope().getNamespace(prefix);
        }
        if (ns == Name.NO_NAMESPACE) {
            ns = null;
        }
        return ns == null ? null : ns.toString();
    }

    public String lookupPrefix(String uri) {
        String prefix;
        switch(item.getType()) {
            case ElementItem:
                prefix = ((Element) item).getNamespaceScope().getNearestPrefix(URI.create(uri));
                break;
            case DocumentItem:
                return null;
            case AttributeItem:
                prefix = ((Attribute) item).getElement().getNamespaceScope().getNearestPrefix(URI.create(uri));
            default:
                prefix = ((Element) ((Child) item).getParent()).getNamespaceScope().getNearestPrefix(URI.create(uri));
        }
        return prefix == null ? null : prefix.length() == 0 ? null : prefix;
    }

    public Object getUserData(String key) {
        return userData == null ? null : userData.get(key);
    }

    public Object getFeature(String feature, String version) {
        return null;
    }

    public boolean isSupported(String feature, String version) {
        return false;
    }

    public boolean isEqualNode(Node other) {
        if (!(other instanceof DOMNodeProxy)) {
            return false;
        }
        DOMNodeProxy proxy = (DOMNodeProxy) other;
        if (proxy.item.getType() == item.getType()) {
            if (item instanceof Named) {
                if (!((Named) item).getName().equals(((Named) proxy.item).getName())) {
                    return false;
                }
            }
        }
        return false;
    }

    public Object setUserData(String key, Object data, UserDataHandler handler) {
        if (userData == null) {
            userData = new HashMap<String, Object>();
        }
        return userData.put(key, data);
    }

    public String getLocalName() {
        return item instanceof Named ? ((Named) item).getName().getLocalName() : null;
    }

    public String getNamespaceURI() {
        if (item instanceof Named) {
            URI ns = ((Named) item).getName().getNamespaceName();
            return ns == Name.NO_NAMESPACE ? null : ns.toString();
        } else {
            return null;
        }
    }

    public void normalize() {
    }
}

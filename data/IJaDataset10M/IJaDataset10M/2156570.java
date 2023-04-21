package gnu.xml.dom.html2;

import gnu.xml.dom.DomDOMException;
import gnu.xml.dom.DomElement;
import gnu.xml.dom.DomEvent;
import org.w3c.dom.DOMException;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.events.UIEvent;
import org.w3c.dom.html2.HTMLElement;

/**
 * Abstract implementation of an HTML element node.
 *
 * @author <a href='mailto:dog@gnu.org'>Chris Burdess</a>
 */
public abstract class DomHTMLElement extends DomElement implements HTMLElement {

    protected DomHTMLElement(DomHTMLDocument owner, String namespaceURI, String name) {
        super(owner, namespaceURI, name);
    }

    /**
   * Returns the value of the specified attribute.
   * The attribute name is case insensitive.
   */
    protected String getHTMLAttribute(String name) {
        if (hasAttributes()) {
            NamedNodeMap attrs = getAttributes();
            int len = attrs.getLength();
            for (int i = 0; i < len; i++) {
                Node attr = attrs.item(i);
                String attrName = attr.getLocalName();
                if (attrName == null) {
                    attrName = attr.getNodeName();
                }
                if (attrName.equalsIgnoreCase(name)) {
                    return attr.getNodeValue();
                }
            }
        }
        return "";
    }

    protected int getIntHTMLAttribute(String name) {
        String value = getHTMLAttribute(name);
        if (value == null) {
            return -1;
        }
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    protected boolean getBooleanHTMLAttribute(String name) {
        String value = getHTMLAttribute(name);
        return value != null;
    }

    /**
   * Sets the value of the specified attribute.
   * The attribute name is case insensitive.
   */
    protected void setHTMLAttribute(String name, String value) {
        Node attr;
        NamedNodeMap attrs = getAttributes();
        int len = attrs.getLength();
        for (int i = 0; i < len; i++) {
            attr = attrs.item(i);
            String attrName = attr.getLocalName();
            if (attrName == null) {
                attrName = attr.getNodeName();
            }
            if (attrName.equalsIgnoreCase(name)) {
                if (value != null) {
                    attr.setNodeValue(value);
                } else {
                    attrs.removeNamedItem(attr.getNodeName());
                }
                return;
            }
        }
        if (value != null) {
            DomHTMLDocument doc = (DomHTMLDocument) getOwnerDocument();
            attr = doc.createAttribute(name);
            attr.setNodeValue(value);
        }
    }

    protected void setIntHTMLAttribute(String name, int value) {
        setHTMLAttribute(name, Integer.toString(value));
    }

    protected void setBooleanHTMLAttribute(String name, boolean value) {
        setHTMLAttribute(name, value ? name : null);
    }

    /**
   * Returns the first parent element with the specified name.
   */
    protected Node getParentElement(String name) {
        for (Node parent = getParentNode(); parent != null; parent = parent.getParentNode()) {
            String parentName = parent.getLocalName();
            if (parentName == null) {
                parentName = parent.getNodeName();
            }
            if (name.equalsIgnoreCase(parentName)) {
                return parent;
            }
        }
        return null;
    }

    /**
   * Returns the first child element with the specified name.
   */
    protected Node getChildElement(String name) {
        for (Node child = getFirstChild(); child != null; child = child.getNextSibling()) {
            String childName = child.getLocalName();
            if (childName == null) {
                childName = child.getLocalName();
            }
            if (name.equalsIgnoreCase(childName)) {
                return child;
            }
        }
        return null;
    }

    /**
   * Returns the index of this element among elements of the same name,
   * relative to its parent.
   */
    protected int getIndex() {
        int index = 0;
        Node parent = getParentNode();
        if (parent != null) {
            for (Node ctx = parent.getFirstChild(); ctx != null; ctx = ctx.getNextSibling()) {
                if (ctx == this) {
                    return index;
                }
                index++;
            }
        }
        throw new DomDOMException(DOMException.NOT_FOUND_ERR);
    }

    protected void dispatchUIEvent(String name) {
        UIEvent event = new DomEvent.DomUIEvent(name);
        dispatchEvent(event);
    }

    public String getId() {
        return getHTMLAttribute("id");
    }

    public void setId(String id) {
        setHTMLAttribute("id", id);
    }

    public String getTitle() {
        return getHTMLAttribute("title");
    }

    public void setTitle(String title) {
        setHTMLAttribute("title", title);
    }

    public String getLang() {
        return getHTMLAttribute("lang");
    }

    public void setLang(String lang) {
        setHTMLAttribute("lang", lang);
    }

    public String getDir() {
        return getHTMLAttribute("dir");
    }

    public void setDir(String dir) {
        setHTMLAttribute("dir", dir);
    }

    public String getClassName() {
        return getHTMLAttribute("class");
    }

    public void setClassName(String className) {
        setHTMLAttribute("class", className);
    }
}

package org.apache.html.dom;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.w3c.dom.html.HTMLElement;
import org.w3c.dom.html.HTMLOptionElement;
import org.w3c.dom.html.HTMLSelectElement;

/**
 * @xerces.internal
 * @version $Revision: 447255 $ $Date: 2006-09-18 01:36:42 -0400 (Mon, 18 Sep 2006) $
 * @author <a href="mailto:arkin@openxml.org">Assaf Arkin</a>
 * @see org.w3c.dom.html.HTMLOptionElement
 * @see org.apache.xerces.dom.ElementImpl
 */
public class HTMLOptionElementImpl extends HTMLElementImpl implements HTMLOptionElement {

    private static final long serialVersionUID = -4486774554137530907L;

    public boolean getDefaultSelected() {
        return getBinary("default-selected");
    }

    public void setDefaultSelected(boolean defaultSelected) {
        setAttribute("default-selected", defaultSelected);
    }

    public String getText() {
        Node child;
        StringBuffer text = new StringBuffer();
        child = getFirstChild();
        while (child != null) {
            if (child instanceof Text) {
                text.append(((Text) child).getData());
            }
            child = child.getNextSibling();
        }
        return text.toString();
    }

    public void setText(String text) {
        Node child;
        Node next;
        child = getFirstChild();
        while (child != null) {
            next = child.getNextSibling();
            removeChild(child);
            child = next;
        }
        insertBefore(getOwnerDocument().createTextNode(text), getFirstChild());
    }

    public int getIndex() {
        Node parent;
        NodeList options;
        int i;
        parent = getParentNode();
        while (parent != null && !(parent instanceof HTMLSelectElement)) parent = parent.getParentNode();
        if (parent != null) {
            options = ((HTMLElement) parent).getElementsByTagName("OPTION");
            for (i = 0; i < options.getLength(); ++i) if (options.item(i) == this) return i;
        }
        return -1;
    }

    public void setIndex(int index) {
        Node parent;
        NodeList options;
        Node item;
        parent = getParentNode();
        while (parent != null && !(parent instanceof HTMLSelectElement)) parent = parent.getParentNode();
        if (parent != null) {
            options = ((HTMLElement) parent).getElementsByTagName("OPTION");
            if (options.item(index) != this) {
                getParentNode().removeChild(this);
                item = options.item(index);
                item.getParentNode().insertBefore(this, item);
            }
        }
    }

    public boolean getDisabled() {
        return getBinary("disabled");
    }

    public void setDisabled(boolean disabled) {
        setAttribute("disabled", disabled);
    }

    public String getLabel() {
        return capitalize(getAttribute("label"));
    }

    public void setLabel(String label) {
        setAttribute("label", label);
    }

    public boolean getSelected() {
        return getBinary("selected");
    }

    public void setSelected(boolean selected) {
        setAttribute("selected", selected);
    }

    public String getValue() {
        return getAttribute("value");
    }

    public void setValue(String value) {
        setAttribute("value", value);
    }

    /**
     * Constructor requires owner document.
     * 
     * @param owner The owner HTML document
     */
    public HTMLOptionElementImpl(HTMLDocumentImpl owner, String name) {
        super(owner, name);
    }
}

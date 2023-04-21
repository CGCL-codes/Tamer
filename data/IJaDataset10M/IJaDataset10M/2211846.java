package org.akrogen.tkui.dom.xhtml.internal.dom;

import org.akrogen.tkui.core.ui.bindings.IUIElementBindable;
import org.apache.xerces.dom.CoreDocumentImpl;
import org.w3c.dom.DOMException;
import org.w3c.dom.html.HTMLOptionElement;

public class HTMLOptionElementImpl extends HTMLElementImpl implements HTMLOptionElement {

    public static final String SELECTED_ATTR = "selected";

    public HTMLOptionElementImpl(CoreDocumentImpl ownerDocument, String namespaceURI, String qualifiedName, String localName) throws DOMException {
        super(ownerDocument, namespaceURI, qualifiedName, localName);
    }

    public String getUIElementId() {
        return null;
    }

    public void bindDOMElementWithUIElement(IUIElementBindable uiElement) {
    }

    public boolean getDefaultSelected() {
        return false;
    }

    public boolean getDisabled() {
        return getBinary("disabled");
    }

    public int getIndex() {
        return 0;
    }

    public String getLabel() {
        return null;
    }

    public boolean getSelected() {
        return getBinary(SELECTED_ATTR);
    }

    public String getText() {
        return null;
    }

    public String getValue() {
        return null;
    }

    public void setDefaultSelected(boolean flag) {
    }

    public void setDisabled(boolean flag) {
        setAttribute("disabled", flag);
    }

    public void setLabel(String s) {
    }

    public void setSelected(boolean flag) {
        setAttribute(SELECTED_ATTR, flag);
    }

    public void setValue(String s) {
    }
}

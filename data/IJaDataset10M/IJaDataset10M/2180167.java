package org.akrogen.tkui.dom.xul.internal.dom.trees;

import org.akrogen.tkui.dom.xul.dom.trees.Treecell;
import org.akrogen.tkui.dom.xul.internal.dom.XULElementImpl;
import org.apache.xerces.dom.CoreDocumentImpl;
import org.w3c.dom.DOMException;

public class TreecellImpl extends XULElementImpl implements Treecell {

    public TreecellImpl(CoreDocumentImpl ownerDocument, String namespaceURI, String qualifiedName, String localName) throws DOMException {
        super(ownerDocument, namespaceURI, qualifiedName, localName);
    }

    public String getUIElementId() {
        return null;
    }

    public String getLabel() {
        return super.getAttribute(LABEL_ATTR);
    }

    public void setLabel(String label) {
        super.setAttribute(LABEL_ATTR, label);
    }

    public String getSrc() {
        return super.getAttribute(SRC_ATTR);
    }

    public void setSrc(String src) {
        super.setAttribute(SRC_ATTR, src);
    }

    public boolean isDisabled() {
        return false;
    }

    public boolean isEditable() {
        return false;
    }

    public void setDisabled(boolean disabled) {
    }

    public void setEditable(boolean editable) {
    }
}

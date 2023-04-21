package org.akrogen.tkui.dom.xul.internal.dom.trees;

import org.akrogen.tkui.dom.xul.dom.trees.Treeitem;
import org.akrogen.tkui.dom.xul.internal.dom.XULElementImpl;
import org.apache.xerces.dom.CoreDocumentImpl;
import org.w3c.dom.DOMException;

public class TreeitemImpl extends XULElementImpl implements Treeitem {

    public TreeitemImpl(CoreDocumentImpl ownerDocument, String namespaceURI, String qualifiedName, String localName) throws DOMException {
        super(ownerDocument, namespaceURI, qualifiedName, localName);
    }

    public String getUIElementId() {
        return null;
    }

    public boolean isChecked() {
        return false;
    }

    public boolean isOpen() {
        return false;
    }

    public boolean isSelected() {
        return false;
    }

    public void setChecked(boolean checked) {
    }

    public void setOpen(boolean open) {
    }

    public void setSelected(boolean selected) {
    }
}

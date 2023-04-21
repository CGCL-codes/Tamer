package org.akrogen.tkui.dom.xaml.internal.system.windows.controls;

import org.akrogen.tkui.core.ui.UIConstants;
import org.akrogen.tkui.core.ui.bindings.IUIElementBindable;
import org.akrogen.tkui.dom.xaml.system.windows.controls.Label;
import org.apache.xerces.dom.CoreDocumentImpl;
import org.w3c.dom.DOMException;

public class LabelImpl extends ContentControlImpl implements Label {

    public LabelImpl(CoreDocumentImpl ownerDocument, String namespaceURI, String qualifiedName, String localName) throws DOMException {
        super(ownerDocument, namespaceURI, qualifiedName, localName);
    }

    public String getUIElementId() {
        return UIConstants.UI_LABEL_ID;
    }

    public void bindDOMElementWithUIElement(IUIElementBindable uiElement) {
        super.bindDOMElementWithUIElement(uiElement);
    }
}

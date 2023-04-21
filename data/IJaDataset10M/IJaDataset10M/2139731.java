package org.akrogen.tkui.dom.xaml.internal.system.windows.controls.primitives;

import org.akrogen.tkui.core.ui.UIConstants;
import org.akrogen.tkui.core.ui.bindings.IUIElementBindable;
import org.akrogen.tkui.dom.xaml.internal.system.windows.controls.ContentControlImpl;
import org.apache.xerces.dom.CoreDocumentImpl;
import org.w3c.dom.DOMException;

public abstract class ButtonBaseImpl extends ContentControlImpl {

    public ButtonBaseImpl(CoreDocumentImpl ownerDocument, String namespaceURI, String qualifiedName, String localName) throws DOMException {
        super(ownerDocument, namespaceURI, qualifiedName, localName);
    }

    public void bindDOMElementWithUIElement(IUIElementBindable uiElement) {
        super.bindDOMElementWithUIElement(uiElement);
    }

    public String getUIElementId() {
        return UIConstants.UI_BUTTON_ID;
    }
}

package org.akrogen.tkui.dom.xul.internal.dom.containers.boxes;

import org.akrogen.tkui.dom.xul.dom.containers.boxes.VBox;
import org.akrogen.tkui.dom.xul.layouts.boxes.IXULBoxLayout;
import org.apache.xerces.dom.CoreDocumentImpl;
import org.w3c.dom.DOMException;

/**
 * XUL {@link VBox} implementation.
 * 
 * @version 1.0.0
 * @author <a href="mailto:angelo.zerr@gmail.com">Angelo ZERR</a>
 * 
 * @see http://developer.mozilla.org/en/docs/XUL:vbox
 */
public class VBoxImpl extends BoxImpl implements VBox {

    public VBoxImpl(CoreDocumentImpl ownerDocument, String namespaceURI, String qualifiedName, String localName) throws DOMException {
        super(ownerDocument, namespaceURI, qualifiedName, localName);
    }

    protected void internalOnDOMNodeInsertedIntoDocument() {
        super.internalOnDOMNodeInsertedIntoDocument();
        IXULBoxLayout boxLayout = (IXULBoxLayout) getLayout();
        boxLayout.setOrient(IXULBoxLayout.ORIENT_VERTICAL);
    }
}

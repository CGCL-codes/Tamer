package org.akrogen.tkui.dom.xforms.internal.dom.controls;

import java.util.Iterator;
import javax.xml.namespace.NamespaceContext;
import org.akrogen.core.xml.utils.DOMUtils;
import org.akrogen.tkui.core.dom.IDOMElementNS;
import org.akrogen.tkui.core.ui.bindings.ITextBindable;
import org.akrogen.tkui.core.ui.bindings.IUIElementBindable;
import org.akrogen.tkui.dom.xforms.IXFormsDocumentContainer;
import org.akrogen.tkui.dom.xforms.XFormsConstants;
import org.akrogen.tkui.dom.xforms.dom.model.IXFormsInstanceElement;
import org.akrogen.tkui.dom.xforms.dom.model.IXFormsModelElement;
import org.akrogen.tkui.dom.xforms.internal.dom.XFormsElementImpl;
import org.apache.xerces.dom.CoreDocumentImpl;
import org.ufacekit.core.databinding.dom.events.DOMEventsObservables;
import org.ufacekit.core.databinding.dom.xpath.DOMXPathObservables;
import org.ufacekit.ui.databinding.events.observable.IObservableEvent;
import org.w3c.dom.DOMException;
import org.w3c.dom.Node;

public abstract class XFormsControlElementImpl extends XFormsElementImpl {

    public XFormsControlElementImpl(CoreDocumentImpl ownerDocument, String namespaceURI, String qualifiedName, String localName) throws DOMException {
        super(ownerDocument, namespaceURI, qualifiedName, localName);
    }

    protected void applyBindings() {
        applyBindings((IUIElementBindable) getUIElement());
    }

    protected void applyBindings(IUIElementBindable uiElement) {
        String ref = this.getAttribute(XFormsConstants.REF_ATTRIBUTE);
        if (ref.length() < 1) return;
        IXFormsDocumentContainer formDocumentContainer = super.getXFormsDocumentContainer();
        IXFormsModelElement model = formDocumentContainer.getDefaultModel();
        if (model != null) {
            IXFormsInstanceElement instance = model.getDefaultInstance();
            if (instance != null) {
                try {
                    if (uiElement instanceof ITextBindable) {
                        String p = this.lookupNamespaceURI(XFormsConstants.XFORMS_NAMESPACE_URI);
                        System.out.println(p);
                        NamespaceContext namespaceContext = formDocumentContainer.getNamespaceContext();
                        ((ITextBindable) uiElement).bindText(DOMXPathObservables.observeCharacterData(getDataBindingContext().getValidationRealm(), instance.getRootNodeObservable(), ref, null, namespaceContext));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public IDOMElementNS[] getDOMElementsWhereUIElementMustBeBuiltBefore() {
        Node label = DOMUtils.getFirstMatchingChild(this, XFormsConstants.LABEL_ELEMENT);
        if (label != null) {
            IDOMElementNS[] list = new IDOMElementNS[1];
            list[0] = (IDOMElementNS) label;
            return list;
        }
        return null;
    }

    protected IObservableEvent createObserveEvent(String typeArg) {
        return DOMEventsObservables.observeEvent(getXFormsProcessor().getDocumentEvent(), this, typeArg);
    }
}

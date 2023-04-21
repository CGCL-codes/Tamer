package org.eclipse.bpel.model.impl;

import org.eclipse.wst.wsdl.ExtensibilityElement;
import org.eclipse.wst.wsdl.WSDLElement;
import org.eclipse.wst.wsdl.internal.impl.WSDLElementImpl;
import org.w3c.dom.Element;

@SuppressWarnings("restriction")
public class ExtensibilityElementImpl extends org.eclipse.wst.wsdl.internal.impl.ExtensibilityElementImpl implements ExtensibilityElement {

    @Override
    protected void reconcile(Element changedElement) {
        reconcileAttributes(changedElement);
        reconcileContents(changedElement);
    }

    @Override
    public void elementChanged(Element changedElement) {
        if (!isUpdatingDOM()) {
            if (!isReconciling) {
                isReconciling = true;
                try {
                    reconcile(changedElement);
                    WSDLElement theContainer = getContainer();
                    if (theContainer != null && theContainer.getElement() == changedElement) {
                        ((WSDLElementImpl) theContainer).elementChanged(changedElement);
                    }
                } finally {
                    isReconciling = false;
                }
                traverseToRootForPatching();
            }
        }
    }

    @Override
    public boolean isUpdatingDOM() {
        return super.isUpdatingDOM();
    }

    public void setUpdatingDOM(boolean updatingDOM) {
        this.updatingDOM = updatingDOM;
    }
}

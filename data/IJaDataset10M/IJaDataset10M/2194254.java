package org.eclipse.core.internal.databinding.dom.update;

import org.w3c.dom.Attr;

public class DOMUpdaterRemoveAttrActionImpl implements IDOMUpdaterAction {

    private Attr attr;

    public DOMUpdaterRemoveAttrActionImpl(Attr attr) {
        this.attr = attr;
    }

    public void execute() {
        attr.getOwnerElement().removeAttributeNode(attr);
    }
}

package org.mariella.rcp.resources;

import org.eclipse.swt.widgets.Composite;

public class VResourceEditorSupport extends AbstractVResourceEditorSupport {

    public VResourceEditorSupport(VResourceEditorPart editorPart) {
        super(editorPart);
    }

    void implementCreatePartControl(Composite parent) {
        ((VResourceSingleEditorCustomizationCallback) customizationCallback).implementCreatePartControl(parent);
        refresh(false);
    }
}

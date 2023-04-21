package com.netprogress.rcp.ui.framework.editors;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.*;

public class FormEditorInput implements IEditorInput {

    private String name;

    public FormEditorInput(String name) {
        this.name = name;
    }

    public boolean exists() {
        return true;
    }

    public ImageDescriptor getImageDescriptor() {
        return PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_OBJ_ELEMENT);
    }

    public String getName() {
        return name;
    }

    public IPersistableElement getPersistable() {
        return null;
    }

    public String getToolTipText() {
        return getName();
    }

    public Object getAdapter(Class adapter) {
        return null;
    }
}

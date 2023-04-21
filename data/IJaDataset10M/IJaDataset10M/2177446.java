package org.skyfree.ghyll.tcard.repository;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IPersistableElement;
import org.skyfree.ghyll.tcard.core.IStoragePath;
import org.skyfree.ghyll.tcard.core.ITWorkpiece;

public class WorkpieceInput implements IEditorInput {

    ITWorkpiece workpiece;

    public WorkpieceInput(ITWorkpiece workpiece) {
        this.workpiece = workpiece;
    }

    @Override
    public boolean exists() {
        return true;
    }

    @Override
    public ImageDescriptor getImageDescriptor() {
        return null;
    }

    @Override
    public String getName() {
        return this.workpiece.getName();
    }

    @Override
    public IPersistableElement getPersistable() {
        return null;
    }

    @Override
    public String getToolTipText() {
        return this.workpiece.getDescription();
    }

    @SuppressWarnings("unchecked")
    @Override
    public Object getAdapter(Class adapter) {
        if (adapter == ITWorkpiece.class) {
            return this.workpiece;
        } else if (adapter == IStoragePath.class) {
            return this.workpiece.getPath();
        }
        return null;
    }
}

package org.informaticisenzafrontiere.openstaff;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.util.Assert;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IPersistableElement;
import org.eclipse.ui.plugin.AbstractUIPlugin;

public class ModificaEditorInput implements IEditorInput {

    private String operazione;

    private int id;

    public ModificaEditorInput(String operazione, int id) {
        super();
        Assert.isNotNull(operazione);
        this.operazione = operazione;
        this.id = id;
    }

    @Override
    public boolean exists() {
        return false;
    }

    @Override
    public ImageDescriptor getImageDescriptor() {
        return AbstractUIPlugin.imageDescriptorFromPlugin("org.informaticisenzafrontiere.openstaff", IImageKeys.MODIFICA);
    }

    @Override
    public String getName() {
        return operazione;
    }

    @Override
    public IPersistableElement getPersistable() {
        return null;
    }

    @Override
    public String getToolTipText() {
        return operazione;
    }

    @Override
    public Object getAdapter(Class adapter) {
        return null;
    }

    public boolean equals(Object obj) {
        if (super.equals(obj)) return true;
        if (!(obj instanceof ModificaEditorInput)) return false;
        ModificaEditorInput other = (ModificaEditorInput) obj;
        return operazione.equals(other.operazione);
    }

    public int hashCode() {
        return operazione.hashCode();
    }

    public int getId() {
        return id;
    }
}

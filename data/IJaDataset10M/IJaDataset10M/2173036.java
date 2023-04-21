package org.informaticisenzafrontiere.openstaff;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.util.Assert;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IPersistableElement;
import org.eclipse.ui.plugin.AbstractUIPlugin;

public class ModificaRuoloEditorInput implements IEditorInput {

    private String operazione;

    public ModificaRuoloEditorInput(String operazione) {
        super();
        Assert.isNotNull(operazione);
        this.operazione = operazione;
    }

    @Override
    public boolean exists() {
        return false;
    }

    @Override
    public ImageDescriptor getImageDescriptor() {
        return AbstractUIPlugin.imageDescriptorFromPlugin("org.informaticisenzafrontiere.openstaff", IImageKeys.MODIFICA_RUOLO);
    }

    @Override
    public String getName() {
        return operazione;
    }

    public String getNomeRuolo() {
        int n = operazione.indexOf("|") - 1;
        String nome = operazione.substring(0, n);
        return nome;
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
        if (!(obj instanceof ModificaRuoloEditorInput)) return false;
        ModificaRuoloEditorInput other = (ModificaRuoloEditorInput) obj;
        return operazione.equals(other.operazione);
    }

    public int hashCode() {
        return operazione.hashCode();
    }
}

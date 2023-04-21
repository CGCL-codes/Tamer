package org.informaticisenzafrontiere.openstaff;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.util.Assert;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IPersistableElement;
import org.eclipse.ui.plugin.AbstractUIPlugin;

public class VisualizzaAgreementEditorInput implements IEditorInput {

    private String operazione;

    private int idEmployee;

    private int roleID;

    public VisualizzaAgreementEditorInput(String operazione, int idEmployee, int roleID) {
        super();
        Assert.isNotNull(operazione);
        this.operazione = operazione;
        this.idEmployee = idEmployee;
        this.roleID = roleID;
    }

    @Override
    public boolean exists() {
        return false;
    }

    @Override
    public ImageDescriptor getImageDescriptor() {
        return AbstractUIPlugin.imageDescriptorFromPlugin("org.informaticisenzafrontiere.openstaff", IImageKeys.VISUALIZZA_CONTRATTO);
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
        if (!(obj instanceof VisualizzaAgreementEditorInput)) return false;
        VisualizzaAgreementEditorInput other = (VisualizzaAgreementEditorInput) obj;
        return operazione.equals(other.operazione);
    }

    public int hashCode() {
        return operazione.hashCode();
    }

    public int getId() {
        return idEmployee;
    }

    public int getRole() {
        return roleID;
    }
}

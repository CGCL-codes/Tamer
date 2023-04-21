package org.informaticisenzafrontiere.openstaff;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IPersistableElement;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.informaticisenzafrontiere.openstaff.model.Agreement;

public class AggiungiContrattoEditorInput implements IEditorInput {

    private String operazione = "Add an agreement";

    private Agreement agreement;

    public AggiungiContrattoEditorInput(Agreement agreement) {
        super();
        this.agreement = agreement;
    }

    @Override
    public boolean exists() {
        return false;
    }

    @Override
    public ImageDescriptor getImageDescriptor() {
        return AbstractUIPlugin.imageDescriptorFromPlugin("org.informaticisenzafrontiere.openstaff", IImageKeys.AGGIUNGI_CONTRATTO);
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

    public Agreement getAgreement() {
        return agreement;
    }
}

package org.wsmostudio.choreography.navigator.actions;

import org.eclipse.jface.dialogs.*;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.wsmo.common.Identifier;
import org.wsmo.factory.ChoreographyFactory;
import org.wsmo.service.choreography.Choreography;
import org.wsmo.service.signature.StateSignature;
import org.wsmostudio.choreography.ChoreographyPlugin;
import org.wsmostudio.runtime.WSMORuntime;
import org.wsmostudio.ui.*;
import org.wsmostudio.ui.editors.model.ObservableModel;
import org.wsmostudio.ui.views.navigator.actions.AbstractAction;

public class CreateStateSignatureAction extends AbstractAction {

    public void run() {
        Choreography chor = (Choreography) ((IStructuredSelection) navigator.getTree().getSelection()).getFirstElement();
        if (chor.getStateSignature() != null) {
            MessageDialog.openWarning(navigator.getSite().getShell(), "Incorrect Usage", "The selected choreography already has a state signature set!");
            return;
        }
        ObservableModel uiModel = (ObservableModel) navigator.getWsmoInput().getAdapter(ObservableModel.class);
        IdentifierInputDialog iriInputDialog = new IdentifierInputDialog(navigator.getSite().getShell(), "New State Signature", "New State Signature Identifier (leave blank for anonymous): ", Utils.findTopContainer(uiModel), WSMORuntime.getRuntime().getWsmoFactory(), true);
        if (iriInputDialog.open() != Dialog.OK) {
            return;
        }
        Identifier sSigID = iriInputDialog.getIdentifier();
        ChoreographyFactory fact = ChoreographyPlugin.getFactory();
        StateSignature sSig = fact.containers.createStateSignature(sSigID);
        chor.setStateSignature(sSig);
        uiModel.setChanged();
        navigator.fireEntityChanged(chor);
        navigator.getTree().refresh(chor);
        navigator.getTree().setExpandedState(chor, true);
    }
}

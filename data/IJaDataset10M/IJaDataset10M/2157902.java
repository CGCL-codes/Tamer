package org.wsmostudio.ui.views.navigator.actions;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.omwg.ontology.*;
import org.wsmo.common.Entity;
import org.wsmo.common.exception.InvalidModelException;
import org.wsmo.service.Capability;
import org.wsmostudio.runtime.LogManager;
import org.wsmostudio.ui.editors.model.*;

/**
 * An action belonging to the context menu of the <i>WSMO Navigator</i> view.
 * This action removes the selected Axiom definition from the containing
 * Ontology.
 *
 * @author not attributable
 * @version $Revision: 469 $ $Date: 2006-01-09 07:51:14 -0500 (Mon, 09 Jan 2006) $
 */
public class RemoveAxiomAction extends AbstractAction {

    public void run() {
        Axiom targetAxiom = (Axiom) ((IStructuredSelection) navigator.getTree().getSelection()).getFirstElement();
        if (false == navigator.ensureEditorForEntityIsClosed(targetAxiom)) {
            return;
        }
        ObservableModel uiModel = (ObservableModel) navigator.getWsmoInput().getAdapter(ObservableModel.class);
        Entity contanerEntity = null;
        try {
            if (uiModel instanceof OntologyModel) {
                ((OntologyModel) uiModel).removeAxiom(targetAxiom);
                contanerEntity = targetAxiom.getOntology();
            } else {
                Object containerObject = navigator.getTree().getTree().getSelection()[0].getParentItem().getData();
                if (containerObject instanceof Capability) {
                    Capability capaEntity = (Capability) containerObject;
                    if (capaEntity.listAssumptions().contains(targetAxiom)) {
                        capaEntity.removeAssumption(targetAxiom);
                    } else if (capaEntity.listPreConditions().contains(targetAxiom)) {
                        capaEntity.removePreCondition(targetAxiom);
                    } else if (capaEntity.listPostConditions().contains(targetAxiom)) {
                        capaEntity.removePostCondition(targetAxiom);
                    } else if (capaEntity.listEffects().contains(targetAxiom)) {
                        capaEntity.removeEffect(targetAxiom);
                    }
                    if (uiModel != null) {
                        uiModel.setChanged();
                    }
                    contanerEntity = capaEntity;
                }
            }
            if (contanerEntity != null) {
                navigator.getTree().refresh(contanerEntity);
            }
        } catch (InvalidModelException ime) {
            LogManager.logError("Error deleting axiom!", ime);
        }
    }
}

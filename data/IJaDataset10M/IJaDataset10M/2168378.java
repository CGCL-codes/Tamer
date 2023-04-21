package com.sri.emo.wizard.completion;

import com.jcorporate.expresso.core.controller.Transition;
import com.jcorporate.expresso.core.db.DBException;
import com.sri.emo.controller.CompletionWizardAction;
import com.sri.emo.wizard.WizardException;
import com.sri.emo.wizard.completion.persistence.CompletionDefinition;
import com.sri.emo.wizard.expressoimpl.WizardController;
import com.sri.emo.wizard.wizardgateway.RunWizard;
import com.sri.emo.wizard.wizardgateway.WizardGatewayController;

/**
 * Allows for locating completion wizards for a given node.
 *
 * @author Michael Rimov
 * @version 1.0
 */
public class CompletionWizardSelector {

    public CompletionWizardSelector() {
        super();
    }

    /**
     * Resolves a definingNodeId to a CompletionWizard.
     *
     * @param definingNodeId         String the target node id by which the completion wizard
     *                               is found.
     * @param optionalNodeToModifyId String the node id to operate the wizard on.  May be null.
     * @return Transition to the appropriate wizard gateway with parameters populated
     *         or null if no appropriate completion wizard is found.
     * @throws WizardException
     */
    public Transition locateCompletionWizardForNode(final String definingNodeId, final String optionalNodeToModifyId) throws WizardException {
        try {
            CompletionDefinition definition = new CompletionDefinition();
            definition.setField(CompletionDefinition.FLD_TARGET_NODE, definingNodeId);
            if (definition.find()) {
                Transition wizardGateway = new Transition();
                wizardGateway.setControllerObject(WizardGatewayController.class);
                wizardGateway.setState(RunWizard.STATE_NAME);
                wizardGateway.setLabel("Run Wizard");
                wizardGateway.addParam(WizardController.WIZ_PARAMETER_ID, definition.getField(CompletionDefinition.FLD_ID));
                if (optionalNodeToModifyId != null) {
                    wizardGateway.addParam(CompletionWizardAction.PARAM_TARGET_ID, optionalNodeToModifyId);
                }
                return wizardGateway;
            } else {
                return null;
            }
        } catch (DBException ex) {
            throw new WizardException("Error searching for matching completion wizard", ex);
        }
    }
}

package com.sri.emo.wizard.wizardgateway;

import com.jcorporate.expresso.core.controller.*;
import com.jcorporate.expresso.core.db.DBException;
import com.sri.common.controller.StateHandler;
import com.sri.emo.dbobj.WizDefinition;
import com.sri.emo.wizard.WizDefinitionRepository;
import com.sri.emo.wizard.expressoimpl.WizardController;

/**
 * Prompts to delete a wizard.
 *
 * @author Michael Rimov
 */
public class PromptDeleteWizard extends WizardGatewayHandler implements StateHandler {

    /**
     * Name of the State for which this handler deals with.
     */
    public static final String STATE_NAME = "promptDelete";

    /**
     * Friendly description of this handler.
     */
    public static final String STATE_DESCRIPTION = "Prompt to Delete a Wizard";

    /**
     * Constructs this particular state handler.
     *
     * @param handlerOwner Controller the controller that is the parent of this
     *                     state handler.
     * @param myRepository WizDefinitionRepository the repository to use
     *                     for data access methods.
     */
    public PromptDeleteWizard(final Controller handlerOwner, final WizDefinitionRepository myRepository) {
        super(handlerOwner, myRepository);
    }

    /**
     * Called to handle the request.
     *
     * @param request  ControllerRequest The Function's ControllerRequest
     *                 object.
     * @param response ControllerResponse The Function's ControllerResponse
     *                 object.
     * @throws DBException         upon underlying database exception error.
     * @throws ControllerException upon underlying ControllerException error.
     */
    public void handleRequest(final ExpressoRequest request, final ExpressoResponse response) throws DBException, ControllerException {
        response.setTitle("Confirm Delete Wizard");
        WizDefinition wizdef = getWizDef(request);
        response.add(new Output("prompt", wizdef.getWizName()));
        Transition yes = new Transition("yes", "yes", this.getOwner().getClass(), DoDeleteWizard.STATE_NAME);
        yes.addParam(WizardController.WIZ_PARAMETER_ID, request.getParameter(WizardController.WIZ_PARAMETER_ID));
        response.add(yes);
        Transition no = new Transition("no", "no", this.getOwner().getClass(), ListWizards.STATE_NAME);
        response.add(no);
    }
}

package com.sri.emo.wizard.selection.management;

import com.jcorporate.expresso.core.controller.*;
import com.jcorporate.expresso.core.db.DBException;
import com.sri.common.controller.StateHandler;
import com.sri.emo.dbobj.WizStep;

/**
 * State that prompts for instructions.
 *
 * @author Michael Rimov
 */
public class PromptInstructions extends EditFinalState implements StateHandler {

    /**
     * Id of the input parameter on the form.
     */
    public static final String PARAM_INPUT_ID = "Emo.StepType";

    /**
     * Constant for access to this state.
     */
    public static final String STATE_NAME = "promptInstructions";

    /**
     * Constant for description of this state.
     */
    public static final String STATE_DESCRIPTION = "Enter Instructions For Step";

    /**
     * Default constructor.
     *
     * @param wizId  The wizard ID.
     * @param stepId The step ID which may be null if we are dealing with a new step.
     * @throws DBException Upon database exception.
     */
    public PromptInstructions(final String wizId, final String stepId) throws DBException {
        super(wizId, stepId);
    }

    /**
     * Prompts for any instructions.  There are no additional types - per se
     * <p>{@inheritDoc}</p>
     *
     * @param request  ExpressoRequest The Function's ExpressoRequest
     *                 object.
     * @param response ExpressoResponse The Function's ExpressoResponse
     *                 object.
     * @throws DBException         upon underlying database exception error.
     * @throws ControllerException upon underlying ControllerException error.
     */
    public void handleRequest(final ExpressoRequest request, final ExpressoResponse response) throws DBException, ControllerException {
        String title = "Enter Instructions for " + getStepTitle();
        response.setTitle(title);
        response.add(new Output("pageTitle", title));
        ErrorCollection ec = request.getErrorCollection();
        if (ec == null) {
            ec = new ErrorCollection();
        }
        WizStep step = getStepFromParameters(request, ec);
        if (ec.getErrorCount() > 0) {
            response.saveErrors(ec);
        }
        Block commonDef = renderCommonControls(step, request, response);
        response.add(commonDef);
        addStepTypeLabel(response, step);
        if (isEditing()) {
            Transition edit = buildEditTypeTransition(step);
            response.add(edit);
        }
        Transition save = buildSaveAndReturnTransition(step);
        response.add(save);
        response.add(buildCancelTransition());
    }
}

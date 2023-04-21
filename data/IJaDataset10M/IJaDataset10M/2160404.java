package com.sri.emo.wizard.selection.management;

import com.jcorporate.expresso.core.controller.*;
import com.jcorporate.expresso.core.db.DBException;
import com.jcorporate.expresso.services.controller.ui.DefaultAutoElement;
import com.sri.emo.dbobj.StepAttributes;
import com.sri.emo.dbobj.WizStep;
import java.util.HashMap;
import java.util.Map;

/**
 * Combines common controls into a step editor.
 *
 * @author Michael Rimov
 */
public class EditFinalState extends AbstractStepHandler {

    /**
     * Constant for title field.
     */
    public static final String PARAM_TITLE = "Title";

    /**
     * Constant for directive field.
     */
    public static final String PARAM_DIRECTIVE = "Directive";

    /**
     * Constant for helptext field.
     */
    public static final String PARAM_HELPTEXT = "HelpText";

    /**
     * Name of the save transition.
     */
    public static final String TRANSITION_SAVE = "save";

    /**
     * Name of the edit transition.
     */
    public static final String TRANSITION_EDIT = "edit";

    /**
     * Constant in the session where attributes are stored.
     */
    public static final String ATTRIBUTE_SESSION_KEY = EditFinalState.class + ".Attributes";

    /**
     * Constant for where attributes are stored in the Request context.
     */
    public static final String REQUEST_ATTRIBUTES = "ObjectAttributes";

    /**
     * Default constructor.
     *
     * @param wizId  The wizard id.
     * @param stepId The Step Id.
     * @throws DBException upon error.
     */
    public EditFinalState(final String wizId, final String stepId) throws DBException {
        super(wizId, stepId);
    }

    /**
     * Renders the controls that are common to each of the 'last definition'
     * steps.  These are <tt>Title</tt> <tt>Helptext</tt> <tt>Directive</tt>
     *
     * @param stepDefinition WizStep
     * @param request        ExpressoRequest
     * @param response       ExpressoResponse
     * @return block instance containing input variables.
     * @throws ControllerException upon Input rendering error.
     * @throws DBException         upon DataObject.getField() error.
     */
    protected Block renderCommonControls(final WizStep stepDefinition, final ExpressoRequest request, final ExpressoResponse response) throws ControllerException, DBException {
        Block block = DefaultAutoElement.getAutoControllerElement().createDBObjectBlock(request, response, stepDefinition);
        Input input = new Input(WizStep.FLD_HELPTEXT);
        input.setDefaultValue(stepDefinition.getHelpTextRaw());
        block.add(input);
        block.setName("wizstep");
        return block;
    }

    /**
     * Returns true if we didn't try to save and we got an error collection.  Then
     * we should jump back once in the workflow.
     *
     * @param ec       ErrorCollection to examine if we need to transition
     * @param response ExpressoResponse the ExpressoResponse object.
     * @return boolean true if we need to transition to some other prompt state.
     */
    protected boolean isNeedTransitionOnError(final ErrorCollection ec, final ExpressoResponse response) {
        if (ec.getErrorCount() > 0) {
            return (!(WizardStepController.STATE_DO_ADD.equals(response.getRequestedState()) || WizardStepController.STATE_DO_EDIT.equals(response.getRequestedState())));
        } else {
            return false;
        }
    }

    /**
     * Parses the appropriate attributes from the controller request and
     * saves it to the session for 'save' retrieval.
     *
     * @param request ExpressoRequest the ExpressoRequest object
     * @return Map the Map
     * @throws ControllerException upon error.
     */
    protected Map buildAndStoreStepAttributes(final ExpressoRequest request) throws ControllerException {
        Map returnValue = new HashMap();
        String val;
        val = request.getParameter(StepAttributes.ATTRIBUTE_INSTANCE_FIELD);
        if (val != null && val.length() > 0) {
            returnValue.put(StepAttributes.ATTRIBUTE_INSTANCE_FIELD, val);
        }
        val = request.getParameter(StepAttributes.ATTRIBUTE_INSTANCE_ID);
        if (val != null && val.length() > 0) {
            returnValue.put(StepAttributes.ATTRIBUTE_INSTANCE_ID, val);
        }
        val = request.getParameter(StepAttributes.ATTRIBUTE_MODEL);
        if (val != null && val.length() > 0) {
            returnValue.put(StepAttributes.ATTRIBUTE_MODEL, val);
        }
        val = request.getParameter(StepAttributes.ATTRIBUTE_MODEL_FIELD);
        if (val != null && val.length() > 0) {
            returnValue.put(StepAttributes.ATTRIBUTE_MODEL_FIELD, val);
        }
        val = request.getParameter(StepAttributes.ATTRIBUTE_TEXT_STYLE);
        if (val != null && val.length() > 0) {
            returnValue.put(StepAttributes.ATTRIBUTE_TEXT_STYLE, val);
        }
        request.getSession().setPersistentAttribute(ATTRIBUTE_SESSION_KEY, returnValue);
        return returnValue;
    }

    /**
     * Constructs the 'save and return' transition.  Sub classes will need
     * to add their own parameters to this transition because some edit
     * pages have gone through different workflows and will have additional
     * data attached to them.  All WizStep fields to be modified should
     * be sent with this parameter.
     *
     * @param step WizStep
     * @return Transition that might be needed to have extra parameters
     *         if needed from other steps.
     * @throws DBException upon getField() error.
     */
    protected Transition buildSaveAndReturnTransition(final WizStep step) throws DBException {
        Transition t = new Transition();
        t.setName(TRANSITION_SAVE);
        t.setLabel("Save and return to main wizard page");
        t.setControllerObject(WizardStepController.class);
        t.addParam(WizStep.FLD_STEP_TYPE, step.getType() + "");
        t.addParam(WizStep.FLD_WIZID, step.getWizId());
        if (isEditing()) {
            t.setState(WizardStepController.STATE_DO_EDIT);
            t.addParam(WizStep.FLD_ID, step.getId());
        } else {
            t.setState(WizardStepController.STATE_DO_ADD);
        }
        return t;
    }

    /**
     * Constructs a transition that determines the 'step type'.
     *
     * @param stepDefinition WizStep
     * @return Transition
     * @throws DBException         upon DBObject.getField() Error
     * @throws ControllerException upon response.add() error.
     */
    protected Transition buildEditTypeTransition(final WizStep stepDefinition) throws ControllerException, DBException {
        stepDefinition.getType();
        Transition t = new Transition();
        t.setLabel("Edit");
        t.setName(TRANSITION_EDIT);
        t.setControllerObject(WizardStepController.class);
        t.addParam(WizStep.FLD_STEP_TYPE, stepDefinition.getStepType() + "");
        t.addParam(WizStep.FLD_WIZID, stepDefinition.getWizId());
        t.setState(PromptStepType.STATE_NAME);
        assert stepDefinition.getId().length() != 0 : "There should be an id defined for wizstep for edit link building";
        t.addParam(WizStep.FLD_ID, stepDefinition.getId());
        return t;
    }

    /**
     * Creates output for the text label of what type of step we're dealing
     * with. Saves the output to the name &quot;stepType&quot;
     *
     * @param response ExpressoResponse the response to add the output to.
     * @param step     WizStep the step we're working with.
     * @throws DBException         upon database error.
     * @throws ControllerException upon controller error.
     */
    protected void addStepTypeLabel(final ExpressoResponse response, final WizStep step) throws DBException, ControllerException {
        response.add(new Output("stepType", step.getValidValueDescrip(WizStep.FLD_STEP_TYPE)));
    }
}

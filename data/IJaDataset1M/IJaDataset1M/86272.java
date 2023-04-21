package org.jrcaf.flow.ui.internal.action;

import java.util.Map;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.jrcaf.core.JRCAFCorePlugin;
import org.jrcaf.core.call.CallType;
import org.jrcaf.core.model.ModelEvent;
import org.jrcaf.core.parameter.ClassValue;
import org.jrcaf.core.parameter.IdRefValue;
import org.jrcaf.flow.action.IActionCaller;
import org.jrcaf.flow.action.IActionDefinition;
import org.jrcaf.flow.action.TargetMapping;
import org.jrcaf.flow.activity.IActivity;
import org.jrcaf.flow.ui.UIFlowPlugin;
import org.jrcaf.flow.ui.action.StandardFlowUIActionFactory.ShowPartType;
import org.jrcaf.mvc.MVCPlugin;
import org.jrcaf.mvc.part.IActionHandler;
import org.jrcaf.mvc.part.IDirtyListener;
import org.jrcaf.mvc.part.IPart;
import org.jrcaf.mvc.part.IPartDefinition;
import org.jrcaf.mvc.part.IPartListener;
import org.jrcaf.mvc.part.parameter.PartReferenceValue;
import org.jrcaf.mvc.registry.IPartRegistry;
import org.jrcaf.mvc.validator.IValidationResultStrategie;

/**
 *  Abstract base class for MVC-Actions.
 */
public abstract class AbstractMVCAction extends AbstractViewAction implements IActionHandler, IDirtyListener, IPartListener {

    private static final String SAVE_RESULT_NAME = "save";

    protected IPart part;

    /**
    * Creates an AbstractMVCAction.
    * @param aParameters The parameters.
    * @param aActivity The enclosing activity.
    * @param aActionDefinition The action definition.
    * @param aCalledBy The caller.
    * @category Init
    */
    public AbstractMVCAction(Map<String, Object> aParameters, IActivity aActivity, IActionDefinition aActionDefinition, IActionCaller aCalledBy) {
        super(aParameters, aActivity, aActionDefinition, aCalledBy);
    }

    /**
    * Called before the action is finished to do some cleanup.
    * @param aResults The results of the finished action.
    */
    protected void preFinishAction(Map<String, Object> aResults, String aResultName) {
        part.finish(aResultName);
        part.readResult(aResults);
    }

    @Override
    protected void handleError(String aText, String aMessage) {
        super.handleError(aText, aMessage);
        MessageBox errorMessage = new MessageBox(Display.getDefault().getActiveShell());
        errorMessage.setText(aText);
        errorMessage.setMessage(aMessage);
        errorMessage.open();
    }

    /**
    * @see org.jrcaf.flow.action.AbstractAction#updateAction(java.util.Map, java.lang.String, org.jrcaf.flow.action.TargetMapping)
    */
    @Override
    public void updateAction(Map<String, Object> aParameters, String aCalled, TargetMapping aTargetMapping) {
        super.updateAction(aParameters, aCalled, aTargetMapping);
        part.update(aParameters, aCalled, aTargetMapping.targetName);
    }

    @Override
    protected void dispose() {
        part.dispose();
        super.dispose();
    }

    protected void createPart() {
        final Object partRef = getParameter(IPartRegistry.PART_REF, false);
        IPartDefinition partDefinition = null;
        if (partRef == null) {
            final ClassValue viewClassValue = (ClassValue) getParameter(ShowPartType.VIEW_CLASS_PARAMATER_NAME, false);
            final ClassValue viewControllerClassValue = (ClassValue) getParameter(ShowPartType.VIEW_CONTROLLER_CLASS_PARAMATER_NAME, false);
            final ClassValue controllerClassValue = (ClassValue) getParameter(ShowPartType.CONTROLLER_CLASS_PARAMATER_NAME, false);
            if ((viewClassValue != null) && (controllerClassValue != null)) if (viewControllerClassValue != null) partDefinition = MVCPlugin.getPartRegistry().createPartDefinition(viewClassValue.getConfigurationElement(), viewControllerClassValue.getConfigurationElement(), controllerClassValue.getConfigurationElement(), viewClassValue.getContributorName()); else partDefinition = MVCPlugin.getPartRegistry().createPartDefinition(viewClassValue.getConfigurationElement(), controllerClassValue.getConfigurationElement(), viewClassValue.getContributorName()); else {
                UIFlowPlugin.getDefault().getLog().log(new Status(IStatus.ERROR, JRCAFCorePlugin.ID_PLUGIN, IStatus.OK, "IView or IController null", null));
                return;
            }
            partDefinition.setRuleDefinitions(getDefinition().getRules());
            partDefinition.setParameters(getParameters(), getDefinition().getParameters());
        } else {
            if (partRef instanceof PartReferenceValue) partDefinition = ((PartReferenceValue) partRef).getPartDefinition(); else if (partRef instanceof IdRefValue) partDefinition = MVCPlugin.getPartRegistry().getPartFor(((IdRefValue) partRef).getValue()); else {
                UIFlowPlugin.getDefault().getLog().log(new Status(IStatus.ERROR, JRCAFCorePlugin.ID_PLUGIN, IStatus.OK, "Unexpected type of partRef Parameter " + partRef.getClass().getName(), null));
                return;
            }
        }
        part = partDefinition.createPart(this, this, this, getValidationResultStrategy());
        part.addDirtyListener(this);
    }

    /**
    * Saves the changes made by the MVC action.
    * @param results The results of the action.
    */
    public void save(Map<String, Object> results) {
        callAction(SAVE_RESULT_NAME, results);
    }

    /**
    * Updates the MVC action.
    * @param aEvent The update event.
    */
    public void update(@SuppressWarnings("unused") ModelEvent aEvent) {
    }

    /**
    * @return The IValidationResultStrategie for this Action
    */
    public IValidationResultStrategie getValidationResultStrategy() {
        return null;
    }

    /**
    * @return Returns the part
    */
    public IPart getPart() {
        return part;
    }

    /**
    * @see org.jrcaf.mvc.part.IPartListener#setSuccessDefault(boolean)
    */
    public void setSuccessDefault(boolean aValue) {
        return;
    }

    /**
    * @see org.jrcaf.mvc.part.IPartListener#setCancelDefault(boolean)
    */
    public void setCancelDefault(@SuppressWarnings("unused") boolean aValue) {
        return;
    }

    /**
    * @see org.jrcaf.mvc.part.IPartListener#setSuccessEnabled(boolean)
    */
    public void setSuccessEnabled(@SuppressWarnings("unused") boolean aValue) {
        return;
    }

    /**
    * @see org.jrcaf.mvc.part.IPartListener#setCancelEnabled(boolean)
    */
    public void setCancelEnabled(@SuppressWarnings("unused") boolean aValue) {
        return;
    }

    /**
    * @see org.jrcaf.mvc.part.IDirtyListener#dirtyStateChanged(boolean)
    */
    public void dirtyStateChanged(boolean aDirtyFlag) {
        if (aDirtyFlag == true) setDirty(); else resetDirty();
    }

    /**
    * Sets the action state to dirty. 
    */
    public void setDirty() {
        return;
    }

    /**
    * Resets the action state to clean.
    */
    public void resetDirty() {
        return;
    }

    /**
    * @see org.jrcaf.mvc.part.IActionHandler#invoke(java.util.Map, org.jrcaf.core.call.CallType, java.lang.String)
    */
    public void invoke(Map<String, Object> aResults, CallType aCallType, String aName) {
        switch(aCallType) {
            case CALL:
                {
                    callAction(aName, aResults);
                    break;
                }
            case FORK:
                {
                    forkAction(aName, aResults);
                    break;
                }
            case FORWARD:
                {
                    finishAction(aName, aResults);
                    break;
                }
            case RETURN:
                {
                }
            case END:
                {
                }
        }
    }
}

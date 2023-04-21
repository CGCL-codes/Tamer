package org.jrcaf.flow.ui.action;

import java.util.Map;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.jrcaf.flow.FlowPlugin;
import org.jrcaf.flow.action.IActionCaller;
import org.jrcaf.flow.action.IActionDefinition;
import org.jrcaf.flow.activity.IActivity;
import org.jrcaf.flow.ui.internal.action.AbstractMVCAction;

/**
 *  Action to show an error on action creation using a message box.
 */
public class FlowUIErrorAction extends AbstractMVCAction {

    /**
    * Creates a new {@link FlowUIErrorAction}
    * @param aParameters The parameters.
    * @param aActivity The enclosing activity.
    * @param aActionDefinition The action definition.
    * @param aCalledBy The caller.
    * @category Init
    */
    public FlowUIErrorAction(Map<String, Object> aParameters, IActivity aActivity, IActionDefinition aActionDefinition, IActionCaller aCalledBy) {
        super(aParameters, aActivity, aActionDefinition, aCalledBy);
    }

    /**
    * @see org.jrcaf.flow.action.IAction#execute()
    */
    public void execute() {
        final String message = "No action type " + getDefinition().getActionType();
        FlowPlugin.getDefault().getLog().log(new Status(IStatus.WARNING, FlowPlugin.ID_PLUGIN, IStatus.OK, message, null));
        Shell parent = (Shell) getParameter(PARENT_RESULT_NAME, false);
        if (parent == null) parent = getShellProvider().getShell();
        MessageBox messageBox = new MessageBox(parent);
        messageBox.setMessage(message);
        messageBox.setText("Error creating action.");
    }
}

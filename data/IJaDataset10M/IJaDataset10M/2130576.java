package org.isistan.flabot.edit.editor.actions;

import java.util.List;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.ui.actions.SelectionAction;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbenchPart;
import org.isistan.flabot.edit.componenteditor.editparts.ComponentDiagramEditPart;
import org.isistan.flabot.edit.editor.CommandExecutor;
import org.isistan.flabot.edit.editor.FlabotGraphicalEditor;
import org.isistan.flabot.edit.editormodel.Diagram;
import org.isistan.flabot.edit.ucmeditor.dialogs.EventDialog;
import org.isistan.flabot.edit.ucmeditor.editparts.UCMDiagramEditPart;
import org.isistan.flabot.messages.Messages;

/**
 * This action is used to Opens a dialog to add/edit/remove Condition Events.
 * 
 * @author $Author: franco $
 *
 */
public class RunEventManagerAction extends SelectionAction {

    /**
	 * Action id
	 */
    public static final String RUN_EVENT_MANAGER = "RUN_EVENT_MANAGER";

    /**
	 * Creates a new RunEventManagerAction in the given workbench part
	 * @param part
	 */
    public RunEventManagerAction(IWorkbenchPart part) {
        super(part);
        setText(Messages.getString("org.isistan.flabot.edit.editor.actions.RunEventManagerAction.text"));
        setId(RUN_EVENT_MANAGER);
    }

    /**
	 * Determines whether the action should be enabled or not.
	 * @return true if one object is selected, false otherwise
	 */
    @Override
    protected boolean calculateEnabled() {
        return canPerformAction();
    }

    /**
	 * Determines whether the action should be enabled or not.
	 * @return true if one object is selected, false otherwise
	 */
    private boolean canPerformAction() {
        if (getSelectedObjects().isEmpty()) return false;
        List parts = getSelectedObjects();
        if (parts.size() > 1) return false;
        Object o = parts.get(0);
        return (o instanceof UCMDiagramEditPart || o instanceof ComponentDiagramEditPart);
    }

    /**
	 * Opens a dialog to edit Condition Events, it returns the commands given by the dialog.
	 * 
	 * @return the created command
	 */
    private Command getCommand() {
        Diagram diagram = ((FlabotGraphicalEditor) getWorkbenchPart()).getModel();
        EventDialog managerdialog = new EventDialog(Display.getCurrent().getActiveShell());
        return managerdialog.open(diagram.getCoreModel(), diagram.getCoreModel().getConditionEvents());
    }

    /**
	 * Creates a command and then executes it.
	 */
    @Override
    public void run() {
        Command command = getCommand();
        CommandExecutor commandExecutor = (CommandExecutor) getWorkbenchPart().getAdapter(CommandExecutor.class);
        commandExecutor.executeCommand(command, false);
    }
}

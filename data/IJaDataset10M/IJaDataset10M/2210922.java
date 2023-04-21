package org.isistan.flabot.edit.ucmeditor.actions;

import java.util.Iterator;
import java.util.List;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.gef.ui.actions.SelectionAction;
import org.eclipse.ui.IWorkbenchPart;
import org.isistan.flabot.coremodel.ResponsibilityNode;
import org.isistan.flabot.edit.editor.CommandExecutor;
import org.isistan.flabot.edit.editor.commands.SetDetailLevelCommand;
import org.isistan.flabot.edit.editormodel.Diagram;
import org.isistan.flabot.edit.editormodel.VisualModel;
import org.isistan.flabot.edit.ucmeditor.editparts.UCMDiagramEditPart;
import org.isistan.flabot.messages.Messages;

/**
 * This action is used to show/hide all visual condition dependencies in UCM diagrams.
 * 
 * @author $Author: franco $
 *
 */
public class ShowHideAllConditionDependeciesAction extends SelectionAction {

    /**
	 * Action id
	 */
    public static final String SHOW_HIDE_VISUAL_DEPENDENCIES = "SHOW_HIDE_VISUAL_DEPENDENCIES";

    private boolean checked = false;

    /**
	 * Creates a new ShowHideAllConditionDependeciesAction in the given workbench part
	 * @param part
	 */
    public ShowHideAllConditionDependeciesAction(IWorkbenchPart part) {
        super(part);
        setText(Messages.getString("org.isistan.flabot.edit.ucmeditor.actions.ShowHideAllConditionDependeciesAction.text"));
        setId(SHOW_HIDE_VISUAL_DEPENDENCIES);
        setChecked(checked);
    }

    /**
	 * Determines whether the action should be enabled or not.
	 * @return true if a UCM diagram is selected, false otherwise
	 */
    @Override
    protected boolean calculateEnabled() {
        return canPerformAction();
    }

    /**
	 * Determines whether the action should be enabled or not.
	 * @return true if a UCM diagram is selected, false otherwise
	 */
    private boolean canPerformAction() {
        if (getSelectedObjects().isEmpty()) return false;
        List parts = getSelectedObjects();
        if (parts.size() > 1) return false;
        return (parts.get(0) instanceof UCMDiagramEditPart);
    }

    /**
	 * Creates a command and then executes it.
	 * Also changed the checked state of the action.
	 */
    @Override
    public void run() {
        Command command = getCommand();
        CommandExecutor commandExecutor = (CommandExecutor) getWorkbenchPart().getAdapter(CommandExecutor.class);
        commandExecutor.executeCommand(command, false);
        checked = !checked;
        setChecked(checked);
    }

    /**
	 * Creates and return a command to execute the change of level detail of all responsibility nodes in a diagram.
	 * 
	 * @return the created command
	 */
    public Command getCommand() {
        CompoundCommand commands = new CompoundCommand();
        int levelDetail = VisualModel.LOW_DETAIL;
        if (isChecked()) levelDetail = VisualModel.HIGH_DETAIL;
        Diagram diagram = (Diagram) ((EditPart) getSelectedObjects().get(0)).getModel();
        obtainCommands(commands, diagram.getChildren(), levelDetail);
        return commands;
    }

    /**
	 * Adds all the commands to change the level of detail of each responsibility node in the UCM diagram.
	 * It is a recursive function that will look in all children's sons.
	 * 
	 * @param commands the list of commands to add
	 * @param children the list of visual models
	 * @param levelDetail the new level of detail 
	 */
    protected void obtainCommands(CompoundCommand commands, List children, int levelDetail) {
        VisualModel retVisual = null;
        for (Iterator iter = children.iterator(); iter.hasNext() && retVisual == null; ) {
            VisualModel visual = (VisualModel) iter.next();
            if (visual.getSemanticModel() instanceof ResponsibilityNode) {
                if (visual.getDetailLevel() != levelDetail) commands.add(new SetDetailLevelCommand(visual, levelDetail));
            }
            obtainCommands(commands, visual.getChildren(), levelDetail);
        }
    }
}

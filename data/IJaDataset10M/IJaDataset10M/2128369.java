package org.isistan.flabot.edit.ucmeditor.actions;

import java.util.ArrayList;
import java.util.List;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.gef.ui.actions.SelectionAction;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbenchPart;
import org.isistan.flabot.coremodel.ComponentRole;
import org.isistan.flabot.coremodel.CoremodelFactory;
import org.isistan.flabot.coremodel.Responsibility;
import org.isistan.flabot.coremodel.ResponsibilityNode;
import org.isistan.flabot.coremodel.SimplePathNode;
import org.isistan.flabot.coremodel.StubNode;
import org.isistan.flabot.coremodel.UseCaseMap;
import org.isistan.flabot.edit.editor.CommandExecutor;
import org.isistan.flabot.edit.editor.dialogs.ResponsibilitySelectionDialog;
import org.isistan.flabot.edit.editormodel.NodeVisualModel;
import org.isistan.flabot.edit.editormodel.VisualModel;
import org.isistan.flabot.edit.ucmeditor.commands.visual.InsertResponsibilityCommand;
import org.isistan.flabot.edit.ucmeditor.editparts.PathNodeEditPart;
import org.isistan.flabot.messages.Messages;

/**
 * This action is used to insert a responsibility node
 * 
 * @author $Author: franco $
 *
 */
public class InsertResponsibilityAction extends SelectionAction {

    /**
	 * Action id
	 */
    public static final String INSERT_RESPONSIBILITY = "INSERT_RESPONSIBILITY";

    protected ComponentRole role;

    protected UseCaseMap map;

    /**
	 * Creates a new InsertResponsibilityAction in the given workbench part
	 * @param part
	 */
    public InsertResponsibilityAction(IWorkbenchPart part) {
        super(part);
        setText(Messages.getString("org.isistan.flabot.edit.ucmeditor.actions.InsertResponsibilityAction.text"));
        setId(INSERT_RESPONSIBILITY);
    }

    /**
	 * Determines whether the action should be enabled or not.
	 * @return true if a responsibility node is selected, false otherwise
	 */
    @Override
    protected boolean calculateEnabled() {
        return canPerformAction();
    }

    /**
	 * Determines whether the action should be enabled or not.
	 * @return true if a responsibility node is selected, false otherwise
	 */
    private boolean canPerformAction() {
        if (getSelectedObjects().isEmpty()) return false;
        List parts = getSelectedObjects();
        if (parts.size() > 1) return false;
        Object o = parts.get(0);
        if (!(o instanceof PathNodeEditPart)) return false;
        PathNodeEditPart part = (PathNodeEditPart) o;
        VisualModel parent = ((VisualModel) part.getModel()).getParent();
        if (parent == null || !(parent.getSemanticModel() instanceof ComponentRole)) return false;
        SimplePathNode node = (SimplePathNode) part.getSemanticModel();
        if (node instanceof ResponsibilityNode || node instanceof StubNode) return false;
        return !node.isEnd() && !node.isStart();
    }

    /**
	 * Returns a list of responsibilities. The responsibilitites associated with responsibility's role are returned.
	 * 
	 * @param visualNode the visual selected node
	 * @return a list of responsibilities
	 */
    protected List getResponsibilitiesList(VisualModel visualNode) {
        List responsibilities = new ArrayList();
        NodeVisualModel parent = (NodeVisualModel) visualNode.getParent();
        role = (ComponentRole) parent.getSemanticModel();
        if (role.getComponent() != null) {
            responsibilities = role.getComponent().getFeatures();
            map = role.getMap();
        }
        return responsibilities;
    }

    /**
	 * Creates and open a dialog to let the user select a responsibility and returns it.
	 * 
	 * @param visualNode the visual selected node
	 * @return the selected responsibility, or null
	 */
    protected Responsibility getSelectedResponsibility(VisualModel visualNode) {
        ResponsibilitySelectionDialog dialog = ResponsibilitySelectionDialog.getDefault();
        Responsibility selectedResponsibility = dialog.openSingle(Display.getCurrent().getActiveShell(), Messages.getString("org.isistan.flabot.edit.ucmeditor.actions.InsertResponsibilityAction.dialogName"), Messages.getString("org.isistan.flabot.edit.ucmeditor.actions.InsertResponsibilityAction.dialogDescription"), getResponsibilitiesList(visualNode), null);
        return selectedResponsibility;
    }

    /**
	 * Opens a dialog for the user to select a responsibility to associate to the responsibility node, then
	 * creates a command that executes the insertion. 
	 * 
	 * @return the created command
	 */
    protected Command getCommand() {
        List editparts = getSelectedObjects();
        PathNodeEditPart part = (PathNodeEditPart) editparts.get(0);
        NodeVisualModel visualNode = part.getCastedModel();
        Responsibility selectedResponsibility = getSelectedResponsibility(visualNode);
        CompoundCommand compound = new CompoundCommand(Messages.getString("org.isistan.flabot.edit.ucmeditor.actions.InsertResponsibilityAction.commandLabel"));
        if (selectedResponsibility != null) {
            ResponsibilityNode responsibilityNode = CoremodelFactory.eINSTANCE.createResponsibilityNode();
            responsibilityNode.setResponsibility(selectedResponsibility);
            responsibilityNode.setRole(role);
            responsibilityNode.setMap(map);
            compound.add(part.getConnectionsDeleteCommand(part.getTargetConnections()));
            compound.add(new InsertResponsibilityCommand(visualNode, responsibilityNode));
        }
        return compound;
    }

    /**
	 * Shows an edition dialog, creates a command and then executes it.
	 */
    @Override
    public void run() {
        Command command = getCommand();
        CommandExecutor commandExecutor = (CommandExecutor) getWorkbenchPart().getAdapter(CommandExecutor.class);
        commandExecutor.executeCommand(command, false);
    }
}

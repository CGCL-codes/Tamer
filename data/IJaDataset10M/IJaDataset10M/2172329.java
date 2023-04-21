package org.eclipse.emf.edit.ui.action;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.actions.BaseSelectionListenerAction;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.UnexecutableCommand;
import org.eclipse.emf.edit.domain.EditingDomain;

public class CommandActionHandler extends BaseSelectionListenerAction {

    /**
   * This keeps track of the editing domain of the action.
   */
    protected EditingDomain domain;

    /**
   * This keeps track of the command delegate that is created by {@link #createCommand}.
   */
    protected Command command;

    /**
   * This constructs and instance in this editing domain.
   */
    public CommandActionHandler(EditingDomain domain) {
        super("");
        this.domain = domain;
    }

    /**
   * This constructs and instance in this editing domain.
   */
    public CommandActionHandler(EditingDomain domain, String label) {
        super(label);
        this.domain = domain;
    }

    /**
   * This returns the action's domain.
   */
    public EditingDomain getEditingDomain() {
        return domain;
    }

    /**
   * This sets the action's domain.
   */
    public void setEditingDomain(EditingDomain domain) {
        this.domain = domain;
    }

    /**
   * This simply execute the command.
   */
    @Override
    public void run() {
        domain.getCommandStack().execute(command);
    }

    /**
   * When the selection changes, this will call {@link #createCommand} with the appropriate collection of selected objects.
   */
    @Override
    public boolean updateSelection(IStructuredSelection selection) {
        List<?> list = selection.toList();
        Collection<Object> collection = new ArrayList<Object>(list);
        command = createCommand(collection);
        return command.canExecute();
    }

    /**
   * This default implementation simply returns {@link org.eclipse.emf.common.command.UnexecutableCommand#INSTANCE}.
   */
    public Command createCommand(Collection<?> selection) {
        return UnexecutableCommand.INSTANCE;
    }
}

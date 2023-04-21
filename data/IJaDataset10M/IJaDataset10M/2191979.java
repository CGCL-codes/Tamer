package org.eclipse.bpel.common.ui.command;

import java.util.EventObject;
import org.eclipse.bpel.common.ui.details.IOngoingChange;
import org.eclipse.bpel.common.ui.editmodel.EditModelCommandStack;
import org.eclipse.bpel.common.ui.editmodel.PlaceHolderCommand;
import org.eclipse.bpel.common.ui.editmodel.EditModelCommandStack.SharedCommandStackChangedEvent;
import org.eclipse.bpel.common.ui.editmodel.EditModelCommandStack.SharedCommandStackListener;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CommandStack;
import org.eclipse.gef.commands.CommandStackListener;

/**
 * Support for using IOngoingChange with the EditModel framework.
 */
public class EditModelCommandFramework implements ICommandFramework {

    IOngoingChange currentChange;

    CommandStack commandStack;

    boolean ignoreEvents = false;

    public EditModelCommandFramework(EditModelCommandStack editModelCommandStack) {
        this.commandStack = editModelCommandStack;
        editModelCommandStack.addCommandStackListener(new CommandStackListener() {

            public void commandStackChanged(EventObject event) {
                if (ignoreEvents) return;
                if (event instanceof SharedCommandStackChangedEvent) {
                    SharedCommandStackChangedEvent e = (SharedCommandStackChangedEvent) event;
                    if (e.getProperty() == SharedCommandStackListener.EVENT_START_EXECUTE) {
                        applyCurrentChange();
                    }
                    if (e.getProperty() == SharedCommandStackListener.EVENT_START_UNDO) {
                        if (commandStack.getUndoCommand() instanceof PlaceHolderCommand) {
                            if (currentChange != null) currentChange.restoreOldState();
                        }
                    }
                }
            }
        });
    }

    public void abortCurrentChange() {
        finishCurrentChange(true);
    }

    public void applyCurrentChange() {
        finishCurrentChange(false);
    }

    public void notifyChangeInProgress(IOngoingChange ongoingChange) {
        if (currentChange != ongoingChange) {
            applyCurrentChange();
            if (commandStack.getUndoCommand() instanceof PlaceHolderCommand) {
                throw new IllegalStateException();
            }
            PlaceHolderCommand placeholderCommand = new PlaceHolderCommand(ongoingChange.getLabel());
            ignoreEvents = true;
            try {
                commandStack.execute(placeholderCommand);
            } finally {
                ignoreEvents = false;
            }
            currentChange = ongoingChange;
        }
    }

    public void notifyChangeDone(IOngoingChange ongoingChange) {
        if (currentChange == ongoingChange) applyCurrentChange();
    }

    public void execute(Command command) {
        commandStack.execute(command);
    }

    protected void finishCurrentChange(boolean changeUndone) {
        if (currentChange == null) return;
        IOngoingChange change = currentChange;
        currentChange = null;
        if (ignoreEvents) {
            throw new IllegalStateException();
        }
        if (!(commandStack.getUndoCommand() instanceof PlaceHolderCommand)) {
            throw new IllegalStateException();
        }
        ignoreEvents = true;
        try {
            commandStack.undo();
        } finally {
            ignoreEvents = false;
        }
        Command cmd = change.createApplyCommand();
        if (cmd != null) {
            cmd.setLabel(change.getLabel());
            if (changeUndone) {
                change.restoreOldState();
            } else {
                commandStack.execute(cmd);
            }
        } else {
            change.restoreOldState();
        }
    }
}

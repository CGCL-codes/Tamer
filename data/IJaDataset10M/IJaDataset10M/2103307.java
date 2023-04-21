package org.suse.ui.command;

import org.eclipse.gef.commands.Command;
import org.suse.ui.model.Transition;

public class TransitionSetNameCommand extends Command {

    private String oldName;

    private String newName;

    private Transition transition;

    public void setName(String name) {
        if (name == null) {
            name = "";
        }
        newName = name;
    }

    public void setTransition(Transition transition) {
        this.transition = transition;
    }

    public void execute() {
        oldName = transition.getName();
        transition.setName(newName);
    }

    public void undo() {
        transition.setName(oldName);
    }
}

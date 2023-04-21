package com.prolix.editor.commands;

import org.eclipse.gef.commands.Command;
import uk.ac.reload.straker.datamodel.learningdesign.LD_DataComponent;

public class SetTitleCommand extends Command {

    private LD_DataComponent component;

    private String old_title, new_title;

    public SetTitleCommand(LD_DataComponent component, String new_title) {
        super("Set Title");
        this.component = component;
        this.old_title = this.component.getTitle();
        this.new_title = new_title;
    }

    public boolean canExecute() {
        if (this.component == null) return false; else return true;
    }

    public void execute() {
        this.component.setTitle(this.new_title);
        this.component.getDataModel().fireDataComponentChanged(this.component);
    }

    public void redo() {
        execute();
    }

    public void undo() {
        this.component.setTitle(this.old_title);
        this.component.getDataModel().fireDataComponentChanged(this.component);
    }
}

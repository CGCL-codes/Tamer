package org.eclipse.gef.examples.logicdesigner.model.commands;

import org.eclipse.gef.examples.logicdesigner.LogicMessages;
import org.eclipse.gef.examples.logicdesigner.model.LogicDiagram;
import org.eclipse.gef.examples.logicdesigner.model.LogicSubpart;

public class AddCommand extends org.eclipse.gef.commands.Command {

    private LogicSubpart child;

    private LogicDiagram parent;

    private int index = -1;

    public AddCommand() {
        super(LogicMessages.AddCommand_Label);
    }

    public void execute() {
        if (index < 0) parent.addChild(child); else parent.addChild(child, index);
    }

    public LogicDiagram getParent() {
        return parent;
    }

    public void redo() {
        if (index < 0) parent.addChild(child); else parent.addChild(child, index);
    }

    public void setChild(LogicSubpart subpart) {
        child = subpart;
    }

    public void setIndex(int i) {
        index = i;
    }

    public void setParent(LogicDiagram newParent) {
        parent = newParent;
    }

    public void undo() {
        parent.removeChild(child);
    }
}

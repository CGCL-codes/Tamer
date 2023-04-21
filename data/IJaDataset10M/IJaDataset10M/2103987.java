package edu.thu.keg.iw.app.description.ui.flow.commands;

import org.eclipse.gef.commands.Command;
import edu.thu.keg.iw.app.description.model.flow.FlowNodeModel;
import edu.thu.keg.iw.app.description.model.flow.SubFlowNodeModel;

public class CreateFlowNodeChildCommand extends Command {

    private FlowNodeModel child;

    private SubFlowNodeModel parent;

    public void setChild(FlowNodeModel child) {
        this.child = child;
    }

    public void setParent(SubFlowNodeModel parent) {
        this.parent = parent;
    }

    public void execute() {
        if (parent != null && child != null) {
            parent.addFlowNode(child);
        }
    }

    public void undo() {
        if (parent != null && child != null) parent.removeFlowNode(child);
    }
}

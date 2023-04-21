package org.suse.ui.command;

import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.commands.Command;
import org.suse.ui.model.Node;

public class NodeChangeConstraintCommand extends Command {

    private Rectangle newConstraint;

    private Rectangle oldConstraint;

    private Node node;

    public void execute() {
        oldConstraint = new Rectangle(node.getConstraint());
        node.setConstraint(newConstraint);
    }

    public void undo() {
        node.setConstraint(oldConstraint);
    }

    public void setNewConstraint(Rectangle r) {
        newConstraint = r;
    }

    public void setNode(Node node) {
        this.node = node;
    }
}

package org.plazmaforge.studio.dbdesigner.commands;

import org.eclipse.draw2d.geometry.*;
import org.eclipse.gef.commands.Command;
import org.plazmaforge.studio.dbdesigner.model.ERBasicNode;

public class SetConstraintCommand extends Command {

    private Point newLoc;

    private Point oldLoc;

    private Dimension newDim;

    private Dimension oldDim;

    private ERBasicNode node;

    public SetConstraintCommand() {
    }

    public void setPart(ERBasicNode node) {
        this.node = node;
    }

    public void setLocation(Rectangle rectangle) {
        setLocation(rectangle.getLocation());
        setSize(rectangle.getSize());
    }

    public void setLocation(Point point) {
        oldLoc = node.getLocation();
        newLoc = point;
    }

    public void setSize(Dimension dimension) {
        oldDim = node.getSize();
        newDim = dimension;
    }

    public void execute() {
        node.getDiagram().selectNode(node);
        node.setConstraints(newLoc, newDim);
    }

    public void undo() {
        node.getDiagram().selectNode(node);
        node.setConstraints(oldLoc, oldDim);
    }
}

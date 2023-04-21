package org.drawx.gef.sample.client.tool.example.model.command;

import org.drawx.gef.sample.client.tool.example.model.OrangeModel;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.commands.Command;

public class MoveCommand extends Command {

    private Object orange;

    private Rectangle constraint;

    private Rectangle old;

    public MoveCommand(Object model, Rectangle rect) {
        orange = model;
        constraint = rect;
        old = ((OrangeModel) orange).getConstraint();
    }

    public void execute() {
        ((OrangeModel) orange).setConstraint(constraint);
    }

    public void undo() {
        ((OrangeModel) orange).setConstraint(old);
    }
}

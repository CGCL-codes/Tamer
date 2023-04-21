package net.kornr.abstractcanvas.awt.command;

import java.awt.Graphics2D;
import java.awt.geom.NoninvertibleTransformException;
import java.util.LinkedList;
import net.kornr.abstractcanvas.awt.CanvasStack;
import net.kornr.abstractcanvas.awt.CanvasState;
import net.kornr.abstractcanvas.awt.Command;

public class ContextRestore implements Command {

    public void applyCommand(LinkedList<CanvasState> statestack, Graphics2D g, CanvasStack canvastack) {
        CanvasState state = statestack.getLast();
        try {
            g.transform(state.getTransform().createInverse());
        } catch (NoninvertibleTransformException e) {
            e.printStackTrace();
        }
        statestack.removeLast();
        state = statestack.getLast();
        g.transform(state.getTransform());
    }
}

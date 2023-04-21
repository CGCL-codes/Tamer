package net.kornr.abstractcanvas.awt.command;

import java.awt.Graphics2D;
import java.util.LinkedList;
import net.kornr.abstractcanvas.awt.CanvasStack;
import net.kornr.abstractcanvas.awt.CanvasState;
import net.kornr.abstractcanvas.awt.Command;

public class SetPixelHeight extends AbstractDoubleValueContainer implements Command {

    public SetPixelHeight(double value) {
        super(value);
    }

    public void applyCommand(LinkedList<CanvasState> statestack, Graphics2D g, CanvasStack canvastack) {
        canvastack.setSize(canvastack.getWidth(), (int) getValue());
    }
}

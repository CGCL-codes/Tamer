package net.kornr.abstractcanvas.awt;

import java.awt.Graphics2D;
import java.util.LinkedList;

/**
 * The Command interface shall be implemented by classes representing 
 * a canvas drawing unit (see the net.kornr.abstractcanvas.awt.command package)
 * 
 */
public interface Command {

    public void applyCommand(LinkedList<CanvasState> statestack, Graphics2D g, CanvasStack canvastack);
}

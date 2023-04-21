package org.galaxy.gpf.event.graphics;

import java.awt.event.ActionEvent;
import javax.swing.Action;
import org.galaxy.gpf.event.AbstractApplicationEvent;

/**
 * Action for drawing a rectangle.
 * 
 * @author Cheng Liang
 * @version 1.0.0
 */
public class DrawRectangleAction extends AbstractApplicationEvent {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -1232175075644277534L;

    /** The action id. */
    public static final String id = "action.draw.rectangle";

    /** The action. */
    private static final DrawRectangleAction action = new DrawRectangleAction();

    /**
	 * Gets the singleton action in application.
	 * 
	 * @return the singleton action
	 */
    public static Action getAction() {
        return action;
    }

    /**
	 * Instantiates a new draw rectangle action.
	 */
    private DrawRectangleAction() {
        super(id);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
    }
}

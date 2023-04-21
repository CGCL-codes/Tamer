package org.opensourcephysics.display;

import java.awt.event.MouseEvent;

/**
 * InteractiveMouseHandler defines how an object receives notification that
 * a mouse action has occured in an InteractivePanel.
 * @author Wolfgang Christian
 * @author Francisco Equembre
 * @version 1.0
 */
public interface InteractiveMouseHandler {

    public void handleMouseAction(InteractivePanel panel, MouseEvent evt);
}

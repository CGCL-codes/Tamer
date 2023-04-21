package antsnest.level.defaults;

import java.awt.event.*;
import javax.swing.*;

/**
 * Starts a drag when a mouse button is pressed over a component
 * @author Chris Clohosy
 */
public class DragMouseAdapter extends MouseMotionAdapter {

    /**
	 * Called when a mouse is dragged
	 * @param e the event that caused the call
	 */
    public void mouseDragged(MouseEvent e) {
        JComponent c = (JComponent) e.getSource();
        TransferHandler handler = c.getTransferHandler();
        handler.exportAsDrag(c, e, TransferHandler.MOVE);
    }
}

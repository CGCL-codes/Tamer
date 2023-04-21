package org.redfire.screen;

import java.awt.Cursor;
import java.awt.event.MouseEvent;
import javax.swing.event.MouseInputAdapter;

public class VirtualScreenYMouseListener extends MouseInputAdapter {

    private double y = 0;

    public void mouseEntered(MouseEvent e) {
        ScreenShare.instance.t.setCursor(Cursor.getPredefinedCursor(Cursor.N_RESIZE_CURSOR));
    }

    public void mouseExited(MouseEvent e) {
        ScreenShare.instance.t.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
    }

    public void mousePressed(MouseEvent e) {
        VirtualScreen.instance.showWarning = false;
        this.y = e.getY();
    }

    public void mouseReleased(MouseEvent e) {
        VirtualScreen.instance.showWarning = true;
    }

    public void mouseDragged(MouseEvent e) {
        double newY = e.getY();
        int delta = Long.valueOf(Math.round(this.y - newY)).intValue();
        int newYPosition = VirtualScreenBean.vScreenSpinnerY - delta;
        int newHeight = VirtualScreenBean.vScreenSpinnerHeight + delta;
        if (newYPosition >= 0 && newHeight >= 0) {
            VirtualScreen.instance.doUpdateBounds = false;
            ScreenShare.instance.jVScreenYSpin.setValue(newYPosition);
            ScreenShare.instance.jVScreenHeightSpin.setValue(newHeight);
            VirtualScreen.instance.doUpdateBounds = true;
            VirtualScreen.instance.updateVScreenBounds();
        }
    }
}

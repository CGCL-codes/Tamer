package jextar.event;

import java.awt.Point;
import java.awt.event.MouseEvent;
import javax.swing.AbstractButton;
import javax.swing.ButtonModel;
import javax.swing.JComponent;
import javax.swing.plaf.basic.BasicButtonListener;
import jextar.JextarButton;
import jextar.JextarButtonModel;
import jextar.interfaces.*;

/**
 * This Class manages all types of mouse events that occur on a JextarButton.
 * Most of the work however is delegated to its superclass BasicButtonListener. This
 * way a JextarButton behaves like any other Swing button. A direct call to one of its
 * methods will never make any sense.  
 */
public class JextarButtonListener extends BasicButtonListener implements ZoomListener {

    transient long lastPressedTimestamp = -1;

    transient boolean shouldDiscardRelease = false;

    public JextarButtonListener(AbstractButton b) {
        super(b);
    }

    /**
     * A View changing method. The zoom value changed, so possibly repaint.
     */
    public void zoomChanged(ZoomEvent e) {
        JextarButton button = (JextarButton) e.getSource();
        if (button.isAutoRepaint()) {
            button.repaint();
        }
    }

    public void mouseMoved(MouseEvent e) {
        JextarButton b = (JextarButton) e.getSource();
        if (!b.getModel().isRollover()) {
            if (b.click(e.getPoint())) {
                super.mouseEntered(e);
            }
        } else {
            if (!b.click(e.getPoint())) {
                super.mouseExited(e);
            }
        }
        super.mouseMoved(e);
        ZoomFunction func = b.getZoomFunction();
        if (func != null) {
            double zoom = func.computeZoom(e.getPoint(), b.getWidth(), b.getHeight());
            ((JextarButton) e.getSource()).setZoom(zoom);
        }
    }

    /**
     * A Model Changing Method
     */
    public void mouseEntered(MouseEvent e) {
        JextarButton b = (JextarButton) e.getSource();
        JextarButtonModel model = (JextarButtonModel) b.getModel();
        if (b.isRolloverEnabled()) {
            model.setRollover(true);
        }
        if (model.isPressed()) {
            model.setArmed(true);
        }
    }

    public void mouseExited(MouseEvent e) {
        JextarButton b = (JextarButton) e.getSource();
        ButtonModel model = b.getModel();
        if (b.isRolloverEnabled()) {
            model.setRollover(false);
        }
        model.setArmed(false);
        ((JextarButton) e.getSource()).setZoom(0);
    }

    public void mousePressed(MouseEvent e) {
        JextarButton b = (JextarButton) e.getSource();
        if (b.click(e.getPoint())) {
            super.mousePressed(e);
        }
    }
}

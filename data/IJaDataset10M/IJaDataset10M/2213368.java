package com.iver.cit.gvsig.fmap.tools.Behavior;

import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.image.BufferedImage;
import com.iver.cit.gvsig.fmap.MapControl;
import com.iver.cit.gvsig.fmap.tools.BehaviorException;
import com.iver.cit.gvsig.fmap.tools.Listeners.ToolListener;

/**
 * <p>When user is working with a tool on a {@link MapControl MapControl} instance, <code>Behavior</code> defines
 *  the basic ways of interacting: selecting a point, a circle, a rectangle, or ...</p>
 *
 * <p>All events generated will be <code>MouseEvent</code>, and will depend on the nature of the
 *  <i>behavior</i>, like the kind of tool for applying the changes.</p>
 * 
 * <p><code>Behavior</code> defines the common and basic functionality for all kinds of interacting ways
 *  with the <code>MapControl</code> object.</p>
 *
 * @see IBehavior
 *
 * @author Luis W. Sevilla
 */
public abstract class Behavior implements IBehavior {

    /**
	 * Reference to the <code>MapControl</code> object that uses.
	 * 
	 * @see #getMapControl()
	 * @see #setMapControl(MapControl)
	 */
    private MapControl mapControl;

    public abstract ToolListener getListener();

    public void paintComponent(Graphics g) {
        BufferedImage img = getMapControl().getImage();
        if (img != null) {
            g.drawImage(img, 0, 0, null);
        }
    }

    public void setMapControl(MapControl mc) {
        mapControl = mc;
    }

    public Cursor getCursor() {
        return getListener().getCursor();
    }

    public MapControl getMapControl() {
        return mapControl;
    }

    public void mouseClicked(MouseEvent e) throws BehaviorException {
    }

    public void mouseEntered(MouseEvent e) throws BehaviorException {
    }

    public void mouseExited(MouseEvent e) throws BehaviorException {
    }

    public void mousePressed(MouseEvent e) throws BehaviorException {
    }

    public void mouseReleased(MouseEvent e) throws BehaviorException {
    }

    public void mouseDragged(MouseEvent e) throws BehaviorException {
    }

    public void mouseMoved(MouseEvent e) throws BehaviorException {
    }

    public void mouseWheelMoved(MouseWheelEvent e) throws BehaviorException {
    }
}

package net.etherstorm.jopenrpg.swing.map;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import net.etherstorm.jopenrpg.swing.ImageLib;

/**
 * 
 * 
 * 
 * $Date: 2003/12/01 00:03:41 $<br>
 * @author tedberg
 * @author $Author: tedberg $
 * @version $Revision: 1.2 $
 * @since Nov 29, 2003
 */
public class MapPanelZoomState extends AbstractMapPanelState {

    /**
	 * 
	 */
    public MapPanelZoomState() {
        super();
    }

    /**
	 * @param p
	 */
    public MapPanelZoomState(JMapPanel p) {
        super(p);
    }

    /**
	 * @param g
	 * @see net.etherstorm.jopenrpg.swing.map.AbstractMapPanelState#paint(java.awt.Graphics)
	 */
    public void paint(Graphics g) {
    }

    /**
	 * 
	 * @see net.etherstorm.jopenrpg.swing.map.AbstractMapPanelState#updateLabel()
	 */
    protected void updateLabel() {
    }

    /**
	 * 
	 */
    public void useZoomInCursor() {
        Toolkit tk = Toolkit.getDefaultToolkit();
        Dimension bestsize = tk.getBestCursorSize(32, 32);
        if (bestsize.width != 0) {
            Cursor myCursor = tk.createCustomCursor(ImageLib.loadImage("icons/ZoomIn32.gif").getImage(), new Point(10, 10), "My Cursor");
            getPanel().setCursor(myCursor);
        }
    }

    /**
	 * 
	 */
    public void useZoomOutCursor() {
        Toolkit tk = Toolkit.getDefaultToolkit();
        Dimension bestsize = tk.getBestCursorSize(32, 32);
        if (bestsize.width != 0) {
            Cursor myCursor = tk.createCustomCursor(ImageLib.loadImage("icons/ZoomOut32.gif").getImage(), new Point(10, 10), "My Cursor");
            getPanel().setCursor(myCursor);
        }
    }

    /**
	 * @param e
	 * @see java.awt.event.KeyListener#keyPressed(java.awt.event.KeyEvent)
	 */
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_CONTROL) useZoomOutCursor();
    }

    /**
	 * @param e
	 * @see java.awt.event.KeyListener#keyReleased(java.awt.event.KeyEvent)
	 */
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_CONTROL) useZoomInCursor();
    }

    /**
	 * @param e
	 * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
	 */
    public void mouseClicked(MouseEvent e) {
        getPanel().requestFocus();
        if (e.getButton() == MouseEvent.BUTTON1) {
            double scale = getPanel().getZoom();
            if ((e.getModifiersEx() & InputEvent.CTRL_DOWN_MASK) == InputEvent.CTRL_DOWN_MASK) {
                scale -= 10;
            } else {
                scale += 10;
            }
            getPanel().setVisible(false);
            Point p = getScaledPoint(e.getPoint());
            getPanel().setZoom(scale);
            getPanel().centerOnPoint(p.x, p.y);
            getPanel().setVisible(true);
        }
        getPanel().requestFocus();
    }
}

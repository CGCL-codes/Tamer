package com.jvito.plot;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

/**
 * A Listener for zooming in and out of a <code>PlotPanel</code>. If the SHIFT-Key is pressed then you can zoom in
 * and out by pressing the first-mouse-button and dragging the mouse up and down.
 * 
 * @author Daniel Hakenjos
 * @version $Id: ZoomingListener.java,v 1.2 2008/04/12 14:28:13 djhacker Exp $
 */
public class ZoomingListener implements MouseListener, MouseMotionListener {

    private boolean mousepressed;

    private PlotPanel panel;

    private int starty;

    /**
	 * Creates a new ZoomingListener.
	 * 
	 * @param panel
	 */
    public ZoomingListener(PlotPanel panel) {
        mousepressed = false;
        this.panel = panel;
        this.starty = 0;
    }

    /**
	 * Not used.
	 * 
	 * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
	 */
    public void mouseClicked(MouseEvent arg0) {
    }

    /**
	 * Not used.
	 * 
	 * @see java.awt.event.MouseListener#mouseEntered(java.awt.event.MouseEvent)
	 */
    public void mouseEntered(MouseEvent arg0) {
    }

    /**
	 * Not used.
	 * 
	 * @see java.awt.event.MouseListener#mouseExited(java.awt.event.MouseEvent)
	 */
    public void mouseExited(MouseEvent arg0) {
    }

    /**
	 * Recognizes wether the left mouse-button is pressed.
	 * 
	 * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
	 */
    public void mousePressed(MouseEvent event) {
        if (!event.isShiftDown()) {
            return;
        }
        if (event.getButton() == MouseEvent.BUTTON1) {
            mousepressed = true;
            starty = event.getY();
        }
    }

    /**
	 * Recognizes wether the left mouse-button was released.
	 * 
	 * @see java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent)
	 */
    public void mouseReleased(MouseEvent event) {
        if (event.getButton() == MouseEvent.BUTTON1) {
            mousepressed = false;
            starty = 0;
        }
    }

    /**
	 * @see java.awt.event.MouseMotionListener#mouseDragged(java.awt.event.MouseEvent)
	 */
    public void mouseDragged(MouseEvent event) {
        if ((event.isShiftDown()) && (mousepressed)) {
            int dy = event.getY() - starty;
            dy = -dy;
            Dimension dim = panel.getDimension();
            float factor = (dy) / ((float) dim.getHeight());
            int dx = (int) (dim.width * factor);
            panel.setDimension(new Dimension(dim.width + dx, dim.height + dy));
            Point p = panel.getOrigin();
            panel.setOrigin(new Point(p.x - dx / 2, p.y - dy / 2));
            panel.repaint();
            starty = event.getY();
        }
    }

    /**
	 * Not used.
	 * 
	 * @see java.awt.event.MouseMotionListener#mouseMoved(java.awt.event.MouseEvent)
	 */
    public void mouseMoved(MouseEvent event) {
    }
}

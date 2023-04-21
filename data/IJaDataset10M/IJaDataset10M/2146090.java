package ircam.jmax.editors.table;

import java.awt.*;
import java.awt.event.*;
import ircam.jmax.toolkit.*;
import javax.swing.*;

public class PositionDrawer extends TableInteractionModule implements XORPainter {

    public PositionDrawer(TablePositionListener theListener) {
        super();
        itsXORHandler = new XORHandler(this);
        itsListener = theListener;
    }

    public void activate(int x, int y) {
        interactionBeginAt(x, y);
        itsListener.startFollowPosition();
        active = true;
    }

    public void mousePressed(MouseEvent e) {
        if (active) {
            itsXORHandler.end();
            itsListener.stopFollowPosition(e.getX(), e.getY(), e);
            active = false;
        }
    }

    public void mouseMoved(MouseEvent e) {
        if (active) itsXORHandler.moveTo(e.getX(), e.getY()); else activate(e.getX(), e.getY());
    }

    public void mouseEntered(MouseEvent e) {
        activate(e.getX(), e.getY());
    }

    public void mouseExited(MouseEvent e) {
        if (active) {
            itsXORHandler.moveTo(e.getX(), e.getY());
            itsXORHandler.end();
            active = false;
        }
    }

    /**
   * used to set the starting point of the interaction.
   */
    public void interactionBeginAt(int x, int y) {
        movingPoint.setLocation(x, y);
        itsXORHandler.beginAt(x, y);
    }

    public void mouseDragged(MouseEvent e) {
    }

    public void mouseReleased(MouseEvent e) {
    }

    protected void unBindFromProducer() {
        super.unBindFromProducer();
        active = false;
    }

    /**
   * from the XORPainter interface
   */
    public void XORErase() {
        XORDraw(0, 0);
    }

    /**
   * from the XOR painter interface. The actual drawing routine
   */
    public void XORDraw(int dx, int dy) {
        Graphics g = gc.getGraphicDestination().getGraphics();
        Rectangle rect = gc.getGraphicDestination().getBounds();
        g.setColor(((TableDisplay) gc.getGraphicDestination()).getRenderer().getBackColor());
        g.setXORMode(((TableDisplay) gc.getGraphicDestination()).getRenderer().getForeColor());
        movingPoint.setLocation(movingPoint.x + dx, movingPoint.y + dy);
        g.drawLine(movingPoint.x, rect.y, movingPoint.x, rect.height);
        g.drawLine(0, movingPoint.y, rect.width, movingPoint.y);
        g.setColor(Color.black);
        g.setPaintMode();
        g.dispose();
    }

    TablePositionListener itsListener;

    Point startSelection = new Point();

    Point movingPoint = new Point();

    XORHandler itsXORHandler;

    boolean active = false;
}

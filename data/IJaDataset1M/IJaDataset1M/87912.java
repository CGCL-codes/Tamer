package org.proteomecommons.MSExpedite.app;

import java.awt.Container;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;

/**
 *
 * @author takis
 */
public abstract class SessionButton extends JButton implements IDnDButton, ISessionButton, MouseListener, MouseMotionListener {

    protected ISessionHandler sessionHandler = null;

    protected ActionListener actionListener = null;

    Point startPoint = new Point(0, 0);

    Point currentPoint = new Point(0, 0);

    boolean removeComponent = false;

    public SessionButton(ISessionHandler handler, String text, String icon) {
        super();
        if (handler != null) set(handler);
        if (icon != null) setIcon(new ImageIcon(this.getClass().getClassLoader().getResource(icon)));
        setToolTipText(text);
    }

    public SessionButton(ISessionHandler handler, String text, ImageIcon icon, ActionListener al) {
        set(handler);
        actionListener = al;
        setIcon(icon);
        setToolTipText(text);
        addActionListener(actionListener);
    }

    public void finilize() {
        if (actionListener != null) removeActionListener(actionListener);
    }

    public void set(ISessionHandler handler) {
        this.sessionHandler = handler;
        if (actionListener != null) removeActionListener(actionListener);
        actionListener = getActionListener();
        addActionListener(actionListener);
    }

    public ISessionHandler getSessionHandler() {
        return sessionHandler;
    }

    public String getShortDescription() {
        return this.getToolTipText();
    }

    public void enableDnD(boolean b) {
        removeMouseListener(this);
        removeMouseMotionListener(this);
        if (b) {
            addMouseListener(this);
            addMouseListener(this);
        }
    }

    private void decrementComponentPos() {
        Container container = getParent();
        if (!(container instanceof JToolBar)) return;
        JToolBar toolbar = (JToolBar) container;
        int count = toolbar.getComponentCount();
        int currentIndex = toolbar.getComponentIndex(this);
        if (currentIndex == 0) return;
        toolbar.add(this, currentIndex - 1);
    }

    public String toString() {
        return getShortDescription();
    }

    public void mouseClicked(MouseEvent e) {
    }

    public void mousePressed(MouseEvent e) {
        startPoint = MouseInfo.getPointerInfo().getLocation();
        currentPoint = new Point(0, 0);
        removeComponent = false;
    }

    public void mouseReleased(MouseEvent e) {
        if (removeComponent) {
            Container container = getParent();
            if (!(container instanceof JToolBar)) return;
            JToolBar toolbar = (JToolBar) container;
            toolbar.remove(this);
            container = SwingUtilities.getWindowAncestor(toolbar);
            container.setVisible(true);
        }
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

    public void mouseDragged(MouseEvent e) {
        removeComponent = false;
        currentPoint = MouseInfo.getPointerInfo().getLocation();
        Container container = getParent();
        if (!(container instanceof JToolBar)) return;
        JToolBar toolbar = (JToolBar) container;
        Point locOnScreen = toolbar.getLocationOnScreen();
        if (locOnScreen.y < e.getPoint().y) {
            removeComponent = true;
            return;
        }
        Point pDiff = diff(startPoint, currentPoint);
        Rectangle bounds = getBounds();
        int x = (bounds.x + bounds.width) / 2;
        if (pDiff.x > 0 && ((pDiff.x + x) > (bounds.x + bounds.width))) {
            incrementComponentPos();
        } else if (pDiff.x < 0 && ((pDiff.x + x) < (bounds.x))) {
            decrementComponentPos();
        }
        int y = 0;
        Point p = new Point(x, y);
    }

    public void mouseMoved(MouseEvent e) {
    }

    private Point diff(Point p1, Point p2) {
        int x = p2.x - p1.x;
        int y = p2.y - p1.y;
        return new Point(x, y);
    }

    private void incrementComponentPos() {
        Container container = getParent();
        if (!(container instanceof JToolBar)) return;
        JToolBar toolbar = (JToolBar) container;
        int count = toolbar.getComponentCount();
        int currentIndex = toolbar.getComponentIndex(this);
        if (currentIndex == count - 1) return;
        toolbar.add(this, currentIndex + 1);
    }

    protected abstract ActionListener getActionListener();
}

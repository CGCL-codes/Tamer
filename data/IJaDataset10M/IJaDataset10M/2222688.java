package org.SCAraide.gcd.artifacts.util;

import java.awt.Container;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import javax.swing.JComponent;
import org.SCAraide.gcd.Diagram;

/**
 *
 * @author JayDee
 */
public class MovableArtefact implements MouseListener, MouseMotionListener {

    protected JComponent component;

    protected int lastX;

    protected int lastY;

    public MovableArtefact() {
        component = null;
        lastX = 0;
        lastY = 0;
    }

    public void linkToComponent(JComponent component) {
        this.component = component;
        component.addMouseListener(this);
        component.addMouseMotionListener(this);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
        lastX = e.getX();
        lastY = e.getY();
        if (e.getButton() == MouseEvent.BUTTON1) {
            if (component instanceof SelectableArtefact) {
                Container parent = component.getParent();
                while ((parent != null) && !(parent instanceof Diagram)) {
                    parent = parent.getParent();
                }
                if ((parent != null) && (parent instanceof Diagram)) {
                    Diagram diag = (Diagram) parent;
                    diag.setSelectedArtifact((SelectableArtefact) component);
                }
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        updateLocation(e);
    }

    @Override
    public void mouseMoved(MouseEvent e) {
    }

    private void updateLocation(MouseEvent e) {
        component.setLocation(component.getX() + (e.getX() - lastX), component.getY() + (e.getY() - lastY));
    }
}

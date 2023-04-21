package net.sourceforge.jruntimedesigner.events;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class DetachableMouseListener implements MouseListener {

    private IListenerActivator activator;

    private MouseListener listener;

    public DetachableMouseListener(MouseListener listener, IListenerActivator activator) {
        if (listener == null) {
            throw new IllegalArgumentException("Mandatory parameter listener is null!");
        }
        if (activator == null) {
            throw new IllegalArgumentException("Mandatory parameter activator is null!");
        }
        this.activator = activator;
        this.listener = listener;
    }

    public void mouseClicked(MouseEvent e) {
        if (activator.isListenerEnabled()) {
            listener.mouseClicked(e);
        }
    }

    public void mouseEntered(MouseEvent e) {
        if (activator.isListenerEnabled()) {
            listener.mouseEntered(e);
        }
    }

    public void mouseExited(MouseEvent e) {
        if (activator.isListenerEnabled()) {
            listener.mouseExited(e);
        }
    }

    public void mousePressed(MouseEvent e) {
        if (activator.isListenerEnabled()) {
            listener.mousePressed(e);
        }
    }

    public void mouseReleased(MouseEvent e) {
        if (activator.isListenerEnabled()) {
            listener.mouseReleased(e);
        }
    }
}

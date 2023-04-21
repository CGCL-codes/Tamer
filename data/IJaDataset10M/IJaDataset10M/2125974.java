package net.sf.xpontus.core.controller.handlers;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JPopupMenu;

/**
 * A mouse listener to display a popup menu
 * @author Yves Zoundi
 */
public class PopupListener extends MouseAdapter {

    private JPopupMenu popup;

    /**
     * Create a new instance of PopupListener
     */
    public PopupListener() {
    }

    /**
     * Create a new instance of PopupListener
     * @param popup A popup menu
     */
    public PopupListener(JPopupMenu popup) {
        setPopup(popup);
    }

    /**
     *
     * @param e
     */
    public void mouseReleased(MouseEvent e) {
        if (e.isPopupTrigger() || (e.getButton() == 3)) {
            popup.show(e.getComponent(), e.getX(), e.getY());
        }
    }

    /**
     * Set the popup menu associated to this listener
     * @param popup The popup menu associated to this listener
     */
    public void setPopup(JPopupMenu popup) {
        this.popup = popup;
    }

    /**
     * Return the popup menu associated to this listener
     * @return The popup menu associated to this listener
     */
    public JPopupMenu getPopup() {
        return popup;
    }
}

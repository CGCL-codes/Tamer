package org.jfree.ui;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.AbstractButton;

/**
 * Enables a button to have a simple floating effect. The border of the button is only visible,
 * when the mouse pointer is floating over the button.
 *
 * @author Thomas Morgner
 */
public final class FloatingButtonEnabler extends MouseAdapter {

    /** A single instance. */
    private static FloatingButtonEnabler singleton;

    /**
     * Default constructor.
     */
    private FloatingButtonEnabler() {
    }

    /**
     * Returns a default instance of this enabler.
     *
     * @return a shared instance of this class.
     */
    public static FloatingButtonEnabler getInstance() {
        if (singleton == null) {
            singleton = new FloatingButtonEnabler();
        }
        return singleton;
    }

    /**
     * Adds a button to this enabler.
     *
     * @param button  the button.
     */
    public void addButton(final AbstractButton button) {
        button.addMouseListener(this);
        button.setBorderPainted(false);
    }

    /**
     * Removes a button from the enabler.
     *
     * @param button  the button.
     */
    public void removeButton(final AbstractButton button) {
        button.addMouseListener(this);
        button.setBorderPainted(true);
    }

    /**
     * Triggers the drawing of the border when the mouse entered the button area.
     *
     * @param e  the mouse event.
     */
    public void mouseEntered(final MouseEvent e) {
        if (e.getSource() instanceof AbstractButton) {
            final AbstractButton button = (AbstractButton) e.getSource();
            if (button.isEnabled()) {
                button.setBorderPainted(true);
            }
        }
    }

    /**
     * Disables the drawing of the border when the mouse leaves the button area.
     *
     * @param e  the mouse event.
     */
    public void mouseExited(final MouseEvent e) {
        if (e.getSource() instanceof AbstractButton) {
            final AbstractButton button = (AbstractButton) e.getSource();
            button.setBorderPainted(false);
            if (button.getParent() != null) {
                button.getParent().repaint();
            }
        }
    }
}

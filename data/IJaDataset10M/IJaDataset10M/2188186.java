package org.dishevelled.identify;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JPopupMenu;

/**
 * Context menu listener.
 *
 * @author  Michael Heuer
 * @version $Revision$ $Date$
 */
public final class ContextMenuListener extends MouseAdapter {

    /** Context menu. */
    private final JPopupMenu contextMenu;

    /**
     * Create a new context menu listener for the specified context menu.
     *
     * @param contextMenu context menu, must not be null
     */
    public ContextMenuListener(final JPopupMenu contextMenu) {
        if (contextMenu == null) {
            throw new IllegalArgumentException("contextMenu must not be null");
        }
        this.contextMenu = contextMenu;
    }

    /** {@inheritDoc} */
    public void mousePressed(final MouseEvent event) {
        if (event.isPopupTrigger()) {
            showContextMenu(event);
        }
    }

    /** {@inheritDoc} */
    public void mouseReleased(final MouseEvent event) {
        if (event.isPopupTrigger()) {
            showContextMenu(event);
        }
    }

    /**
     * Show context menu.
     *
     * @param event mouse event
     */
    private void showContextMenu(final MouseEvent event) {
        contextMenu.show(event.getComponent(), event.getX(), event.getY());
    }
}

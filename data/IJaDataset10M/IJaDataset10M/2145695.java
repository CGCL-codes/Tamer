package de.psychomatic.mp3db.gui.modules.listener;

import java.awt.event.MouseEvent;
import javax.swing.JPopupMenu;
import javax.swing.JTree;

/**
 * Popuplistener for trees
 * @author Kykal
 */
public class TreePopUpListener extends PopUpListener {

    /**
     * Creates the listenener with specific popup
     * @param menu Popup menu
     */
    public TreePopUpListener(final JPopupMenu menu) {
        super(menu);
    }

    /**
     * Selects the treenode before popup is shown
     * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
     */
    @Override
    public void mouseClicked(final MouseEvent arg0) {
        if (arg0.getButton() == MouseEvent.BUTTON3) {
            if (arg0.getSource() instanceof JTree) {
                final JTree tree = (JTree) arg0.getSource();
                final int row = tree.getRowForLocation(arg0.getX(), arg0.getY());
                if (row > -1) {
                    tree.setSelectionRow(row);
                }
            }
            super.mouseClicked(arg0);
        }
    }
}

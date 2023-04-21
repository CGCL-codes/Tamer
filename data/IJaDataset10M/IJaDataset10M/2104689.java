package gpsxml.gui.popup;

import gpsxml.MenuTreeNode;
import gpsxml.action.MenuAccessControlListener;
import gpsxml.action.DeleteTreeNode;
import gpsxml.action.EditTreeNodeListener;
import gpsxml.gui.*;
import java.awt.event.ActionListener;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

/**
 *
 * @author PLAYER, Keith Ralph
 */
public class LinkPopupBuilder {

    private JMenu groupToUserMenuItem = new JMenu("Assign permission to a user group");

    private JMenuItem editPopupMenuItem = new JMenuItem("Edit");

    private JMenuItem deleteMenuItem = new JMenuItem("Delete");

    private JCheckBoxMenuItem accessControlMenuItem = new JCheckBoxMenuItem("Protected");

    /** Creates a new instance of LinkPopupBuilder */
    public LinkPopupBuilder() {
    }

    public JPopupMenu getLinkPopupMenu(MenuTreePanel menuTreePanel) {
        addEditListener(new EditTreeNodeListener(menuTreePanel, MenuTreeNode.LINKITEM));
        deleteMenuItem.addActionListener(new DeleteTreeNode(menuTreePanel));
        getAccessControlMenuItem().addActionListener(new MenuAccessControlListener(menuTreePanel));
        JPopupMenu popupMenu = new JPopupMenu();
        PopupUtil popupUtil = new PopupUtil();
        popupMenu.add(popupUtil.getAddMenuPopup(menuTreePanel));
        popupMenu.add(editPopupMenuItem);
        popupMenu.add(deleteMenuItem);
        popupMenu.addSeparator();
        popupMenu.add(getAccessControlMenuItem());
        popupMenu.add(groupToUserMenuItem);
        return popupMenu;
    }

    public void addEditListener(ActionListener listener) {
        editPopupMenuItem.addActionListener(listener);
    }

    public JCheckBoxMenuItem getAccessControlMenuItem() {
        return accessControlMenuItem;
    }

    public JMenu getGroupToUserMenuItem() {
        return groupToUserMenuItem;
    }
}

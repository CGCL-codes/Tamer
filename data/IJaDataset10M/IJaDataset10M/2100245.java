package org.juicyapps.gui.addressbook;

import java.awt.Component;
import java.io.File;
import javax.swing.ImageIcon;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import org.juicyapps.persistence.pojo.JuicyContact;
import org.juicyapps.persistence.pojo.JuicyFolder;

public class ContactTreeCellRenderer extends DefaultTreeCellRenderer {

    /**
	 * 
	 */
    private static final long serialVersionUID = 2232668107941760544L;

    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
        super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
        Object item = node.getUserObject();
        if (item.getClass().equals(JuicyFolder.class)) {
            JuicyFolder folder = (JuicyFolder) item;
            if (expanded) {
                setIcon(new ImageIcon("img" + File.separator + "folderOpenedSmall.png"));
            } else {
                setIcon(new ImageIcon("img" + File.separator + "folderClosedSmall.png"));
            }
            setText(folder.getJfName());
        } else if (item.getClass().equals(JuicyContact.class)) {
            JuicyContact contact = (JuicyContact) item;
            setIcon(new ImageIcon("img" + File.separator + "accmgmtSmall.png"));
            setText(contact.getJcFname() + " " + contact.getJcLname());
        } else {
            if (expanded) {
                setIcon(new ImageIcon("img" + File.separator + "folderOpenedSmall.png"));
            } else {
                setIcon(new ImageIcon("img" + File.separator + "folderClosedSmall.png"));
            }
            setText("Contacts");
        }
        return this;
    }
}

package net.suberic.pooka.gui;

import javax.swing.JComponent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import java.awt.event.ActionEvent;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.mail.Store;
import javax.mail.Folder;
import javax.mail.MessagingException;
import java.util.*;
import net.suberic.pooka.Pooka;
import javax.mail.FolderNotFoundException;
import javax.swing.JOptionPane;
import net.suberic.pooka.FolderInfo;
import javax.mail.event.*;

public class ChooserFolderNode extends MailTreeNode {

    protected Folder folder = null;

    protected boolean hasLoaded = false;

    protected String folderProperty;

    /**
     * creates a tree node that points to a folder
     *
     * @param newFolder	the folder for this node
     * @param newFolderPropery the property which defines the folder
     * @param newParent the parent component
     */
    public ChooserFolderNode(Folder newFolder, String newFolderProperty, JComponent newParent) {
        super(newFolder, newParent);
        folder = newFolder;
        folderProperty = newFolderProperty;
    }

    /**
     * a Folder is a leaf if it cannot contain sub folders
     */
    public boolean isLeaf() {
        try {
            if ((getFolder().getType() & Folder.HOLDS_FOLDERS) == 0) {
                return true;
            }
        } catch (MessagingException me) {
        }
        return false;
    }

    /**
     * return the number of children for this folder node. The first
     * time this method is called we load up all of the folders
     * under the store's defaultFolder
     */
    public int getChildCount() {
        if (!hasLoaded) {
            loadChildren();
        }
        return super.getChildCount();
    }

    /**
     * returns the children of this folder node.  The first
     * time this method is called we load up all of the folders
     * under the store's defaultFolder
     */
    public java.util.Enumeration children() {
        if (!hasLoaded) {
            loadChildren();
        }
        return super.children();
    }

    /**
     * This loads (or reloads) the children of the FolderNode from
     * the list of Children on the FolderInfo.
     */
    public void loadChildren() {
        if (isLeaf()) {
            hasLoaded = true;
            return;
        }
        Folder folder = getFolder();
        try {
            Folder[] folderList = folder.list();
            for (int i = 0; i < folderList.length; i++) {
                ChooserFolderNode node = new ChooserFolderNode(folderList[i], getFolderProperty() + "." + folderList[i].getName(), getParentContainer());
                insert(node, i);
            }
        } catch (MessagingException me) {
            if (me instanceof FolderNotFoundException) {
                JOptionPane.showInternalMessageDialog(((FolderPanel) getParentContainer()).getMainPanel().getMessagePanel(), Pooka.getProperty("error.FolderWindow.folderNotFound", "Could not find folder.") + "\n" + me.getMessage());
            } else {
                me.printStackTrace();
            }
        }
        hasLoaded = true;
    }

    /**
     * returns the folder for this node
     */
    public Folder getFolder() {
        return folder;
    }

    public String getFolderProperty() {
        return folderProperty;
    }

    public String getFolderName() {
        return getFolder().getName();
    }

    /**
     * override toString() since we only want to display a folder's
     * name, and not the full path of the folder
     */
    public String toString() {
        return folder.getName();
    }
}

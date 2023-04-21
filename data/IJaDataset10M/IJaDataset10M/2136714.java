package net.suberic.pooka.gui;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.mail.Store;
import javax.mail.Folder;
import javax.mail.MessagingException;

/**
 * Node which represents a Folder in the javax.mail apis. 
 *
 * @version 1.4, 97/12/12
 * @author Christopher Cotton
 */
public class FolderTreeNode extends DefaultMutableTreeNode {

    protected Folder folder = null;

    protected boolean hasLoaded = false;

    /**
     * creates a tree node that points to the particular Store.
     *
     * @param what	the store for this node
     */
    public FolderTreeNode(Folder what) {
        super(what);
        folder = what;
    }

    /**
     * a Folder is a leaf if it cannot contain sub folders
     */
    public boolean isLeaf() {
        try {
            if ((folder.getType() & Folder.HOLDS_FOLDERS) == 0) return true;
        } catch (MessagingException me) {
        }
        return false;
    }

    /**
     * returns the folder for this node
     */
    public Folder getFolder() {
        return folder;
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

    protected void loadChildren() {
        if (isLeaf()) {
            hasLoaded = true;
            return;
        }
        try {
            Folder[] sub = folder.list();
            int num = sub.length;
            for (int i = 0; i < num; i++) {
                FolderTreeNode node = new FolderTreeNode(sub[i]);
                insert(node, i);
            }
        } catch (MessagingException me) {
            me.printStackTrace();
        }
    }

    /**
     * override toString() since we only want to display a folder's
     * name, and not the full path of the folder
     */
    public String toString() {
        return folder.getName();
    }
}

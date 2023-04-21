package com.tensegrity.palobrowser.bookmarks;

import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.palo.api.Connection;
import org.palo.api.NamedEntity;
import com.tensegrity.palobrowser.PalobrowserPlugin;
import com.tensegrity.palobrowser.connectionspec.ConnectionSpec;
import com.tensegrity.palobrowser.connectionspec.ConnectionSpecManager;
import com.tensegrity.palobrowser.folder.Folder;
import com.tensegrity.palobrowser.tree.TreeNode;

/**
 * <code>BookmarkTreeLabelProvider</code>
 * This class provides the labels for the <code>BookmarkExplorer</code> tree.
 * Its labels are <code>Folder</code> names and <code>Bookmark</code>
 * names.
 * 
 * @author Philipp Bouillon
 * @version $ID$
 */
public class BookmarkTreeLabelProvider extends LabelProvider {

    /**
	 * Returns the <code>Image</code> for the specified object. If the object
	 * is a <code>Folder</code>, the folder icon is returned, if it is a 
	 * <code>Bookmark</code>, the view icon is returned.
	 * Otherwise (which should not happen), the element icon is returned.
	 */
    public Image getImage(Object obj) {
        String imageKey = ISharedImages.IMG_OBJ_ELEMENT;
        if (obj instanceof TreeNode) {
            TreeNode node = (TreeNode) obj;
            if (node.getUserObject() instanceof Folder) {
                imageKey = ISharedImages.IMG_OBJ_FOLDER;
            } else if (node.getUserObject() instanceof Bookmark) {
                return PalobrowserPlugin.getDefault().getImageRegistry().get("icons/view.png");
            }
        }
        return PlatformUI.getWorkbench().getSharedImages().getImage(imageKey);
    }

    /**
	 * Returns the name of the <code>Folder</code> or the
	 * <code>Bookmark</code>. Other named entities do not appear in this tree.
	 */
    public String getText(Object obj) {
        if (obj instanceof TreeNode) {
            TreeNode node = (TreeNode) obj;
            if (node.getUserObject() instanceof Folder) {
                if (node.getParent() != null && node.getParent().getParent() == null) {
                    Connection connection = ((Folder) node.getUserObject()).getConnection();
                    ConnectionSpecManager specManager = ConnectionSpecManager.getInstance();
                    ConnectionSpec spec = specManager.getActive(connection);
                    if (spec != null) {
                        return spec.getName();
                    }
                }
            }
            if (node.getUserObject() instanceof NamedEntity) {
                return ((NamedEntity) node.getUserObject()).getName();
            }
        }
        return obj.toString();
    }
}

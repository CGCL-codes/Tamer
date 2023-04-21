package org.columba.addressbook.gui.tree;

import javax.swing.JTree;
import javax.swing.ListSelectionModel;
import org.columba.addressbook.folder.AbstractFolder;
import org.columba.addressbook.folder.AddressbookTreeNode;
import org.columba.addressbook.gui.frame.AddressbookFrameController;
import org.columba.addressbook.gui.tree.util.AddressbookTreeCellRenderer;

/**
 * Custom JTree using an appropriate model and renderer.
 *
 * @author fdietz
 */
public class TreeView extends JTree {

    private AddressbookTreeNode root;

    private AddressbookTreeModel model;

    protected AddressbookFrameController frameController;

    public TreeView(AddressbookFrameController frameController) {
        this.frameController = frameController;
        model = AddressbookTreeModel.getInstance();
        setModel(model);
        setShowsRootHandles(true);
        setRootVisible(false);
        expandRow(0);
        getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        setCellRenderer(new AddressbookTreeCellRenderer(true));
    }

    public AbstractFolder getRootFolder() {
        return (AbstractFolder) model.getRoot();
    }

    public void removeAll() {
        root.removeAllChildren();
    }

    /**
     * @return FrameController
     */
    public AddressbookFrameController getFrameController() {
        return frameController;
    }
}

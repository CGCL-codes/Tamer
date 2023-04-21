package org.photovault.swingui.folderpane;

import java.util.EventObject;
import javax.swing.*;
import javax.swing.event.CellEditorListener;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeCellEditor;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import javax.swing.event.TreeModelListener;
import javax.swing.event.TreeModelEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.*;
import org.photovault.swingui.PhotoFolderSelectionDlg;
import org.photovault.folder.PhotoFolder;

/**
 * Implements the folder pane for PhotoInfoEditor. This In practice, this control 
 * displays the folder tree and folder into which selected photos belong.
 */
public class FolderTreePane extends JPanel implements TreeModelListener {

    /**
     * Constructs a new FolderPreePane
     * @param ctrl FolderController whose state is displayed
     */
    public FolderTreePane(FolderController ctrl) {
        super();
        createUI();
        this.ctrl = ctrl;
    }

    /**
     * Create the UI for this control. Called by constructor.
     */
    void createUI() {
        GridBagLayout layout = new GridBagLayout();
        setLayout(layout);
        folderTree = new JTree();
        folderTree.setRootVisible(true);
        folderTree.setShowsRootHandles(false);
        folderTree.setEditable(true);
        folderTree.setCellEditor(new FolderNodeEditor(this));
        folderTree.setCellRenderer(new FolderNodeEditor(this));
        JScrollPane scrollPane = new JScrollPane(folderTree);
        scrollPane.setPreferredSize(new Dimension(300, 300));
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.gridheight = GridBagConstraints.REMAINDER;
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.weightx = 1;
        c.weighty = 1;
        c.fill = GridBagConstraints.BOTH;
        c.anchor = GridBagConstraints.SOUTHEAST;
        add(scrollPane);
        layout.setConstraints(scrollPane, c);
    }

    /**
     * 
     * @param model 
     */
    public void setFolderTreeModel(TreeModel model) {
        TreeModel oldModel = folderTree.getModel();
        if (oldModel != null) {
            oldModel.removeTreeModelListener(this);
        }
        folderTree.setModel(model);
        model.addTreeModelListener(this);
    }

    public void treeNodesInserted(TreeModelEvent e) {
        final TreePath parentPath = e.getTreePath();
        SwingUtilities.invokeLater(new java.lang.Runnable() {

            public void run() {
                folderTree.expandPath(parentPath);
            }
        });
    }

    public void treeNodesRemoved(TreeModelEvent e) {
    }

    public void treeNodesChanged(TreeModelEvent e) {
    }

    public void treeStructureChanged(TreeModelEvent e) {
    }

    /** returns the selected folder or null if none selected
     */
    PhotoFolder getSelectedFolder() {
        PhotoFolder selected = null;
        TreePath path = folderTree.getSelectionPath();
        if (path != null) {
            FolderNode treeNode = (FolderNode) path.getLastPathComponent();
            selected = treeNode.getFolder();
        }
        return selected;
    }

    /**
     * Add all photos in the model to currently selected folder.
     */
    protected void addAllToSelectedFolder() {
        PhotoFolder selected = getSelectedFolder();
        if (selected != null) {
            ctrl.addAllToFolder(selected);
        }
    }

    /**
     * Remove all photos in current model from the selected folder.
     */
    protected void removeAllFromSelectedFolder() {
        PhotoFolder selected = getSelectedFolder();
        if (selected != null) {
            ctrl.removeAllFromFolder(selected);
        }
    }

    public void expandPath(TreePath path) {
        folderTree.expandPath(path);
    }

    JTree folderTree = null;

    FolderController ctrl = null;

    private static final String ADD_ALL_TO_FOLDER_CMD = "addAllToFolder";

    private static final String ADD_ALL_TO_THIS_FOLDER_CMD = "addAllToThisFolder";

    private static final String REMOVE_ALL_FROM_THIS_FOLDER_CMD = "removeAllFromThisFolder";
}

package com.ravermeister.sync.core;

import java.awt.Component;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Enumeration;
import javax.swing.JLabel;
import javax.swing.JTree;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeExpansionListener;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.filechooser.FileSystemView;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import sun.awt.shell.ShellFolder;

public class FileSystemTree extends JTree {

    private FileSystemView fileSystemView = FileSystemView.getFileSystemView();

    private ArrayList<File> filesToIgnore = new ArrayList<File>();

    private DefaultTreeModel treeModel;

    private DefaultMutableTreeNode rootNode;

    public FileSystemTree() {
        super();
        File[] roots = fileSystemView.getRoots();
        File file = roots[0];
        rootNode = new DefaultMutableTreeNode(file);
        rootNode = prepareTreeNode(rootNode);
    }

    public void init(DefaultTreeModel model) {
        treeModel = model;
        init();
    }

    public DefaultTreeModel init() {
        if (treeModel == null) treeModel = new DefaultTreeModel(rootNode);
        setModel(treeModel);
        setRootVisible(true);
        setCellRenderer(new DefaultTreeCellRenderer() {

            public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
                JLabel label = (JLabel) super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
                DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
                File f = (File) node.getUserObject();
                label.setText(fileSystemView.getSystemDisplayName(f));
                label.setIcon(fileSystemView.getSystemIcon(f));
                label.setToolTipText(fileSystemView.getSystemTypeDescription(f));
                return label;
            }
        });
        addTreeExpansionListener(new TreeExpansionListener() {

            public void treeCollapsed(TreeExpansionEvent event) {
                TreePath path = event.getPath();
                TreeNode collapsedNode = (TreeNode) path.getLastPathComponent();
                TreePath selectedPath = getSelectionPath();
                TreeNode selectedNode = null;
                if (selectedPath != null) {
                    selectedNode = (TreeNode) selectedPath.getLastPathComponent();
                    if (isSelectedNodeInCollapsedNode(selectedNode.getParent(), collapsedNode)) {
                        setSelectionPath(path);
                    }
                }
            }

            private boolean isSelectedNodeInCollapsedNode(TreeNode selectedNodeParent, TreeNode collapsedNode) {
                if (selectedNodeParent == null) {
                    return false;
                } else if (collapsedNode.equals(selectedNodeParent)) {
                    return true;
                } else {
                    return isSelectedNodeInCollapsedNode(selectedNodeParent.getParent(), collapsedNode);
                }
            }

            public void treeExpanded(TreeExpansionEvent event) {
                DefaultMutableTreeNode node = (DefaultMutableTreeNode) event.getPath().getLastPathComponent();
                prepareChildTreeNodes(node);
            }
        });
        prepareChildTreeNodes(rootNode);
        expandRow(0);
        return treeModel;
    }

    public DefaultMutableTreeNode prepareTreeNode(DefaultMutableTreeNode node) {
        File f = (File) node.getUserObject();
        File[] files = f.listFiles();
        if (files == null) return node;
        for (int i = 0; i < files.length; i++) {
            File file = files[i];
            try {
                ShellFolder sf = ShellFolder.getShellFolder(file);
                if (sf.isLink()) continue;
            } catch (FileNotFoundException fnfe) {
                fnfe.printStackTrace();
            }
            if (file.isDirectory() && !file.isHidden()) node.add(new DefaultMutableTreeNode(file));
        }
        return node;
    }

    private void prepareChildTreeNodes(DefaultMutableTreeNode node) {
        Enumeration<DefaultMutableTreeNode> e = node.children();
        while (e.hasMoreElements()) {
            DefaultMutableTreeNode child = e.nextElement();
            prepareTreeNode(child);
        }
    }

    public DefaultTreeModel getModel() {
        return treeModel;
    }
}

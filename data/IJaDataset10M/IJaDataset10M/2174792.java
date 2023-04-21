package uk.ac.mmu.manmetassembly.gui;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import javax.swing.*;
import javax.swing.tree.*;
import javax.swing.event.*;

/**
 * for showing the file system structure in a JTree
 * @mmu:author Warren Li
 */
public class FileTreeSystem extends BorderedPanel {

    /**
     * The FileTree to display File System Structure
     */
    protected FileTree tree;

    /**
     * Reference to the EditorView
     */
    private EditorView parentJPanel;

    /**
     * The treeModel to display tree list
     */
    protected DefaultTreeModel treeModel;

    protected FileTreeSystem(EditorView parent) {
        super("File System");
        parentJPanel = parent;
        initGUI();
    }

    private void initGUI() {
        setLayout(new BorderLayout());
        File files[] = File.listRoots();
        JTree.DynamicUtilTreeNode root = new JTree.DynamicUtilTreeNode("Computer", files);
        for (Enumeration kids = root.children(); kids.hasMoreElements(); ) {
            JTree.DynamicUtilTreeNode node = (JTree.DynamicUtilTreeNode) kids.nextElement();
            node.setAllowsChildren(true);
        }
        tree = new FileTree(root);
        tree.setShowsRootHandles(true);
        tree.setRootVisible(true);
        tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        tree.addTreeExpansionListener(new FileTreeExpansionListener());
        MouseListener ml = new MouseAdapter() {

            public void mousePressed(MouseEvent e) {
                int selRow = tree.getRowForLocation(e.getX(), e.getY());
                if (selRow != -1) {
                    TreePath path = tree.getPathForLocation(e.getX(), e.getY());
                    JTree.DynamicUtilTreeNode node = (JTree.DynamicUtilTreeNode) path.getLastPathComponent();
                    File file = (File) node.getUserObject();
                    if (e.getClickCount() == 2) {
                        if (correntFormat(file) != false) {
                            parentJPanel.openTab(file);
                        }
                    }
                }
            }
        };
        tree.addMouseListener(ml);
        treeModel = (DefaultTreeModel) tree.getModel();
        JScrollPane pane = new JScrollPane();
        pane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED & JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        pane.getViewport().add(tree);
        this.add(pane, BorderLayout.CENTER);
    }

    class FileTree extends JTree {

        public FileTree(JTree.DynamicUtilTreeNode root) {
            super(root);
        }

        public String convertValueToText(Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
            if (value != null) {
                if (value instanceof JTree.DynamicUtilTreeNode) {
                    Object obj = ((JTree.DynamicUtilTreeNode) value).getUserObject();
                    if (obj instanceof File) {
                        File theFile = (File) obj;
                        if (theFile.getParent() != null) {
                            return theFile.getName();
                        }
                    }
                    return obj.toString();
                } else {
                    return value.toString();
                }
            }
            return "";
        }
    }

    class FileTreeExpansionListener implements TreeExpansionListener {

        private final DirFilter filter = new DirFilter();

        public void treeCollapsed(TreeExpansionEvent event) {
        }

        public void treeExpanded(TreeExpansionEvent event) {
            TreePath path = event.getPath();
            JTree.DynamicUtilTreeNode node = (JTree.DynamicUtilTreeNode) path.getLastPathComponent();
            if (node.getChildCount() == 0) {
                File file = (File) node.getUserObject();
                File[] temp = file.listFiles(filter);
                if ((temp != null) && (temp.length > 0)) {
                    java.util.Arrays.sort(temp);
                    JTree.DynamicUtilTreeNode.createChildren(node, temp);
                    for (Enumeration kids = node.children(); kids.hasMoreElements(); ) {
                        JTree.DynamicUtilTreeNode subnode = (JTree.DynamicUtilTreeNode) kids.nextElement();
                        File subfile = (File) subnode.getUserObject();
                        if (subfile.isDirectory()) {
                            subnode.setAllowsChildren(true);
                        } else {
                            subnode.setAllowsChildren(false);
                        }
                    }
                    treeModel.reload(node);
                }
            }
        }
    }

    private String[] extensions = { "asm", "txt" };

    public boolean correntFormat(File file) {
        String ext = getExt(file);
        for (int j = 0; j < extensions.length; j++) {
            if (extensions[j].equalsIgnoreCase(ext)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Method to get file extensions
	 */
    public String getExt(File f) {
        if (f != null) {
            String filename = f.getName();
            int i = filename.lastIndexOf('.');
            if (i > 0 && i < filename.length() - 1) {
                return filename.substring(i + 1);
            }
            ;
        }
        return null;
    }

    class MyListSelectionListener implements ListSelectionListener {

        public void valueChanged(ListSelectionEvent event) {
        }
    }

    class DirFilter implements FileFilter {

        private String[] extensions = { "asm", "txt" };

        public boolean accept(File file) {
            String ext = getExtension(file);
            for (int j = 0; j < extensions.length; j++) {
                if (extensions[j].equalsIgnoreCase(ext)) {
                    return true;
                }
            }
            return (file.isDirectory() && !file.isHidden()) ? true : false;
        }
    }

    public String getExtension(File f) {
        if (f != null) {
            String filename = f.getName();
            int i = filename.lastIndexOf('.');
            if (i > 0 && i < filename.length() - 1) {
                return filename.substring(i + 1);
            }
            ;
        }
        return null;
    }
}

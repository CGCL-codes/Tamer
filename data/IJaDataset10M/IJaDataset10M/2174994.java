package edu.whitman.halfway.jigs.gui.desque;

import java.awt.*;
import java.io.*;
import java.util.*;
import java.util.List;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.tree.*;
import edu.whitman.halfway.util.*;

public class FileViewerPanel extends JPanel implements TreeSelectionListener {

    private DefaultTreeModel treeModel;

    private JTree tree;

    public FileViewerPanel() {
        treeModel = new DefaultTreeModel(new DefaultMutableTreeNode());
        tree = new JTree(treeModel);
        JScrollPane treeView = new JScrollPane(tree);
        buildTree(null, false, null);
        this.setLayout(new BorderLayout());
        this.add(treeView, BorderLayout.CENTER);
        tree.addTreeSelectionListener(this);
        tree.setCellRenderer(new FileTreeCellRenderer());
    }

    public File getSelectedFile() {
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
        return ((FileTreeNode) node.getUserObject()).getFile();
    }

    public void setSelectedFile(File f) {
        buildTree(f, false, null);
    }

    public JTree getTree() {
        return tree;
    }

    public void reloadTree() {
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
        FileTreeNode treeNode = (FileTreeNode) node.getUserObject();
        buildTree(treeNode.getFile(), false, null);
    }

    private void buildTree(File f, boolean showHidden, FileFilter fFilter) {
        File parentFile, oldParent;
        if ((f == null) || !f.exists()) f = new File(System.getProperty("user.dir"));
        if (!f.isDirectory()) parentFile = f.getParentFile(); else parentFile = f;
        DefaultMutableTreeNode parentNode = new DefaultMutableTreeNode(new FileTreeNode(parentFile));
        DefaultMutableTreeNode newNode;
        DefaultMutableTreeNode childNode = new DefaultMutableTreeNode();
        File[] children;
        oldParent = parentFile;
        do {
            parentNode = new DefaultMutableTreeNode(new FileTreeNode(parentFile));
            children = parentFile.listFiles(fFilter);
            Arrays.sort(children, new DirectoriesFirstComparator());
            for (int i = 0; i < children.length; i++) {
                newNode = new DefaultMutableTreeNode(new FileTreeNode(children[i]));
                if (newNode.getUserObject().equals(childNode.getUserObject())) {
                    parentNode.add(childNode);
                } else {
                    if (!children[i].isHidden() || showHidden) parentNode.add(newNode);
                }
            }
            childNode = parentNode;
            oldParent = parentFile;
        } while ((parentFile = oldParent.getParentFile()) != null);
        treeModel.setRoot(parentNode);
        List pathList = FileUtil.splitFile(f);
        Iterator iter = pathList.iterator();
        String s;
        while (iter.hasNext()) {
            s = (String) iter.next();
            for (int i = 0; i < parentNode.getChildCount(); i++) {
                newNode = (DefaultMutableTreeNode) parentNode.getChildAt(i);
                if (((FileTreeNode) newNode.getUserObject()).getName().equals(s)) {
                    parentNode = newNode;
                    continue;
                }
            }
        }
        TreePath newPath = new TreePath(parentNode.getPath());
        tree.expandPath(newPath);
        tree.makeVisible(newPath);
        tree.setSelectionPath(newPath);
        tree.validate();
    }

    private void addChildren(DefaultMutableTreeNode node, boolean showHidden, FileFilter fFilter) {
        FileTreeNode treeNode = (FileTreeNode) node.getUserObject();
        File treeFile = treeNode.getFile();
        if (!treeFile.exists() || !treeFile.isDirectory()) return;
        File[] children = treeFile.listFiles(fFilter);
        Arrays.sort(children);
        for (int i = 0; i < children.length; i++) {
            if (!children[i].isHidden() || showHidden) node.add(new DefaultMutableTreeNode(new FileTreeNode(children[i])));
        }
        treeModel.nodeChanged(node);
        tree.expandPath(new TreePath(node.getPath()));
    }

    public void valueChanged(TreeSelectionEvent e) {
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
        if (node == null) return;
        if (node.isLeaf()) {
            addChildren(node, false, null);
        }
    }

    public static void main(String[] args) {
        JFrame f = new JFrame();
        Container c = f.getContentPane();
        c.setLayout(new BorderLayout());
        c.add(new FileViewerPanel(), BorderLayout.CENTER);
        f.pack();
        f.show();
    }

    class FileTreeCellRenderer extends DefaultTreeCellRenderer {

        public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
            super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
            if (leaf && isDirectory(value)) setIcon(super.getClosedIcon());
            return this;
        }

        protected boolean isDirectory(Object value) {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
            FileTreeNode treeNode = (FileTreeNode) (node.getUserObject());
            if (treeNode.getFile().isDirectory()) return true;
            return false;
        }
    }

    class CaselessComparator implements Serializable, Comparator {

        public int compare(Object o1, Object o2) {
            return ((File) o1).getName().compareToIgnoreCase(((File) o2).getName());
        }

        public boolean equals(Object obj) {
            return (obj instanceof CaselessComparator);
        }
    }
}

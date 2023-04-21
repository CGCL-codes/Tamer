package de.sistemich.mafrasi.stopmotion.gui.modules.importmodule;

import java.io.File;
import java.io.FileFilter;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Enumeration;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;

public class ImportTreeNode implements MutableTreeNode {

    private File f_;

    private MutableTreeNode parent_;

    private ImportTreeNode this_ = this;

    private static final FileFilter filter = new FileFilter() {

        @Override
        public boolean accept(File pathname) {
            return pathname.isDirectory();
        }
    };

    private static final Comparator<File> comparator = new Comparator<File>() {

        @Override
        public int compare(File o1, File o2) {
            return o1.getName().compareTo(o2.getName());
        }
    };

    public ImportTreeNode(File f, MutableTreeNode parent) {
        f_ = f;
        parent_ = parent;
    }

    public File getFile() {
        return f_;
    }

    @Override
    public TreeNode getChildAt(int childIndex) {
        File[] children = listChildren();
        return new ImportTreeNode(children[childIndex], this);
    }

    @Override
    public int getChildCount() {
        File[] children = listChildren();
        return children.length;
    }

    @Override
    public TreeNode getParent() {
        return parent_;
    }

    @Override
    public int getIndex(TreeNode node) {
        if (node instanceof ImportTreeNode) {
            ImportTreeNode importNode = (ImportTreeNode) node;
            File[] children = listChildren();
            for (int i = 0; i < children.length; i++) {
                File child = children[i];
                if (child.getAbsolutePath().equals(importNode.getFile())) {
                    return i;
                }
            }
            return -1;
        } else {
            return -1;
        }
    }

    @Override
    public boolean getAllowsChildren() {
        return true;
    }

    @Override
    public boolean isLeaf() {
        return false;
    }

    @Override
    public Enumeration<ImportTreeNode> children() {
        return new Enumeration<ImportTreeNode>() {

            private File[] children = listChildren();

            private int i_ = 0;

            @Override
            public boolean hasMoreElements() {
                return children.length > i_;
            }

            @Override
            public ImportTreeNode nextElement() {
                return new ImportTreeNode(children[i_++], this_);
            }
        };
    }

    public File[] listChildren() {
        File[] children = f_.listFiles(filter);
        if (children == null) {
            return new File[0];
        } else {
            Arrays.sort(children, comparator);
            return children;
        }
    }

    @Override
    public void insert(MutableTreeNode child, int index) {
    }

    @Override
    public void remove(int index) {
    }

    @Override
    public void remove(MutableTreeNode node) {
    }

    @Override
    public void setUserObject(Object object) {
    }

    @Override
    public void removeFromParent() {
        parent_.remove(this);
    }

    @Override
    public void setParent(MutableTreeNode newParent) {
    }

    @Override
    public String toString() {
        return f_.getName() + System.getProperty("file.separator");
    }
}

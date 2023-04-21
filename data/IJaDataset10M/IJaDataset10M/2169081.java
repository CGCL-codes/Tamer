package jsmbget.gui;

import javax.swing.tree.TreeNode;
import java.io.File;
import java.io.IOException;
import java.util.Enumeration;
import jsmbget.util.TreeSorter;

public class JSmbGetNode implements TreeNode {

    private File file;

    JSmbGetNode[] children;

    TreeSorter treeSorter;

    public JSmbGetNode(File file) {
        this.file = file;
        treeSorter = new TreeSorter();
    }

    public Enumeration children() {
        getChildren();
        return new JSmbGetEnumeration(children);
    }

    public boolean getAllowsChildren() {
        return !isLeaf();
    }

    public TreeNode getChildAt(int idx) {
        getChildren();
        return children[idx];
    }

    private void getChildren() {
        File[] childrenFiles = file.listFiles();
        childrenFiles = (File[]) treeSorter.sort(childrenFiles);
        if (childrenFiles != null) {
            children = new JSmbGetNode[childrenFiles.length];
        } else {
            children = new JSmbGetNode[0];
        }
        for (int i = 0; i < children.length; i++) {
            children[i] = new JSmbGetNode(childrenFiles[i]);
        }
    }

    public int getChildCount() {
        getChildren();
        return children.length;
    }

    public File getFile() {
        return file;
    }

    public int getIndex(TreeNode treeNode) {
        getChildren();
        for (int i = 0; i < children.length; i++) {
            if (children[i].equals(treeNode)) {
                return i;
            }
        }
        return -1;
    }

    public TreeNode getParent() {
        JSmbGetNode parent = new JSmbGetNode(file.getParentFile());
        return parent;
    }

    public boolean isLeaf() {
        return file.isFile();
    }

    public String toString() {
        return file.toString();
    }
}

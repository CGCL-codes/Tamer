package net.cattaka.swing.fileview;

import java.io.File;
import java.util.Arrays;
import java.util.Comparator;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeModel;

public class FileViewTree extends JTree {

    private static final long serialVersionUID = 1L;

    private DefaultTreeModel treeModel;

    public class FileTreeNode extends JTree.DynamicUtilTreeNode {

        private static final long serialVersionUID = 1L;

        public FileTreeNode(File value, File[] children) {
            super(value, children);
            if (value == null) {
                throw new NullPointerException();
            }
        }

        public File getFile() {
            return (File) getUserObject();
        }

        public FileTreeNode[] getChildren() {
            FileTreeNode[] result = new FileTreeNode[this.getChildCount()];
            for (int i = 0; i < result.length; i++) {
                result[i] = (FileTreeNode) this.getChildAt(i);
            }
            return result;
        }

        public void reflesh() {
            while (this.getChildCount() > 0) {
                treeModel.removeNodeFromParent((FileTreeNode) this.getChildAt(0));
            }
            if (this.childValue != null && this.childValue instanceof File[]) {
                File[] files = ((File) getUserObject()).listFiles();
                Arrays.sort(files, new FileComparator());
                for (File file : files) {
                    if (file.isDirectory()) {
                        treeModel.insertNodeInto(new FileTreeNode(file, file.listFiles()), this, this.getChildCount());
                    } else {
                        treeModel.insertNodeInto(new FileTreeNode(file, null), this, this.getChildCount());
                    }
                }
            }
        }

        @Override
        protected void loadChildren() {
            this.loadedChildren = true;
            if (this.childValue != null && this.childValue instanceof File[]) {
                File[] files = (File[]) this.childValue;
                Arrays.sort(files, new FileComparator());
                for (File file : files) {
                    if (file.isDirectory()) {
                        this.add(new FileTreeNode(file, file.listFiles()));
                    } else {
                        this.add(new FileTreeNode(file, null));
                    }
                }
            }
        }

        @Override
        public String toString() {
            return ((File) getUserObject()).getName();
        }
    }

    public FileViewTree() {
        super();
        JPopupMenuForFileView popup = new JPopupMenuForFileView(true);
        popup.install(this);
    }

    public FileViewTree(File rootDirectory) {
        this();
        setRootDirectory(rootDirectory);
    }

    public void setRootDirectory(File rootDirectory) {
        if (rootDirectory != null) {
            FileTreeNode rootNode;
            if (rootDirectory.isDirectory()) {
                rootNode = new FileTreeNode(rootDirectory, rootDirectory.listFiles());
            } else {
                rootNode = new FileTreeNode(rootDirectory, null);
            }
            treeModel = new DefaultTreeModel(rootNode);
            this.setModel(treeModel);
        } else {
            treeModel = null;
            this.setModel(treeModel);
        }
    }

    /** 使用先でオーバーライドして使う */
    public void doOpen(File file) {
    }
}

class FileComparator implements Comparator<File> {

    public int compare(File o1, File o2) {
        if (o1 == o2) {
            return 0;
        }
        if (o1.isDirectory()) {
            if (!o2.isDirectory()) {
                return -1;
            }
        } else {
            if (o2.isDirectory()) {
                return 1;
            }
        }
        return o1.getName().compareTo(o2.getName());
    }
}

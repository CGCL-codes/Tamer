package tjacobs.tree;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.DefaultTreeCellEditor;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import tjacobs.io.IOUtils;
import tjacobs.ui.util.WindowUtilities;

/**
 * View the file system in a JTree
 * @author tjacobs
 *
 */
public class FileSystemTreeModel implements TreeModel {

    File mRoot;

    ArrayList<TreeModelListener> mListeners = new ArrayList<TreeModelListener>(1);

    public FileSystemTreeModel() {
        File f = new File(".");
        f = f.getAbsoluteFile();
        File par = null;
        do {
            par = f.getParentFile();
            if (par != null) {
                f = par;
            } else {
                setRootFile(f);
                return;
            }
        } while (mRoot == null);
    }

    public FileSystemTreeModel(File root) {
        setRootFile(root);
    }

    private void setRootFile(File root) {
        mRoot = root;
    }

    public void addTreeModelListener(TreeModelListener l) {
        mListeners.add(l);
    }

    public Object getChild(Object parent, int index) {
        try {
            File fi = (File) parent;
            File[] subs = fi.listFiles();
            if (index < subs.length) {
                return subs[index];
            }
        } catch (ClassCastException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public int getChildCount(Object parent) {
        try {
            File fi = (File) parent;
            return fi.listFiles().length;
        } catch (ClassCastException ex) {
            ex.printStackTrace();
        }
        return 0;
    }

    public int getIndexOfChild(Object parent, Object child) {
        try {
            File fi = (File) parent;
            File[] subs = fi.listFiles();
            for (int i = 0; i < subs.length; i++) {
                File f = subs[i];
                if (f.equals(child)) return i;
            }
        } catch (ClassCastException ex) {
            ex.printStackTrace();
        }
        return -1;
    }

    public Object getRoot() {
        return mRoot;
    }

    public boolean isLeaf(Object node) {
        File f = (File) node;
        return (!f.isDirectory());
    }

    public void removeTreeModelListener(TreeModelListener l) {
        mListeners.remove(l);
    }

    public void valueForPathChanged(TreePath path, Object newValue) {
        TreeModelEvent ev = new TreeModelEvent(newValue, path.getPath());
        File f = (File) path.getPathComponent(0);
        boolean worked = f.renameTo(new File(f.getParent(), (String) newValue));
        if (!worked) {
            JOptionPane.showMessageDialog(null, "Rename Operation Failed");
        } else {
            for (TreeModelListener l : mListeners) {
                l.treeNodesChanged(ev);
            }
        }
    }

    public static class NodeRunner extends MouseAdapter {

        private JTree mTree;

        private OutputStream mOut;

        private OutputStream mErr;

        private String[] mExecFileTypes;

        public NodeRunner(JTree tree) {
            this(tree, System.out, System.err);
        }

        public NodeRunner(JTree tree, OutputStream out, OutputStream err) {
            mTree = tree;
            mTree.addMouseListener(this);
            mOut = out;
            mErr = err;
        }

        public void mouseClicked(MouseEvent me) {
            if (me.getClickCount() == 2) {
                File f = (File) mTree.getSelectionPath().getLastPathComponent();
                if (f.isDirectory()) return;
                if (mExecFileTypes != null) {
                    boolean canExec = false;
                    String extension = IOUtils.getFileExtension(f);
                    for (String s : mExecFileTypes) {
                        if (s.equals(extension)) {
                            canExec = true;
                            break;
                        }
                    }
                    if (!canExec) return;
                }
                try {
                    String cmd = f.getAbsolutePath();
                    final Process p = Runtime.getRuntime().exec(cmd);
                    Runnable r = new Runnable() {

                        public void run() {
                            IOUtils.pipe(p.getInputStream(), mOut);
                        }
                    };
                    Thread t = new Thread(r);
                    t.start();
                    r = new Runnable() {

                        public void run() {
                            IOUtils.pipe(p.getErrorStream(), mErr);
                        }
                    };
                    t = new Thread(r);
                    t.start();
                } catch (IOException iox) {
                    iox.printStackTrace();
                }
            }
        }
    }

    public static void addExecExecution(JTree tree) {
        new NodeRunner(tree);
    }

    public static void addExecExecution(JTree tree, String[] execExtensions) {
        NodeRunner nr = new NodeRunner(tree);
        nr.mExecFileTypes = execExtensions;
    }

    public static void addExecExecution(JTree tree, OutputStream out, OutputStream err) {
        new NodeRunner(tree, out, err);
    }

    public static void addExecExecution(JTree tree, OutputStream out, OutputStream err, String[] execExtensions) {
        NodeRunner nr = new NodeRunner(tree, out, err);
        nr.mExecFileTypes = execExtensions;
    }

    public static void main(String[] args) {
        FileSystemTreeModel tm = new FileSystemTreeModel();
        JTree tree = new JTree(tm);
        FileSystemTreeModel.addExecExecution(tree, new String[] { "exe", "bat", "jar" });
        DefaultTreeCellRenderer rend = new FileSystemTreeRenderer();
        tree.setCellRenderer(rend);
        tree.setCellEditor(new DefaultTreeCellEditor(tree, rend, new FileSystemTreeEditor(tree, rend)));
        tree.setEditable(true);
        JScrollPane sp = new JScrollPane(tree);
        WindowUtilities.visualize(sp);
    }
}

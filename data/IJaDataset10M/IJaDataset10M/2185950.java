package installer;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.StringTokenizer;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.tree.TreePath;

/**
 * The class DirectoryChooser
 *
 * @copyright (c) 1999 by nailware, all rights reserved.
 * @author Michael Nagler
 * @version 1.0
 */
public class DirectoryChooser extends JPanel implements TreeSelectionListener, ItemListener {

    static final boolean DEBUG = true;

    static File[] roots = File.listRoots();

    static String FS = System.getProperty("file.separator");

    JComboBox cbxRoots = null;

    JScrollPane pane = new JScrollPane();

    DirectoryTree[] tree;

    int treeID;

    String title = "Choose Directory";

    File directory = new File(System.getProperty("user.home"));

    boolean result;

    Dimension size;

    /**
     * This constructor creates a new DirectoryChooser with the
     * selected default System.getProperty("user.home").
     */
    public DirectoryChooser(Dimension dim) {
        size = dim;
        JPanel pnl = new JPanel();
        pnl.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
        pnl.setLayout(new BorderLayout(4, 4));
        tree = new DirectoryTree[roots.length];
        for (int i = 0; i < tree.length; i++) {
            if (System.getProperty("user.home").startsWith(roots[i].getAbsolutePath())) {
                tree[i] = new DirectoryTree(roots[i]);
                tree[i].addTreeSelectionListener(this);
                treeID = i;
            } else {
                tree[i] = null;
            }
        }
        if (roots.length > 1) {
            cbxRoots = new JComboBox(roots);
            cbxRoots.addItemListener(this);
            pnl.add(cbxRoots, BorderLayout.NORTH);
        }
        pane.setPreferredSize(new Dimension(size.width - 50, size.height - 50));
        pane.setViewportView(tree[treeID]);
        add(pane);
        setDirectory(directory);
    }

    public Dimension getPreferredSize() {
        return size;
    }

    public void setDirectory(File directory) {
        if (directory.isDirectory()) {
            if (cbxRoots != null) {
                cbxRoots.setSelectedItem(DirectoryTree.getRoot(directory));
            }
            tree[treeID].select(directory);
        }
        this.directory = directory;
    }

    public File getDirectory() {
        return directory;
    }

    public void valueChanged(TreeSelectionEvent evt) {
        updateDirectory();
        newDirectory(directory.getAbsolutePath());
    }

    public void newDirectory(String path) {
    }

    protected void updateDirectory() {
        StringBuffer result = new StringBuffer();
        if (cbxRoots != null) {
            result.append(cbxRoots.getSelectedItem());
        } else {
            result.append(roots[0].getAbsolutePath());
        }
        if (tree[treeID].getSelectionPath() != null) {
            String path = tree[treeID].getSelectionPath().toString();
            path = path.substring(1, path.length() - 1);
            StringTokenizer tokens = new StringTokenizer(path, ",");
            while (tokens.hasMoreTokens()) {
                result.append(tokens.nextToken().trim() + FS);
            }
        }
        directory = new File(result.toString());
    }

    public void itemStateChanged(ItemEvent e) {
        if ((e.getSource() == cbxRoots) && (e.getStateChange() == ItemEvent.SELECTED) && (treeID != cbxRoots.getSelectedIndex())) {
            if (roots[cbxRoots.getSelectedIndex()].canWrite()) {
                pane.getViewport().remove(tree[treeID]);
                treeID = cbxRoots.getSelectedIndex();
                if (tree[treeID] == null) {
                    tree[treeID] = new DirectoryTree(roots[treeID]);
                    tree[treeID].addTreeSelectionListener(this);
                }
                pane.setViewportView(tree[treeID]);
                pane.revalidate();
            } else {
                JOptionPane.showMessageDialog(null, "Auf Laufwerk " + roots[cbxRoots.getSelectedIndex()] + " kann nicht geschrieben werden!", "Fehler", JOptionPane.ERROR_MESSAGE);
                cbxRoots.setSelectedIndex(treeID);
            }
        }
        updateDirectory();
    }
}

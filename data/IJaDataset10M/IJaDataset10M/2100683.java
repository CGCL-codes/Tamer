package com.sohsoft.widgets;

import java.awt.BorderLayout;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.Collections;
import java.util.Vector;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTree;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.event.TreeWillExpandListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.ExpandVetoException;
import javax.swing.tree.TreePath;

public class JDirBrowser extends BasicWidget implements TreeSelectionListener, TreeWillExpandListener {

    private File dir;

    private Vector<String> dirNames;

    private JScrollPane sp;

    private JTree tree;

    private JFileBrowser fb;

    private JSplitPane spp;

    private int layer;

    private static int MAX_LAYER = 2;

    public JDirBrowser() {
        this(".");
    }

    public JDirBrowser(String dirName) {
        super();
        layer = 0;
        setDir(dirName);
    }

    @Override
    protected void createGUI() {
        setLayout(new BorderLayout());
        tree = new JTree();
        sp = new JScrollPane(tree);
        fb = new JFileBrowser();
        spp = new JSplitPane(JSplitPane.VERTICAL_SPLIT, sp, fb);
        spp.setDividerLocation(100);
        add(spp);
    }

    @Override
    protected void attatchListener() {
        tree.addTreeSelectionListener(this);
    }

    @Override
    protected void updateView() {
    }

    public void setDir(String dirName) {
        dir = new File(dirName);
        DefaultMutableTreeNode nodes = addNodes(null, dir);
        setTreeRoot(nodes);
    }

    private void setTreeRoot(DefaultMutableTreeNode root) {
        tree = new JTree(root);
        sp.setViewportView(tree);
        attatchListener();
        updateView();
    }

    private class NodeFile {

        String path;

        public NodeFile(String path) {
            this.path = path;
        }

        @Override
        public String toString() {
            File f = new File(path);
            return f.getName();
        }

        public String getAbsPath() {
            return path;
        }
    }

    private DefaultMutableTreeNode addOneLayerNodes(DefaultMutableTreeNode curTop, File dir) {
        this.dir = dir;
        if (dir == null) return null;
        if (curTop == null) {
            curTop = new DefaultMutableTreeNode(new NodeFile(dir.getAbsolutePath()));
        }
        if (dir != null) {
            File[] files = dir.listFiles();
            if (files != null) {
                String fn;
                for (int i = 0; i < files.length; i++) {
                    if (files[i].isDirectory()) {
                        fn = files[i].getAbsolutePath();
                        if (fn != null && fn.length() > 0) {
                            curTop.add(new DefaultMutableTreeNode(new NodeFile(fn)));
                        }
                    }
                }
            }
        }
        return curTop;
    }

    private DefaultMutableTreeNode addNodes(DefaultMutableTreeNode curTop, File dir) {
        String curPath = dir.getPath();
        DefaultMutableTreeNode curDir = new DefaultMutableTreeNode(new NodeFile(curPath));
        if (curTop != null) {
            curTop.add(curDir);
        }
        Vector ol = new Vector();
        String[] tmp = dir.list();
        if (tmp == null) return null;
        for (int i = 0; i < tmp.length; i++) ol.addElement(tmp[i]);
        Collections.sort(ol, String.CASE_INSENSITIVE_ORDER);
        File f;
        Vector files = new Vector();
        for (int i = 0; i < ol.size(); i++) {
            String thisObject = (String) ol.elementAt(i);
            String newPath;
            if (curPath.equals(".")) newPath = thisObject; else newPath = curPath + File.separator + thisObject;
            if ((f = new File(newPath)).isDirectory()) {
                addNodes(curDir, f);
            } else files.addElement(thisObject);
        }
        for (int fnum = 0; fnum < files.size(); fnum++) {
            String tfn = (String) files.elementAt(fnum);
            File tf = new File(tfn);
            if (tf.isDirectory()) {
                curDir.add(new DefaultMutableTreeNode(new NodeFile((String) files.elementAt(fnum))));
            }
        }
        return curDir;
    }

    public static void main(String[] args) {
        demo(new JDirBrowser("c:\\Windows\\Help"));
    }

    public void valueChanged(TreeSelectionEvent e) {
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) e.getPath().getLastPathComponent();
        NodeFile nf = (NodeFile) node.getUserObject();
        fb.setDir(nf.getAbsPath());
    }

    public void treeWillCollapse(TreeExpansionEvent event) throws ExpandVetoException {
        System.out.println("willCollapse");
    }

    public void treeWillExpand(TreeExpansionEvent event) throws ExpandVetoException {
        System.out.println("willExpand");
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getSource() == tree) {
            if (e.getButton() == MouseEvent.BUTTON1) {
                if (e.getClickCount() == 2) {
                    TreePath tp = tree.getSelectionPath();
                    DefaultMutableTreeNode tn = (DefaultMutableTreeNode) tp.getLastPathComponent();
                    NodeFile nf = (NodeFile) tn.getUserObject();
                    File f = new File(nf.getAbsPath());
                    addOneLayerNodes(tn, f);
                    if (!tree.isExpanded(tp)) tree.expandPath(tp); else tree.collapsePath(tp);
                }
            }
        }
    }
}

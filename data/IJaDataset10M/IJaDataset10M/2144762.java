package org.knopflerfish.bundle.desktop.prefs;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.tree.*;
import java.awt.*;
import java.awt.event.*;
import java.util.prefs.*;
import java.util.*;
import java.io.*;
import org.knopflerfish.bundle.desktop.swing.Activator;

/**
 * JTree that display display/edit a Preferences node and its subnodes.
 */
public class JPrefsTree extends JTree {

    JPopupMenu popup;

    Collection menuItemsRO = new HashSet();

    String rootName = "Preferences";

    public JPrefsTree() {
        super();
        setModel(new PrefsTreeModel(null, rootName));
        setCellRenderer(new PrefsTreeRenderer());
        ToolTipManager.sharedInstance().registerComponent(this);
        addTreeExpansionListener(new TreeExpansionListener() {

            public void treeCollapsed(TreeExpansionEvent event) {
            }

            public void treeExpanded(TreeExpansionEvent event) {
                onExpand(event.getPath());
            }
        });
        popup = new JPopupMenu();
        JMenuItem mi;
        mi = new JMenuItem("Search...") {

            {
                addActionListener(new ActionListener() {

                    public void actionPerformed(ActionEvent ev) {
                        doSearch();
                    }
                });
            }
        };
        popup.add(mi);
        mi = new JMenuItem("New node...") {

            {
                addActionListener(new ActionListener() {

                    public void actionPerformed(ActionEvent ev) {
                        doCreate();
                    }
                });
            }
        };
        menuItemsRO.add(mi);
        popup.add(mi);
        mi = new JMenuItem("New key...") {

            {
                addActionListener(new ActionListener() {

                    public void actionPerformed(ActionEvent ev) {
                        doAddKey();
                    }
                });
            }
        };
        menuItemsRO.add(mi);
        popup.add(mi);
        mi = new JMenuItem("Remove this node") {

            {
                addActionListener(new ActionListener() {

                    public void actionPerformed(ActionEvent ev) {
                        doRemove();
                    }
                });
            }
        };
        menuItemsRO.add(mi);
        popup.add(mi);
        popup.add(new JPopupMenu.Separator());
        mi = new JMenuItem("Export this node as XML...") {

            {
                addActionListener(new ActionListener() {

                    public void actionPerformed(ActionEvent ev) {
                        doExport();
                    }
                });
            }
        };
        mi = new JMenuItem("Import XML...") {

            {
                addActionListener(new ActionListener() {

                    public void actionPerformed(ActionEvent ev) {
                        doImport();
                    }
                });
            }
        };
        addMouseListener(new MouseAdapter() {

            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) {
                    return;
                }
                TreePath tp = getPathForLocation(e.getX(), e.getY());
                if (tp != null) {
                    setSelectionPath(tp);
                }
                popup.show(e.getComponent(), e.getX(), e.getY());
            }
        });
    }

    Preferences node;

    public void setPreferences(Preferences node) {
        this.node = node;
        setModel(new PrefsTreeModel(node, rootName));
    }

    JFileChooser fc;

    void doImport() {
        if (fc == null) {
            fc = new JFileChooser();
            fc.setCurrentDirectory(new File(System.getProperty("user.dir", ".")));
        }
        fc.setDialogTitle("Import preferences XML");
        fc.setApproveButtonText("Import");
        int returnVal = fc.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            try {
                InputStream in = new FileInputStream(fc.getSelectedFile());
                Preferences.importPreferences(in);
                setPreferences(node);
                Activator.log.info("imported " + fc.getSelectedFile());
            } catch (Exception e) {
                Activator.log.warn("Failed to load", e);
            }
        }
    }

    public void setEditable(boolean b) {
        super.setEditable(b);
        for (Iterator it = menuItemsRO.iterator(); it.hasNext(); ) {
            JMenuItem mi = (JMenuItem) it.next();
            mi.setEnabled(b);
        }
    }

    void doSearch() {
        String name = JOptionPane.showInputDialog(this, "Search for node or value", "Search", JOptionPane.OK_CANCEL_OPTION);
        if (name == null || "".equals(name)) {
            return;
        }
        searchAndExpand(name, 30);
    }

    public void searchAndExpand(String name, int maxLevel) {
        try {
            Collection paths = new LinkedHashSet();
            TreePath tp = new TreePath(getModel().getRoot());
            search(tp, paths, name.toLowerCase(), true, 0, maxLevel);
            for (Iterator it = paths.iterator(); it.hasNext(); ) {
                TreePath p = (TreePath) it.next();
            }
            TreeUtils.expandPaths(this, paths);
        } catch (Exception e) {
            Activator.log.warn("Failed to search", e);
        }
    }

    void search(TreePath tp, Collection paths, String q, boolean bMatchValues, int level, int maxLevel) throws BackingStoreException {
        if (level > maxLevel) {
            return;
        }
        Object obj = tp.getLastPathComponent();
        if (!(obj instanceof PrefsTreeNode)) {
            return;
        }
        PrefsTreeNode node = (PrefsTreeNode) obj;
        node.assertLoad();
        Preferences p = node.getPrefs();
        if (-1 != p.name().toLowerCase().indexOf(q)) {
            paths.add(tp);
        }
        String[] keys = p.keys();
        for (int i = 0; i < keys.length; i++) {
            if (-1 != keys[i].toLowerCase().indexOf(q)) {
                paths.add(tp);
            }
            if (bMatchValues) {
                String val = p.get(keys[i], null);
                if (val != null && (-1 != val.toLowerCase().indexOf(q))) {
                    paths.add(tp);
                }
            }
        }
        for (Enumeration e = node.children(); e.hasMoreElements(); ) {
            Object child = e.nextElement();
            search(tp.pathByAddingChild(child), paths, q, bMatchValues, level + 1, maxLevel);
        }
    }

    void doCreate() {
        TreePath tp = getSelectionPath();
        if (tp == null) {
            return;
        }
        try {
            PrefsTreeNode node = (PrefsTreeNode) tp.getLastPathComponent();
            String name = JOptionPane.showInputDialog(this, "New node name", "New node...", JOptionPane.YES_NO_OPTION);
            if (name != null && !"".equals(name)) {
                Collection oldPaths = TreeUtils.getExpandedPaths(this);
                Preferences p = node.getPrefs().node(name);
                PrefsTreeNode pNode = new PrefsTreeNode(p);
                oldPaths.add(tp);
                oldPaths.add(tp.pathByAddingChild(pNode));
                node.getPrefs().flush();
                node.rescan();
                setModel(getModel());
                TreeUtils.expandPaths(this, oldPaths);
            }
        } catch (Exception e) {
            Activator.log.warn("Failed to create", e);
        }
    }

    void doRemove() {
        TreePath tp = getSelectionPath();
        if (tp == null) {
            return;
        }
        try {
            PrefsTreeNode node = (PrefsTreeNode) tp.getLastPathComponent();
            Collection oldPaths = TreeUtils.getExpandedPaths(this);
            node.getPrefs().removeNode();
            setModel(getModel());
            TreeUtils.expandPaths(this, oldPaths);
        } catch (Exception e) {
            Activator.log.warn("Failed to create", e);
        }
    }

    protected void doAddKey() {
        TreePath tp = getSelectionPath();
        if (tp == null) {
            return;
        }
        try {
            String name = JOptionPane.showInputDialog(this, "New key name", "New key...", JOptionPane.YES_NO_OPTION);
            if (name != null && !"".equals(name)) {
                PrefsTreeNode node = (PrefsTreeNode) tp.getLastPathComponent();
                node.getPrefs().put(name, node.getPrefs().get(name, ""));
                node.getPrefs().flush();
            }
        } catch (Exception e) {
            Activator.log.warn("Failed to add key", e);
        }
    }

    void doExport() {
        TreePath[] paths = getSelectionPaths();
        if (paths == null || paths.length < 1) {
            return;
        }
        if (fc == null) {
            fc = new JFileChooser();
            fc.setCurrentDirectory(new File(System.getProperty("user.dir", ".")));
        }
        fc.setDialogTitle("Export preferences XML");
        fc.setApproveButtonText("Export");
        int returnVal = fc.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            OutputStream out = null;
            try {
                PrefsTreeNode node = (PrefsTreeNode) paths[0].getLastPathComponent();
                File f = fc.getSelectedFile();
                out = new FileOutputStream(f);
                node.getPrefs().exportSubtree(out);
            } catch (Exception e) {
                Activator.log.warn("Failed to export", e);
            } finally {
                try {
                    out.close();
                } catch (Exception ignored) {
                }
            }
        }
    }

    protected void onExpand(TreePath path) {
        Object node = path.getLastPathComponent();
        if (node instanceof PrefsTreeNode) {
            PrefsTreeNode pNode = (PrefsTreeNode) node;
            try {
                setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                pNode.setHasBeenExpanded(true);
                pNode.rescan();
                TreeModel model = getModel();
                if (model instanceof DefaultTreeModel) {
                    DefaultTreeModel dtModel = (DefaultTreeModel) model;
                    dtModel.nodeStructureChanged(pNode);
                    invalidate();
                    revalidate();
                    getParent().invalidate();
                    repaint();
                }
            } finally {
                setCursor(Cursor.getDefaultCursor());
            }
        }
    }
}

class PrefsTreeRenderer extends DefaultTreeCellRenderer {

    static Icon leafIcon = null;

    static Icon nodeOpenIcon = null;

    static Icon nodeClosedIcon = null;

    Color bgColor = Color.white;

    public PrefsTreeRenderer() {
        super();
        if (nodeOpenIcon == null) {
            nodeOpenIcon = new ImageIcon(PrefsTreeRenderer.class.getResource("/prefs/folder_open16x16.gif"));
            nodeClosedIcon = new ImageIcon(PrefsTreeRenderer.class.getResource("/prefs/folder16x16.gif"));
            leafIcon = new ImageIcon(PrefsTreeRenderer.class.getResource("/prefs/Default.gif"));
        }
    }

    public void setBackground(Color c) {
        super.setBackground(c);
        this.bgColor = c;
    }

    Object node;

    boolean bSel;

    public void paintComponent(Graphics g) {
        Dimension size = getSize();
        if (!bSel) {
            g.setColor(bgColor);
            g.fillRect(0, 0, size.width - 1, size.height - 1);
            g.setColor(Color.black);
        }
        super.paintComponent(g);
    }

    public Component getTreeCellRendererComponent(JTree tree, Object node, boolean bSel, boolean bExpanded, boolean bLeaf, int row, boolean bFocus) {
        this.node = node;
        this.bSel = bSel;
        Component c = (DefaultTreeCellRenderer) super.getTreeCellRendererComponent(tree, node, bSel, bExpanded, bLeaf, row, bFocus);
        setOpaque(false);
        setLeafIcon(null);
        setClosedIcon(null);
        setOpenIcon(null);
        Icon icon;
        if (node instanceof PrefsTreeNode) {
            try {
                PrefsTreeNode pNode = (PrefsTreeNode) node;
                Preferences prefs = pNode.getPrefs();
                String[] names = prefs.childrenNames();
                String[] keys = prefs.keys();
                if (names.length > 0) {
                    setIcon(bExpanded ? nodeOpenIcon : nodeClosedIcon);
                } else {
                    if (keys.length > 0) {
                        setIcon(leafIcon);
                    } else {
                        setIcon(nodeClosedIcon);
                    }
                }
                setToolTipText(prefs.absolutePath());
            } catch (Exception ignored) {
            }
        }
        return this;
    }
}

package edu.xtec.jclic.edit;

import edu.xtec.util.Options;
import javax.swing.*;
import javax.swing.tree.*;
import javax.swing.event.*;
import java.awt.BorderLayout;

/**
 *
 * @author Francesc Busquets (fbusquets@xtec.net)
 * @version 1.0
 */
public class EditorTreePanel extends JPanel implements TreeSelectionListener {

    Options options;

    public Editor currentItem;

    protected java.util.HashMap editPanels;

    protected boolean onlySelect;

    protected Class selectable;

    protected EditorPanel currentPanel;

    protected JTree tree;

    protected JPanel edit;

    protected Editor root;

    /** Creates new EditorTreePanel */
    public EditorTreePanel(Editor root, Options options, boolean onlySelect, Class selectable) {
        super(new BorderLayout());
        this.options = options;
        this.onlySelect = onlySelect;
        this.selectable = selectable;
        this.root = root;
        editPanels = new java.util.HashMap();
        init();
    }

    public JTree getTree() {
        return tree;
    }

    public void setRootEditor(Editor root) {
        this.root = root;
        tree.setModel(root.getTreeModel());
        root.setCurrentTree(tree);
    }

    protected void init() {
        tree = root.createJTree();
        if (onlySelect && selectable != null) {
            tree.setSelectionModel(new DefaultTreeSelectionModel() {

                public void setSelectionPath(TreePath path) {
                    Object o = path.getLastPathComponent();
                    if (o instanceof Editor) {
                        Object u = ((Editor) o).getUserObject();
                        if (selectable.isInstance(u)) {
                            super.setSelectionPath(path);
                            return;
                        }
                    }
                    resetRowSelection();
                }
            });
        }
        tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        tree.addTreeSelectionListener(this);
        if (onlySelect) {
            JScrollPane scroll = new JScrollPane(tree);
            scroll.setPreferredSize(new java.awt.Dimension(250, 300));
            add(scroll, BorderLayout.CENTER);
        } else {
            edit = new JPanel();
            edit.setLayout(new java.awt.BorderLayout());
            edit.setPreferredSize(new java.awt.Dimension(250, 300));
            JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, false, new JScrollPane(tree), edit);
            split.setResizeWeight(1);
            split.setPreferredSize(new java.awt.Dimension(520, 300));
            add(split, BorderLayout.CENTER);
            SwingUtilities.invokeLater(new Runnable() {

                public void run() {
                    tree.setSelectionInterval(0, 0);
                }
            });
        }
    }

    public void valueChanged(TreeSelectionEvent treeSelectionEvent) {
        currentItem = null;
        Editor node = (Editor) tree.getLastSelectedPathComponent();
        if (node == null) return;
        currentItem = node;
        currentItemChanged();
    }

    protected void currentItemChanged() {
        if (edit != null) {
            if (currentPanel != null) {
                currentPanel.removeEditor(true);
            }
            Class pc = currentItem.getEditorPanelClass();
            EditorPanel ep = (EditorPanel) editPanels.get(pc);
            if (ep == null) {
                ep = currentItem.createEditorPanel(options);
                editPanels.put(pc, ep);
            }
            ep.attachEditor(currentItem, true);
            if (currentPanel != ep) {
                if (currentPanel != null) edit.remove(currentPanel);
                edit.add(ep, BorderLayout.CENTER);
                edit.revalidate();
                edit.repaint();
            }
            currentPanel = ep;
        }
    }

    public EditorPanel getCurrentPanel() {
        return currentPanel;
    }
}

package com.frinika.client.gui;

import java.awt.Component;
import java.awt.GridLayout;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Vector;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;
import com.frinika.client.Context;
import com.frinika.client.Context.SongNode;
import com.frinika.ejb.SongEJB;

public class SongTreePanel extends JPanel {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    protected JTree tree;

    DefaultTreeModel treeModel;

    DefaultMutableTreeNode root;

    Context context;

    public SongTreePanel(Context context) {
        super(new GridLayout(1, 0));
        this.context = context;
        Collection<SongEJB> list = context.getRootSongList();
        root = new DefaultMutableTreeNode();
        for (SongEJB song : list) {
            Context.SongNode songNode = context.createSongNode(null, song);
            root.add(songNode);
        }
        treeModel = new DefaultTreeModel(root);
        tree = new JTree(treeModel);
        DefaultTreeCellRenderer renderer = new DefaultTreeCellRenderer() {

            private static final long serialVersionUID = 1L;

            @Override
            public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
                if (value instanceof SongNode) {
                    SongNode sn = (SongNode) value;
                    SongEJB s = sn.getSong();
                    value = "<html> <font color=#0000ff>" + s.getTitle() + "<font> <font color=#000000>" + s.getComposer() + "<font>";
                } else {
                    value = "SONGS";
                }
                super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
                return this;
            }
        };
        tree.setCellRenderer(renderer);
        tree.setEditable(true);
        tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        tree.setShowsRootHandles(true);
        JScrollPane scrollPane = new JScrollPane(tree);
        add(scrollPane);
    }

    public SongEJB selectedSong() {
        TreePath parentPath = tree.getSelectionPath();
        if (parentPath.getLastPathComponent() instanceof SongNode) {
            SongNode parent = (SongNode) parentPath.getLastPathComponent();
            return parent.getSong();
        } else {
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    public void notifySongCreated(SongEJB leaf) {
        if (leaf.getParent() == null) {
            Context.SongNode songNode = context.createSongNode(null, leaf);
            root.add(songNode);
            return;
        }
        SongEJB songH = context.getHistory(leaf);
        System.out.println(" songH " + songH.getId() + "  " + leaf.getId());
        Vector<SongEJB> list = new Vector<SongEJB>();
        while (songH.getParent() != null) {
            songH = songH.getParent();
            System.out.println(" ---  " + songH.getId());
            list.add(0, songH);
        }
        MutableTreeNode parent = root;
        TreePath tp = new TreePath(root);
        try {
            for (SongEJB x : list) {
                System.out.println(" looking for " + x.getId());
                Enumeration<SongNode> e = parent.children();
                parent = null;
                while (e.hasMoreElements()) {
                    SongNode sn = e.nextElement();
                    System.out.println(" visiting " + sn.getId());
                    if (sn.getSong().getId().equals(x.getId())) {
                        System.out.println(" found " + x.getId());
                        parent = sn;
                        tp = tp.pathByAddingChild(sn);
                        break;
                    }
                }
                if (parent == null) {
                    throw new Exception(" Ancestor not found in the tree oooops " + x.getId());
                }
            }
            SongNode newChild = context.createSongNode((SongNode) parent, leaf);
            treeModel.insertNodeInto(newChild, parent, parent.getChildCount());
            tp = tp.pathByAddingChild(newChild);
            tree.setSelectionPath(tp);
            tree.scrollPathToVisible(tp);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }
}

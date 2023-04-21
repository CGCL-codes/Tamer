package org.jimcat.gui.tagtree;

import java.awt.BorderLayout;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import javax.swing.JComponent;
import javax.swing.tree.TreeSelectionModel;
import org.jdesktop.swingx.JXTree;
import org.jimcat.gui.dndutil.TreeDropTargetListener;
import org.jimcat.model.tag.Tag;
import org.jvnet.substance.SubstanceLookAndFeel;

/**
 * A facade class for tag tree handling.
 * 
 * $Id: TagTree.java 934 2007-06-15 08:40:58Z 07g1t1u2 $
 * 
 * @author Herbert
 */
public class TagTree extends JComponent {

    /**
	 * list of listeners
	 */
    private List<TagTreeListener> myListeners = new CopyOnWriteArrayList<TagTreeListener>();

    /**
	 * a reference to the contained JTree
	 */
    private JXTree tree;

    /**
	 * default constructor
	 */
    public TagTree() {
        initComponent();
    }

    /**
	 * builds up layout
	 */
    private void initComponent() {
        setLayout(new BorderLayout());
        tree = new JXTree();
        tree.setCellRenderer(new CheckBoxTreeCellRenderer());
        tree.setCellEditor(new CheckBoxTreeCellEditor(tree));
        tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        tree.setInvokesStopCellEditing(true);
        CheckBoxTreeActionHandler handler = new CheckBoxTreeActionHandler();
        tree.addMouseListener(handler);
        tree.addKeyListener(handler);
        TagTreePopupHandler popupHandler = new TagTreePopupHandler(tree);
        tree.addMouseListener(popupHandler);
        tree.addKeyListener(popupHandler);
        tree.setDragEnabled(true);
        TagTreeTransferHandler.installTagTreeTransferHandler(this);
        tree.setOpaque(false);
        tree.setEditable(true);
        tree.setModel(new TagTreeModel(this));
        tree.setRootVisible(false);
        tree.setShowsRootHandles(true);
        tree.expandAll();
        tree.collapseAll();
        tree.putClientProperty(SubstanceLookAndFeel.WATERMARK_TO_BLEED, Boolean.TRUE);
        TreeDropTargetListener.addNewTreeDropTargetListener(tree);
        add(tree, BorderLayout.CENTER);
    }

    /**
	 * this will set all tags within the list. All other tags are unset.
	 * 
	 * Setting those tags will happen quiettly, no TagTreeListener will be
	 * informed
	 * 
	 * @param tags
	 */
    public void selectTags(Set<Tag> tags) {
        setSubtreeTags((TagTreeNode) tree.getModel().getRoot(), tags);
        tree.repaint();
    }

    /**
	 * sets Tags recursivlly / quiettly
	 * 
	 * @param node
	 * @param tags
	 * 
	 * @see TagTree#selectTags(Set)
	 */
    private void setSubtreeTags(TagTreeNode node, Set<Tag> tags) {
        if (node == null) {
            return;
        }
        if (tags.contains(node.getTag())) {
            node.setStateQuietly(CheckBoxState.SET);
        } else {
            node.setStateQuietly(CheckBoxState.UNSET);
        }
        TagTreeNode child = null;
        Enumeration<?> children = node.children();
        while (children.hasMoreElements()) {
            child = (TagTreeNode) children.nextElement();
            setSubtreeTags(child, tags);
        }
        if (child != null) {
            CheckBoxTreeActionHandler.updateParent(child);
        }
    }

    /**
	 * this methode will return a set including all Tags currently selected
	 * within his tree
	 * 
	 * @return a list of selected tags
	 */
    public List<Tag> getSelectedTags() {
        List<Tag> tags = new LinkedList<Tag>();
        addSubTreeTags((TagTreeNode) tree.getModel().getRoot(), tags);
        return tags;
    }

    /**
	 * internally used to handel tree recusivelly. this methode will iterate
	 * through the subtree and add all selected tags to the given List
	 * 
	 * @param node -
	 *            root of subtree
	 * @param tags -
	 *            the list of tags to modified
	 */
    private void addSubTreeTags(TagTreeNode node, List<Tag> tags) {
        if (node == null) {
            return;
        }
        if (node.getTag() instanceof Tag) {
            if (node.getState() == CheckBoxState.SET) {
                tags.add((Tag) node.getTag());
            }
        } else {
            TagTreeNode child = null;
            Enumeration<?> children = node.children();
            while (children.hasMoreElements()) {
                child = (TagTreeNode) children.nextElement();
                addSubTreeTags(child, tags);
            }
        }
    }

    /**
	 * registers a new listener to this TagTree
	 * 
	 * @param listener
	 */
    public void addTagTreeListener(TagTreeListener listener) {
        myListeners.add(listener);
    }

    /**
	 * removes a probably registered Listener from this observeable
	 * 
	 * @param listener
	 */
    public void removeTagTreeListener(TagTreeListener listener) {
        myListeners.remove(listener);
    }

    /**
	 * informes all listeners about a selection
	 * 
	 * @param tag -
	 *            the selected tag
	 */
    protected void notifyListenersAboutSelection(Tag tag) {
        for (TagTreeListener listener : myListeners) {
            listener.tagSelected(tag);
        }
    }

    /**
	 * informes all listeners about an unselection
	 * 
	 * @param tag -
	 *            the unselected tag
	 */
    protected void notifyListenersAboutUnSelection(Tag tag) {
        for (TagTreeListener listener : myListeners) {
            listener.tagUnSelected(tag);
        }
    }

    /**
	 * @return the tree
	 */
    public JXTree getTree() {
        return tree;
    }
}

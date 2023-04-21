package org.columba.mail.gui.tree;

import java.awt.Point;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JComponent;
import javax.swing.JTree;
import javax.swing.Timer;
import javax.swing.ToolTipManager;
import javax.swing.TransferHandler;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import org.columba.core.xml.XmlElement;
import org.columba.mail.config.IFolderItem;
import org.columba.mail.folder.IMailFolder;
import org.frapuccino.swing.SortedJTree;

/**
 * this class does all the dirty work for the TreeController
 */
public class TreeView extends SortedJTree {

    /** The treepath that was selected before the drag and drop began. */
    private TreePath selectedPathBeforeDrag;

    /** The treepath that is under the mouse pointer in a drag and drop action. */
    private TreePath dropTargetPath;

    /** The component is in a drag and drop action */
    private boolean isInDndMode = false;

    /**
	 * A Timer that expands/collapses leafs when the mouse hovers above it. This
	 * is only used during Drag and Drop.
	 */
    private Timer dndAutoExpanderTimer;

    /**
	 * Constructa a tree view
	 * 
	 * @param model
	 *            the tree model that this JTree should use.
	 */
    public TreeView(javax.swing.tree.TreeModel model) {
        super(model);
        ToolTipManager.sharedInstance().registerComponent(this);
        putClientProperty("JTree.lineStyle", "Angled");
        setShowsRootHandles(true);
        setRootVisible(false);
        IMailFolder root = (IMailFolder) treeModel.getRoot();
        expand(root);
        repaint();
        setDropTarget(new DropHandler());
        dndAutoExpanderTimer = new Timer(1000, new TreeLeafActionListener(this));
        dndAutoExpanderTimer.setRepeats(false);
    }

    /**
	 * Expands the specified node so it corresponds to the expanded attribute in
	 * the configuration.
	 * 
	 * @param parent
	 *            node to check if it should be expanded or not.
	 */
    public final void expand(IMailFolder parent) {
        IFolderItem item = parent.getConfiguration();
        XmlElement property = item.getElement("property");
        if (property != null) {
            String expanded = property.getAttribute("expanded");
            if (expanded == null) {
                expanded = "true";
            }
            int row = getRowForPath(new TreePath(parent.getPath()));
            if (expanded.equals("true")) {
                expandRow(row);
            }
        }
        for (int i = 0; i < parent.getChildCount(); i++) {
            IMailFolder child = (IMailFolder) parent.getChildAt(i);
            expand(child);
        }
    }

    /**
	 * Returns the tree node that is intended for a drop action. If this method
	 * is called during a non-drag-and-drop invocation there is no guarantee
	 * what it will return.
	 * 
	 * @return the folder tree node that is targeted for the drop action; null
	 *         otherwise.
	 */
    public IMailFolder getDropTargetFolder() {
        IMailFolder node = null;
        if (dropTargetPath != null) {
            node = (IMailFolder) dropTargetPath.getLastPathComponent();
        } else {
            if (getSelectionPath() != null) node = (IMailFolder) getSelectionPath().getLastPathComponent();
        }
        return node;
    }

    /**
	 * Sets the stored drop target path to null. This should be done after the
	 * getDropTargetFolder() has been used in a folder command.
	 */
    void resetDropTargetFolder() {
        dropTargetPath = null;
    }

    /**
	 * Returns the tree node that was selected before a drag and drop was
	 * initiated. If this method is called during a non-drag-and-drop invocation
	 * there is no guarantee what it will return.
	 * 
	 * @return the folder that is being dragged; null if it wasnt initiated in
	 *         this component.
	 */
    public DefaultMutableTreeNode getSelectedNodeBeforeDragAction() {
        DefaultMutableTreeNode node = null;
        if (selectedPathBeforeDrag != null) {
            node = (DefaultMutableTreeNode) selectedPathBeforeDrag.getLastPathComponent();
        }
        return node;
    }

    /**
	 * Returns true if the tree is in a Drag and Drop action.
	 * 
	 * @return true if the tree is in a Drag and Drop action; false otherwise.
	 */
    public boolean isInDndAction() {
        return isInDndMode;
    }

    /**
	 * Sets up this TreeView for Drag and drop action. Stores the selected tree
	 * leaf before the action begins, this is used later when the Drag and drop
	 * action is completed.
	 */
    private void setUpDndAction() {
        isInDndMode = true;
        selectedPathBeforeDrag = getSelectionPath();
    }

    /**
	 * Resets this TreeView after a Drag and drop action has occurred. Selects
	 * the previous selected tree leaf before the DnD action began.
	 */
    private void resetDndAction() {
        dndAutoExpanderTimer.stop();
        setSelectionPath(selectedPathBeforeDrag);
        selectedPathBeforeDrag = null;
        isInDndMode = false;
    }

    /**
	 * Our own drop target implementation. This treeview class uses its own drop
	 * target since the common drop target in Swing >1.4 does not provide a fine
	 * grained support for dragging items onto leafs, when some leafs does not
	 * accept new items.
	 * 
	 * @author redsolo
	 */
    private class DropHandler extends DropTarget {

        private boolean canImport;

        /** The latest mouse location. */
        private Point location;

        /**
		 * Our own implementation to ask the transfer handler for each leaf the
		 * user moves above. {@inheritDoc}
		 */
        public void dragOver(DropTargetDragEvent e) {
            if ((location == null) || (!location.equals(e.getLocation()))) {
                location = e.getLocation();
                TreePath targetPath = getClosestPathForLocation(location.x, location.y);
                if ((dropTargetPath != null) && (targetPath == dropTargetPath)) {
                    return;
                }
                dropTargetPath = targetPath;
                dndAutoExpanderTimer.restart();
                TreeView.this.getSelectionModel().setSelectionPath(dropTargetPath);
                DataFlavor[] flavors = e.getCurrentDataFlavors();
                JComponent c = (JComponent) e.getDropTargetContext().getComponent();
                TransferHandler importer = c.getTransferHandler();
                if ((importer != null) && importer.canImport(c, flavors)) {
                    canImport = true;
                } else {
                    canImport = false;
                }
                int dropAction = e.getDropAction();
                if (canImport) {
                    e.acceptDrag(dropAction);
                } else {
                    e.rejectDrag();
                }
            }
        }

        /** {@inheritDoc} */
        public void dragEnter(DropTargetDragEvent e) {
            setUpDndAction();
            DataFlavor[] flavors = e.getCurrentDataFlavors();
            JComponent c = (JComponent) e.getDropTargetContext().getComponent();
            TransferHandler importer = c.getTransferHandler();
            if ((importer != null) && importer.canImport(c, flavors)) {
                canImport = true;
            } else {
                canImport = false;
            }
            int dropAction = e.getDropAction();
            if (canImport) {
                e.acceptDrag(dropAction);
            } else {
                e.rejectDrag();
            }
        }

        /** {@inheritDoc} */
        public void dragExit(DropTargetEvent e) {
            resetDndAction();
            dropTargetPath = null;
        }

        /** {@inheritDoc} */
        public void drop(DropTargetDropEvent e) {
            int dropAction = e.getDropAction();
            JComponent c = (JComponent) e.getDropTargetContext().getComponent();
            TransferHandler importer = c.getTransferHandler();
            if (canImport && (importer != null)) {
                e.acceptDrop(dropAction);
                try {
                    Transferable t = e.getTransferable();
                    e.dropComplete(importer.importData(c, t));
                } catch (RuntimeException re) {
                    e.dropComplete(false);
                }
            } else {
                e.rejectDrop();
            }
            resetDndAction();
        }

        /** {@inheritDoc} */
        public void dropActionChanged(DropTargetDragEvent e) {
            int dropAction = e.getDropAction();
            if (canImport) {
                e.acceptDrag(dropAction);
            } else {
                e.rejectDrag();
            }
        }
    }

    /**
	 * An ActionListener that collapses/expands leafs in a tree.
	 * 
	 * @author redsolo
	 */
    private class TreeLeafActionListener implements ActionListener {

        /**
		 * Constructs a leaf listener.
		 * 
		 * @param parent
		 *            the parent JTree.
		 */
        public TreeLeafActionListener(JTree parent) {
        }

        /** {@inheritDoc} */
        public void actionPerformed(ActionEvent e) {
            if (dropTargetPath != null) {
                if (isExpanded(dropTargetPath)) {
                    collapsePath(dropTargetPath);
                } else {
                    expandPath(dropTargetPath);
                }
            }
        }
    }
}

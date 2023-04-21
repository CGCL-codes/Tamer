package org.plantstreamer.treetable.node;

import java.util.List;
import org.communications.CommunicationManager.STATUS;
import org.jdesktop.swingx.treetable.AbstractMutableTreeTableNode;
import org.opcda2out.output.database.nodes.PersistentOPCItemInfo;
import org.plantstreamer.Main;
import org.plantstreamer.treetable.OPCTreeTableModel;
import org.plantstreamer.treetable.OPCTreeTableModel.SELECTIONSTATUS;

/**
 * An abstract OPC node
 *
 * @author Joao Leal
 */
public abstract class AbstractOPCTreeTableNode extends AbstractMutableTreeTableNode {

    /**
     * The total number of selected child OPC items (does not include itself)
     * (can be higher than the number of childs)
     */
    private int totalSelectedItemCount = 0;

    /**
     * Creates a new AbstractOPCTreeTableNode
     *
     * @param name the node name to be displayed in the tree
     */
    public AbstractOPCTreeTableNode(String name) {
        super(name, true);
    }

    @Override
    public AbstractOPCTreeTableNode getChildAt(int childIndex) {
        return (AbstractOPCTreeTableNode) super.getChildAt(childIndex);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getColumnCount() {
        return OPCTreeTableModel.COLUMN_COUNT;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isEditable(int column) {
        if (column == OPCTreeTableModel.COLUMN_SAVE) {
            if (Main.opc2outManager.isRunning() || Main.opc.getConnectionStatus() != STATUS.CONNECTED) {
                return false;
            }
            SELECTIONSTATUS s = getSelectionStatus();
            return s == SELECTIONSTATUS.UNSELECTED || s == SELECTIONSTATUS.SELECTED;
        }
        return false;
    }

    /**
     * Determines whether or not this node may be removed from its parent
     *
     * @return <code>true</code> if this node may be removed, <code>false</code>
     * otherwise
     */
    public boolean isRemovable() {
        return true;
    }

    /**
     * Removes all the children in this node
     */
    public void removeAll() {
        if (children != null) {
            for (int i = children.size() - 1; i >= 0; i--) {
                children.get(i).setParent(null);
            }
            children.clear();
        }
    }

    /**
     * Provides an array with the current children inside this node
     *
     * @return An array containing this node's children
     */
    public AbstractOPCTreeTableNode[] getChildren() {
        if (children != null) {
            return children.toArray(new AbstractOPCTreeTableNode[children.size()]);
        } else {
            return new AbstractOPCTreeTableNode[0];
        }
    }

    /**
     * Whether or not the node is selected
     *
     * @return <code>true</code> if the node is selected,
     *         <code>false</code> otherwise
     */
    public boolean isSelected() {
        return getSelectionStatus() == SELECTIONSTATUS.SELECTED;
    }

    /**
     * Returns the selection status of this node
     *
     * @return The selection status of this node
     */
    public abstract SELECTIONSTATUS getSelectionStatus();

    /**
     * Sets the current selection status
     * 
     * @param newStatus The node selection status (not null)
     * @return Whether or not the selection status was changed
     */
    public abstract boolean setSelectionStatus(SELECTIONSTATUS newStatus);

    /**
     * Checks if a selection status change is valid
     *
     * @param oldStatus The previous selection status
     * @param newStatus The new selection status
     */
    protected static void checkSelectionStatusChange(SELECTIONSTATUS oldStatus, SELECTIONSTATUS newStatus) {
        if (newStatus == null) {
            throw new IllegalArgumentException("The selection status must not be null");
        }
        if (newStatus == SELECTIONSTATUS.REQUEST) {
            if (oldStatus != SELECTIONSTATUS.UNSELECTED) {
                throw new IllegalArgumentException("Requests for selection can only be performed for unselected nodes");
            }
        } else if (newStatus == SELECTIONSTATUS.REVALIDATE) {
            if (oldStatus != SELECTIONSTATUS.SELECTED) {
                throw new IllegalArgumentException("Can only revalidate previously selected nodes");
            }
        }
    }

    @Override
    public AbstractOPCTreeTableNode getParent() {
        return (AbstractOPCTreeTableNode) parent;
    }

    /**
     * Whether or not this node is an OPC item property
     *
     * @return <code>true</code> if it is an OPC item property
     */
    public abstract boolean isOPCitemProperty();

    /**
     * Determines if the current node or its children contain any selected OPC
     * item
     *
     * @return <code>true</code> if it contains selected OPC items,
     *         <code>false</code> if there no selected items
     */
    public boolean containsSelectedChilds() {
        return totalSelectedItemCount > 0;
    }

    /**
     * Returns the total number of selected OPC item/property in this node path
     *
     * @return The total number of selected OPC item/property in this node path
     */
    public int getTotalSelectedItemCount() {
        return totalSelectedItemCount;
    }

    /**
     * Sets the total number of selected childs contained by this node
     * 
     * @param totalSelectedItemCount The new number of the selected childs count
     */
    public void setTotalSelectedItemCount(int totalSelectedItemCount) {
        this.totalSelectedItemCount = totalSelectedItemCount;
    }

    /**
     * Provides a list with the selected OPC items and the OPC items with
     * selected properties
     *
     * @return A list with the selected OPC items and the OPC items with
     * selected properties
     */
    public abstract List<PersistentOPCItemInfo> getItemsWithSelectedChilds();

    /**
     * 
     * @return A list with all the OPC items (whether they are selected or not)
     *         and populates properties with the selected properties or the
     *         properties which contain other selected properties
     */
    public abstract List<PersistentOPCItemInfo> getAllItemsNProperties();

    /**
     * Determines if this node is equal or contains within its childs a
     * provided node
     *
     * @param node The node to be found
     * @return <code>true<code> if the node was found, <code>false</code>
     *         otherwise
     */
    public boolean containsNode(AbstractOPCTreeTableNode node) {
        if (this.equals(node)) {
            return true;
        }
        AbstractOPCTreeTableNode child;
        for (int i = 0; i < getChildCount(); i++) {
            child = getChildAt(i);
            if (!child.isLeaf() && child.containsNode(node)) {
                return true;
            }
        }
        return false;
    }
}

package org.xaware.ide.xadev.gui.dragndroptree;

import org.xaware.ide.xadev.datamodel.XMLTreeNode;
import org.xaware.ide.xadev.gui.DNDTreeHandler;

/**
 * @author hcurtis
 * 
 */
public interface IDropHandler {

    /**
     * Indicates this object can be dropped on the supplied node.
     * 
     * @param node -
     *            The XMLTreeNode the object was dropped on
     * @return Returns true if there is a valid operation to perform the drop on this node
     */
    boolean isDropSupported(XMLTreeNode node);

    /**
     * Indicates if a popup menu is required to specify how the drop should take place. It returns false if there is
     * only one option avaiable
     * 
     * @return Returns true if a popup menu is required to select the type of drop operation
     */
    boolean isPopupDropMenuRequired();

    /**
     * Indicates to the dropped tree if this object supports being dropped onto an Element
     * 
     * @param node -
     *            This is the selected (dropped on) tree node
     * @return Returns true if it does support being dropped on an Element, false otherwise.
     */
    boolean supportsDropOn(XMLTreeNode node);

    /**
     * Indicates to the dropped tree if this object supports being dropped before the selected Element
     * 
     * @param node -
     *            This is the selected (dropped on) tree node
     * @return Returns true if it does support being dropped before the selected Element, false otherwise.
     */
    boolean supportsDropBefore(XMLTreeNode node);

    /**
     * Indicates to the dropped tree if this object supports being dropped after the selected Element as a sibling
     * 
     * @param node -
     *            This is the selected (dropped on) tree node
     * @return Returns true if it does support being dropped after the selected Element, false otherwise.
     */
    boolean supportsDropAfter(XMLTreeNode node);

    /**
     * Indicates to the dropped tree if this object supports being dropped as a child of the selected Element
     * 
     * @param treeHandler
     *            The DNDTreeHandler containing the tree on which the drop is landing.
     * @return Returns true if it does support being dropped as a child of the selected Element, false otherwise.
     */
    boolean supportsDropAsChildOf(XMLTreeNode node);

    /**
     * Complete the drop on the supplied node
     * 
     * @param node -
     *            This is the selected (dropped on) tree node
     * @param treeHandler
     *            The DNDTreeHandler containing the tree on which the drop is landing.
     * @return Returns true if the operation was successful, false otherwise.
     */
    boolean doDropOn(XMLTreeNode node, DNDTreeHandler treeHandler);

    /**
     * Complete the drop adding an element before the supplied node
     * 
     * @param node -
     *            This is the selected (dropped on) tree node
     * @param treeHandler
     *            The DNDTreeHandler containing the tree on which the drop is landing.
     * @param sourceNode -
     *            The original XMLTreeNode being dragged (may be null).
     * @return Returns true if the operation was successful, false otherwise.
     */
    boolean doDropBefore(XMLTreeNode node, DNDTreeHandler treeHandler, XMLTreeNode sourceNode);

    /**
     * Complete the drop adding an element after the supplied node
     * 
     * @param node -
     *            This is the selected (dropped on) tree node
     * @param treeHandler
     *            The DNDTreeHandler containing the tree on which the drop is landing.
     * @param sourceNode -
     *            The original XMLTreeNode being dragged (may be null).
     * @return Returns true if the operation was successful, false otherwise.
     */
    boolean doDropAfter(XMLTreeNode node, DNDTreeHandler treeHandler, XMLTreeNode sourceNode);

    /**
     * Complete the drop on the supplied node, adding the object as a child to the supplied node
     * 
     * @param node -
     *            This is the selected (dropped on) tree node
     * @param treeHandler
     *            The DNDTreeHandler containing the tree on which the drop is landing.
     * @param sourceNode -
     *            The original XMLTreeNode being dragged (may be null).
     * @return Returns true if the operation was successful, false otherwise.
     */
    boolean doDropAsAChild(XMLTreeNode node, DNDTreeHandler treeHandler, XMLTreeNode sourceNode);

    void updateTargetElementNamespaces(XMLTreeNode p_targetNode, XMLTreeNode p_selectedNode);
}

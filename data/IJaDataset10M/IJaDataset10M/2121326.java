package com.sri.common.taglib;

/**
 * <tt>Composite</tt> Tree Node interface.  Allows for iterating children (if any)
 * and all information needed to render the tree node.
 *
 * @author Michael Rimov
 */
public interface TreeNode {

    /**
     * If there are no children then getNested() returns NO_CHILDREN which
     * is a <tt>Special Case</tt> of no children nodes.
     */
    TreeNode[] NO_CHILDREN = new TreeNode[0];

    /**
     * Retrieve the Label of the tree node.
     *
     * @return String the label
     */
    String getLabel();

    /**
     * Retrieve the link if the node is clicked.
     *
     * @return String url for the link of the node.
     */
    String getLink();

    /**
     * Retrieve the CSS style for the node when it is selected. (Or for folders,
     * when its children are selected or it is open)
     *
     * @return String
     */
    String getSelectedStyle();

    /**
     * Retrieve the CSS style for the node when it is unselected. (Or for
     * folders, when it is closed)
     *
     * @return String
     */
    String getUnselectedStyle();

    /**
     * Retrieve all children (may be an empty array) if no children.
     *
     * @return TreeNode[] or TreeNode.NO_CHILDREN if there are none.
     */
    TreeNode[] getNested();

    /**
     * Retrieve url of the icon associated with the node.  This is
     * optional, and if it is set, the image tag will be set inside
     * the &lt;a&gt; tag.
     *
     * @return String
     */
    String getIconUrl();

    /**
     * Returns true if this node is selected.  If it is a folder node,
     * then it should normally appear open as well.
     *
     * @return true if it or its children are selected.
     */
    boolean isSelected();

    /**
     * Manipulator that sets the selected value to true.  Expected behavior
     * is that when something is selected, all parents are selected.  If something
     * is de-selected, then all parents are de-selected.
     *
     * @param selectionValue true if the value should be selected.
     */
    void setSelected(boolean selectionValue);

    /**
     * Expands all folder nodes by marking them selected without marking
     * any leaf nodes necessarily selected.
     */
    void expandAllFolders();

    /**
     * Collapses all folders by marking the root node as unselected.
     */
    void collapseAllFolders();
}

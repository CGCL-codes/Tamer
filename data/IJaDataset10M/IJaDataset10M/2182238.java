package com.loribel.commons.abstraction;

/**
 * Abstraction to represent a hierarchy.
 * See {@link GB_SimpleNode} for details...
 *
 * @author Gregory Borelli
 */
public interface GB_SimpleNodeSet extends GB_SimpleNode, GB_NameOwnerSet {

    /**
     * Add an array of child to this node.
     *
     * @param a_childArray GB_SimpleNode[] -
     */
    void addAllChild(GB_SimpleNode[] a_childArray);

    /**
     * Add a Child to the this node.
     *
     * @param a_child GB_SimpleNode -
     *
     * @return boolean
     */
    boolean addChild(GB_SimpleNode a_child);

    /**
     * Remove all children to this node.
     */
    void removeAllChild();

    /**
     * Remove a child.
     *
     * @param a_child GB_SimpleNode -
     *
     * @return boolean
     */
    boolean removeChild(GB_SimpleNode a_child);

    /**
     * Set the labelIcon.
     *
     * @param a_labelIcon GB_LabelIcon -
     */
    void setLabelIcon(GB_LabelIcon a_labelIcon);

    /**
     * Set the value of this node.
     *
     * @param a_value Object -
     */
    void setValue(Object a_value);
}

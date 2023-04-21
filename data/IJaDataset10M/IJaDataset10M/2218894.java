package com.sun.perseus.model;

/**
 * <code>DecoratedNode</code> is the base interface for handling
 * properties with inheritance. For example, the <code>TextNode</code>
 * interface is an extension that defines text decoration properties
 * such as fontFamily, fontSize or fontWeight. By the same token,
 * the <code>GraphicsNode</code> extension defines graphical properties
 * such as the fillColor, the strokeWidth or the strokeColor.
 *
 * @see com.sun.perseus.model.TextNode
 * @see com.sun.perseus.model.GraphicsNode
 *
 * @version $Id: DecoratedNode.java,v 1.4 2006/04/21 06:36:45 st125089 Exp $
 */
public interface DecoratedNode {

    /** 
     * Check if the property is inherited.
     *
     * @param propertyIndex the index of the property whose 
     *        inherit status is checked.
     * @return true if the input property is inherited. False
     *         otherwise
     */
    boolean isInherited(int propertyIndex);

    /**
     * Sets the input property's inheritance status
     * @param propertyIndex the index of the property whose inherit
     *        status is set
     * @param inherit the new inherit status for the property at
     *        index propertyIndex.
     */
    void setInherited(int propertyIndex, boolean inherit);

    /**
     * Sets the input float property's inheritance status
     * @param propertyIndex the index of the property whose inherit
     *        status is set
     * @param inherit the new inherit status for the property at
     *        index propertyIndex.
     */
    void setFloatInherited(int propertyIndex, boolean inherit);

    /**
     * Sets the input packed property's inheritance status
     * @param propertyIndex the index of the property whose inherit
     *        status is set
     * @param inherit the new inherit status for the property at
     *        index propertyIndex.
     */
    void setPackedInherited(int propertyIndex, boolean inherit);

    /**
     * @return the number of properties on this node
     */
    int getNumberOfProperties();
}

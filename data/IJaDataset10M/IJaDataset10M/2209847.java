package com.xith3d.scenegraph;

/**
 * DecalGroup node guarantees that its children will be rendered
 * in their index order.
 */
public class DecalGroup extends OrderedGroup {

    /**
     * Used by direct sub-classes to define where their capabilty
     * bit positions start. The first bit in a sub-class should be defined
     * as DecalGroup.LAST_CAPS_BIT_POSITION+1.
     */
    public static final int LAST_CAPS_BIT_POSITION = OrderedGroup.LAST_CAPS_BIT_POSITION;

    /**
     * Constructs a new DecalGroup object.
     */
    public DecalGroup() {
        super();
    }
}

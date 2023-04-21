package org.xith3d.collider;

import org.xith3d.datatypes.Coord3f;

/**
 * Interface used to define how a force acts on a collision node.
 * 
 * @author David Yazel
 */
public interface Force {

    /**
     * @param node The node the force is acting on
     * @param time The time that has passed since the last time this force was applied
     * @return If false then this force is removed from the node
     */
    boolean apply(ColliderNode node, long time, Coord3f velocity);
}

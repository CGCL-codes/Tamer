package org.xith3d.spatial;

/**
 * Used to add user controlled culling.
 * 
 * @author David Yazel
 * @author Marvin Froehlich (aka Qudus) [code cleaning]
 */
public interface SpatialCuller {

    /**
     * Implement this method to supply a culling check against a node
     * in a spatial container.
     * @param handle The spatial handle for this object
     * @return True if this node should be culled. False if it should  be kept.
     */
    boolean cull(SpatialHandle handle);
}

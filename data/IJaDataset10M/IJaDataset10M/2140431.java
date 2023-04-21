package org.xith3d.terrain.legacy.heightmap;

import org.xith3d.scenegraph.Geometry;

/**
 * Represents a terrain generation object.
 * 
 * @author William Denniss
 * @version 1.0 - 22 December 2003
 */
public interface Terrain {

    /**
     * Peform CPU calculations to create the terrain
     */
    public void generateTerrain();

    /**
     * Build 3D geometry from terrain
     */
    public Geometry generateGeometry(float startX, float startY, float stepX, float stepY);
}

package org.npsnet.v.properties.model.terrain;

import javax.vecmath.*;

/**
 * A property interface for terrain segments: rectangular blocks
 * of terrain.  The terrain segments are assumed to be centered
 * about the (model space) origin in the X/Z plane.
 *
 * @author Andrzej Kapolka
 */
public interface TerrainSegment extends Terrain {

    /**
     * Returns the natural resolution of this terrain segment.
     * For heightfield terrain segments, the natural resolution
     * is the resolution of the source heightfield.
     *
     * @return the natural resolution of this terrain segment
     */
    public TerrainResolution getNaturalResolution();

    /**
     * Checks whether this terrain segment can be sampled at the
     * specified resolution.
     *
     * @param res the desired resolution
     * @return <code>true</code> if this terrain segment may be
     * sampled at the specified resolution; <code>false</code>
     * otherwise
     */
    public boolean isSupportedResolution(TerrainResolution res);

    /**
     * Returns the region occupied by this terrain segment.
     *
     * @return a <code>TerrainRect</code> representing the region
     * occupied by this terrain segment
     */
    public TerrainRect getOccupiedRegion();

    /**
     * Samples the entire terrain segment at its natural resolution.
     *
     * @param hf the array to contain the heightfield data
     */
    public void getHeightfield(double[] hf);

    /**
     * Samples a region of this terrain segment at the segment's natural
     * resolution.
     *
     * @param hf the array to contain the heightfield data
     * @param tr the region to sample
     */
    public void getHeightfield(double[] hf, TerrainRect tr);

    /**
     * Samples a region of this terrain segment.
     *
     * @param hf the array to contain the heightfield data
     * @param tr the region to sample
     * @param res the resolution at which to sample the terrain
     */
    public void getHeightfield(double[] hf, TerrainRect tr, TerrainResolution res);
}

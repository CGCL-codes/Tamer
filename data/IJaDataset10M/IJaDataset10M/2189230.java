package de.bloxel.world.chunk;

import com.jme3.math.Vector3f;
import de.bloxel.math.OctTree;
import de.bloxel.world.Bloxel;

/**
 * This chunk use a ({@link OctTree} to define the chunk volume.
 * 
 * @author Andreas H�hmann
 * @since 0.2.0
 */
public class OctTreeChunk extends BaseChunk<OctTree<Bloxel>> {

    /**
   * @param theCenter
   *          is absolute position in the "world", if you using only one chunk the use the {@link Vector3f#ZERO}, if you
   *          combine more than one chunk (i.e. in a grid of chunks for open world scenarios) then only the central
   *          chunk will have the {@link Vector3f#ZERO} as center and all other chunks will stay around the center chunk
   * @param theChunkSize
   *          in x, y and z direction, then the chunk have a volume of (x:-aSize..aSize, y:-aSize..aSize,
   *          z:-aSize..aSize), depending on this size the chunk can have as much {@link Bloxel}S as possible,
   *          all {@link Bloxel}S of the chunk will be <b>inside</b> the chunk
   */
    public OctTreeChunk(final Vector3f theCenter, final float theChunkSize) {
        this(theCenter, theChunkSize, DEFAULT_BLOXEL_DIMENSION);
    }

    /**
   * @param theCenter
   *          is absolute position in the "world", if you using only one chunk the use the {@link Vector3f#ZERO}, if you
   *          combine more than one chunk (i.e. in a grid of chunks for open world scenarios) then only the central
   *          chunk will have the {@link Vector3f#ZERO} as center and all other chunks will stay around the center chunk
   * @param theChunkSize
   *          in x, y and z direction, then the chunk have a volume of (x:-aSize..aSize, y:-aSize..aSize,
   *          z:-aSize..aSize), depending on this size the chunk can have as much {@link Bloxel}S as possible,
   *          all {@link Bloxel}S of the chunk will be <b>inside</b> the chunk
   * @param theBloxelSize
   *          size of each bloxel in the chunk, (x:-aSize..aSize, y:-aSize..aSize, z:-aSize..aSize)
   */
    public OctTreeChunk(final Vector3f theCenter, final float theChunkSize, final float theBloxelSize) {
        super(new OctTree<Bloxel>(theCenter, theChunkSize, theBloxelSize));
    }
}

package xenon3d.scene;

import javax.media.opengl.GL2;

/**
 * Geometry is an abstract class that specifies the geometry component
 * information required by a Shape or Mesh node. Geometry objects describe both
 * the geometry and topology of the Shape or Mesh node that references them.
 * Geometry objects consist of four generic geometric types: GeometryArray,
 * Quadric, Raster and Text3D. Each of these geometric types defines a visible
 * object or set of objects.
 *
 * @author Volker Everts
 * @version 0.1 - 13.08.2008: Created
 * @version 0.2 - 04.09.2011: Adapted
 */
public abstract class Geometry extends NodeComponent {

    /**
     * Performs vertical flipping of all texture coordinates.
     */
    public abstract void flipTextureCoordsVertically();

    /**
     * Returns whether texture coordinates are vertically flipped.
     * @return true, if texture coordinates are flipped
     */
    public abstract boolean areTextureCoordsVerticallyFlipped();

    /**
     * Draws this Geometry object.
     * @param gl the OpenGL gl interface object
     */
    abstract void draw(GL2 gl);
}

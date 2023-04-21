package com.jme.scene.shape;

import java.nio.FloatBuffer;
import com.jme.math.Vector3f;
import com.jme.scene.TexCoords;
import com.jme.util.geom.BufferUtils;

/**
 * A box with solid (filled) faces.
 * 
 * @author Mark Powell
 * @version $Revision: 4131 $, $Date: 2009-03-19 13:15:28 -0700 (Thu, 19 Mar 2009) $
 */
public class Box extends AbstractBox {

    private static final int[] GEOMETRY_INDICES_DATA = { 2, 1, 0, 3, 2, 0, 6, 5, 4, 7, 6, 4, 10, 9, 8, 11, 10, 8, 14, 13, 12, 15, 14, 12, 18, 17, 16, 19, 18, 16, 22, 21, 20, 23, 22, 20 };

    private static final float[] GEOMETRY_NORMALS_DATA = { 0, 0, -1, 0, 0, -1, 0, 0, -1, 0, 0, -1, 1, 0, 0, 1, 0, 0, 1, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 1, 0, 0, 1, 0, 0, 1, -1, 0, 0, -1, 0, 0, -1, 0, 0, -1, 0, 0, 0, 1, 0, 0, 1, 0, 0, 1, 0, 0, 1, 0, 0, -1, 0, 0, -1, 0, 0, -1, 0, 0, -1, 0 };

    private static final float[] GEOMETRY_TEXTURE_DATA = { 1, 0, 0, 0, 0, 1, 1, 1, 1, 0, 0, 0, 0, 1, 1, 1, 1, 0, 0, 0, 0, 1, 1, 1, 1, 0, 0, 0, 0, 1, 1, 1, 1, 0, 0, 0, 0, 1, 1, 1, 1, 0, 0, 0, 0, 1, 1, 1 };

    private static final long serialVersionUID = 1L;

    public Box() {
        this(null);
    }

    /**
     * Creates a new box.
     * <p>
     * Center and vertices information must be supplied later.
     * 
     * @param name the name of the box.
     */
    public Box(String name) {
        super(name);
    }

    /**
     * Creates a new box.
     * <p>
     * The box has the given center and extends in the out from the center by
     * the given amount in <em>each</em> direction. So, for example, a box
     * with extent of 0.5 would be the unit cube.
     * 
     * @param name the name of the box.
     * @param center the center of the box.
     * @param x the size of the box along the x axis, in both directions.
     * @param y the size of the box along the y axis, in both directions.
     * @param z the size of the box along the z axis, in both directions.
     */
    public Box(String name, Vector3f center, float x, float y, float z) {
        super(name);
        updateGeometry(center, x, y, z);
    }

    /**
     * Constructor instantiates a new <code>Box</code> object.
     * <p>
     * The minimum and maximum point are provided, these two points define the
     * shape and size of the box but not it’s orientation or position. You should
     * use the {@link #setLocalTranslation()} and {@link #setLocalRotation()}
     * methods to define those properties.
     * 
     * @param name the name of the box.
     * @param min the minimum point that defines the box.
     * @param max the maximum point that defines the box.
     */
    public Box(String name, Vector3f min, Vector3f max) {
        super(name);
        updateGeometry(min, max);
    }

    /**
     * Creates a clone of this box.
     * <p>
     * The cloned box will have ‘_clone’ appended to it’s name, but all other
     * properties will be the same as this box.
     */
    public Box clone() {
        return new Box(getName() + "_clone", center.clone(), xExtent, yExtent, zExtent);
    }

    protected void duUpdateGeometryIndices() {
        if (getIndexBuffer() == null) {
            setIndexBuffer(BufferUtils.createIntBuffer(GEOMETRY_INDICES_DATA));
        }
    }

    protected void duUpdateGeometryNormals() {
        if (getNormalBuffer() == null) {
            setNormalBuffer(BufferUtils.createVector3Buffer(24));
            getNormalBuffer().put(GEOMETRY_NORMALS_DATA);
        }
    }

    protected void duUpdateGeometryTextures() {
        if (getTextureCoords().get(0) == null) {
            getTextureCoords().set(0, new TexCoords(BufferUtils.createVector2Buffer(24)));
            FloatBuffer tex = getTextureCoords().get(0).coords;
            tex.put(GEOMETRY_TEXTURE_DATA);
        }
    }

    protected void duUpdateGeometryVertices() {
        setVertexBuffer(BufferUtils.createVector3Buffer(getVertexBuffer(), 24));
        setVertexCount(24);
        Vector3f[] v = computeVertices();
        getVertexBuffer().put(new float[] { v[0].x, v[0].y, v[0].z, v[1].x, v[1].y, v[1].z, v[2].x, v[2].y, v[2].z, v[3].x, v[3].y, v[3].z, v[1].x, v[1].y, v[1].z, v[4].x, v[4].y, v[4].z, v[6].x, v[6].y, v[6].z, v[2].x, v[2].y, v[2].z, v[4].x, v[4].y, v[4].z, v[5].x, v[5].y, v[5].z, v[7].x, v[7].y, v[7].z, v[6].x, v[6].y, v[6].z, v[5].x, v[5].y, v[5].z, v[0].x, v[0].y, v[0].z, v[3].x, v[3].y, v[3].z, v[7].x, v[7].y, v[7].z, v[2].x, v[2].y, v[2].z, v[6].x, v[6].y, v[6].z, v[7].x, v[7].y, v[7].z, v[3].x, v[3].y, v[3].z, v[0].x, v[0].y, v[0].z, v[5].x, v[5].y, v[5].z, v[4].x, v[4].y, v[4].z, v[1].x, v[1].y, v[1].z });
    }
}

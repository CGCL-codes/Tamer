package homura.hde.core.scene;

import homura.hde.core.renderer.Renderer;
import homura.hde.core.scene.batch.GeomBatch;
import homura.hde.core.scene.batch.PointBatch;
import homura.hde.core.scene.intersection.CollisionResults;
import homura.hde.util.colour.ColorRGBA;
import homura.hde.util.geom.BufferUtils;
import homura.hde.util.maths.Vector2f;
import homura.hde.util.maths.Vector3f;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.logging.Logger;

/**
 * <code>Point</code> defines a collection of vertices that are rendered as
 * single points.
 * 
 * @author Mark Powell
 * @version $Id: Point.java,v 1.21 2007/08/02 21:54:36 nca Exp $
 */
public class Point extends Geometry {

    private static final Logger logger = Logger.getLogger(Point.class.getName());

    private static final long serialVersionUID = 1L;

    public Point() {
    }

    /**
	 * Constructor instantiates a new <code>Point</code> object with a given
	 * set of data. Any data may be null, except the vertex array. If this is
	 * null an exception is thrown.
	 * 
	 * @param name
	 *            the name of the scene element. This is required for
	 *            identification and comparision purposes.
	 * @param vertex
	 *            the vertices or points.
	 * @param normal
	 *            the normals of the points.
	 * @param color
	 *            the color of the points.
	 * @param texture
	 *            the texture coordinates of the points.
	 */
    public Point(String name, Vector3f[] vertex, Vector3f[] normal, ColorRGBA[] color, Vector2f[] texture) {
        super(name, BufferUtils.createFloatBuffer(vertex), BufferUtils.createFloatBuffer(normal), BufferUtils.createFloatBuffer(color), BufferUtils.createFloatBuffer(texture));
        generateIndices(0);
        logger.info("Point created.");
    }

    /**
	 * Constructor instantiates a new <code>Point</code> object with a given
	 * set of data. Any data may be null, except the vertex array. If this is
	 * null an exception is thrown.
	 * 
	 * @param name
	 *            the name of the scene element. This is required for
	 *            identification and comparision purposes.
	 * @param vertex
	 *            the vertices or points.
	 * @param normal
	 *            the normals of the points.
	 * @param color
	 *            the color of the points.
	 * @param texture
	 *            the texture coordinates of the points.
	 */
    public Point(String name, FloatBuffer vertex, FloatBuffer normal, FloatBuffer color, FloatBuffer texture) {
        super(name, vertex, normal, color, texture);
        generateIndices(0);
        logger.info("Point created.");
    }

    @Override
    public void reconstruct(FloatBuffer vertices, FloatBuffer normals, FloatBuffer colors, FloatBuffer textureCoords) {
        super.reconstruct(vertices, normals, colors, textureCoords);
        generateIndices(0);
    }

    @Override
    public void reconstruct(FloatBuffer vertices, FloatBuffer normals, FloatBuffer colors, FloatBuffer textureCoords, int batchIndex) {
        super.reconstruct(vertices, normals, colors, textureCoords, batchIndex);
        generateIndices(batchIndex);
    }

    protected void setupBatchList() {
        batchList = new ArrayList<GeomBatch>(1);
        PointBatch batch = new PointBatch();
        batch.setParentGeom(this);
        batchList.add(batch);
    }

    public PointBatch getBatch(int index) {
        return (PointBatch) batchList.get(index);
    }

    public void generateIndices(int batchIndex) {
        PointBatch batch = getBatch(batchIndex);
        if (batch.getIndexBuffer() == null || batch.getIndexBuffer().limit() != batch.getVertexCount()) {
            batch.setIndexBuffer(BufferUtils.createIntBuffer(batch.getVertexCount()));
        } else batch.getIndexBuffer().rewind();
        for (int x = 0; x < batch.getVertexCount(); x++) batch.getIndexBuffer().put(x);
    }

    /**
     * @return true if points are to be drawn antialiased
     */
    public boolean isAntialiased() {
        return getBatch(0).isAntialiased();
    }

    /**
     * Sets whether the point should be antialiased. May decrease performance. If
     * you want to enabled antialiasing, you should also use an alphastate with
     * a source of SB_SRC_ALPHA and a destination of DB_ONE_MINUS_SRC_ALPHA or
     * DB_ONE.
     * 
     * @param antiAliased
     *            true if the line should be antialiased.
     */
    public void setAntialiased(boolean antialiased) {
        getBatch(0).setAntialiased(antialiased);
    }

    /**
     * @return the pixel size of each point.
     */
    public float getPointSize() {
        return getBatch(0).getPointSize();
    }

    /**
     * Sets the pixel width of the point when drawn. Non anti-aliased point
     * sizes are rounded to the nearest whole number by opengl.
     * 
     * @param size
     *            The size to set.
     */
    public void setPointSize(float size) {
        getBatch(0).setPointSize(size);
    }

    /**
     * @return true if points are to be drawn antialiased
     */
    public boolean isAntialiased(int batchIndex) {
        return getBatch(batchIndex).isAntialiased();
    }

    /**
     * Sets whether the point should be antialiased. May decrease performance. If
     * you want to enabled antialiasing, you should also use an alphastate with
     * a source of SB_SRC_ALPHA and a destination of DB_ONE_MINUS_SRC_ALPHA or
     * DB_ONE.
     * 
     * @param antiAliased
     *            true if the line should be antialiased.
     */
    public void setAntialiased(boolean antialiased, int batchIndex) {
        getBatch(batchIndex).setAntialiased(antialiased);
    }

    /**
     * @return the pixel size of each point.
     */
    public float getPointSize(int batchIndex) {
        return getBatch(batchIndex).getPointSize();
    }

    /**
     * Sets the pixel width of the point when drawn. Non anti-aliased point
     * sizes are rounded to the nearest whole number by opengl.
     * 
     * @param size
     *            The size to set.
     */
    public void setPointSize(float size, int batchIndex) {
        getBatch(batchIndex).setPointSize(size);
    }

    /**
     * <code>draw</code> calls super to set the render state then passes
     * itself to the renderer. LOGIC: 1. If we're not RenderQueue calling draw
     * goto 2, if we are, goto 3 2. If we are supposed to use queue, add to
     * queue and RETURN, else 3 3. call super draw 4. tell renderer to draw me.
     * 
     * @param r
     *            the renderer to display
     */
    public void draw(Renderer r) {
        PointBatch batch;
        if (getBatchCount() == 1) {
            batch = getBatch(0);
            if (batch != null && batch.isEnabled()) {
                batch.setLastFrustumIntersection(frustrumIntersects);
                batch.draw(r);
                return;
            }
        }
        for (int i = 0, cSize = getBatchCount(); i < cSize; i++) {
            batch = getBatch(i);
            if (batch != null && batch.isEnabled()) batch.onDraw(r);
        }
    }

    public void findCollisions(Spatial scene, CollisionResults results) {
        ;
    }

    public boolean hasCollision(Spatial scene, boolean checkTriangles) {
        return false;
    }
}

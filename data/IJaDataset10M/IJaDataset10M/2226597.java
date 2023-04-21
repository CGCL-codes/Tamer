package jogamp.graph.curve.text;

import java.util.ArrayList;
import com.jogamp.graph.geom.Vertex;
import com.jogamp.graph.geom.Triangle;
import com.jogamp.graph.geom.Vertex.Factory;
import com.jogamp.graph.curve.OutlineShape;
import com.jogamp.graph.math.Quaternion;

public class GlyphShape {

    private Quaternion quat = null;

    private OutlineShape shape = null;

    /** Create a new Glyph shape
     * based on Parametric curve control polyline
     */
    public GlyphShape(Vertex.Factory<? extends Vertex> factory) {
        shape = new OutlineShape(factory);
    }

    /** Create a new GlyphShape from a {@link OutlineShape}
     * @param factory vertex impl factory {@link Factory}
     * @param shape {@link OutlineShape} representation of the Glyph
     */
    public GlyphShape(Vertex.Factory<? extends Vertex> factory, OutlineShape shape) {
        this(factory);
        this.shape = shape;
        this.shape.transformOutlines(OutlineShape.VerticesState.QUADRATIC_NURBS);
    }

    public final Vertex.Factory<? extends Vertex> vertexFactory() {
        return shape.vertexFactory();
    }

    public OutlineShape getShape() {
        return shape;
    }

    public int getNumVertices() {
        return shape.getVertices().size();
    }

    /** Get the rotational Quaternion attached to this Shape
     * @return the Quaternion Object
     */
    public Quaternion getQuat() {
        return quat;
    }

    /** Set the Quaternion that shall defien the rotation
     * of this shape.
     * @param quat
     */
    public void setQuat(Quaternion quat) {
        this.quat = quat;
    }

    /** Triangluate the glyph shape
     * @return ArrayList of triangles which define this shape
     */
    public ArrayList<Triangle> triangulate() {
        return shape.triangulate();
    }

    /** Get the list of Vertices of this Object
     * @return arrayList of Vertices
     */
    public ArrayList<Vertex> getVertices() {
        return shape.getVertices();
    }
}

package org.skycastle.shape;

import com.jme.math.Vector2f;
import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.TriMesh;
import com.jme.util.geom.BufferUtils;
import org.skycastle.util.ParameterChecker;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

/**
 * A set of buffers containing all data for a {@link TriMesh}, with fundtions to set the data.
 * Helps making it easier to create shapes programmatically.
 *
 * @author Hans Haggstrom
 */
public final class MeshData {

    private final int myNumberOfVertexes;

    private final int myNumberOfIndexes;

    private final FloatBuffer myVertexes;

    private final FloatBuffer myColors;

    private final FloatBuffer myTextureCoordinates;

    private final FloatBuffer myNormals;

    private final IntBuffer myIndexes;

    /**
     * Creates a new {@link MeshData}.
     *
     * @param numberOfVertexes  number of vertices (nodes) in the mesh data.
     * @param numberOfTriangles number of triangle faces to define across different vertices.
     */
    public MeshData(int numberOfVertexes, int numberOfTriangles) {
        ParameterChecker.checkPositiveNonZeroInteger(numberOfVertexes, "numberOfVertexes");
        ParameterChecker.checkPositiveNonZeroInteger(numberOfTriangles, "numberOfTriangles");
        myNumberOfVertexes = numberOfVertexes;
        myNumberOfIndexes = numberOfTriangles * 3;
        myVertexes = BufferUtils.createVector3Buffer(myNumberOfVertexes);
        myColors = BufferUtils.createColorBuffer(myNumberOfVertexes);
        myTextureCoordinates = BufferUtils.createVector2Buffer(myNumberOfVertexes);
        myNormals = BufferUtils.createVector3Buffer(myNumberOfVertexes);
        myIndexes = BufferUtils.createIntBuffer(myNumberOfIndexes);
    }

    /**
     * @param triMesh the {@link TriMesh} to apply the defined shape to.  Should not be null.
     */
    public void applyToTriMesh(TriMesh triMesh) {
        ParameterChecker.checkNotNull(triMesh, "triMesh");
        triMesh.reconstruct(myVertexes, myNormals, myColors, myTextureCoordinates, myIndexes);
    }

    /**
     * Specifies the properties of a vertex in the mesh data.
     * Uses grey color, and a fixed normal and texture coordinate position.
     *
     * @param index    index of the vertex to set properties for.
     * @param position position of the vertex in object space.
     */
    public void setPoint(final int index, final Vector3f position) {
        setPoint(index, position, new ColorRGBA(0.5f, 0.5f, 0.5f, 1.0f), new Vector3f(0, 1, 0), new Vector2f());
    }

    /**
     * Specifies the properties of a vertex in the mesh data.
     *
     * @param index              index of the vertex to set properties for.
     * @param position           position of the vertex in object space.
     * @param color              color of the vertex.
     * @param normal             normal for the vertex.
     * @param textureCoordinates texture coordinates for the vertex.
     */
    public void setPoint(final int index, final Vector3f position, final ColorRGBA color, final Vector3f normal, final Vector2f textureCoordinates) {
        BufferUtils.setInBuffer(position, myVertexes, index);
        BufferUtils.setInBuffer(normal, myNormals, index);
        BufferUtils.setInBuffer(color, myColors, index);
        BufferUtils.setInBuffer(textureCoordinates, myTextureCoordinates, index);
    }

    /**
     * @return number of vertexes in this {@link MeshData}.
     */
    public int getNumberOfVertexes() {
        return myNumberOfVertexes;
    }

    /**
     * @return number of indexes in this {@link MeshData}.
     */
    public int getNumberOfIndexes() {
        return myNumberOfIndexes;
    }

    /**
     * @return number of triangles in this {@link MeshData}.
     */
    public int getNumberOfTriangles() {
        return myNumberOfIndexes / 3;
    }

    /**
     * Specifies which vertexes should make up a triangle face.
     * Faces are defined in clockwice order (TODO: Check that)
     *
     * @param triangleIndex the index of the triangle to specify.
     * @param vertex1Index  index of first vertex making up a corner f the triangle face.
     * @param vertex2Index  index of second vertex making up a corner f the triangle face.
     * @param vertex3Index  index of third vertex making up a corner f the triangle face.
     * @param flipped       if true, the faces will be created in counterclockwice order instead.
     */
    public void setFace(int triangleIndex, int vertex1Index, int vertex2Index, int vertex3Index, boolean flipped) {
        if (flipped) {
            myIndexes.put(triangleIndex * 3, vertex1Index);
            myIndexes.put(triangleIndex * 3 + 1, vertex3Index);
            myIndexes.put(triangleIndex * 3 + 2, vertex2Index);
        } else {
            myIndexes.put(triangleIndex * 3, vertex1Index);
            myIndexes.put(triangleIndex * 3 + 1, vertex2Index);
            myIndexes.put(triangleIndex * 3 + 2, vertex3Index);
        }
    }

    /**
     * Specifies which vertexes should make up a quad face.
     * Faces are defined in clockwice order (TODO: Check that)
     *
     * @param triangleIndex the index of the first triangle in the quad to specify.
     * @param vertex1Index  index of first vertex making up a corner f the quad face.
     * @param vertex2Index  index of second vertex making up a corner f the quad face.
     * @param vertex3Index  index of third vertex making up a corner f the quad face.
     * @param vertex4Index  index of fourth vertex making up a corner f the quad face.
     * @param flipped       if true, the faces will be created in counterclockwice order instead.
     */
    public void setQuadFace(int triangleIndex, int vertex1Index, int vertex2Index, int vertex3Index, int vertex4Index, boolean flipped) {
        setFace(triangleIndex, vertex1Index, vertex2Index, vertex3Index, flipped);
        setFace(triangleIndex + 1, vertex3Index, vertex4Index, vertex1Index, flipped);
    }

    /**
     * Generates normals for the shape based on the normals of the faces.
     */
    public void calculateNormals() {
        addFaceNormalsToCornerNormals();
        normalizeAllNormals();
    }

    private void addFaceNormalsToCornerNormals() {
        Vector3f va = new Vector3f();
        Vector3f vb = new Vector3f();
        Vector3f vc = new Vector3f();
        Vector3f na = new Vector3f();
        Vector3f nb = new Vector3f();
        Vector3f nc = new Vector3f();
        Vector3f faceNormal = new Vector3f();
        for (int i = 0; i < getNumberOfTriangles(); i++) {
            int a = myIndexes.get(i * 3);
            int b = myIndexes.get(i * 3 + 1);
            int c = myIndexes.get(i * 3 + 2);
            getVector3f(myVertexes, a, va);
            getVector3f(myVertexes, b, vb);
            getVector3f(myVertexes, c, vc);
            getNormal(a, na);
            getNormal(b, nb);
            getNormal(c, nc);
            calculateFaceNormal(va, vb, vc, faceNormal);
            na.addLocal(faceNormal);
            nb.addLocal(faceNormal);
            nc.addLocal(faceNormal);
            setNormal(a, na);
            setNormal(b, nb);
            setNormal(c, nc);
        }
    }

    private void normalizeAllNormals() {
        Vector3f normal = new Vector3f();
        for (int i = 0; i < getNumberOfVertexes(); i++) {
            getNormal(i, normal);
            normal.normalizeLocal();
            BufferUtils.setInBuffer(normal, myNormals, i);
        }
    }

    private void calculateFaceNormal(final Vector3f va, final Vector3f vb, final Vector3f vc, final Vector3f normalOut) {
        normalOut.set(vb);
        normalOut.subtractLocal(va).crossLocal(vc.x - va.x, vc.y - va.y, vc.z - va.z);
        normalOut.normalizeLocal();
    }

    private void clearBufferLimit() {
        myVertexes.clear();
        myColors.clear();
        myTextureCoordinates.clear();
        myNormals.clear();
        myIndexes.clear();
    }

    private void setBufferLimit(final int vertexIndex, final int indicesIndex) {
        myVertexes.position(3 * vertexIndex);
        myColors.position(4 * vertexIndex);
        myTextureCoordinates.position(2 * vertexIndex);
        myNormals.position(3 * vertexIndex);
        myIndexes.position(indicesIndex);
        myVertexes.flip();
        myColors.flip();
        myTextureCoordinates.flip();
        myNormals.flip();
        myIndexes.flip();
    }

    private void getVector3f(final FloatBuffer vertexes, final int vertexIndex, final Vector3f vectorOut) {
        vectorOut.set(vertexes.get(vertexIndex * 3), vertexes.get(vertexIndex * 3 + 1), vertexes.get(vertexIndex * 3 + 2));
    }

    /**
     * Averages the the normals of the two specified vertexes.
     * @param vertexIndexA index of vertex whose normal should be smoothed with the other veertex normal
     * @param vertexIndexB index of vertex whose normal should be smoothed with the other veertex normal
     */
    public void smoothNormals(final int vertexIndexA, final int vertexIndexB) {
        Vector3f na = new Vector3f();
        Vector3f nb = new Vector3f();
        getNormal(vertexIndexA, na);
        getNormal(vertexIndexB, nb);
        na.addLocal(nb);
        na.divideLocal(2f);
        setNormal(vertexIndexA, na);
        setNormal(vertexIndexB, na);
    }

    /**
     * @param vertexIndex the vertex index to get a normal at.
     * @param normalOut the Vector3f to write the normal in
     */
    public void getNormal(final int vertexIndex, final Vector3f normalOut) {
        getVector3f(myNormals, vertexIndex, normalOut);
    }

    /**
     * @param vertexIndex the vertex index to set a normal at.
     * @param normal the normal to set.
     */
    public void setNormal(final int vertexIndex, final Vector3f normal) {
        BufferUtils.setInBuffer(normal, myNormals, vertexIndex);
    }
}

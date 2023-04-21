package glmodel;

import java.util.ArrayList;
import org.lwjgl.util.glu.*;
import java.nio.*;

/**
 * Holds a mesh object containing triangles and vertices.  Verts and Triangles
 * are stored as ArrayLists for flexibility and also as arrays for speedier
 * processing.
 *
 * The rebuild() function converts the ArrayLists to arrays, assigns
 * neighbor triangles to each vert, and recalculates normals. Handles normal
 * smoothing, and preserves sharp edges.
 *
 * see projectVerts() for setting screen positions (Zdepth) of verts
 * see sortTriangles() for Z depth sorting
 * see regenerateNormals() to calculate smoothed normals
 *
 * oct 2008: added funcs to handle groups
 * sep 2008: changed gluProject() params due to lwjgl 2.0 changes
 * jun 2006: add makeclone()
 */
public class GL_Mesh {

    public ArrayList<GL_Vertex> vertexData = new ArrayList<GL_Vertex>();

    public ArrayList<GL_Triangle> triangleData = new ArrayList<GL_Triangle>();

    public GL_Vertex[] vertices;

    public GL_Triangle[] triangles;

    public int numVertices = 0;

    public int numTriangles = 0;

    public String name = "";

    private float maxSmoothingAngle = 89f;

    private float cos_angle = (float) Math.cos(Math.toRadians(maxSmoothingAngle));

    public String materialLibeName = null;

    public GLMaterial[] materials = null;

    GL_Triangle[][] groupFaces = new GL_Triangle[1][];

    String[] groupNames = new String[] { "default" };

    String[] groupMaterialNames = new String[] { null };

    int currentGroup = 0;

    private FloatBuffer projectedVert = allocFloats(16);

    public GL_Mesh() {
    }

    /**
		 * the smoothing angle determines how smooth or faceted a model will appear.
		 * If the angle between the normals of two neighboring faces is greater than
		 * the smoothing angle then the faces will be smoothed (the vertex normals
		 * at the edge of the faces will be averaged.
		 * <PRE>
		 *               N1        example: the angle between face normals 1 and 2
		 *              |                   is 90 degrees.  If maxSmoothingAngle is 80
		 *         _____|___                these faces will have a hard edge.  If
		 *      face1       |___ N2         maxSmoothingAngle is 100 they will be smoothed.
		 *                  |
		 *                  |
		 *              face2
		 * </PRE>
		 *
		 * @param maxSmoothingAngle
		 */
    public void setSmoothingAngle(float maxSmoothingAngle) {
        this.maxSmoothingAngle = maxSmoothingAngle;
        cos_angle = (float) Math.cos(Math.toRadians(maxSmoothingAngle));
    }

    /**
		 * get the vertex for the given index (aka vertex ID)
		 * this assumes that rebuild() was run to build the vertices array from temporary vertexData
		 * @param id of vertex
		 * @return the vertex
		 */
    public GL_Vertex vertex(int id) {
        if (vertexData != null) {
            return (GL_Vertex) vertexData.get(id);
        } else {
            return vertices[id];
        }
    }

    public GL_Triangle triangle(int id) {
        if (triangleData != null) {
            return (GL_Triangle) triangleData.get(id);
        } else {
            return triangles[id];
        }
    }

    /**
		 * add vertex to arraylist.
		 * rebuild() will copy verts into an array for faster retrieval.
		 * tag each vert with an ID (its index into the vertex array)
		 * this is needed for makeClone() to correctly clone this mesh
		 * @param newVertex
		 */
    public void addVertex(GL_Vertex newVertex) {
        newVertex.ID = vertexData.size();
        vertexData.add(newVertex);
    }

    public void addVertex(float x, float y, float z) {
        addVertex(new GL_Vertex(x, y, z));
    }

    public void addTriangle(GL_Triangle newTriangle) {
        triangleData.add(newTriangle);
    }

    public void addTriangle(int v1, int v2, int v3) {
        addTriangle(vertex(v1), vertex(v2), vertex(v3));
    }

    public void addTriangle(GL_Vertex a, GL_Vertex b, GL_Vertex c) {
        addTriangle(new GL_Triangle(a, b, c));
    }

    public void removeVertex(GL_Vertex v) {
        vertexData.remove(v);
    }

    public void removeTriangle(GL_Triangle t) {
        triangleData.remove(t);
    }

    public void removeVertexAt(int pos) {
        vertexData.remove(pos);
    }

    public void removeTriangleAt(int pos) {
        triangleData.remove(pos);
    }

    /**
		 * Copy vertex and triangle data into arrays for faster
		 * access.  Find the neighbor triangles for each vertex.
		 * This data should not change once the mesh is loaded,
		 * so we call rebuild() only when the object is imported.
		 */
    public void rebuild() {
        if (vertexData == null || triangleData == null) {
            System.out.println("GL_Mesh.rebuild(): can't rebuild mesh after finalize() was run.");
            return;
        }
        numVertices = vertexData.size();
        vertices = new GL_Vertex[numVertices];
        for (int i = 0; i < numVertices; i++) {
            vertices[i] = vertex(i);
            vertices[i].ID = i;
            vertices[i].resetNeighbors();
        }
        GL_Triangle tri;
        numTriangles = triangleData.size();
        triangles = new GL_Triangle[numTriangles];
        for (int i = 0; i < numTriangles; i++) {
            triangles[i] = tri = (GL_Triangle) triangleData.get(i);
            tri.ID = i;
            tri.p1.addNeighborTri(tri);
            tri.p2.addNeighborTri(tri);
            tri.p3.addNeighborTri(tri);
        }
    }

    public void rebuild_OLD() {
        GL_Triangle tri;
        numVertices = vertexData.size();
        vertices = new GL_Vertex[numVertices];
        for (int i = 0; i < numVertices; i++) {
            vertices[i] = (GL_Vertex) vertexData.get(i);
        }
        numTriangles = triangleData.size();
        triangles = new GL_Triangle[numTriangles];
        for (int i = 0; i < numTriangles; i++) {
            triangles[i] = (GL_Triangle) triangleData.get(i);
            triangles[i].ID = i;
        }
        for (int i = 0; i < numVertices; i++) {
            vertices[i].ID = i;
            vertices[i].resetNeighbors();
        }
        for (int i = 0; i < numTriangles; i++) {
            tri = triangles[i];
            tri.p1.addNeighborTri(tri);
            tri.p2.addNeighborTri(tri);
            tri.p3.addNeighborTri(tri);
        }
    }

    /**
		 * remove tempoarary vert and triangle arraylists.  These lists are used
		 * to gather verts and triangles dynamically (ie. when loading or building a mesh
		 * algorithmically).  Once the mesh is complete then call finalize() to convert the
		 * arraylists to fixed-length arrays, and to free up redundant arraylist memory.
		 * CAN'T REBUILD AGAIN once finalize has been run!
		 */
    public void finalize() {
        if (vertexData == null || triangleData == null) {
            System.out.println("GL_Mesh.finalize(): looks like finalize() was already run.");
            return;
        }
        vertexData.clear();
        triangleData.clear();
        vertexData = null;
        triangleData = null;
        for (int i = 0; i < vertices.length; i++) {
            vertices[i].neighborTris.trimToSize();
        }
    }

    /**
         * For the given Vert in the given Triangle, make a list of triangles
         * that are neighbors to the given Triangle.  Only count as neighbors
         * those triangles that form a smooth surface with this triangle,
         * meaning the angle between this triangle and the neighbor triangle
         * is > 90 degrees (the actual min degrees value is in cos_angle).
         *
         * Requires that rebuild() has been run so that the vertex has
         * a list of neighbor triangles populated (see addNeighborTri()),
         * and the triangle face normals have been calculated. (see
         * GL_Triangle.regenerateNormal()).
         */
    public void registerSmoothNeighbors(ArrayList<GL_Triangle> neighborTris, GL_Vertex v, GL_Triangle t) {
        GL_Triangle neighborTri;
        for (int i = 0; i < v.neighborTris.size(); i++) {
            neighborTri = (GL_Triangle) v.neighborTris.get(i);
            if (GL_Triangle.onSameSurface(t, neighborTri, cos_angle)) {
                if (!neighborTris.contains(neighborTri)) {
                    neighborTris.add(neighborTri);
                }
            }
        }
        if (neighborTris.size() == 0) {
            neighborTris.add(t);
        }
    }

    /**
         * Recalculate normals for each vertex in each triangle.
         * This allows a vertex to have a different normal for each
         * triangle it's in (so we can have sharp edges or smooth surfaces).
         *
         * Requires that neighoring triangles have already been set.
         * @see rebuild(), registerNeighbors(), setSmoothingAngle()
         */
    public void regenerateNormals() {
        GL_Triangle tri;
        for (int i = 0; i < numTriangles; i++) {
            triangles[i].recalcFaceNormal();
        }
        for (int i = 0; i < numTriangles; i++) {
            tri = triangles[i];
            tri.resetNeighbors();
            registerSmoothNeighbors(tri.neighborsP1, tri.p1, tri);
            registerSmoothNeighbors(tri.neighborsP2, tri.p2, tri);
            registerSmoothNeighbors(tri.neighborsP3, tri.p3, tri);
        }
        for (int i = 0; i < numTriangles; i++) {
            tri = triangles[i];
            tri.norm1 = tri.recalcVertexNormal(tri.neighborsP1);
            tri.norm2 = tri.recalcVertexNormal(tri.neighborsP2);
            tri.norm3 = tri.recalcVertexNormal(tri.neighborsP3);
        }
    }

    /**
		 * Return minimum point in the mesh.
		 */
    public GL_Vector min() {
        if (numVertices == 0) return new GL_Vector(0f, 0f, 0f);
        float minX = vertices[0].pos.x;
        float minY = vertices[0].pos.y;
        float minZ = vertices[0].pos.z;
        for (int i = 0; i < numVertices; i++) {
            if (vertices[i].pos.x < minX) minX = vertices[i].pos.x;
            if (vertices[i].pos.y < minY) minY = vertices[i].pos.y;
            if (vertices[i].pos.z < minZ) minZ = vertices[i].pos.z;
        }
        return new GL_Vector(minX, minY, minZ);
    }

    /**
		 * Return maximum point in the mesh.
		 */
    public GL_Vector max() {
        if (numVertices == 0) return new GL_Vector(0f, 0f, 0f);
        float maxX = vertices[0].pos.x;
        float maxY = vertices[0].pos.y;
        float maxZ = vertices[0].pos.z;
        for (int i = 0; i < numVertices; i++) {
            if (vertices[i].pos.x > maxX) maxX = vertices[i].pos.x;
            if (vertices[i].pos.y > maxY) maxY = vertices[i].pos.y;
            if (vertices[i].pos.z > maxZ) maxZ = vertices[i].pos.z;
        }
        return new GL_Vector(maxX, maxY, maxZ);
    }

    /**
		 * Return the center point of the mesh.
		 */
    public GL_Vector getCenter() {
        GL_Vector max = max();
        GL_Vector min = min();
        return new GL_Vector((max.x + min.x) / 2f, (max.y + min.y) / 2f, (max.z + min.z) / 2f);
    }

    /**
		 * Returns the dimensions of this mesh.
		 */
    public GL_Vector getDimension() {
        GL_Vector max = max();
        GL_Vector min = min();
        return new GL_Vector(max.x - min.x, max.y - min.y, max.z - min.z);
    }

    /**
		 * return the vertex array
		 */
    public GL_Vertex[] getVertexArray() {
        return vertices;
    }

    /**
	     * "Project" the verts to find their screen coords.  Store these
	     * screen coords in the vertex.posS vector.
	     * GLU.gluProject() will create screen x,y coords and a z depth value between 0 and 1.
	     *
	     *	NOTE: sept2008: lwjgl 2.0 changed args for GLU.gluProject()
	     *
	     * @see  sortTriangles()
	     */
    public void projectVerts(GL_Mesh obj, FloatBuffer modelMatrix, FloatBuffer projectionMatrix, IntBuffer viewport) {
        GL_Vertex v;
        for (int i = 0; i < obj.vertices.length; i++) {
            v = obj.vertices[i];
            GLU.gluProject(v.pos.x, v.pos.y, v.pos.z, modelMatrix, projectionMatrix, viewport, projectedVert);
            v.posS.x = projectedVert.get(0);
            v.posS.y = projectedVert.get(1);
            v.posS.z = projectedVert.get(2);
        }
        calcZDepths();
    }

    /**
	     * Calculate the average Z depth of each triangle.  Used by
	     * sortTriangles() below.
	     */
    public void calcZDepths() {
        for (int i = 0; i < triangles.length; i++) {
            triangles[i].calcZdepth();
        }
    }

    /**
	     * Z sort the triangles of this mesh.  Call projectVerts()
	     * and calcZDepths() first to set correct Z depth of all verts
	     * and triangles.
	     */
    public void sortTriangles() {
        if (triangles != null) {
            triangles = sortTriangles(triangles, 0, triangles.length - 1);
        }
    }

    /**
	     * Z sort the given triangle array.  Call projectVerts() first
	     * to set correct Z depth of all verts and triangles.
	     */
    public GL_Triangle[] sortTriangles(GL_Triangle[] tri, int L, int R) {
        float m = (tri[L].Zdepth + tri[R].Zdepth) / 2;
        int i = L;
        int j = R;
        GL_Triangle temp;
        do {
            while (tri[i].Zdepth > m) i++;
            while (tri[j].Zdepth < m) j--;
            if (i <= j) {
                temp = tri[i];
                tri[i] = tri[j];
                tri[j] = temp;
                i++;
                j--;
            }
        } while (j >= i);
        if (L < j) sortTriangles(tri, L, j);
        if (R > i) sortTriangles(tri, i, R);
        return tri;
    }

    /**
	     * Return the front-most triangle that contains the given screen x,y position,
	     * or null if no triangle contains the x,y position.  Requires that projectVerts()
	     * has been run to populate the vertex screen positions.
	     *
	     * @param x     cursor x  (screen coordinates)
	     * @param y     cursor y  (screen coordinates)
	     * @return      the triangle at the cursor x,y
	     * @see projectVerts()
	     */
    public GL_Triangle getTriangle(float x, float y) {
        return getTriangle(x, y, triangles);
    }

    /**
	     * Given a screen point and a triangles array, return the front-most triangle that
	     * contains the given screen x,y position, or null if no triangle contains the
	     * x,y position.  Requires that projectVerts() has been run to populate the
	     * vertex screen positions.
	     *
	     * @param x     cursor x  (screen coordinates)
	     * @param y     cursor y  (screen coordinates)
	     * @return      the triangle at the cursor x,y
	     * @see projectVerts()
	     */
    public static GL_Triangle getTriangle(float x, float y, GL_Triangle[] _triangles) {
        ArrayList<GL_Triangle> candidates = new ArrayList<GL_Triangle>();
        GL_Triangle closestT = null;
        GL_Triangle t;
        float minZ = 100000;
        for (int i = 0; i < _triangles.length; i++) {
            t = _triangles[i];
            if (pointInTriangle(x, y, t.p1.posS.x, t.p1.posS.y, t.p2.posS.x, t.p2.posS.y, t.p3.posS.x, t.p3.posS.y)) {
                candidates.add(t);
            }
        }
        for (int j = 0; j < candidates.size(); j++) {
            if (((GL_Triangle) (candidates.get(j))).Zdepth < minZ) {
                closestT = (GL_Triangle) (candidates.get(j));
                minZ = closestT.Zdepth;
            }
        }
        return closestT;
    }

    /**
	     * Return true if the point px,py is inside the triangle created
	     * by the three triangle coordinates.
	     */
    public static boolean pointInTriangle(float px, float py, float x1, float y1, float x2, float y2, float x3, float y3) {
        float b0 = ((x2 - x1) * (y3 - y1) - (x3 - x1) * (y2 - y1));
        float b1 = ((x2 - px) * (y3 - py) - (x3 - px) * (y2 - py)) / b0;
        float b2 = ((x3 - px) * (y1 - py) - (x1 - px) * (y3 - py)) / b0;
        float b3 = ((x1 - px) * (y2 - py) - (x2 - px) * (y1 - py)) / b0;
        if (b1 > 0 && b2 > 0 && b3 > 0) {
            return true;
        }
        return false;
    }

    /**
	     * return a copy of this mesh.
	     * @return the cloned mesh
	     */
    public GL_Mesh makeClone() {
        GL_Mesh clone = new GL_Mesh();
        GL_Triangle ct;
        clone.vertexData.ensureCapacity(vertices.length);
        clone.triangleData.ensureCapacity(triangles.length);
        for (int i = 0; i < vertices.length; i++) {
            clone.addVertex(vertices[i].makeClone());
        }
        for (int i = 0; i < triangles.length; i++) {
            clone.addTriangle((ct = triangles[i].makeClone()));
            ct.p1 = clone.vertex(ct.p1.ID);
            ct.p2 = clone.vertex(ct.p2.ID);
            ct.p3 = clone.vertex(ct.p3.ID);
        }
        clone.name = name + " [cloned]";
        clone.rebuild();
        return clone;
    }

    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("<object id=" + name + ">\r\n");
        for (int i = 0; i < numVertices; i++) {
            buffer.append(vertices[i].toString());
        }
        return buffer.toString();
    }

    public static final int SIZE_FLOAT = 4;

    public static FloatBuffer allocFloats(int howmany) {
        FloatBuffer fb = ByteBuffer.allocateDirect(howmany * SIZE_FLOAT).order(ByteOrder.nativeOrder()).asFloatBuffer();
        return fb;
    }

    public int numGroups() {
        return groupFaces.length;
    }

    public String getGroupName(int g) {
        return groupNames[g];
    }

    public String getGroupMaterialName(int g) {
        return groupMaterialNames[g];
    }

    public GL_Triangle[] getGroupFaces(int g) {
        return groupFaces[g];
    }

    public void selectGroup(int g) {
        if (g > groupFaces.length - 1) {
            g = groupFaces.length - 1;
        }
        currentGroup = g;
    }

    /**
         * Allocate arrays to hold <num> groups.  The GL_OBJ_Importer calls this
         * once, after it has loaded the OBJ and knows how many groups it has.
         *
         * Use initGroups() to allocate triangle arrays for each group.
         *
         * @param num  number of groups to allocate
         * @see initGroup()
         */
    public void makeGroups(int num) {
        groupFaces = new GL_Triangle[num][];
        groupNames = new String[num];
        groupMaterialNames = new String[num];
    }

    /**
         * allocate triangle array for a group.  Also set name and material name.
         * @param groupNum      number of group to init
         * @param name            group name
         * @param materialName     group's material
         * @param numTriangles     number of triangles in the group
         * @see makeGroups()
         */
    public void initGroup(int groupNum, String name, String materialName, int numTriangles) {
        groupFaces[groupNum] = new GL_Triangle[numTriangles];
        groupNames[groupNum] = name;
        groupMaterialNames[groupNum] = materialName;
        currentGroup = groupNum;
    }

    /**
         * add triangle to given group.  used by Importer to load mesh groups.
         * Groups had to be allocated first by makeGroups() and inited with
         * initGroup().
         * @param newTriangle
         * @param groupNum
         * @param triangleNum
         * @see makeGroups()
         * @see setGroup()
         */
    public void addTriangle(GL_Triangle newTriangle, int groupNum, int triangleNum) {
        newTriangle.ID = triangleData.size();
        newTriangle.groupID = groupNum;
        triangleData.add(newTriangle);
        groupFaces[groupNum][triangleNum] = newTriangle;
    }
}

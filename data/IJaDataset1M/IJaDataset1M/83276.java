package gleem;

import java.util.*;
import gleem.linalg.*;

/** A 2x2 square in the X-Y plane, centered about the origin, with the
    normal pointing in the +Z direction. */
public class ManipPartSquare extends ManipPartTriBased {

    private static final Vec3f[] vertices = { new Vec3f(-1, 1, 0), new Vec3f(-1, -1, 0), new Vec3f(1, -1, 0), new Vec3f(1, 1, 0) };

    private static final int[] vertexIndices = { 0, 1, 2, 0, 2, 3 };

    private static Vec3f[] normals = null;

    private static int[] normalIndices = null;

    public ManipPartSquare() {
        super();
        if (normals == null) {
            NormalCalc.NormalInfo normInfo = NormalCalc.computeFacetedNormals(vertices, vertexIndices, true);
            normals = normInfo.normals;
            normalIndices = normInfo.normalIndices;
        }
        setVertices(vertices);
        setVertexIndices(vertexIndices);
        setNormals(normals);
        setNormalIndices(normalIndices);
    }
}

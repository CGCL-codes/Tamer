package net.sf.gamine.render;

import android.util.*;
import net.sf.gamine.common.*;

/**
 * This is a Mesh subclass for easily creating spherical meshes.  The resolution of the mesh can be controlled by
 * specifying the number of lines of latitude and longitude.
 */
public class Sphere extends Mesh {

    /**
   * Create a new spherical mesh.
   *
   * @param radius        the radius of the sphere
   * @param numLatitude   the number of lines of latitude the mesh should have
   * @param numLongitude  the number of lines of longitude the mesh should have
   */
    public Sphere(float radius, int numLatitude, int numLongitude) {
        int numVertices = numLatitude * numLongitude + 2;
        Float3 vertexPositions[] = new Float3[numVertices];
        Float3 vertexNormals[] = new Float3[numVertices];
        int index = 0;
        vertexPositions[index] = new Float3(0, radius, 0);
        vertexNormals[index++] = new Float3(0, 1, 0);
        for (int i = 0; i < numLatitude; i++) {
            float phi = (float) (((i + 1) * Math.PI) / (numLatitude + 1));
            float sphi = FloatMath.sin(phi);
            float cphi = FloatMath.cos(phi);
            float y = radius * cphi;
            float r = radius * sphi;
            for (int j = 0; j < numLongitude; j++) {
                float theta = (float) ((j * 2 * Math.PI) / numLongitude);
                float stheta = FloatMath.sin(theta);
                float ctheta = FloatMath.cos(theta);
                vertexPositions[index] = new Float3(r * ctheta, y, r * stheta);
                vertexNormals[index++] = new Float3(sphi * ctheta, cphi, sphi * stheta);
            }
        }
        vertexPositions[index] = new Float3(0, -radius, 0);
        vertexNormals[index] = new Float3(0, -1, 0);
        int numFaces = (numLatitude - 1) * numLongitude * 2 + 2 * numLongitude;
        Int3 faceVertexIndices[] = new Int3[numFaces];
        index = 0;
        for (int i = 1; i < numLongitude; i++) faceVertexIndices[index++] = new Int3(0, i + 1, i);
        faceVertexIndices[index++] = new Int3(0, 1, numLongitude);
        for (int i = 1; i < numLatitude; i++) {
            int base = (i - 1) * numLongitude + 1;
            for (int j = 0; j < numLongitude; j++) {
                int v1 = base + j;
                int v2 = (j == numLongitude - 1 ? base : v1 + 1);
                int v3 = v1 + numLongitude;
                int v4 = v2 + numLongitude;
                faceVertexIndices[index++] = new Int3(v1, v4, v3);
                faceVertexIndices[index++] = new Int3(v1, v2, v4);
            }
        }
        int first = (numLatitude - 1) * numLongitude + 1;
        int last = numVertices - 1;
        for (int i = first; i < last - 1; i++) faceVertexIndices[index++] = new Int3(i, i + 1, last);
        faceVertexIndices[index] = new Int3(last - 1, first, last);
        initialize(vertexPositions, vertexNormals, faceVertexIndices);
    }
}

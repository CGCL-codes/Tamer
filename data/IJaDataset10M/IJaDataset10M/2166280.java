package org.xith3d.occluder;

import org.openmali.spatial.bodies.Plane;

/**
 * An occluder face contains the connectivity information between itself and the
 * other faces. In addition it stores the indexes into the underlying vertices
 * and the vertex normals at each vertex.
 * 
 * @author David Yazel
 */
public class OccluderFace {

    /**
     * Index of each Vertex within an object, that makes up the
     * triangle of this face
     */
    public int vertexIndices[] = new int[3];

    /**
     * Equation of a plane, that contains this triangle
     */
    public Plane planeEquation;

    /**
     * Index of each face, that neighbours this one within the object
     */
    public int neighbourIndices[] = new int[3];

    /**
     * Is the face visible by the Light?
     */
    public boolean visible;

    public OccluderFace() {
        neighbourIndices[0] = -1;
        neighbourIndices[1] = -1;
        neighbourIndices[2] = -1;
    }
}

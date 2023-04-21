package bitWave.geometry;

import bitWave.linAlg.Vec4;

/**
 * A line connecting two vertices.
 * 
 * @author fw
 * 
 */
public interface Line {

    /**
     * Returns the Vertex at the given index within the line.
     * @param index
     * @return The Vertex at the given index.
     */
    Vertex getVertexByIndex(int index);

    Vec4 getVertexPositionByIndex(int index);

    int getVertexIndexByIndex(int index);

    /**
     * Returns the number of vertices in the list.
     * @return The vertex count.
     */
    int getVertexCount();

    /**
     * Adds the given Vertex to the line.
     * @param vertex The vertex to include in the line.
     */
    void addPoint(Vertex vertex);

    void addPoint(int vertexIndex);

    /**
     * Returns the index of the line within the mesh.
     * @return The line index.
     */
    int getIndex();

    /** 
     * Returns a reference to the mesh that owns the line.
     * @return The line's mesh.
     */
    Mesh getMesh();

    /**
     * Removes all points from the line.
     */
    void clear();

    /**
     * Sets the capacity of the line to the given number of vertices.
     * @param capacity The suggested capacity.
     */
    void setCapacity(int capacity);
}

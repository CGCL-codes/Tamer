package bitWave.geometry.impl;

import java.io.Serializable;
import bitWave.geometry.Line;
import bitWave.geometry.Mesh;
import bitWave.geometry.Vertex;
import bitWave.linAlg.Vec4;

public class LineImpl implements Line, Serializable {

    private static final long serialVersionUID = 0;

    /** The mesh to which this line belongs. */
    private final Mesh m_mesh;

    /** The index of the line within the mesh. */
    private int m_lineIndex;

    /** The vertex indices within the mesh. */
    private int[] m_vertexIndices = new int[8];

    private int m_vertexCount = 0;

    public LineImpl(Mesh mesh, int lineIndex, int... vertexIndices) {
        this.m_mesh = mesh;
        this.m_lineIndex = lineIndex;
        for (int i = 0; i < vertexIndices.length; i++) addPoint(vertexIndices[i]);
    }

    public Vertex getVertexByIndex(int index) {
        return this.m_mesh.getVertexByIndex(this.m_vertexIndices[index]);
    }

    public void addPoint(int vertexIndex) {
        if (this.m_vertexCount == this.m_vertexIndices.length) {
            resize(this.m_vertexCount * 2);
        }
        this.m_vertexIndices[this.m_vertexCount] = vertexIndex;
        this.m_vertexCount++;
    }

    public void addPoint(Vec4 position) {
        int index = this.m_mesh.addVertex(position);
        addPoint(index);
    }

    public void addPoint(Vertex vertex) {
        if (vertex.getMesh() == this.m_mesh) addPoint(vertex.getIndex()); else {
            addPoint(vertex.getPosition());
        }
    }

    public int getIndex() {
        return this.m_lineIndex;
    }

    public Mesh getMesh() {
        return this.m_mesh;
    }

    public int getVertexCount() {
        return this.m_vertexCount;
    }

    public void clear() {
        this.m_vertexCount = 0;
    }

    protected void resize(int capacity) {
        int[] old = this.m_vertexIndices;
        this.m_vertexIndices = new int[capacity];
        for (int i = 0; i < this.m_vertexCount; i++) this.m_vertexIndices[i] = old[i];
    }

    public void setCapacity(int capacity) {
        resize(Math.max(capacity, this.m_vertexIndices.length));
    }

    public int getVertexIndexByIndex(int index) {
        return this.m_vertexIndices[index];
    }

    public Vec4 getVertexPositionByIndex(int index) {
        return this.m_mesh.getVertexPositionByIndex(this.m_vertexIndices[index]);
    }
}

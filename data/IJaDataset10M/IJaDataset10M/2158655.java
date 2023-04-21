package com.selcukcihan.android.xface.xengine;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.FloatBuffer;
import java.util.Vector;
import com.selcukcihan.android.xface.xmath.Vector3;

public abstract class Geometry extends NamedObj {

    public Vector3Buffer m_vertices = null;

    public Vertex2DBuffer m_texCoords = null;

    public Vector3Buffer m_normals = null;

    Geometry(final String name) {
        super(name);
        m_vertices = new Vector3Buffer();
        m_texCoords = new Vertex2DBuffer();
        m_normals = new Vector3Buffer();
    }

    public Vector3 computeCentroid() {
        Vector3 mean = new Vector3(0, 0, 0);
        Vector3 bMin = new Vector3(1000000, 1000000, 1000000);
        Vector3 bMax = new Vector3(-1000000, -1000000, -1000000);
        m_vertices.rewind();
        while (m_vertices.hasRemaining()) {
            Vector3 curVertex = m_vertices.get();
            if (bMin.x > curVertex.x) bMin.x = curVertex.x;
            if (bMin.y > curVertex.y) bMin.y = curVertex.y;
            if (bMin.z > curVertex.z) bMin.z = curVertex.z;
            if (bMax.x < curVertex.x) bMax.x = curVertex.x;
            if (bMax.y < curVertex.y) bMax.y = curVertex.y;
            if (bMax.z < curVertex.z) bMax.z = curVertex.z;
        }
        mean = (bMax.opAdd(bMin)).opDivideScalar(2.f);
        return mean;
    }

    public Vector3 computeMeanOfVertices() {
        Vector3 mean = new Vector3(0, 0, 0);
        m_vertices.rewind();
        while (m_vertices.hasRemaining()) {
            Vector3 curVertex = m_vertices.get();
            mean = mean.opAdd(curVertex.opDivideScalar((float) m_vertices.size()));
        }
        return mean;
    }

    public void subtractMeanFromVertices(final Vector3 mean) {
        for (int i = 0; i < m_vertices.size(); i++) {
            Vector3 curVertex = m_vertices.get(i);
            m_vertices.put(i, curVertex.opSubtract(mean));
        }
    }

    public void readBinary(RandomAccessFile fp) throws IOException {
        int sz = BinaryModelBatchLoader.readUInt(fp);
        if (sz == 0) return;
        m_vertices = new Vector3Buffer(sz);
        for (int i = 0; i < sz; i++) {
            Vector3 v = new Vector3();
            v.x = Float.intBitsToFloat(BinaryModelBatchLoader.readUInt(fp));
            v.y = Float.intBitsToFloat(BinaryModelBatchLoader.readUInt(fp));
            v.z = Float.intBitsToFloat(BinaryModelBatchLoader.readUInt(fp));
            m_vertices.put(v);
        }
        m_vertices.rewind();
        sz = BinaryModelBatchLoader.readUInt(fp);
        m_normals = new Vector3Buffer(sz);
        for (int i = 0; i < sz; i++) {
            Vector3 v = new Vector3();
            v.x = Float.intBitsToFloat(BinaryModelBatchLoader.readUInt(fp));
            v.y = Float.intBitsToFloat(BinaryModelBatchLoader.readUInt(fp));
            v.z = Float.intBitsToFloat(BinaryModelBatchLoader.readUInt(fp));
            m_normals.put(v);
        }
        m_normals.rewind();
        sz = BinaryModelBatchLoader.readUInt(fp);
        m_texCoords = new Vertex2DBuffer(sz);
        for (int i = 0; i < sz; i++) {
            Vertex2D v = new Vertex2D();
            v.x = Float.intBitsToFloat(BinaryModelBatchLoader.readUInt(fp));
            v.y = Float.intBitsToFloat(BinaryModelBatchLoader.readUInt(fp));
            m_texCoords.put(v);
        }
        m_texCoords.rewind();
    }

    public void writeBinary(RandomAccessFile fp) throws IOException {
        fp.writeInt(m_vertices.size());
        fp.write(m_vertices.byteArray());
        m_vertices.rewind();
        fp.writeInt(m_normals.size());
        fp.write(m_normals.byteArray());
        m_normals.rewind();
        fp.writeInt(m_texCoords.size());
        fp.write(m_texCoords.byteArray());
        m_texCoords.rewind();
    }

    public Geometry copyFrom(final Geometry rhs) {
        if (this == rhs) return this;
        m_vertices = new Vector3Buffer(rhs.m_vertices);
        m_normals = new Vector3Buffer(rhs.m_normals);
        m_texCoords = new Vertex2DBuffer(rhs.m_texCoords);
        return this;
    }

    public void setVertices(final Vector3[] pVert, int size) {
        throw new UnsupportedOperationException();
    }

    public void setVertices(final Vector<Vector3> vertices) {
        if (m_vertices.size() == vertices.size()) {
            for (int i = 0; i < vertices.size(); i++) {
                m_vertices.put(i, vertices.get(i));
            }
        } else m_vertices = new Vector3Buffer(vertices);
    }

    public void setNormals(final Vector3[] pNorm, int size) {
        throw new UnsupportedOperationException();
    }

    public void setNormals(final Vector<Vector3> normals) {
        if (m_normals.size() == normals.size()) {
            for (int i = 0; i < normals.size(); i++) {
                m_normals.put(i, normals.get(i));
            }
        } else m_normals = new Vector3Buffer(normals);
    }

    public void setTexCoords(final Vertex2D pTex, int size) {
        throw new UnsupportedOperationException();
    }

    public void setTexCoords(final Vector<Vertex2D> texCoords) {
        if (m_texCoords.size() == texCoords.size()) {
            for (int i = 0; i < texCoords.size(); i++) {
                m_texCoords.put(texCoords.get(i));
            }
        } else m_texCoords = new Vertex2DBuffer(texCoords);
    }

    public Vector3Buffer getVertices() {
        m_vertices.rewind();
        return m_vertices;
    }

    public Vector3Buffer getNormals() {
        m_normals.rewind();
        return m_normals;
    }

    public FloatBuffer getNormalsGL() {
        m_normals.rewind();
        return m_normals.floatBuffer();
    }

    public Vertex2DBuffer getTexCoords() {
        m_texCoords.rewind();
        return m_texCoords;
    }

    public FloatBuffer getTexCoordsGL() {
        m_texCoords.rewind();
        return m_texCoords.floatBuffer();
    }

    public int getVertexCount() {
        return m_vertices.size();
    }

    public static Vector3 computeFaceNormal(final Vector3 p1, final Vector3 p2, final Vector3 p3) {
        Vector3 u = new Vector3();
        Vector3 v = new Vector3();
        u.x = p2.x - p1.x;
        u.y = p2.y - p1.y;
        u.z = p2.z - p1.z;
        v.x = p3.x - p1.x;
        v.y = p3.y - p1.y;
        v.z = p3.z - p1.z;
        Vector3 facenormal = u.unitCross(v);
        return facenormal;
    }
}

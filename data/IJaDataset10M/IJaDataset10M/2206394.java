package xmage.turbine.util;

import xmage.math.Triangle3d;
import xmage.turbine.TriMesh;
import java.nio.DoubleBuffer;
import java.nio.IntBuffer;

public class TriMeshIteratorHelper {

    public TriMeshIteratorHelper() {
    }

    private TriMesh mesh = null;

    private IntBuffer tris = null;

    private DoubleBuffer verts = null;

    private int triNum = 0;

    private int idx = 0;

    public void setMesh(TriMesh mesh) {
        if (mesh == null) {
            throw new IllegalArgumentException("Mesh may not be null");
        }
        this.mesh = mesh;
        tris = mesh.getTriangles();
        verts = mesh.getVertices();
        if (tris == null || verts == null) {
            throw new IllegalArgumentException("Triangle and vertex buffers may not be null. " + "Maybe you need to triangulate dynamic mesh?");
        }
        reset();
    }

    private final Triangle3d tri3 = new Triangle3d();

    private final Tri tri = new Tri();

    public Triangle3d getTriangle3d() {
        return triNum == 0 ? null : tri3;
    }

    public Tri getTri() {
        return triNum == 0 ? null : tri;
    }

    public boolean hasNext() {
        return triNum < mesh.getNumTriangles();
    }

    public Triangle3d nextTriangle3d() {
        triNum++;
        tris.position(idx);
        verts.position(tris.get() * 3);
        tri3.a.set(verts.get(), verts.get(), verts.get());
        verts.position(tris.get() * 3);
        tri3.b.set(verts.get(), verts.get(), verts.get());
        verts.position(tris.get() * 3);
        tri3.c.set(verts.get(), verts.get(), verts.get());
        tri3.set(tri3.a, tri3.b, tri3.c);
        idx += 3;
        return tri3;
    }

    public Tri nextTri() {
        triNum++;
        tris.position(idx);
        idx += 3;
        tri.idx = idx;
        tri.v1 = tris.get();
        tri.v2 = tris.get();
        tri.v3 = tris.get();
        return tri;
    }

    public void reset() {
        triNum = 0;
        idx = 0;
    }
}

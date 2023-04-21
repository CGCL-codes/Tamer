package xmage.turbine.util;

import xmage.math.Triangle3d;
import xmage.turbine.TriMesh;
import huf.data.Iterator;

public class Triangle3dIterator implements Iterator<Triangle3d> {

    private final TriMeshIteratorHelper helper = new TriMeshIteratorHelper();

    public Triangle3dIterator() {
    }

    public Triangle3dIterator(TriMesh mesh) {
        helper.setMesh(mesh);
    }

    public void setMesh(TriMesh mesh) {
        helper.setMesh(mesh);
    }

    public Triangle3d get() {
        return helper.getTriangle3d();
    }

    public boolean hasNext() {
        return helper.hasNext();
    }

    public Triangle3d next() {
        return helper.nextTriangle3d();
    }

    public void reset() {
        helper.reset();
    }

    public Iterator<Triangle3d> iterator() {
        return this;
    }

    public void remove() {
        throw new UnsupportedOperationException();
    }
}

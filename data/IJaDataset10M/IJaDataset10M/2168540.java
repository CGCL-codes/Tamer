package org.ode4j.cpp.internal;

import org.cpp4j.java.DoubleArray;
import org.cpp4j.java.RefInt;
import org.ode4j.math.DVector3;
import org.ode4j.ode.DGeom;
import org.ode4j.ode.DSpace;
import org.ode4j.ode.DTriMesh;
import org.ode4j.ode.DTriMeshData;
import org.ode4j.ode.OdeHelper;
import org.ode4j.ode.DTriMesh.DTriArrayCallback;
import org.ode4j.ode.DTriMesh.DTriCallback;
import org.ode4j.ode.DTriMesh.DTriRayCallback;
import org.ode4j.ode.internal.DxTriMeshData;
import org.ode4j.ode.internal.Common.DMatrix4;

/**
 * TriMesh code by Erwin de Vries.
 *
 * Trimesh data.
 * This is where the actual vertexdata (pointers), and BV tree is stored.
 * Vertices should be single precision!
 * This should be more sophisticated, so that the user can easyly implement
 * another collision library, but this is a lot of work, and also costs some
 * performance because some data has to be copied.
 */
public class ApiCppCollisionTrimesh extends ApiCppTimer {

    public static DTriMeshData dGeomTriMeshDataCreate() {
        return OdeHelper.createTriMeshData();
    }

    public static void dGeomTriMeshDataDestroy(DTriMeshData g) {
        g.destroy();
    }

    /** @deprecated TZ: find a better name. */
    enum TRIMESH1 {

        TRIMESH_FACE_NORMALS
    }

    ;

    void dGeomTriMeshDataSet(DTriMeshData g, int data_id, Object in_data) {
        throw new UnsupportedOperationException();
    }

    Object dGeomTriMeshDataGet(DTriMeshData g, int data_id) {
        throw new UnsupportedOperationException();
    }

    void dGeomTriMeshSetLastTransform(DGeom g, DMatrix4 last_trans) {
        throw new UnsupportedOperationException();
    }

    public static void dGeomTriMeshSetLastTransform(DTriMesh g, DoubleArray last_trans) {
        throw new UnsupportedOperationException();
    }

    double[] dGeomTriMeshGetLastTransform(DGeom g) {
        throw new UnsupportedOperationException();
    }

    public static void dGeomTriMeshDataBuildSingle(DTriMeshData g, final float[] Vertices, int VertexStride, int VertexCount, final int[] Indices, int IndexCount, int TriStride) {
        ((DxTriMeshData) g).build(Vertices, Indices);
    }

    void dGeomTriMeshDataBuildSingle1(DTriMeshData g, final double[] Vertices, int VertexStride, int VertexCount, final int[] Indices, int IndexCount, int TriStride, final int[] Normals) {
        throw new UnsupportedOperationException();
    }

    void dGeomTriMeshDataBuildDouble(DTriMeshData g, final double[] Vertices, int VertexStride, int VertexCount, final int[] Indices, int IndexCount, int TriStride) {
        throw new UnsupportedOperationException();
    }

    void dGeomTriMeshDataBuildDouble1(DTriMeshData g, final double[] Vertices, int VertexStride, int VertexCount, final int[] Indices, int IndexCount, int TriStride, final int[] Normals) {
        throw new UnsupportedOperationException();
    }

    void dGeomTriMeshDataBuildSimple(DTriMeshData g, final double[] Vertices, int VertexCount, final int[] Indices, int IndexCount) {
        throw new UnsupportedOperationException();
    }

    void dGeomTriMeshDataBuildSimple1(DTriMeshData g, final double[] Vertices, int VertexCount, final int[] Indices, int IndexCount, final int[] Normals) {
        throw new UnsupportedOperationException();
    }

    void dGeomTriMeshDataPreprocess(DTriMeshData g) {
        throw new UnsupportedOperationException();
    }

    void dGeomTriMeshDataGetBuffer(DTriMeshData g, byte[][] buf, RefInt bufLen) {
        throw new UnsupportedOperationException();
    }

    void dGeomTriMeshDataSetBuffer(DTriMeshData g, byte[] buf) {
        throw new UnsupportedOperationException();
    }

    void dGeomTriMeshSetCallback(DGeom g, DTriCallback Callback) {
        throw new UnsupportedOperationException();
    }

    DTriCallback dGeomTriMeshGetCallback(DGeom g) {
        throw new UnsupportedOperationException();
    }

    void dGeomTriMeshSetArrayCallback(DGeom g, DTriArrayCallback ArrayCallback) {
        throw new UnsupportedOperationException();
    }

    DTriArrayCallback dGeomTriMeshGetArrayCallback(DGeom g) {
        throw new UnsupportedOperationException();
    }

    void dGeomTriMeshSetRayCallback(DTriMesh g, DTriRayCallback Callback) {
        throw new UnsupportedOperationException();
    }

    DTriRayCallback dGeomTriMeshGetRayCallback(DTriMesh g) {
        throw new UnsupportedOperationException();
    }

    private interface DTriTriMergeCallback {

        int callback(DTriMesh TriMesh, int FirstTriangleIndex, int SecondTriangleIndex);
    }

    void dGeomTriMeshSetTriMergeCallback(DTriMesh g, DTriTriMergeCallback Callback) {
        throw new UnsupportedOperationException();
    }

    DTriTriMergeCallback dGeomTriMeshGetTriMergeCallback(DTriMesh g) {
        throw new UnsupportedOperationException();
    }

    public static DTriMesh dCreateTriMesh(DSpace space, DTriMeshData Data, DTriCallback Callback, DTriArrayCallback ArrayCallback, DTriRayCallback RayCallback) {
        return OdeHelper.createTriMesh(space, Data, Callback, ArrayCallback, RayCallback);
    }

    void dGeomTriMeshSetData(DTriMesh g, DTriMeshData Data) {
        throw new UnsupportedOperationException();
    }

    DTriMeshData dGeomTriMeshGetData(DTriMesh g) {
        throw new UnsupportedOperationException();
    }

    void dGeomTriMeshEnableTC(DTriMesh g, int geomClass, int enable) {
        throw new UnsupportedOperationException();
    }

    int dGeomTriMeshIsTCEnabled(DTriMesh g, int geomClass) {
        throw new UnsupportedOperationException();
    }

    void dGeomTriMeshClearTCCache(DTriMesh g) {
        throw new UnsupportedOperationException();
    }

    DTriMeshData dGeomTriMeshGetTriMeshDataID(DTriMesh g) {
        throw new UnsupportedOperationException();
    }

    void dGeomTriMeshGetTriangle(DTriMesh g, int Index, DVector3 v0, DVector3 v1, DVector3 v2) {
        throw new UnsupportedOperationException();
    }

    void dGeomTriMeshGetPoint(DTriMesh g, int Index, double u, double v, DVector3 Out) {
        throw new UnsupportedOperationException();
    }

    int dGeomTriMeshGetTriangleCount(DTriMesh g) {
        throw new UnsupportedOperationException();
    }

    void dGeomTriMeshDataUpdate(DTriMeshData g) {
        throw new UnsupportedOperationException();
    }
}

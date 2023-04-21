package com.selcukcihan.xfacej.xmath;

import com.selcukcihan.xfacej.xengine.IndexedFaceSet;
import com.selcukcihan.xfacej.xengine.IntegerBuffer;
import com.selcukcihan.xfacej.xengine.Vector3Buffer;

public class XMath {

    private static final double EPSILON = 0.0000000001;

    private static final void CROSS(double[] dest, double[] v1, double[] v2) {
        dest[0] = v1[1] * v2[2] - v1[2] * v2[1];
        dest[1] = v1[2] * v2[0] - v1[0] * v2[2];
        dest[2] = v1[0] * v2[1] - v1[1] * v2[0];
    }

    private static final double DOT(double[] v1, double[] v2) {
        return (v1[0] * v2[0] + v1[1] * v2[1] + v1[2] * v2[2]);
    }

    private static final void SUB(double[] dest, double[] v1, double[] v2) {
        dest[0] = v1[0] - v2[0];
        dest[1] = v1[1] - v2[1];
        dest[2] = v1[2] - v2[2];
    }

    public static final float lengthSqrRayPnt3(final Ray3 ray, final Vector3 pnt) {
        float dist;
        Vector3 d = new Vector3(ray.getDirection());
        Vector3 o = new Vector3(ray.getOrigin());
        float t0 = d.dot(pnt.opSubtract(o)) / d.dot(d);
        if (t0 <= 0) dist = (pnt.opSubtract(o)).lengthSqr(); else dist = (pnt.opSubtract(o.opAdd(d.opMultiplyScalar(t0)))).lengthSqr();
        return dist;
    }

    public boolean intersectMeshRay3(final IndexedFaceSet pMesh, final Ray3 ray, int[] index) {
        Vector3Buffer pVert = pMesh.getVertices();
        IntegerBuffer pInd = pMesh.getIndices();
        double[] t = new double[1];
        double[] u = new double[1];
        double[] v = new double[1];
        for (int i = 0; i < pMesh.getIndexCount(); i += 3) {
            Vector3 a = pVert.get(pInd.get(i));
            Vector3 b = pVert.get(pInd.get(i + 1));
            Vector3 c = pVert.get(pInd.get(i + 2));
            if ((c.opSubtract(b)).cross(c.opSubtract(a)).dot(ray.getDirection()) < 0) continue;
            Vector3 o = ray.getOrigin();
            Vector3 d = ray.getDirection();
            double[] orig = { o.x, o.y, o.z };
            double[] dir = { d.x, d.y, d.z };
            double[] vert0 = { a.x, a.y, a.z };
            double[] vert1 = { b.x, b.y, b.z };
            double[] vert2 = { c.x, c.y, c.z };
            float dist, distmin = 1000000;
            if (intersect_triangle(orig, dir, vert2, vert0, vert1, t, u, v) != 0) {
                index[0] = pInd.get(i);
                for (int k = 0; k < 3; ++k) {
                    dist = lengthSqrRayPnt3(ray, pVert.get(pInd.get(i + k)));
                    if (dist < distmin) {
                        distmin = dist;
                        index[0] = pInd.get(i + k);
                    }
                }
                return true;
            }
        }
        return false;
    }

    public boolean intersectMeshRay3(final IndexedFaceSet pMesh, final Ray3 ray, Vector3 pnt) {
        Vector3Buffer pVert = pMesh.getVertices();
        IntegerBuffer pInd = pMesh.getIndices();
        double[] t = new double[1];
        double[] u = new double[1];
        double[] v = new double[1];
        for (int i = 0; i < pMesh.getIndexCount(); i += 3) {
            Vector3 a = pVert.get(pInd.get(i));
            Vector3 b = pVert.get(pInd.get(i + 1));
            Vector3 c = pVert.get(pInd.get(i + 2));
            if ((c.opSubtract(b)).cross(c.opSubtract(a)).dot(ray.getDirection()) < 0) continue;
            Vector3 o = ray.getOrigin();
            Vector3 d = ray.getDirection();
            double[] orig = { o.x, o.y, o.z };
            double[] dir = { d.x, d.y, d.z };
            double[] vert0 = { a.x, a.y, a.z };
            double[] vert1 = { b.x, b.y, b.z };
            double[] vert2 = { c.x, c.y, c.z };
            if (intersect_triangle(orig, dir, vert2, vert0, vert1, t, u, v) != 0) {
                pnt.setVector(c.opMultiplyScalar((float) (1 - u[0] - v[0])).opAdd(a.opMultiplyScalar((float) u[0])).opAdd(b.opMultiplyScalar((float) v[0])));
                return true;
            }
        }
        return false;
    }

    public static final int intersect_triangle(double[] orig, double[] dir, double[] vert0, double[] vert1, double[] vert2, double[] t, double[] u, double[] v) {
        double[] edge1 = new double[3];
        double[] edge2 = new double[3];
        double[] tvec = new double[3];
        double[] pvec = new double[3];
        double[] qvec = new double[3];
        double det, inv_det;
        SUB(edge1, vert1, vert0);
        SUB(edge2, vert2, vert0);
        CROSS(pvec, dir, edge2);
        det = DOT(edge1, pvec);
        if (det > -EPSILON && det < EPSILON) return 0;
        inv_det = 1.0 / det;
        SUB(tvec, orig, vert0);
        u[0] = DOT(tvec, pvec) * inv_det;
        if (u[0] < 0.0 || u[0] > 1.0) return 0;
        CROSS(qvec, tvec, edge1);
        v[0] = DOT(dir, qvec) * inv_det;
        if (v[0] < 0.0 || u[0] + v[0] > 1.0) return 0;
        t[0] = DOT(edge2, qvec) * inv_det;
        return 1;
    }

    public static final int intersect_triangle1(double[] orig, double[] dir, double[] vert0, double[] vert1, double[] vert2, double[] t, double[] u, double[] v) {
        double[] edge1 = new double[3];
        double[] edge2 = new double[3];
        double[] tvec = new double[3];
        double[] pvec = new double[3];
        double[] qvec = new double[3];
        double det, inv_det;
        SUB(edge1, vert1, vert0);
        SUB(edge2, vert2, vert0);
        CROSS(pvec, dir, edge2);
        det = DOT(edge1, pvec);
        if (det > EPSILON) {
            SUB(tvec, orig, vert0);
            u[0] = DOT(tvec, pvec);
            if (u[0] < 0.0 || u[0] > det) return 0;
            CROSS(qvec, tvec, edge1);
            v[0] = DOT(dir, qvec);
            if (v[0] < 0.0 || u[0] + v[0] > det) return 0;
        } else if (det < -EPSILON) {
            SUB(tvec, orig, vert0);
            u[0] = DOT(tvec, pvec);
            if (u[0] > 0.0 || u[0] < det) return 0;
            CROSS(qvec, tvec, edge1);
            v[0] = DOT(dir, qvec);
            if (v[0] > 0.0 || u[0] + v[0] < det) return 0;
        } else return 0;
        inv_det = 1.0 / det;
        t[0] = DOT(edge2, qvec) * inv_det;
        (u[0]) *= inv_det;
        (v[0]) *= inv_det;
        return 1;
    }

    public static final int intersect_triangle2(double[] orig, double[] dir, double[] vert0, double[] vert1, double[] vert2, double[] t, double[] u, double[] v) {
        double[] edge1 = new double[3];
        double[] edge2 = new double[3];
        double[] tvec = new double[3];
        double[] pvec = new double[3];
        double[] qvec = new double[3];
        double det, inv_det;
        SUB(edge1, vert1, vert0);
        SUB(edge2, vert2, vert0);
        CROSS(pvec, dir, edge2);
        det = DOT(edge1, pvec);
        SUB(tvec, orig, vert0);
        inv_det = 1.0 / det;
        if (det > EPSILON) {
            u[0] = DOT(tvec, pvec);
            if (u[0] < 0.0 || u[0] > det) return 0;
            CROSS(qvec, tvec, edge1);
            v[0] = DOT(dir, qvec);
            if (v[0] < 0.0 || u[0] + v[0] > det) return 0;
        } else if (det < -EPSILON) {
            u[0] = DOT(tvec, pvec);
            if (u[0] > 0.0 || u[0] < det) return 0;
            CROSS(qvec, tvec, edge1);
            v[0] = DOT(dir, qvec);
            if (v[0] > 0.0 || u[0] + v[0] < det) return 0;
        } else return 0;
        t[0] = DOT(edge2, qvec) * inv_det;
        (u[0]) *= inv_det;
        (v[0]) *= inv_det;
        return 1;
    }

    public static final int intersect_triangle3(double[] orig, double[] dir, double[] vert0, double[] vert1, double[] vert2, double[] t, double[] u, double[] v) {
        double[] edge1 = new double[3];
        double[] edge2 = new double[3];
        double[] tvec = new double[3];
        double[] pvec = new double[3];
        double[] qvec = new double[3];
        double det, inv_det;
        SUB(edge1, vert1, vert0);
        SUB(edge2, vert2, vert0);
        CROSS(pvec, dir, edge2);
        det = DOT(edge1, pvec);
        SUB(tvec, orig, vert0);
        inv_det = 1.0 / det;
        CROSS(qvec, tvec, edge1);
        if (det > EPSILON) {
            u[0] = DOT(tvec, pvec);
            if (u[0] < 0.0 || u[0] > det) return 0;
            v[0] = DOT(dir, qvec);
            if (v[0] < 0.0 || u[0] + v[0] > det) return 0;
        } else if (det < -EPSILON) {
            u[0] = DOT(tvec, pvec);
            if (u[0] > 0.0 || u[0] < det) return 0;
            v[0] = DOT(dir, qvec);
            if (v[0] > 0.0 || u[0] + v[0] < det) return 0;
        } else return 0;
        t[0] = DOT(edge2, qvec) * inv_det;
        (u[0]) *= inv_det;
        (v[0]) *= inv_det;
        return 1;
    }
}

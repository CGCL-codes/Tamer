package de.grogra.imp3d;

import javax.vecmath.Point2f;
import javax.vecmath.Point3f;
import javax.vecmath.Tuple2f;
import javax.vecmath.Tuple3f;
import javax.vecmath.Vector3f;

/**
 * @author semme
 *
 *	Utility class for Half-Edge Objects.  
 *
 */
public class HalfEdgeUtil {

    public static void insertFrustum(int uCount, float topRadius, float baseRadius, HalfEdgeStructSmall heo) {
        for (int u = 0; u < uCount; u++) {
            Tuple3f Vertex1 = new Point3f();
            Tuple3f Vertex2 = new Point3f();
            Tuple3f Vertex3 = new Point3f();
            Tuple3f Vertex4 = new Point3f();
            Tuple3f Normal12 = new Point3f();
            Tuple3f Normal34 = new Point3f();
            Tuple2f Tex1 = new Point2f();
            Tuple2f Tex2 = new Point2f();
            Tuple2f Tex3 = new Point2f();
            Tuple2f Tex4 = new Point2f();
            float phi = (float) (2.0f * Math.PI * (float) u / (float) uCount);
            float cosPhi = (float) Math.cos(phi);
            float sinPhi = (float) Math.sin(phi);
            Normal12.set(cosPhi, sinPhi, 0);
            Tex1.set(phi / (float) (2.0f * Math.PI), 1);
            Vertex1.set((float) (topRadius * cosPhi), (float) (topRadius * sinPhi), 1);
            Tex2.set(phi / (float) (2.0f * Math.PI), 0);
            Vertex2.set((float) (baseRadius * cosPhi), (float) (baseRadius * sinPhi), 0);
            phi = (float) (2.0f * Math.PI * (float) ((u + 1) % uCount) / (float) uCount);
            cosPhi = (float) Math.cos(phi);
            sinPhi = (float) Math.sin(phi);
            Normal34.set(cosPhi, sinPhi, 0);
            Tex4.set(phi / (float) (2.0f * Math.PI), 1);
            Vertex4.set((float) (topRadius * cosPhi), (float) (topRadius * sinPhi), 1);
            Tex3.set(phi / (float) (2.0f * Math.PI), 0);
            Vertex3.set((float) (baseRadius * cosPhi), (float) (baseRadius * sinPhi), 0);
            heo.addTriangle(Vertex1, Vertex2, Vertex3, Normal12, Normal12, Normal34, Tex1, Tex2, Tex3);
            heo.addTriangle(Vertex1, Vertex3, Vertex4, Normal12, Normal34, Normal34, Tex1, Tex3, Tex4);
        }
        Tuple3f centerNormal = new Point3f();
        Tuple3f centerVertex = new Point3f();
        Tuple2f centerTex = new Point2f();
        centerNormal.set(0, 0, -1);
        centerTex.set(0.5f, 0.5f);
        centerVertex.set(0, 0, 0);
        for (int u = 0; u < uCount; u++) {
            Tuple3f Vertex1 = new Point3f();
            Tuple3f Vertex2 = new Point3f();
            Tuple2f Tex1 = new Point2f();
            Tuple2f Tex2 = new Point2f();
            float phi = (float) (2.0f * Math.PI * (float) -u / (float) uCount);
            float cosPhi = (float) Math.cos(phi);
            float sinPhi = (float) Math.sin(phi);
            Tex1.set(cosPhi / 2 + 0.5f, sinPhi / 2 + 0.5f);
            Vertex1.set((float) (baseRadius * cosPhi), (float) (baseRadius * sinPhi), 0);
            phi = (float) (2.0f * Math.PI * (float) -((u + 1) % uCount) / (float) uCount);
            cosPhi = (float) Math.cos(phi);
            sinPhi = (float) Math.sin(phi);
            Tex2.set(cosPhi / 2 + 0.5f, sinPhi / 2 + 0.5f);
            Vertex2.set((float) (baseRadius * cosPhi), (float) (baseRadius * sinPhi), 0);
            heo.addTriangle(centerVertex, Vertex1, Vertex2, centerNormal, centerNormal, centerNormal, centerTex, Tex1, Tex2);
        }
        centerNormal = new Point3f();
        centerVertex = new Point3f();
        centerTex = new Point2f();
        centerNormal.set(0, 0, +1);
        centerTex.set(0.5f, 0.5f);
        centerVertex.set(0, 0, 1);
        for (int u = 0; u < uCount; u++) {
            Tuple3f Vertex1 = new Point3f();
            Tuple3f Vertex2 = new Point3f();
            Tuple2f Tex1 = new Point2f();
            Tuple2f Tex2 = new Point2f();
            float phi = (float) (2.0f * Math.PI * (float) u / (float) uCount);
            float cosPhi = (float) Math.cos(phi);
            float sinPhi = (float) Math.sin(phi);
            Tex1.set(cosPhi / 2 + 0.5f, sinPhi / 2 + 0.5f);
            Vertex1.set((float) (topRadius * cosPhi), (float) (topRadius * sinPhi), 1);
            phi = (float) (2.0f * Math.PI * (float) ((u + 1) % uCount) / (float) uCount);
            cosPhi = (float) Math.cos(phi);
            sinPhi = (float) Math.sin(phi);
            Tex2.set(cosPhi / 2 + 0.5f, sinPhi / 2 + 0.5f);
            Vertex2.set((float) (topRadius * cosPhi), (float) (topRadius * sinPhi), 1);
            heo.addTriangle(centerVertex, Vertex1, Vertex2, centerNormal, centerNormal, centerNormal, centerTex, Tex1, Tex2);
        }
    }

    public static void insertTetrahedra(HalfEdgeStructSmall heo) {
        Tuple3f Vertex4 = new Point3f(1f, 1f, 1f);
        Tuple3f Vertex3 = new Point3f(-1f, 1f, -1f);
        Tuple3f Vertex2 = new Point3f(-1f, -1f, 1f);
        Tuple3f Vertex1 = new Point3f(1f, -1f, -1f);
        Tuple3f Normal4 = new Point3f(-1f, -1f, -1f);
        Tuple3f Normal3 = new Point3f(1f, 1f, -1f);
        Tuple3f Normal2 = new Point3f(1f, -1f, 1f);
        Tuple3f Normal1 = new Point3f(-1f, 1f, 1f);
        Tuple2f Tex1 = new Point2f(0, 0);
        Tuple2f Tex2 = new Point2f(1, 0);
        Tuple2f Tex3 = new Point2f(0, 1);
        Tuple2f Tex4 = new Point2f(1, 1);
        heo.addTriangle(Vertex1, Vertex2, Vertex3, Normal4, Normal4, Normal4, Tex1, Tex2, Tex3);
        heo.addTriangle(Vertex1, Vertex3, Vertex4, Normal3, Normal3, Normal3, Tex1, Tex3, Tex4);
        heo.addTriangle(Vertex1, Vertex4, Vertex2, Normal2, Normal2, Normal2, Tex1, Tex4, Tex2);
        heo.addTriangle(Vertex4, Vertex3, Vertex2, Normal1, Normal1, Normal1, Tex4, Tex3, Tex2);
    }

    /**
	 * Inserts an Cone inside an empty Half-Edge object 
	 * Should be inside of an utility class.
	 */
    public static void insertCone(int uCount, float baseRadius, HalfEdgeStructSmall heo) {
        for (int u = 0; u < uCount; u++) {
            Tuple3f Vertex1 = new Point3f();
            Tuple3f Vertex2 = new Point3f();
            Tuple3f Vertex3 = new Point3f();
            Tuple3f Vertex4 = new Point3f();
            Tuple3f Normal12 = new Point3f();
            Tuple3f Normal34 = new Point3f();
            Tuple2f Tex1 = new Point2f();
            Tuple2f Tex2 = new Point2f();
            Tuple2f Tex3 = new Point2f();
            Tuple2f Tex4 = new Point2f();
            float phi = (float) (2.0f * Math.PI * (float) u / (float) uCount);
            float cosPhi = (float) Math.cos(phi);
            float sinPhi = (float) Math.sin(phi);
            Normal12.set(cosPhi, sinPhi, 0);
            Tex1.set(phi / (float) (2.0f * Math.PI), 1);
            Vertex1.set((float) (0), (float) (0), 1);
            Tex2.set(phi / (float) (2.0f * Math.PI), 0);
            Vertex2.set((float) (baseRadius * cosPhi), (float) (baseRadius * sinPhi), 0);
            phi = (float) (2.0f * Math.PI * (float) ((u + 1) % uCount) / (float) uCount);
            cosPhi = (float) Math.cos(phi);
            sinPhi = (float) Math.sin(phi);
            Normal34.set(cosPhi, sinPhi, 0);
            Tex4.set(phi / (float) (2.0f * Math.PI), 1);
            Vertex4.set((float) (0), (float) (0), 1);
            Tex3.set(phi / (float) (2.0f * Math.PI), 0);
            Vertex3.set((float) (baseRadius * cosPhi), (float) (baseRadius * sinPhi), 0);
            heo.addTriangle(Vertex1, Vertex2, Vertex3, Normal12, Normal12, Normal34, Tex1, Tex2, Tex3);
        }
        Tuple3f centerNormal = new Point3f();
        Tuple3f centerVertex = new Point3f();
        Tuple2f centerTex = new Point2f();
        centerNormal.set(0, 0, -1);
        centerTex.set(0.5f, 0.5f);
        centerVertex.set(0, 0, 0);
        for (int u = 0; u < uCount; u++) {
            Tuple3f Vertex1 = new Point3f();
            Tuple3f Vertex2 = new Point3f();
            Tuple2f Tex1 = new Point2f();
            Tuple2f Tex2 = new Point2f();
            float phi = (float) (2.0f * Math.PI * (float) -u / (float) uCount);
            float cosPhi = (float) Math.cos(phi);
            float sinPhi = (float) Math.sin(phi);
            Tex1.set(cosPhi / 2 + 0.5f, sinPhi / 2 + 0.5f);
            Vertex1.set((float) (baseRadius * cosPhi), (float) (baseRadius * sinPhi), 0);
            phi = (float) (2.0f * Math.PI * (float) -((u + 1) % uCount) / (float) uCount);
            cosPhi = (float) Math.cos(phi);
            sinPhi = (float) Math.sin(phi);
            Tex2.set(cosPhi / 2 + 0.5f, sinPhi / 2 + 0.5f);
            Vertex2.set((float) (baseRadius * cosPhi), (float) (baseRadius * sinPhi), 0);
            heo.addTriangle(centerVertex, Vertex1, Vertex2, centerNormal, centerNormal, centerNormal, centerTex, Tex1, Tex2);
        }
    }

    /**
	 * Method used by the sphere Method
	 * copied from GLDisplay
	 * 
	 * @param phi
	 * @param theta
	 * @return
	 */
    private static Vector3f genVertexSphere(float phi, float theta) {
        float cosPhi = (float) Math.cos(phi);
        float sinPhi = (float) Math.sin(phi);
        float cosTheta = (float) Math.cos(theta);
        float sinTheta = (float) Math.sin(theta);
        float x = cosPhi * cosTheta;
        float y = sinPhi * cosTheta;
        float z = sinTheta;
        return new Vector3f(x, y, z);
    }

    /**
	 * Inserts an Sphere inside an empty Half-Edge object 
	 * Should be inside of an utility class.
	 * 
	 * @param uCount
	 * @param vCount
	 */
    public static void insertSphere(int uCount, int vCount, HalfEdgeStructSmall heo, float radius) {
        for (int v = -vCount; v < vCount; v += 2) {
            float theta1 = (float) (Math.PI * (float) (v + 2) / (float) vCount / 2.0f);
            float theta2 = (float) (Math.PI * (float) v / (float) vCount / 2.0f);
            for (int u = 0; u <= uCount - 1; u++) {
                float phi = (float) (Math.PI * 2 * (float) u / (float) uCount);
                Vector3f v1 = genVertexSphere(phi, theta1);
                Vector3f v2 = genVertexSphere(phi, theta2);
                phi = (float) (Math.PI * 2 * (float) (u + 1) / (float) uCount);
                Vector3f v3 = genVertexSphere(phi, theta1);
                Vector3f v4 = genVertexSphere(phi, theta2);
                Tuple2f tex1 = new Point2f((float) u / (float) uCount, 0.5f + (float) (v + 2) / (float) vCount / 2f);
                Tuple2f tex2 = new Point2f((float) u / (float) uCount, 0.5f + (float) v / (float) vCount / 2f);
                Tuple2f tex3 = new Point2f(((float) u + 1.f) / (float) uCount, 0.5f + (float) (v + 2) / (float) vCount / 2f);
                Tuple2f tex4 = new Point2f(((float) u + 1.f) / (float) uCount, 0.5f + (float) v / (float) vCount / 2f);
                v1.scale(radius);
                v2.scale(radius);
                v3.scale(radius);
                v4.scale(radius);
                if (v > -vCount) {
                    heo.addTriangle(v2, v4, v1, v2, v4, v1, tex2, tex4, tex1);
                }
                if (v + 2 < vCount) {
                    heo.addTriangle(v3, v1, v4, v3, v1, v4, tex3, tex1, tex4);
                }
            }
        }
    }

    /**
	 * inserts a Box insides an empty Half-Edge object.
	 * 
	 * 
	 * Should be inside of an utility class.
	 * Plus the normal and texture data are totally messed up. 
	 * 
	 * @param x0
	 * @param z0
	 * @param y0
	 * @param x1
	 * @param z1
	 * @param y1
	 */
    public static void insertBox(float x0, float z0, float y0, float x1, float z1, float y1, HalfEdgeStructSmall heo) {
        Tuple3f normal = new Point3f();
        Tuple3f vertex1 = new Point3f();
        Tuple3f vertex2 = new Point3f();
        Tuple3f vertex3 = new Point3f();
        Tuple3f vertex4 = new Point3f();
        Tuple2f tex1 = new Point2f();
        Tuple2f tex2 = new Point2f();
        Tuple2f tex3 = new Point2f();
        Tuple2f tex4 = new Point2f();
        normal.set(-1, 0, 0);
        tex1.set(0, 1f / 3);
        vertex4.set(x0, y0, z0);
        tex2.set(0.25f, 1f / 3);
        vertex3.set(x0, y0, z1);
        tex3.set(0.25f, 2f / 3);
        vertex2.set(x0, y1, z1);
        tex4.set(0, 2f / 3);
        vertex1.set(x0, y1, z0);
        heo.addTriangle(vertex3, vertex1, vertex4, normal, normal, normal, tex3, tex1, tex4);
        heo.addTriangle(vertex1, vertex3, vertex2, normal, normal, normal, tex1, tex3, tex2);
        normal.set(1, 0, 0);
        tex1.set(0.75f, 1f / 3);
        vertex4.set(x1, y0, z0);
        tex2.set(0.75f, 2f / 3);
        vertex3.set(x1, y1, z0);
        tex3.set(0.5f, 2f / 3);
        vertex2.set(x1, y1, z1);
        tex4.set(0.5f, 1f / 3);
        vertex1.set(x1, y0, z1);
        heo.addTriangle(vertex1, vertex3, vertex2, normal, normal, normal, tex1, tex3, tex2);
        heo.addTriangle(vertex3, vertex1, vertex4, normal, normal, normal, tex3, tex1, tex4);
        normal.set(0, -1, 0);
        tex1.set(0.25f, 0);
        vertex4.set(x0, y0, z0);
        tex2.set(0.5f, 0);
        vertex3.set(x1, y0, z0);
        tex3.set(0.5f, 1f / 3);
        vertex2.set(x1, y0, z1);
        tex4.set(0.25f, 1f / 3);
        vertex1.set(x0, y0, z1);
        heo.addTriangle(vertex1, vertex3, vertex2, normal, normal, normal, tex1, tex3, tex2);
        heo.addTriangle(vertex3, vertex1, vertex4, normal, normal, normal, tex3, tex1, tex4);
        normal.set(0, 1, 0);
        tex1.set(0.25f, 1);
        vertex4.set(x0, y1, z0);
        tex2.set(0.25f, 2f / 3);
        vertex3.set(x0, y1, z1);
        tex3.set(0.5f, 2f / 3);
        vertex2.set(x1, y1, z1);
        tex4.set(0.5f, 1);
        vertex1.set(x1, y1, z0);
        heo.addTriangle(vertex3, vertex1, vertex4, normal, normal, normal, tex3, tex1, tex4);
        heo.addTriangle(vertex1, vertex3, vertex2, normal, normal, normal, tex1, tex3, tex2);
        normal.set(0, 0, -1);
        tex1.set(1, 1f / 3);
        vertex4.set(x0, y0, z0);
        tex2.set(1, 2f / 3);
        vertex3.set(x0, y1, z0);
        tex3.set(0.75f, 2f / 3);
        vertex2.set(x1, y1, z0);
        tex4.set(0.75f, 1f / 3);
        vertex1.set(x1, y0, z0);
        heo.addTriangle(vertex3, vertex1, vertex4, normal, normal, normal, tex3, tex1, tex4);
        heo.addTriangle(vertex1, vertex3, vertex2, normal, normal, normal, tex1, tex3, tex2);
        normal.set(0, 0, 1);
        tex1.set(0.25f, 1f / 3);
        vertex4.set(x0, y0, z1);
        tex2.set(0.5f, 1f / 3);
        vertex3.set(x1, y0, z1);
        tex3.set(0.5f, 2f / 3);
        vertex2.set(x1, y1, z1);
        tex4.set(0.25f, 2f / 3);
        vertex1.set(x0, y1, z1);
        heo.addTriangle(vertex3, vertex1, vertex4, normal, normal, normal, tex3, tex1, tex4);
        heo.addTriangle(vertex1, vertex3, vertex2, normal, normal, normal, tex1, tex3, tex2);
    }
}

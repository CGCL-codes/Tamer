package acm2.atomics;

import javax.media.j3d.*;
import javax.vecmath.*;

public class Octahedron extends Shape3D {

    public Octahedron() {
        int i = 0;
        TriangleArray qa = new TriangleArray(24, TriangleArray.COORDINATES | TriangleArray.NORMALS);
        qa.setCoordinate(i, new Point3f(0f, 0f, 0.707107f));
        qa.setNormal(i++, new Vector3f(0f, -0.707107f, 0.707107f));
        qa.setCoordinate(i, new Point3f(-0.5f, -0.5f, 0f));
        qa.setNormal(i++, new Vector3f(0f, -0.707107f, 0.707107f));
        qa.setCoordinate(i, new Point3f(0.5f, -0.5f, 0f));
        qa.setNormal(i++, new Vector3f(0f, -0.707107f, 0.707107f));
        qa.setCoordinate(i, new Point3f(0f, 0f, 0.707107f));
        qa.setNormal(i++, new Vector3f(0.707107f, 0f, 0.707107f));
        qa.setCoordinate(i, new Point3f(0.5f, -0.5f, 0f));
        qa.setNormal(i++, new Vector3f(0.707107f, 0f, 0.707107f));
        qa.setCoordinate(i, new Point3f(0.5f, 0.5f, 0f));
        qa.setNormal(i++, new Vector3f(0.707107f, 0f, 0.707107f));
        qa.setCoordinate(i, new Point3f(0f, 0f, 0.707107f));
        qa.setNormal(i++, new Vector3f(-0.707107f, 0f, 0.707107f));
        qa.setCoordinate(i, new Point3f(-0.5f, 0.5f, 0f));
        qa.setNormal(i++, new Vector3f(-0.707107f, 0f, 0.707107f));
        qa.setCoordinate(i, new Point3f(-0.5f, -0.5f, 0f));
        qa.setNormal(i++, new Vector3f(-0.707107f, 0f, 0.707107f));
        qa.setCoordinate(i, new Point3f(0f, 0f, 0.707107f));
        qa.setNormal(i++, new Vector3f(0f, 0.707107f, 0.707107f));
        qa.setCoordinate(i, new Point3f(0.5f, 0.5f, 0f));
        qa.setNormal(i++, new Vector3f(0f, 0.707107f, 0.707107f));
        qa.setCoordinate(i, new Point3f(-0.5f, 0.5f, 0f));
        qa.setNormal(i++, new Vector3f(0f, 0.707107f, 0.707107f));
        qa.setCoordinate(i, new Point3f(0f, 0f, -0.707107f));
        qa.setNormal(i++, new Vector3f(0f, -0.707107f, -0.707107f));
        qa.setCoordinate(i, new Point3f(0.5f, -0.5f, 0f));
        qa.setNormal(i++, new Vector3f(0f, -0.707107f, -0.707107f));
        qa.setCoordinate(i, new Point3f(-0.5f, -0.5f, 0f));
        qa.setNormal(i++, new Vector3f(0f, -0.707107f, -0.707107f));
        qa.setCoordinate(i, new Point3f(0f, 0f, -0.707107f));
        qa.setNormal(i++, new Vector3f(0.707107f, 0f, -0.707107f));
        qa.setCoordinate(i, new Point3f(0.5f, 0.5f, 0f));
        qa.setNormal(i++, new Vector3f(0.707107f, 0f, -0.707107f));
        qa.setCoordinate(i, new Point3f(0.5f, -0.5f, 0f));
        qa.setNormal(i++, new Vector3f(0.707107f, 0f, -0.707107f));
        qa.setCoordinate(i, new Point3f(0f, 0f, -0.707107f));
        qa.setNormal(i++, new Vector3f(-0.707107f, 0f, -0.707107f));
        qa.setCoordinate(i, new Point3f(-0.5f, -0.5f, 0f));
        qa.setNormal(i++, new Vector3f(-0.707107f, 0f, -0.707107f));
        qa.setCoordinate(i, new Point3f(-0.5f, 0.5f, 0f));
        qa.setNormal(i++, new Vector3f(-0.707107f, 0f, -0.707107f));
        qa.setCoordinate(i, new Point3f(0f, 0f, -0.707107f));
        qa.setNormal(i++, new Vector3f(0f, 0.707107f, -0.707107f));
        qa.setCoordinate(i, new Point3f(-0.5f, 0.5f, 0f));
        qa.setNormal(i++, new Vector3f(0f, 0.707107f, -0.707107f));
        qa.setCoordinate(i, new Point3f(0.5f, 0.5f, 0f));
        qa.setNormal(i, new Vector3f(0f, 0.707107f, -0.707107f));
        setGeometry(qa);
    }
}

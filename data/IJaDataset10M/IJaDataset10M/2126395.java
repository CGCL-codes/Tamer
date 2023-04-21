package cms2;

import javax.media.j3d.*;
import javax.vecmath.*;

public class Ex01 extends Shape3D {

    public Ex01() {
        int i = 0;
        QuadArray qa = new QuadArray(24, QuadArray.COORDINATES | QuadArray.NORMALS);
        qa.setCoordinate(i, new Point3f(0.5f, -.390977f, -0.144736f));
        qa.setNormal(i++, new Vector3f(1f, 0f, 0f));
        qa.setCoordinate(i, new Point3f(0.5f, 0.390977f, -0.093984f));
        qa.setNormal(i++, new Vector3f(1f, 0f, 0f));
        qa.setCoordinate(i, new Point3f(0.5f, 0.390977f, 0.093984f));
        qa.setNormal(i++, new Vector3f(1f, 0f, 0f));
        qa.setCoordinate(i, new Point3f(0.5f, -0.390977f, 0.144736f));
        qa.setNormal(i++, new Vector3f(1f, 0f, 0f));
        qa.setCoordinate(i, new Point3f(-0.5f, 0.390977f, 0.093984f));
        qa.setNormal(i++, new Vector3f(-1f, 0f, 0f));
        qa.setCoordinate(i, new Point3f(-0.5f, 0.390977f, -0.093984f));
        qa.setNormal(i++, new Vector3f(-1f, 0f, 0f));
        qa.setCoordinate(i, new Point3f(-0.5f, -0.390977f, -0.144736f));
        qa.setNormal(i++, new Vector3f(-1f, 0f, 0f));
        qa.setCoordinate(i, new Point3f(-0.5f, -.390977f, 0.144736f));
        qa.setNormal(i++, new Vector3f(-1f, 0f, 0f));
        qa.setCoordinate(i, new Point3f(0.5f, 0.390977f, 0.093984f));
        qa.setNormal(i++, new Vector3f(0f, 0.26929f, 0.9979f));
        qa.setCoordinate(i, new Point3f(-0.5f, 0.390977f, 0.093984f));
        qa.setNormal(i++, new Vector3f(0f, 0.26929f, 0.9979f));
        qa.setCoordinate(i, new Point3f(-0.5f, -0.390977f, 0.144736f));
        qa.setNormal(i++, new Vector3f(0f, 0.26929f, 0.9979f));
        qa.setCoordinate(i, new Point3f(0.5f, -0.390977f, 0.144736f));
        qa.setNormal(i++, new Vector3f(0f, 0.26929f, 0.9979f));
        qa.setCoordinate(i, new Point3f(0.5f, 0.390977f, -0.093984f));
        qa.setNormal(i++, new Vector3f(0f, 0.26929f, -0.9979f));
        qa.setCoordinate(i, new Point3f(0.5f, -0.390977f, -0.144736f));
        qa.setNormal(i++, new Vector3f(0f, 0.26929f, -0.9979f));
        qa.setCoordinate(i, new Point3f(-0.5f, -0.390977f, -0.144736f));
        qa.setNormal(i++, new Vector3f(0f, 0.26929f, -0.9979f));
        qa.setCoordinate(i, new Point3f(-0.5f, 0.390977f, -0.093984f));
        qa.setNormal(i++, new Vector3f(0f, 0.26929f, -0.9979f));
        qa.setCoordinate(i, new Point3f(0.5f, 0.390977f, 0.093984f));
        qa.setNormal(i++, new Vector3f(0f, 1f, 0f));
        qa.setCoordinate(i, new Point3f(0.5f, 0.390977f, -0.093984f));
        qa.setNormal(i++, new Vector3f(0f, 1f, 0f));
        qa.setCoordinate(i, new Point3f(-0.5f, 0.390977f, -0.093984f));
        qa.setNormal(i++, new Vector3f(0f, 1f, 0f));
        qa.setCoordinate(i, new Point3f(-0.5f, 0.390977f, 0.093984f));
        qa.setNormal(i++, new Vector3f(0f, 1f, 0f));
        qa.setCoordinate(i, new Point3f(0.5f, -0.390977f, 0.144736f));
        qa.setNormal(i++, new Vector3f(0f, -1f, 0f));
        qa.setCoordinate(i, new Point3f(-0.5f, -0.390977f, 0.144736f));
        qa.setNormal(i++, new Vector3f(0f, -1f, 0f));
        qa.setCoordinate(i, new Point3f(-0.5f, -0.390977f, -0.144736f));
        qa.setNormal(i++, new Vector3f(0f, -1f, 0f));
        qa.setCoordinate(i, new Point3f(0.5f, -0.390977f, -0.144736f));
        qa.setNormal(i++, new Vector3f(0f, -1f, 0f));
        setGeometry(qa);
    }
}

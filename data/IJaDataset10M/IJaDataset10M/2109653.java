package org.konte.image;

import org.konte.misc.Matrix3;
import org.konte.misc.Matrix4;
import org.konte.misc.Point2;
import org.konte.misc.Vector3;
import org.konte.model.DrawingContext;
import org.konte.model.Transform;
import org.konte.model.TransformModifier;
import org.konte.parse.ParseException;

/**
 *
 * @author pt
 */
public class SimpleCamera implements Camera {

    protected float viewerDfromCamera = 1;

    protected Matrix3 cameraRotationMatrix;

    protected Vector3 position;

    protected Vector3 target;

    protected String name;

    protected Canvas canvas;

    public SimpleCamera() {
    }

    public SimpleCamera(Transform pos) throws ParseException {
        setPosition(pos);
    }

    @Override
    public Point2 mapTo2D(Vector3 v) {
        Vector3 d = cameraRotationMatrix.multiply(Vector3.sub(v, position));
        if (d.z >= 1f) return new Point2(d.x / d.z, d.y / d.z);
        float fct = 2f - d.z;
        return new Point2(d.x * fct, d.y * fct);
    }

    @Override
    public String getName() {
        return (name == null ? "" : name);
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    private static final float toRad = 3.14159265f / 180f;

    @Override
    public void setPosition(Transform posT) throws ParseException {
        DrawingContext pos = posT.transform(DrawingContext.ZERO);
        position = new Vector3(pos.getx(), -pos.gety(), pos.getz());
        float rx = 0f;
        float ry = 0f;
        float rz = 0f;
        for (TransformModifier tr : posT.acqTrs) {
            if (tr instanceof TransformModifier.rx) rx += tr.evaluateAll()[0] * toRad; else if (tr instanceof TransformModifier.ry) ry += tr.evaluateAll()[0] * toRad; else if (tr instanceof TransformModifier.rz) rz += tr.evaluateAll()[0] * toRad;
        }
        cameraRotationMatrix = Matrix3.rotation(rx, ry, rz);
        target = cameraRotationMatrix.multiply(new Vector3(0f, 0f, -1f));
    }

    public Vector3 getPosition() {
        return position;
    }

    public Matrix3 getCameraRotationMatrix() {
        return cameraRotationMatrix;
    }

    @Override
    public String toString() {
        StringBuilder bd = new StringBuilder();
        bd.append(getName()).append(" [").append(position).append("] R*\n").append(cameraRotationMatrix);
        return bd.toString();
    }

    public void setCanvas(Canvas canvas) {
        this.canvas = canvas;
    }

    public float distMetric(Matrix4 matrix) {
        float xs = (position.x - matrix.m03);
        float ys = (position.y - matrix.m13);
        float zs = (position.z - matrix.m23);
        return xs * xs + ys * ys + zs * zs;
    }

    public Vector3 getTarget() {
        return target;
    }

    public void setTarget(Vector3 trgt) {
        target = trgt;
        Vector3 diff = Vector3.sub(target, position);
        float rx = 0f;
        if (diff.z == 0f) {
            rx = (float) Math.PI / 2f;
            if (diff.y < 0f) rx = -rx;
        } else rx = (float) Math.atan2(diff.z, diff.z);
        float ry = 0f;
        if (diff.x == 0f) {
            ry = (float) Math.PI / 2f;
            if (diff.z < 0f) ry = -ry;
        } else ry = (float) Math.atan2(diff.z, diff.x);
        float rz = 0f;
        if (diff.x == 0f) {
            rz = (float) Math.PI / 2f;
            if (diff.y < 0f) rz = -rz;
        } else rz = (float) Math.atan2(diff.y, diff.x);
        cameraRotationMatrix = Matrix3.rotation(rx, ry, rz);
    }
}

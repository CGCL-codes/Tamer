package vizz3d.common.interfaces;

public class Vector3d {

    public float x;

    public float y;

    public float z;

    public float w;

    public Vector3d() {
        x = 0;
        y = 0;
        z = 0;
        w = 1;
    }

    public Vector3d(float xx, float yy, float zz) {
        x = xx;
        y = yy;
        z = zz;
        w = 1;
    }

    public Vector3d(double xx, double yy, double zz) {
        x = (float) xx;
        y = (float) yy;
        z = (float) zz;
        w = 1;
    }

    public void cross(Vector3d vector1, Vector3d vector2) {
        x = (vector1.y * vector2.z) - (vector1.z * vector2.y);
        y = (vector1.z * vector2.x) - (vector1.x * vector2.z);
        z = (vector1.x * vector2.y) - (vector1.y * vector2.x);
    }

    public double dot(Vector3d vector) {
        return x * vector.x + y * vector.y + z * vector.z;
    }

    public float magnitude() {
        return (float) Math.sqrt(x * x + y * y + z * z);
    }

    public void normalize() {
        float mag = magnitude();
        if (mag != 0.0f) {
            x /= mag;
            y /= mag;
            z /= mag;
        }
    }
}

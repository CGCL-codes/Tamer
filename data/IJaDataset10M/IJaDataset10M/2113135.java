package zildo.monde.util;

/**
 * @author Tchegito
 *
 */
public class Vector4f {

    public float x;

    public float y;

    public float z;

    public float w;

    public Vector4f(float x, float y, float z, float w) {
        set(x, y, z, w);
    }

    public Vector4f(Vector4f v) {
        set(v.x, v.y, v.z, v.w);
    }

    public void set(float x, float y, float z, float w) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }

    public Vector4f scale(float scale) {
        x *= scale;
        y *= scale;
        z *= scale;
        w *= scale;
        return this;
    }
}

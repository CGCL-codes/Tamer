package jpatch.entity;

import javax.vecmath.*;

public class GlMaterial {

    public static final int AMBIENT = 0;

    public static final int DIFFUSE = 4;

    public static final int SPECULAR = 8;

    public static final int EMISSION = 12;

    public static final int SHININESS = 16;

    private final float[] array = new float[17];

    public GlMaterial(final Color3f ka, final Color3f kd, final Color3f ks, final Color3f ke, final float shininess) {
        setKa(ka);
        setKd(kd);
        setKs(ks);
        setKe(ke);
        setShininess(shininess);
    }

    public GlMaterial(final Color4f ka, final Color4f kd, final Color4f ks, final Color4f ke, final float shininess) {
        setKa(ka);
        setKd(kd);
        setKs(ks);
        setKe(ke);
        setShininess(shininess);
    }

    public float[] getArray() {
        return array;
    }

    public Color4f getKa(Color4f color) {
        color.set(array[AMBIENT], array[AMBIENT + 1], array[AMBIENT + 2], array[AMBIENT + 3]);
        return color;
    }

    public Color3f getKa(Color3f color) {
        color.set(array[AMBIENT], array[AMBIENT + 1], array[AMBIENT + 2]);
        return color;
    }

    public Color4f getKd(Color4f color) {
        color.set(array[DIFFUSE], array[DIFFUSE + 1], array[DIFFUSE + 2], array[DIFFUSE + 3]);
        return color;
    }

    public Color3f getKd(Color3f color) {
        color.set(array[DIFFUSE], array[DIFFUSE + 1], array[DIFFUSE + 2]);
        return color;
    }

    public Color4f getKs(Color4f color) {
        color.set(array[SPECULAR], array[SPECULAR + 1], array[SPECULAR + 2], array[SPECULAR + 3]);
        return color;
    }

    public Color3f getKs(Color3f color) {
        color.set(array[SPECULAR], array[SPECULAR + 1], array[SPECULAR + 2]);
        return color;
    }

    public Color4f getKe(Color4f color) {
        color.set(array[EMISSION], array[EMISSION + 1], array[EMISSION + 2], array[EMISSION + 3]);
        return color;
    }

    public Color3f getKe(Color3f color) {
        color.set(array[EMISSION], array[EMISSION + 1], array[EMISSION + 2]);
        return color;
    }

    public float getShininess() {
        return array[SHININESS];
    }

    public void setKa(Color4f ka) {
        array[AMBIENT] = ka.x;
        array[AMBIENT + 1] = ka.y;
        array[AMBIENT + 2] = ka.z;
        array[AMBIENT + 3] = ka.w;
    }

    public void setKa(Color3f ka) {
        array[AMBIENT] = ka.x;
        array[AMBIENT + 1] = ka.y;
        array[AMBIENT + 2] = ka.z;
        array[AMBIENT + 3] = 1.0f;
    }

    public void setKd(Color4f kd) {
        array[DIFFUSE] = kd.x;
        array[DIFFUSE + 1] = kd.y;
        array[DIFFUSE + 2] = kd.z;
        array[DIFFUSE + 3] = kd.w;
    }

    public void setKd(Color3f kd) {
        array[DIFFUSE] = kd.x;
        array[DIFFUSE + 1] = kd.y;
        array[DIFFUSE + 2] = kd.z;
        array[DIFFUSE + 3] = 1.0f;
    }

    public void setKs(Color4f ks) {
        array[SPECULAR] = ks.x;
        array[SPECULAR + 1] = ks.y;
        array[SPECULAR + 2] = ks.z;
        array[SPECULAR + 3] = ks.w;
    }

    public void setKs(Color3f ks) {
        array[SPECULAR] = ks.x;
        array[SPECULAR + 1] = ks.y;
        array[SPECULAR + 2] = ks.z;
        array[SPECULAR + 3] = 1.0f;
    }

    public void setKe(Color4f ke) {
        array[EMISSION] = ke.x;
        array[EMISSION + 1] = ke.y;
        array[EMISSION + 2] = ke.z;
        array[EMISSION + 3] = ke.w;
    }

    public void setKe(Color3f ke) {
        array[EMISSION] = ke.x;
        array[EMISSION + 1] = ke.y;
        array[EMISSION + 2] = ke.z;
        array[EMISSION + 3] = 1.0f;
    }

    public void setShininess(float shininess) {
        array[SHININESS] = shininess;
    }
}

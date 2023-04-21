package net.playbesiege.utils;

import com.ardor3d.math.Quaternion;
import com.ardor3d.math.Transform;
import com.ardor3d.math.type.ReadOnlyColorRGBA;
import com.ardor3d.math.type.ReadOnlyMatrix3;
import com.ardor3d.math.type.ReadOnlyTransform;
import com.ardor3d.math.type.ReadOnlyVector3;

public class MySpatial {

    private final Meshy meshy;

    private final int i;

    Transform t = new Transform();

    public MySpatial(Meshy testMeshy, int i) {
        this.meshy = testMeshy;
        this.i = i;
    }

    public void setRotation(Quaternion rotate) {
        t.setRotation(rotate);
    }

    public void setRotation(ReadOnlyMatrix3 rotate) {
        t.setRotation(rotate);
    }

    public void setTranslation(ReadOnlyVector3 amount) {
        t.setTranslation(amount);
    }

    public void applyTransform() {
        meshy.transform(i, t);
    }

    public void applyTransform(ReadOnlyTransform t) {
        meshy.transform(i, t);
    }

    public ReadOnlyTransform getTransform() {
        return t;
    }

    public void setSolidColor(ReadOnlyColorRGBA color) {
        meshy.setSolidColor(i, color);
    }

    public ReadOnlyVector3 getTranslation() {
        return t.getTranslation();
    }

    public ReadOnlyMatrix3 getRotation() {
        return t.getMatrix();
    }

    public void remove() {
        meshy.free(i);
    }
}

package kfschmidt.rendering;

import java.awt.Color;
import kfschmidt.geom3d.Point3D;

public class AppliedTexture {

    Color mAmbientColor = new Color(.1f, 0f, 0f, 1f);

    Color mSpecular = new Color(1f, 1f, 1f, 1f);

    Color mDiffuse = new Color(.6f, .2f, 0f, 1f);

    float mSpecularWeight = .5f;

    float mDiffuseWeight = .6f;

    float mAmbientWeight = .1f;

    protected AppliedTexture(Color c) {
        mAmbientColor = c;
    }

    protected AppliedTexture() {
    }

    public Color getColorAtPoint(Point3D point) {
        return mAmbientColor;
    }

    public Color getSpecularColor() {
        return mSpecular;
    }

    public Color getDiffuseColor() {
        return mDiffuse;
    }

    public Color getAmbientColor() {
        return mAmbientColor;
    }

    public void setDiffuseColor(Color c) {
        mDiffuse = c;
    }

    public void setAmbientColor(Color c) {
        mAmbientColor = c;
    }

    public void setSpecularColor(Color c) {
        mSpecular = c;
    }

    public float getSpecularWeight() {
        return mSpecularWeight;
    }

    public float getDiffuseWeight() {
        return mDiffuseWeight;
    }

    public float getAmbientWeight() {
        return mAmbientWeight;
    }
}

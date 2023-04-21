package eu.cherrytree.paj.graphics;

import javax.media.opengl.GL2;

public class Material {

    private float[] ambient;

    private float[] diffuse;

    private float[] emissive;

    private float[] specular;

    private float shinines;

    public Material() {
        ambient = new float[4];
        diffuse = new float[4];
        emissive = new float[4];
        specular = new float[4];
        setAmbient(1.0f, 1.0f, 1.0f, 1.0f);
        setDiffuse(1.0f, 1.0f, 1.0f, 1.0f);
        setEmissive(0.0f, 0.0f, 0.0f, 1.0f);
        setSpecular(0.0f, 0.0f, 0.0f, 0.0f);
        setShininess(0.0f);
    }

    public Material(Material mat) {
        ambient = new float[4];
        diffuse = new float[4];
        emissive = new float[4];
        specular = new float[4];
        float[] color = mat.getAmbient();
        setAmbient(color[0], color[1], color[2], color[3]);
        color = mat.getDiffuse();
        setDiffuse(color[0], color[1], color[2], color[3]);
        color = mat.getEmissive();
        setEmissive(color[0], color[1], color[2], color[3]);
        color = mat.getSpecular();
        setSpecular(color[0], color[1], color[2], color[3]);
        setShininess(mat.getShininess());
    }

    public void setAmbient(float R, float G, float B, float A) {
        ambient[0] = R;
        ambient[1] = G;
        ambient[2] = B;
        ambient[3] = A;
    }

    public void setDiffuse(float R, float G, float B, float A) {
        diffuse[0] = R;
        diffuse[1] = G;
        diffuse[2] = B;
        diffuse[3] = A;
    }

    public void setEmissive(float R, float G, float B, float A) {
        emissive[0] = R;
        emissive[1] = G;
        emissive[2] = B;
        emissive[3] = A;
    }

    public void setSpecular(float R, float G, float B, float A) {
        specular[0] = R;
        specular[1] = G;
        specular[2] = B;
        specular[3] = A;
    }

    public void setShininess(float val) {
        shinines = (float) Math.max(Math.min(val, 128.0), 0.0);
    }

    public float[] getAmbient() {
        float[] ret = new float[4];
        ret[0] = ambient[0];
        ret[1] = ambient[1];
        ret[2] = ambient[2];
        ret[3] = ambient[3];
        return ret;
    }

    public float[] getDiffuse() {
        float[] ret = new float[4];
        ret[0] = diffuse[0];
        ret[1] = diffuse[1];
        ret[2] = diffuse[2];
        ret[3] = diffuse[3];
        return ret;
    }

    public float[] getEmissive() {
        float[] ret = new float[4];
        ret[0] = emissive[0];
        ret[1] = emissive[1];
        ret[2] = emissive[2];
        ret[3] = emissive[3];
        return ret;
    }

    public float[] getSpecular() {
        float[] ret = new float[4];
        ret[0] = specular[0];
        ret[1] = specular[1];
        ret[2] = specular[2];
        ret[3] = specular[3];
        return ret;
    }

    public float getShininess() {
        return shinines;
    }

    public void setOpacity(float val) {
        if (val >= 0.0f && val <= 1.0f) {
            ambient[3] = val;
            diffuse[3] = val;
            emissive[3] = val;
            specular[3] = val;
        }
    }

    public void applyMaterial(GL2 gl) {
        gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_AMBIENT, ambient, 0);
        gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_DIFFUSE, diffuse, 0);
        gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_EMISSION, emissive, 0);
        gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_SPECULAR, specular, 0);
        gl.glMaterialf(GL2.GL_FRONT_AND_BACK, GL2.GL_SHININESS, shinines);
    }
}

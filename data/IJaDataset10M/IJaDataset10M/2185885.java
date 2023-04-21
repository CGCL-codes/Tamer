package hybris;

import java.awt.Color;
import java.awt.Graphics;

/**
 *
 * @author menderleit
 */
public class Object3D {

    private int numFaces;

    private Quad3D[] face;

    private Vector3 pos;

    private Vector3 rot;

    public Object3D(int numFaces) {
        this.numFaces = numFaces;
        face = new Quad3D[numFaces];
        for (int i = 0; i < numFaces; i++) {
            face[i] = new Quad3D();
        }
        pos = new Vector3();
        rot = new Vector3();
    }

    public void setFace(int f, int v, Vector3 v3) {
        if (f >= 0 && f < numFaces) {
            face[f].setVector(v, v3);
        }
    }

    public void setFaceColor(int f, Color col) {
        if (f >= 0 && f < numFaces) {
            face[f].setColor(col);
        }
    }

    public void setPosition(Vector3 pos) {
        this.pos = pos.copy();
    }

    public void setRotation(Vector3 rot) {
        this.rot = rot.copy();
    }

    public void draw(Graphics g, int screenWidth, int screenHeight, double POV) {
        for (int i = 0; i < numFaces; i++) {
            face[i].draw(g, screenWidth, screenHeight, POV, pos, rot);
        }
    }
}

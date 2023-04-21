package joelib.gui.molviewer.java3d.graphics3D;

import javax.media.j3d.QuadArray;
import javax.media.j3d.Shape3D;
import javax.media.j3d.TriangleArray;

/**
 * Description of the Class
 *
 * @author     wegnerj
 * @license    GPL
 * @cvsversion    $Revision: 1.5 $, $Date: 2004/08/31 14:23:21 $
 */
public class Sphere {

    Shape3D shape;

    float[][][] verts;

    int nPhi = 6;

    int nPsi = 4;

    /**
     *Constructor for the Sphere object
     */
    public Sphere() {
        createShape();
    }

    /**
     * Gets the shape attribute of the Sphere object
     *
     * @return   The shape value
     */
    Shape3D getShape() {
        return shape;
    }

    /**
     * Description of the Method
     */
    void createShape() {
        fillVerts();
        float[] coords = new float[nPhi * nPsi * 9];
        float[] norms = new float[nPhi * nPsi * 9];
        int n = 0;
        for (int psi = 0; psi < (nPsi - 1); psi++) {
            for (int phi = 0; phi < (nPhi - 2); phi++) {
                norms[n] = verts[psi][phi][0];
                norms[n + 1] = verts[psi][phi][1];
                norms[n + 2] = verts[psi][phi][2];
                norms[n + 3] = verts[psi + 1][phi][0];
                norms[n + 4] = verts[psi + 1][phi][1];
                norms[n + 5] = verts[psi + 1][phi][2];
                norms[n + 6] = verts[psi + 1][phi + 1][0];
                norms[n + 7] = verts[psi + 1][phi + 1][1];
                norms[n + 8] = verts[psi + 1][phi + 1][2];
                n += 9;
            }
        }
        for (int i = 0; i < norms.length; i++) {
            coords[i] = norms[i] * 2.0f;
        }
        System.out.println("Creating tri");
        TriangleArray tri = new TriangleArray(nPhi * nPsi * 3, QuadArray.COORDINATES | QuadArray.NORMALS);
        System.out.println("Setting norms and coords");
        tri.setCoordinates(0, coords);
        tri.setNormals(0, norms);
        shape = new Shape3D(tri);
    }

    /**
     * Description of the Method
     */
    void fillVerts() {
        double mPhi = Math.PI / ((1.0 * nPhi) - 1.0) * 2.0;
        double mPsi = Math.PI / ((1.0 * nPsi) - 1.0) * 1.0;
        verts = new float[nPsi][nPhi][3];
        float r;
        float z;
        float phiAngle;
        float psiAngle;
        psiAngle = 0.0f;
        for (int psi = 0; psi < nPsi; psi++) {
            z = (float) Math.cos(psiAngle);
            r = (float) Math.sin(psiAngle);
            phiAngle = 0.0f;
            for (int phi = 0; phi < nPhi; phi++) {
                verts[psi][phi][0] = r * (float) Math.cos(phiAngle);
                verts[psi][phi][1] = r * (float) Math.sin(phiAngle);
                verts[psi][phi][2] = z;
                phiAngle += mPhi;
            }
            psiAngle += mPsi;
        }
    }
}

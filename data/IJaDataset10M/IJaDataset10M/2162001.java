package org.stellarium.projector;

import org.stellarium.StelUtility;
import static org.stellarium.ui.SglAccess.*;
import org.stellarium.vecmath.Rectangle4i;
import javax.media.opengl.GL;
import javax.vecmath.Matrix4d;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;
import static java.lang.StrictMath.*;

/**
 * Class which handle projection modes and projection matrix
 * Overide some function usually handled by glu
 * <p/>
 * See <a href="http://cvs.sourceforge.net/viewcvs.py/stellarium/stellarium/src/custom_projector.cpp?rev=1.20&view=markup">C++ version</a> of this file.
 *
 * @author Fred Simon
 * @version 0.8.2
 */
public class CustomProjector extends DefaultProjector {

    protected CustomProjector(Rectangle4i viewport) {
        this(viewport, 175);
    }

    protected CustomProjector(Rectangle4i viewport, double fov) {
        super(viewport, fov);
    }

    /**
     * Init the viewing matrix, setting the field of view, the clipping planes, and screen ratio
     * The function is a reimplementation of glOrtho
     */
    protected void initProjectMatrix() {
        glMatrixMode(GL.GL_PROJECTION);
        glLoadMatrixd(matProjection);
        glMatrixMode(GL.GL_MODELVIEW);
    }

    /**
     * Override glVertex3f
     * Here is the Main trick for texturing in fisheye mode : The trick is to compute the
     * new coordinate in orthographic projection which will simulate the fisheye projection.
     */
    public void sVertex3(double x, double y, double z, final Matrix4d mat) {
        Point3d win = new Point3d();
        Point3d v = new Point3d(x, y, z);
        projectCustom(v, win, mat);
        gluUnProject(win, mat, matProjection, vecViewport, v);
        glVertex3dv(StelUtility.toArray(v), 0);
    }

    public void sSphere(double radius, double one_minus_oblateness, int slices, int stacks, final Matrix4d mat, boolean orient_inside) {
        glPushMatrix();
        glLoadMatrixd(mat);
        Vector3d lightPos3 = new Vector3d();
        byte[] isLightOnBuff = new byte[1];
        Vector3d transNorm;
        float c;
        float[] ambientLight = new float[4];
        float[] diffuseLight = new float[4];
        glGetBooleanv(GL.GL_LIGHTING, isLightOnBuff, 0);
        boolean isLightOn = (isLightOnBuff[0] != 0);
        if (isLightOn) {
            lightPos3.set(getLightPos());
            Point3d v = new Point3d(0., 0., 0.);
            mat.transform(v);
            lightPos3.sub(v);
            lightPos3.normalize();
            glGetLightfv(GL.GL_LIGHT0, GL.GL_AMBIENT, ambientLight, 0);
            glGetLightfv(GL.GL_LIGHT0, GL.GL_DIFFUSE, diffuseLight, 0);
            glDisable(GL.GL_LIGHTING);
        }
        double x, y, z;
        double s, t;
        int i, j;
        double nsign;
        if (orient_inside) {
            nsign = -1.0;
            t = 1.0;
        } else {
            nsign = 1.0;
            t = 0;
        }
        final double drho = PI / stacks;
        double[] cos_sin_rho = new double[2 * (stacks + 1)];
        int cos_sin_rho_p = 0;
        for (i = 0; i <= stacks; i++) {
            double rho = i * drho;
            cos_sin_rho[cos_sin_rho_p] = cos(rho);
            cos_sin_rho_p++;
            cos_sin_rho[cos_sin_rho_p] = sin(rho);
            cos_sin_rho_p++;
        }
        final double dtheta = 2.0 * PI / (float) slices;
        double[] cos_sin_theta = new double[2 * (slices + 1)];
        int cos_sin_theta_p = 0;
        for (i = 0; i <= slices; i++) {
            double theta = (i == slices) ? 0.0 : i * dtheta;
            cos_sin_theta[cos_sin_theta_p] = cos(theta);
            cos_sin_theta_p++;
            cos_sin_theta[cos_sin_theta_p] = sin(theta);
            cos_sin_theta_p++;
        }
        final double ds = 1.0 / slices;
        final double dt = nsign / stacks;
        for (i = 0, cos_sin_rho_p = 0; i < stacks; i++, cos_sin_rho_p += 2) {
            glBegin(GL.GL_QUAD_STRIP);
            s = 0.0f;
            for (j = 0, cos_sin_theta_p = 0; j <= slices; j++, cos_sin_theta_p += 2) {
                x = -cos_sin_theta[cos_sin_theta_p + 1] * cos_sin_rho[cos_sin_rho_p + 1];
                y = cos_sin_theta[cos_sin_theta_p + 0] * cos_sin_rho[cos_sin_rho_p + 1];
                z = nsign * cos_sin_rho[cos_sin_rho_p + 0];
                glTexCoord2d(s, t);
                if (isLightOn) {
                    transNorm = new Vector3d(x * one_minus_oblateness * nsign, y * one_minus_oblateness * nsign, z * nsign);
                    mat.transform(transNorm);
                    c = (float) lightPos3.dot(transNorm);
                    if (c < 0) c = 0f;
                    glColor3f(c * diffuseLight[0] + ambientLight[0], c * diffuseLight[1] + ambientLight[1], c * diffuseLight[2] + ambientLight[2]);
                }
                sVertex3(x * radius, y * radius, z * one_minus_oblateness * radius, mat);
                x = -cos_sin_theta[cos_sin_theta_p + 1] * cos_sin_rho[cos_sin_rho_p + 3];
                y = cos_sin_theta[cos_sin_theta_p] * cos_sin_rho[cos_sin_rho_p + 3];
                z = nsign * cos_sin_rho[cos_sin_rho_p + 2];
                glTexCoord2d(s, t + dt);
                if (isLightOn) {
                    transNorm = new Vector3d(x * one_minus_oblateness * nsign, y * one_minus_oblateness * nsign, z * nsign);
                    mat.transform(transNorm);
                    c = (float) lightPos3.dot(transNorm);
                    if (c < 0) c = 0f;
                    glColor3f(c * diffuseLight[0] + ambientLight[0], c * diffuseLight[1] + ambientLight[1], c * diffuseLight[2] + ambientLight[2]);
                }
                sVertex3(x * radius, y * radius, z * one_minus_oblateness * radius, mat);
                s += ds;
            }
            glEnd();
            t += dt;
        }
        glPopMatrix();
        if (isLightOn) glEnable(GL.GL_LIGHTING);
    }

    public void sCylinder(double radius, double height, int slices, int stacks, final Matrix4d mat, boolean orientInside) {
        glPushMatrix();
        glLoadMatrixd(mat);
        double da, r, dz;
        double z, nsign;
        int i, j;
        nsign = 1.0;
        if (orientInside) glCullFace(GL.GL_FRONT);
        da = 2.0 * PI / slices;
        dz = height / stacks;
        double ds = 1.0 / slices;
        double dt = 1.0 / stacks;
        double t = 0.0;
        z = 0.0;
        r = radius;
        for (j = 0; j < stacks; j++) {
            double s = 0.0;
            glBegin(GL.GL_QUAD_STRIP);
            for (i = 0; i <= slices; i++) {
                double x, y;
                if (i == slices) {
                    x = sin(0.0);
                    y = cos(0.0);
                } else {
                    x = sin(i * da);
                    y = cos(i * da);
                }
                glNormal3d(x * nsign, y * nsign, 0);
                glTexCoord2d(s, t + dt);
                sVertex3(x * r, y * r, z, mat);
                glNormal3d(x * nsign, y * nsign, 0);
                glTexCoord2d(s, t);
                sVertex3(x * r, y * r, z + dz, mat);
                s += ds;
            }
            glEnd();
            t += dt;
            z += dz;
        }
        glPopMatrix();
        if (orientInside) glCullFace(GL.GL_BACK);
    }
}

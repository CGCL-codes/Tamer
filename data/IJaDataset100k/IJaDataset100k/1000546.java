package net.sf.breed.util.joglib;

import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static java.lang.Math.toRadians;
import javax.media.opengl.GL;
import javax.media.opengl.glu.GLU;

/**
 * Utility for setting the camera position.
 * <p>
 * Camera settings are determined by an <code>eye</code> 3D point (where you
 * are), a <code>center</code> position (where you look at), and a vector
 * indicating where your head is pointing into the sky (e.g. x=0, y=1, z=1 for
 * straight up).
 * 
 * @author <a href="mailto:kain@land-of-kain.de">Kai Ruhl</a>
 * @since 22 Jan 2009
 */
public class JoglibCamera {

    /** The Joglib context. */
    private JoglibContext context;

    /**
     * @param context The Joglib context.
     */
    public JoglibCamera(JoglibContext context) {
        this.context = context;
    }

    /**
     * Sets the camera so that it is looking towards (0,0,0) from the
     * (0,0,distance), i.e. only the z-plane can be defined. The field of view
     * angle in the Y direction defines, together with the width/height ratio of
     * the screen, the viewing angle.
     * 
     * @param fieldOfViewAngleY The field of view angle, in Y direction. Useful
     *        in conjunction with the width/height ratio.
     * @param distance The distance of the "viewing point" in z direction.
     */
    public void setCameraBasic(float fieldOfViewAngleY, float distance) {
        final GL gl = context.getGL();
        final GLU glu = context.getGLU();
        gl.glMatrixMode(GL.GL_PROJECTION);
        gl.glLoadIdentity();
        if (0 == fieldOfViewAngleY) {
            fieldOfViewAngleY = 45;
        }
        final double widthHeightRatio = context.getCanvasWidthToHeightRatio();
        final double nearPlane = 0.1;
        final double farPlane = 10000000;
        glu.gluPerspective(fieldOfViewAngleY, widthHeightRatio, nearPlane, farPlane);
        double eyeX = 0;
        double eyeY = 0;
        double eyeZ = distance;
        double centerX = 0;
        double centerY = 0;
        double centerZ = 0;
        double upX = 0;
        double upY = 1;
        double upZ = 0;
        glu.gluLookAt(eyeX, eyeY, eyeZ, centerX, centerY, centerZ, upX, upY, upZ);
        gl.glMatrixMode(GL.GL_MODELVIEW);
        gl.glLoadIdentity();
    }

    /**
     * Sets the camera such that the viewer is on a sphere with the size of
     * <code>distance</code>, looking at (0,0,0) and with the head tangentially
     * into the sky.
     * 
     * @param fieldOfViewAngleY The field of view that the viewer has, in Y
     *        direction.
     * @param xAngle The angle "phi", deviating from the 0 position on the x
     *        axis.
     * @param yAngle The angle "tau", deviating from the 0 position on the y
     *        axis.
     * @param distance The distance of the viewer.
     */
    public void setCameraSphere(float fieldOfViewAngleY, float xAngle, float yAngle, float distance) {
        final GL gl = context.getGL();
        final GLU glu = context.getGLU();
        gl.glMatrixMode(GL.GL_PROJECTION);
        gl.glLoadIdentity();
        if (0 == fieldOfViewAngleY) {
            fieldOfViewAngleY = 45;
        }
        final double widthHeightRatio = context.getCanvasWidthToHeightRatio();
        final double nearPlane = 0.1;
        final double farPlane = 10000000;
        glu.gluPerspective(fieldOfViewAngleY, widthHeightRatio, nearPlane, farPlane);
        float phi = (float) toRadians(xAngle);
        float tau = (float) toRadians(yAngle);
        final float nin = (float) toRadians(90);
        double centerX = 0;
        double centerY = 0;
        double centerZ = 0;
        final double cosPhi = cos(phi);
        final double sinTau = sin(tau);
        final double sinPhi = sin(phi);
        final double cosTau = cos(tau);
        double eyeX = centerX + distance * cosPhi * sinTau;
        double eyeY = centerY + distance * sinPhi * sinTau;
        double eyeZ = centerZ + distance * cosTau;
        final double sinTauNin = sin(tau - nin);
        final double cosTauNin = cos(tau - nin);
        double upX = cosPhi * sinTauNin;
        double upY = sinPhi * sinTauNin;
        double upZ = cosTauNin;
        glu.gluLookAt(eyeX, eyeY, eyeZ, centerX, centerY, centerZ, upX, upY, upZ);
        gl.glMatrixMode(GL.GL_MODELVIEW);
        gl.glLoadIdentity();
    }
}

package drk.graphics;

import drk.graphics.Camera;
import drk.Vector3D;
import javax.media.opengl.GL;
import java.lang.Math;

public class EulerCamera extends Camera {

    public double yrotation, xrotation;

    public EulerCamera() {
        super();
        yrotation = xrotation = 0.0;
    }

    public EulerCamera(Camera c) {
        super(c);
        Vector3D nyrot = new Vector3D(c.ZNormal.x, 0.0, c.ZNormal.z);
        double tm = nyrot.mag();
        nyrot.enormal();
        yrotation = Math.toDegrees(Math.atan2(nyrot.x, nyrot.z));
        xrotation = Math.toDegrees(Math.atan2(c.ZNormal.y, tm));
    }

    boolean isinit = false;

    public boolean isInitialized() {
        return isinit;
    }

    public void render(GL gl) {
        if (!isInitialized()) initialize(gl);
        double[] crm = new double[16];
        gl.glRotated(-xrotation, 1.0, 0.0, 0.0);
        gl.glRotated(-yrotation, 0.0, 1.0, 0.0);
        gl.glTranslated(-Position.x, -Position.y, -Position.z);
        gl.glGetDoublev(GL.GL_MODELVIEW_MATRIX, crm, 0);
        XNormal.x = crm[0];
        XNormal.y = crm[4];
        XNormal.z = crm[8];
        YNormal.x = crm[1];
        YNormal.y = crm[5];
        YNormal.z = crm[9];
        ZNormal.x = -crm[2];
        ZNormal.y = -crm[6];
        ZNormal.z = -crm[10];
    }
}

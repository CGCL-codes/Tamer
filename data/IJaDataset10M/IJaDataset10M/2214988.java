package xmage.turbine;

import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;

/**
 * Curve-based vertex animation interpolator using Hermite algorithm.
 */
public class TriMeshHermiteInterpolator implements TriMeshInterpolator {

    private DoubleBuffer vert0 = null;

    private DoubleBuffer vert1 = null;

    private DoubleBuffer vert2 = null;

    private DoubleBuffer vert3 = null;

    private DoubleBuffer vertd = null;

    private FloatBuffer norm0 = null;

    private FloatBuffer norm1 = null;

    private FloatBuffer norm2 = null;

    private FloatBuffer norm3 = null;

    private FloatBuffer normd = null;

    private FloatBuffer tc0 = null;

    private FloatBuffer tc1 = null;

    private FloatBuffer tc2 = null;

    private FloatBuffer tc3 = null;

    private FloatBuffer tcd = null;

    private float ft = 0.0f;

    private float ft2 = 0.0f;

    private float ft3 = 0.0f;

    private double dt = 0.0;

    private double dt2 = 0.0;

    private double dt3 = 0.0;

    private int i = 0;

    public TriMeshHermiteInterpolator() {
    }

    public void interpolate(TriMeshFrame beforeFrame, TriMeshFrame lastFrame, TriMeshFrame nextFrame, TriMeshFrame afterFrame, float ratio, TriMeshFrame destFrame, int numVerts) {
        vert0 = beforeFrame.getVertices();
        vert1 = lastFrame.getVertices();
        vert2 = nextFrame.getVertices();
        vert3 = afterFrame.getVertices();
        vertd = destFrame.getVertices();
        norm0 = beforeFrame.getNormals();
        norm1 = lastFrame.getNormals();
        norm2 = nextFrame.getNormals();
        norm3 = afterFrame.getNormals();
        normd = destFrame.getNormals();
        tc0 = beforeFrame.getTexCoords();
        tc1 = lastFrame.getTexCoords();
        tc2 = nextFrame.getTexCoords();
        tc3 = afterFrame.getTexCoords();
        tcd = destFrame.getTexCoords();
        vert0.position(0);
        vert1.position(0);
        vert2.position(0);
        vert3.position(0);
        vertd.position(0);
        norm0.position(0);
        norm1.position(0);
        norm2.position(0);
        norm3.position(0);
        normd.position(0);
        tc0.position(0);
        tc1.position(0);
        tc2.position(0);
        tc3.position(0);
        tcd.position(0);
        ft = ratio;
        ft2 = ft * ft;
        ft3 = ft * ft2;
        dt = ft;
        dt2 = ft2;
        dt3 = ft3;
        for (i = 0; i < numVerts; i++) {
            vertd.put(hi(vert0.get(), vert1.get(), vert2.get(), vert3.get(), dt, ft2, dt3, dtn2));
            vertd.put(hi(vert0.get(), vert1.get(), vert2.get(), vert3.get(), dt, dt2, dt3, dtn2));
            vertd.put(hi(vert0.get(), vert1.get(), vert2.get(), vert3.get(), dt, dt2, dt3, dtn2));
            normd.put(hi(norm0.get(), norm1.get(), norm2.get(), norm3.get(), ft, ft2, ft3, tn2));
            normd.put(hi(norm0.get(), norm1.get(), norm2.get(), norm3.get(), ft, ft2, ft3, tn2));
            normd.put(hi(norm0.get(), norm1.get(), norm2.get(), norm3.get(), ft, ft2, ft3, tn2));
            tcd.put(hi(tc0.get(), tc1.get(), tc2.get(), tc3.get(), ft, ft2, ft3, tn2));
            tcd.put(hi(tc0.get(), tc1.get(), tc2.get(), tc3.get(), ft, ft2, ft3, tn2));
        }
    }

    private float tension = 0.0f;

    private float tn2 = 0.0f;

    private double dtn2 = 0.0;

    public void setTension(float tension) {
        this.tension = tension;
        tn2 = (1.0f - tension) / 2.0f;
        dtn2 = tn2;
    }

    public float getTension() {
        return tension;
    }

    private double hi(double n0, double n1, double n2, double n3, double t, double t2, double t3, double tn) {
        return (((2.0 * t3) - (3.0 * t2) + 1.0) * n1) + ((t3 - (2.0 * t2) + t) * (((n1 - n0) * tn) + ((n2 - n1) * tn))) + ((t3 - t2) * (((n2 - n1) * tn) + ((n3 - n2) * tn))) + (((3.0 * t2) - (2.0 * t3)) * n2);
    }

    private float hi(float n0, float n1, float n2, float n3, float t, float t2, float t3, float tn) {
        return (((2.0f * t3) - (3.0f * t2) + 1.0f) * n1) + ((t3 - (2.0f * t2) + t) * (((n1 - n0) * tn) + ((n2 - n1) * tn))) + ((t3 - t2) * (((n2 - n1) * tn) + ((n3 - n2) * tn))) + (((3.0f * t2) - (2.0f * t3)) * n2);
    }
}

package de.grogra.mtg;

import javax.vecmath.Matrix4d;
import javax.vecmath.Point3f;
import javax.vecmath.Vector3d;

public class MTGBranchElement {

    private float length;

    private float topdia;

    private float botdia;

    private float alpha;

    private float beta;

    private float gamma;

    private int nodeIndex;

    private Vector3d _dirp;

    private Vector3d _dirs;

    private Vector3d _origin;

    private Point3f[] topSurface;

    private Point3f[] botSurface;

    private int numOfSurfacePoints;

    private int order;

    public void setOrder(int order) {
        this.order = order;
    }

    public int getOrder() {
        return order;
    }

    public int getNumOfSurfacePoints() {
        return numOfSurfacePoints;
    }

    public Point3f[] getTopSurface(MTGBranchElement nextElement) {
        if ((topSurface == null) || (botSurface == null)) {
            topSurface = new Point3f[numOfSurfacePoints];
            botSurface = new Point3f[numOfSurfacePoints];
            computeSurfaces(nextElement);
        }
        return topSurface;
    }

    public Point3f[] getBotSurface(MTGBranchElement nextElement) {
        if ((topSurface == null) || (botSurface == null)) {
            topSurface = new Point3f[numOfSurfacePoints];
            botSurface = new Point3f[numOfSurfacePoints];
            computeSurfaces(nextElement);
        }
        return botSurface;
    }

    private void computeSurfaces(MTGBranchElement nextElement) {
        Point3f pt = new Point3f((float) this._origin.x, (float) this._origin.y, (float) this._origin.z);
        Point3f vec;
        Point3f pt2;
        if (nextElement == null) {
            vec = new Point3f(this._dirp);
            vec.scale(this.length);
            pt2 = new Point3f(pt.x + vec.x, pt.y + vec.y, pt.z + vec.z);
        } else {
            pt2 = new Point3f(nextElement.getOrigin());
            vec = new Point3f();
            vec.sub(pt2, pt);
        }
        Matrix4d transToOrigin = new Matrix4d();
        transToOrigin.setIdentity();
        transToOrigin.setTranslation(new Vector3d(-pt.x, -pt.y, -pt.z));
        Point3f pt_t = new Point3f();
        Point3f pt2_t = new Point3f();
        transToOrigin.transform(pt, pt_t);
        transToOrigin.transform(pt2, pt2_t);
        vec.sub(pt2_t, pt_t);
        double rotateToYZPlaneAngle = 0;
        if ((vec.x == 0) && (vec.y >= 0)) rotateToYZPlaneAngle = 0; else if ((vec.x == 0) && (vec.y < 0)) rotateToYZPlaneAngle = Math.PI; else if ((vec.x > 0) && (vec.y > 0)) rotateToYZPlaneAngle = Math.atan(vec.x / vec.y); else if ((vec.x > 0) && (vec.y < 0)) rotateToYZPlaneAngle = Math.atan(Math.abs(vec.y) / vec.x) + (Math.PI / 2.0); else if ((vec.x < 0) && (vec.y < 0)) rotateToYZPlaneAngle = Math.atan(Math.abs(vec.x) / Math.abs(vec.y)) + Math.PI; else if ((vec.x < 0) && (vec.y > 0)) rotateToYZPlaneAngle = Math.atan(vec.y / Math.abs(vec.x)) + (Math.PI / 2.0 * 3.0); else if ((vec.y == 0) && (vec.x > 0)) rotateToYZPlaneAngle = (Math.PI / 2.0); else if ((vec.y == 0) && (vec.x < 0)) rotateToYZPlaneAngle = (Math.PI / 2.0 * 3.0);
        Matrix4d rotateToYZPlane = new Matrix4d();
        rotateToYZPlane.setIdentity();
        rotateToYZPlane.rotZ(rotateToYZPlaneAngle);
        Point3f pt_r1 = new Point3f();
        Point3f pt2_r1 = new Point3f();
        rotateToYZPlane.transform(pt_t, pt_r1);
        rotateToYZPlane.transform(pt2_t, pt2_r1);
        vec.sub(pt2_r1, pt_r1);
        double rotateToYAxisAngle = 0;
        if ((vec.z == 0) && (vec.y >= 0)) rotateToYAxisAngle = 0; else if ((vec.z == 0) && (vec.y < 0)) rotateToYAxisAngle = Math.PI; else if ((vec.z > 0) && (vec.y > 0)) rotateToYAxisAngle = Math.atan(vec.y / vec.z) + (Math.PI / 2.0 * 3.0); else if ((vec.z > 0) && (vec.y < 0)) rotateToYAxisAngle = Math.atan(vec.z / Math.abs(vec.y)) + (Math.PI); else if ((vec.z < 0) && (vec.y < 0)) rotateToYAxisAngle = Math.atan(Math.abs(vec.y) / Math.abs(vec.z)) + (Math.PI / 2.0); else if ((vec.z < 0) && (vec.y > 0)) rotateToYAxisAngle = Math.atan(Math.abs(vec.z) / vec.y); else if ((vec.y == 0) && (vec.z > 0)) rotateToYAxisAngle = (Math.PI / 2.0 * 3.0); else if ((vec.y == 0) && (vec.z < 0)) rotateToYAxisAngle = (Math.PI / 2.0);
        Matrix4d rotateToYAxis = new Matrix4d();
        rotateToYAxis.setIdentity();
        rotateToYAxis.rotX(rotateToYAxisAngle);
        Point3f pt_r2 = new Point3f();
        Point3f pt2_r2 = new Point3f();
        rotateToYAxis.transform(pt_r1, pt_r2);
        rotateToYAxis.transform(pt2_r1, pt2_r2);
        Point3f pt_a = new Point3f(pt_r2);
        Point3f pt2_a = new Point3f(pt2_r2);
        double angleIncrement = (Math.PI * 2.0 / numOfSurfacePoints);
        for (int i = 0; i < numOfSurfacePoints; i++) {
            double x = Math.cos((double) (i + 1) * angleIncrement) * botdia;
            double z = Math.sin((double) (i + 1) * angleIncrement) * botdia;
            botSurface[i] = new Point3f((float) x, 0, (float) z);
        }
        for (int i = 0; i < numOfSurfacePoints; i++) {
            double x = Math.cos((double) (i + 1) * angleIncrement) * topdia;
            double z = Math.sin((double) (i + 1) * angleIncrement) * topdia;
            topSurface[i] = new Point3f((float) x, pt2_a.y, (float) z);
        }
        rotateToYAxis.invert();
        rotateToYZPlane.invert();
        transToOrigin.invert();
        for (int i = 0; i < botSurface.length; ++i) {
            rotateToYAxis.transform(botSurface[i]);
            rotateToYZPlane.transform(botSurface[i]);
            transToOrigin.transform(botSurface[i]);
        }
        for (int i = 0; i < topSurface.length; ++i) {
            rotateToYAxis.transform(topSurface[i]);
            rotateToYZPlane.transform(topSurface[i]);
            transToOrigin.transform(topSurface[i]);
        }
    }

    public float getLength() {
        return length;
    }

    public void setLength(float length) {
        this.length = length;
    }

    public float getTopDia() {
        return topdia;
    }

    public void setTopDia(float topdia) {
        this.topdia = topdia;
    }

    public float getBotDia() {
        return botdia;
    }

    public void setBotDia(float botdia) {
        this.botdia = botdia;
    }

    public float getAlpha() {
        return alpha;
    }

    public void setAlpha(float alpha) {
        this.alpha = alpha;
    }

    public float getGamma() {
        return gamma;
    }

    public void setGamma(float gamma) {
        this.gamma = gamma;
    }

    public Vector3d getDirp() {
        return _dirp;
    }

    public void setDirp(Vector3d _dirp) {
        if (this._dirp == null) this._dirp = new Vector3d();
        this._dirp.set(_dirp);
    }

    public Vector3d getDirs() {
        return _dirs;
    }

    public void setDirs(Vector3d _dirs) {
        if (this._dirs == null) this._dirs = new Vector3d();
        this._dirs.set(_dirs);
    }

    public Vector3d getOrigin() {
        return _origin;
    }

    public void setOrigin(Vector3d _origin) {
        if (this._origin == null) this._origin = new Vector3d();
        this._origin.set(_origin);
    }

    public MTGBranchElement(int _nodeIndex) {
        nodeIndex = _nodeIndex;
        length = 0;
        topdia = -1;
        botdia = -1;
        alpha = 0;
        beta = 0;
        gamma = 0;
        numOfSurfacePoints = 16;
        order = -1;
    }

    public int getNodeIndex() {
        return nodeIndex;
    }

    public float getBeta() {
        return beta;
    }

    public void setBeta(float beta) {
        this.beta = beta;
    }
}

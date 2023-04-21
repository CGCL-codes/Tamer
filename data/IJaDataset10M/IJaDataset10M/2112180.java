package org.xith3d.loaders.models.impl.bsp.util;

import javax.vecmath.Point3f;
import org.xith3d.loaders.models.impl.bsp.lumps.BSPVertex;

/**
 * TODO: Insert package comments here
 * 
 * @author David Yazel
 */
public class PatchSurface {

    private static final int MAXMESHLEVEL = 2;

    private static final float MINDIST = 0.5f * 0.5f;

    private int mCount;

    public BSPVertex mPoints[];

    public int mIndices[];

    private int sizex = 0;

    private int sizey = 0;

    private static Point3f a = new Point3f();

    private static Point3f b = new Point3f();

    private static Point3f v0 = new Point3f();

    private static Point3f v1 = new Point3f();

    private static Point3f v2 = new Point3f();

    private int getLevelWidth(int lvl) {
        return ((1 << (lvl + 1)) + 1);
    }

    private boolean findSize(int controlx, int controly, BSPVertex cp[]) {
        boolean found = false;
        BSPVertex a = null;
        BSPVertex b = null;
        int ai = 0;
        int bi = 0;
        for (int v = 0; v < controly; v++) {
            for (int u = 0; u < controlx; u += 2) {
                ai = v * controlx + u;
                bi = v * controlx + u + 2;
                a = cp[ai];
                b = cp[bi];
                if ((a.position.x != b.position.x) || (a.position.y != b.position.y) || (a.position.z != b.position.z)) {
                    found = true;
                    break;
                }
            }
            if (found) break;
        }
        if (!found) {
            System.out.println("bad mesh control points\n");
            return false;
        }
        int levelx = findLevel(cp[ai].position, cp[ai + 1].position, cp[bi].position);
        sizex = (getLevelWidth(levelx) - 1) * ((controlx - 1) / 2) + 1;
        for (int u = 0; u < controlx; u++) {
            for (int v = 0; v < controly; v += 2) {
                ai = v * controlx + u;
                bi = ((v + 2) * controlx) + u;
                a = cp[ai];
                b = cp[bi];
                if ((a.position.x != b.position.x) || (a.position.y != b.position.y) || (a.position.z != b.position.z)) {
                    found = true;
                    break;
                }
            }
            if (found) break;
        }
        if (!found) {
            return (false);
        }
        int levely = findLevel(cp[ai].position, cp[ai + 1].position, cp[bi].position);
        sizey = (getLevelWidth(levely) - 1) * ((controly - 1) / 2) + 1;
        return (true);
    }

    private int findLevel(Point3f cv0, Point3f cv1, Point3f cv2) {
        int level;
        a.set(0f, 0f, 0f);
        b.set(0f, 0f, 0f);
        v0.set(cv0);
        v1.set(cv1);
        v2.set(cv2);
        for (level = 0; level < MAXMESHLEVEL - 1; level++) {
            a.interpolate(v0, v1, 0.5f);
            b.interpolate(v1, v2, 0.5f);
            v2.interpolate(a, b, 0.5f);
            if (v2.distanceSquared(v1) < MINDIST) break;
            v1.set(a);
        }
        return (level);
    }

    private void fillPatch(int controlx, int controly, BSPVertex p[]) {
        int stepx = (sizex - 1) / (controlx - 1);
        for (int u = 0; u < sizex; u += stepx) fillCurve(controly, sizey, sizex, p, u);
        for (int v = 0; v < sizey; v++) fillCurve(controlx, sizex, 1, p, v * sizex);
    }

    private void fillCurve(int numcp, int size, int stride, BSPVertex p[], int start) {
        int step, halfstep, i, mid;
        BSPVertex a = new BSPVertex();
        BSPVertex b = new BSPVertex();
        step = (size - 1) / (numcp - 1);
        while (step > 0) {
            halfstep = step / 2;
            for (i = 0; i < size - 1; i += step * 2) {
                mid = (i + step) * stride;
                a.avg(p[start + i * stride], p[start + mid]);
                b.avg(p[start + mid], p[start + (i + step * 2) * stride]);
                p[mid + start].avg(a, b);
                if (halfstep > 0) {
                    p[start + (i + halfstep) * stride] = a.copy();
                    p[start + (i + 3 * halfstep) * stride] = b.copy();
                }
            }
            step /= 2;
        }
    }

    public PatchSurface(BSPVertex cp[], int npoints, int controlx, int controly) {
        findSize(controlx, controly, cp);
        int size = sizex * sizey;
        mPoints = new BSPVertex[size];
        for (int i = 0; i < size; i++) mPoints[i] = new BSPVertex();
        int stepx = (sizex - 1) / (controlx - 1);
        int stepy = (sizey - 1) / (controly - 1);
        int cv = 0;
        for (int y = 0; y < sizey; y += stepy) {
            for (int x = 0; x < sizex; x += stepx) {
                int p = y * sizex + x;
                mPoints[p] = cp[cv++].copy();
            }
        }
        fillPatch(controlx, controly, mPoints);
        mCount = (sizex - 1) * (sizey - 1) * 6;
        mIndices = new int[mCount];
        int ii = 0;
        for (int y = 0; y < sizey - 1; ++y) {
            for (int x = 0; x < sizex - 1; ++x) {
                mIndices[ii++] = y * sizex + x;
                mIndices[ii++] = (y + 1) * sizex + x;
                mIndices[ii++] = y * sizex + x + 1;
                mIndices[ii++] = y * sizex + x + 1;
                mIndices[ii++] = (y + 1) * sizex + x;
                mIndices[ii++] = (y + 1) * sizex + x + 1;
            }
        }
    }
}

package com.embeddedmicro.branch;

import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;

public class Branch {

    private long animationTime;

    private Vector2D[] center;

    private Vector2D[] nVec, wVec;

    private Vector2D[] tip;

    private Bezier[] c_bez;

    private int nVert;

    private long genTime;

    private long growDur;

    private float width, base, length;

    private Rectangle bounds;

    private float start_angle;

    public boolean grown, split;

    public long fadeTime;

    private Path draw;

    private Matrix matrix;

    public Paint paint;

    public boolean active;

    public int zbuf;

    Branch(int mV) {
        active = false;
        bounds = new Rectangle();
        draw = new Path();
        matrix = new Matrix();
        tip = new Vector2D[3];
        tip[0] = new Vector2D();
        tip[1] = new Vector2D();
        tip[2] = new Vector2D();
        paint = new Paint();
        set_maxVert(mV);
        reset();
    }

    public void reset() {
        genTime = System.currentTimeMillis();
        fadeTime = 0;
        grown = false;
        split = false;
    }

    public long get_age() {
        return animationTime;
    }

    public float get_length() {
        return length;
    }

    public void set_length(float l) {
        length = l;
    }

    public float get_percent_grown() {
        return animationTime / growDur;
    }

    public void set_growDur(long time) {
        growDur = time;
    }

    public void set_start_angle(float a) {
        start_angle = a;
    }

    public void set_nVert(int v) {
        nVert = v;
    }

    public void set_maxVert(int v) {
        nVec = new Vector2D[v];
        wVec = new Vector2D[v];
        center = new Vector2D[v];
        for (int i = 0; i < v; i++) {
            wVec[i] = new Vector2D();
            nVec[i] = new Vector2D();
            center[i] = new Vector2D();
        }
        c_bez = new Bezier[v - 1];
        for (int i = 0; i < v - 1; i++) {
            c_bez[i] = new Bezier();
        }
    }

    public Vector2D get_point(float t) {
        float u = t * (nVert - 1);
        int uint = (int) Math.floor(u);
        float uf = u - uint;
        return c_bez[uint].getPoint(uf);
    }

    public float get_angle(float t) {
        float u = t * (nVert - 1);
        int uint = (int) Math.floor(u);
        float uf = u - uint;
        return c_bez[uint].getnAngle(uf);
    }

    public void set_center_path(Vector2D[] vec) {
        for (int i = 0; i < nVert; i++) {
            center[i].set(vec[i]);
        }
        update_bounds();
    }

    public void set_point(int i, float x, float y) {
        center[i].set(x, y);
    }

    public void set_point(int i, Vector2D v) {
        center[i].set(v);
    }

    public Vector2D[] get_center_path() {
        return center;
    }

    private void update_bounds() {
        float minx, miny, maxx, maxy;
        minx = maxx = center[0].x;
        miny = maxy = center[0].y;
        for (int i = 1; i < nVert; i++) {
            if (center[i].x > maxx) maxx = center[i].x;
            if (center[i].x < minx) minx = center[i].x;
            if (center[i].y > maxy) maxy = center[i].y;
            if (center[i].y < miny) miny = center[i].y;
        }
        bounds.min.x = minx;
        bounds.min.y = miny;
        bounds.max.x = maxx;
        bounds.max.y = maxy;
    }

    public Rectangle get_bounds() {
        return bounds;
    }

    public int get_nVert() {
        return nVert;
    }

    public Vector2D get_cv(int i) {
        return center[i];
    }

    public void set_width(float w) {
        width = w;
    }

    public float get_width() {
        return width;
    }

    public void generate_center_curve() {
        c_bez[0].setCurve(center[0], center[0], center[1], center[2]);
        for (int i = 1; i < nVert - 2; i++) {
            c_bez[i].setCurve(center[i - 1], center[i], center[i + 1], center[i + 2]);
        }
        c_bez[nVert - 2].setCurve(center[nVert - 3], center[nVert - 2], center[nVert - 1], center[nVert - 1]);
    }

    public void generate_normals() {
        nVec[0].setPtDA(1.0f, (float) (start_angle - Math.PI / 2));
        for (int i = 1; i < nVert - 1; i++) {
            nVec[i].setPtDA(1.0f, c_bez[i].getiAngle());
        }
        nVec[nVert - 1].set(0.0f, 0.0f);
    }

    private void generate_widths() {
        float tgen = (float) animationTime / growDur;
        if (tgen > 1.0f) tgen = 1.0f;
        tgen = (tgen - 1) * (tgen - 1) * (tgen - 1) + 1;
        base = width * tgen;
        float ratio = base / ((nVert - 1) * tgen);
        for (int i = 0; i < nVert; i++) {
            wVec[i].x = nVec[i].x;
            wVec[i].y = nVec[i].y;
            wVec[i].scale(base - (ratio * i));
        }
    }

    public void transform(Vector2D origin, Vector2D move, float scale) {
        for (int i = 0; i < nVert; i++) {
            center[i].transform(origin, move, scale);
        }
        for (int i = 0; i < nVert - 1; i++) {
            c_bez[i].transform(origin, move, scale);
        }
        bounds.transform(origin, move, scale);
        width *= scale;
        length *= scale;
        if (grown) {
            matrix.setScale(scale, scale, origin.x, origin.y);
            draw.transform(matrix);
        }
    }

    private void set_tip(float t, float x0, float y0, float x1, float y1, float x2, float y2, float x3, float y3) {
        if (t == 0.0f) {
            tip[0].set(x0, y0);
            tip[1].set(x0, y0);
            tip[2].set(x0, y0);
            return;
        } else if (t == 1.0f) {
            tip[0].set(x1, y1);
            tip[1].set(x2, y2);
            tip[2].set(x3, y3);
            return;
        }
        float qx1 = x0 + (x1 - x0) * t;
        float qy1 = y0 + (y1 - y0) * t;
        float qx2 = x1 + (x2 - x1) * t;
        float qy2 = y1 + (y2 - y1) * t;
        float qx3 = x2 + (x3 - x2) * t;
        float qy3 = y2 + (y3 - y2) * t;
        float rx2 = qx1 + (qx2 - qx1) * t;
        float ry2 = qy1 + (qy2 - qy1) * t;
        float rx3 = qx2 + (qx3 - qx2) * t;
        float ry3 = qy2 + (qy3 - qy2) * t;
        float bx3 = rx2 + (rx3 - rx2) * t;
        float by3 = ry2 + (ry3 - ry2) * t;
        tip[0].set(qx1, qy1);
        tip[1].set(rx2, ry2);
        tip[2].set(bx3, by3);
    }

    public Path generate_path() {
        animationTime = System.currentTimeMillis() - genTime;
        float tgen = (float) animationTime / growDur;
        tgen = (tgen - 1) * (tgen - 1) * (tgen - 1) + 1;
        if (grown == false) {
            if (tgen > 1.0f) {
                tgen = 1.0f;
                grown = true;
            }
            float u = tgen * (nVert - 1);
            int uint = (int) Math.floor(u);
            float uf = u - uint;
            generate_widths();
            draw.reset();
            try {
                draw.moveTo(c_bez[0].a.x + wVec[0].x, c_bez[0].a.y + wVec[0].y);
                for (int i = 0; i < uint; i++) {
                    draw.cubicTo(c_bez[i].b.x + wVec[i].x, c_bez[i].b.y + wVec[i].y, c_bez[i].c.x + wVec[i + 1].x, c_bez[i].c.y + wVec[i + 1].y, c_bez[i].d.x + wVec[i + 1].x, c_bez[i].d.y + wVec[i + 1].y);
                }
                if (uf > 0 && uint < c_bez.length) {
                    set_tip(uf, c_bez[uint].a.x, c_bez[uint].a.y, c_bez[uint].b.x, c_bez[uint].b.y, c_bez[uint].c.x, c_bez[uint].c.y, c_bez[uint].d.x, c_bez[uint].d.y);
                    draw.cubicTo(tip[0].x + wVec[uint].x, tip[0].y + wVec[uint].y, tip[1].x, tip[1].y, tip[2].x, tip[2].y);
                    draw.cubicTo(tip[1].x, tip[1].y, tip[0].x - wVec[uint].x, tip[0].y - wVec[uint].y, c_bez[uint].a.x - wVec[uint].x, c_bez[uint].a.y - wVec[uint].y);
                }
                for (int i = uint - 1; i >= 0; i--) {
                    draw.cubicTo(c_bez[i].c.x - wVec[i + 1].x, c_bez[i].c.y - wVec[i + 1].y, c_bez[i].b.x - wVec[i].x, c_bez[i].b.y - wVec[i].y, c_bez[i].a.x - wVec[i].x, c_bez[i].a.y - wVec[i].y);
                }
                draw.cubicTo(c_bez[0].a.x - wVec[0].x + wVec[0].y, c_bez[0].a.y - wVec[0].y - wVec[0].x, c_bez[0].a.x + wVec[0].x + wVec[0].y, c_bez[0].a.y + wVec[0].y - wVec[0].x, c_bez[0].a.x + wVec[0].x, c_bez[0].a.y + wVec[0].y);
                draw.close();
            } catch (ArrayIndexOutOfBoundsException e) {
            }
        }
        return draw;
    }
}

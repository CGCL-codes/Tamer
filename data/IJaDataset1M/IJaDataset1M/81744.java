package com.sun.pisces;

public class Transformer extends PathSink {

    PathSink output;

    long m00, m01, m02;

    long m10, m11, m12;

    boolean scaleAndTranslate;

    public Transformer() {
    }

    public Transformer(PathSink output, Transform6 transform) {
        if (output instanceof Transformer) {
            Transformer t = (Transformer) output;
            this.output = t.output;
            this.m00 = (transform.m00 * t.m00 + transform.m10 * t.m01) >> 16;
            this.m01 = (transform.m01 * t.m00 + transform.m11 * t.m01) >> 16;
            this.m10 = (transform.m00 * t.m10 + transform.m10 * t.m11) >> 16;
            this.m11 = (transform.m01 * t.m10 + transform.m11 * t.m11) >> 16;
            this.m02 = transform.m02 * t.m00 + transform.m12 * t.m01 + t.m02;
            this.m12 = transform.m02 * t.m10 + transform.m12 * t.m11 + t.m12;
        } else {
            this.output = output;
            setTransform(transform);
        }
        classify();
    }

    public void setTransform(Transform6 transform) {
        this.m00 = (long) transform.m00;
        this.m01 = (long) transform.m01;
        this.m02 = (long) transform.m02 << 16;
        this.m10 = (long) transform.m10;
        this.m11 = (long) transform.m11;
        this.m12 = (long) transform.m12 << 16;
        classify();
    }

    private void classify() {
        if (m01 == 0 && m10 == 0) {
            scaleAndTranslate = true;
        } else {
            scaleAndTranslate = false;
        }
    }

    public void setOutput(PathSink output) {
        this.output = output;
    }

    public void moveTo(int x0, int y0) {
        long tx0, ty0;
        if (scaleAndTranslate) {
            tx0 = m00 * x0 + m02;
            ty0 = m11 * y0 + m12;
        } else {
            tx0 = m00 * x0 + m01 * y0 + m02;
            ty0 = m10 * x0 + m11 * y0 + m12;
        }
        output.moveTo((int) (tx0 >> 16), (int) (ty0 >> 16));
    }

    public void lineJoin() {
        output.lineJoin();
    }

    public void lineTo(int x1, int y1) {
        long tx1, ty1;
        if (scaleAndTranslate) {
            tx1 = m00 * x1 + m02;
            ty1 = m11 * y1 + m12;
        } else {
            tx1 = m00 * x1 + m01 * y1 + m02;
            ty1 = m10 * x1 + m11 * y1 + m12;
        }
        output.lineTo((int) (tx1 >> 16), (int) (ty1 >> 16));
    }

    public void quadTo(int x1, int y1, int x2, int y2) {
        long tx1, ty1, tx2, ty2;
        if (scaleAndTranslate) {
            tx1 = m00 * x1 + m02;
            ty1 = m11 * y1 + m12;
            tx2 = m00 * x2 + m02;
            ty2 = m11 * y2 + m12;
        } else {
            tx1 = m00 * x1 + m01 * y1 + m02;
            ty1 = m10 * x1 + m11 * y1 + m12;
            tx2 = m00 * x2 + m01 * y2 + m02;
            ty2 = m10 * x2 + m11 * y2 + m12;
        }
        output.quadTo((int) (tx1 >> 16), (int) (ty1 >> 16), (int) (tx2 >> 16), (int) (ty2 >> 16));
    }

    public void cubicTo(int x1, int y1, int x2, int y2, int x3, int y3) {
        long tx1, ty1, tx2, ty2, tx3, ty3;
        if (scaleAndTranslate) {
            tx1 = m00 * x1 + m02;
            ty1 = m11 * y1 + m12;
            tx2 = m00 * x2 + m02;
            ty2 = m11 * y2 + m12;
            tx3 = m00 * x3 + m02;
            ty3 = m11 * y3 + m12;
        } else {
            tx1 = m00 * x1 + m01 * y1 + m02;
            ty1 = m10 * x1 + m11 * y1 + m12;
            tx2 = m00 * x2 + m01 * y2 + m02;
            ty2 = m10 * x2 + m11 * y2 + m12;
            tx3 = m00 * x3 + m01 * y3 + m02;
            ty3 = m10 * x3 + m11 * y3 + m12;
        }
        output.cubicTo((int) (tx1 >> 16), (int) (ty1 >> 16), (int) (tx2 >> 16), (int) (ty2 >> 16), (int) (tx3 >> 16), (int) (ty3 >> 16));
    }

    public void close() {
        output.close();
    }

    public void end() {
        output.end();
    }
}

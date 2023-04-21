package net.wrburns.joctree;

import net.wrburns.joctree.Vector3;

public class Matrix3 {

    public double s11, s12, s13, s21, s22, s23, s31, s32, s33;

    public void loadIdentity() {
        s11 = s22 = s33 = 1d;
        s12 = s13 = s21 = s23 = s31 = s32 = 0d;
    }

    public Matrix3 multiply(Matrix3 b) {
        Matrix3 res = new Matrix3();
        res.s11 = (s11 * b.s11) + (s12 * b.s21) + (s13 * b.s31);
        res.s12 = (s11 * b.s12) + (s12 * b.s22) + (s13 * b.s32);
        res.s13 = (s11 * b.s13) + (s12 * b.s23) + (s13 * b.s33);
        res.s21 = (s21 * b.s11) + (s22 * b.s21) + (s23 * b.s31);
        res.s22 = (s21 * b.s12) + (s22 * b.s22) + (s23 * b.s32);
        res.s23 = (s21 * b.s13) + (s22 * b.s23) + (s23 * b.s33);
        res.s31 = (s31 * b.s11) + (s32 * b.s21) + (s33 * b.s31);
        res.s32 = (s31 * b.s12) + (s32 * b.s22) + (s33 * b.s32);
        res.s33 = (s31 * b.s13) + (s32 * b.s23) + (s33 * b.s33);
        return res;
    }

    public static Matrix3 newXRotation(double deg) {
        Matrix3 m = new Matrix3();
        double rad;
        rad = Math.toRadians(deg);
        m.s11 = 1;
        m.s12 = m.s13 = m.s21 = m.s31 = 0;
        m.s22 = m.s33 = Math.cos(rad);
        m.s32 = Math.sin(rad);
        m.s23 = -m.s32;
        return m;
    }

    public static Matrix3 newYRotation(double deg) {
        Matrix3 m = new Matrix3();
        double rad;
        rad = Math.toRadians(deg);
        m.s22 = 1;
        m.s12 = m.s23 = m.s21 = m.s32 = 0;
        m.s11 = m.s33 = Math.cos(rad);
        m.s13 = Math.sin(rad);
        m.s31 = -m.s13;
        return m;
    }

    public static Matrix3 newZRotation(double deg) {
        Matrix3 m = new Matrix3();
        double rad;
        rad = Math.toRadians(deg);
        m.s33 = 1;
        m.s13 = m.s23 = m.s31 = m.s32 = 0;
        m.s11 = m.s22 = Math.cos(rad);
        m.s21 = Math.sin(rad);
        m.s12 = -m.s21;
        return m;
    }
}

package trb.fps.util;

import trb.jsg.util.Vec3;

/**
 * Calculates the closest points between two line segments. This class can be
 * reused. However it is not thread safe.
 *
 * Taken from Real Time Collision Detection, page 149.
 * 
 * @author tomrbryn
 */
public class SegmentSegmentDistance {

    private static final float EPSILON = 0.00001f;

    /** closest squared distance between segments */
    public float squaredDistance = 0f;

    /** S1 intersection parameter */
    public float s = 0f;

    /** S2 intersection parameter */
    public float t = 0f;

    /** Closest point on S1*/
    public Vec3 c1 = new Vec3();

    /** Closest point on S2*/
    public Vec3 c2 = new Vec3();

    private Vec3 d1 = new Vec3();

    private Vec3 d2 = new Vec3();

    private Vec3 r = new Vec3();

    private Vec3 temp = new Vec3();

    /**
     * @return the squared distance between between line segments
     */
    public float calculate(Vec3 p1, Vec3 q1, Vec3 p2, Vec3 q2) {
        d1.sub(q1, p1);
        d2.sub(q2, p2);
        r.sub(p1, p2);
        float a = d1.dot(d1);
        float e = d2.dot(d2);
        float f = d2.dot(r);
        if (a <= EPSILON && e <= EPSILON) {
            s = 0f;
            t = 0f;
            c1.set(p1);
            c2.set(p2);
            temp.sub(c1, c2);
            return temp.dot(temp);
        }
        if (a <= EPSILON) {
            s = 0f;
            t = f / e;
            t = clamp(t, 0f, 1f);
        } else {
            float c = d1.dot(r);
            if (e <= EPSILON) {
                t = 0f;
                s = clamp(-c / a, 0f, 1f);
            } else {
                float b = d1.dot(d2);
                float denom = a * e - b * b;
                if (denom != 0f) {
                    s = clamp((b * f - c * e) / denom, 0f, 1f);
                } else {
                    s = 0f;
                }
                float tnom = b * s + f;
                if (tnom < 0f) {
                    t = 0f;
                    s = clamp(-c / a, 0f, 1f);
                } else if (tnom > e) {
                    t = 1f;
                    s = clamp((b - c) / a, 0f, 1f);
                } else {
                    t = tnom / e;
                }
            }
        }
        c1.scaleAdd(s, d1, p1);
        c2.scaleAdd(t, d2, p2);
        temp.sub(c1, c2);
        squaredDistance = temp.dot(temp);
        return squaredDistance;
    }

    private float clamp(float n, float min, float max) {
        if (n < min) return min;
        if (n > max) return max;
        return n;
    }

    public static void main(String[] args) {
        Vec3 p1 = new Vec3(0, 0, -10);
        Vec3 q1 = new Vec3(0, 0, -2);
        Vec3 p2 = new Vec3(0, -10, 0);
        Vec3 q2 = new Vec3(0, 10, 0);
        SegmentSegmentDistance calc = new SegmentSegmentDistance();
        float distance = calc.calculate(p1, q1, p2, q2);
        System.out.println(distance + " " + calc.c1 + " " + calc.c2 + " " + calc.s + " " + calc.t);
    }
}

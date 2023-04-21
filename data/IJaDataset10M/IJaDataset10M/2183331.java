package me.fantasy2.stg;

import me.fantasy2.MathUtil;
import odk.lang.FastMath;

/**
 * @author Cloudee
 * 
 */
public class BulletUtils {

    public static boolean isTouchBullet(float bx, float by, float br, float x0, float y0, float r0) {
        float dx = (bx - x0);
        float dy = (by - y0);
        float dr = (br + r0);
        return dr * dr >= dx * dx + dy * dy;
    }

    public static boolean isTouchLaser(float p1x, float p1y, float p2x, float p2y, float laserR, float x0, float y0, float r0) {
        r0 += laserR;
        boolean ret;
        if (p1x == p2x) {
            if (p1y == p2y) {
                ret = MathUtil.distance2(x0, y0, p1x, p1y) < r0 * r0;
            } else {
                if (MathUtil.between(y0, p1y, p2y)) {
                    ret = FastMath.abs(p1x - x0) < r0;
                } else {
                    float r2 = r0 * r0;
                    ret = MathUtil.distance2(x0, y0, p1x, p1y) < r2 || MathUtil.distance2(x0, y0, p2x, p2y) < r2;
                }
            }
        } else {
            float k = (p2y - p1y) / (p2x - p1x);
            float kk = k * k;
            float x = (kk * p1x + k * (y0 - p1y) + x0) / (kk + 1);
            float y = k * (x - p1x) + p1y;
            if (MathUtil.between(x, p1x, p2x)) {
                ret = MathUtil.distance2(x0, y0, x, y) < r0 * r0;
            } else {
                float r2 = r0 * r0;
                ret = MathUtil.distance2(x0, y0, p1x, p1y) < r2 || MathUtil.distance2(x0, y0, p2x, p2y) < r2;
            }
        }
        return ret;
    }
}

package utils.ifs;

import utils.linear.LVect2d;

/**
 *
 * @author Calvin Ashmore
 */
public class LinearMapping_v2 implements IFSUtil.Mapping_v2 {

    private double a, b, c, d, e, f;

    public LinearMapping_v2(double a, double b, double c, double d, double e, double f) {
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
        this.e = e;
        this.f = f;
    }

    public LVect2d map(LVect2d point, LVect2d dest) {
        dest.x = a * point.x + b * point.y + c;
        dest.y = d * point.x + e * point.y + f;
        return dest;
    }
}

package utils.pointfield.attractor;

/**
 *
 * @author Calvin Ashmore
 */
public class APoint3d extends APoint {

    public APoint3d() {
    }

    public APoint3d(APoint3d source) {
        x = source.x;
        y = source.y;
        z = source.z;
        index = source.index;
    }

    public double diff(APoint last) {
        APoint3d last1 = (APoint3d) last;
        return Math.abs(last1.x - x) + Math.abs(last1.y - y) + Math.abs(last1.z - z);
    }

    public double bound() {
        if (Double.isNaN(x) || Double.isNaN(y) || Double.isNaN(z)) return 100000;
        return Math.max(Math.abs(x), Math.max(Math.abs(y), Math.abs(z)));
    }

    public void setMinValues() {
        x = -Float.MAX_VALUE;
        y = -Float.MAX_VALUE;
        z = -Float.MAX_VALUE;
    }

    public void setMaxValues() {
        x = Float.MAX_VALUE;
        y = Float.MAX_VALUE;
        z = Float.MAX_VALUE;
    }

    public void minimize(APoint point) {
        APoint3d point1 = (APoint3d) point;
        x = Math.min(x, point1.x);
        y = Math.min(y, point1.y);
        z = Math.min(z, point1.z);
    }

    public void maximize(APoint point) {
        APoint3d point1 = (APoint3d) point;
        x = Math.max(x, point1.x);
        y = Math.max(y, point1.y);
        z = Math.max(z, point1.z);
    }

    public double z;

    public final APoint3d clone() {
        return new APoint3d(this);
    }
}

package jp.go.ipa.jgcl;

/**
 *  Q     :      `   v f           _   \       N   X B
 * <p>
 *      N   X   C   X ^   X   A
 *  `   v f           _     W l ({@link Pnt2D Pnt2D}) point
 *            B
 * </p>
 * <p>
 * point    null      \       B
 * </p>
 *
 * @version $Revision: 1.14 $, $Date: 2000/04/26 09:39:17 $
 * @author Information-technology Promotion Agency, Japan
 */
public abstract class PointOnGeometry2D extends Pnt2D {

    /**
	 *  `   v f           _     W l B
	 * <p>
	 * null      \       B
	 * </p>
	 * @serial
	 */
    private Pnt2D point;

    /**
	 *  `   v f           _     W l   ^     I u W F N g   \ z     B
	 * <p>
	 * point    null      \       B
	 * </p>
	 *
	 * @param point	 `   v f     _     W l
	 */
    protected PointOnGeometry2D(Pnt2D point) {
        super();
        this.point = point;
    }

    /**
	 *  `   v f           _     W l       B
	 * <p>
	 * null                      B
	 * </p>
	 *
	 * @return	 `   v f           _     W l
	 */
    public Pnt2D point() {
        return point;
    }

    /**
	 *      _   X    W l       B
	 *
	 * @return	 _   X    W l
	 */
    public double x() {
        if (point == null) {
            point = this.coordinates();
        }
        return point.x();
    }

    /**
	 *      _   Y    W l       B
	 *
	 * @return	 _   Y    W l
	 */
    public double y() {
        if (point == null) {
            point = coordinates();
        }
        return point.y();
    }

    /**
	 *  x [ X       `   v f             \ b h B
	 *
	 * @return	 x [ X       `   v f
	 */
    public abstract Geometry geometry();

    /**
	 *  x [ X       `   v f                 _     W l               \ b h B
	 *
	 * @return	 x [ X       `   v f                       _     W l
	 */
    abstract Pnt2D coordinates();
}

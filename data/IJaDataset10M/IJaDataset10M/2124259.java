package com.vividsolutions.jts.operation.overlay.snap;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.CoordinateFilter;
import com.vividsolutions.jts.geom.Geometry;

/**
 * Removes common most-significant mantissa bits from one or more
 * {@link Geometry}s.
 * <p>
 * The CommonBitsRemover "scavenges" precision which is "wasted" by a large
 * displacement of the geometry from the origin. For example, if a small
 * geometry is displaced from the origin by a large distance, the displacement
 * increases the significant figures in the coordinates, but does not affect the
 * <i>relative</i> topology of the geometry. Thus the geometry can be translated
 * back to the origin without affecting its topology. In order to compute the
 * translation without affecting the full precision of the coordinate values,
 * the translation is performed at the bit level by removing the common leading
 * mantissa bits.
 * <p>
 * If the geometry envelope already contains the origin, the translation
 * procedure cannot be applied. In this case, the common bits value is computed
 * as zero.
 * <p>
 * If the geometry crosses the Y axis but not the X axis (and <i>mutatis
 * mutandum</i>), the common bits for Y are zero, but the common bits for X are
 * non-zero.
 * 
 * @version 1.7
 */
public class CommonBitsRemover {

    private Coordinate commonCoord;

    private CommonCoordinateFilter ccFilter = new CommonCoordinateFilter();

    public CommonBitsRemover() {
    }

    /**
	 * Add a geometry to the set of geometries whose common bits are being
	 * computed. After this method has executed the common coordinate reflects
	 * the common bits of all added geometries.
	 * 
	 * @param geom
	 *            a Geometry to test for common bits
	 */
    public void add(Geometry geom) {
        geom.apply(ccFilter);
        commonCoord = ccFilter.getCommonCoordinate();
    }

    /**
	 * The common bits of the Coordinates in the supplied Geometries.
	 */
    public Coordinate getCommonCoordinate() {
        return commonCoord;
    }

    /**
	 * Removes the common coordinate bits from a Geometry. The coordinates of
	 * the Geometry are changed.
	 * 
	 * @param geom
	 *            the Geometry from which to remove the common coordinate bits
	 * @return the shifted Geometry
	 */
    public Geometry removeCommonBits(Geometry geom) {
        if (commonCoord.x == 0.0 && commonCoord.y == 0.0) return geom;
        Coordinate invCoord = new Coordinate(commonCoord);
        invCoord.x = -invCoord.x;
        invCoord.y = -invCoord.y;
        Translater trans = new Translater(invCoord);
        geom.apply(trans);
        geom.geometryChanged();
        return geom;
    }

    /**
	 * Adds the common coordinate bits back into a Geometry. The coordinates of
	 * the Geometry are changed.
	 * 
	 * @param geom
	 *            the Geometry to which to add the common coordinate bits
	 */
    public void addCommonBits(Geometry geom) {
        Translater trans = new Translater(commonCoord);
        geom.apply(trans);
        geom.geometryChanged();
    }

    class CommonCoordinateFilter implements CoordinateFilter {

        private CommonBits commonBitsX = new CommonBits();

        private CommonBits commonBitsY = new CommonBits();

        @Override
        public void filter(Coordinate coord) {
            commonBitsX.add(coord.x);
            commonBitsY.add(coord.y);
        }

        public Coordinate getCommonCoordinate() {
            return new Coordinate(commonBitsX.getCommon(), commonBitsY.getCommon());
        }
    }

    class Translater implements CoordinateFilter {

        Coordinate trans = null;

        public Translater(Coordinate trans) {
            this.trans = trans;
        }

        @Override
        public void filter(Coordinate coord) {
            coord.x += trans.x;
            coord.y += trans.y;
        }
    }
}

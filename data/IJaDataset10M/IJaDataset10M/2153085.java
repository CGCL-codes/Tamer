package jp.go.ipa.jgcl;

import java.util.Enumeration;
import org.magiclight.common.MLUtil;
import org.magiclight.common.VectorNS;

/**
 *  `   v f     _     X g             N   X
 *
 * @version $Revision: 1.3 $, $Date: 2000/04/26 09:39:18 $
 * @author Information-technology Promotion Agency, Japan
 */
class PointOnGeometryList {

    /**
	 *  `   v f     _     X g
	 * @see VectorNS
	 */
    VectorNS<Pnt> list;

    PointOnGeometryList() {
        super();
        list = new VectorNS<Pnt>();
    }

    void addPoint(PointOnCurve2D thePointOnCurve) {
        PointOnCurve2D mate;
        for (Enumeration en = list.elements(); en.hasMoreElements(); ) {
            if (thePointOnCurve.parametricallyIdentical((PointOnCurve2D) en.nextElement())) {
                return;
            }
        }
        list.addElement(thePointOnCurve);
    }

    void addPoint(ParametricCurve2D curve, double parameter) {
        addPoint(new PointOnCurve2D(curve, parameter, MLUtil.DEBUG));
    }

    void addPoint(PointOnCurve3D thePointOnCurve) {
        PointOnCurve3D mate;
        for (Enumeration en = list.elements(); en.hasMoreElements(); ) {
            if (thePointOnCurve.parametricallyIdentical((PointOnCurve3D) en.nextElement())) {
                return;
            }
        }
        list.addElement(thePointOnCurve);
    }

    void addPoint(ParametricCurve3D curve, double parameter) {
        addPoint(new PointOnCurve3D(curve, parameter, MLUtil.DEBUG));
    }

    /**
	 * 2D         _   z             o   B
	 * @return	2D         _   z
	 * @see	PointOnCurve2D
	 */
    PointOnCurve2D[] toPointOnCurve2DArray() {
        PointOnCurve2D[] pocs = new PointOnCurve2D[list.size()];
        list.copyInto(pocs);
        return pocs;
    }

    /**
	 * 3D         _   z             o   B
	 * @return	3D         _   z
	 * @see	PointOnCurve3D
	 */
    PointOnCurve3D[] toPointOnCurve3DArray() {
        PointOnCurve3D[] pocs = new PointOnCurve3D[list.size()];
        list.copyInto(pocs);
        return pocs;
    }
}

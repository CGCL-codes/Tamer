package org.fudaa.dodico.interpolateur;

import java.util.Arrays;
import org.fudaa.ctulu.CtuluLibGeometrie;
import org.fudaa.ctulu.gis.GISPoint;

/**
 * Un classe representant une source pour une interpolation des plus proches voisins.
 *
 * @author deniger
 * @version $Id: InterpolationNearestNeighborSrc.java,v 1.5 2006-10-19 14:12:30 deniger Exp $
 */
public class InterpolationNearestNeighborSrc implements InterpolationNearestNeighborSrcInterface {

    InterpolationSrcInterface ref_;

    public InterpolationNearestNeighborSrc(final InterpolationSrcInterface _ref) {
        ref_ = _ref;
    }

    public int getPtsNb() {
        return ref_.getPtsNb();
    }

    public double getPtX(final int _i) {
        return ref_.getPtX(_i);
    }

    public double getPtY(final int _i) {
        return ref_.getPtY(_i);
    }

    public int getNbValues() {
        return ref_.getNbValues();
    }

    public double getV(final int _value, final int _i) {
        return ref_.getV(_value, _i);
    }

    /**
   * Remplit le tableau <code>_fourIndexToSet</code> ( qui doit avoir une taille d'au moins 4) avec les indices des
   * points les plus proches de (_x,_y) dans les 4 quadrants. Si aucun trouve, l'indice sera mis a -1 L'ordre est le
   * suivant:<br>
   *
   * <pre>
   *      3  | 2
   *     ----|----
   *      0  | 1
   * </pre>
   *
   * @param _x le x demande
   * @param _y le y
   * @param _fourIndexToSet tableau de 4 a remplir.
   * @return false si le tableau a une taille <4
   */
    public boolean getQuadrantIdx(final double _x, final double _y, final int[] _fourIndexToSet) {
        if ((_fourIndexToSet == null) || (_fourIndexToSet.length < 4)) {
            return false;
        }
        Arrays.fill(_fourIndexToSet, -1);
        double d0 = Double.MAX_VALUE;
        double d1 = Double.MAX_VALUE;
        double d2 = Double.MAX_VALUE;
        double d3 = Double.MAX_VALUE;
        double xref, yref;
        double diffx;
        double diffy;
        double dist;
        for (int i = getPtsNb() - 1; i >= 0; i--) {
            xref = getPtX(i);
            yref = getPtY(i);
            diffx = xref - _x;
            diffy = yref - _y;
            dist = CtuluLibGeometrie.getD2(xref, yref, _x, _y);
            if ((diffx <= 0) && (diffy <= 0) && (dist < d0)) {
                d0 = dist;
                _fourIndexToSet[0] = i;
            }
            if ((diffx >= 0) && (diffy <= 0) && (dist < d1)) {
                d1 = dist;
                _fourIndexToSet[1] = i;
            }
            if ((diffx >= 0) && (diffy >= 0) && (dist < d2)) {
                d2 = dist;
                _fourIndexToSet[2] = i;
            }
            if ((diffx <= 0) && (diffy >= 0) && (dist < d3)) {
                d3 = dist;
                _fourIndexToSet[3] = i;
            }
        }
        return true;
    }

    /**
   * @author Fred Deniger
   * @version $Id: InterpolationNearestNeighborSrc.java,v 1.5 2006-10-19 14:12:30 deniger Exp $
   */
    public static class ListOnZ extends InterpolationNearestNeighborSrc {

        /**
     * @param _l liste des points.
     */
        public ListOnZ(final GISPoint[] _l) {
            super(new InterpolationSrcDefault(_l));
        }
    }
}
